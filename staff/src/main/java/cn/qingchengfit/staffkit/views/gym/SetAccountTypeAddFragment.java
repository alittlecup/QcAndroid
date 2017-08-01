package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.OnlineLimit;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.DoneAccountEvent;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.rxbus.event.EventMutiChoose;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.OnlineLimitFragment;
import cn.qingchengfit.staffkit.views.batch.BatchActivity;
import cn.qingchengfit.staffkit.views.custom.ClubCardView;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Utils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/2/1 2016.
 */
public class SetAccountTypeAddFragment extends BaseFragment {

    @BindView(R.id.order_sutdent_count) CommonInputView orderSutdentCount;
    @BindView(R.id.add_card_type) TextView addCardType;
    @BindView(R.id.cardtype_list) LinearLayout cardtypeList;
    @BindView(R.id.pay_online_money) CommonInputView payOnlineMoney;
    @BindView(R.id.lable) TextView lable;
    @BindView(R.id.switcher_limit) SwitchCompat switcher;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.layout_limit_peop) RelativeLayout layoutLimitPeop;
    @BindView(R.id.limit_num) CommonInputView limitNum;
    @BindView(R.id.limit_who) CommonInputView limitWho;
    @BindView(R.id.pay_online) ExpandedLayout payOnline;
    @Inject GymUseCase useCase;
    @Inject CoachService coachService;
    @Inject RestRepository restRepository;
    @BindView(R.id.sw_need_pay) SwitchCompat swNeedPay;
    @BindView(R.id.layout_need_pay) LinearLayout layoutNeedPay;
    @BindView(R.id.layout_pay) LinearLayout layoutPay;
    @BindView(R.id.can_not_close) View canNotClose;
    @BindView(R.id.tag_pro) ImageView tagPro;
    private DialogList stucount; // 选择学员数量
    private SystemInitBody body;
    private int mType;//课程种类
    private List<CardTpl> card_tpls;
    private Subscription sp;
    private List<Rule> rules = new ArrayList<>();
    private int mMaxUer;
    private HashMap<String, HashMap<Integer, String>> cardCost = new HashMap();
    private boolean isFree = false;
    private List<String> mCannotChangeCarttpl = new ArrayList<>();
    /**
     * 是否已经有预约，如果已经有预约  则不能修改为无需支付
     */
    private boolean mHasOrder = false;
    /**
     * 用来记录在线支付的约课限制
     */
    private ArrayList<Integer> mLimit = new ArrayList<>();

    private ArrayList<CardTplBatchShip> mCardtplShip;
    private MaterialDialog cancleDialog;

    public static SetAccountTypeAddFragment newInstance(int mType, ArrayList<Rule> rules, int max_user, boolean isFree, boolean hasOder) {
        Bundle args = new Bundle();
        SetAccountTypeAddFragment fragment = new SetAccountTypeAddFragment();
        args.putInt("type", mType);
        args.putInt("max_user", max_user);
        args.putParcelableArrayList("rules", rules);
        args.putBoolean("isFree", isFree);
        args.putBoolean("hasOrder", hasOder);

        fragment.setArguments(args);
        return fragment;
    }

    public static SetAccountTypeAddFragment newInstance(int mType, ArrayList<Rule> rules, int max_user, boolean isFree, boolean hasOder,
        List<CardTplBatchShip> ships) {
        Bundle args = new Bundle();
        SetAccountTypeAddFragment fragment = new SetAccountTypeAddFragment();
        args.putInt("type", mType);
        args.putInt("max_user", max_user);
        args.putParcelableArrayList("rules", rules);
        args.putBoolean("isFree", isFree);
        args.putBoolean("hasOrder", hasOder);
        if (ships != null) args.putParcelableArrayList("ships", (ArrayList<CardTplBatchShip>) ships);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            rules = getArguments().getParcelableArrayList("rules");
            mMaxUer = getArguments().getInt("max_user");
            isFree = getArguments().getBoolean("isFree", false);
            mHasOrder = getArguments().getBoolean("hasOrder", false);
            mCardtplShip = getArguments().getParcelableArrayList("ships");

            if (mCardtplShip != null) {
                for (int i = 0; i < mCardtplShip.size(); i++) {
                    if (mCardtplShip.get(i).status != 1) mCannotChangeCarttpl.add(mCardtplShip.get(i).id);
                }
            }
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_add_account_type, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("设置结算方式", false, null, 0, null);
        if (mMaxUer > 0) {
            orderSutdentCount.setContent(mMaxUer + "");
        } else {
            mMaxUer = 1;
        }
        if (rules != null) {
            for (Rule rule : rules) {
                if (rule.channel.equalsIgnoreCase(Configs.CHANNEL_ONLINE)) {
                    payOnline.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override public void onGlobalLayout() {
                            CompatUtils.removeGlobalLayout(payOnline.getViewTreeObserver(), this);
                            payOnline.setExpanded(true);
                        }
                    });
                    payOnlineMoney.setContent(rule.cost);
                    if (rule.limits != null && rule.limits.user_status != null) {
                        switcher.setChecked(true);
                        limitNum.setVisibility(View.VISIBLE);
                        limitWho.setVisibility(View.VISIBLE);
                        limitNum.setContent(rule.limits.user_count + "");
                        if (rule.limits.user_status != null) {
                            mLimit = new ArrayList<Integer>();
                            mLimit.addAll(rule.limits.user_status);
                            String limStr = "";
                            if (mLimit.contains(0)) {
                                limStr = limStr.concat("新注册");
                            }
                            if (mLimit.contains(1)) {
                                if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                                limStr = limStr.concat("已接洽");
                            }
                            if (mLimit.contains(2)) {
                                if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                                limStr = limStr.concat("会员");
                            }
                            limitWho.setContent(limStr);
                        }
                    } else {
                        switcher.setChecked(false);
                        limitNum.setVisibility(View.GONE);
                        limitWho.setVisibility(View.GONE);
                    }
                }

                if (cardCost.get(rule.card_tpl_id) != null) {
                    cardCost.get(rule.card_tpl_id).put(rule.from_number, rule.cost);
                } else {
                    HashMap<Integer, String> c = new HashMap<>();
                    c.put(rule.from_number, rule.cost);
                    cardCost.put(rule.card_tpl_id, c);
                }
            }
        }
        HashMap<String, Object> params = GymUtils.getParams(coachService, null);
        params.put("key", PermissionServerUtils.CARDSETTING);
        params.put("method", "get");
        sp = restRepository.getGet_api()
            .qcGetCardTplsPermission(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcDataResponse<CardTpls>>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcDataResponse<CardTpls> qcResponseGymCardtpl) {
                    if (qcResponseGymCardtpl.getStatus() == ResponseConstant.SUCCESS) {
                        card_tpls = qcResponseGymCardtpl.data.card_tpls;
                        for (int i = 0; i < qcResponseGymCardtpl.data.card_tpls.size(); i++) {
                            CardTpl card_tpl = qcResponseGymCardtpl.data.card_tpls.get(i);

                            ClubCardView ccv = new ClubCardView(getContext(), card_tpl.getName(), cardCost.containsKey(card_tpl.getId()),
                                card_tpl.getType(), mMaxUer, mType, mCannotChangeCarttpl.contains(card_tpl.getId()));
                            ccv.setCardid(card_tpl.getId());
                            if (cardCost.containsKey(card_tpl.getId())) {
                                cardtypeList.addView(ccv, 0);
                                ccv.setCost(cardCost.get(card_tpl.getId()));
                            } else {
                                cardtypeList.addView(ccv);
                            }
                        }
                    } else {
                        // ToastUtils.logHttp(qcResponseGymCardtpl);
                    }
                }
            });
        RxBusAdd(EventMutiChoose.class).subscribe(new Action1<EventMutiChoose>() {
            @Override public void call(EventMutiChoose eventMutiChoose) {
                mLimit = (ArrayList) eventMutiChoose.data;
                String limStr = "";
                if (mLimit.contains(0)) {

                    limStr = limStr.concat("新注册");
                }
                if (mLimit.contains(1)) {
                    if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                    limStr = limStr.concat("已接洽");
                }
                if (mLimit.contains(2)) {
                    if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                    limStr = limStr.concat("会员");
                }
                limitWho.setContent(limStr);
            }
        });
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    limitNum.setVisibility(View.VISIBLE);
                    limitWho.setVisibility(View.VISIBLE);
                    if (limitWho.isEmpty()) {
                        limitWho.setContent("新注册、已接洽、会员");
                        mLimit.clear();
                        mLimit.add(0);
                        mLimit.add(1);
                        mLimit.add(2);
                    }
                } else {
                    limitNum.setVisibility(View.GONE);
                    limitWho.setVisibility(View.GONE);
                }
            }
        });
        payOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    switcher.setChecked(false);
                }
            }
        });

        if (isFree) {
            layoutPay.setVisibility(View.GONE);
        } else {
            layoutPay.setVisibility(View.VISIBLE);
        }
        swNeedPay.setClickable(false);
        swNeedPay.setChecked(!isFree);
        swNeedPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFree = !isChecked;
                if (isChecked) {
                    layoutPay.setVisibility(View.VISIBLE);
                } else {
                    layoutPay.setVisibility(View.GONE);
                }
            }
        });
        canNotClose.setVisibility(mHasOrder ? View.VISIBLE : View.GONE);

        if (getActivity() instanceof BatchActivity) {
            ((BatchActivity) getActivity()).setOnBackClick(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onCancel();
                }
            });
        }
        RxRegiste(GymBaseInfoAction.getGymByModel(coachService.id(), coachService.model()).filter(new Func1<List<CoachService>, Boolean>() {
            @Override public Boolean call(List<CoachService> list) {
                return list != null && list.size() > 0;
            }
        }).subscribe(new Action1<List<CoachService>>() {
            @Override public void call(List<CoachService> list) {
                final boolean isPro = GymUtils.getSystemEndDay(list.get(0)) > 0;
                tagPro.setVisibility(isPro ? View.GONE : View.VISIBLE);
                layoutNeedPay.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (isPro) {
                            swNeedPay.toggle();
                        } else {
                            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
                        }
                    }
                });
            }
        }));

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
    }

    private void onCancel() {
        if (cancleDialog == null) {
            cancleDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .canceledOnTouchOutside(true)
                .content("是否保存结算方式？")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                        onComfirm();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                })
                .build();
        }
        if (!cancleDialog.isShowing()) cancleDialog.show();
    }

    private void initView() {

    }

    @OnClick(R.id.order_sutdent_count) public void clickStuCount() {
        stucount =
            new DialogList(getContext()).list(mType == Configs.TYPE_PRIVATE ? StringUtils.getNums(1, 10) : StringUtils.getNums(1, 300),
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        orderSutdentCount.setContent(Integer.toString(position + 1));
                        mMaxUer = position + 1;
                        for (int i = 0; i < cardtypeList.getChildCount(); i++) {
                            ClubCardView ccv = (ClubCardView) cardtypeList.getChildAt(i);
                            ccv.setContentCount(Integer.parseInt(orderSutdentCount.getContent()));
                            ccv.freshContent();
                        }
                        stucount.dismiss();
                    }
                }).title("选择人数");
        stucount.show();
    }

    @OnClick(R.id.can_not_close) public void canNotClose() {
        showAlert(R.string.alert_batch_has_ordered);
    }

    @SuppressWarnings("unused") @OnClick(R.id.add_card_type) public void onAddCardType() {
        AddAccountTypeFragment.start(this, 201);
    }

    @OnClick(R.id.comfirm) public void onComfirm() {
        if (isFree) {
            RxBus.getBus().post(new DoneAccountEvent(null, mMaxUer, isFree));
            getActivity().onBackPressed();
        } else {

            ArrayList<Rule> rules = new ArrayList<>();
            for (int i = 0; i < cardtypeList.getChildCount(); i++) {
                ClubCardView ccv = (ClubCardView) cardtypeList.getChildAt(i);
                if (ccv.isExpand()) {
                    if (ccv.isComppleted()) {
                        rules.addAll(ccv.getRules(ccv.getCardid()));
                    } else {
                        ToastUtils.show(ccv.getLable() + "未填写完整");
                        return;
                    }
                }
            }
            if (payOnline.isExpanded()) {
                if (payOnlineMoney.isEmpty()) {
                    ToastUtils.show("在线支付未填写完整");
                    return;
                } else {
                    Rule rule = new Rule();
                    rule.card_tpl_id = "0";
                    rule.cost = payOnlineMoney.getContent();
                    rule.from_number = 1;
                    rule.to_number = mMaxUer + 1;
                    rule.channel = Configs.CHANNEL_ONLINE;
                    if (switcher.isChecked()) {
                        if (limitNum.isEmpty()) {
                            ToastUtils.show("在线支付未填写完整");
                            return;
                        }
                        rule.limits = new OnlineLimit(Integer.parseInt(limitNum.getContent()), mLimit);
                    }

                    rules.add(rule);
                }
            }

            if (rules.size() == 0 && !payOnline.isExpanded()) {
                ToastUtils.show("至少需要选择一种结算方式");
            } else {
                if (this.rules != null) {
                    this.rules.removeAll(rules);
                    if (rules.size() > 0) {
                        rules.addAll(this.rules);
                    }
                }

                //已经设置
                RxBus.getBus().post(new DoneAccountEvent(rules, mMaxUer, isFree));
                getActivity().onBackPressed();
            }
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IntentUtils.RESULT_OK) {
            cardtypeList.addView(new ClubCardView(getContext(), IntentUtils.getIntentString(data), true, data.getIntExtra("type", 1),
                Integer.parseInt(orderSutdentCount.getContent()), mType, false));
        }
    }

    @Override public void onDestroyView() {
        if (getActivity() instanceof BatchActivity) {
            ((BatchActivity) getActivity()).resumeBackBtn();
        }

        RxBus.getBus().post(new EventFresh());
        if (sp != null) sp.unsubscribe();
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return SetAccountTypeAddFragment.class.getName();
    }

    @OnClick({ R.id.layout_limit_peop, R.id.limit_who, R.id.limit_num }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_limit_peop:
                switcher.toggle();

                break;
            case R.id.limit_who:

                OnlineLimitFragment.newInstance(Utils.toIntArray(mLimit)).show(getFragmentManager(), "");
                break;
            case R.id.limit_num:
                stucount = new DialogList(getContext()).list(StringUtils.getNums(1, mMaxUer), new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        limitNum.setContent(Integer.toString(position + 1));
                        stucount.dismiss();
                    }
                }).title("选择人数");
                stucount.show();
                break;
        }
    }
}
