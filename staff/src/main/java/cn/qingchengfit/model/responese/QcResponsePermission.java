package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QcResponsePermission extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("permissions") public List<Permission> permissions;
    }
}