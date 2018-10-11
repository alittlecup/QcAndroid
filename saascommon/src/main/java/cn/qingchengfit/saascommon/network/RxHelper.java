package cn.qingchengfit.saascommon.network;


import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/10/30.
 */

public final class RxHelper {

    public static  <T> Observable.Transformer<T, T> schedulersTransformer() {
        return observable ->
                observable.onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }
    public static  <T> ObservableTransformer<T, T> schedulersTransformerR2() {
        return observable ->
            observable
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread());
    }
    public static  <T> FlowableTransformer<T, T> schedulersTransformerFlow() {
        return observable ->
            observable
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread());
    }

}
