package cn.partytime.model;

import java.util.List;

/**
 * Created by lENOVO on 2016/11/28.
 */
public class TimerDanmuFileConfig {


    private Integer result;

    private List<TimerDanmuFileModel> data;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<TimerDanmuFileModel> getData() {
        return data;
    }

    public void setData(List<TimerDanmuFileModel> data) {
        this.data = data;
    }
}
