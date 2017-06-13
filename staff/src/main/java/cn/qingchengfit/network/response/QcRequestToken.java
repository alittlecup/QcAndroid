package cn.qingchengfit.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangming on 16/11/15.
 */
public class QcRequestToken {
    @SerializedName("token") public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
