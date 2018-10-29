package cn.qingchengfit.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxShareUtil {
  public static void shareBitmap(Context context, Bitmap bitmap, boolean isFriend) {
    String wechat_code = AppUtils.getManifestData(context, "WX_ID");
    if (!TextUtils.isEmpty(wechat_code)) {
      IWXAPI api = WXAPIFactory.createWXAPI(context, wechat_code, true);
      api.registerApp(wechat_code);
      WXImageObject wxImageObject = new WXImageObject(bitmap);
      WXMediaMessage mediaMessage = new WXMediaMessage();
      mediaMessage.mediaObject = wxImageObject;
      Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
      mediaMessage.thumbData = Util.bmpToByteArray(thumbBmp, true);
      SendMessageToWX.Req req = new SendMessageToWX.Req();
      req.transaction = buildTransaction("img");
      req.message = mediaMessage;
      req.scene =
          (!isFriend) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
      api.sendReq(req);
    }
  }

  private static String buildTransaction(final String type) {
    return (type == null) ? String.valueOf(System.currentTimeMillis())
        : type + System.currentTimeMillis();
  }
}
