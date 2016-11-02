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
 * Created by Paper on 15/8/6 2015.
 */
public class GetCodeBean {

    public String area_code;
    public String phone;

    private GetCodeBean(Builder builder) {
        area_code = builder.area_code;
        phone = builder.phone;
    }

    public static final class Builder {
        private String area_code;
        private String phone;

        public Builder() {
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public GetCodeBean build() {
            return new GetCodeBean(this);
        }
    }
}
