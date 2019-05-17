package cn.qingchengfit.student.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class PtagQuestion {

  String title;
  String id;
  String remarks;
  int answer_type;
  int answer_length;
  boolean is_required;
  List<PtagAnswerOption> options;
  int sequence_number;
  String answer;
  String parent_question_id;
  int child_value_limits;
  boolean isShow;
  private Integer max_limits;
  private Integer min_limits;
  private String help_text;
  private boolean is_summary;
  private String replace_title;
  //记录编辑过程中的editText
  private String edit_text;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public String getRemarks() {
    return remarks;
  }

  public int getAnswer_type() {
    return answer_type;
  }

  public int getAnswer_length() {
    return answer_length;
  }

  public boolean isIs_required() {
    return is_required;
  }

  public int getSequence_number() {
    return sequence_number;
  }

  public String getAnswer() {
    return answer == null ? "" : answer;
  }

  public String getHelp_text() {
    return help_text;
  }

  public List<String> getAnswerList(){
    List<String> answers = new ArrayList<>();
    if (answer != null && !answer.isEmpty()) {
      Gson gson = new Gson();
      List<Integer> answerList =
          gson.fromJson(answer, new TypeToken<ArrayList<Integer>>() {
          }.getType());
      for (int answer : answerList) {
        answers.add(String.valueOf(answer));
      }
    }
    return answers;
  }

  public void setEdit_text(String edit_text) {
    this.edit_text = edit_text;
  }

  public String getEdit_text() {
    return edit_text == null ? "" : edit_text;
  }

  public List<PtagAnswerOption> getOptions() {
    return options;
  }

  public boolean isShow() {
    return isShow || (parent_question_id == null && !is_summary);
  }

  public void setShow(boolean show) {
    isShow = show;
  }

  public int getChild_value_limits() {
    return child_value_limits;
  }

  public String getParent_question_id() {
    return parent_question_id;
  }

  public Integer getMax_limits() {
    return max_limits;
  }

  public Integer getMin_limits() {
    return min_limits;
  }

  public boolean isIs_summary() {
    return is_summary;
  }

  public String getReplace_title() {
    return replace_title == null || replace_title.isEmpty() ? title : replace_title;
  }

  public void setReplace_title(String replace_title) {
    this.replace_title = replace_title;
  }
}
