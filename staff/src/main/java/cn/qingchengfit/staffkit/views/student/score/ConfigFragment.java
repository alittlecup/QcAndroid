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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreRule;
import cn.qingchengfit.model.responese.ScoreRuleAward;
import cn.qingchengfit.model.responese.ScoreRuleCard;
import cn.qingchengfit.model.responese.StudentScoreAwardRuleBean;
import cn.qingchengfit.model.responese.StudentScoreBaseConfigBean;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * //Created by yangming on 16/12/23.
 */
public class ConfigFragment extends BaseFragment
    implements ConfigPresenter.PresenterView, FlexibleAdapter.OnItemClickListener {

  Toolbar toolbar;
  TextView toolbarTitile;
  @Inject ConfigPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  SwitcherLayout swtScoreConfig;
  LinearLayout llConfigBase;
  RecyclerView recyclerviewScoreBaseDetail;
  LinearLayout llConfigAwardLable;
  RecyclerView recyclerviewAward;
  LinearLayout llConfigAwardAdd;
  TextView tvStudentScoreAwardRule;
  LinearLayout llStudentScoreConfigContent;
  TextView tvScoreRuleUpdateInfo;
  RecyclerView recyclerviewAwardDis;
  TextView tvStudentScoreAwardRuleDis;
  private CommonFlexAdapter flexibleAdapterBaseScore;
  private List<AbstractFlexibleItem> itemsBaseScore = new ArrayList<>();
  private List<StudentScoreAwardRuleBean> scoreAwardRuleBeanListOrigin = new ArrayList<>();
  private CommonFlexAdapter flexibleAdapterAwardActive;
  private List<AbstractFlexibleItem> itemsAwardActive = new ArrayList<>();
  private List<StudentScoreAwardRuleBean> scoreAwardRuleBeanListActive = new ArrayList<>();
  private CommonFlexAdapter flexibleAdapterAwardDis;
  private List<AbstractFlexibleItem> itemsAwardDis = new ArrayList<>();
  private List<StudentScoreAwardRuleBean> scoreAwardRuleBeanListDis = new ArrayList<>();
  private boolean isOpen;
  private ScoreRule scoreRule;
  private boolean disAwardShow = false;

  public static ConfigFragment newInstance(boolean open) {

    Bundle args = new Bundle();
    args.putBoolean("open", open);
    ConfigFragment fragment = new ConfigFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_student_score_config, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    swtScoreConfig = (SwitcherLayout) view.findViewById(R.id.swt_score_config);
    llConfigBase = (LinearLayout) view.findViewById(R.id.ll_config_base);
    recyclerviewScoreBaseDetail =
        (RecyclerView) view.findViewById(R.id.recyclerview_score_base_detail);
    llConfigAwardLable = (LinearLayout) view.findViewById(R.id.ll_config_award_lable);
    recyclerviewAward = (RecyclerView) view.findViewById(R.id.recyclerview_award);
    llConfigAwardAdd = (LinearLayout) view.findViewById(R.id.ll_config_award_add);
    tvStudentScoreAwardRule = (TextView) view.findViewById(R.id.tv_student_score_award_rule);
    llStudentScoreConfigContent =
        (LinearLayout) view.findViewById(R.id.ll_student_score_config_content);
    tvScoreRuleUpdateInfo = (TextView) view.findViewById(R.id.tv_score_rule_update_info);
    recyclerviewAwardDis = (RecyclerView) view.findViewById(R.id.recyclerview_award_dis);
    tvStudentScoreAwardRuleDis = (TextView) view.findViewById(R.id.tv_student_score_award_rule_dis);
    view.findViewById(R.id.ll_config_base).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ConfigFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.ll_config_award_add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ConfigFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.tv_student_score_award_rule_dis)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onDisShowClick();
          }
        });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    initView();
    presenter.getScoreStatus();
    return view;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(R.string.qc_title_student_score_config);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initView() {

    // 基础积分
    recyclerviewScoreBaseDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
    flexibleAdapterBaseScore = new CommonFlexAdapter(itemsBaseScore);
    recyclerviewScoreBaseDetail.setAdapter(flexibleAdapterBaseScore);
    recyclerviewScoreBaseDetail.setHasFixedSize(true);
    recyclerviewScoreBaseDetail.setNestedScrollingEnabled(false);

    // 积分奖励
    recyclerviewAward.setLayoutManager(new LinearLayoutManager(getActivity()));
    flexibleAdapterAwardActive = new CommonFlexAdapter(itemsAwardActive, this);
    recyclerviewAward.setAdapter(flexibleAdapterAwardActive);
    recyclerviewAward.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    recyclerviewAward.setHasFixedSize(true);
    recyclerviewAward.setNestedScrollingEnabled(false);

    recyclerviewAwardDis.setLayoutManager(new LinearLayoutManager(getActivity()));
    flexibleAdapterAwardDis = new CommonFlexAdapter(itemsAwardDis);
    recyclerviewAwardDis.setAdapter(flexibleAdapterAwardDis);
    recyclerviewAwardDis.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    recyclerviewAwardDis.setHasFixedSize(true);
    recyclerviewAwardDis.setNestedScrollingEnabled(false);

    // 开关
    swtScoreConfig.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
          llStudentScoreConfigContent.setVisibility(View.VISIBLE);
          presenter.getScoreRuls();
          presenter.getScoreAward();
        } else {
          showLoading();
          presenter.postScoreStatus();
        }
      }
    });
    if (isLoading && (scoreRule == null || scoreRule.allDisable())) {
      swtScoreConfig.setOpen(false);
      presenter.postScoreStatus();
    } else {
      swtScoreConfig.getViewTreeObserver()
          .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
              CompatUtils.removeGlobalLayout(swtScoreConfig.getViewTreeObserver(), this);
              if (getArguments().getBoolean("open", false) && !swtScoreConfig.isOpen()) {
                swtScoreConfig.setOpen(true);
                presenter.openScoreStatus();
              }
            }
          });
    }
    isLoading = true;
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

  @Override public void onScoreStatus(boolean score) {
    if (!swtScoreConfig.isOpen()) {

      isOpen = score;
      swtScoreConfig.setOpen(isOpen);
      if (isOpen) {
        presenter.getScoreRuls();
      }
    } else {
      presenter.getScoreRuls();
    }
  }

  @Override public void onScoreStatusFail(String e) {
    ToastUtils.show(e);
  }

  @Override public void onScoreRules(ScoreRule scoreRule) {
    hideLoading();
    if (scoreRule.buycard_enable
        || scoreRule.chargecard_enable
        || scoreRule.checkin_enable
        || scoreRule.priarrange_enable
        || scoreRule.teamarrange_enable) {

      this.scoreRule = scoreRule;
      presenter.getScoreAward();

      tvScoreRuleUpdateInfo.setText(new StringBuilder().append("修改时间 ")
          .append(
              (!TextUtils.isEmpty(scoreRule.updated_at)) ? scoreRule.updated_at.replace("T", " ")
                  : " ")
          .append(" 由")
          .append(
              (scoreRule.updated_by != null && !TextUtils.isEmpty(scoreRule.updated_by.username))
                  ? scoreRule.updated_by.username : " ")
          .append("操作")
          .toString());

      itemsBaseScore.clear();
      if (scoreRule.teamarrange_enable != null && scoreRule.teamarrange_enable) {
        StudentScoreBaseConfigBean baseConfigBean = new StudentScoreBaseConfigBean();
        baseConfigBean.lable = "团课预约";
        baseConfigBean.value = scoreRule.teamarrange;
        itemsBaseScore.add(new BaseConfigItem(baseConfigBean));
      }

      if (scoreRule.priarrange_enable != null && scoreRule.priarrange_enable) {
        StudentScoreBaseConfigBean baseConfigBean = new StudentScoreBaseConfigBean();
        baseConfigBean.lable = "私教预约";
        baseConfigBean.value = scoreRule.priarrange;
        itemsBaseScore.add(new BaseConfigItem(baseConfigBean));
      }

      if (scoreRule.checkin_enable != null && scoreRule.checkin_enable) {
        StudentScoreBaseConfigBean baseConfigBean = new StudentScoreBaseConfigBean();
        baseConfigBean.lable = "签到";
        baseConfigBean.value = scoreRule.checkin;
        itemsBaseScore.add(new BaseConfigItem(baseConfigBean));
      }

      if (scoreRule.buycard_enable != null
          && scoreRule.buycard_enable
          && scoreRule.buycard != null
          && !scoreRule.buycard.isEmpty()) {
        StudentScoreBaseConfigBean baseConfigBean = new StudentScoreBaseConfigBean();
        baseConfigBean.lable = "新购会员卡";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < scoreRule.buycard.size(); i++) {
          ScoreRuleCard ruleCard = scoreRule.buycard.get(i);
          if (i < scoreRule.buycard.size() - 1) {
            stringBuilder.append("实收金额")
                .append(ruleCard.start)
                .append("至")
                .append(ruleCard.end)
                .append("，每一元得")
                .append(ruleCard.money)
                .append("积分\n")
                .toString();
          } else {
            stringBuilder.append("实收金额")
                .append(ruleCard.start)
                .append("至")
                .append(ruleCard.end)
                .append("，每一元得")
                .append(ruleCard.money)
                .append("积分")
                .toString();
          }
        }
        baseConfigBean.value = stringBuilder.toString();
        itemsBaseScore.add(new BaseConfigItem(baseConfigBean));
      }

      if (scoreRule.chargecard_enable != null
          && scoreRule.chargecard_enable
          && scoreRule.chargecard != null
          && !scoreRule.chargecard.isEmpty()) {
        StudentScoreBaseConfigBean baseConfigBean = new StudentScoreBaseConfigBean();
        baseConfigBean.lable = "会员卡续费";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < scoreRule.chargecard.size(); i++) {
          ScoreRuleCard ruleCard = scoreRule.chargecard.get(i);
          if (i < scoreRule.chargecard.size() - 1) {
            stringBuilder.append("实收金额")
                .append(ruleCard.start)
                .append("至")
                .append(ruleCard.end)
                .append("，每一元得")
                .append(ruleCard.money)
                .append("积分\n")
                .toString();
          } else {
            stringBuilder.append("实收金额")
                .append(ruleCard.start)
                .append("至")
                .append(ruleCard.end)
                .append("，每一元得")
                .append(ruleCard.money)
                .append("积分")
                .toString();
          }
        }
        baseConfigBean.value = stringBuilder.toString();
        itemsBaseScore.add(new BaseConfigItem(baseConfigBean));
      }

      flexibleAdapterBaseScore.updateDataSet(itemsBaseScore);
      flexibleAdapterBaseScore.notifyDataSetChanged();
    } else {
      this.scoreRule = null;
      getFragmentManager().beginTransaction()
          .replace(mCallbackActivity.getFragId(), BaseConfigFragment.newInstance(new ScoreRule()))
          .addToBackStack(null)
          .commit();
    }
  }

  @Override public void onScoreAward(List<ScoreRuleAward> awards) {
    // TODO: 16/12/28 set ui
    hideLoading();
    scoreAwardRuleBeanListOrigin.clear();
    itemsAwardActive.clear();
    scoreAwardRuleBeanListActive.clear();
    itemsAwardDis.clear();
    scoreAwardRuleBeanListDis.clear();
    if (!awards.isEmpty()) {
      for (ScoreRuleAward award : awards) {
        scoreAwardRuleBeanListOrigin.add(award.toAwardRuleBean());
        if (award.is_active) {
          scoreAwardRuleBeanListActive.add(award.toAwardRuleBean());
          itemsAwardActive.add(new ScoreAwardItem(award.toAwardRuleBean()));
        } else {
          // TODO: 16/12/29
          scoreAwardRuleBeanListDis.add(award.toAwardRuleBean());
          itemsAwardDis.add(new ScoreAwardItem(award.toAwardRuleBean()));
        }
      }
      flexibleAdapterAwardActive.updateDataSet(itemsAwardActive);
      flexibleAdapterAwardDis.updateDataSet(itemsAwardDis);
      flexibleAdapterAwardDis.notifyDataSetChanged();
    } else {
      flexibleAdapterAwardActive.updateDataSet(itemsAwardActive);
      flexibleAdapterAwardActive.notifyDataSetChanged();
    }
  }

  @Override public void onCloseSuccess() {
    hideLoading();
    llStudentScoreConfigContent.setVisibility(View.GONE);
    tvScoreRuleUpdateInfo.setText("");
    scoreRule = new ScoreRule();
    itemsBaseScore.clear();
    flexibleAdapterBaseScore.updateDataSet(itemsBaseScore);
    flexibleAdapterBaseScore.notifyDataSetChanged();

    itemsAwardActive.clear();
    flexibleAdapterAwardActive.updateDataSet(itemsAwardActive);
    flexibleAdapterAwardActive.notifyDataSetChanged();

    itemsAwardDis.clear();
    flexibleAdapterAwardDis.updateDataSet(itemsAwardDis);
    flexibleAdapterAwardDis.notifyDataSetChanged();
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  @Override public boolean onItemClick(int position) {
    // 16/12/26 跳转积分奖励规则-编辑
    if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING_CAN_CHANGE)) {
      showAlert(R.string.alert_permission_forbid);
      return true;
    }
    ArrayList<StudentScoreAwardRuleBean> others = new ArrayList<>();
    others.addAll(scoreAwardRuleBeanListActive);
    others.remove(position);
    getFragmentManager().beginTransaction()
        .replace(mCallbackActivity.getFragId(),
            ScoreAwardAddFragment.modify(this, 22, scoreAwardRuleBeanListActive.get(position),
                others))
        .addToBackStack(null)
        .commit();
    return false;
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ll_config_base:
        if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), BaseConfigFragment.newInstance(scoreRule))
            .addToBackStack(null)
            .commit();
        break;
      case R.id.ll_config_award_add:
        if (!serPermisAction.check(PermissionServerUtils.SCORE_SETTING_CAN_CHANGE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), ScoreAwardAddFragment.newInstance(this, 11,
                (ArrayList<StudentScoreAwardRuleBean>) scoreAwardRuleBeanListActive))
            .addToBackStack(null)
            .commit();
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 11) {
        // 16/12/27 重新请求奖励规则接口
        showLoading();
        presenter.getScoreAward();
      }
    }
  }

  public void onDisShowClick() {
    recyclerviewAwardDis.setVisibility(disAwardShow ? View.GONE : View.VISIBLE);
    tvStudentScoreAwardRuleDis.setText(disAwardShow ? "显示已删除的奖励规则" : "隐藏已删除的奖励规则");
    disAwardShow = !disAwardShow;
  }
}
