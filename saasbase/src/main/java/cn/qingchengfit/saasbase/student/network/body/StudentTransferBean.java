package cn.qingchengfit.saasbase.student.network.body;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class StudentTransferBean {
    public Float create_count = 0f;
    public Float following_count = 0f;
    public Float member_count = 0f;
    public List<QcStudentBeanWithFollow> users;
}
