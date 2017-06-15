package cn.qingchengfit.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangming on 16/11/15.
 */

public class QcResponse {
    @SerializedName("status") public int status;
    @SerializedName("msg") public String msg;
    @SerializedName("info") public String info;
    @SerializedName("level") public String level;
    @SerializedName("error_code") public String error_code;

    public String toJsonStr() {
        return msg;
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
