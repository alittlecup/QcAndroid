package cn.qingchengfit.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangming on 16/11/15.
 */

public class QcResponseData<T> extends QcResponse {

    @SerializedName("data") public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
