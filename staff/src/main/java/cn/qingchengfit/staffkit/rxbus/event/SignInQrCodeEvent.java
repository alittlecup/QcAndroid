package cn.qingchengfit.staffkit.rxbus.event;

/**
 * Created by yangming on 16/8/31.
 */
public class SignInQrCodeEvent {

    private boolean isSignIn = true;

    public SignInQrCodeEvent(boolean isSignIn) {
        this.isSignIn = isSignIn;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
