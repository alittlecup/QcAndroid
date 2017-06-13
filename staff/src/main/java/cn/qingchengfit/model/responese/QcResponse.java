package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;

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
 * <p> http响应基本结构
 * Created by Paper on 15/7/30 2015.
 */
public class QcResponse {
    @SerializedName("status") public int status;
    @SerializedName("msg") public String msg;
    @SerializedName("info") public String info;
    @SerializedName("level") public String level;
    @SerializedName("error_code") public String error_code;

    public String toJsonStr() {
        //        if (BuildConfig.DEBUG) {
        //            return "status:" + status +
        //                    "\nmsg:" + msg +
        //                    "\ninfo:" + info +
        //                    "\nlevel:" + level +
        //                    "\nerror_code:" + error_code;
        //        } else {
        return msg;
        //        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return toJsonStr();
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
