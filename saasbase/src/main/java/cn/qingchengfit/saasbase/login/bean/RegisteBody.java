package cn.qingchengfit.saasbase.login.bean;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/2/15 2016.
 */
public class RegisteBody {
    public String username;
    public String password;
    public String code;
    public int gender;//0,1       0是男
    public String phone;
    public String city;
    public String area_code;
    public String wechat_openid;
    public boolean has_read_agreement;
    public boolean session_config = true;

    public RegisteBody() {
    }

    private RegisteBody(Builder builder) {
        setUsername(builder.username);
        setPassword(builder.password);
        setCode(builder.code);
        setGender(builder.gender);
        setPhone(builder.phone);
        setCity(builder.city);
        area_code = builder.area_code;
        wechat_openid = builder.wechat_openid;
        has_read_agreement = builder.has_read_agreement;
        session_config = builder.session_config;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static final class Builder {
        private String username;
        private String password;
        private String code;
        private int gender;
        private String phone;
        private String city;
        private String area_code;
        private String wechat_openid;
        private boolean has_read_agreement;
        private boolean session_config;

        public Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder gender(int val) {
            gender = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public Builder wechat_openid(String val) {
            wechat_openid = val;
            return this;
        }

        public Builder has_read_agreement(boolean has_read_agreement) {
            has_read_agreement = has_read_agreement;
            return this;
        }

        public Builder session_config(boolean val) {
            session_config = val;
            return this;
        }

        public RegisteBody build() {
            return new RegisteBody(this);
        }
    }
}
