package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/8/30.
 */
public class SignInCancelEvent {

    private int checkInId;

    public SignInCancelEvent(int checkInId) {
        this.checkInId = checkInId;
    }

    public int getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(int checkInId) {
        this.checkInId = checkInId;
    }
}
