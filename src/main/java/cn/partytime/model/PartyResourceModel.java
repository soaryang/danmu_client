package cn.partytime.model;

import java.util.List;

/**
 * Created by liuwei on 2016/9/13.
 */
public class PartyResourceModel {

    private Party party;

    private List<ResourceFile> bigExpressionList;

    private List<ResourceFile> specialImageList;

    private List<ResourceFile> specialVideoList;

    private List<String> pathList;

    private String adTimerDanmuPath;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<ResourceFile> getBigExpressionList() {
        return bigExpressionList;
    }

    public void setBigExpressionList(List<ResourceFile> bigExpressionList) {
        this.bigExpressionList = bigExpressionList;
    }

    public List<ResourceFile> getSpecialImageList() {
        return specialImageList;
    }

    public void setSpecialImageList(List<ResourceFile> specialImageList) {
        this.specialImageList = specialImageList;
    }

    public List<ResourceFile> getSpecialVideoList() {
        return specialVideoList;
    }

    public void setSpecialVideoList(List<ResourceFile> specialVideoList) {
        this.specialVideoList = specialVideoList;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public String getAdTimerDanmuPath() {
        return adTimerDanmuPath;
    }

    public void setAdTimerDanmuPath(String adTimerDanmuPath) {
        this.adTimerDanmuPath = adTimerDanmuPath;
    }
}
