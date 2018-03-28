package cn.qingchengfit.weex.component;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ViewGroup;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.ref.SoftReference;

/**
 * Created by huangbaole on 2018/3/21.
 */

public class QcQrCode extends WXImage {
  private SoftReference<Bitmap> mBitmapSoftReference;

  public QcQrCode(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }

  private int backgroundColor = Color.WHITE;

  @WXComponentProp(name = "background") public void setQcbackground(String value) {
    int i = Color.parseColor(value);
    backgroundColor = i;
  }

  private int foregroundColor = Color.BLACK;

  @WXComponentProp(name = "foreground") public void setQcforeground(String value) {
    int i = Color.parseColor(value);
    foregroundColor = i;
    try {
      mBitmapSoftReference = new SoftReference<>(encodeAsBitmap(value, size, size));
      getHostView().setImageBitmap(mBitmapSoftReference.get());
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

  @WXComponentProp(name = "value") public void setQcValue(String value) {
    try {
      mBitmapSoftReference = new SoftReference<>(encodeAsBitmap(value, size, size));
      getHostView().setImageBitmap(mBitmapSoftReference.get());
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

  private int size = 500;

  @WXComponentProp(name = "size") public void setQcSize(String str) {
    int value = Integer.valueOf(str);
    ViewGroup.LayoutParams layoutParams = getHostView().getLayoutParams();
    layoutParams.width = value;
    layoutParams.height = value;
    getHostView().setLayoutParams(layoutParams);
    this.size = value;
  }

  Bitmap encodeAsBitmap(String str, int width, int height) throws WriterException {
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, null);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int w = result.getWidth();
    int h = result.getHeight();
    int[] pixels = new int[w * h];
    for (int y = 0; y < h; y++) {
      int offset = y * w;
      for (int x = 0; x < w; x++) {
        pixels[offset + x] = result.get(x, y) ? foregroundColor : backgroundColor;
      }
    }
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
    return bitmap;
  }
}
