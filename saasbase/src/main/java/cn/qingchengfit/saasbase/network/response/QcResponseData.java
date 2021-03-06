package cn.qingchengfit.saasbase.network.response;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yangming on 16/9/20.
 */

public class QcResponseData<T> extends QcResponse {

    @SerializedName("data") public T data;
    @Deprecated public int pages;
    @Deprecated public int current_page;
    @Deprecated public int total_count;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
