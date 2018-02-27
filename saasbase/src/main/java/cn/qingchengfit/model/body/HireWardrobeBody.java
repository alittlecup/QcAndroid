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
 * Created by Paper on 16/9/5.
 */
public class HireWardrobeBody {
    public Long locker_id;
    public String user_id;
    public boolean is_long_term_borrow;
    /**
     * 支付方式
     * deal_mode = 1 # 会员卡支付
     * deal_mode = 2 # 现金支付
     * deal_mode = 3 # 微信支付
     * deal_mode = 4 # 银行卡支付
     */
    public Integer deal_mode;
    public String card_id;
    public String cost;
    public String start;
    public String end;

    private HireWardrobeBody(Builder builder) {
        locker_id = builder.locker_id;
        user_id = builder.user_id;
        is_long_term_borrow = builder.is_long_term_borrow;
        deal_mode = builder.deal_mode;
        card_id = builder.card_id;
        cost = builder.cost;
        start = builder.start;
        end = builder.end;
    }

    public static final class Builder {
        private Long locker_id;
        private String user_id;
        private boolean is_long_term_borrow;
        private Integer deal_mode;
        private String card_id;
        private String cost;
        private String start;
        private String end;

        public Builder() {
        }

        public Builder locker_id(Long val) {
            locker_id = val;
            return this;
        }

        public Builder user_id(String val) {
            user_id = val;
            return this;
        }

        public Builder is_long_term_borrow(boolean val) {
            is_long_term_borrow = val;
            return this;
        }

        public Builder deal_mode(Integer val) {
            deal_mode = val;
            return this;
        }

        public Builder card_id(String val) {
            card_id = val;
            return this;
        }

        public Builder cost(String val) {
            cost = val;
            return this;
        }

        public Builder start(String val) {
            start = val;
            return this;
        }

        public Builder end(String val) {
            end = val;
            return this;
        }

        public HireWardrobeBody build() {
            return new HireWardrobeBody(this);
        }
    }
}
