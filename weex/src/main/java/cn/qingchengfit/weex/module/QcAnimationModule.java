package cn.qingchengfit.weex.module;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.dom.DOMAction;
import com.taobao.weex.dom.RenderAction;
import com.taobao.weex.dom.WXDomHandler;
import com.taobao.weex.dom.action.Actions;
import com.taobao.weex.ui.animation.WXAnimationBean;
import com.taobao.weex.ui.component.WXComponent;

/**
 * Created by huangbaole on 2018/2/7.
 */

public class QcAnimationModule extends WXModule {
  @JSMethod
  public void move(@Nullable String ref, @Nullable String animation, @Nullable String callBack) {
    if (!TextUtils.isEmpty(ref) && !TextUtils.isEmpty(animation) && mWXSDKInstance != null) {
      DOMAction animationActions = new QcAnimation(ref, animation, callBack);
      //Due to animation module rely on the result of the css-layout and the batch mechanism of
      //css-layout, the animation.transition must be delayed the batch time.
      WXSDKManager.getInstance().getWXDomManager().postActionDelay(mWXSDKInstance.getInstanceId(),
          animationActions,
          false, WXDomHandler.DELAY_TIME);
    }
  }

  //add by moxun on 12/26/2016
  public static class AnimationHolder {

    private WXAnimationBean wxAnimationBean;
    private String callback;

    public void execute(WXSDKInstance mInstance, WXComponent component) {
      RenderAction action = Actions.getAnimationAction(component.getRef(), wxAnimationBean, callback);
      WXSDKManager.getInstance().getWXRenderManager().runOnThread(mInstance.getInstanceId(), action);
    }

    public AnimationHolder(WXAnimationBean wxAnimationBean, String callback) {
      this.wxAnimationBean = wxAnimationBean;
      this.callback = callback;
    }
  }


}
