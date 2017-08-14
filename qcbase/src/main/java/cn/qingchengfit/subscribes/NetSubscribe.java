package cn.qingchengfit.subscribes;

import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import rx.Subscriber;

public abstract class NetSubscribe<T> extends Subscriber<T> {
    public NetSubscribe() {
    }

    @Override public void onError(Throwable e) {
        String s = e.getMessage();
        if (s == null) {
            s = "";
        }
        LogUtil.e("NetWorkThrowable", s);
        ToastUtils.show(s);
    }

    @Override public void onCompleted() {
    }
}