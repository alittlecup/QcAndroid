package cn.qingchengfit.student.view.ptag;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.CoachPtagAnswer;
import cn.qingchengfit.student.bean.CoachPtagAnswerBody;
import cn.qingchengfit.student.bean.CoachPtagQuestionnaire;
import cn.qingchengfit.student.bean.PtagQuestion;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.event.DynamicPtagItemEvent;
import cn.qingchengfit.student.item.CoachPtagItem;
import cn.qingchengfit.student.respository.StudentRepository;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

public class CoachPtagQuestionVM
    extends FlexibleViewModel<CoachPtagQuestionnaire, CoachPtagItem, HashMap<String, Object>> {

  @Inject StudentRepository repository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentWrap studentWrap;
  public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
  public MutableLiveData<CoachPtagQuestionnaire> data = new MutableLiveData<>();
  public ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
  public MutableLiveData<Boolean> isWarnning = new MutableLiveData<>();
  private List<CoachPtagItem> originList = new ArrayList<>();
  private String naireId = "";

  @Inject public CoachPtagQuestionVM() {

  }

  @NonNull @Override
  protected LiveData<CoachPtagQuestionnaire> getSource(@NonNull HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    //查看会员问卷内容
    if (params.containsKey("user_id")) {
      return Transformations.map(repository.qcGetPtagAnswers(loginStatus.staff_id(), params),
          input -> {
            if (input.data != null) data.setValue(input.data);
            isLoading.setValue(false);
            return dealResource(input);
          });
    }
    //查看会员训练反馈问卷内容
    if (params.containsKey("naire_id")) {
      naireId = (String) params.get("naire_id");
      return Transformations.map(repository.qcGetTrainerFeedbackNaire(naireId), input -> {
        if (input.data != null) data.setValue(input.data);
        isLoading.setValue(false);
        return dealResource(input);
      });
    }
    return Transformations.map(repository.qcGetPtagQuestionnaire(loginStatus.staff_id(), params),
        input -> {
          if (input.data != null) data.setValue(input.data);
          isLoading.setValue(false);
          return dealResource(input);
        });
  }

  @Override
  protected boolean isSourceValid(@Nullable CoachPtagQuestionnaire coachPtagQuestionnaire) {
    return coachPtagQuestionnaire != null;
  }

  @Override
  protected List<CoachPtagItem> map(@NonNull CoachPtagQuestionnaire coachPtagQuestionnaire) {
    List<CoachPtagItem> items = FlexibleItemProvider.with(new CoachPtagItemFactory())
        .from(coachPtagQuestionnaire.getQuestions());
    originList.addAll(items);
    return items;
  }

  public void onSave() {
    if (items == null) {
      return;
    }
    List<CoachPtagAnswer> answers = new ArrayList<>();
    for (AbstractFlexibleItem item : items.get()) {
      if (item instanceof CoachPtagItem) {
        CoachPtagAnswer result = ((CoachPtagItem) item).getResult();
        if (result.getAnswer() == null && ((CoachPtagItem) item).getData().isIs_required() || (
            result.getAnswer() != null
                && result.getAnswer().equals(""))) {
          isWarnning.setValue(true);
          return;
        }
        answers.add(result);
      }
    }
    if (naireId.isEmpty()) {
      //提交问卷
      repository.qcSubmitPtagAnswer(new CoachPtagAnswerBody.Builder().user_id(studentWrap.id())
          .question_naire_id(data.getValue().getId())
          .questions(answers)
          .build(), defaultResult);
    } else {
      //提交训练反馈
      HashMap<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("questions", answers);
      repository.qcModifyTrainerFeedbackNaire(naireId, bodyParams, defaultResult);
    }
  }

  //动态修改题目内容
  public void dynamicNotifyItem(DynamicPtagItemEvent event) {
    PtagQuestion question = new PtagQuestion();
    for (int i = 0; i < originList.size(); i++) {
      AbstractFlexibleItem item = originList.get(i);
      if (item != null) {
        PtagQuestion temp = ((CoachPtagItem) item).getData();
        if (temp.getId().equals(event.getParentId())) {
          //index = i;
          //break;
          question = temp;
        } else if (temp.getParent_question_id() != null && temp.getParent_question_id()
            .equals(question.getId())) {
          String newTitle = calTitle(event.getType(), event.getPosition(), question, temp);
          if (!newTitle.isEmpty()) {
            temp.setTitle(temp.getTitle()
                .replaceAll("((\\s|\\d)(.*)(\\s|\\d))|(\\d)", newTitle.replace("分", "")));
          }
          temp.setShow(!newTitle.isEmpty());
          ((CoachPtagItem) item).setPtagQuestion(temp);
          originList.set(i, (((CoachPtagItem) item)));
        }
      }
    }
    List<CoachPtagItem> newItems = new ArrayList<>(originList);
    if (getLiveItems() instanceof MutableLiveData) {
      ((MutableLiveData<List<CoachPtagItem>>) getLiveItems()).setValue(newItems);
    }
  }

  private String calTitle(int type, int position, PtagQuestion question, PtagQuestion data) {
    String str = "";
    switch (type) {
      case DynamicPtagItemEvent.TYPE_TRAINER_FEEDBACK:
        if (position > 0 && position < question.getOptions().size() - 1) {
          str =
              data.getChild_value_limits() < 0 ? question.getOptions().get(position - 1).getValue()
                  : question.getOptions().get(position + 1).getValue();
        } else if (position == 0 && data.getChild_value_limits() > 0) {
          str = question.getOptions().get(position + 1).getValue();
        } else if (position == question.getOptions().size() - 1
            && data.getChild_value_limits() < 0) {
          str = question.getOptions().get(position - 1).getValue();
        }
        break;
      case DynamicPtagItemEvent.TYPE_TRAINER_GOAL:
        int max = position + 2;
        int min = position - 2;
        if (max > 10) max = 10;
        if (min < 1) min = 1;
        if (position != 1 && data.getChild_value_limits() < 0) {
          str = String.valueOf(min);
        }
        if (position != 10 && data.getChild_value_limits() > 0) {
          str = String.valueOf(max);
        }
        break;
    }
    return str;
  }

  static class CoachPtagItemFactory
      implements FlexibleItemProvider.Factory<PtagQuestion, CoachPtagItem> {

    @NonNull @Override public CoachPtagItem create(PtagQuestion ptagQuestion) {
      return FlexibleFactory.create(CoachPtagItem.class, ptagQuestion);
    }
  }
}
