package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.network.response.QcResponse;

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
 * Created by Paper on 15/9/15 2015.
 */
public class QcCoachRespone extends QcResponse {

    /**
     * data : {"coach":{"username":"黄三三","phone":"15123358198","weixin":"哈哈哈","description":"","city":"北京市","short_description":"我不是教练","gender":0,"id":2,"avatar":"http://zoneke-img.b0.upaiyun.com//2015/9/1442461927.jpg.jpg"}}
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * coach : {"username":"黄三三","phone":"15123358198","weixin":"哈哈哈","description":"","city":"北京市","short_description":"我不是教练","gender":0,"id":2,"avatar":"http://zoneke-img.b0.upaiyun.com//2015/9/1442461927.jpg.jpg"}
         */

        private CoachEntity coach;

        public CoachEntity getCoach() {
            return coach;
        }

        public void setCoach(CoachEntity coach) {
            this.coach = coach;
        }

        public static class CoachEntity {
            /**
             * username : 黄三三
             * phone : 15123358198
             * weixin : 哈哈哈
             * description :
             * city : 北京市
             * short_description : 我不是教练
             * gender : 0
             * id : 2
             * avatar : http://zoneke-img.b0.upaiyun.com//2015/9/1442461927.jpg.jpg
             */

            private String username;
            private String phone;
            private String weixin;
            private String description;
            private DistrictEntity gd_district;
            private String short_description;
            private int gender;
            private int id;
            private String avatar;

            public DistrictEntity getDistrict() {
                return gd_district;
            }

            public void setDistrict(DistrictEntity district) {
                this.gd_district = district;
            }

            public String getDistrictStr() {
                if (gd_district != null && gd_district.province != null && gd_district.city != null) {
                    if (gd_district.city.name.startsWith(gd_district.province.name)) {
                        return gd_district.city.name;
                    } else {
                        return gd_district.province.name + gd_district.city.name;
                    }
                } else {
                    return "";
                }
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

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getShort_description() {
                return short_description;
            }

            public void setShort_description(String short_description) {
                this.short_description = short_description;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {

                this.avatar = avatar;
            }

        }
    }
}
