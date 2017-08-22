package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/8/27.
 */
public class SignInManualEvent {

    private boolean isSignIn = true;
    private boolean isCancel = false;

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
