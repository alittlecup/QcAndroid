package cn.qingchengfit.staffkit.views.student.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.ScoreRule;
import cn.qingchengfit.model.responese.ScoreRuleCard;
import cn.qingchengfit.model.responese.StudentScoreRuleBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/26.
 */
public class BaseConfigFragment extends BaseFragment implements BaseConfigPresenter.PresenterView, View.OnClickListener {

    @Inject BaseConfigPresenter presenter;
    @BindView(R.id.sw_score_config_group) SwitcherLayout swScoreConfigGroup;
    @BindView(R.id.civ_score_config_group) CommonInputView civScoreConfigGroup;
    @BindView(R.id.sw_score_config_private) SwitcherLayout swScoreConfigPrivate;
    @BindView(R.id.civ_score_config_private) CommonInputView civScoreConfigPrivate;
    @BindView(R.id.sw_score_config_signin) SwitcherLayout swScoreConfigSignin;
    @BindView(R.id.civ_score_config_signin) CommonInputView civScoreConfigSignin;
    @BindView(R.id.sw_score_config_buy) SwitcherLayout swScoreConfigBuy;
    @BindView(R.id.recyclerView_score_config_buy) RecyclerView recyclerViewBuy;
    @BindView(R.id.tv_student_score_rule_buy) TextView tvStudentScoreRuleBuy;
    @BindView(R.id.sw_score_config_charge) SwitcherLayout swScoreConfigCharge;
    @BindView(R.id.recyclerView_score_config_charge) RecyclerView recyclerViewCharge;
    @BindView(R.id.tv_student_score_rule_charge) TextView tvStudentScoreRuleCharge;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private CommonFlexAdapter flexibleAdapterBuy;
    private List<AbstractFlexibleItem> itemsBuy = new ArrayList<>();
    private List<ScoreRuleCard> ruleListBuy = new ArrayList<>();

    private CommonFlexAdapter flexibleAdapterCharge;
    private List<AbstractFlexibleItem> itemsCharge = new ArrayList<>();
    private List<ScoreRuleCard> ruleListCharge = new ArrayList<>();

    private ScoreRule scoreRule = new ScoreRule();

    public static BaseConfigFragment newInstance(ScoreRule scoreRule) {
        Bundle args = new Bundle();
        args.putParcelable("scoreRule", scoreRule);
        BaseConfigFragment fragment = new BaseConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoreRule = getArguments().getParcelable("scoreRule");
        if (scoreRule == null) scoreRule = new ScoreRule();
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score_config_base, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        initView();

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.qc_title_student_score_config_base);
        toolbar.inflateMenu(R.menu.menu_comfirm);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                doConfirm();
                return false;
            }
        });
    }

    @Override public boolean isBlockTouch() {
        return false;
    }

    private void initView() {

        // 新购会员卡积分
        recyclerViewBuy.setLayoutManager(new LinearLayoutManager(getActivity()));
        flexibleAdapterBuy = new CommonFlexAdapter(itemsBuy);
        recyclerViewBuy.setAdapter(flexibleAdapterBuy);
        recyclerViewBuy.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerViewBuy.setHasFixedSize(true);
        recyclerViewBuy.setNestedScrollingEnabled(false);

        // 会员卡续费积分
        recyclerViewCharge.setLayoutManager(new LinearLayoutManager(getActivity()));
        flexibleAdapterCharge = new CommonFlexAdapter(itemsCharge, this);
        recyclerViewCharge.setAdapter(flexibleAdapterCharge);
        recyclerViewCharge.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerViewCharge.setHasFixedSize(true);
        recyclerViewCharge.setNestedScrollingEnabled(false);

        // 团课预约积分
        swScoreConfigGroup.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigGroup.setOpen(b);
                civScoreConfigGroup.setVisibility(b ? View.VISIBLE : View.GONE);
                //if (!b) {
                //    civScoreConfigGroup.setContent("");
                //}
            }
        });

        // 私教预约积分
        swScoreConfigPrivate.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigPrivate.setOpen(b);
                civScoreConfigPrivate.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        // 签到积分
        swScoreConfigSignin.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigSignin.setOpen(b);
                civScoreConfigSignin.setVisibility(b ? View.VISIBLE : View.GONE);
                //if (!b) {
                //    civScoreConfigSignin.setContent("");
                //}
            }
        });

        // 签到积分
        swScoreConfigBuy.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigBuy.setOpen(b);
                recyclerViewBuy.setVisibility(b ? View.VISIBLE : View.GONE);
                tvStudentScoreRuleBuy.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        // 签到积分
        swScoreConfigCharge.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                swScoreConfigCharge.setOpen(b);
                recyclerViewCharge.setVisibility(b ? View.VISIBLE : View.GONE);
                tvStudentScoreRuleCharge.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        if (scoreRule != null) {

            if (scoreRule != null && scoreRule.teamarrange_enable != null && scoreRule.teamarrange_enable) {
                swScoreConfigGroup.setOpen(true);
                civScoreConfigGroup.setContent(TextUtils.isEmpty(scoreRule.teamarrange) ? "" : scoreRule.teamarrange);
            } else {
                swScoreConfigGroup.setOpen(false);
            }

            if (scoreRule != null && scoreRule.priarrange_enable != null && scoreRule.priarrange_enable) {
                swScoreConfigPrivate.setOpen(true);
                civScoreConfigPrivate.setContent(TextUtils.isEmpty(scoreRule.priarrange) ? "" : scoreRule.priarrange);
            } else {
                swScoreConfigPrivate.setOpen(false);
            }

            if (scoreRule != null && scoreRule.checkin_enable != null && scoreRule.checkin_enable) {
                swScoreConfigSignin.setOpen(true);
                civScoreConfigSignin.setContent(TextUtils.isEmpty(scoreRule.checkin) ? "" : scoreRule.checkin);
            } else {
                swScoreConfigSignin.setOpen(false);
            }

            setRuleValueBuy();

            setRuleValueCharge();
        } else {
            scoreRule = new ScoreRule();
        }
    }

    public void setRuleValueBuy() {
        if (scoreRule != null && scoreRule.buycard_enable != null && scoreRule.buycard_enable) {
            swScoreConfigBuy.setOpen(true);
            if (scoreRule.buycard != null && !scoreRule.buycard.isEmpty()) {
                ruleListBuy.clear();
                itemsBuy.clear();
                for (ScoreRuleCard scoreRuleCard : scoreRule.buycard) {
                    StudentScoreRuleBean ruleBean = new StudentScoreRuleBean();
                    ruleBean.amountStart = scoreRuleCard.start;
                    ruleBean.amountEnd = scoreRuleCard.end;
                    ruleBean.perScore = scoreRuleCard.money;
                    ruleListBuy.add(ruleBean.toScoreRuleCard());

                    itemsBuy.add(new ScoreRuleItem(ruleBean, new View.OnClickListener() {
                        @Override public void onClick(View view) {
                            int pos = (int) view.getTag();
                            itemsBuy.remove(pos);
                            flexibleAdapterBuy.updateDataSet(itemsBuy);
                            flexibleAdapterBuy.notifyDataSetChanged();
                            ruleListBuy.remove(pos);
                        }
                    }));
                }
            }
            flexibleAdapterBuy.updateDataSet(itemsBuy);
            flexibleAdapterBuy.notifyDataSetChanged();
        }
    }

    public void setRuleValueCharge() {
        if (scoreRule != null && scoreRule.chargecard_enable != null && scoreRule.chargecard_enable) {
            swScoreConfigCharge.setOpen(true);
            if (scoreRule.chargecard != null && !scoreRule.chargecard.isEmpty()) {
                ruleListCharge.clear();
                itemsCharge.clear();
                for (ScoreRuleCard scoreRuleCard : scoreRule.chargecard) {
                    StudentScoreRuleBean ruleBean = new StudentScoreRuleBean();
                    ruleBean.amountStart = scoreRuleCard.start;
                    ruleBean.amountEnd = scoreRuleCard.end;
                    ruleBean.perScore = scoreRuleCard.money;
                    ruleListCharge.add(ruleBean.toScoreRuleCard());

                    itemsCharge.add(new ScoreRuleItem(ruleBean, new View.OnClickListener() {
                        @Override public void onClick(View view) {
                            int pos = (int) view.getTag();
                            itemsCharge.remove(pos);
                            flexibleAdapterCharge.updateDataSet(itemsCharge);
                            flexibleAdapterCharge.notifyDataSetChanged();
                            ruleListCharge.remove(pos);
                        }
                    }));
                }
            }
            flexibleAdapterCharge.updateDataSet(itemsCharge);
            flexibleAdapterCharge.notifyDataSetChanged();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @OnClick({ R.id.tv_student_score_rule_buy, R.id.tv_student_score_rule_charge }) public void onRuleAddClick(View view) {
        buildRule(false);
        switch (view.getId()) {
            case R.id.tv_student_score_rule_buy:
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(),
                        ScoreRuleAddFragemnt.newInstance(this, 11, ScoreRuleAddFragemnt.TYPE_BUY, (ArrayList<ScoreRuleCard>) ruleListBuy))
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.tv_student_score_rule_charge:
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(), ScoreRuleAddFragemnt.newInstance(this, 22, ScoreRuleAddFragemnt.TYPE_CHARGE,
                        (ArrayList<ScoreRuleCard>) ruleListCharge))
                    .addToBackStack(null)
                    .commit();
                break;
        }
    }

    @Override public void onClick(View view) {

    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //initToolbar(toolbar);//// TODO: 2017/4/10 麻蛋 这种方法要改  又 Add Fragment
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) { // 新购卡
                StudentScoreRuleBean ruleBean = new StudentScoreRuleBean();
                ruleBean.amountStart = IntentUtils.getIntentString(data, 0);
                ruleBean.amountEnd = IntentUtils.getIntentString(data, 1);
                ruleBean.perScore = IntentUtils.getIntentString(data, 2);
                ruleListBuy.add(ruleBean.toScoreRuleCard());
                scoreRule.buycard.add(ruleBean.toScoreRuleCard());
                itemsBuy.add(new ScoreRuleItem(ruleBean, new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        int pos = (int) view.getTag();
                        itemsBuy.remove(pos);
                        flexibleAdapterBuy.updateDataSet(itemsBuy);
                        flexibleAdapterBuy.notifyDataSetChanged();
                        ruleListBuy.remove(pos);
                    }
                }));
                flexibleAdapterBuy.updateDataSet(itemsBuy);
                flexibleAdapterBuy.notifyDataSetChanged();
            } else if (requestCode == 22) { // 充值
                StudentScoreRuleBean ruleBean = new StudentScoreRuleBean();
                ruleBean.amountStart = IntentUtils.getIntentString(data, 0);
                ruleBean.amountEnd = IntentUtils.getIntentString(data, 1);
                ruleBean.perScore = IntentUtils.getIntentString(data, 2);
                ruleListCharge.add(ruleBean.toScoreRuleCard());
                scoreRule.chargecard.add(ruleBean.toScoreRuleCard());
                itemsCharge.add(new ScoreRuleItem(ruleBean, new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        int pos = (int) view.getTag();
                        itemsCharge.remove(pos);
                        flexibleAdapterCharge.updateDataSet(itemsCharge);
                        flexibleAdapterCharge.notifyDataSetChanged();
                        ruleListCharge.remove(pos);
                    }
                }));
                flexibleAdapterCharge.updateDataSet(itemsCharge);
                flexibleAdapterCharge.notifyDataSetChanged();
            }
        }
    }

    private void doConfirm() {
        buildRule(true);
        if (scoreRule == null) scoreRule = new ScoreRule();
        if (scoreRule.chargecard != null && scoreRule.chargecard.size() == 0) {
            ToastUtils.show("请至少添加一个续费积分");
            return;
        }
        if (scoreRule.buycard != null && scoreRule.buycard.size() == 0) {
            ToastUtils.show("请至少添加一个购卡积分");
            return;
        }

        if (scoreRule.teamarrange_enable
            || scoreRule.priarrange_enable
            || scoreRule.checkin_enable
            || scoreRule.chargecard_enable
            || scoreRule.buycard_enable) {
            presenter.postScoreBaseConfig(scoreRule);
        } else {
            ToastUtils.show("请至少设置一项基础积分");
        }
    }

    private void buildRule(boolean checkEmpty) {
        if (swScoreConfigGroup.isOpen()) {
            if (checkEmpty && TextUtils.isEmpty(civScoreConfigGroup.getContent())) {
                ToastUtils.show("请填写团课积分奖励或者关闭团课积分奖励");
                return;
            }
            scoreRule.teamarrange_enable = true;
            scoreRule.teamarrange = civScoreConfigGroup.getContent();
        } else {
            scoreRule.teamarrange_enable = false;
            scoreRule.teamarrange = null;
        }

        if (swScoreConfigPrivate.isOpen()) {
            if (checkEmpty && TextUtils.isEmpty(civScoreConfigPrivate.getContent())) {
                ToastUtils.show("请填写私教积分奖励或者关闭私教积分奖励");
                return;
            }
            scoreRule.priarrange_enable = true;
            scoreRule.priarrange = civScoreConfigPrivate.getContent();
        } else {
            scoreRule.priarrange_enable = false;
            scoreRule.priarrange = null;
        }

        if (swScoreConfigSignin.isOpen()) {
            if (checkEmpty && TextUtils.isEmpty(civScoreConfigSignin.getContent())) {
                ToastUtils.show("请填写签到积分奖励或者关闭签到积分奖励");
                return;
            }
            scoreRule.checkin_enable = true;
            scoreRule.checkin = civScoreConfigSignin.getContent();
        } else {
            scoreRule.checkin_enable = false;
            scoreRule.checkin = null;
        }

        if (!swScoreConfigBuy.isOpen()) {
            scoreRule.buycard_enable = false;
            scoreRule.buycard = null;
        } else {
            scoreRule.buycard_enable = true;
            if (scoreRule.buycard == null) scoreRule.buycard = new ArrayList<>();
            scoreRule.buycard.clear();
            scoreRule.buycard.addAll(ruleListBuy);
        }
        if (!swScoreConfigCharge.isOpen()) {
            scoreRule.chargecard_enable = false;
            scoreRule.chargecard = null;
        } else {
            scoreRule.chargecard_enable = true;
            if (scoreRule.chargecard == null) scoreRule.chargecard = new ArrayList<>();
            scoreRule.chargecard.clear();
            scoreRule.chargecard.addAll(ruleListCharge);
        }
    }

    @Override public void onConfigSuccess() {
        presenter.postScoreStatus();
    }

    @Override public void onOpenSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {

    }
}
