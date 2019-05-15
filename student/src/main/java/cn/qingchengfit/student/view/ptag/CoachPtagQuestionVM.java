package cn.qingchengfit.student.view.ptag;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArraySet;
import android.util.Pair;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.CoachPtagAnswer;
import cn.qingchengfit.student.bean.CoachPtagAnswerBody;
import cn.qingchengfit.student.bean.CoachPtagQuestionnaire;
import cn.qingchengfit.student.bean.PtagAnswerOption;
import cn.qingchengfit.student.bean.PtagQuestion;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.event.DynamicPtagItemEvent;
import cn.qingchengfit.student.item.CoachPtagItem;
import cn.qingchengfit.student.respository.StudentRepository;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
  private Map<String, PtagAnswerOption> trainerStyleParams = new HashMap<>();
  private List<CoachPtagItem> originList = new ArrayList<>();
  private Set<String> idSet = new ArraySet<>();
  private String naireId = "";
  private int firstChildPosition = -1;

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
        if (!((CoachPtagItem) item).getData().isIs_summary() && (result.getAnswer() == null
            && ((CoachPtagItem) item).getData().isIs_required() || (result.getAnswer() != null
            && result.getAnswer().equals("")))) {
          isWarnning.setValue(true);
          return;
        }
        answers.add(result);
      }
    }
    for (CoachPtagItem removeItem : originList){
      if (!removeItem.getData().isShow()){
        CoachPtagAnswer answer = new CoachPtagAnswer();
        answer.setId(removeItem.getData().getId());
        answer.setAnswer("");
        answers.add(answer);
      }
    }
    Collections.sort(answers,
        (o1, o2) -> o1.getId().compareTo(o2.getId()));

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

  //动态标题题目预处理
  public List<CoachPtagItem> preHandlerItem(List<CoachPtagItem> coachPtagItems, String type,
      String userGoal) {
    Iterator<CoachPtagItem> itemIterator = coachPtagItems.iterator();
    while (itemIterator.hasNext()) {
      CoachPtagItem item = itemIterator.next();
      PtagQuestion data = item.getData();
      if (firstChildPosition < 0) {
        data.setShow(data.getAnswer() != null && !data.getAnswer().isEmpty());
      }
      if (userGoal != null && !userGoal.isEmpty() && data.getChild_value_limits() == 0) {
        data.setReplace_title(data.getTitle().replaceAll("_+", userGoal));
      }
      if (!data.isShow()) {
        itemIterator.remove();
      } else if (data.getAnswer() != null
          && !data.getAnswer().isEmpty()
          && data.getParent_question_id() != null) {
        item.setPtagQuestion(findParentQuestion(data, type));
      }
    }
    return coachPtagItems;
  }

  //动态修改题目内容
  public void dynamicNotifyItem(DynamicPtagItemEvent event) {
    PtagQuestion question = new PtagQuestion();
    firstChildPosition = 0;
    //如果是训练风格
    if (event.getType() == DynamicPtagItemEvent.TYPE_TRAINER_STYLE) {
      PtagAnswerOption option = event.getOption();
      idSet.add(option.getId());

      trainerStyleParams.put(event.getParentId(), option);
    }
    for (int i = 0; i < originList.size(); i++) {
      AbstractFlexibleItem item = originList.get(i);
      if (item != null) {
        PtagQuestion temp = ((CoachPtagItem) item).getData();
        if (temp.isIs_summary()) {
          //如果是需要总结的问题
          if (idSet.size() < originList.size() - 1) {
            continue;
          } else {
            temp.setReplace_title(
                temp.getTitle().replaceAll("_+", getUserTrainerStyle(trainerStyleParams)));
            temp.setShow(true);
            ((CoachPtagItem) item).setPtagQuestion(temp);
            originList.set(i, (((CoachPtagItem) item)));
          }
        } else if (temp.getId().equals(event.getParentId())) {
          //index = i;
          //break;
          question = temp;
          ((CoachPtagItem) item).setPtagQuestion(temp);
          originList.set(i, (((CoachPtagItem) item)));
        } else if (temp.getParent_question_id() != null && temp.getParent_question_id()
            .equals(question.getId())) {
          String newTitle = calTitle(event.getType(), event.getPosition(), question, temp);
          if (!newTitle.isEmpty()) {
            temp.setReplace_title(temp.getTitle().replaceAll("_+", newTitle.replace("分", "")));
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

  //获取最大权重值 --- 训练风格
  private String getUserTrainerStyle(Map<String, PtagAnswerOption> map) {
    int max = -1;
    String result = "";
    Map<String, Pair<Integer, String>> weightParams = new HashMap<>();
    for (String key : map.keySet()) {
      PtagAnswerOption option = map.get(key);
      if (weightParams.containsKey(option.getOption_type())) {
        Pair<Integer, String> pair =  weightParams.get(option.getOption_type());
        weightParams.put(option.getOption_type(),
            new Pair<>(pair.first + option.getWeight(), pair.second));
      } else {
        weightParams.put(option.getOption_type(), new Pair<>(option.getWeight(), option.getValue()));
      }
    }

    for (String k : weightParams.keySet()){
      if (weightParams.get(k).first > max){
        max = weightParams.get(k).first;
        result = weightParams.get(k).second;
      }
    }
    return result;
  }

  private PtagQuestion findParentQuestion(PtagQuestion child, String type) {
    if (firstChildPosition >= 0) {
      return child;
    }
    int t = type.equals("MEMBER_TRAINING_FEEDBACK") ? DynamicPtagItemEvent.TYPE_TRAINER_FEEDBACK
        : DynamicPtagItemEvent.TYPE_TRAINER_GOAL;

    for (PtagQuestion question : data.getValue().getQuestions()) {
      if (question.getId().equals(child.getParent_question_id())) {
        switch (t) {
          case DynamicPtagItemEvent.TYPE_TRAINER_FEEDBACK:
            for (int i = 0; i < question.getOptions().size(); i++) {
              if (question.getAnswerList().contains(question.getOptions().get(i).getId())) {
                firstChildPosition = i;
              }
            }
            break;
          case DynamicPtagItemEvent.TYPE_TRAINER_GOAL:
            firstChildPosition = (int) Float.parseFloat(question.getAnswer());
            break;
        }
        child.setReplace_title(child.getTitle()
            .replaceAll("_+", calTitle(t, firstChildPosition, question, child).replace("分", "")));
        break;
      }
    }
    return child;
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
