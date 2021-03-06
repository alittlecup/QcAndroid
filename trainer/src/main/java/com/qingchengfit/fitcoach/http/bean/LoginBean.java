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
 * Created by Paper on 15/8/5 2015.
 */
public class LoginBean {

    public String phone;
    public String password;
    public String push_id;
    public String push_channel_id;
    public String code;
    public String device_type;
    public String area_code;
    public boolean has_read_agreement;
    public boolean session_config = true;
    public LoginBean() {
    }

    public LoginBean(String phone, String password) {
        this.phone = phone;
        this.password = password;
        this.code = password;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
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

    public void setHas_read_agreement(boolean has_read_agreement) {
        this.has_read_agreement = has_read_agreement;
    }
}
