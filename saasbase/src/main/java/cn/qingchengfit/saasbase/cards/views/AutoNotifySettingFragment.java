package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.NotifyIsOpen;
import cn.qingchengfit.saasbase.cards.presenters.ChangeAutoNotifyPresenter;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.List;
import javax.inject.Inject;
@Leaf(module = "card",path = "/autonotify/")
public class AutoNotifySettingFragment extends SaasBaseFragment
  implements ChangeAutoNotifyPresenter.OnGetNotifySettingListener {

	TextView textStoreValue;
	TextView textSecondValue;
	TextView textTimeValue;
	TextView btnSettingChange;
	Toolbar toolbar;
	TextView toolbarTitle;

  private boolean storeIsOpen;

  @Inject ChangeAutoNotifyPresenter changeAutoNotifyPresenter;
  @Inject IPermissionModel serPermisAction;
  private boolean secondIsOpen;
  private boolean timeIsOpen;
  private String storeValue;
  private String secondValue;
  private String timeValue;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_auto_notify, container, false);
    textStoreValue = (TextView) view.findViewById(R.id.text_store_value);
    textSecondValue = (TextView) view.findViewById(R.id.text_second_value);
    textTimeValue = (TextView) view.findViewById(R.id.text_time_value);
    btnSettingChange = (TextView) view.findViewById(R.id.btn_change_setting);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

    delegatePresenter(changeAutoNotifyPresenter,this);
    changeAutoNotifyPresenter.setOnGetNotifySettingListener(this);
    changeAutoNotifyPresenter.getNotifySettingRequest();
    initView();
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("自动提醒");
  }

  private void initView() {
    btnSettingChange.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (!serPermisAction.check(PermissionServerUtils.CARDBALANCE_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        routeTo("/autonotify/edit/", null);
      }});
  }

  @Override public void onGetSuccess(List<NotifyIsOpen> notifyIsOpens) {
    for (NotifyIsOpen balanceDetail : notifyIsOpens) {
      switch (balanceDetail.key) {
        case ChangeAutoNotifyPresenter.STORE_CARD_REMINE:
          storeIsOpen = balanceDetail.value.equals("true");
          break;
        case ChangeAutoNotifyPresenter.STORE_CARD_REMINE_VALUE:
          storeValue = String.valueOf(balanceDetail.value);
          break;
        case ChangeAutoNotifyPresenter.SECOND_CARD_REMIND:
          secondIsOpen = balanceDetail.value.equals("true");
          break;
        case ChangeAutoNotifyPresenter.SECOND_CARD_REMIND_VALUE:
          secondValue = String.valueOf(balanceDetail.value);
          break;
        case ChangeAutoNotifyPresenter.TIME_CARD_REMINE:
          timeIsOpen = balanceDetail.value.equals("true");
          break;
        case ChangeAutoNotifyPresenter.TIME_CARD_REMINE_VALUE:
          timeValue = String.valueOf(balanceDetail.value);
          break;
      }
    }
    if (storeIsOpen) {
      textStoreValue.setText(" <" + storeValue + "元");
      textStoreValue.setTextColor(getResources().getColor(R.color.qc_allotsale_green));
    } else {
      textStoreValue.setText("已关闭");
      textStoreValue.setTextColor(getResources().getColor(R.color.red));
    }

    if (secondIsOpen) {
      textSecondValue.setText(" < " + secondValue + "次");
      textSecondValue.setTextColor(getResources().getColor(R.color.qc_allotsale_green));
    } else {
      textSecondValue.setText("已关闭");
      textSecondValue.setTextColor(getResources().getColor(R.color.red));
    }

    if (timeIsOpen) {
      textTimeValue.setText(" < " + timeValue + "天");
      textTimeValue.setTextColor(getResources().getColor(R.color.qc_allotsale_green));
    } else {
      textTimeValue.setText("已关闭");
      textTimeValue.setTextColor(getResources().getColor(R.color.red));
    }
  }

  @Override public void onGetFailed() {

  }

  @Override public void onSettingSuccess() {

  }

  @Override public void onSettingFailed() {

  }

  @Override public String getFragmentName() {
    return AutoNotifySettingFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}