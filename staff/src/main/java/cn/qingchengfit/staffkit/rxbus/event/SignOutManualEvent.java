package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/9/1.
 */
public class SignOutManualEvent {

    private int position;
    private int checkInId;
    private int type;

    public SignOutManualEvent(int position, int checkInId) {
        this.position = position;
        this.checkInId = checkInId;
        type = 0;
    }

    private SignOutManualEvent(Builder builder) {
        setPosition(builder.position);
        setCheckInId(builder.checkInId);
        type = builder.type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final class Builder {
        private int position;
        private int checkInId;
        private int type;

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

        public Builder type(int val) {
            type = val;
            return this;
        }

        public SignOutManualEvent build() {
            return new SignOutManualEvent(this);
        }
    }
}
