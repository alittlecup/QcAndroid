package cn.qingchengfit.staffkit.views.card.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.CardTypeWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.custom.BottomPayDialog;
import cn.qingchengfit.staffkit.views.custom.BottomPayDialogBuilder;
import cn.qingchengfit.staffkit.views.custom.SimpleChooseFragment;
import cn.qingchengfit.staffkit.views.gym.WriteDescFragment;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import timber.log.Timber;

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
 * Created by Paper on 16/3/23 2016.
 */
public class CompletedBuyFragment extends BaseFragment implements CompletedBuyView {
    @BindView(R.id.cardname) TextView cardname;
    @BindView(R.id.cardid) TextView cardid;
    @BindView(R.id.valid_date) TextView validDate;
    @BindView(R.id.students) TextView students;
    @BindView(R.id.realcard_num) TextView realcardNum;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.pay_money) TextView payMoney;
    @BindView(R.id.comfirm) Button comfirm;
    @BindView(R.id.sale) CommonInputView sale;
    @BindView(R.id.mark) CommonInputView mark;
    @BindView(R.id.pay_method) CommonInputView payMethod;
    @BindView(R.id.card) CardView card;
    @BindView(R.id.card_bg) LinearLayout cardBg;

    List<Staff> mSalers;
    Staff mChosenSaler;
    CreateCardBody body;
    @Inject CardTypeWrapper card_tpl;
    @Inject CompletedBuyPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BuyCardActivity) {
            body = ((BuyCardActivity) getActivity()).getBuyCardBody();
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_charge, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        cardname.setText(card_tpl.name());
        realcardNum.setText("实体卡号: " + body.card_no);
        students.setText("绑定会员: " + body.user_name);
        switch (card_tpl.type()) {
            case Configs.CATEGORY_VALUE:
                balance.setText("余额: " + body.account + "元");
                if (body.check_valid) {
                    validDate.setText(body.valid_from + "至" + body.valid_to);
                } else {
                    validDate.setText("有效期: 不限");
                }
                break;
            case Configs.CATEGORY_TIMES:
                balance.setText(String.format(Locale.CHINA, "余额: %.0f次", Float.parseFloat(body.times)));
                if (body.check_valid) {
                    validDate.setText(body.valid_from + "至" + body.valid_to);
                } else {
                    validDate.setText("有效期: 不限");
                }
                break;
            case Configs.CATEGORY_DATE:
                balance.setText(
                    (DateUtils.dayNumFromToday(DateUtils.formatDateFromYYYYMMDD(body.end)) + 1) + StringUtils.getUnit(getContext(),
                        Configs.CATEGORY_DATE));
                validDate.setText(body.start + "至" + body.end);
                break;
        }

        payMoney.setText(body.price + "元");
        body.charge_type = 7;
        CompatUtils.setBg(cardBg, ColorUtils.parseColor(card_tpl.color(), 200));
        card.setCardBackgroundColor(ColorUtils.parseColor(card_tpl.color(), 200).getColor());
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarLayout.setVisibility(View.VISIBLE);
        toolbarTitile.setText("完善信息");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.sale, R.id.mark, R.id.pay_method, R.id.comfirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sale: //销售
                presenter.getSalers();
                //                SimpleChooseFragment.start(this, 1, "选择销售", new ArrayList<ImageThreeTextBean>());
                break;
            case R.id.mark:
                WriteDescFragment.start(this, 2, "购卡备注", body == null ? "请填写备注信息" : body.remarks);
                break;
            case R.id.pay_method:
                BottomPayDialog f = new BottomPayDialogBuilder(presenter.hasEditPermission()).build();
                f.setTargetFragment(this, 3);
                f.show(getFragmentManager(), "");
                break;
            case R.id.comfirm://发送信息,生成code
                if (sale.isEmpty()) {
                    ToastUtils.show("请选择销售");
                    return;
                }
                showLoading();
                presenter.buyCard(body);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {//备注信息
                if (TextUtils.isEmpty(IntentUtils.getIntentString(data))) {
                    mark.setContent("未填写");
                } else {
                    mark.setContent("已填写");
                }
            } else if (requestCode == 1) {//选择销售
                if (mSalers != null) {
                    int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                    if (pos > 0) {
                        mChosenSaler = mSalers.get(pos - 1);
                        sale.setContent(mChosenSaler.username);
                        body.seller_id = mChosenSaler.id;
                    } else {
                        sale.setContent("无销售");
                        mChosenSaler = null;
                        body.seller_id = null;
                    }
                }
            } else if (requestCode == 3) {//付款方式
                int p = Integer.parseInt(IntentUtils.getIntentString(data));
                try {
                    payMethod.setContent(getResources().getStringArray(R.array.pay_method)[p]);
                    switch (p) {
                        case 0:
                            body.charge_type = 7;
                            break;
                        case 1:
                            body.charge_type = 6;
                            break;
                        case 2:
                            body.charge_type = 1;
                            break;
                        case 3:
                            body.charge_type = 2;
                            break;
                        case 4:
                            body.charge_type = 3;
                            break;
                        case 5:
                            body.charge_type = 4;
                            break;
                    }
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }
            } else if (requestCode == 404) {
                if (data != null) {
                    int ret = data.getIntExtra(IntentUtils.RESULT, -1);
                    if (ret == 0) {
                        presenter.cacluScore(body.price, body.user_ids);
                    } else {
                      onSuccess();
                      //ToastUtils.show("支付失败");
                    }
                }
            }
        }
    }

    @Override public String getFragmentName() {
        return CompletedBuyFragment.class.getName();
    }

    @Override public void onSalers(List<Staff> salers) {
        mSalers = salers;
        ArrayList<ImageThreeTextBean> d = new ArrayList<>();
        d.add(new ImageThreeTextBean("", "无销售", "", ""));
        for (Staff saler : salers) {
            d.add(new ImageThreeTextBean(saler.avatar, saler.username, "", ""));
        }

        SimpleChooseFragment.start(this, 1, "选择销售", d);
    }

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.showS("购卡成功");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }

    @Override public void onWxPay(String url) {

        Intent toWeb = new Intent(getContext(), WebActivity.class);
        toWeb.putExtra("url", url);
        startActivityForResult(toWeb, 404);
        hideLoading();
    }

    @Override public void onScoreHint(String s) {
        hideLoading();
        new MaterialDialog.Builder(getContext()).autoDismiss(true)
            .canceledOnTouchOutside(false)
            .title("会员卡添加成功!")
            .positiveText(R.string.common_comfirm)
            .content(getString(R.string.caclu_score_hint, s))
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    onSuccess();
                }
            })
            .show();
    }
}
