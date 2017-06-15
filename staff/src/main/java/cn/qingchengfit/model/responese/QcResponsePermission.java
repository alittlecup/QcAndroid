package cn.qingchengfit.model.responese;

import cn.qingchengfit.staffkit.usecase.bean.Permission;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QcResponsePermission extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("permissions") public List<Permission> permissions;
    }
}