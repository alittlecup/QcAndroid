package cn.qingchengfit.shop.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.HttpException;
import cn.qingchengfit.utils.ToastUtils;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import rx.Observable;
import rx.RxReactiveStreams;

/**
 * Created by huangbaole on 2017/11/29.
 */

public class LiveDataReactiveStreams {
  public static <T> LiveData<T> fromPublisher(final Observable<QcDataResponse<T>> observable) {
    return new PublisherLiveData<>(RxReactiveStreams.toPublisher(observable));
  }

  public static <T> LiveData<T> fromPublisher(final Publisher<QcDataResponse<T>> observable) {
    return new PublisherLiveData<>(observable);
  }

  /**
   * Defines a {@link LiveData} object that wraps a {@link Publisher}.
   * <p>
   * <p>
   * When the LiveData becomes active, it subscribes to the emissions from the Publisher.
   * <p>
   * <p>
   * When the LiveData becomes inactive, the subscription is cleared.
   * LiveData holds the last value emitted by the Publisher when the LiveData was active.
   * <p>
   * Therefore, in the case of a hot RxJava Observable, when a new LiveData {@link Observer} is
   * added, it will automatically notify with the last value held in LiveData,
   * which might not be the last value emitted by the Publisher.
   * <p>
   * <p>
   * Note that LiveData does NOT handle errors and it expects that errors are treated as states
   * in the data that's held. In case of an error being emitted by the publisher, an error will
   * be propagated to the main thread and the app will crash.
   *
   * @param <T> The type of data hold by this instance.
   */
  private static class PublisherLiveData<T> extends LiveData<T> {
    private final Publisher mPublisher;
    final AtomicReference<LiveDataSubscriber> mSubscriber;

    PublisherLiveData(@NonNull final Publisher publisher) {
      mPublisher = publisher;
      mSubscriber = new AtomicReference<>();
    }

    @Override protected void onActive() {
      super.onActive();
      LiveDataSubscriber s = new LiveDataSubscriber();
      mSubscriber.set(s);
      mPublisher.subscribe(s);
    }

    @Override protected void onInactive() {
      super.onInactive();
      LiveDataSubscriber s = mSubscriber.getAndSet(null);
      if (s != null) {
        s.cancelSubscription();
      }
    }

    final class LiveDataSubscriber extends AtomicReference<Subscription>
        implements Subscriber<QcDataResponse<T>> {

      @Override public void onSubscribe(Subscription s) {
        if (compareAndSet(null, s)) {
          s.request(Long.MAX_VALUE);
        } else {
          s.cancel();
        }
      }

      @Override public void onNext(QcDataResponse<T> item) {
        if (item.status == 200) {
          postValue(item.data);
        } else {
          onError(new HttpException(item.getMsg(), 0));
        }
      }

      @Override public void onError(final Throwable ex) {
        mSubscriber.compareAndSet(this, null);
        // REFACTOR: 2017/11/30 就事件总线吧
        if (ex instanceof HttpException) {
          if (!(((HttpException) ex).getmAction() == HttpException.ERRORACTION.DISMISS_TOAST)) {
            ToastUtils.show(ex.getMessage());
          }
        } else {
          ToastUtils.show("网络异常");
        }
        RxBus.getBus().post(SwipeRefreshLayout.class, false);
      }

      @Override public void onComplete() {
        mSubscriber.compareAndSet(this, null);
      }

      public void cancelSubscription() {
        Subscription s = get();
        if (s != null) {
          s.cancel();
        }
      }
    }
  }
}
