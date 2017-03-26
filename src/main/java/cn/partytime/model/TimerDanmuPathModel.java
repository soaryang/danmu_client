package cn.partytime.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lENOVO on 2016/12/1.
 */
public class TimerDanmuPathModel implements Serializable {

    private String partyId;

    private List<String> pathList;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
