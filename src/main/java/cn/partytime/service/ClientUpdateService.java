package cn.partytime.service;


import cn.partytime.config.ConfigUtils;
import cn.partytime.json.RestResult;
import cn.partytime.model.*;

import cn.partytime.model.Properties;
import cn.partytime.util.DateUtils;
import cn.partytime.util.FileUtils;
import cn.partytime.util.HttpUtils;
import cn.partytime.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created by yang on 2017/2/17.
 */
@Service
@Slf4j
public class ClientUpdateService {

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private Properties properties;


    public void createSchtasks(VersionInfo versionInfo){
        //判断是否获取新的定时任务；如果本地有定时任务未执行就不拉新的定时任务=
        String  machineNum = configUtils.getMachineNum();
        List<UpdatePlanMachine> updatePlanMachineList =  versionInfo.getUpdatePlanMachineList();
        if(ListUtils.checkListIsNull(updatePlanMachineList)){
            //删除过时的任务
            deleteFinishUpdatePlan();

            if(0==versionInfo.getType()){
                String  dateStr = DateUtils.transferLongToDate("yyyy/MM/dd",versionInfo.getUpdateDate());
                String hourStr = DateUtils.transferLongToDate("HH:mm:00",versionInfo.getUpdateDate());
                String time = DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",versionInfo.getUpdateDate());
                boolean createNewUpateFlg = setNewUpdatePlan(0);
                if(createNewUpateFlg){
                    return;
                }
                boolean flg = setRequestResult(versionInfo,machineNum,time,"none",0);
                if(flg){
                    deleteTask("JavaUpdate");
                    execShell("schtasks /create  /tn JavaUpdate /tr "+configUtils.getTimerJaveUpdateVbsPath()+" /sc ONCE  /ST "+hourStr+" /SD "+dateStr);
                }

                //String result =HttpUtils.httpRequestStr(configUtils.getUpdateVersionResultCommitNetUrl()+"?id="+versionInfo.getId()+"&result="+status+"&machineNum="+machineNum+"&type="+versionInfo.getType(), "GET", null);


            }else{
                String  dateStr = DateUtils.transferLongToDate("yyyy/MM/dd",versionInfo.getUpdateDate());
                String hourStr = DateUtils.transferLongToDate("HH:mm:ss",versionInfo.getUpdateDate());
                String time = DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",versionInfo.getUpdateDate());
                boolean createNewUpateFlg = setNewUpdatePlan(1);
                if(createNewUpateFlg){
                    return;
                }
                //HttpUtils.httpRequestStr(configUtils.getUpdateVersionResultCommitNetUrl()+"?id="+versionInfo.getId()+"&result="+status+"&machineNum="+machineNum+"&type="+versionInfo.getType(), "GET", null);
                boolean flg = setRequestResult(versionInfo,machineNum,time,"none",0);
                if(flg){
                    deleteTask("FlashUpdate");
                    execShell("schtasks /create  /tn FlashUpdate /tr "+configUtils.getTimerFlashUpdateVbsPath()+" /sc ONCE  /ST "+hourStr+" /SD "+dateStr);
                }

            }
        }
    }

    public boolean setNewUpdatePlan(int type){
        String path = "";
        if(type==0){
            path = configUtils.getJavaVersionDirectory();
        }else{
            path = configUtils.getFlashVersionDirectory();
        }
        List<ClientVersion> versionList= findVersionList(path);
        if(ListUtils.checkListIsNotNull(versionList)){
            for(ClientVersion clientVersion:versionList){
                if(!"success".equals(clientVersion.getStatus())){
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteFinishUpdatePlan(){
        String javaVersionPath = configUtils.getJavaVersionDirectory();
        String flashVersionPath = configUtils.getFlashVersionDirectory();
        List<ClientVersion> javaVersionList= findVersionList(javaVersionPath);
        List<ClientVersion> flashVersionList= findVersionList(flashVersionPath);
        delteTaskInfo(javaVersionList,javaVersionPath,0);
        delteTaskInfo(flashVersionList,flashVersionPath,0);
    }

    public void delteTaskInfo(List<ClientVersion> clientVersionList, String path, int type){
        if(ListUtils.checkListIsNotNull(clientVersionList)){
            for(ClientVersion clientVersion:clientVersionList){
                if("success".equals(clientVersion.getStatus()) || "rollback".equals(clientVersion.getStatus())){
                    String time = DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",clientVersion.getUpdateDate());
                    /*if(type==0){
                        deleteTask("JavaUpdate_"+time);
                    }else{
                        deleteTask("FlashUpdate_"+time);
                    }*/
                    delteFile(path+File.separator+time);

                }
            }
        }
    }

    /**
     * 删除定时任务
     * @param taskName
     */
    private void deleteTask(String taskName){
        try {
            execShell("schtasks /delete /tn "+taskName+" /F");
        }catch (Exception e){
            log.info("====>"+e.getMessage());
        }
    }

    /**
     * 删除计划任务
     * @param path
     */
    private void delteFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    public boolean setRequestResult(VersionInfo versionInfo, String machineNum, String time, String status, int code){
        try {
            //Gson gson = new Gson();
            //String result = HttpUtils.httpRequestStr(configUtils.getUpdateVersionResultCommitNetUrl()+"?id="+versionInfo.getId()+"&result="+status+"&machineNum="+machineNum+"&type="+versionInfo.getType(), "GET", null);
            ClientVersion clientVersion = new ClientVersion();
            clientVersion.setId(versionInfo.getId());
            clientVersion.setVersion(versionInfo.getVersion());
            clientVersion.setMachineNum(machineNum);
            clientVersion.setUpdateDate(versionInfo.getUpdateDate());
            clientVersion.setStatus(status);
            clientVersion.setCode(code);
            /*if(StringUtils.isEmpty(result)){
                clientVersion.setCode(0);
            }else{
                clientVersion.setCode(1);
            }*/
            clientVersion.setDomainName(configUtils.getDomain());
            String str = JSON.toJSONString(clientVersion);

            int type = versionInfo.getType();
            String filePath = "";
            if(type==0){
                filePath = configUtils.getJavaVersionDirectory()+ File.separator+time;
            }else{
                filePath = configUtils.getFlashVersionDirectory()+ File.separator+time;
            }
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
                FileUtils.outputFile(filePath,str);
                return true;
            }else{
                FileUtils.outputFile(filePath,str);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void sendErrorRequest(String status,int type){
        String machineNum = configUtils.getMachineNum();
        VersionConfig versionConfig = findVersionConfig();
        if(versionConfig!=null){
            List<VersionInfo> versionInfoList = findVersionConfig().getData();
            if(ListUtils.checkListIsNotNull(versionInfoList)){
                for(VersionInfo versionInfo:versionInfoList){
                    checkIsSendStatusRequest(versionInfo,machineNum,status,type);
                }
            }
        }
    }
    private void checkIsSendStatusRequest(VersionInfo versionInfo, String machineNum, String status, int type){
        if(versionInfo.getType()==type){
            List<UpdatePlanMachine> updatePlanMachineList =  versionInfo.getUpdatePlanMachineList();
            if(ListUtils.checkListIsNotNull(updatePlanMachineList)){
                for(UpdatePlanMachine updatePlanMachine:updatePlanMachineList){
                    // //更新状态 0 未更新  1更新成功  2更新失败  3开始更新
                    if(machineNum.equals(updatePlanMachine.getMachineNum()) && updatePlanMachine.getStatus()==3){
                        HttpUtils.httpRequestStr(configUtils.getUpdateVersionResultCommitNetUrl()+"?id="+versionInfo.getId()+"&result="+status+"&machineNum="+machineNum+"&type="+versionInfo.getType(), "GET", null);
                    }
                }
            }
        }
    }


    public VersionConfig findVersionConfig(){
        String jsonStr = HttpUtils.httpRequestStr(configUtils.getUpdateVersionUrl()+"?addressId="+properties.getAddressId(), "GET", null);
        return strToVersionConfig(jsonStr);
    }

    public static VersionConfig strToVersionConfig(String jsonStr){

        VersionConfig versionConfig = JSON.parseObject(jsonStr, VersionConfig.class);
        return versionConfig;
    }


    private String execShell(String shellString){
        StringBuffer sb = new StringBuffer();
        try {
            log.info(shellString);
            final Process process = Runtime.getRuntime().exec(shellString);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String line = "";
                    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    try {
                        while ((line = input.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(input!=null){
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call shell failed. error code is :" + exitValue);
            } else {
                log.info("shell exec success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /*private String execShell(String shellString) {
        log.info(shellString);
        Process process = null;
        StringBuffer sb = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(shellString);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call shell failed. error code is :" + exitValue);
            } else {
                log.info("shell exec success");
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return sb.toString();
    }*/

    public static List<String> findFileList(File f){
        List<String> fileList = new ArrayList<>();
        if(f!=null){
            if(f.isDirectory()){
                File[] fileArray=f.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {
                        fileList.add(fileArray[i].getName());
                    }
                }
            }
        }
        return fileList;
    }

    public boolean checkVersionIsFitForUpdate(int type,String dateStr){
        String path ="";
        if(type==0){
            path = configUtils.getJavaVersionDirectory();
        }else{
            path = configUtils.getFlashVersionDirectory();
        }
        String updatePlanFile = path+File.separator+dateStr;
        File file = new File(updatePlanFile);
        if(!file.exists()){
            log.info("file is not exist");
            return false;
        }

        List<String> fileList = findFileList(new File(path));
        List<ClientVersion> versionList = new ArrayList<>();
        Date date  = DateUtils.strToDate(dateStr,"yyyy-MM-dd-HH-mm-00");
        versionList = findVersionList(path);
        versionList = versionListSortByUpdateDate(versionList);
        if(ListUtils.checkListIsNotNull(versionList)){
            for(ClientVersion clientVersion:versionList){
                if(clientVersion.getUpdateDate()<date.getTime()){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public List<ClientVersion> findVersionList(String path){

        List<String> fileList = findFileList(new File(path));
        List<ClientVersion> versionList = new ArrayList<>();
        if(ListUtils.checkListIsNotNull(fileList)) {
            for (String str : fileList) {
                String versionStr = FileUtils.txt2String(path+File.separator+ str);
                if(!StringUtils.isEmpty(versionStr)){
                    ClientVersion clientVersion = JSON.parseObject(versionStr, ClientVersion.class);
                    versionList.add(clientVersion);
                }
            }
        }
        return versionList;
    }


    public void repeatRequest(){
        String javaPath = configUtils.getJavaVersionDirectory();
        String flashPath = configUtils.getFlashVersionDirectory();
        sendReqeust(javaPath,0);
        sendReqeust(flashPath,1);
    }

    private void sendReqeust(String path,int type){
        List<String> fileList = findFileList(new File(path));
        Map<String,ClientVersion> stringClientVersionMap = new HashMap<String,ClientVersion>();

        if(ListUtils.checkListIsNotNull(fileList)){
            for(String str:fileList){
                String versionStr = FileUtils.txt2String(path+File.separator+ str);
                if(!StringUtils.isEmpty(versionStr)){
                    ClientVersion clientVersion = JSON.parseObject(versionStr, ClientVersion.class);
                    if(!"none".equals(clientVersion.getStatus()) && clientVersion.getCode()==0){
                        String result = HttpUtils.httpRequestStr(configUtils.getUpdateVersionResultCommitNetUrl()+"?id="+clientVersion.getId()+"&result="+clientVersion.getStatus()+"&machineNum="+clientVersion.getMachineNum()+"&type="+type, "GET", null);
                        if(!StringUtils.isEmpty(result)){
                            RestResult restResult = JSON.parseObject(result, RestResult.class);
                            if(restResult.getResult()==200){
                                VersionInfo versionInfo = new VersionInfo();
                                versionInfo.setId(clientVersion.getId());
                                versionInfo.setType(type);
                                versionInfo.setUpdateDate(clientVersion.getUpdateDate());
                                versionInfo.setVersion(clientVersion.getVersion());

                                setRequestResult(versionInfo,clientVersion.getMachineNum(),str,clientVersion.getStatus(),1);
                            }
                        }
                    }
                }
            }
        }
    }

    public String findUpdateVersionFile(int type) {
        String path ="";
        if(type==0){
            path = configUtils.getJavaVersionDirectory();
        }else{
            path = configUtils.getFlashVersionDirectory();
        }
        List<String> fileList = findFileList(new File(path));
        Map<String,ClientVersion>stringClientVersionMap = new HashMap<String,ClientVersion>();

        List<ClientVersion> errorVersionList = new ArrayList<>();
        List<ClientVersion> startVersionList = new ArrayList<>();
        List<ClientVersion> noStartVersionList = new ArrayList<>();
        if(ListUtils.checkListIsNotNull(fileList)){
            for(String str:fileList){
                String versionStr = FileUtils.txt2String(path+File.separator+ str);
                if(!StringUtils.isEmpty(versionStr)){
                    ClientVersion clientVersion = JSON.parseObject(versionStr, ClientVersion.class);
                    if("start".equals(clientVersion.getStatus())){
                        startVersionList.add(clientVersion);
                    }else if("error".equals(clientVersion.getStatus())){
                        errorVersionList.add(clientVersion);
                    }else if("none".equals(clientVersion.getStatus())){
                        noStartVersionList.add(clientVersion);
                    }
                }
            }
        }
        errorVersionList = versionListSortByUpdateDate(errorVersionList);

        if(ListUtils.checkListIsNotNull(errorVersionList)){
            long updateDdate = errorVersionList.get(0).getUpdateDate();
            if(updateDdate==0){
                return "";
            }else{
                return DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",updateDdate);
            }

        }

        //再更新未更新的
        startVersionList= versionListSortByUpdateDate(startVersionList);
        if(ListUtils.checkListIsNotNull(startVersionList)){
            long updateDdate = startVersionList.get(0).getUpdateDate();
            if(updateDdate==0){
                return "";
            }else{
                return DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",updateDdate);
            }
        }

        noStartVersionList = versionListSortByUpdateDate(noStartVersionList);
        if(ListUtils.checkListIsNotNull(noStartVersionList)){
            long updateDdate = noStartVersionList.get(0).getUpdateDate();
            if(updateDdate==0){
                return "";
            }else{
                return DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",updateDdate);
            }
        }
        return "";
    }

    public List<ClientVersion> versionListSortByUpdateDate(List<ClientVersion> versionList){
        if(ListUtils.checkListIsNull(versionList)){
            return  new ArrayList<ClientVersion>();
        }
        Collections.sort(versionList, new Comparator<ClientVersion>() {
            public int compare(ClientVersion arg0, ClientVersion arg1) {
                long hits0 = arg0.getUpdateDate();
                long hits1 = arg1.getUpdateDate();
                if (hits1 < hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return versionList;
    }

    public String findVersionFileNameByVersion(int type,String version){
        String path = "";
        if(type==0){
            path = configUtils.getJavaVersionDirectory();
        }else{
            path = configUtils.getFlashVersionDirectory();
        }
        List<ClientVersion> versionList= findVersionList(path);
        if(ListUtils.checkListIsNotNull(versionList)){
            for(ClientVersion clientVersion:versionList){
                if(version.equals(clientVersion.getVersion())){
                    return DateUtils.transferLongToDate("yyyy-MM-dd-HH-mm-00",clientVersion.getUpdateDate());
                }
            }
        }
        return "";
    }
}
