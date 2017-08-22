package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInEvent {

    public static final int ACTION_SIGNIN_IGNOR = 0;
    public static final int ACTION_SIGNIN_CONFIRM = 1;
    public static final int ACTION_SIGNIN_LOCKER = 2;
    public static final int ACTION_SIGNOUT_CONFIRM = 3;
    public static final int ACTION_SIGNOUT_IGNOR = 4;
    public static final int ACTION_SIGNIN_ADD_IMG = 5;
    public static final int ACTION_SIGNOUT_ADD_IMG = 6;
    public static final int ACTION_SIGNOUT_MANUAL = 7;

    private int position;
    private int checkInId;
    private int action;

    public SignInEvent(int position, int checkInId) {
        this.position = position;
        this.checkInId = checkInId;
    }

    private SignInEvent(Builder builder) {
        position = builder.position;
        checkInId = builder.checkInId;
        action = builder.action;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(int checkInId) {
        this.checkInId = checkInId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public static final class Builder {

        private int position;
        private int checkInId;
        private int action;

        public Builder() {
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public Builder checkInId(int val) {
            checkInId = val;
            return this;
        }

        public Builder action(int val) {
            action = val;
            return this;
        }

        public SignInEvent build() {
            return new SignInEvent(this);
        }
    }
}
