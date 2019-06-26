package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.NotifyIsOpen;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.presenters.ChangeAutoNotifyPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.SwitcherLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@Leaf(module = "card",path = "/autonotify/edit/")
public class ChangeAutoNotifyFragment extends BaseFragment implements ChangeAutoNotifyPresenter.OnGetNotifySettingListener {

    private static String storeId, storeValueId;
    private static String secondId, secondValueId;
    private static String timeId, timeValueId;
	SwitcherLayout swStoreNotify;
	SwitcherLayout swSecondNotify;
	SwitcherLayout swTimeNotify;
	EditText textStoreValue;
	EditText textSecondValue;
	EditText textTimeValue;
	ViewGroup llStore;
	ViewGroup llSecond;
	ViewGroup llTime;
	Toolbar toolbar;
	TextView toolbarTitle;

    @Inject ChangeAutoNotifyPresenter changeAutoNotifyPresenter;
    private List<CardBalanceNotifyBody.ConfigsBean> configsBeanList = new ArrayList<>();
    private CardBalanceNotifyBody.ConfigsBean configsBean;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saas_change_notify, container, false);
      swStoreNotify = (SwitcherLayout) view.findViewById(R.id.sw_store_notify);
      swSecondNotify = (SwitcherLayout) view.findViewById(R.id.sw_second_notify);
      swTimeNotify = (SwitcherLayout) view.findViewById(R.id.sw_time_notify);
      textStoreValue = (EditText) view.findViewById(R.id.text_store_condition);
      textSecondValue = (EditText) view.findViewById(R.id.text_second_condition);
      textTimeValue = (EditText) view.findViewById(R.id.text_time_condition);
      llStore = (ViewGroup) view.findViewById(R.id.ll_store_condition);
      llSecond = (ViewGroup) view.findViewById(R.id.ll_second);
      llTime = (ViewGroup) view.findViewById(R.id.ll_time_condition);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

      delegatePresenter(changeAutoNotifyPresenter,this);
        initToolbar(toolbar);
        initData();
        initView();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitle.setText("修改自动提醒设置");
        toolbar.inflateMenu(R.menu.menu_save);
        RxMenuItem.clicks(toolbar.getMenu().getItem(0))
          .throttleFirst(500, TimeUnit.MILLISECONDS)
          .subscribe(new BusSubscribe<Void>() {
              @Override public void onNext(Void aVoid) {
                  if (saveSetting()) changeAutoNotifyPresenter.putBalanceRemindCondition(configsBeanList);
              }
          });

    }

    private void initView() {
        swStoreNotify.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swStoreNotify.setOpen(b);
                configsBean = new CardBalanceNotifyBody.ConfigsBean();
                configsBean.setId(storeId);
                configsBean.setValue(b ? 1 : 0);
                configsBeanList.add(configsBean);
                refreshView();
            }
        });
        swSecondNotify.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swSecondNotify.setOpen(b);
                configsBean = new CardBalanceNotifyBody.ConfigsBean();
                configsBean.setId(secondId);
                configsBean.setValue(b ? 1 : 0);
                configsBeanList.add(configsBean);
                refreshView();
            }
        });
        swTimeNotify.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swTimeNotify.setOpen(b);
                configsBean = new CardBalanceNotifyBody.ConfigsBean();
                configsBean.setId(timeId);
                configsBean.setValue(b ? 1 : 0);
                configsBeanList.add(configsBean);
                refreshView();
            }
        });
    }

    private boolean saveSetting() {
        if (swStoreNotify.isOpen() && TextUtils.isEmpty(textStoreValue.getText())) {
            DialogUtils.showAlert(getContext(), "请填写储值卡余额小于多少元");
            return false;
        }
        if (swStoreNotify.isOpen() && Integer.parseInt(textStoreValue.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "储值卡余额不可小于0元");
            return false;
        }
        if (swSecondNotify.isOpen() && TextUtils.isEmpty(textSecondValue.getText())) {
            DialogUtils.showAlert(getContext(), "请填写次卡余额小于多少次");
            return false;
        }
        if (swSecondNotify.isOpen() && Integer.parseInt(textSecondValue.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "次卡余额不可小于0次");
            return false;
        }
        if (swTimeNotify.isOpen() && TextUtils.isEmpty(textTimeValue.getText())) {
            DialogUtils.showAlert(getContext(), "请填写剩余有效期少于多少天");
            return false;
        }
        if (swTimeNotify.isOpen() && Integer.parseInt(textTimeValue.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "剩余有效期不可小于0天");
            return false;
        }
        if (swStoreNotify.isOpen() && !TextUtils.isEmpty(textStoreValue.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId(storeValueId);
            configsBean.setValue(Integer.valueOf(textStoreValue.getText().toString()));
            configsBeanList.add(configsBean);
        }
        if (swSecondNotify.isOpen() && !TextUtils.isEmpty(textSecondValue.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId(secondValueId);
            configsBean.setValue(Integer.valueOf(textSecondValue.getText().toString()));
            configsBeanList.add(configsBean);
        }
        if (swTimeNotify.isOpen() && !TextUtils.isEmpty(textTimeValue.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId(timeValueId);
            configsBean.setValue(Integer.valueOf(textTimeValue.getText().toString()));
            configsBeanList.add(configsBean);
        }
        return true;
    }

    private void refreshView() {
        llStore.setVisibility(swStoreNotify.isOpen() ? View.VISIBLE : View.GONE);
        llSecond.setVisibility(swSecondNotify.isOpen() ? View.VISIBLE : View.GONE);
        llTime.setVisibility(swTimeNotify.isOpen() ? View.VISIBLE : View.GONE);
    }

    private void initData() {
        changeAutoNotifyPresenter.setOnGetNotifySettingListener(this);
        changeAutoNotifyPresenter.getNotifySettingRequest();
    }

    @Override public String getFragmentName() {
        return ChangeAutoNotifyFragment.class.getName();
    }

    @Override public void onGetSuccess(List<NotifyIsOpen> notifyIsOpens) {
        for (NotifyIsOpen balanceDetail : notifyIsOpens) {
            switch (balanceDetail.key) {
                case ChangeAutoNotifyPresenter.STORE_CARD_REMINE:
                    swStoreNotify.setOpen(balanceDetail.value.equals("true"));
                    storeId = balanceDetail.id;
                    break;
                case ChangeAutoNotifyPresenter.STORE_CARD_REMINE_VALUE:
                    if (swStoreNotify.isOpen()) {
                        llStore.setVisibility(View.VISIBLE);
                        textStoreValue.setText(String.valueOf(balanceDetail.value));
                    } else {
                        llStore.setVisibility(View.GONE);
                    }
                    storeValueId = balanceDetail.id;
                    break;
                case ChangeAutoNotifyPresenter.SECOND_CARD_REMIND:
                    swSecondNotify.setOpen(balanceDetail.value.equals("true"));
                    secondId = balanceDetail.id;
                    break;
                case ChangeAutoNotifyPresenter.SECOND_CARD_REMIND_VALUE:
                    if (swSecondNotify.isOpen()) {
                        llSecond.setVisibility(View.VISIBLE);
                        textSecondValue.setText(balanceDetail.value);
                    } else {
                        llSecond.setVisibility(View.GONE);
                    }
                    secondValueId = balanceDetail.id;
                    break;
                case ChangeAutoNotifyPresenter.TIME_CARD_REMINE:
                    swTimeNotify.setOpen(balanceDetail.value.equals("true"));
                    timeId = balanceDetail.id;
                    break;
                case ChangeAutoNotifyPresenter.TIME_CARD_REMINE_VALUE:
                    if (swTimeNotify.isOpen()) {
                        llTime.setVisibility(View.VISIBLE);
                        textTimeValue.setText(balanceDetail.value);
                    } else {
                        llTime.setVisibility(View.GONE);
                    }
                    timeValueId = balanceDetail.id;
                    break;
            }
        }
    }

    @Override public void onGetFailed() {
        Toast.makeText(getContext(), "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
    }

    @Override public void onSettingSuccess() {
        ToastUtils.show("设置成功");
        getActivity().onBackPressed();
    }

    @Override public void onSettingFailed() {

    }
}