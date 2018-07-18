package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FollowRecordStatusItem
  extends AbstractFlexibleItem<FollowRecordStatusItem.ViewHolder> {

  FollowRecordStatus status;

  public FollowRecordStatusItem(FollowRecordStatus status) {
    this.status = status;
  }

  public FollowRecordStatus getStatus() {
    return status;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record_status;
  }

  @Override public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    ViewHolder vh = new ViewHolder(view, adapter);
    return vh;
  }

  @Override public boolean isSwipeable() {
    return true;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position,
    List payloads) {
    holder.t.setText(status.getTrack_status());
  }

  class ViewHolder extends FlexibleViewHolder {
    TextView t;
    View rightView;
    View fontView;

    private View.OnClickListener listener = v -> RxBus.getBus()
      .post(new EventClickViewPosition.Builder().id(v.getId())
        .position(getFlexibleAdapterPosition())
        .build());

    public ViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      t = view.findViewById(R.id.tv);
      rightView = view.findViewById(R.id.rear_right_view);
      fontView = view.findViewById(R.id.front_view);
      view.findViewById(R.id.btn_edit).setOnClickListener(listener);
      view.findViewById(R.id.btn_del).setOnClickListener(listener);
    }

    @Override protected boolean shouldActivateViewWhileSwiping() {
      return true;
    }

    @Override public View getFrontView() {
      return fontView;
    }

    @Override public View getRearRightView() {
      return rightView;
    }
  }
}
