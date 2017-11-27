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
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
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

    private static int storeId, storeValueId;
    private static int secondId, secondValueId;
    private static int timeId, timeValueId;
    @BindView(R2.id.sw_store_notify) SwitcherLayout swStoreNotify;
    @BindView(R2.id.sw_second_notify) SwitcherLayout swSecondNotify;
    @BindView(R2.id.sw_time_notify) SwitcherLayout swTimeNotify;
    @BindView(R2.id.text_store_condition) EditText textStoreValue;
    @BindView(R2.id.text_second_condition) EditText textSecondValue;
    @BindView(R2.id.text_time_condition) EditText textTimeValue;
    @BindView(R2.id.ll_store_condition) ViewGroup llStore;
    @BindView(R2.id.ll_second) ViewGroup llSecond;
    @BindView(R2.id.ll_time_condition) ViewGroup llTime;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitle;

    @Inject ChangeAutoNotifyPresenter changeAutoNotifyPresenter;
    private String storeValue;
    private String secondValue;
    private String timeValue;
    private List<CardBalanceNotifyBody.ConfigsBean> configsBeanList = new ArrayList<>();
    private CardBalanceNotifyBody.ConfigsBean configsBean;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saas_change_notify, container, false);
        unbinder = ButterKnife.bind(this, view);
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
                        storeValue = balanceDetail.value;
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
                        secondValue = balanceDetail.value;
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
                        timeValue = balanceDetail.value;
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