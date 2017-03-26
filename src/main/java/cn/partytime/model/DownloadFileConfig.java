package cn.partytime.model;

import cn.partytime.json.PartyResourceResult;

import java.util.List;

/**
 * Created by liuwei on 2016/8/18.
 */
public class DownloadFileConfig {

    private Integer result;

    private List<PartyResourceResult> data;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<PartyResourceResult> getData() {
        return data;
    }

    public void setData(List<PartyResourceResult> data) {
        this.data = data;
    }


}
