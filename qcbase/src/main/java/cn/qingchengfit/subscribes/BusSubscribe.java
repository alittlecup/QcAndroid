package cn.qingchengfit.subscribes;

import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import rx.Subscriber;

public abstract class BusSubscribe<T> extends Subscriber<T> {
    public BusSubscribe() {
    }

    @Override public void onError(Throwable e) {
        String s = e.getMessage();
        if (s == null) {
            s = "";
        }
        LogUtil.e("BusThrowable", s);
        ToastUtils.show(s);
    }

    @Override public void onCompleted() {
    }
}