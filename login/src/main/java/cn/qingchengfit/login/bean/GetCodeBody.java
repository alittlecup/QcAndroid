package cn.qingchengfit.login.bean;

public class GetCodeBody {
    public String phone;
    public String area_code;

    public GetCodeBody(String phone) {
        this.phone = phone;
    }

    private GetCodeBody(Builder builder) {
        phone = builder.phone;
        area_code = builder.area_code;
    }

    public static final class Builder {
        private String phone;
        private String area_code;

        public Builder() {
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public GetCodeBody build() {
            return new GetCodeBody(this);
        }
    }
}
