package cn.qingchengfit.model.body;

/**
 * Created by peggy on 16/5/27.
 */

public class RenewBody {
    public String type;
    public int channel;
    public String id;
    public String model;
    public String app_id;
    public int times;
    public boolean favorable;

    private RenewBody(Builder builder) {
        type = builder.type;
        channel = builder.channel;
        id = builder.id;
        model = builder.model;
        app_id = builder.app_id;
        times = builder.times;
        favorable = builder.favorable;
    }

    public static final class Builder {
        private String type;
        private int channel;
        private String id;
        private String model;
        private String app_id;
        private int times;
        private boolean favorable;

        public Builder() {
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder channel(int val) {
            channel = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder model(String val) {
            model = val;
            return this;
        }

        public Builder app_id(String val) {
            app_id = val;
            return this;
        }

        public Builder times(int val) {
            times = val;
            return this;
        }

        public Builder favorable(boolean val) {
            favorable = val;
            return this;
        }

        public RenewBody build() {
            return new RenewBody(this);
        }
    }
}
