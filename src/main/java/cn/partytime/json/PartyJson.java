package cn.partytime.json;

import java.util.List;

/**
 * Created by administrator on 2016/12/26.
 */
public class PartyJson {

    private String partyId;

    private String name;

    private String movieAlias;

    private List<Resource> expressions;

    private List<Resource> specialEffects;

    private List<VideoResourceJson> localVideoUrl;

    private List<Resource> timerDanmuUrl;

    private String adTimerDanmuUrl;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Resource> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Resource> expressions) {
        this.expressions = expressions;
    }

    public List<Resource> getSpecialEffects() {
        return specialEffects;
    }

    public void setSpecialEffects(List<Resource> specialEffects) {
        this.specialEffects = specialEffects;
    }

    public List<VideoResourceJson> getLocalVideoUrl() {
        return localVideoUrl;
    }

    public void setLocalVideoUrl(List<VideoResourceJson> localVideoUrl) {
        this.localVideoUrl = localVideoUrl;
    }

    public List<Resource> getTimerDanmuUrl() {
        return timerDanmuUrl;
    }

    public void setTimerDanmuUrl(List<Resource> timerDanmuUrl) {
        this.timerDanmuUrl = timerDanmuUrl;
    }

    public String getMovieAlias() {
        return movieAlias;
    }

    public void setMovieAlias(String movieAlias) {
        this.movieAlias = movieAlias;
    }

    public String getAdTimerDanmuUrl() {
        return adTimerDanmuUrl;
    }

    public void setAdTimerDanmuUrl(String adTimerDanmuUrl) {
        this.adTimerDanmuUrl = adTimerDanmuUrl;
    }
}
