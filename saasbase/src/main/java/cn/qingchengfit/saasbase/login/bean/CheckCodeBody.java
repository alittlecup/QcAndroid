package cn.qingchengfit.saasbase.login.bean;

public class CheckCodeBody {
    public String phone;
    public String code;

    private CheckCodeBody(Builder builder) {
        phone = builder.phone;
        code = builder.code;
    }

    public static final class Builder {
        private String phone;
        private String code;

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

        public CheckCodeBody build() {
            return new CheckCodeBody(this);
        }
    }
}
