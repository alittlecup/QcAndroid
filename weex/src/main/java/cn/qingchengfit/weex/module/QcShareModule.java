package cn.qingchengfit.weex.module;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventShareFun;
import cn.qingchengfit.model.common.ShareBean;
import cn.qingchengfit.views.ShareDialogWithExtendFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.google.gson.Gson;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2018/2/6.
 */

public class QcShareModule extends WXSDKEngine.DestroyableModule {
  JSCallback jsCallback;
  Subscription subscribe;

  @JSMethod(uiThread = true) public void share(String json, final JSCallback callback) {
    try {
      ShareBean bean = new Gson().fromJson(json, ShareBean.class);
      if (bean.extra == null) {
        Context context = mWXSDKInstance.getContext();
        if (context instanceof FragmentActivity) {
          ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link)
              .show(((FragmentActivity) context).getSupportFragmentManager(), "");
        }
      } else {

        Context context = mWXSDKInstance.getContext();
        if (context instanceof FragmentActivity) {
          ShareDialogWithExtendFragment.newInstance(bean)
              .show(((FragmentActivity) context).getSupportFragmentManager(), "");
        }
      }
    } catch (Exception e) {

    }
    if (jsCallback == null) {
      subscribe = RxBus.getBus()
          .register(EventShareFun.class)
          .observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(new Action1<EventShareFun>() {
            public void call(EventShareFun eventShareFun) {
              if (eventShareFun.shareExtends != null) {
                Gson gson = new Gson();
                String json = gson.toJson(eventShareFun.shareExtends);
                jsCallback.invoke(json);
              }
            }
          });
    }
    jsCallback=callback;
  }

  @Override public void destroy() {
    subscribe.unsubscribe();
  }
}
