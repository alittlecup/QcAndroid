package cn.qingchengfit.network;

import rx.Subscriber;

/**
 * Created by yangming on 16/11/16.
 */

public abstract class ResultSubscribe<T> extends Subscriber<T> {
    public ResultSubscribe() {
    }

    @Override public void onNext(T t) {
        _onNext(t);
    }

    @Override public void onError(Throwable e) {
        _onError(e.getMessage());
    }

    @Override public void onCompleted() {
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
