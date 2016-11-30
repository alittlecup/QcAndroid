package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.Permission;
import java.util.List;

public class QcResponsePermission extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("permissions")
        public List<Permission> permissions;
    }
}