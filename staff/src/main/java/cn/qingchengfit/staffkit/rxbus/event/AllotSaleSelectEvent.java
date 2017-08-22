package cn.qingchengfit.staffkit.rxbus.event;

import cn.qingchengfit.model.base.StudentBean;

/**
 * Created by yangming on 16/10/18.
 */

public class AllotSaleSelectEvent {
    public static final int TYPE_SELECT = 1;
    public static final int TYPE_REMOVE = 2;
    public int isSelect = 0;
    public int position = 0;
    public StudentBean studentBean;

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public StudentBean getStudentBean() {
        return studentBean;
    }

    public void setStudentBean(StudentBean studentBean) {
        this.studentBean = studentBean;
    }
}
