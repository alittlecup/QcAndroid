package cn.qingchengfit.saasbase.user.bean;

public class CheckCodeBody {
    public String area_code;
    public String phone;
    public String code;
    public String wechat_openid;//用于检查是否注册过

    private CheckCodeBody(Builder builder) {
        area_code = builder.area_code;
        phone = builder.phone;
        code = builder.code;
        wechat_openid = builder.wechat_openid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String phone;
        private String code;
        private String wechat_openid;
        private String area_code;

        public Builder() {
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder wechat_openid(String val) {
            wechat_openid = val;
            return this;
        }

        public CheckCodeBody build() {
            return new CheckCodeBody(this);
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }
    }
}
