package cn.partytime.model;

/**
 * Created by administrator on 2017/2/20.
 */
public class UpdatePlanMachine {

    //左边墙壁为1  右边墙壁为2
    private String machineNum;

    //更新状态 0 未更新  1更新成功  2更新失败  3开始更新
    private Integer status =0;

    public String getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(String machineNum) {
        this.machineNum = machineNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
