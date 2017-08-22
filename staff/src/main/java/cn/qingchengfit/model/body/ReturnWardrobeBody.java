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
public class ReturnWardrobeBody {

    public Long locker_id;
    public boolean is_long_term_borrow;
    public int deal_mode;
    public String card_id;
    public String cost;

    private ReturnWardrobeBody(Builder builder) {
        locker_id = builder.locker_id;
        is_long_term_borrow = builder.is_long_term_borrow;
        deal_mode = builder.deal_mode;
        card_id = builder.card_id;
        cost = builder.cost;
    }

    public static final class Builder {
        private Long locker_id;
        private boolean is_long_term_borrow;
        private int deal_mode;
        private String card_id;
        private String cost;

        public Builder() {
        }

        public Builder locker_id(Long val) {
            locker_id = val;
            return this;
        }

        public Builder is_long_term_borrow(boolean val) {
            is_long_term_borrow = val;
            return this;
        }

        public Builder deal_mode(int val) {
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

        public ReturnWardrobeBody build() {
            return new ReturnWardrobeBody(this);
        }
    }
}
