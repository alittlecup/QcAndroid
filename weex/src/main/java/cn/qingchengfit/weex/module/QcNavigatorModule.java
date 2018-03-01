package cn.qingchengfit.weex.module;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.navigator.WXNavigatorModule;
import com.taobao.weex.bridge.JSCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2018/2/28.
 */

public class QcNavigatorModule extends WXNavigatorModule {
  JSCallback curCallback;
  public QcNavigatorModule(){
      RxBus.getBus()
          .register(QcNavigatorModule.class,String.class)
          .subscribeOn(rx.schedulers.Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<String>() {
            @Override public void call(String s) {
              curCallback.invoke(s);
            }
          });
  }
  @JSMethod(uiThread = true)
  public void go(String path,String options, JSCallback callback) {
    if("select_member".equals(path)){
      JSONObject jsonObject = JSON.parseObject(options);
      String shop_id = jsonObject.getString("shop_id");
      String brand_id = jsonObject.getString("brand_id");
      String user = jsonObject.getString("user");
      String users = jsonObject.getString("users");
      String uri="qcstaff://student/select_member/?shop_id="+shop_id+"&brand_id="+brand_id;
      if(!TextUtils.isEmpty(user)){
        uri=uri+"&user="+user;
      }
      if(!TextUtils.isEmpty(users)){
        uri=uri+"&multiple=1&users="+users;
      }
      Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
      mWXSDKInstance.getContext().startActivity(intent);
      curCallback=callback;

    }
  }

}
