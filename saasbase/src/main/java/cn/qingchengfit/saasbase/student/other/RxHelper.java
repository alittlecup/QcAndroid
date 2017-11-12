package cn.qingchengfit.saasbase.student.other;


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
}
