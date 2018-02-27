package cn.qingchengfit.model.body;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/25.
 */
public class ScanBody {

    String session_id;
    String url;
    String module;
    String brand_id;
    String shop_id;

    public ScanBody(String session_id, String url, String module, String brand_id, String shop_id) {
        this.session_id = session_id;
        this.url = url;
        this.module = module;
        this.brand_id = brand_id;
        this.shop_id = shop_id;
    }

    private ScanBody(Builder builder) {
        session_id = builder.session_id;
        url = builder.url;
        module = builder.module;
        brand_id = builder.brand_id;
        shop_id = builder.shop_id;
    }

    public static final class Builder {
        private String session_id;
        private String url;
        private String module;
        private String brand_id;
        private String shop_id;

        public Builder() {
        }

        public Builder session_id(String val) {
            session_id = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder module(String val) {
            module = val;
            return this;
        }

        public Builder brand_id(String val) {
            brand_id = val;
            return this;
        }

        public Builder shop_id(String val) {
            shop_id = val;
            return this;
        }

        public ScanBody build() {
            return new ScanBody(this);
        }
    }
}
