package com.qingchengfit.fitcoach.http.bean;

import java.util.List;

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
 * Created by Paper on 15/8/21 2015.
 */
public class QcResponCoachSys extends QcResponse {

    /**
     * data : {"systems":[{"name":"青橙健身工作室","id":2,"url":"http://feature2.qingchengfit.cn"},{"name":"","id":3,"url":"http://feature2.qingchengfit.cn"}]}
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
         * systems : [{"name":"青橙健身工作室","id":2,"url":"http://feature2.qingchengfit.cn"},{"name":"","id":3,"url":"http://feature2.qingchengfit.cn"}]
         */
        private List<SystemsEntity> systems;

        public List<SystemsEntity> getSystems() {
            return systems;
        }

        public void setSystems(List<SystemsEntity> systems) {
            this.systems = systems;
        }

        public static class SystemsEntity {
            /**
             * name : 青橙健身工作室
             * id : 2
             * url : http://feature2.qingchengfit.cn
             */
            private String name;
            private int id;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
