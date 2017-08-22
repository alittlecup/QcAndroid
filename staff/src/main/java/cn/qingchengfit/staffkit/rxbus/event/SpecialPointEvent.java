package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/11/7.
 */
@Deprecated public class SpecialPointEvent {
    public static final int ACTION_SHOW = 1;
    public static final int ACTION_DISMISS = 2;
    public static final int ACTION_UPDATE = 3;
    public int action = 0;

    public SpecialPointEvent() {
    }

    public SpecialPointEvent(int action) {
        this.action = action;
    }
}
