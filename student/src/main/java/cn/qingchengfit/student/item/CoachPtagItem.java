package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.CoachPtagAnswer;
import cn.qingchengfit.student.bean.PtagQuestion;
import cn.qingchengfit.student.event.DynamicPtagItemEvent;
import cn.qingchengfit.student.view.ptag.PtagAnswerTransform;
import cn.qingchengfit.student.widget.LimitWordEditText;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

public class CoachPtagItem extends AbstractFlexibleItem<CoachPtagItem.CoachPtagVH> {

  public static final int SINGLE_SELECT = 0;
  public static final int MULTIPLE_SELECT = 1;
  public static final int TEXT = 2;
  public static final int TRUE_OR_FALSE = 3;
  public static final int NUMBER = 4;

  private PtagQuestion ptagQuestion;
  private LimitWordEditText editText;
  private LinearLayout llContainer;
  private CommonInputView commonInputView;
  private PtagAnswerTransform ptagAnswerTransform = new PtagAnswerTransform();
  private int number = -1;

  public CoachPtagItem(PtagQuestion ptagQuestion) {

    this.ptagQuestion = ptagQuestion;
  }

  public PtagQuestion getData() {
    return ptagQuestion;
  }

  @Override public boolean equals(Object o) {
    if (o == null) return false;
    if (o instanceof CoachPtagItem) {
      return ptagQuestion.getId().equals(((CoachPtagItem) o).getData().getId());
    }
    return false;
  }

  public void setPtagQuestion(PtagQuestion ptagQuestion) {
    this.ptagQuestion = ptagQuestion;
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_ptag_select_single;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  @Override public CoachPtagVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CoachPtagVH(view, adapter);
  }

  @Override public int getItemViewType() {
    return ptagQuestion.getAnswer_type();
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CoachPtagVH holder, int position,
      List payloads) {
    loadData(holder, position);
  }

  private void loadData(CoachPtagVH holder, int position) {
    if (ptagAnswerTransform.getListener() == null) {
      ptagAnswerTransform.setListener(index -> {
        if (ptagQuestion.getOptions().get(index).getMax_value() != null) {
          //目标动机
          RxBus.getBus()
              .post(new DynamicPtagItemEvent(ptagQuestion.getId(), index,
                  DynamicPtagItemEvent.TYPE_TRAINER_FEEDBACK));
        } else if (ptagQuestion.getOptions().get(index).getWeight() != null) {
          //训练风格
          RxBus.getBus()
              .post(new DynamicPtagItemEvent(ptagQuestion.getOptions().get(index),
                  DynamicPtagItemEvent.TYPE_TRAINER_STYLE, ptagQuestion.getId()));
        }
      });
    }
    holder.tvTitle.setText(ptagQuestion.getReplace_title());
    holder.imgHelp.setVisibility(ptagQuestion.getHelp_text().isEmpty() ? View.GONE : View.VISIBLE);
    holder.imgHelp.setTag(position);
    holder.inputSelectNumber.setTag(position);
    if (!ptagQuestion.getRemarks().isEmpty()) {
      holder.tvQuestionRemark.setVisibility(View.VISIBLE);
      holder.tvQuestionRemark.setText(ptagQuestion.getRemarks());
    }
    holder.inputSelectNumber.setVisibility(View.GONE);
    switch (ptagQuestion.getAnswer_type()) {
      case SINGLE_SELECT:
      case MULTIPLE_SELECT:
        //选择题答案列表
        List<String> answerIds = new ArrayList<>(ptagQuestion.getAnswerList());
        holder.editAnswer.setVisibility(View.GONE);
        if (holder.llOptions.getChildCount() <= 1) {
          for (View view : ptagAnswerTransform.transformDataSelect(holder.itemView.getContext(),
              holder.llOptions, ptagQuestion.getOptions(),
              ptagQuestion.getAnswer_type() == SINGLE_SELECT, answerIds)) {
            holder.llOptions.addView(view);
          }
        }
        llContainer = holder.llOptions;
        break;
      case TEXT:
      case TRUE_OR_FALSE:
        //如果是需要总结的，则不显示输入框
        if (ptagQuestion.isIs_summary()) {
          break;
        }
        holder.inputSelectNumber.setVisibility(View.GONE);
        holder.editAnswer.setLimit(ptagQuestion.getAnswer_length());
        holder.editAnswer.setTag(position);
        if (ptagQuestion.getEdit_text().isEmpty() && !ptagQuestion.getAnswer().isEmpty()) {
          holder.editAnswer.setText(ptagQuestion.getAnswer());
        }else {
          holder.editAnswer.setText(ptagQuestion.getEdit_text());
        }
        holder.editAnswer.setVisibility(View.VISIBLE);
        editText = holder.editAnswer;
        break;
      case NUMBER:
        holder.editAnswer.setVisibility(View.GONE);
        holder.inputSelectNumber.setVisibility(View.VISIBLE);
        if (number < 0 && !ptagQuestion.getAnswer().isEmpty()) {
          holder.inputSelectNumber.setContent(ptagQuestion.getAnswer());
        }
        commonInputView = holder.inputSelectNumber;
        break;
    }
  }

  //获取问卷结果
  public CoachPtagAnswer getResult() {
    CoachPtagAnswer answer = new CoachPtagAnswer();
    answer.setId(ptagQuestion.getId());
    switch (getItemViewType()) {
      case SINGLE_SELECT:
      case MULTIPLE_SELECT:
        List<Integer> ids = new ArrayList<>();
        if (llContainer != null) {
          for (int index : ptagAnswerTransform.getSelectPosition(llContainer)) {
            ids.add(Integer.parseInt(ptagQuestion.getOptions().get(index - 1).getId()));
          }
        } else {
          for (String id : ptagQuestion.getAnswerList()) {
            ids.add(Integer.parseInt(id));
          }
        }
        answer.setAnswer(ids.size() == 0 ? null : ids);
        break;
      case TEXT:
      case TRUE_OR_FALSE:
        String result = "";
        if (editText != null) {
          result = editText.getText().toString();
        } else {
          result = ptagQuestion.getAnswer();
        }
        if (result.equals("是")) {
          answer.setAnswer(true);
        } else if (result.equals("否")) {
          answer.setAnswer(false);
        } else {
          answer.setAnswer(result);
        }
        break;
      case NUMBER:
        if (commonInputView != null && !commonInputView.getContent().equals("请选择")) {
          //已有答案不修改，结果为Float，直接传float报错。
          answer.setAnswer((int) Float.parseFloat(commonInputView.getContent()));
        } else if (commonInputView == null) {
          answer.setAnswer((int) Float.parseFloat(ptagQuestion.getAnswer()));
        }
        break;
    }
    return answer;
  }

  public class CoachPtagVH extends FlexibleViewHolder {
    TextView tvTitle;
    ImageView imgHelp;
    LinearLayout llOptions;
    LimitWordEditText editAnswer;
    TextView tvQuestionRemark;
    CommonInputView inputSelectNumber;
    DialogList dialogList;

    public CoachPtagVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTitle = view.findViewById(R.id.tv_ptag_select_title);
      imgHelp = view.findViewById(R.id.img_ptag_select_help);
      llOptions = view.findViewById(R.id.ll_ptag_select_answer);
      tvQuestionRemark = view.findViewById(R.id.tv_ptag_question_remark);
      editAnswer = view.findViewById(R.id.edit_ptag_answer);
      inputSelectNumber = view.findViewById(R.id.input_selected_number);
      inputSelectNumber.setOnClickListener(v -> {
        if (dialogList == null) {
          dialogList = new DialogList(view.getContext()).list(CmStringUtils.getNums(1, 10),
              (parent, v1, position, id) -> {
                ((CommonInputView) v).setContent(String.valueOf(position + 1));
                if (((CoachPtagItem) adapter.getItem((int) v.getTag())).getData().getMax_limits()
                    != null) {
                  RxBus.getBus()
                      .post(new DynamicPtagItemEvent(
                          ((CoachPtagItem) adapter.getItem((int) v.getTag())).getData().getId(),
                          position + 1, DynamicPtagItemEvent.TYPE_TRAINER_GOAL));
                }
                ((CoachPtagItem) adapter.getItem((int) v.getTag())).setNumber(position + 1);
              }).title("选择评分");
        }
        dialogList.show();
      });
      editAnswer.setListener((v, text) -> {
        if (adapter.getItem((int)v.getTag()) instanceof CoachPtagItem){
          ((CoachPtagItem) adapter.getItem((int) v.getTag())).getData().setEdit_text(text);
        }
      });
      imgHelp.setOnClickListener(v -> {
        if (adapter.getItem((int) v.getTag()) instanceof CoachPtagItem) {
          DialogUtils.showAlert(v.getContext(),
              ((CoachPtagItem) adapter.getItem((int) v.getTag())).getData().getHelp_text());
        }
      });
    }
  }
}
