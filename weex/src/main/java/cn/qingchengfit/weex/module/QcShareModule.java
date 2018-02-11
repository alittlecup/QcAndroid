package cn.qingchengfit.weex.module;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import cn.qingchengfit.model.common.ShareBean;
import cn.qingchengfit.views.ShareDialogWithExtendFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.google.gson.Gson;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by huangbaole on 2018/2/6.
 */

public class QcShareModule extends WXModule {
  @JSMethod(uiThread = true) public void share(String json) {
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
  }
}
