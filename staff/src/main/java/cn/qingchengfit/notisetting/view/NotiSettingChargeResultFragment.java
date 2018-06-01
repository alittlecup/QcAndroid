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

	Toolbar toolbar;
	TextView toolbarTitle;
	ImageView imgResult;
	TextView tvResultTitle;
	TextView tvContent;
	Button btnChargeHistory;
	Button btnBack;
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
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    imgResult = (ImageView) view.findViewById(R.id.img_result);
    tvResultTitle = (TextView) view.findViewById(R.id.tv_result_title);
    tvContent = (TextView) view.findViewById(R.id.tv_content);
    btnChargeHistory = (Button) view.findViewById(R.id.btn_charge_history);
    btnBack = (Button) view.findViewById(R.id.btn_back);
    view.findViewById(R.id.btn_charge_history).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnChargeHistoryClicked();
      }
    });
    view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnBackClicked();
      }
    });

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

 public void onBtnChargeHistoryClicked() {
    if (result)
      routeTo(new NotiSettingChargeHistoryFragment());
    else getActivity().onBackPressed();
  }

 public void onBtnBackClicked() {
    getActivity().finish();
  }
}
