package cn.qingchengfit.saasbase.staff.model.body;

import android.support.annotation.StringRes;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;

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
 * Created by Paper on 16/5/16 2016.
 */
public class ManagerBody {
    private String id;

    private String username;
    private String phone;
    private String avatar;
    private Integer gender;
    private String position_id;
    private String area_code;
    public Boolean staff_enable;
    public Boolean coach_enable;

    public ManagerBody() {
    }


    @StringRes
    public int checkDataInPos(){
        if (CmStringUtils.isEmpty(username))
            return R.string.e_saler_username;
        return 0;
    }

    private ManagerBody(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setPhone(builder.phone);
        setAvatar(builder.avatar);
        setGender(builder.gender);
        setPosition_id(builder.position_id);
        setArea_code(builder.area_code);
        staff_enable = builder.staff_enable;
        coach_enable = builder.coach_enable;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final class Builder {
        private String id;
        private String username;
        private String phone;
        private String avatar;
        private Integer gender;
        private String position_id;
        private String area_code;
        private Boolean staff_enable;
        private Boolean coach_enable;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder gender(Integer val) {
            gender = val;
            return this;
        }

        public Builder position_id(String val) {
            position_id = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public Builder staff_enable(Boolean val) {
            staff_enable = val;
            return this;
        }

        public Builder coach_enable(Boolean val) {
            coach_enable = val;
            return this;
        }

        public ManagerBody build() {
            return new ManagerBody(this);
        }
    }


}
