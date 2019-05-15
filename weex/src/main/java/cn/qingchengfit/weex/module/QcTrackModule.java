package cn.qingchengfit.weex.module;

import android.util.Log;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by huangbaole on 2018/3/6.
 */

public class QcTrackModule extends WXModule {

  @JSMethod
  public  void track(String key,String json){
    Log.d("TAG", "track: "+key+"-->"+json);
  }

  @JSMethod
  public  void trackAppView(String url,String title,String name){
    Log.d("TAG", "track: "+url +"-->"+name);
  }

}
