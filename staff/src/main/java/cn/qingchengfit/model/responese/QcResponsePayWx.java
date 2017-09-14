package cn.qingchengfit.model.responese;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by peggy on 16/5/29.
 */

public class QcResponsePayWx extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("url") public String url;
        @SerializedName("qr_code_url") public String qr_code_url;
    }
}
