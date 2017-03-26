package cn.partytime.model;

import java.util.Date;

/**
 * Created by liuwei on 2016/8/26.
 */
public class DownloadPartyModel {

    private String partyId;

    private Date lastDownloadTime;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Date getLastDownloadTime() {
        return lastDownloadTime;
    }

    public void setLastDownloadTime(Date lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }
}
