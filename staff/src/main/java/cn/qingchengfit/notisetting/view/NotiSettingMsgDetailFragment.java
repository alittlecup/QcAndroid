package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.notisetting.presenter.NotiSettingMsgDetailPresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 2017/7/31.
 */
public class NotiSettingMsgDetailFragment extends BaseFragment
    implements NotiSettingMsgDetailPresenter.MVPView {

	Toolbar toolbar;
	TextView toolbarTitle;
	TextView tvCurGym;
	TextView tvCurCount;
	Button btnCharge;

  @Inject NotiSettingMsgDetailPresenter presenter;
  @Inject GymWrapper gymWrapper;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_noti_setting_msg, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tvCurGym = (TextView) view.findViewById(R.id.tv_cur_gym);
    tvCurCount = (TextView) view.findViewById(R.id.tv_cur_count);
    btnCharge = (Button) view.findViewById(R.id.btn_charge);
    view.findViewById(R.id.btn_charge).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnChargeClicked();
      }
    });
    view.findViewById(R.id.tv_msg_charge_history).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onTvMsgChargeHistoryClicked();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    presenter.queryCurSMSleft();
    tvCurGym.setText(gymWrapper.name());
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("短信");
  }

  @Override public String getFragmentName() {
    return NotiSettingMsgDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

 public void onBtnChargeClicked() {
    routeTo(new NotiSettingMsgChargeFragment());
  }

 public void onTvMsgChargeHistoryClicked() {
    routeTo(new NotiSettingChargeHistoryFragment());
  }

  @Override public void onCurSMSLeft(int x) {
    tvCurCount.setText(x + "");
  }
}
