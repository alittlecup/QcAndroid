package cn.qingchengfit.staffkit.views.student.score;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.qingchengfit.model.responese.ScoreRuleCard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.ArrayList;
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
public class ScoreRuleAddFragemnt extends BaseFragment
    implements ScoreRuleAddPresenter.PresenterView, View.OnTouchListener {

  public static final int TYPE_BUY = 445;
  public static final int TYPE_CHARGE = 303;
  @Inject ScoreRuleAddPresenter presenter;
	EditText etStudentScoreAmountStart;
	EditText etStudentScoreAmountEnd;
	CommonInputView civScoreValue;
	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
  private int ruleType;
  private int requestCode;
  private ArrayList<ScoreRuleCard> others;

  public static ScoreRuleAddFragemnt newInstance(Fragment target, int requestCode, int ruleType,
      ArrayList<ScoreRuleCard> others) {
    Bundle args = new Bundle();
    args.putInt("ruleType", ruleType);
    args.putInt("requestCode", requestCode);
    args.putParcelableArrayList("others", others);
    ScoreRuleAddFragemnt scoreFragment = new ScoreRuleAddFragemnt();
    scoreFragment.setTargetFragment(target, requestCode);
    scoreFragment.setArguments(args);
    return scoreFragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ruleType = getArguments().getInt("ruleType", TYPE_BUY);
    requestCode = getArguments().getInt("requestCode", 11);
    others = getArguments().getParcelableArrayList("others");
    //
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_student_score_rule_add, container, false);
    etStudentScoreAmountStart = (EditText) view.findViewById(R.id.et_student_score_amount_start);
    etStudentScoreAmountEnd = (EditText) view.findViewById(R.id.et_student_score_amount_end);
    civScoreValue = (CommonInputView) view.findViewById(R.id.civ_score_value);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);

    delegatePresenter(presenter, this);
    initTitle();
    initView();
    view.setOnTouchListener(this);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getView().setOnTouchListener(this);
  }

  private void initTitle() {
    initToolbar(toolbar);
    String title = "";
    switch (ruleType) {
      case TYPE_BUY:
        title = "添加购卡积分";
        break;
      case TYPE_CHARGE:
        title = "添加充值积分";
        break;
    }
    toolbarTitle.setText(title);
    toolbar.inflateMenu(R.menu.menu_comfirm);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (TextUtils.isEmpty(etStudentScoreAmountStart.getText().toString())) {
          ToastUtils.show("请输入范围初始金额");
          return false;
        }
        if (TextUtils.isEmpty(etStudentScoreAmountEnd.getText().toString())) {
          ToastUtils.show("请输入范围结束金额");
          return false;
        }
        if (TextUtils.isEmpty(civScoreValue.getContent())) {
          ToastUtils.show("请输入获得积分");
          return false;
        }
        // TODO: 16/12/26 判断金额区间
        if (others != null && !others.isEmpty()) {
          double inputStart = Double.valueOf(etStudentScoreAmountStart.getText().toString());
          double inputEnd = Double.valueOf(etStudentScoreAmountEnd.getText().toString());
          for (ScoreRuleCard scoreRuleBean : others) {
            double otherStart = Double.valueOf(scoreRuleBean.start);
            double otherEnd = Double.valueOf(scoreRuleBean.end);
            if ((otherStart <= inputStart && inputStart <= otherEnd) || (otherStart <= inputEnd
                && inputEnd <= otherEnd)) {
              ToastUtils.show("输入的金额区间与其他规则金额区间冲突，请重新输入");
              return true;
            }
          }
        }
        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK,
            IntentUtils.instanceStringsIntent(etStudentScoreAmountStart.getText().toString(),
                etStudentScoreAmountEnd.getText().toString(), civScoreValue.getContent()));
        getActivity().onBackPressed();
        return false;
      }
    });
  }

  private void initView() {

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

  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    return true;
  }
}
