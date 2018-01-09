package cn.qingchengfit.weex.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Created by huangbaole on 2018/1/8.
 */

public class ImageAdapter implements IWXImgLoaderAdapter {
  @Override public void setImage(final String url, final ImageView view, WXImageQuality quality,
      final WXImageStrategy strategy) {
    Log.d("TAG", "setImage: " + Thread.currentThread().getName());
    WXSDKManager.getInstance().postOnUiThread(new Runnable() {

      @Override public void run() {
        if (view == null || view.getLayoutParams() == null) {
          return;
        }
        if (TextUtils.isEmpty(url)) {
          view.setImageBitmap(null);
          return;
        }
        String temp = url;
        if (url.startsWith("//")) {
          temp = "http:" + url;
        }
        if (view.getLayoutParams().width <= 0 || view.getLayoutParams().height <= 0) {
          return;
        }

        Glide.with(WXEnvironment.getApplication())
            .load(temp)
            .asBitmap()
            .placeholder(cn.qingchengfit.widgets.R.drawable.img_loadingimage)
            .error(cn.qingchengfit.widgets.R.drawable.img_loadingimage)
            .into(view);
      }
    }, 0);
  }
}