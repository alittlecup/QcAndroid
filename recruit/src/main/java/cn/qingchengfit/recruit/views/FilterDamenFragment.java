package cn.qingchengfit.recruit.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.databinding.LayoutDamenFilterBinding;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import java.util.HashMap;

/**
 * Created by fb on 2017/6/15.
 */

public class FilterDamenFragment extends BaseFragment {


  private OnDemandsListener listener;
  LayoutDamenFilterBinding db;

  public static FilterDamenFragment newInstanceResumeFilter() {
    Bundle args = new Bundle();
    args.putInt("t", 1);
    FilterDamenFragment fragment = new FilterDamenFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static FilterDamenFragment newInstanceJobsFilter() {
    Bundle args = new Bundle();
    args.putInt("t", 0);
    FilterDamenFragment fragment = new FilterDamenFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    db = DataBindingUtil.inflate(inflater,R.layout.layout_damen_filter, container, false);
    if (getArguments() != null && getArguments().getInt("t") == 0) {
      db.layoutWorkDemand.addChildren(getResources().getStringArray(R.array.filter_work_year));
    } else {
      db.layoutWorkDemand.setVisibility(View.GONE);
      db.tvWorkYearHeader.setVisibility(View.GONE);
      db.dividerWorkExp.setVisibility(View.GONE);
    }
    db.algGender.addChildren(getResources().getStringArray(R.array.filter_gender));
    db.algEdu.addChildren(getResources().getStringArray(R.array.filter_degree));
    db.algAge.addChildren(getResources().getStringArray(R.array.filter_age));
    db.algHeight.addChildren(getResources().getStringArray(R.array.filter_height));
    db.algWeight.addChildren(getResources().getStringArray(R.array.filter_weight));
    db.tvStudentFilterConfirm.setOnClickListener(view -> onConfirm());
    db.tvStudentFilterReset.setOnClickListener(view -> onReset());
    return db.getRoot();
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

   public void onConfirm() {
    HashMap<String, Object> params = new HashMap<>();
    params = RecruitBusinessUtils.getWrokExpParams(db.layoutWorkDemand.getCheckPos(), params);
    params = RecruitBusinessUtils.getGenderParams(db.algGender.getCheckPos(), params);
    params = RecruitBusinessUtils.getDegreeParams(db.algEdu.getCheckPos(), params);
    params = RecruitBusinessUtils.getAgeParams(db.algAge.getCheckPos(), params);
    params = RecruitBusinessUtils.getHeightParams(db.algHeight.getCheckPos(), params);
    params = RecruitBusinessUtils.getWeightParams(db.algWeight.getCheckPos(), params);
    if (listener != null) {
      listener.onDemands(params);
    }
  }

   public void onReset() {
    db.layoutWorkDemand.clearAllCheck();
    db.algAge.clearAllCheck();
    db.algEdu.clearAllCheck();
    db.algHeight.clearAllCheck();
    db.algWeight.clearAllCheck();
    db.algGender.clearAllCheck();
    if (listener != null) {
      listener.onDemandsReset();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public OnDemandsListener getListener() {
    return listener;
  }

  public void setListener(OnDemandsListener listener) {
    this.listener = listener;
  }

  public interface OnDemandsListener {
    void onDemands(HashMap<String, Object> params);

    void onDemandsReset();
  }
}
