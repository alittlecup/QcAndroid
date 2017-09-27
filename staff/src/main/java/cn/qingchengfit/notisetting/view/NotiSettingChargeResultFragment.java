package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;

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
 * Created by Paper on 2017/8/2.
 */
public class NotiSettingChargeResultFragment extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.img_result) ImageView imgResult;
  @BindView(R.id.tv_result_title) TextView tvResultTitle;
  @BindView(R.id.tv_content) TextView tvContent;
  @BindView(R.id.btn_charge_history) Button btnChargeHistory;
  @BindView(R.id.btn_back) Button btnBack;
  private boolean result = false;

  public static NotiSettingChargeResultFragment newInstanceSuccess(int count) {
    Bundle args = new Bundle();
    args.putBoolean("r", true);
    args.putInt("c", count);
    NotiSettingChargeResultFragment fragment = new NotiSettingChargeResultFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static NotiSettingChargeResultFragment newInstanceFailed() {
    Bundle args = new Bundle();
    args.putBoolean("r", false);
    NotiSettingChargeResultFragment fragment = new NotiSettingChargeResultFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    result = getArguments().getBoolean("r", false);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_charge_sms_success, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    if (result) {

      imgResult.setImageResource(R.drawable.vd_result_success);
      tvResultTitle.setText("充值成功");
      tvContent.setText(getString(R.string.left_sms_count, getArguments().getInt("c", 0)));
    } else {
      imgResult.setImageResource(R.drawable.vd_result_fail);
      tvResultTitle.setText("充值失败");
      tvContent.setText(getString(R.string.contact_gm));
      btnChargeHistory.setText("返回重试");
      btnBack.setVisibility(View.GONE);
    }
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("短信充值");
  }

  @Override public String getFragmentName() {
    return NotiSettingChargeResultFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.btn_charge_history) public void onBtnChargeHistoryClicked() {
    if (result)
      routeTo(new NotiSettingChargeHistoryFragment());
    else getActivity().onBackPressed();
  }

  @OnClick(R.id.btn_back) public void onBtnBackClicked() {
    getActivity().finish();
  }
}
