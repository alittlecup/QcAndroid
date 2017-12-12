package cn.qingchengfit.saasbase.student.network.body;

import java.util.List;

import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.saasbase.student.bean.Absentce;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AbsentceListWrap extends QcListData {
    public List<Absentce> attendances;
}
