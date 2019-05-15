package cn.qingchengfit.student.view.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.Utils;
import cn.qingchengfit.student.bean.CoachMemberPtagData;
import cn.qingchengfit.student.bean.CoachPtagStat;
import cn.qingchengfit.student.bean.CoachStudentOverview;
import cn.qingchengfit.student.bean.ShopPtagStat;
import cn.qingchengfit.student.databinding.StudentCoachHomePageBinding;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

@Leaf(module = "student", path = "/student/coach/home/") public class StudentCoachHomePage
    extends SaasBindingFragment<StudentCoachHomePageBinding, StudentCoachHomeVM> {

  private CoachMemberPtagData trainingGoal;
  private CoachMemberPtagData trainingFeedback;

  @Override protected void subscribeUI() {
    mViewModel.overview.observe(this, coachStudentOverview -> {
      if (coachStudentOverview != null) loadDataToUI(coachStudentOverview);
    });
  }

  @Override
  public StudentCoachHomePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StudentCoachHomePageBinding.inflate(inflater, container, false);
    mBinding.setPage(this);
    mBinding.setToolbarModel(new ToolbarModel("会员"));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
  }

  @Override protected boolean isfitSystemPadding() {
    return false;
  }

  @Override public void onResume() {
    super.onResume();
    mViewModel.getStudentData();
  }

  private void loadDataToUI(CoachStudentOverview overview) {
    SpannableStringBuilder text = new SpanUtils().append(
        StringUtils.formatePrice(String.valueOf(overview.getAll_user_count())))
        .append("人")
        .setFontSize(11, true)
        .create();
    mBinding.tvAllStudent.setText(text);
    mBinding.tvRegisterRecent.setText(
        getString(R.string.coach_student_new_register, overview.getRegistered_users_count()));
    mBinding.tvAlreadyApproach.setText(
        getString(R.string.coach_student_already_follow, overview.getFollowing_users_count()));
    mBinding.tvStudent.setText(
        getString(R.string.coach_student_member, overview.getMember_users_count()));
    ShopPtagStat stat = overview.getShop_ptag_stat();
    trainingGoal = stat.getMEMBER_TRAIN_OBJECTIVES();
    trainingFeedback = stat.getMEMBER_TRAINING_FEEDBACK();
    mBinding.tvTrainerFeedback.setText(trainingFeedback.getTitle());
    setFeedBackItem(mBinding.tvLowScoreMember, mBinding.tvLowStandard,
        trainingFeedback.getStat().get(0));
    setFeedBackItem(mBinding.tvMediumScoreMember, mBinding.tvMediumStandard,
        trainingFeedback.getStat().get(1));
    setFeedBackItem(mBinding.tvHighScoreMember, mBinding.tvHighStandard,
        trainingFeedback.getStat().get(2));

    mBinding.vdTrainerGoal.setText(trainingGoal.getTitle());

    setItem(mBinding.civInputLostWeight, trainingGoal.getStat().get(0));
    setItem(mBinding.civInputChangeHealth, trainingGoal.getStat().get(1));
    setItem(mBinding.civInputAddMuscle, trainingGoal.getStat().get(2));
    setItem(mBinding.civInputSportPerformance, trainingGoal.getStat().get(3));
  }

  private void setItem(CommonInputView civ, CoachPtagStat data) {
    civ.setContent(String.valueOf(data.getCount()) + " 人");
    civ.setLabel(data.getTitle());
    Glide.with(getContext()).load(data.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        civ.setLabelLeftDrawable(new BitmapDrawable(getResources(), resource));
      }
    });
  }

  private void setFeedBackItem(TextView tvScore, TextView tvStandard, CoachPtagStat data) {
    tvScore.setText(new SpanUtils().append(
        StringUtils.formatePrice(String.valueOf(data.getCount())))
        .append("人")
        .setFontSize(11, true)
        .create());
    Glide.with(getContext()).load(data.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        tvStandard.setCompoundDrawablesWithIntrinsicBounds(
            new BitmapDrawable(getResources(), resource), null, null, null);
        tvStandard.setCompoundDrawablePadding(MeasureUtils.dpToPx(5f, getResources()));
        tvStandard.setText(data.getTitle());
      }
    });
  }

  public void onClickAllStudent(View v) {
    routeTo("student", "/student/coach/all",
        new StudentCoachAllPageParams().filterValue("").filterName("").build());
  }

  public void onClickLosWeight(View v) {
    routeToWithParams(trainingGoal, 0);
  }

  public void onClickChangeHealth(View v) {
    routeToWithParams(trainingGoal, 1);
  }

  public void onClickAddMosule(View v) {
    routeToWithParams(trainingGoal, 2);
  }

  public void onClickSoprtPerformance(View v) {
    routeToWithParams(trainingGoal, 3);
  }

  public void onClickLowScore(View v) {
    routeToWithParams(trainingFeedback, 0);
  }

  public void onClickMediumScore(View v) {
    routeToWithParams(trainingFeedback, 1);
  }

  public void onClickHighScore(View v) {
    routeToWithParams(trainingFeedback, 2);
  }

  private void routeToWithParams(CoachMemberPtagData data, int position) {
    if (data != null) {
      routeTo("student", "/student/coach/all",
          new StudentCoachAllPageParams().filterName(data.getTitle())
              .filterValue(data.getStat().get(position).getOption_type())
              .build());
    }
  }

  public void onPlayVideo(View v) {
    //Utils.playVideoFromUrl(Constants.PTAG_VIDEO, getContext());
    WebActivity.startWeb(Constants.PTAG_INTRODUCTION_ARTICAL, getContext());
  }

  public void onShowHelpArtical(View v){
    WebActivity.startWeb(Constants.PTAG_INTRODUCTION_ARTICAL, getContext());
  }
}
