package cn.qingchengfit.student.item;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.databinding.StItemFollowRecordBinding;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FollowRecordItem extends AbstractFlexibleItem<FollowRecordItem.FollowRecordVH> {

  FollowRecord followRecord;

  public FollowRecordItem(FollowRecord followRecord) {
    this.followRecord = followRecord;
  }

  public FollowRecord getData() {
    return followRecord;
  }

  @Override public boolean equals(Object o) {
    return o instanceof FollowRecord&&((FollowRecord) o).getId().equals(followRecord.getId());
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record;
  }

  @Override public FollowRecordVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FollowRecordVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FollowRecordVH holder, int position,
      List payloads) {
    holder.binding.setInfo(followRecord);
    if (TextUtils.isEmpty(followRecord.getFollowStatusString())) {
      holder.binding.tvFollowStatus.setVisibility(View.GONE);
    } else {
      holder.binding.tvFollowStatus.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(followRecord.getFollowTimeString())) {
      holder.binding.tvNextTime.setVisibility(View.GONE);
    } else {
      holder.binding.tvNextTime.setVisibility(View.VISIBLE);
    }
    holder.binding.tvGymName.setText(followRecord.getShop());


    //holder.binding.img01.setVisibility(followRecord.getAttachSize() > 0 ?View.VISIBLE:View.GONE);
    //holder.binding.img02.setVisibility(followRecord.getAttachSize() > 1 ?View.VISIBLE:View.GONE);
    //holder.binding.img03.setVisibility(followRecord.getAttachSize() > 2 ?View.VISIBLE:View.GONE);
    //
    //PhotoUtils.small(holder.binding.img01,followRecord.getImagePos(0));
    //PhotoUtils.small(holder.binding.img02,followRecord.getImagePos(1));
    //PhotoUtils.small(holder.binding.img03,followRecord.getImagePos(2));

  }

  public class FollowRecordVH extends FlexibleViewHolder {
    StItemFollowRecordBinding binding;

    public FollowRecordVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      binding = StItemFollowRecordBinding.bind(view);
      binding.llImage.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (view.getId() == R.id.ll_image) {
        super.onClick(view);
      }
    }
  }
}
