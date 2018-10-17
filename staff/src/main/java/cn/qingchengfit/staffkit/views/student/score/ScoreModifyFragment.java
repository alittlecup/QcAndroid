package cn.qingchengfit.staffkit.views.student.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.EditTextActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;

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
 * Created by Paper on 2016/12/26.
 */
public class ScoreModifyFragment extends BaseFragment
    implements ScoreModifyPresenter.PresenterView {

  public static final int TYPE_PLUS = 531;
  public static final int TYPE_SUB = 720;
  private static final int RESULT_ADD_REMARKS = 11;
  public int type;
  @Inject ScoreModifyPresenter presenter;
  TextView tvCurScore;
  TextView tvScoreAfterLable;
  TextView tvScoreAfterValue;
  CommonInputView civRemark;
  TextView tvStudentScoreLable;
  EditText etScore;
  private String remarks;
  private String curScore;

  @Inject public ScoreModifyFragment() {
  }

  public String getCurScore() {
    return curScore;
  }

  public void setCurScore(String curScore) {
    this.curScore = curScore;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_student_score_modify, container, false);
    tvCurScore = (TextView) view.findViewById(R.id.tv_cur_score);
    tvScoreAfterLable = (TextView) view.findViewById(R.id.tv_score_after_lable);
    tvScoreAfterValue = (TextView) view.findViewById(R.id.tv_score_after_value);
    civRemark = (CommonInputView) view.findViewById(R.id.civ_remark);
    tvStudentScoreLable = (TextView) view.findViewById(R.id.tv_student_score_lable);
    etScore = (EditText) view.findViewById(R.id.et_score);
    view.findViewById(R.id.civ_remark).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ScoreModifyFragment.this.onClick();
      }
    });

    delegatePresenter(presenter, this);
    initTitle();
    initView();
    return view;
  }

  private void initTitle() {
    String title = "";
    switch (type) {
      case TYPE_PLUS:
        title = "增加积分";
        break;
      case TYPE_SUB:
        title = "扣除积分";
        break;
    }
    mCallbackActivity.setToolbar(title, false, null, R.menu.menu_comfirm,
        new Toolbar.OnMenuItemClickListener() {
          @Override public boolean onMenuItemClick(MenuItem item) {
            if (TextUtils.isEmpty(etScore.getText().toString())) {
              ToastUtils.show("请填写修改积分值");
              return true;
            }
            String scoreChange = "0";
            switch (type) {
              case TYPE_PLUS:
                scoreChange = "+" + etScore.getText().toString();
                break;
              case TYPE_SUB:
                scoreChange = "-" + etScore.getText().toString();
                break;
            }
            presenter.postScoreChange(scoreChange, civRemark.getContent());
            return false;
          }
        });
  }

  private void initView() {
    switch (type) {
      case TYPE_PLUS:
        tvStudentScoreLable.setText("增加积分值");
        tvScoreAfterLable.setText("增加后积分：");
        break;
      case TYPE_SUB:
        tvStudentScoreLable.setText("扣除积分值");
        tvScoreAfterLable.setText("扣除后积分：");
        break;
    }
    tvCurScore.setText(curScore);

    etScore.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void afterTextChanged(Editable editable) {

        String temp = editable.toString();
        if (temp.contains(".")) {
          int posDot = temp.indexOf(".");
          if (posDot > 0 && temp.length() - posDot - 1 > 2) {
            editable.delete(posDot + 3, posDot + 4);
          }
        }

        if (TextUtils.isEmpty(editable.toString())) {
          tvScoreAfterValue.setText(curScore);
        } else {
          double current = Double.valueOf(curScore);
          double x = Double.valueOf(editable.toString());
          double result = 0;
          switch (type) {
            case TYPE_PLUS:
              result = (current + x);
              break;
            case TYPE_SUB:
              result = (current - x);
              break;
          }
          tvScoreAfterValue.setText(String.valueOf(result));
        }
      }
    });
  }

  @Override public String getFragmentName() {
    return ScoreModifyFragment.class.getName();
  }

  public void onClick() {
    // TODO: 2017/11/6
    Intent toAddOrigin = new Intent(getActivity(), EditTextActivity.class);
    toAddOrigin.putExtra("title", "填写备注");
    startActivityForResult(toAddOrigin, RESULT_ADD_REMARKS);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == RESULT_ADD_REMARKS) {
        remarks = IntentUtils.getIntentString(data);
        civRemark.setContent(remarks);
      }
    }
  }

  @Override public void onSuccess() {
    getActivity().onBackPressed();
  }

  @Override public void onShowError(String message) {
    ToastUtils.show(message);
  }
}
