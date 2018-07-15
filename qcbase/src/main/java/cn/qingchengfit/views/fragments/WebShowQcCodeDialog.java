package cn.qingchengfit.views.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.lang.ref.SoftReference;

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
 * Created by Paper on 2017/4/28.
 */
public class WebShowQcCodeDialog extends DialogFragment {
  String url;
  String title;
  String content;
	TextView tvGymName;
	ImageView imgQrCode;
	TextView tvTitle;

  private SoftReference<Bitmap> mBitmapSoftReference;

  public static WebShowQcCodeDialog newInstance(String url,String title ,String content) {
    Bundle args = new Bundle();
    WebShowQcCodeDialog fragment = new WebShowQcCodeDialog();
    args.putString("url",url);
    args.putString("title",title);
    args.putString("content",content);
    fragment.setArguments(args);
    return fragment;
  }


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    url = getArguments().getString("url");
    title = getArguments().getString("title");
    content = getArguments().getString("content");
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home_page_qrcode, container, false);
    tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
    imgQrCode = (ImageView) view.findViewById(R.id.img_qr_code);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnCloseClicked();
      }
    });
    view.findViewById(R.id.btn_save_qrcode).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnSaveQrcodeClicked();
      }
    });
    view.findViewById(R.id.root_view).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onRootViewClicked();
      }
    });

    tvTitle.setText(title);
    tvGymName.setText(content);
    tvGymName.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
    try {
      mBitmapSoftReference = new SoftReference<Bitmap>(encodeAsBitmap(url));
      imgQrCode.setImageBitmap(mBitmapSoftReference.get());
    } catch (Exception e) {

    }
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
      }
    });
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

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

 public void onBtnCloseClicked() {
    dismissAllowingStateLoss();
  }

 public void onBtnSaveQrcodeClicked() {
    MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
        mBitmapSoftReference.get(), title, title);
    ToastUtils.show("二维码已保存");
  }

 public void onRootViewClicked() {
    getActivity().onBackPressed();
  }
}

