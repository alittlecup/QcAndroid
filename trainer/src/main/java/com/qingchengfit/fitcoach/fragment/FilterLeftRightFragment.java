package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/6/15.
 */

//两个列表关联的筛选
public class FilterLeftRightFragment extends BaseFragment {

  @BindView(R.id.filter_left_list) RecyclerView filterLeftList;
  @BindView(R.id.filter_right_list) RecyclerView filterRightList;
  private CommonFlexAdapter leftAdapter, rightAdapter;
  private OnLeftRightSelectListener listener;

  List<AbstractFlexibleItem> leftItemList = new ArrayList<>();
  List<AbstractFlexibleItem> rightItemList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_left_right_filter, container, false);
    ButterKnife.bind(this, view);

    initView();
    return view;
  }

  private void initView() {

    leftAdapter = new CommonFlexAdapter(leftItemList);
    leftAdapter.setMode(SelectableAdapter.MODE_SINGLE);
    leftAdapter.addListener(new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int position) {
        if (listener != null) {
          listener.onLeftSelected(position);
        }
        leftAdapter.toggleSelection(position);
        leftAdapter.notifyDataSetChanged();
        return true;
      }
    });
    filterLeftList.setLayoutManager(new LinearLayoutManager(getContext()));
    filterLeftList.setAdapter(leftAdapter);

    rightAdapter = new CommonFlexAdapter(rightItemList);
    rightAdapter.setMode(SelectableAdapter.MODE_SINGLE);
    rightAdapter.addListener(new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int position) {
        if (listener != null) {
          listener.onRightSelected(position);
        }
        rightAdapter.toggleSelection(position);
        rightAdapter.notifyDataSetChanged();
        return true;
      }
    });
    filterRightList.setLayoutManager(new LinearLayoutManager(getContext()));
    filterLeftList.setAdapter(rightAdapter);
  }

  public void setListener(OnLeftRightSelectListener listener){
    this.listener = listener;
  }

  public void setLeftItemList(List<? extends AbstractFlexibleItem> items) {
    if (items.size() > 0) {
      leftItemList.clear();
      leftItemList.addAll(items);
      leftAdapter.notifyDataSetChanged();
    }
  }

  public void setRightItemList(List<? extends AbstractFlexibleItem> items) {
    if (items.size() > 0) {
      rightItemList.clear();
      rightItemList.addAll(items);
      rightAdapter.notifyDataSetChanged();
    }
  }

  public void setFilterAnimation(final ViewGroup filterLayout, boolean isShow) {
    final ViewGroup.LayoutParams params = filterLayout.getLayoutParams();
    params.height = 0;
    filterLayout.setLayoutParams(params);
    final int startHeight;
    final int endHeight;
    if (isShow) {
      startHeight = 0;
      endHeight = leftAdapter.getItemCount() * MeasureUtils.dpToPx(40f, getResources());
    } else {
      startHeight = leftAdapter.getItemCount() * MeasureUtils.dpToPx(40f, getResources());
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

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public interface OnLeftRightSelectListener{
    void onLeftSelected(int position);
    void onRightSelected(int position);
  }

}
