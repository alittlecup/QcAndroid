package cn.qingchengfit.staffkit.train.model;

import cn.qingchengfit.model.responese.QcResponsePage;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/3/28.
 */

public class GroupListResponse extends QcResponsePage {
    @SerializedName("teams") public List<GroupListBean> datas;
}
