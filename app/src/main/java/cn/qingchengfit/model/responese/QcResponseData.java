package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.http.bean.QcResponse;

/**
 * Created by yangming on 16/9/20.
 */

public class QcResponseData<T> extends QcResponse {

    @SerializedName("data")
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
