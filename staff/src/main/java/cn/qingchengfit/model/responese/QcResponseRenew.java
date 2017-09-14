package cn.qingchengfit.model.responese;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by peggy on 16/5/27.
 */

public class QcResponseRenew extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {

        @SerializedName("status") public String status;
        @SerializedName("url") public String url;
    }
}
