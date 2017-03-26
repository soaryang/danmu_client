package cn.partytime.json;

import java.util.List;

/**
 * Created by administrator on 2016/12/26.
 */
public class ConfigJson {
    private String show = "r";
    private Double expressionScale = 0.3;
    private Boolean dsFaceIn = true;
    private Integer maxSize=60;
    private Integer screen=100;
    private Double speed1=0.7;
    private String bgColor="0x000000";
    private Boolean bgVisible=true;
    private Boolean testServer=false;
    private Integer offY=0;
    private Boolean textOrder=false;
    private Boolean topShow=true;
    private Boolean localVideo=true;
    private Boolean addBgJpg=false;
    private String bgJpgUrl="localResoource/background/bg.jpg";
    private Integer setZ=0;
    private Integer addRectWidth=0;
    private Integer addRectHeight=0;
    private Integer valueAbs=10;
    private Boolean camera=false;
    private Boolean livePlayer=false;
    private Object liveServer;
    private Object liveStream;
    private String toPublicVideo="1.flv";

    private Number flightY = 0;
    private Number flightScale = 1;

    private List<PartyJson> partys;

    private  List<Resource> adExpressions;

    private  List<Resource> adSpecialEffects;

    private  List<VideoResourceJson> adVideoUrl;


    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Double getExpressionScale() {
        return expressionScale;
    }

    public void setExpressionScale(Double expressionScale) {
        this.expressionScale = expressionScale;
    }

    public Boolean getDsFaceIn() {
        return dsFaceIn;
    }

    public void setDsFaceIn(Boolean dsFaceIn) {
        this.dsFaceIn = dsFaceIn;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Double getSpeed1() {
        return speed1;
    }

    public void setSpeed1(Double speed1) {
        this.speed1 = speed1;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public Boolean getBgVisible() {
        return bgVisible;
    }

    public void setBgVisible(Boolean bgVisible) {
        this.bgVisible = bgVisible;
    }

    public Boolean getTestServer() {
        return testServer;
    }

    public void setTestServer(Boolean testServer) {
        this.testServer = testServer;
    }

    public Integer getOffY() {
        return offY;
    }

    public void setOffY(Integer offY) {
        this.offY = offY;
    }

    public Boolean getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(Boolean textOrder) {
        this.textOrder = textOrder;
    }

    public Boolean getTopShow() {
        return topShow;
    }

    public void setTopShow(Boolean topShow) {
        this.topShow = topShow;
    }

    public Boolean getLocalVideo() {
        return localVideo;
    }

    public void setLocalVideo(Boolean localVideo) {
        this.localVideo = localVideo;
    }

    public Boolean getAddBgJpg() {
        return addBgJpg;
    }

    public void setAddBgJpg(Boolean addBgJpg) {
        this.addBgJpg = addBgJpg;
    }

    public String getBgJpgUrl() {
        return bgJpgUrl;
    }

    public void setBgJpgUrl(String bgJpgUrl) {
        this.bgJpgUrl = bgJpgUrl;
    }

    public Integer getSetZ() {
        return setZ;
    }

    public void setSetZ(Integer setZ) {
        this.setZ = setZ;
    }

    public Integer getAddRectWidth() {
        return addRectWidth;
    }

    public void setAddRectWidth(Integer addRectWidth) {
        this.addRectWidth = addRectWidth;
    }

    public Integer getAddRectHeight() {
        return addRectHeight;
    }

    public void setAddRectHeight(Integer addRectHeight) {
        this.addRectHeight = addRectHeight;
    }

    public Integer getValueAbs() {
        return valueAbs;
    }

    public void setValueAbs(Integer valueAbs) {
        this.valueAbs = valueAbs;
    }

    public Boolean getCamera() {
        return camera;
    }

    public void setCamera(Boolean camera) {
        this.camera = camera;
    }

    public Boolean getLivePlayer() {
        return livePlayer;
    }

    public void setLivePlayer(Boolean livePlayer) {
        this.livePlayer = livePlayer;
    }

    public Object getLiveServer() {
        return liveServer;
    }

    public void setLiveServer(Object liveServer) {
        this.liveServer = liveServer;
    }

    public Object getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(Object liveStream) {
        this.liveStream = liveStream;
    }

    public void setLiveStream(String liveStream) {
        this.liveStream = liveStream;
    }

    public List<PartyJson> getPartys() {
        return partys;
    }

    public void setPartys(List<PartyJson> partys) {
        this.partys = partys;
    }


    public List<Resource> getAdSpecialEffects() {
        return adSpecialEffects;
    }

    public void setAdSpecialEffects(List<Resource> adSpecialEffects) {
        this.adSpecialEffects = adSpecialEffects;
    }

    public List<VideoResourceJson> getAdVideoUrl() {
        return adVideoUrl;
    }

    public void setAdVideoUrl(List<VideoResourceJson> adVideoUrl) {
        this.adVideoUrl = adVideoUrl;
    }

    public String getToPublicVideo() {
        return toPublicVideo;
    }

    public void setToPublicVideo(String toPublicVideo) {
        this.toPublicVideo = toPublicVideo;
    }

    public List<Resource> getAdExpressions() {
        return adExpressions;
    }

    public void setAdExpressions(List<Resource> adExpressions) {
        this.adExpressions = adExpressions;
    }

    public Number getFlightY() {
        return flightY;
    }

    public void setFlightY(Number flightY) {
        this.flightY = flightY;
    }

    public Number getFlightScale() {
        return flightScale;
    }

    public void setFlightScale(Number flightScale) {
        this.flightScale = flightScale;
    }
}
