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
 * Created by Paper on 16/2/22 2016.
 */
public class FixPhoneBody {
    String phone;
    String password;
    String code;
    String area_code;

    public FixPhoneBody(String phone, String password, String code) {
        this.phone = phone;
        this.password = password;
        this.code = code;
    }

    private FixPhoneBody(Builder builder) {
        setPhone(builder.phone);
        setPassword(builder.password);
        setCode(builder.code);
        area_code = builder.area_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public static final class Builder {
        private String phone;
        private String password;
        private String code;
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

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public FixPhoneBody build() {
            return new FixPhoneBody(this);
        }
    }
}
