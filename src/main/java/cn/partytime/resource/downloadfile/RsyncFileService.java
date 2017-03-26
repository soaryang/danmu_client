package cn.partytime.resource.downloadfile;

import cn.partytime.config.ConfigUtils;
import cn.partytime.json.*;
import cn.partytime.model.*;
import cn.partytime.util.Const;
import cn.partytime.util.HttpUtils;
import cn.partytime.util.ListUtils;
import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by administrator on 2016/12/8.
 */
@Service
@Slf4j
public class RsyncFileService {

    @Autowired
    private Properties properties;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private Configuration configuration;



    public void rsyncFile(){
        String shellString = "rsync -arvIz --delete --password-file="+configUtils.rsyncPasswordFile()+" rsync_user@"+configUtils.getRsyncIp()+"::"+configUtils.rsyncName()+" "+configUtils.cmdRsyncFilePath;
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(shellString);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call shell failed. error code is :" + exitValue);
            }else{
                log.info("file upload success");
            }
        } catch (Exception e) {
            log.error("",e);
        }

    }





    public void createFlashConfig() {
        String jsonStr = HttpUtils.httpRequestStr(configUtils.getInitUrl()+"?addressId="+properties.getAddressId(), "GET", null);
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        //Gson gson = gsonBuilder.create();
        //DownloadFileConfig downloadFileConfig = gson.fromJson(jsonStr, DownloadFileConfig.class);
        DownloadFileConfig downloadFileConfig = JSON.parseObject(jsonStr,DownloadFileConfig.class);
        if (null != downloadFileConfig) {
            List<PartyResourceResult> partyResourceResultList = downloadFileConfig.getData();

            //获取定时弹幕
            List<TimerDanmuPathModel>  timerDanmuPathModels  = findTimerDanmuFile();

            //获取广告弹幕
            AdTimerFileResource adTimerFileResource = findAdTimerDanmu();
            List<TimerDanmuFileModel> timerDanmuFileModelList =adTimerFileResource.getTimerDanmuFileLogicModels();
            Map<String,String> adTimerMap = new HashMap<String,String>();
            if(ListUtils.checkListIsNotNull(timerDanmuFileModelList)){
                for(TimerDanmuFileModel timerDanmuFileModel:timerDanmuFileModelList){
                    String realFilePath = configUtils.realSaveAdtimerFilePath() +timerDanmuFileModel.getPath();
                    File file = new File(realFilePath);
                    if(file.exists()){
                        adTimerMap.put(timerDanmuFileModel.getPartyId(),configUtils.saveFilePath+"/adTimerDanmu"+timerDanmuFileModel.getPath());
                    }
                }
            }


            if (null != partyResourceResultList) {
                Map<String, Object> resourceFileMap = new HashMap<String, Object>();
                List<PartyResourceModel> partyResourceModelList = new ArrayList<>();
                for (PartyResourceResult partyResourceResult : partyResourceResultList) {
                    String partyId = partyResourceResult.getParty().getId();
                    List<ResourceFile> resourceFileList = partyResourceResult.getResourceFileList();
                    if (null != resourceFileList) {
                        List<ResourceFile> bigExpressionList = new ArrayList<>();
                        List<ResourceFile> specialImageList = new ArrayList<>();
                        List<ResourceFile> specialVideoList = new ArrayList<>();

                        for (ResourceFile resourceFile : resourceFileList) {
                            File file = new File(configUtils.rsyncSaveFilePath()+"/upload"+resourceFile.getFileUrl());
                            if( file.exists()){
                                resourceFile.setLocalFilePath(configUtils.saveFilePath+"/upload"+resourceFile.getFileUrl());
                                if (Const.RESOURCE_EXPRESSIONS == resourceFile.getFileType()) {
                                    bigExpressionList.add(resourceFile);
                                } else if (Const.RESOURCE_SPECIAL_IMAGES == resourceFile.getFileType()) {
                                    specialImageList.add(resourceFile);
                                } else if (Const.RESOURCE_SPECIAL_VIDEOS == resourceFile.getFileType()) {
                                    specialVideoList.add(resourceFile);
                                }
                            }
                        }
                        PartyResourceModel partyResourceModel = new PartyResourceModel();
                        partyResourceModel.setParty(partyResourceResult.getParty());
                        partyResourceModel.setBigExpressionList(bigExpressionList);
                        partyResourceModel.setSpecialImageList(specialImageList);
                        partyResourceModel.setSpecialVideoList(specialVideoList);
                        //填充广告弹幕
                        partyResourceModel.setAdTimerDanmuPath(adTimerMap.get(partyId));

                        if( null != timerDanmuPathModels) {
                            for (TimerDanmuPathModel timerDanmuPathModel : timerDanmuPathModels) {
                                if (partyResourceModel.getParty().getId().equals(timerDanmuPathModel.getPartyId())) {
                                    partyResourceModel.setPathList(timerDanmuPathModel.getPathList());
                                }

                            }
                        }
                        partyResourceModelList.add(partyResourceModel);
                    }
                }
                resourceFileMap.put("partyResourceModelList", partyResourceModelList);
                List<ResourceFile> resourceFiles = adTimerFileResource.getResourceFileList();
                List<ResourceFile> adExpressionList = new ArrayList<>();
                List<ResourceFile> specialImageList = new ArrayList<>();
                List<ResourceFile> specialVideoList = new ArrayList<>();

                if(ListUtils.checkListIsNotNull(resourceFiles)){
                    for (ResourceFile resourceFile : resourceFiles) {
                        File file = new File(configUtils.rsyncSaveFilePath()+"/upload"+resourceFile.getFileUrl());
                        if( file.exists()){
                            resourceFile.setLocalFilePath(configUtils.saveFilePath+"/upload"+resourceFile.getFileUrl());
                            if (Const.RESOURCE_EXPRESSIONS == resourceFile.getFileType()) {
                                adExpressionList.add(resourceFile);
                            } else if (Const.RESOURCE_SPECIAL_IMAGES == resourceFile.getFileType()) {
                                specialImageList.add(resourceFile);
                            } else if (Const.RESOURCE_SPECIAL_VIDEOS == resourceFile.getFileType()) {
                                specialVideoList.add(resourceFile);
                            }
                        }
                    }
                }
                resourceFileMap.put("adExpressionList", adExpressionList);

                resourceFileMap.put("adSpecialEffectList", specialImageList);

                resourceFileMap.put("adVideoUrlList", specialVideoList);

                createConfigFile(resourceFileMap);
            }

        }

    }

    /**
     * 根据模板创建配置文件
     *
     * @param model
     * @throws IOException
     */
    private void createConfigFile(Map<String, Object> model) {
        Template template = null; // freeMarker template
        String content = null;

        try {
            template = configuration.getTemplate("resourceConfig.ftl");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(content)) {
            //文件保存位置
            File saveDir = new File(configUtils.programFlashPath());
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(configUtils.programFlashPath() + File.separator + "config");
            //如果配置文件已经存在
            if(file.exists()){
                //GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
                //Gson gson = gsonbuilder.create();
                BufferedReader in = null;
                StringBuffer buffer = new StringBuffer();
                String line = "";
                try {
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while ((line = in.readLine()) != null){
                        buffer.append(line);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //ConfigJson configJson = gson.fromJson(buffer.toString(),ConfigJson.class);
                ConfigJson configJson =  JSON.parseObject(buffer.toString(),ConfigJson.class);
                Object object = model.get("partyResourceModelList");
                Object objectAdExpression = model.get("adExpressionList");
                Object objectAdSpecialEffects = model.get("adSpecialEffectList");
                Object objectAdVideoUrl = model.get("adVideoUrlList");

                if(objectAdExpression!=null){
                    List<ResourceFile> expressionList = (List<ResourceFile>)objectAdExpression;
                    if(ListUtils.checkListIsNotNull(expressionList)){
                        List<Resource> resourceList = new ArrayList<>();
                        for(ResourceFile resourceFile : expressionList){
                            Resource resource = new Resource();
                            resource.setId(resourceFile.getId());
                            resource.setUrl(resourceFile.getLocalFilePath());
                            resourceList.add(resource);
                        }
                        configJson.setAdExpressions(resourceList);
                    }else{
                        configJson.setAdExpressions(new ArrayList<>());
                    }
                }else{
                    configJson.setAdExpressions(new ArrayList<>());
                }

                if(objectAdSpecialEffects!=null){
                    List<ResourceFile> specialEffectsList = (List<ResourceFile>)objectAdSpecialEffects;
                    if(ListUtils.checkListIsNotNull(specialEffectsList)){
                        List<Resource> resourceList = new ArrayList<>();
                        for(ResourceFile resourceFile : specialEffectsList){
                            Resource resource = new Resource();
                            resource.setId(resourceFile.getId());
                            resource.setUrl(resourceFile.getLocalFilePath());
                            resourceList.add(resource);
                        }
                        configJson.setAdSpecialEffects(resourceList);
                    }else{
                        configJson.setAdSpecialEffects(new ArrayList<>());
                    }
                }else{
                    configJson.setAdSpecialEffects(new ArrayList<>());
                }
                if(objectAdVideoUrl!=null){
                    List<ResourceFile> specialEffectsList = (List<ResourceFile>)objectAdVideoUrl;
                    List<VideoResourceJson> resourceList = new ArrayList<>();
                    if(ListUtils.checkListIsNotNull(specialEffectsList)){
                        for(ResourceFile resourceFile : specialEffectsList){
                            VideoResourceJson videoResourceJson = new VideoResourceJson();
                            videoResourceJson.setId(resourceFile.getId());
                            videoResourceJson.setUrl(resourceFile.getLocalFilePath());
                            videoResourceJson.setType("click");
                            resourceList.add(videoResourceJson);
                        }
                        configJson.setAdVideoUrl(resourceList);
                    }else{
                        configJson.setAdVideoUrl(new ArrayList<>());
                    }
                }else{
                    configJson.setAdVideoUrl(new ArrayList<>());
                }


                if( null != object){
                    List<PartyResourceModel> partyResourceModelList = (List<PartyResourceModel>)object;
                    List<PartyJson> partyJsonList = new ArrayList<>();
                    for(PartyResourceModel partyResourceModel : partyResourceModelList){
                        PartyJson partyJson = new PartyJson();
                        partyJson.setName(partyResourceModel.getParty().getName());
                        partyJson.setPartyId(partyResourceModel.getParty().getId());
                        partyJson.setMovieAlias(partyResourceModel.getParty().getMovieAlias());
                        List<ResourceFile> expressionList = partyResourceModel.getBigExpressionList();

                        if( null != expressionList && expressionList.size() > 0){
                            List<Resource> resourceList = new ArrayList<>();
                            for(ResourceFile resourceFile : expressionList){
                                Resource resource = new Resource();
                                resource.setId(resourceFile.getId());
                                resource.setUrl(resourceFile.getLocalFilePath());
                                resourceList.add(resource);
                            }
                            partyJson.setExpressions(resourceList);
                        }else{
                            partyJson.setExpressions(new ArrayList<>());
                        }

                        List<ResourceFile> specialImageList= partyResourceModel.getSpecialImageList();
                        if( null != specialImageList && specialImageList.size() > 0){
                            List<Resource> resourceList = new ArrayList<>();
                            for(ResourceFile resourceFile : specialImageList){
                                Resource resource = new Resource();
                                resource.setId(resourceFile.getId());
                                resource.setUrl(resourceFile.getLocalFilePath());
                                resourceList.add(resource);
                            }
                            partyJson.setSpecialEffects(resourceList);
                        }else{
                            partyJson.setSpecialEffects(new ArrayList<>());
                        }

                        List<ResourceFile> specialVideoList = partyResourceModel.getSpecialVideoList();

                        if(partyResourceModel.getAdTimerDanmuPath()!=null){
                            partyJson.setAdTimerDanmuUrl(partyResourceModel.getAdTimerDanmuPath());
                        }


                        if( null != specialVideoList && specialVideoList.size() > 0){
                            List<VideoResourceJson> resourceList = new ArrayList<>();
                            for(ResourceFile resourceFile : specialVideoList){
                                VideoResourceJson videoResourceJson = new VideoResourceJson();
                                videoResourceJson.setId(resourceFile.getId());
                                videoResourceJson.setUrl(resourceFile.getLocalFilePath());
                                videoResourceJson.setType("click");
                                resourceList.add(videoResourceJson);
                            }
                            partyJson.setLocalVideoUrl(resourceList);

                        }else{
                            partyJson.setLocalVideoUrl(new ArrayList<>());
                        }

                        List<String> pathList = partyResourceModel.getPathList();

                        if( null != pathList && pathList.size() > 0){
                            List<Resource> resourceList = new ArrayList<>();
                            for(String url : pathList){
                                Resource resource = new Resource();
                                resource.setUrl(url);
                                resourceList.add(resource);
                            }
                            partyJson.setTimerDanmuUrl(resourceList);

                        }else{
                            partyJson.setTimerDanmuUrl(new ArrayList<>());
                        }
                        partyJsonList.add(partyJson);

                    }
                    configJson.setPartys(partyJsonList);
                }


                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    //fos.write(gson.toJson(configJson).getBytes());
                    fos.write(JSON.toJSONString(configJson).getBytes());
                }catch (FileNotFoundException e) {
                    log.error("", e);
                } catch (IOException e) {
                    log.error("", e);
                }
            }else{
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(content.getBytes());
                } catch (FileNotFoundException e) {
                    log.error("", e);
                } catch (IOException e) {
                    log.error("", e);
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            }

        }
    }

    private TimerDanmuFileConfig initTimerDanmuFileConfig(){
        String jsonStr = HttpUtils.httpRequestStr(configUtils.getTimerDanmuNetUrl()+"?addressId="+properties.getAddressId(), "GET", null);
        //List<TimerDanmuFileConfig> timerDanmuFileConfigList = JSON.parseArray(jsonStr,TimerDanmuFileConfig.class);
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        //Gson gson = gsonBuilder.create();
        TimerDanmuFileConfig timerDanmuFileConfig = JSON.parseObject(jsonStr, TimerDanmuFileConfig.class);
        return timerDanmuFileConfig;
    }


    /**
     * 获取广告弹幕
     * @return
     */
    public AdTimerFileResource findAdTimerDanmu () {
        String jsonStr = HttpUtils.httpRequestStr(configUtils.getAdTimerDanmuNetUrl()+"?addressId="+properties.getAddressId(), "GET", null);
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        //Gson gson = gsonBuilder.create();
        AdTimerFileConfig adTimerFileConfig = JSON.parseObject(jsonStr, AdTimerFileConfig.class);
        AdTimerFileResource adTimerFileResource = adTimerFileConfig.getData();
        return adTimerFileResource;

        /*List<TimerDanmuFileModel> timerDanmuFileModelList =adTimerFileResource.getTimerDanmuFileLogicModels();
        if(ListUtils.checkListIsNotNull(timerDanmuFileModelList)){
            for(TimerDanmuFileModel timerDanmuFileModel:timerDanmuFileModelList){
                String realFilePath = realAdTimerFileSavePath +timerDanmuFileModel.getPath();
                File file = new File(realFilePath);
                if(file.exists()){
                    adTimerMap.put(timerDanmuFileModel.getPartyId(),saveFilePath+"/adTimerDanmu"+timerDanmuFileModel.getPath());
                }
            }
        }
        return  adTimerMap;*/
    }

    public List<TimerDanmuPathModel> findTimerDanmuFile () {
        log.info("exectue download timerdanmu command:");
        TimerDanmuFileConfig timerDanmuFileConfig = initTimerDanmuFileConfig();
        if( null == timerDanmuFileConfig){
            return null;
        }
        List<TimerDanmuFileModel> timerDanmuFileConfigList = timerDanmuFileConfig.getData();
        Map<String,List<TimerDanmuFileModel>> map = new HashMap<String,List<TimerDanmuFileModel>>();

        List<TimerDanmuPathModel> timerDanmuPathModels = new ArrayList<>();
        if (ListUtils.checkListIsNotNull(timerDanmuFileConfigList)) {
            //timerDanmuPathMap = new HashMap<String,List<String>>();
            for (TimerDanmuFileModel timerDanmuFileModel : timerDanmuFileConfigList) {
                //String fileName = getFileName(timerDanmuFileModel.getPath());
                String partyId = timerDanmuFileModel.getPartyId();
                if (map.containsKey(partyId)) {
                    List<TimerDanmuFileModel> timerDanmuFileModels = map.get(partyId);
                    timerDanmuFileModels.add(timerDanmuFileModel);
                    map.put(partyId, timerDanmuFileModels);
                } else {
                    List<TimerDanmuFileModel> timerDanmuFileModels = new ArrayList<TimerDanmuFileModel>();
                    timerDanmuFileModels.add(timerDanmuFileModel);
                    map.put(partyId, timerDanmuFileModels);
                }
            }
            for (Map.Entry<String, List<TimerDanmuFileModel>> entry : map.entrySet()) {
                TimerDanmuPathModel timerDanmuPathModel = new TimerDanmuPathModel();
                List<TimerDanmuFileModel> pathList = entry.getValue();
                String key = entry.getKey();
                List<String> nameList = new ArrayList<String>();
                for (TimerDanmuFileModel timerDanmuFileModel : pathList) {
                    String path = timerDanmuFileModel.getPath();
                    String realFilePath = configUtils.realSaveTimerFilePath() +path;
                    File file = new File(realFilePath);
                    if(file.exists()){
                        nameList.add(configUtils.saveFilePath+"/timerDanmu"+path);
                    }
                }
                timerDanmuPathModel.setPartyId(key);
                timerDanmuPathModel.setPathList(nameList);
                timerDanmuPathModels.add(timerDanmuPathModel);

            }
            log.info(JSON.toJSONString(timerDanmuPathModels));
            return timerDanmuPathModels;

        }
        return null;
    }



    public void downloadClient(){
        String shellString = "rsync -arvIz --delete --password-file="+configUtils.rsyncPasswordFile()+" rsync_user@"+configUtils.getRsyncIp()+"::"+configUtils.rsyncClientName()+" "+configUtils.programPath();
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(shellString);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call shell failed. error code is :" + exitValue);
            }else{
                log.info("client upload success");
            }
        } catch (Exception e) {
            log.error("",e);
        }

    }


    public void downloadExecuteShell(){
        String shellString = "rsync -arvIz --delete --password-file="+configUtils.rsyncPasswordFile()+" rsync_user@"+configUtils.getRsyncIp()+"::"+configUtils.getExecuteScriptName()+" "+configUtils.getEecuteScriptPath();
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(shellString);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call shell failed. error code is :" + exitValue);
            }else{
                log.info("client upload success");
            }
        } catch (Exception e) {
            log.error("",e);
        }

    }



}
