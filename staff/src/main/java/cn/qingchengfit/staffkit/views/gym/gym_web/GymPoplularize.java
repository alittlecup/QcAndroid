package cn.qingchengfit.staffkit.views.gym.gym_web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventPoplularize;
import cn.qingchengfit.views.fragments.ShareDialogFragment;

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
 * Created by Paper on 2017/1/18.
 */

public class GymPoplularize extends ShareDialogFragment {

  private TextView btnToWX;

  public static GymPoplularize newInstance(String title, String text, String img, String url,
      boolean success) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putString("img", img);
    args.putString("url", url);
    args.putBoolean("s", success);
    GymPoplularize fragment = new GymPoplularize();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public int getLayoutRes() {
    return R.layout.fragment_share_popularize;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this,view);
    btnToWX = (TextView) view.findViewById(R.id.connect_public);
    btnToWX.setText(getArguments().getBoolean("s", false) ? "已对接" : "未对接");

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.btn_to_wechat_public) public void onBtnToWechatPublicClicked() {
    RxBus.getBus().post(new EventPoplularize(R.id.btn_to_wechat_public));
    dismiss();
  }

  @OnClick(R.id.btn_home_qr) public void onBtnHomeQrClicked() {
    RxBus.getBus().post(new EventPoplularize(R.id.btn_home_qr));
    dismiss();
  }

  @OnClick(R.id.btn_more_popularize) public void onBtnMorePopularizeClicked() {
    RxBus.getBus().post(new EventPoplularize(R.id.btn_more_popularize));
    dismiss();
  }
}
