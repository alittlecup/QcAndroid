package cn.qingchengfit.weex.adapter;

import android.content.Context;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXPerformance;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangbaole on 2018/1/10.
 */

public class UserTrackAdapter implements IWXUserTrackAdapter {
  @Override public void commit(Context context, String eventId, String type, WXPerformance perf,
      Map<String, Serializable> params) {

  }
}
