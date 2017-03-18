package com.qingchengfit.fitcoach.bean;

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
 * Created by Paper on 2017/3/7.
 */

public class SyncExpBody {
    public boolean group_is_hidden;
    public boolean private_is_hidden;
    public boolean sale_is_hidden;
    public String description;

    private SyncExpBody(Builder builder) {
        group_is_hidden = builder.group_is_hidden;
        private_is_hidden = builder.private_is_hidden;
        sale_is_hidden = builder.sale_is_hidden;
        description = builder.description;
    }

    public static final class Builder {
        private boolean group_is_hidden;
        private boolean private_is_hidden;
        private boolean sale_is_hidden;
        private String description;

        public Builder() {
        }

        public Builder group_is_hidden(boolean val) {
            group_is_hidden = val;
            return this;
        }

        public Builder private_is_hidden(boolean val) {
            private_is_hidden = val;
            return this;
        }

        public Builder sale_is_hidden(boolean val) {
            sale_is_hidden = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public SyncExpBody build() {
            return new SyncExpBody(this);
        }
    }
}
