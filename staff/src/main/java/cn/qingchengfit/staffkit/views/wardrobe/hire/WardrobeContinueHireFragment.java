package cn.qingchengfit.staffkit.views.wardrobe.hire;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobeBaseInfoFragment;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobePayBottomFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.PayWardrobeItem;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
 * Created by Paper on 16/9/6.
 */
public class WardrobeContinueHireFragment extends BaseFragment implements WardrobeContinueHirePresenter.MVPView {

    @BindView(R.id.name) TextView name;
    @BindView(R.id.region) TextView region;
    @BindView(R.id.card_id) TextView cardId;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.period) CommonInputView period;
    @BindView(R.id.day_count) TextView dayCount;
    @BindView(R.id.cv_cost) CommonInputView cvCost;
    @Inject WardrobeContinueHirePresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private Locker mLocker;
    private TimeDialogWindow pwTime;
    private int mPayMode = -1;
    private Card mChooseCard;

    public static WardrobeContinueHireFragment newInstance(Locker l) {

        Bundle args = new Bundle();
        args.putParcelable("l", l);
        WardrobeContinueHireFragment fragment = new WardrobeContinueHireFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_continue_hire, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(mPresenter, this);
        name.setText(mLocker.name);
        region.setText(mLocker.region.name);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_baseinfo, WardrobeBaseInfoFragment.newInstance(mLocker)).commit();
        int daycount = DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(mLocker.end));
        //        if (daycount <= 0) {//已过期
        //            period.setLabel(DateUtils.Date2YYYYMMDD(new Date(new Date().getTime() + DateUtils.DAY_TIME)) + "至");
        //        } else {//未过期
        period.setLabel(
            DateUtils.Date2YYYYMMDD(new Date(DateUtils.formatDateFromServer(mLocker.end).getTime() + DateUtils.DAY_TIME)) + "  至");
        //        }
        RxBusAdd(PayWardrobeItem.class).subscribe(new Action1<PayWardrobeItem>() {
            @Override public void call(PayWardrobeItem payWardrobeItem) {
                mPayMode = payWardrobeItem.getPay_mode();
                mChooseCard = payWardrobeItem.getCard();
                balance.setVisibility(View.GONE);
                switch (mPayMode) {
                    case 1:
                        if (mChooseCard != null) {
                            cardId.setText(mChooseCard.getName() + "(" + mChooseCard.getId() + ")");
                            balance.setVisibility(View.VISIBLE);
                            balance.setText(mChooseCard.getBalance() + StringUtils.getUnit(getContext(), mChooseCard.getType()));
                            cvCost.setLabel(mChooseCard.getType() == Configs.CATEGORY_VALUE ? "金额(元)" : "金额(次)");
                        }

                        break;
                    case 2:
                        cardId.setText("现金支付");
                        break;
                    case 4:
                        cardId.setText("银行卡支付");
                        break;
                    case 5:
                        cardId.setText("转账支付");
                        break;
                    case 6:
                        cardId.setText("其他支付");
                        break;
                }
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("续租");
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //back
    }

    @Override public String getFragmentName() {
        return WardrobeContinueHireFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.layout_pay_method, R.id.btn_comfirm, R.id.period }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_pay_method:
                WardrobePayBottomFragment.newInstance(mLocker.user.getId(), true).show(getFragmentManager(), "");

                break;
            case R.id.btn_comfirm:
                showLoading();
                try {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("locker_id", mLocker.id + "");
                    body.put("user_id", mLocker.user.getId() + "");
                    if (period.isEmpty()) {
                        onShowError("请选择续租时间");
                        return;
                    } else {
                        body.put("end", period.getContent());
                    }
                    if (mPayMode > 0) {
                        body.put("deal_mode", mPayMode + "");
                        if (mChooseCard != null) body.put("card_id", mChooseCard.getId());
                        if (!cvCost.isEmpty()) body.put("cost", cvCost.getContent());
                    }
                    mPresenter.comfirContinue(App.staffId, body);
                } catch (Exception e) {

                }

                break;
            case R.id.period:
              if (pwTime == null) {
                pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
              }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (date.getTime() < DateUtils.getDayMid(
                            new Date(DateUtils.formatDateFromServer(mLocker.end).getTime() + DateUtils.DAY_TIME))) {
                            ToastUtils.show("续租时间不能小于开始时间");
                            return;
                        }
                        period.setContent(DateUtils.Date2YYYYMMDD(date));
                        mPresenter.changeDay(mLocker.end, date);
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }

    @Override public void onContinueDay(int d) {
        dayCount.setText(getString(R.string.some_day, d + 1));
    }

    @Override public void onHireOk() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
