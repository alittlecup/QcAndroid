package cn.qingchengfit.network;

import android.text.TextUtils;
import cn.qingchengfit.network.response.QcDataResponse;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by yangming on 16/11/16.
 */

public class RxHelper {
    /**
     * 对结果进行预处理
     */
    public static <T> Observable.Transformer<QcDataResponse<T>, T> handleResult() {
        return new Observable.Transformer<QcDataResponse<T>, T>() {
            @Override public Observable<T> call(Observable<QcDataResponse<T>> tObservable) {
                return tObservable.flatMap(new Func1<QcDataResponse<T>, Observable<T>>() {
                    @Override public Observable<T> call(QcDataResponse<T> result) {
                        Timber.e(result.status + "");
                        if (ResponseConstant.checkSuccess(result)) {
                            return createData(result.data);
                        } else {
                            return Observable.error(new RuntimeException(TextUtils.isEmpty(result.msg) ? "请求失败" : result.msg));
                        }
                    }
                }).onBackpressureBuffer().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果进行预处理--带生命周期管理
     */
    public static <T> Observable.Transformer<QcDataResponse<T>, T> handleResult(final ActivityLifeCycleEvent event,
        final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<QcDataResponse<T>, T>() {
            @Override public Observable<T> call(Observable<QcDataResponse<T>> tObservable) {
                // 生命周期管理
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                    lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                        @Override public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                            return activityLifeCycleEvent.equals(event);
                        }
                    });
                return tObservable.flatMap(new Func1<QcDataResponse<T>, Observable<T>>() {
                    @Override public Observable<T> call(QcDataResponse<T> result) {
                        Timber.e(result.status + "");
                        if (result.status == 200) {
                            return createData(result.data);
                        } else {
                            return Observable.error(new RuntimeException("请求失败"));
                        }
                    }
                })
                    .takeUntil(compareLifecycleObservable)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
