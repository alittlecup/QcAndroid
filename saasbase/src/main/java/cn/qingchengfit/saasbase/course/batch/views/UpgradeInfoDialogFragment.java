package cn.qingchengfit.saasbase.course.batch.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import javax.inject.Inject;

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
 * Created by Paper on 2017/1/22.
 */
public class UpgradeInfoDialogFragment extends BaseDialogFragment {

  TextView tvTitle;
  TextView tvPrice;
  Button btn;

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  String module = "";
  long timeStart = 0;

  public static UpgradeInfoDialogFragment newInstance(String module) {
    Bundle args = new Bundle();
    args.putString("module", module);
    UpgradeInfoDialogFragment fragment = new UpgradeInfoDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      this.module = getArguments().getString("module");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    getDialog().getWindow().setWindowAnimations(R.style.animate_dialog);
    View view = inflater.inflate(R.layout.fragment_upgrade_info, container, false);
    timeStart = System.currentTimeMillis()/1000;
    tvTitle = view.findViewById(R.id.tv_title);
    tvPrice = view.findViewById(R.id.tv_price);
    btn = view.findViewById(R.id.btn);
    tvPrice.setVisibility(gymWrapper.isHasFirst() ? View.VISIBLE : View.GONE);
    tvPrice.setText(Html.fromHtml(
      "首月体验价<font size=\"5\" color=\"red\">¥" + gymWrapper.getFirstPrice() + "</font>"));
    btn.setText(gymWrapper.isHasFirst() ? "立即升级" : "立即续费");
    view.findViewById(R.id.btn_close).setOnClickListener(view1 ->{
      dismiss();});
    btn.setOnClickListener(view1 -> {
      SensorsUtils.track("QcSaasRechargeWindowConfirmBtnClick")
        .addProperty("qc_saas_module_key", this.module)
        .commit(getContext());
      onLeftClick();
    });
    SensorsUtils.track("QcSaasRechargeWindowOpen")
      .addProperty("qc_saas_module_key",module)
      .commit(getContext());

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    SensorsUtils.track("QcSaasRechargeWindowClose")
      .addProperty("qc_saas_module_key",module)
      .addProperty("qc_stay_time",System.currentTimeMillis()/1000 - timeStart)
      .commit(getContext());
  }

  public void onLeftClick() {
    Intent toRenewal = new Intent(Intent.ACTION_VIEW, Uri.parse("qcstaff://bottom/"));
    toRenewal.putExtra("cn.qingchengfit.router", "renewal");
    startActivity(toRenewal);
    dismiss();
  }
}

