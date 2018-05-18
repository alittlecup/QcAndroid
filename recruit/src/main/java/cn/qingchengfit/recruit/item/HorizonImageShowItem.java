package cn.qingchengfit.recruit.item;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

public class HorizonImageShowItem
    extends AbstractFlexibleItem<HorizonImageShowItem.ResumeIntentImgShowVH>
    implements FlexibleAdapter.OnItemClickListener {

  List<AbstractFlexibleItem> items;
  CommonFlexAdapter commonFlexAdapter;

  public HorizonImageShowItem(List<AbstractFlexibleItem> imgs) {
    this.items = imgs;
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  public IFlexible getChildItem(int pos) {
    if (commonFlexAdapter != null && commonFlexAdapter.getItemCount() > pos) {
      return commonFlexAdapter.getItem(pos);
    } else {
      return null;
    }
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_imgs_show;
  }

  @Override
  public ResumeIntentImgShowVH createViewHolder(View view, FlexibleAdapter adapter) {
    ResumeIntentImgShowVH holder =
        new ResumeIntentImgShowVH(view, adapter);
    if (holder.itemRv.getOnFlingListener() == null) {
      GravitySnapHelper helper = new GravitySnapHelper(Gravity.START);
      helper.attachToRecyclerView(holder.itemRv);
    }
    holder.itemRv.setLayoutManager(
        new LinearLayoutManager(holder.itemRv.getContext(), LinearLayoutManager.HORIZONTAL, false));
    holder.itemRv.setAdapter(commonFlexAdapter);
    holder.itemRv.addItemDecoration(
        new FlexibleItemDecoration(holder.itemView.getContext()).addItemViewType(
            R.layout.item_jobfairs, 10).withRightEdge(true));
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ResumeIntentImgShowVH holder, int position,
      List payloads) {
    if (items != null) {
      commonFlexAdapter.clear();
      for (AbstractFlexibleItem s : items) {
        commonFlexAdapter.addItem(s);
      }
    }
  }

  public void refresh(List<AbstractFlexibleItem> strings) {
    this.items = strings;
    commonFlexAdapter.clear();
    for (AbstractFlexibleItem string : strings) {
      commonFlexAdapter.addItem(string);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public boolean onItemClick(int i) {
    RxBus.getBus().post(new EventRecycleClick(i, commonFlexAdapter.getItemViewType(i)));
    return true;
  }

  public class ResumeIntentImgShowVH extends FlexibleViewHolder {
	RecyclerView itemRv;

    public ResumeIntentImgShowVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      itemRv = (RecyclerView) view.findViewById(R.id.item_rv);
    }
  }
}