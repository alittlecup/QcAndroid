package cn.qingchengfit.recruit.item;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

public class ResumeIntentImgShowItem extends AbstractFlexibleItem<ResumeIntentImgShowItem.ResumeIntentImgShowVH>
    implements FlexibleAdapter.OnItemClickListener {

  List<String> imgs;

  public ResumeIntentImgShowItem(List<String> imgs) {
    this.imgs = imgs;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_imgs_show;
  }

  @Override public ResumeIntentImgShowVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeIntentImgShowVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeIntentImgShowVH holder, int position, List payloads) {
    if (imgs != null) {
      CommonFlexAdapter commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
      if (holder.itemRv.getOnFlingListener() == null) {
        GravitySnapHelper helper = new GravitySnapHelper(Gravity.START);
        helper.attachToRecyclerView(holder.itemRv);
      }
      holder.itemRv.setLayoutManager(new LinearLayoutManager(holder.itemRv.getContext(), LinearLayoutManager.HORIZONTAL, false));
      holder.itemRv.addItemDecoration(
          new cn.qingchengfit.utils.DividerItemDecoration(holder.itemRv.getContext(), LinearLayoutManager.HORIZONTAL, 6));
      holder.itemRv.setAdapter(commonFlexAdapter);
      for (String s : imgs) {
        commonFlexAdapter.addItem(new Image140Item(s));
      }
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public boolean onItemClick(int i) {
    // TODO: 2017/6/10 图片点击
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