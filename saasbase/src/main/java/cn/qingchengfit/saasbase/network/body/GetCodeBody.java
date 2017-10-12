package cn.qingchengfit.saasbase.network.body;

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
 * Created by Paper on 16/2/15 2016.
 */
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
