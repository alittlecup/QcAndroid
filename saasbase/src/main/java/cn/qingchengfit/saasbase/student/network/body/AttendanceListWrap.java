package cn.qingchengfit.saasbase.student.network.body;

import java.util.List;

import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.saasbase.student.bean.Attendance;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceListWrap extends QcListData {
    public List<Attendance> attendances;
}
