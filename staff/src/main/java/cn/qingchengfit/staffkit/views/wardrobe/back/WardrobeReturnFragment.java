package cn.qingchengfit.staffkit.views.wardrobe.back;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.body.ReturnWardrobeBody;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobeBaseInfoFragment;
import cn.qingchengfit.staffkit.views.wardrobe.WardrobePayBottomFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.PayWardrobeItem;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import java.util.Date;
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
 * Created by Paper on 16/8/31.
 */
public class WardrobeReturnFragment extends BaseFragment implements WardrobeReturnPresenter.MVPView {

    @BindView(R.id.return_money) CommonInputView returnMoney;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.region) TextView region;

    @Inject WardrobeReturnPresenter mPresenter;
    @BindView(R.id.expand_layout) ExpandedLayout expandLayout;
    @BindView(R.id.card_id) TextView cardId;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.layout_pay_method) LinearLayout layoutPayMethod;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private Locker mLocker;
    private int mPayMode;
    private Card mChooseCard;

    public static WardrobeReturnFragment newInstance(Locker l) {

        Bundle args = new Bundle();
        args.putParcelable("l", l);
        WardrobeReturnFragment fragment = new WardrobeReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocker = getArguments().getParcelable("l");
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_return, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(mPresenter, this);

        name.setText(mLocker.name);
        region.setText(mLocker.region.name);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_baseinfo, WardrobeBaseInfoFragment.newInstance(mLocker)).commit();
        if (DateUtils.interval(new Date(), DateUtils.formatDateFromServer(mLocker.end)) < 0) {
            expandLayout.setLabel("是否收费");
        } else {
            expandLayout.setLabel("是否退费");
        }
        RxBusAdd(PayWardrobeItem.class).subscribe(new Action1<PayWardrobeItem>() {
            @Override public void call(PayWardrobeItem payWardrobeItem) {
                mPayMode = payWardrobeItem.getPay_mode();
                mChooseCard = payWardrobeItem.getCard();
                returnMoney.setVisibility(View.VISIBLE);
                returnMoney.setLabel("金额(元)");
                balance.setVisibility(View.GONE);
                switch (mPayMode) {
                    case 1:
                        if (mChooseCard != null) {
                            cardId.setText(mChooseCard.getName() + "(" + mChooseCard.getId() + ")");
                            balance.setVisibility(View.VISIBLE);
                            balance.setText(mChooseCard.getBalance() + StringUtils.getUnit(getContext(), mChooseCard.getType()));

                            if (mChooseCard.getType() == Configs.CATEGORY_VALUE) {

                            } else if (mChooseCard.getType() == Configs.CATEGORY_TIMES) {
                                returnMoney.setLabel("次数");
                            } else if (mChooseCard.getType() == Configs.CATEGORY_DATE) {
                                returnMoney.setVisibility(View.GONE);
                            }
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
        toolbarTitile.setText("归还更衣柜");
    }

    @Override public String getFragmentName() {
        return WardrobeReturnFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.comfirm) public void onClick() {
        if (expandLayout.isExpanded()) {
            if (mPayMode <= 0 || (mPayMode == 1 && mChooseCard == null) || (returnMoney.getVisibility() == View.VISIBLE
                && returnMoney.isEmpty())) {
                onShowError("请填写退款信息");
                return;
            }
            showLoading();
            mPresenter.backLong(App.staffId, new ReturnWardrobeBody.Builder().is_long_term_borrow(true)
                .locker_id(mLocker.id)
                .card_id(mChooseCard == null ? null : mChooseCard.getId())
                .cost(returnMoney.getVisibility() == View.VISIBLE ? returnMoney.getContent() : null)
                .deal_mode(mPayMode)
                .build());
        } else {
            showLoading();
            mPresenter.backLong(App.staffId, new ReturnWardrobeBody.Builder().is_long_term_borrow(true).locker_id(mLocker.id)
                //                    .card_id(mChooseCard == null ? null : mChooseCard.getId())
                //                    .cost(returnMoney.getVisibility() == View.VISIBLE ? returnMoney.getContent() : null)
                //                    .deal_mode(mPayMode)
                .build());
        }
    }

    @OnClick(R.id.pay_method) public void chooseMethod() {
        WardrobePayBottomFragment.newInstance(mLocker.user.getId(),
            DateUtils.interval(new Date(), DateUtils.formatDateFromServer(mLocker.end)) < 0).show(getFragmentManager(), "");
    }

    @Override public void onReturnSuccess() {
        hideLoading();
        ToastUtils.show("退还更衣柜成功");
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
