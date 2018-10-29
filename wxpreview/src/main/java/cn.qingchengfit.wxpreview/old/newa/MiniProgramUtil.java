package cn.qingchengfit.wxpreview.old.newa;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;

public class MiniProgramUtil {
  private static final String MINIPROGRAM_KEY = "mini_program";

  private MiniProgramUtil() {
  }

  public static void saveMiniProgream(Context context, MiniProgram program) {
    String curAppName = AppUtils.getCurAppName(context);
    PreferenceUtils.setPrefString(context, MINIPROGRAM_KEY+curAppName, new Gson().toJson(program));
  }

  public static MiniProgram getMiniProgream(Context context) {
    String curAppName = AppUtils.getCurAppName(context);
    String prefString = PreferenceUtils.getPrefString(context, MINIPROGRAM_KEY+curAppName, "");
    if (TextUtils.isEmpty(prefString)) {
      return null;
    } else {
      try {
        return new Gson().fromJson(prefString, MiniProgram.class);
      } catch (Exception e) {
        return null;
      }
    }
  }
}
