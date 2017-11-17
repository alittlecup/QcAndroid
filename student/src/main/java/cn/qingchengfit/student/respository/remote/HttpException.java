package cn.qingchengfit.student.respository.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2017/11/15.
 */

public class HttpException extends RuntimeException {
    @Retention(RetentionPolicy.SOURCE)
    public @interface ERRORACTION{
        int DISMISS_TOAST=-1;
    }
    private @ERRORACTION int mAction=0;
    public HttpException(String msg,@ERRORACTION int action){
        super(msg);
        this.mAction=action;
    }

    public int getmAction() {
        return mAction;
    }
}
