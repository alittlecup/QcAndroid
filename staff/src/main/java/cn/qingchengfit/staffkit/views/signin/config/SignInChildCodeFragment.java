package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGContents;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGEncoder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.VpFragment;
import com.google.zxing.WriterException;

/**
 * Created by fb on 2017/9/27.
 */

public class SignInChildCodeFragment extends VpFragment implements TitleFragment {

	TextView tvCodeSign;
	TextView tvSignIntroduction;
	ImageView imgSignCode;

  private String url;
  private boolean isSignIn;

  public static SignInChildCodeFragment newInstance(String url, boolean isSignIn) {
    Bundle args = new Bundle();
    args.putString("img_url", url);
    args.putBoolean("sign", isSignIn);
    SignInChildCodeFragment fragment = new SignInChildCodeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      url = getArguments().getString("img_url");
      isSignIn = getArguments().getBoolean("sign");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_code_sign_in, container, false);
    tvCodeSign = (TextView) view.findViewById(R.id.tv_code_sign);
    tvSignIntroduction = (TextView) view.findViewById(R.id.tv_sign_introduction);
    imgSignCode = (ImageView) view.findViewById(R.id.img_sign_code);

    QRGEncoder qrgEncoder =
        new QRGEncoder(url, null, QRGContents.Type.TEXT, MeasureUtils.dpToPx(180f, getResources()));
    try {
      imgSignCode.setImageBitmap(qrgEncoder.encodeAsBitmap());
    } catch (WriterException e) {
      e.printStackTrace();
    }
    tvCodeSign.setText(isSignIn ? "签到" : "签出");
    tvSignIntroduction.setText(
        isSignIn ? getResources().getString(R.string.code_sign_in_introduction)
            : getResources().getString(R.string.code_sign_out_introduction));
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

  }

  @Override public String getTitle() {
    return isSignIn ? "店内签到二维码" : "店内签出二维码";
  }
}
