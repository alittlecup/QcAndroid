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
 * Created by Paper on 15/9/15 2015.
 */
public class QcCoachRespone extends QcResponse {


    /**
     * data : {"coach":{"username":"测试","phone":"15123358198","weixin":"","description":"","city":"","short_description":"","gender":0,"id":6,"avatar":""}}
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
         * coach : {"username":"测试","phone":"15123358198","weixin":"","description":"","city":"","short_description":"","gender":0,"id":6,"avatar":""}
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
             * username : 测试
             * phone : 15123358198
             * weixin :
             * description :
             * city :
             * short_description :
             * gender : 0
             * id : 6
             * avatar :
             */

            private String username;
            private String phone;
            private String weixin;
            private String description;
            private String city;
            private String short_description;
            private int gender;
            private int id;
            private String avatar;

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

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
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
