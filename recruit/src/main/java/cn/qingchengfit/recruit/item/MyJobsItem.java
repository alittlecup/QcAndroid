package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class MyJobsItem extends AbstractFlexibleItem<MyJobsItem.MyJobsVH> {

  boolean hasNewInvited = false;

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

  @Override public MyJobsVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new MyJobsVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, MyJobsVH holder, int position,
      List payloads) {
    holder.vHasInvited.setVisibility(hasNewInvited ? View.VISIBLE : View.GONE);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class MyJobsVH extends FlexibleViewHolder {
    @BindView(R2.id.layout_i_sent) RelativeLayout layoutISent;
    @BindView(R2.id.v_has_invited) View vHasInvited;
    @BindView(R2.id.layout_i_invited) RelativeLayout layoutIInvited;
    @BindView(R2.id.layout_i_stared) RelativeLayout layoutIStared;

    public MyJobsVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
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