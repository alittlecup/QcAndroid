package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.AutoLineGroup;
import cn.qingchengfit.widgets.CheckBoxButton;
import cn.qingchengfit.widgets.CheckableButton;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/6/15.
 */

public class FilterDamenFragment extends BaseFragment {

  @BindView(R2.id.cbb_work_grauates) CheckBoxButton cbbWorkGrauates;
  @BindView(R2.id.cbb_work_one_three) CheckBoxButton cbbWorkOneThree;
  @BindView(R2.id.cbb_work_three_five) CheckBoxButton cbbWorkThreeFive;
  @BindView(R2.id.cbb_work_five_eight) CheckBoxButton cbbWorkFiveEight;
  @BindView(R2.id.cbb_work_eight_ten) CheckBoxButton cbbWorkEightTen;
  @BindView(R2.id.cbb_work_ten) CheckBoxButton cbbWorkTen;
  @BindView(R2.id.cbb_work_unlimited) CheckBoxButton cbbWorkUnlimited;
  @BindView(R2.id.cbb_male) CheckBoxButton cbbMale;
  @BindView(R2.id.cbb_female) CheckBoxButton cbbFemale;
  @BindView(R2.id.cbb_gender_unlimited) CheckBoxButton cbbGenderUnlimited;
  @BindView(R2.id.cbb_education_high_school) CheckBoxButton cbbEducationHighSchool;
  @BindView(R2.id.cbb_education_specialist) CheckBoxButton cbbEducationSpecialist;
  @BindView(R2.id.cbb_gender_undergraduate) CheckBoxButton cbbGenderUndergraduate;
  @BindView(R2.id.cbb_gender_postgraduate) CheckBoxButton cbbGenderPostgraduate;
  @BindView(R2.id.cbb_gender_doctor) CheckBoxButton cbbGenderDoctor;
  @BindView(R2.id.cbb_education_unlimited) CheckBoxButton cbbEducationUnlimited;
  @BindView(R2.id.cbb_age_twenty) CheckBoxButton cbbAgeTwenty;
  @BindView(R2.id.cbb_age_twenty_five) CheckBoxButton cbbAgeTwentyFive;
  @BindView(R2.id.cbb_age_thrity) CheckBoxButton cbbAgeThrity;
  @BindView(R2.id.cbb_fourty) CheckBoxButton cbbFourty;
  @BindView(R2.id.cbb_age_big_fourty) CheckBoxButton cbbAgeBigFourty;
  @BindView(R2.id.cbb_age_unlimited) CheckBoxButton cbbAgeUnlimited;
  @BindView(R2.id.cbb_height_s) CheckBoxButton cbbHeightS;
  @BindView(R2.id.cbb_height_m) CheckBoxButton cbbHeightM;
  @BindView(R2.id.cbb_height_l) CheckBoxButton cbbHeightL;
  @BindView(R2.id.cbb_height_xl) CheckBoxButton cbbHeightXl;
  @BindView(R2.id.cbb_height_xxl) CheckBoxButton cbbHeightXxl;
  @BindView(R2.id.cbb_height_unlimited) CheckBoxButton cbbHeightUnlimited;
  @BindView(R2.id.cbb_weight_s) CheckBoxButton cbbWeightS;
  @BindView(R2.id.cbb_weight_l) CheckBoxButton cbbWeightL;
  @BindView(R2.id.cbb_weight_xl) CheckBoxButton cbbWeightXl;
  @BindView(R2.id.cbb_weight_xxl) CheckBoxButton cbbWeightXxl;
  @BindView(R2.id.cbb_weight_xxxl) CheckBoxButton cbbWeightXxxl;
  @BindView(R2.id.cbb_weight_unlimited) CheckBoxButton cbbWeightUnlimited;
  @BindView(R2.id.tv_student_filter_reset) TextView tvStudentFilterReset;
  @BindView(R2.id.tv_student_filter_confirm) TextView tvStudentFilterConfirm;
  @BindView(R2.id.filter_layout) LinearLayout filterLayout;
  @BindView(R2.id.layout_radio_group) AutoLineGroup layoutRadioGroup;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_damen_filter, container, false);
    unbinder = ButterKnife.bind(this, view);
    layoutRadioGroup.setSpacing(MeasureUtils.dpToPx(10f, getResources())
        ,MeasureUtils.dpToPx(10f, getResources()));
    return view;
  }

  public void setFilterAnimation(final ViewGroup filterLayout, boolean isShow) {
    final ViewGroup.LayoutParams params = filterLayout.getLayoutParams();
    params.height = 0;
    filterLayout.setLayoutParams(params);
    final int startHeight;
    final int endHeight;
    if (isShow) {
      startHeight = 0;
      endHeight = filterLayout.getHeight();
    } else {
      startHeight = filterLayout.getHeight();
      endHeight = 0;
    }
    ValueAnimator valueAnimator = ObjectAnimator.ofFloat(startHeight, endHeight);
    valueAnimator.setDuration(500);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        if (startHeight < endHeight) {
          params.height = (int) (endHeight * fraction);
        } else {
          params.height = startHeight - (int) (startHeight * fraction);
        }
        filterLayout.setLayoutParams(params);
      }
    });
    valueAnimator.start();
  }

  @OnClick({R2.id.tv_student_filter_confirm})
  public void onConfirm(){
    Log.e("----fb---", getChecked() + "");
  }

  @OnClick(R2.id.tv_student_filter_reset)
  public void onReset(){
    onResetFilter();
  }

  private void onResetFilter(){
    for(int i = 0; i < filterLayout.getChildCount();i++){
      if (filterLayout.getChildAt(i) instanceof LinearLayout){
        LinearLayout linear = (LinearLayout) filterLayout.getChildAt(i);
        if (linear.getChildAt(1) instanceof AutoLineGroup) {
          AutoLineGroup autoLineGroup = (AutoLineGroup) linear.getChildAt(1);
          for (int j = 0; j < autoLineGroup.getChildCount(); j++) {
            if (autoLineGroup.getChildAt(j) instanceof CheckBoxButton){
              ((CheckBoxButton) autoLineGroup.getChildAt(j)).setChecked(false);

            }
          }
        }
      }
    }
  }

  private List<String> getChecked(){
    List<String> damenFilter = new ArrayList<>();
    for(int i = 0; i < filterLayout.getChildCount();i++){
      if (filterLayout.getChildAt(i) instanceof LinearLayout){
        LinearLayout linear = (LinearLayout) filterLayout.getChildAt(i);
        if (linear.getChildAt(1) instanceof AutoLineGroup) {
          AutoLineGroup autoLineGroup = (AutoLineGroup) linear.getChildAt(1);
          for (int j = 0; j < autoLineGroup.getChildCount(); j++) {
            if (autoLineGroup.getChildAt(j) instanceof CheckBoxButton){
              if (((CheckBoxButton) autoLineGroup.getChildAt(j)).isChecked()){
                damenFilter.add(((CheckBoxButton) autoLineGroup.getChildAt(j)).getContent());
              }
            }
          }
        }
      }
    }
    return damenFilter;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
