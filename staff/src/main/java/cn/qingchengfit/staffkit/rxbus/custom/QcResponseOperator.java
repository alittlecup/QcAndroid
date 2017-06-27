package cn.qingchengfit.staffkit.rxbus.custom;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.rxbus.event.EventLoginChange;
import cn.qingchengfit.utils.ToastUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/14 2016.
 */
public class QcResponseOperator<T> implements Observable.Operator<T, T> {

    @Override public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {

        return new Subscriber<T>() {
            @Override public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override public void onNext(T t) {
                if (t instanceof QcResponse) {
                    if (((QcResponse) t).getStatus() == ResponseConstant.SUCCESS) {
                        subscriber.onNext(t);
                    } else {
                        if (((QcResponse) t).getError_code().equalsIgnoreCase(ResponseConstant.E_Login)) {
                            RxBus.getBus().post(new EventLoginChange());
                        } else {
                            ToastUtils.logHttp((QcResponse) t);
                        }
                    }
                } else {
                    subscriber.onNext(t);
                }
            }
        };
    }
}
