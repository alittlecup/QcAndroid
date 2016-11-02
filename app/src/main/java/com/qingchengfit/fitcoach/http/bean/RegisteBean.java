package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/7/31 2015.
 */
public class RegisteBean {
    public String username;
    public String password;
    public String code;
    public int gender;//0,1
    public String phone;
    public String city;
    public String area_code;





    public RegisteBean() {
    }

    public RegisteBean(String username, String password, String code, int gender) {
        this.username = username;
        this.password = password;
        this.code = code;
        this.gender = gender;
    }

    public RegisteBean(String phone, String username, String password) {

        this.username = username;
        this.password = password;
    }

    private RegisteBean(Builder builder) {
        setUsername(builder.username);
        setPassword(builder.password);
        setCode(builder.code);
        setGender(builder.gender);
        setPhone(builder.phone);
        setCity(builder.city);
        area_code = builder.area_code;
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

    public static final class Builder {
        private String username;
        private String password;
        private String code;
        private int gender;
        private String phone;
        private String city;
        private String area_code;

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

        public RegisteBean build() {
            return new RegisteBean(this);
        }
    }
}
