package cn.qingchengfit.staffkit.views.card.cardlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.NotifyIsOpen;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/2/25.
 */

public class AutoNotifySettingFragment extends BaseFragment implements ChangeAutoNotifyPresenter.OnGetNotifySettingListener {

    @BindView(R.id.text_store_value) TextView textStoreValue;
    @BindView(R.id.text_second_value) TextView textSecondValue;
    @BindView(R.id.text_time_value) TextView textTimeValue;
    @BindView(R.id.btn_change_setting) TextView btnSettingChange;

    @Inject ChangeAutoNotifyPresenter changeAutoNotifyPresenter;
    @Inject SerPermisAction serPermisAction;
    private boolean storeIsOpen;
    private boolean secondIsOpen;
    private boolean timeIsOpen;
    private String storeValue;
    private String secondValue;
    private String timeValue;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_notify, container, false);
        unbinder = ButterKnife.bind(this, view);
        changeAutoNotifyPresenter.setOnGetNotifySettingListener(this);
        changeAutoNotifyPresenter.getNotifySettingRequest();
        initView();
        return view;
    }

    private void initView() {
        mCallbackActivity.setToolbar("自动提醒", false, null, 0, null);
        btnSettingChange.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!serPermisAction.check(PermissionServerUtils.CARDBALANCE_CAN_CHANGE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }

                getFragmentManager().beginTransaction()
                    .replace(R.id.student_frag, new ChangeAutoNotifyFragment())
                    .addToBackStack(null)
                    .commit();
            }
        });
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
}
