package cn.qingchengfit.staffkit.views.card.charge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.common.PayMethodOfflineDialog;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.UpdateCardValidBody;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.PayEvent;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Date;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.utils.DateUtils.formatDateFromYYYYMMDD;

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
public class CardFixValidDayFragment extends BaseFragment implements CardFixValidDayPresenter.MVPView {

    @BindView(R.id.btn_start) CommonInputView btnStart;
    @BindView(R.id.btn_end) CommonInputView btnEnd;
    @BindView(R.id.civ_pay_method) CommonInputView civPayMethod;
    @BindView(R.id.civ_money) CommonInputView civMoney;
    @BindView(R.id.ep_money) ExpandedLayout epMoney;
    @BindView(R.id.ep_vilid_day) ExpandedLayout epValid;

    @Inject CardFixValidDayPresenter presenter;
    @Inject RealcardWrapper realcardWrapper;
    @BindView(R.id.view_disable) View viewDisable;
    private TimeDialogWindow pwTime;
    private int pay_method;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realcard_fix_valid_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        if (realcardWrapper.realCard.getType() == Configs.CATEGORY_DATE) {
            viewDisable.setVisibility(View.VISIBLE);
        }
        mCallbackActivity.setToolbar("修改会员卡有效期", false, null, R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                UpdateCardValidBody body = new UpdateCardValidBody();
                if (epValid.isExpanded()) {
                    if (TextUtils.isEmpty(btnStart.getContent()) || TextUtils.isEmpty(btnEnd.getContent())) {
                        ToastUtils.show("请选择开始日期和结束日期");
                        return true;
                    }

                    body.check_valid = true;
                    if (formatDateFromYYYYMMDD(btnStart.getContent()).getTime() > formatDateFromYYYYMMDD(btnEnd.getContent()).getTime()) {
                        ToastUtils.show("开始日期不能小于结束日期");
                        return true;
                    }
                    if (TextUtils.isEmpty(btnStart.getContent()) || TextUtils.isEmpty(btnEnd.getContent())) {
                        ToastUtils.show("请选择开始日期与结束日期");
                        return true;
                    }
                    body.valid_from = btnStart.getContent();
                    body.valid_to = btnEnd.getContent();
                } else {
                    body.check_valid = false;
                    body.valid_from = null;
                    body.valid_to = null;
                }
                if (epMoney.isExpanded()) {
                    if (pay_method == 0) {
                        ToastUtils.show("请选择支付方式");
                        return true;
                    }
                    try {
                        Float.parseFloat(civMoney.getContent());
                    } catch (Exception e) {
                        ToastUtils.show("请填写正确金额");
                        return true;
                    }
                    body.charge_type = pay_method;
                    body.price = civMoney.getContent();
                }
                showLoading();
                presenter.updateValidDay(body);

                return true;
            }
        });

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

        onCardValidInfo();
        return view;
    }

    void onCardValidInfo() {
        epValid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                epValid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (realcardWrapper.realCard.getType() == Configs.CATEGORY_DATE) {
                    epValid.setExpanded(true);
                    btnStart.setContent(DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getStart()));
                    btnEnd.setContent(DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getEnd()));
                } else {
                    epValid.setExpanded(realcardWrapper.realCard.isCheck_valid());
                    btnStart.setContent(DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getValid_from()));
                    btnEnd.setContent(DateUtils.getYYYYMMDDfromServer(realcardWrapper.realCard.getValid_to()));
                }
            }
        });
    }

    @Override public String getFragmentName() {
        return CardFixValidDayFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.btn_start, R.id.btn_end, R.id.civ_pay_method }) public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                Date start = new Date();
                if (!TextUtils.isEmpty(btnStart.getContent())) start = DateUtils.formatDateFromYYYYMMDD(btnStart.getContent());
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        btnStart.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, start);
                break;
            case R.id.btn_end:
                Date end = new Date();
                if (!TextUtils.isEmpty(btnEnd.getContent())) end = formatDateFromYYYYMMDD(btnEnd.getContent());
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        btnEnd.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, end);
                break;
            case R.id.civ_pay_method:
                new PayMethodOfflineDialog().show(getChildFragmentManager(), null);
                break;
        }
    }

    @Override public void onOk() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.view_disable) public void onViewClicked() {
        ToastUtils.show("期限卡无法关闭有效期");
    }
}
