package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.User_Student;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/6 2016.
 */
public class QcResponseStudentInfo extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("private_url") public String private_url;
        @SerializedName("group_url") public String group_url;
        @SerializedName("user") public User_Student user;
    }
}
