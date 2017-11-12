package cn.qingchengfit.saasbase.student.network.body;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.qingchengfit.network.response.QcListData;

/**
 * Created by huangbaole on 2017/10/27.
 */

public class AllotDataResponseWrap extends QcListData {
    @SerializedName("coaches") public List<AllotDataResponse> coaches;
    @SerializedName("sellers") public List<AllotDataResponse> sellers;

}
