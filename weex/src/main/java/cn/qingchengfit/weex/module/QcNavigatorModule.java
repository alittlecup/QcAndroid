package cn.qingchengfit.weex.module;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.AppUtils;
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

  }
  @JSMethod(uiThread = true)
  public void go(String path,String options, JSCallback callback) {
    String schema= AppUtils.getCurAppSchema(mWXSDKInstance.getContext());
    if("select_member".equals(path)){
      JSONObject jsonObject = JSON.parseObject(options);
      String shop_id = jsonObject.getString("shop_id");
      String brand_id = jsonObject.getString("brand_id");
      String multiple = jsonObject.getString("multiple");
      String user = jsonObject.getString("user_id");
      String users = jsonObject.getString("user_ids");
      String uri=schema+"://student/select_member/?shop_id="+shop_id+"&brand_id="+brand_id;
      if(!TextUtils.isEmpty(user)){
        uri=uri+"&user_id="+user;
      }
      if(!TextUtils.isEmpty(multiple)){
        uri=uri+"&multiple="+multiple;
      }
      if(!TextUtils.isEmpty(users)){
        uri=uri+"&user_ids="+users;
      }
      Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
      mWXSDKInstance.getContext().startActivity(intent);

    }else if("charge_card".equals(path)){
      JSONObject jsonObject = JSONObject.parseObject(options);
      String shop_id = jsonObject.getString("shop_id");
      String brand_id = jsonObject.getString("brand_id");
      String card_id = jsonObject.getString("card_id");

      String uri=schema+"://card/charge_card/?card_id="+card_id;

      Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
      mWXSDKInstance.getContext().startActivity(intent);
    }
    if(curCallback==null){
      curCallback=callback;
      RxBus.getBus()
          .register(QcNavigatorModule.class,String.class)
          .subscribeOn(rx.schedulers.Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<String>() {
            @Override public void call(String s) {
              curCallback.invoke(JSON.parse(s));
            }
          });
    }
    curCallback=callback;


  }

}
