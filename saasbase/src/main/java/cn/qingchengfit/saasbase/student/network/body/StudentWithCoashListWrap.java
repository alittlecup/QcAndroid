package cn.qingchengfit.saasbase.student.network.body;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.saasbase.student.items.QcStudentWithCoach;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class StudentWithCoashListWrap extends QcListData{
    public List<QcStudentWithCoach> users;
}
