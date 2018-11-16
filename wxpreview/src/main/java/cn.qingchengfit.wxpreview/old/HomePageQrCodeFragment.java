package cn.qingchengfit.wxpreview.old;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.wxpreview.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.lang.ref.SoftReference;
import javax.inject.Inject;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/1/21.
 */
public class HomePageQrCodeFragment extends SaasCommonFragment {

  public String mUrl;
  ImageView imgQrCode;
  TextView tvGymName;
  TextView btnSaveQrcode;
  @Inject GymWrapper gymWrapper;

  private SoftReference<Bitmap> mBitmapSoftReference;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home_page_qrcode, container, false);
    imgQrCode = (ImageView) view.findViewById(R.id.img_qr_code);
    tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
    btnSaveQrcode = (TextView) view.findViewById(R.id.btn_save_qrcode);
    if (getArguments() != null) {
      mUrl = getArguments().getString("mUrl");
    }
    view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        HomePageQrCodeFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_save_qrcode).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        HomePageQrCodeFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.root_view).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        HomePageQrCodeFragment.this.onClick(v);
      }
    });

    tvGymName.setText(gymWrapper.name());
    try {
      mBitmapSoftReference = new SoftReference<Bitmap>(encodeAsBitmap(mUrl));
      imgQrCode.setImageBitmap(mBitmapSoftReference.get());
    } catch (WriterException e) {
    }

    return view;
  }

  @Override public String getFragmentName() {
    return HomePageQrCodeFragment.class.getName();
  }

  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.btn_close) {
      getActivity().onBackPressed();
    } else if (i == R.id.btn_save_qrcode) {
      String imgPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
          mBitmapSoftReference.get(), gymWrapper.name(), "微信二维码");
      if (!TextUtils.isEmpty(imgPath)) {
        ToastUtils.show("保存成功");
      }
    } else if (i == R.id.root_view) {
      getActivity().onBackPressed();
    }
  }

  Bitmap encodeAsBitmap(String str) throws WriterException {
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
          MeasureUtils.dpToPx(170f, getResources()), MeasureUtils.dpToPx(170f, getResources()),
          null);
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
