package cn.qingchengfit.saasbase.student.network.body;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class StudentTransferBean {
    public Float create_count;
    public Float following_count;
    public Float member_count;
    public List<QcStudentBeanWithFollow> users;
}
