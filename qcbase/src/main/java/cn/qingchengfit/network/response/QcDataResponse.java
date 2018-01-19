package cn.qingchengfit.network.response;

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
 * Created by Paper on 2017/6/1.
 */

public class QcDataResponse<T> extends QcResponse {

    public QcDataResponse() {
        status = 901;
    }


    public T data;

    private QcDataResponse(Builder builder) {
        setStatus(builder.status);
        setMsg(builder.msg);
        setInfo(builder.info);
        setLevel(builder.level);
        setError_code(builder.error_code);
    }

    //public int total_count;
    //public int current_page;
    //public int pages;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static final class Builder {
        private int status;
        private String msg;
        private String info;
        private String level;
        private String error_code;

        public Builder() {
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Builder msg(String val) {
            msg = val;
            return this;
        }

        public Builder info(String val) {
            info = val;
            return this;
        }

        public Builder level(String val) {
            level = val;
            return this;
        }

        public Builder error_code(String val) {
            error_code = val;
            return this;
        }

        public QcDataResponse build() {
            return new QcDataResponse(this);
        }
    }
}
