package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.DoneAccountEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.custom.ClubCardView;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.ArrayList;

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
public class SetAccountTypeFragment extends BaseFragment {

    @BindView(R.id.order_sutdent_count) CommonInputView orderSutdentCount;
    //    @BindView(R.id.card_list)
    //    RecyclerView cardList;
    @BindView(R.id.add_card_type) TextView addCardType;
    @BindView(R.id.cardtype_list) LinearLayout cardtypeList;
    private DialogList stucount; // 选择学员数量
    private SystemInitBody body;
    private int mType;//课程种类

    public static SetAccountTypeFragment newInstance(int mType) {
        Bundle args = new Bundle();
        SetAccountTypeFragment fragment = new SetAccountTypeFragment();
        args.putInt("type", mType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mType = getArguments().getInt("type");
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_account_type, container, false);
        unbinder = ButterKnife.bind(this, view);
        //        if (getActivity() != null && getActivity() instanceof GymActivity) {
        //            ((GymActivity) getActivity()).setTitle(getString(R.string.titie_set_account_type));
        //        }
        mCallbackActivity.setToolbar(getString(R.string.titie_set_account_type), false, null, 0, null);
        orderSutdentCount.setContent("1");
        initView();
        return view;
    }

    private void initView() {
        body = (SystemInitBody) App.caches.get("init");
        if (body != null && body.card_tpls != null && body.card_tpls.size() > 0) {
            int count = body.batches.get(0).getCapacity();
            orderSutdentCount.setContent(count + "");
            for (CardTpl card_tpl : body.card_tpls) {
                if (card_tpl.isChoosen) {
                    cardtypeList.addView(
                        new ClubCardView(getContext(), card_tpl.getName(), card_tpl.isChoosen, card_tpl.getType(), count, mType,
                            card_tpl.getCosts(), false), 0);
                } else {
                    cardtypeList.addView(
                        new ClubCardView(getContext(), card_tpl.getName(), card_tpl.isChoosen, card_tpl.getType(), count, mType,
                            card_tpl.getCosts(), false));
                }
            }
        } else {
            cardtypeList.addView(new ClubCardView(getContext(), getString(R.string.course_default_value_card), false, 1, 1, mType, false));
            cardtypeList.addView(new ClubCardView(getContext(), getString(R.string.course_default_time_card), false, 2, 1, mType, false));
            cardtypeList.addView(new ClubCardView(getContext(), getString(R.string.course_default_year_card), false, 3, 1, mType, false));
        }
    }

    @OnClick(R.id.order_sutdent_count) public void clickStuCount() {
        stucount = new DialogList(getContext()).list(StringUtils.getNums(1, mType == Configs.TYPE_PRIVATE ? 10 : 300),
            new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    orderSutdentCount.setContent(Integer.toString(position + 1));
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

    @SuppressWarnings("unused") @OnClick(R.id.add_card_type) public void onAddCardType() {
        AddAccountTypeFragment.start(this, 201);
    }

    @OnClick(R.id.comfirm) public void onComfirm() {
        int cou = 0;
        if (body != null) {
            body.card_tpls = new ArrayList<>();
            body.batches.get(0).setCapacity(Integer.parseInt(orderSutdentCount.getContent()));
            body.batches.get(0).setRules(new ArrayList<Rule>());
            for (int i = 0; i < cardtypeList.getChildCount(); i++) {
                ClubCardView ccv = (ClubCardView) cardtypeList.getChildAt(i);
                CardTpl card_tpl = new CardTpl();
                card_tpl.setName(ccv.getLable());
                card_tpl.setType(ccv.getType());
                card_tpl.setCosts(ccv.getCostList());
                card_tpl.isChoosen = ccv.isExpand();
                body.card_tpls.add(card_tpl);

                if (ccv.isExpand()) {
                    if (mType == Configs.TYPE_PRIVATE) {
                        for (int j = 0; j < ccv.getCivCount(); j++) {
                            CommonInputView civ = (CommonInputView) ccv.getCivAt(j);
                            Rule rule = new Rule();
                            rule.card_tpl_name = card_tpl.getName();
                            rule.from_number = j + 1;
                            rule.to_number = j + 2;
                            if (card_tpl.getType() != Configs.CATEGORY_DATE) rule.cost = civ.getContent();

                            if (ccv.getType() != Configs.CATEGORY_DATE && civ.isEmpty()) {
                                ToastUtils.showDefaultStyle(ccv.getLable() + " 价格未填写完整");
                                return;
                            } else {
                                cou++;
                            }
                            body.batches.get(0).getRules().add(rule);
                        }
                    } else {
                        try {
                            CommonInputView civ = (CommonInputView) ccv.getCivAt(0);
                            Rule rule = new Rule();
                            rule.card_tpl_name = card_tpl.getName();
                            rule.from_number = 1;
                            rule.to_number = Integer.parseInt(orderSutdentCount.getContent());
                            if (card_tpl.getType() != Configs.CATEGORY_DATE) rule.cost = civ.getContent();
                            if (ccv.getType() != Configs.CATEGORY_DATE && civ.isEmpty()) {
                                ToastUtils.showDefaultStyle(ccv.getLable() + " 价格未填写完整");
                                return;
                            }
                            body.batches.get(0).getRules().add(rule);
                            cou++;
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < cardtypeList.getChildCount(); i++) {
                ClubCardView ccv = (ClubCardView) cardtypeList.getChildAt(i);

                if (ccv.isExpand()) {

                    if (ccv.getType() == 3) {//期限卡
                        cou++;
                    } else { //储值卡 和 次卡
                        for (String s : ccv.getCostList()) {
                            if (!TextUtils.isEmpty(s)) {
                                cou++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (cou > 0) {
            //            SystemInitBody body = (SystemInitBody) App.caches.get("init");
            //            if (body.batches != null && body.batches.size() > 0) {
            //                Batch batch = body.batches.get(0);
            //                rule.capacity = orderSutdentCount.getContent();
            //                batch.rule = rule;
            //            }

            //已经设置
            RxBus.getBus().post(new DoneAccountEvent());
            getActivity().onBackPressed();
        } else {
            ToastUtils.show("请至少选择一种结算方式");
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
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return SetAccountTypeFragment.class.getName();
    }
}
