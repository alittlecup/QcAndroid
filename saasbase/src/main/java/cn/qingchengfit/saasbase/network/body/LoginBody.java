package cn.qingchengfit.saasbase.network.body;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/18 2016.
 */
public class LoginBody {
    public String phone;
    public String password;
    public String push_id;
    public String push_channel_id;
    public String code;
    public String device_type;
    public String area_code;

    public LoginBody(String username, String password) {
        this.phone = username;
        this.password = password;
    }

    public LoginBody(String phone, String password, String code) {
        this.phone = phone;
        this.password = password;
        this.code = code;
    }

    private LoginBody(Builder builder) {
        phone = builder.phone;
        setPassword(builder.password);
        setPush_id(builder.push_id);
        setPush_channel_id(builder.push_channel_id);
        setCode(builder.code);
        setDevice_type(builder.device_type);
        area_code = builder.area_code;
    }

    public String getUsername() {
        return phone;
    }

    public void setUsername(String username) {
        this.phone = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getPush_channel_id() {
        return push_channel_id;
    }

    public void setPush_channel_id(String push_channel_id) {
        this.push_channel_id = push_channel_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public static final class Builder {
        private String phone;
        private String password;
        private String push_id;
        private String push_channel_id;
        private String code;
        private String device_type;
        private String area_code;

        public Builder() {
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder push_id(String val) {
            push_id = val;
            return this;
        }

        public Builder push_channel_id(String val) {
            push_channel_id = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder device_type(String val) {
            device_type = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public LoginBody build() {
            return new LoginBody(this);
        }
    }
}
