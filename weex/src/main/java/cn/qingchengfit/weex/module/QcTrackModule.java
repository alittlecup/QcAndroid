package cn.qingchengfit.weex.module;

import android.util.Log;
import cn.qingchengfit.utils.SensorsUtils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by huangbaole on 2018/3/6.
 */

public class QcTrackModule extends WXModule {

  @JSMethod
  public  void track(String key,String json){
    Log.d("TAG", "track: "+key+"-->"+json);
    SensorsUtils.track(key, json, mWXSDKInstance.getContext());
  }
}
