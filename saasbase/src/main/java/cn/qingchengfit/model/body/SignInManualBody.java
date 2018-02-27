package cn.qingchengfit.model.body;

/**
 * Created by yangming on 16/9/6.
 */
public class SignInManualBody {

    /**
     * user_id : 1
     * card_id : 1
     * locker_id :
     */

    private int user_id;
    private String card_id;
    private String locker_id;

    private SignInManualBody(Builder builder) {
        setUser_id(builder.user_id);
        setCard_id(builder.card_id);
        setLocker_id(builder.locker_id);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getLocker_id() {
        return locker_id;
    }

    public void setLocker_id(String locker_id) {
        this.locker_id = locker_id;
    }

    public static final class Builder {
        private int user_id;
        private String card_id;
        private String locker_id;

        public Builder() {
        }

        public Builder user_id(int val) {
            user_id = val;
            return this;
        }

        public Builder card_id(String val) {
            card_id = val;
            return this;
        }

        public Builder locker_id(String val) {
            locker_id = val;
            return this;
        }

        public SignInManualBody build() {
            return new SignInManualBody(this);
        }
    }
}
