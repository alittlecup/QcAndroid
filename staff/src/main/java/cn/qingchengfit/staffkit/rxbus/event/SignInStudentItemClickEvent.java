package cn.qingchengfit.staffkit.rxbus.event;

import cn.qingchengfit.model.base.StudentBean;

/**
 * Created by yangming on 16/8/27.
 */
public class SignInStudentItemClickEvent {
    private boolean isSignIn;
    private StudentBean studentBean;

    public StudentBean getStudentBean() {
        return studentBean;
    }

    public void setStudentBean(StudentBean studentBean) {
        this.studentBean = studentBean;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
