package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;



import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.responese.BalanceNotify;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/2/26.
 */

public class FixNotifySettingFragment extends BaseDialogFragment implements FixNotifyPresenter.OnGetBalanceNotifyListener {

    private static int id;
	Toolbar toolbar;
	TextView toolbarTitle;
	SwitcherLayout swBalanceEnough;
	TextView textCheckSignal;
	TextView textOpenNotifyTips;
    @Inject FixNotifyPresenter fixNotifyPresenter;
    @Inject StaffRespository restRepository;
    private HashMap<String, Object> balanceMap = new HashMap<>();
    private List<HashMap<String, Object>> balanceList = new ArrayList<>();

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static FixNotifySettingFragment newInstance() {
        Bundle args = new Bundle();
        FixNotifySettingFragment fragment = new FixNotifySettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.QcAppTheme);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify_setting, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
      swBalanceEnough = (SwitcherLayout) view.findViewById(R.id.sw_balance_enough);
      textCheckSignal = (TextView) view.findViewById(R.id.text_check_signal);
      textOpenNotifyTips = (TextView) view.findViewById(R.id.text_open_notify_tips);
      view.findViewById(R.id.setting_fix_checkin).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OnClick();
        }
      });
      delegatePresenter(fixNotifyPresenter, this);

        initToolbar();
        getData();
        initView();
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void getData() {
        fixNotifyPresenter.setOnGetBalanceNotifyListener(this);
        fixNotifyPresenter.queryBalanceNotify(App.staffId);
    }

    private void initToolbar() {
        toolbarTitle.setText("消息推送设置");
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dismiss();
            }
        });
        initToolbarPadding(toolbar);
    }

    private void initView() {
        swBalanceEnough.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swBalanceEnough.setOpen(b);
                confirm(b);
            }
        });
    }

    private void confirm(boolean b) {
        balanceMap.put("id", id);
        balanceMap.put("value", swBalanceEnough.isOpen());
        balanceList.add(balanceMap);
        CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
        configsBean.setId(id);
        configsBean.setValue(b ? 1 : 0);
        fixNotifyPresenter.putBalanceNotify(App.staffId, configsBean);
    }

 public void OnClick() {
        FixCheckinFragment.start(this, 4);
    }

    @Override public void onGetSuccess(List<BalanceNotify> balanceNotify) {
        for (BalanceNotify notify : balanceNotify) {
            id = notify.getId();
            swBalanceEnough.setOpen(notify.isValue());
        }
    }

    @Override public void onGetFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onDone() {

    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(int e) {

    }

    @Override public void showAlert(String s) {

    }

    @Override public void showAlert(int s) {

    }

    @Override public void showSelectSheet(String title, List<String> strs,
        AdapterView.OnItemClickListener listener) {

    }

    @Override public void popBack() {

    }

    @Override public void popBack(int count) {

    }
}
