package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.VpFragment;
import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;

/**
 * Created by fb on 2017/9/27.
 */

public class SignInChildCodeFragment extends VpFragment implements TitleFragment {

  @BindView(R.id.tv_code_sign) TextView tvCodeSign;
  @BindView(R.id.tv_sign_introduction) TextView tvSignIntroduction;
  @BindView(R.id.img_sign_code) ImageView imgSignCode;
  Unbinder unbinder;
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
    unbinder = ButterKnife.bind(this, view);
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
    unbinder.unbind();
  }

  @Override public String getTitle() {
    return isSignIn ? "店内签到二维码" : "店内签出二维码";
  }
}
