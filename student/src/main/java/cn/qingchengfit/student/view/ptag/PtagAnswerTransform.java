package cn.qingchengfit.student.view.ptag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.PtagAnswerOption;
import java.util.ArrayList;
import java.util.List;

public class PtagAnswerTransform {

  private OnSelectionAnswerListener listener;

  public PtagAnswerTransform() {

  }

  public void setListener(OnSelectionAnswerListener listener) {
    this.listener = listener;
  }

  public OnSelectionAnswerListener getListener() {
    return listener;
  }

  //教练PTAG问卷选择项处理
  public List<View> transformDataSelect(Context context, ViewGroup parent,
      List<PtagAnswerOption> options, boolean isSingle, List<String> answerIds) {
    List<View> views = new ArrayList<>();
    if (options == null) {
      return views;
    }
    int index = 0;
    for (PtagAnswerOption option : options) {
      View view =
          LayoutInflater.from(context).inflate(R.layout.st_view_ptag_seletct_answer, parent, false);
      TextView tvAnswerContent = view.findViewById(R.id.tv_ptag_select_answer_content);
      TextView tvAnswerTitle = view.findViewById(R.id.tv_ptag_select_answer_title);
      CheckBox cbSelected = view.findViewById(R.id.cb_ptag_select);
      cbSelected.setTag(index);
      cbSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isSingle && isChecked) {
          checkSelection(buttonView);
          if (listener != null) {
            listener.onSelected((int) buttonView.getTag());
          }
        }
      });
      if (answerIds.size() != 0 && answerIds.contains(option.getId())) {
        cbSelected.setChecked(true);
      }
      //remark为空时
      if (!option.getRemarks().isEmpty()) {
        tvAnswerTitle.setVisibility(View.VISIBLE);
        tvAnswerTitle.setText(option.getValue());
        tvAnswerContent.setText(option.getRemarks());
      } else {
        tvAnswerTitle.setVisibility(View.GONE);
        tvAnswerContent.setText(option.getValue());
      }
      index++;
      views.add(view);
    }
    return views;
  }

  //获取已经选择的选项
  public List<Integer> getSelectPosition(ViewGroup parent) {
    List<Integer> indexList = new ArrayList<>();
    for (int i = 0; i < parent.getChildCount(); i++) {
      if (parent.getChildAt(i) instanceof ViewGroup) {
        View child = ((ViewGroup) parent.getChildAt(i)).getChildAt(2);
        if (child instanceof CheckBox) {
          if (((CheckBox) child).isChecked()) {
            indexList.add(i);
            //break;
          }
        }
      }
    }
    return indexList;
  }

  //单选控制
  public void checkSelection(View v) {
    if (v.getParent().getParent() != null && v.getParent().getParent() instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup) v.getParent().getParent();
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        if (viewGroup.getChildAt(i) instanceof ViewGroup) {
          View child = ((ViewGroup) viewGroup.getChildAt(i)).getChildAt(2);
          if (child instanceof CheckBox) {
            if (v.equals(child)) {
              continue;
            }
            ((CheckBox) child).setChecked(false);
          }
        }
      }
    }
  }

  public interface OnSelectionAnswerListener {
    void onSelected(int position);
  }
}
