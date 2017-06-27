package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/6/15.
 */

//两个列表关联的筛选
public class FilterLeftRightFragment extends BaseFragment{

  @BindView(R2.id.filter_left_list) RecyclerView filterLeftList;
  @BindView(R2.id.filter_right_list) RecyclerView filterRightList;
  List<AbstractFlexibleItem> leftItemList = new ArrayList<>();
  List<AbstractFlexibleItem> rightItemList = new ArrayList<>();
  private CommonFlexAdapter leftAdapter, rightAdapter;
  private OnLeftRightSelectListener listener;
  private int lastPosition = -1;

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
        clearSelection(lastPosition);
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
        lastPosition = position;
        rightAdapter.toggleSelection(position);
        rightAdapter.notifyDataSetChanged();
        return true;
      }
    });
    filterRightList.setLayoutManager(new LinearLayoutManager(getContext()));
    filterRightList.setAdapter(rightAdapter);
  }

  public void setListener(OnLeftRightSelectListener listener){
    this.listener = listener;
  }

  public void setLeftItemList(List<String> datas) {
    if (datas.size() > 0) {
      leftItemList.clear();
      for (String data : datas){
        leftItemList.add(new FilterCommonLinearItem(data, false));
      }
      leftAdapter.notifyDataSetChanged();
    }
  }

  public void setRightItemList(List<String> datas) {
    if (datas.size() > 0) {
      rightItemList.clear();
      for (String data : datas){
        rightItemList.add(new FilterCommonLinearItem(data));
      }
      rightAdapter.notifyDataSetChanged();
    }
  }

  public void onChangedCity(List<CityBean> cityBeanList){
    if (cityBeanList.size() > 0) {
      rightItemList.clear();
      for (CityBean cityBean : cityBeanList){
        rightItemList.add(new FilterCommonLinearItem(cityBean.name));
      }
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

  private void clearSelection(int lastPosition){
    if (lastPosition >= 0){
      if (rightAdapter.isSelected(lastPosition)){
        rightAdapter.removeSelection(lastPosition);
        rightAdapter.notifyItemChanged(lastPosition);
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public interface OnLeftRightSelectListener{
    void onLeftSelected(int position);
    void onRightSelected(int position);
  }

}
