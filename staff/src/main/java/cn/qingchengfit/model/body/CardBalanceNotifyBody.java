package cn.qingchengfit.model.body;

import java.util.List;

/**
 * Created by fb on 2017/2/28.
 */

public class CardBalanceNotifyBody {

    private List<CardBalanceNotifyBody.ConfigsBean> configs;

    public List<CardBalanceNotifyBody.ConfigsBean> getConfigs() {
        return configs;
    }

    public void setConfigs(List<CardBalanceNotifyBody.ConfigsBean> configs) {
        this.configs = configs;
    }

    public static class ConfigsBean {
        private int id;
        private int value;

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
