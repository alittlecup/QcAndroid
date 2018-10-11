package cn.qingchengfit.student.item;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.animation.DecelerateInterpolator;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/2.
 */

public class BaseGirdListFragment extends BaseListFragment {
  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "";
  }

  @Override protected void initView(Bundle savedInstanceState) {
    super.initView(savedInstanceState);
    GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    rv.setLayoutManager(gridLayoutManager);
    rv.setItemViewCacheSize(4);
    srl.setEnabled(false);
    rv.setBackgroundColor(Color.WHITE);
    commonFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);
  }

  @Override protected void setAnimation() {
    commonFlexAdapter.setAnimationOnScrolling(true)
        .setAnimationInitialDelay(50L)
        .setAnimationInterpolator(new DecelerateInterpolator())
        .setAnimationDelay(100L);
  }

  @Override protected void addDivider() {
    //rv.addItemDecoration(new SpaceItemDecoration(20, getContext()));

  }

  public void setStaffs(List<Staff> list) {
    if (commonFlexAdapter != null) {
      datas.clear();
      if (list != null) {
        for (Staff item : list) {
          datas.add(generateItem(item));
        }
      }
      commonFlexAdapter.updateDataSet(datas);
    }
  }

  public AbstractFlexibleItem generateItem(Staff item) {
    return new ChooseSalerItem(item);
  }

  public void toggleSelection(int position) {
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyItemChanged(position);
  }

  public List<String> getSelectedItemIds() {
      List<Integer> integers = get();
      List<String> strings = new ArrayList<>();
      for (Integer pos : integers) {
        Staff saler = ((ChooseSalerItem) datas.get(pos)).getSaler();
        if(saler.getTeacher()!=null){
          strings.add(saler.getTeacher().getId());
        }else{
          strings.add(saler.getId());
        }
      }
      return strings;
  }
  public List<String> getSelectedItemNames() {
      List<Integer> integers = get();
      List<String> strings = new ArrayList<>();
      for (Integer pos : integers) {
          strings.add(((ChooseSalerItem)datas.get(pos)).getSaler().getUsername());
      }
      return strings;
  }

  public void notifyDataSetChanged() {
    commonFlexAdapter.notifyDataSetChanged();
  }

  public List<Integer> get() {
    return commonFlexAdapter.getSelectedPositions();
  }
}
