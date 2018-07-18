package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.Resource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class BindLiveData {
  public static <T> void bindLiveData(Flowable<QcDataResponse<T>> observable,
    MutableLiveData<T> liveData, MutableLiveData<Resource<Object>> result, String tag) {
    observable.subscribe(new FlowableSubscriber<QcDataResponse<T>>() {
      @Override public void onSubscribe(Subscription s) {
        s.request(1);
      }

      @Override public void onNext(QcDataResponse<T> item) {
        if (item.status == 200) {
          if (liveData != null) liveData.postValue(item.data);
          if (result != null) result.postValue(Resource.success(tag));
        } else {
          if (result != null) result.postValue(Resource.error(item.getMsg(), null));
        }
      }

      @Override public void onError(Throwable ex) {
        if (result != null) result.postValue(Resource.error(ex.getMessage(), null));
      }

      @Override public void onComplete() {

      }
    });
  }
}
