package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FollowRecordStatusItem extends AbstractFlexibleItem<FollowRecordStatusItem.ViewHolder> {

  FollowRecordStatus status;

  public FollowRecordStatusItem(FollowRecordStatus status) {
    this.status = status;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record_status;
  }

  @Override public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    ViewHolder vh = new ViewHolder(view,adapter);
    return vh;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position,
    List payloads) {
    holder.t.setText(status.getTrack_status());
  }

  class ViewHolder extends FlexibleViewHolder{
    TextView t;
    View rightView;
    public ViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      t = view.findViewById(R.id.tv);
      rightView = view.findViewById(R.id.rear_right_view);
      rightView.setOnClickListener(v -> {

      });
    }

    public View getRightView() {
      return rightView;
    }

  }
}
