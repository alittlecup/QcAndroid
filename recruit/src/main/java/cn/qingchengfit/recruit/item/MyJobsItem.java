package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class MyJobsItem extends AbstractFlexibleItem<MyJobsItem.MyJobsVH> {

  boolean hasNewInvited = true;

  public MyJobsItem(boolean hasNewInvited) {
    this.hasNewInvited = hasNewInvited;
  }

  public boolean isHasNewInvited() {
    return hasNewInvited;
  }

  public void setHasNewInvited(boolean hasNewInvited) {
    this.hasNewInvited = hasNewInvited;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_my_jobs;
  }

  @Override public MyJobsVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new MyJobsVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, MyJobsVH holder, int position,
      List payloads) {
    holder.vHasInvited.setVisibility(hasNewInvited ? View.VISIBLE : View.GONE);
  }

  @Override public boolean equals(Object o) {
    return o instanceof MyJobsItem;
  }

  public class MyJobsVH extends FlexibleViewHolder {
	RelativeLayout layoutISent;
	View vHasInvited;
	RelativeLayout layoutIInvited;
	RelativeLayout layoutIStared;

    public MyJobsVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      layoutISent = (RelativeLayout) view.findViewById(R.id.layout_i_sent);
      vHasInvited = (View) view.findViewById(R.id.v_has_invited);
      layoutIInvited = (RelativeLayout) view.findViewById(R.id.layout_i_invited);
      layoutIStared = (RelativeLayout) view.findViewById(R.id.layout_i_stared);

      layoutIInvited.setOnClickListener(this);
      layoutISent.setOnClickListener(this);
      layoutIStared.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (view.getId() == R.id.layout_i_sent
          || view.getId() == R.id.layout_i_invited
          || view.getId() == R.id.layout_i_stared) {
        RxBus.getBus().post(new EventClickViewPosition.Builder().id(view.getId()).build());
        return;
      }
      super.onClick(view);
    }
  }
}