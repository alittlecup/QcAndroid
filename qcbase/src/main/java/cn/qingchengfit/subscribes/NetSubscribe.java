package cn.qingchengfit.subscribes;

import cn.qingchengfit.utils.LogUtil;
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
        //// TODO: 2017/10/11 展示
        //ToastUtils.show(s);
    }

    @Override public void onCompleted() {
    }
}