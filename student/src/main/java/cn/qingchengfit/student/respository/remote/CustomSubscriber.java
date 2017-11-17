package cn.qingchengfit.student.respository.remote;

import cn.qingchengfit.utils.ToastUtils;
import rx.Subscriber;

/**
 * Created by huangbaole on 2017/11/15.
 */

public abstract class CustomSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            if (((HttpException) e).getmAction() == 0) {
                ToastUtils.show(e.getMessage());
            }
        } else {
            ToastUtils.show(e.getMessage());
        }
    }


}
