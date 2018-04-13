package cn.qingchengfit.weex.module;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2018/4/3.
 */

public class QcTelModule extends WXModule {
  @JSMethod public void call(final String phone) {
    boolean mobiPhoneNum = AppUtils.isMobiPhoneNum(phone);
    if (mobiPhoneNum) {
      if (mWXSDKInstance.getContext() instanceof Activity) {
        new RxPermissions((Activity) mWXSDKInstance.getContext()).request(
            Manifest.permission.CALL_PHONE).subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean aBoolean) {
            if (aBoolean) {
              Uri uri = Uri.parse(new StringBuilder().append("tel:").append(phone).toString());
              Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
              dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              mWXSDKInstance.getContext().startActivity(dialntent);
            } else {
              ToastUtils.show("请在应用设置中开启拨打电话权限");
            }
          }
        });
      } else {
        Uri uri = Uri.parse(new StringBuilder().append("tel:").append(phone).toString());
        Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
        dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mWXSDKInstance.getContext().startActivity(dialntent);
      }
    }
  }
}
