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

  public static void saveMiniProgream(Context context,String gymId,MiniProgram program) {
    PreferenceUtils.setPrefString(context, MINIPROGRAM_KEY+gymId, new Gson().toJson(program));
  }

  public static MiniProgram getMiniProgream(Context context,String gymId) {
    String prefString = PreferenceUtils.getPrefString(context, MINIPROGRAM_KEY+gymId, "");
    if (TextUtils.isEmpty(prefString)) {
      return null;
    } else {
      try {
        MiniProgram miniProgram = new Gson().fromJson(prefString, MiniProgram.class);
        if(TextUtils.isEmpty(miniProgram.logo_url)||TextUtils.isEmpty(miniProgram.qrcode_url)){
          return null;
        }
        return miniProgram;
      } catch (Exception e) {
        return null;
      }
    }
  }
}
