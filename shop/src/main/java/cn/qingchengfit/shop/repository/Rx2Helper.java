package cn.qingchengfit.shop.repository;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Rx2Helper {
  public static <T> FlowableTransformer<T, T> schedulersTransformer() {
    return flowable -> flowable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
