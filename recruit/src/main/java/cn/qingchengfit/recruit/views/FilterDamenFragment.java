package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AutoLineGroup;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import java.util.HashMap;

/**
 * Created by fb on 2017/6/15.
 */

public class FilterDamenFragment extends BaseFragment {

  @BindView(R2.id.layout_work_demand) AutoLineGroup layoutWorkDemand;
  @BindView(R2.id.tv_work_year_header) TextView workYearheader;
  @BindView(R2.id.alg_gender) AutoLineGroup algGender;
  @BindView(R2.id.alg_edu) AutoLineGroup algEdu;
  @BindView(R2.id.alg_age) AutoLineGroup algAge;
  @BindView(R2.id.alg_height) AutoLineGroup algHeight;
  @BindView(R2.id.filter_layout) LinearLayout filterLayout;
  @BindView(R2.id.alg_weight) AutoLineGroup algWeight;
  private OnDemandsListener listener;

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
    View view = inflater.inflate(R.layout.layout_damen_filter, container, false);
    unbinder = ButterKnife.bind(this, view);
    if (getArguments() != null && getArguments().getInt("t") == 0) {
      layoutWorkDemand.addChildren(getResources().getStringArray(R.array.filter_work_year));
    } else {
      layoutWorkDemand.setVisibility(View.GONE);
      workYearheader.setVisibility(View.GONE);
    }
    algGender.addChildren(getResources().getStringArray(R.array.filter_gender));
    algEdu.addChildren(getResources().getStringArray(R.array.filter_degree));
    algAge.addChildren(getResources().getStringArray(R.array.filter_age));
    algHeight.addChildren(getResources().getStringArray(R.array.filter_height));
    algWeight.addChildren(getResources().getStringArray(R.array.filter_weight));
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

  @OnClick({ R2.id.tv_student_filter_confirm }) public void onConfirm() {
    HashMap<String, Object> params = new HashMap<>();
    params = RecruitBusinessUtils.getWrokExpParams(layoutWorkDemand.getCheckPos(), params);
    params = RecruitBusinessUtils.getGenderParams(algGender.getCheckPos(), params);
    params = RecruitBusinessUtils.getDegreeParams(algEdu.getCheckPos(), params);
    params = RecruitBusinessUtils.getAgeParams(algAge.getCheckPos(), params);
    params = RecruitBusinessUtils.getHeightParams(algHeight.getCheckPos(), params);
    params = RecruitBusinessUtils.getWeightParams(algWeight.getCheckPos(), params);
    if (listener != null) {
      listener.onDemands(params);
    }
  }

  @OnClick(R2.id.tv_student_filter_reset) public void onReset() {
    layoutWorkDemand.clearAllCheck();
    algAge.clearAllCheck();
    algEdu.clearAllCheck();
    algHeight.clearAllCheck();
    algWeight.clearAllCheck();
    algGender.clearAllCheck();
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
