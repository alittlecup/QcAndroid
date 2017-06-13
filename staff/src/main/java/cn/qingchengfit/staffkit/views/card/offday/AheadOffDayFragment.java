package cn.qingchengfit.staffkit.views.card.offday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.common.PayMethodOfflineDialog;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.AheadOffDayBody;
import cn.qingchengfit.model.common.OffDay;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.PayEvent;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/5/5.
 */
@FragmentWithArgs public class AheadOffDayFragment extends BaseFragment implements OffDayListView {

    @Arg String offDayId;

    @BindView(R.id.tv_hint) TextView tvHint;
    @BindView(R.id.tv_origin_period) TextView tvOriginPeriod;
    @BindView(R.id.tv_after_period) TextView tvAfterPeriod;
    @BindView(R.id.civ_pay_method) CommonInputView civPayMethod;
    @BindView(R.id.civ_money) CommonInputView civMoney;
    @BindView(R.id.ep_money) ExpandedLayout epMoney;
    @BindView(R.id.layout_valid_info) LinearLayout layoutValidInfo;

    @Inject OffDayListPresenter presenter;
    @Inject RealcardWrapper realcardWrapper;

    private int pay_method;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ahead_off_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        mCallbackActivity.setToolbar("提前销假", false, null, R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                showLoading();
                AheadOffDayBody body = new AheadOffDayBody();
                if (epMoney.isExpanded()) {
                    if (pay_method == 0) {
                        ToastUtils.show("请选择支付方式");
                        return true;
                    }
                    body.charge_type = pay_method;
                    String money = civMoney.getContent();
                    try {
                        Float.parseFloat(money);
                    } catch (Exception e) {
                        ToastUtils.show("请填写正确的金额");
                        return true;
                    }
                    body.price = civMoney.getContent();
                }
                showLoading();
                presenter.aheadOffDay(offDayId, body);
                return true;
            }
        });
        onInfo();
        RxBusAdd(PayEvent.class).subscribe(new Action1<PayEvent>() {
            @Override public void call(PayEvent payEvent) {
                switch (payEvent.result) {
                    case 2:
                        pay_method = Configs.CHARGE_MODE_CASH;
                        break;
                    case 4:
                        pay_method = Configs.CHARGE_MODE_CARD;
                        break;
                    case 5:
                        pay_method = Configs.CHARGE_MODE_TRANSFER;
                        break;
                    case 6:
                        pay_method = Configs.CHARGE_MODE_OTHER;
                        break;
                }
                String m = "";
                switch (pay_method) {
                    case Configs.CHARGE_MODE_CASH:
                        m = "现金支付";
                        break;
                    case Configs.CHARGE_MODE_CARD:
                        m = "刷卡支付";
                        break;
                    case Configs.CHARGE_MODE_TRANSFER:
                        m = "转账支付";
                        break;
                    case Configs.CHARGE_MODE_OTHER:
                        m = "其他";
                        break;
                }
                civPayMethod.setContent(m);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    void onInfo() {
        String s = DateUtils.Date2YYYYMMDD(new Date());
        int intervalDay = DateUtils.interval(realcardWrapper.realCard.getLock_start(), s);

        tvHint.setText(getString(R.string.hint_ahead_day_off, s, intervalDay));
        if (TextUtils.isEmpty(realcardWrapper.realCard.getValid_from())) {
            layoutValidInfo.setVisibility(View.GONE);
            return;
        }
        tvOriginPeriod.setText(
            DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getValid_from()) + "至" + DateUtils.getYYYYMMDDfromServer(
                realcardWrapper.realCard.getValid_to()));
        try {
            if (intervalDay > 0) {
                tvAfterPeriod.setText(
                    DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getValid_from()) + "至" + DateUtils.Date2YYYYMMDD(
                        DateUtils.addDay(DateUtils.formatDateFromYYYYMMDD(realcardWrapper.realCard.getValid_to()), intervalDay)));
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    @OnClick(R.id.civ_pay_method) public void onPayMethod() {
        new PayMethodOfflineDialog().show(getChildFragmentManager(), null);
    }

    @Override public String getFragmentName() {
        return AheadOffDayFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onOffDayList(List<OffDay> offDays) {

    }

    @Override public void onSucceess() {
        hideLoading();
        ToastUtils.show("提前销假成功");
        getActivity().onBackPressed();
    }

    @Override public void onFailed(String s) {
        super.onShowError(s);
    }
}
