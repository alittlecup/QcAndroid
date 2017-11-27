package cn.qingchengfit.saasbase.cards.network.body;

import java.util.List;

public class CardBalanceNotifyBody {

    private List<ConfigsBean> configs;

    public List<CardBalanceNotifyBody.ConfigsBean> getConfigs() {
        return configs;
    }

    public void setConfigs(List<CardBalanceNotifyBody.ConfigsBean> configs) {
        this.configs = configs;
    }

    public static class ConfigsBean {
        private String id;
        private int value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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