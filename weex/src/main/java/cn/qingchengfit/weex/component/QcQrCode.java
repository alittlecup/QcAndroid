package cn.qingchengfit.weex.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.qingchengfit.utils.MeasureUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.ref.SoftReference;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by huangbaole on 2018/3/21.
 */

public class QcQrCode extends WXComponent<ImageView> {
  WXSDKInstance instance;
  private SoftReference<Bitmap> mBitmapSoftReference;

  public QcQrCode(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
    this.instance = instance;
  }

  @Override protected ImageView initComponentHostView(@NonNull Context context) {
    ImageView imageView = new ImageView(context);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    return imageView;
  }

  @WXComponentProp(name = "value") public void setQcValue(String value) {
    try {
      mBitmapSoftReference = new SoftReference<Bitmap>(encodeAsBitmap(value));
      getHostView().setImageBitmap(mBitmapSoftReference.get());
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }
  @WXComponentProp(name = "size") public void setQcValue(Integer value) {
    ViewGroup.LayoutParams layoutParams = getHostView().getLayoutParams();
    layoutParams.height=value;
    layoutParams.width=value;
    getHostView().setLayoutParams(layoutParams);
  }

  Bitmap encodeAsBitmap(String str) throws WriterException {
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
          MeasureUtils.dpToPx(170f, instance.getContext().getResources()),
          MeasureUtils.dpToPx(170f, instance.getContext().getResources()), null);
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
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
      }
    }
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
    return bitmap;
  }
}
