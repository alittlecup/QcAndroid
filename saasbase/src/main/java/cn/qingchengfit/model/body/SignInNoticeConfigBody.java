package cn.qingchengfit.model.body;

import java.util.List;

/**
 * Created by yangming on 16/9/29.
 */

public class SignInNoticeConfigBody {

    /**
     * brand_id : 2
     * shop_id : 8
     * id : 16
     * value : 0
     */

    private List<ConfigsBean> configs;

    public List<ConfigsBean> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigsBean> configs) {
        this.configs = configs;
    }

    public static class ConfigsBean {
        private int brand_id;
        private int shop_id;
        private int id;
        private int value;

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
