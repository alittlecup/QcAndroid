package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by peggy on 16/5/31.
 */

public class ResponseService extends QcResponse {
    @SerializedName("data") public CoachService data;
}
