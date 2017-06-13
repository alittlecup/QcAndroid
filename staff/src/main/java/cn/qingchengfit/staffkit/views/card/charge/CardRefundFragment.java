package cn.qingchengfit.staffkit.views.card.charge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.custom.SimpleChooseFragment;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/25 2016.
 */
public class CardRefundFragment extends BaseFragment implements CompletedChargeView {

    @BindView(R.id.deduction_money) CommonInputView deductionMoney;
    @BindView(R.id.refund_money) CommonInputView refundMoney;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.switch_name) TextView name;
    @BindView(R.id.switcher) SwitchCompat switcher;
    @BindView(R.id.switcher_layout) RelativeLayout switcherLayout;
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.extra_period) LinearLayout extraPeriod;
    @BindView(R.id.sale) CommonInputView sale;
    @BindView(R.id.mark) CommonInputView mark;
    @Inject CompletedChargePresenter presenter;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private TimeDialogWindow pwTime;
    private String shopid;

    private ChargeBody chargeBody = new ChargeBody();
    private List<Staff> mSalers;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopid = gymWrapper.shop_id();
    }

    @OnClick(R.id.comfirm) public void onComfirm() {

        if (TextUtils.isEmpty(sale.getContent())) {
            ToastUtils.show("请选择销售");
            return;
        }
        chargeBody.setShop_id(shopid);
        if (TextUtils.isEmpty(deductionMoney.getContent())) {
            ToastUtils.show("请填写" + deductionMoney.getLable());
            return;
        }

        if (TextUtils.isEmpty(refundMoney.getContent())) {
            ToastUtils.show("请填写退款金额");
            return;
        }
        chargeBody.setPrice("-" + refundMoney.getContent());
        switch (realCard.type()) {
            case Configs.CATEGORY_VALUE:
                chargeBody.setAccount("-" + deductionMoney.getContent());
                break;
            case Configs.CATEGORY_TIMES:
                chargeBody.setTimes("-" + deductionMoney.getContent());
                break;
            case Configs.CATEGORY_DATE:
                //                deductionMoney.setLabel("扣费天数(天)");
                //                extraPeriod.setVisibility(View.GONE);
                try {
                    chargeBody.setStart(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.start())));
                    chargeBody.setEnd(
                        DateUtils.minusDay(DateUtils.formatDateFromServer(realCard.end()), Integer.parseInt(deductionMoney.getContent())));
                } catch (Exception e) {

                }
                break;
        }

        if (switcher.isChecked()) {
            if (TextUtils.isEmpty(starttime.getContent())) {
                ToastUtils.show("请填写开始日期");
                return;
            }
            if (TextUtils.isEmpty(endtime.getContent())) {
                ToastUtils.show("请填写结束日期");
                return;
            }
            chargeBody.setCheck_valid(true);
            chargeBody.setValid_from(starttime.getContent());
            chargeBody.setValid_to(endtime.getContent());
        } else {
            chargeBody.setCheck_valid(false);
        }
        chargeBody.setCharge_type(1);
        chargeBody.setType(Integer.toString(Configs.TRADE_REFUND));
        presenter.commitCharge(chargeBody, StringUtils.List2Str(realCard.getRealCard().getUserIds()));
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card_refund, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        mCallbackActivity.setToolbar("会员卡扣费", false, null, 0, null);
        pwTime = new TimeDialogWindow(getContext(), Type.YEAR_MONTH_DAY);
        initView();
        return view;
    }

    private void initView() {
        switch (realCard.type()) {
            case Configs.CATEGORY_VALUE:
                break;
            case Configs.CATEGORY_TIMES:
                deductionMoney.setLabel("扣费次数(次)");
                break;
            case Configs.CATEGORY_DATE:
                deductionMoney.setLabel("扣费天数(天)");
                extraPeriod.setVisibility(View.GONE);
                break;
        }
        name.setText("设置有效期");
        deductionMoney.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                try {
                    Float b = Float.parseFloat(s.toString());
                    Float a = realCard.getRealCard().getBalance();
                    balance.setText("扣费后余额:" + (a - b));
                } catch (Exception e) {
                    balance.setText("扣费后余额:" + realCard.getRealCard().getBalance());
                }
            }
        });
        switcher.setChecked(false);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    starttime.setVisibility(View.VISIBLE);
                    endtime.setVisibility(View.VISIBLE);
                } else {
                    starttime.setVisibility(View.GONE);
                    endtime.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.switcher_layout, R.id.starttime, R.id.endtime, R.id.sale, R.id.mark }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher_layout:
                switcher.toggle();
                break;
            case R.id.starttime:
                pwTime.setRange(2000, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        starttime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.endtime:
                pwTime.setRange(2000, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        endtime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.sale:
                //                ChooseSaleFragment.start(this, 1, shopid, Configs.INIT_TYPE_ADD);
                presenter.getSalers(shopid);
                break;
            case R.id.mark:
                WriteDescFragment.start(this, 5, "备注", "填写");
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                chargeBody.setSeller_id(id);
                sale.setContent(name);
            } else if (requestCode == 5) {
                mark.setContent("已填写");
                chargeBody.setRemarks(IntentUtils.getIntentString(data));
            } else if (requestCode == 2) {
                if (mSalers != null) {
                    int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                    if (pos > 0) {
                        Staff mChosenSaler = mSalers.get(pos - 1);
                        sale.setContent(mChosenSaler.username);
                        chargeBody.setSeller_id(mChosenSaler.id);
                    } else {
                        sale.setContent("无销售");
                        chargeBody.setSeller_id(null);
                    }
                }
            }
        }
    }

    @Override public String getFragmentName() {
        return CardRefundFragment.class.getName();
    }

    @Override public void onSalers(List<Staff> salers) {
        mSalers = salers;
        ArrayList<ImageThreeTextBean> d = new ArrayList<>();
        d.add(new ImageThreeTextBean("", "无销售", "", ""));
        for (Staff saler : salers) {
            d.add(new ImageThreeTextBean(saler.avatar, saler.username, "", ""));
        }

        SimpleChooseFragment.start(this, 2, "选择销售", d);
    }

    @Override public void onSuccess() {
        ToastUtils.showS("扣费成功");
        //for (int i = 0; i < getFragmentManager().getFragments().size() - 1; i++) {
        getActivity().onBackPressed();
        //}
        //        mCallbackActivity.cleanToolbar();
        //        getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //        getFragmentManager().beginTransaction()
        //                .replace(mCallbackActivity.getFragId(), new RealCardDetailFragment())
        //                .commit();
    }

    @Override public void onFailed(String s) {

    }

    @Override public void onWechat(String url) {

    }

    @Override public void onScoreHint(String s) {

    }
}
