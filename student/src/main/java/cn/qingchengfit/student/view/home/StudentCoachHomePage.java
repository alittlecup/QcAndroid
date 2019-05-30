package cn.qingchengfit.student.view.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.CoachMemberPtagData;
import cn.qingchengfit.student.bean.CoachPtagStat;
import cn.qingchengfit.student.bean.CoachStudentOverview;
import cn.qingchengfit.student.bean.ShopPtagStat;
import cn.qingchengfit.student.databinding.StudentCoachHomePageBinding;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.List;

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
    mViewModel.getStudentData();
    return mBinding;
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
    setAttendanceView(overview.getAttendenceStat());
  }

  private void setAttendanceView(CoachStudentOverview.AttendenceStat stat) {
    mBinding.tvAttendCountBelow.setText("最近7-30天缺勤  / " + stat.absence.count + "人");
    mBinding.tvAttendCountHeight.setText("最近30天出勤  / " + stat.attendance.count + "人");
    updateAvatars(stat.absence.avatars, R.id.img_absence_more);
    updateAvatars(stat.attendance.avatars, R.id.img_attendance_more);
  }

  private void updateAvatars(List<String> avatars, int id) {
    int width = DensityUtil.dip2px(getContext(), 28);
    if (avatars != null && !avatars.isEmpty()) {
      for (int i = avatars.size() - 1; i >= 0; i--) {
        ConstraintLayout.LayoutParams layoutParams =
            new ConstraintLayout.LayoutParams(width, width);
        String url = avatars.get(i);
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        layoutParams.rightMargin =
            DensityUtil.dip2px(getContext(), 14 + 24 * (avatars.size() - i - 1));
        layoutParams.topToTop = id;
        layoutParams.goneRightMargin =
            DensityUtil.dip2px(getContext(), 32 + 24 * (avatars.size() - i - 1));
        layoutParams.topMargin = DensityUtil.dip2px(getContext(), 4);
        layoutParams.rightToRight = id;
        PhotoUtils.smallCircle(imageView, url);
        mBinding.root.addView(imageView, layoutParams);
        if (avatars.size() - i >= 3) {
          mBinding.root.findViewById(id).setVisibility(View.VISIBLE);
          break;
        }
      }
    }
  }

  private void setItem(CommonInputView civ, CoachPtagStat data) {
    civ.setContent(String.valueOf(data.getCount()) + " 人");
    civ.setLabel(data.getTitle());
    Glide.with(getContext()).load(data.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Drawable drawable = new BitmapDrawable(getResources(), resource);
        drawable.setBounds(0, 0, MeasureUtils.dpToPx(18f, getResources()),
            MeasureUtils.dpToPx(18f, getResources()));
        civ.setLabelLeftDrawable(drawable);
      }
    });
  }

  private void setFeedBackItem(TextView tvScore, TextView tvStandard, CoachPtagStat data) {
    tvScore.setText(
        new SpanUtils().append(StringUtils.formatePrice(String.valueOf(data.getCount())))
            .append("人")
            .setFontSize(11, true)
            .create());
    Glide.with(getContext()).load(data.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Drawable drawable = new BitmapDrawable(getResources(), resource);
        drawable.setBounds(0, 0, MeasureUtils.dpToPx(18f, getResources()),
            MeasureUtils.dpToPx(18f, getResources()));
        tvStandard.setCompoundDrawables(drawable, null, null, null);
        tvStandard.setCompoundDrawablePadding(MeasureUtils.dpToPx(3f, getResources()));
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

  public void onClickMemberAttend(View v) {
    routeTo("/attendance/page", null);
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

  public void onShowHelpArtical(View v) {
    WebActivity.startWeb(Constants.PTAG_INTRODUCTION_ARTICAL, getContext());
  }
}
