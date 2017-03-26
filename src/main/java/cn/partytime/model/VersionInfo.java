package cn.partytime.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yang on 2017/2/17.
 */
public class VersionInfo implements Serializable {

    private String id;

    private String version;

    //类型 0 java  1 flash
    private int type;

    private long updateDate;

    private List<UpdatePlanMachine> updatePlanMachineList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public List<UpdatePlanMachine> getUpdatePlanMachineList() {
        return updatePlanMachineList;
    }

    public void setUpdatePlanMachineList(List<UpdatePlanMachine> updatePlanMachineList) {
        this.updatePlanMachineList = updatePlanMachineList;
    }
}
