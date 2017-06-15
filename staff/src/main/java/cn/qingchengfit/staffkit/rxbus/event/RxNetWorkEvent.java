package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by peggy on 16/5/31.
 */

public class RxNetWorkEvent {
    public static final int STATE_ERR = -1;
    public int status;

    public RxNetWorkEvent(int status) {
        this.status = status;
    }
}
