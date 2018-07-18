package cn.qingchengfit.student.item;

import android.view.View;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.databinding.StItemFollowRecordBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FollowRecordItem extends AbstractFlexibleItem<FollowRecordItem.FollowRecordVH> {

  FollowRecord followRecord;

  public FollowRecordItem(FollowRecord followRecord) {
    this.followRecord = followRecord;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record;
  }

  @Override public FollowRecordVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FollowRecordVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FollowRecordVH holder, int position,
    List payloads) {
    holder.binding.setInfo(followRecord);
  }

  public class FollowRecordVH extends FlexibleViewHolder{
    StItemFollowRecordBinding binding;
    public FollowRecordVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      binding = StItemFollowRecordBinding.bind(view);
    }
  }
}
