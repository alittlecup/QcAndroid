package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.Constants;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.student.Utils;
import cn.qingchengfit.student.bean.CoachStudentPtagQuestionnaire;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.view.base.StudentBaseInfoBean;
import cn.qingchengfit.student.view.base.StudentBaseInfoParams;
import cn.qingchengfit.student.view.ptag.CoachPtagQuestionPageParams;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.views.activity.WebActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.databinding.FragmentStudentMoreInfoBinding;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 会员详情更多信息
 */
public class StudentMoreInfoFragment extends VpFragment {

  public static final String MEMBER_TRAIN_GOAL = "MEMBER_TRAIN_OBJECTIVES";
  public static final String MEMBER_TRAIN_MOTIVATION = "MEMBER_TRAIN_OBJECTIVES_MOTIVATION";
  public static final String MEMBER_SPORTS_STYLE = "MEMBER_SPORTS_STYLE";
  public static final String MEMBER_SPORTS_LEVEL = "MEMBER_SPORTS_LEVEL";

  @Inject TrainerRepository trainerRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentWrap studentWrap;

  private StudentBaseInfoBean bean;
  private FragmentStudentMoreInfoBinding binding;
  //是否已填写
  private boolean[] isAleadyWrited = new boolean[4];
  private boolean isShowDialogTip = true;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentStudentMoreInfoBinding.inflate(inflater, container, false);
    binding.setPage(this);
    return binding.getRoot();
  }

  public void setBean(StudentBaseInfoBean bean) {
    this.bean = bean;
  }

  @Override public String getTitle() {
    return "更多信息";
  }

  public void onPhysicalMeasurement(View v) {
    Intent toMain = new Intent(getContext(), FragActivity.class);
    toMain.putExtra("type", 16);
    getContext().startActivity(toMain);
  }

  @Override public void onResume() {
    super.onResume();
    getStudentPtagQuestionnaire();
  }

  private void getStudentPtagQuestionnaire() {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("user_id", studentWrap.id());
    RxRegiste(trainerRepository.getTrainerAllApi()
        .qcGetStudentPtagOverview(loginStatus.staff_id(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(coachStudentPtagQuestionnaireWrapperQcDataResponse -> {
          if (coachStudentPtagQuestionnaireWrapperQcDataResponse.status == 200) {
            int i = -1;
            for (CoachStudentPtagQuestionnaire naire : coachStudentPtagQuestionnaireWrapperQcDataResponse.data
                .getQuestin_naires()) {
              String answer = naire.getAnswer();
              if (answer.isEmpty()) {
                continue;
              }
              isShowDialogTip = false;
              switch (naire.getType()) {
                case MEMBER_TRAIN_GOAL:
                  i = 0;
                  binding.inputTrainerGoal.setContent(answer);
                  break;
                case MEMBER_SPORTS_LEVEL:
                  i = 2;
                  binding.inputSportLevel.setContent(answer);
                  break;
                case MEMBER_SPORTS_STYLE:
                  i = 1;
                  binding.inputTrainerStyle.setContent(answer);
                  break;
                case MEMBER_TRAIN_MOTIVATION:
                  i = 3;
                  binding.inputSportGoalMotivation.setContent(answer);
                  break;
              }
              if (i >= 0) {
                isAleadyWrited[i] = true;
              }
            }
          } else {
            ToastUtils.show(coachStudentPtagQuestionnaireWrapperQcDataResponse.status
                + ": "
                + coachStudentPtagQuestionnaireWrapperQcDataResponse.msg);
          }
        }));
  }

  public void onBaseInfo(View v) {
    if (bean == null) {
      DialogUtils.showAlert(getActivity(), "正在加载会员信息，请稍后...");
    } else {
      routeTo("student", "/student/detail/baseInfo",
          StudentBaseInfoParams.builder().bean(bean).build());
    }
  }

  public void onTrainerGoal() {
    routeToPage(MEMBER_TRAIN_GOAL, isAleadyWrited[0] ? studentWrap.id() : "");
  }

  public void onTrainerStyle() {
    routeToPage(MEMBER_SPORTS_STYLE, isAleadyWrited[1] ? studentWrap.id() : "");
  }

  public void onSportLevel() {
    routeToPage(MEMBER_SPORTS_LEVEL, isAleadyWrited[2] ? studentWrap.id() : "");
  }

  public void onSportGoalMotivation() {
    routeToPage(MEMBER_TRAIN_MOTIVATION, isAleadyWrited[3] ? studentWrap.id() : "",
        binding.inputTrainerGoal.getContent().equals("未填写")? "目标" : binding.inputTrainerGoal.getContent());
  }

  private void routeToPage(String type, String user_id) {
    routeTo("student", "/coach/ptag/question", CoachPtagQuestionPageParams.builder()
        .type(type)
        .userId(user_id)
        .isShow(isShowDialogTip)
        .build());
    isShowDialogTip = false;
  }

  private void routeToPage(String type, String user_id, String userGoal) {
    routeTo("student", "/coach/ptag/question", CoachPtagQuestionPageParams.builder()
        .type(type)
        .userId(user_id)
        .isShow(isShowDialogTip)
        .userTrainerGoal(userGoal)
        .build());
    isShowDialogTip = false;
  }

  public void onPlayVideo() {
    WebActivity.startWeb(Constants.PTAG_INTRODUCTION_ARTICAL, getContext());
  }

  public void onDownloadPDF() {
    Utils.openWithBrowser(Constants.PTAG_DOWNLOAD_PDF, getContext());
  }
}
