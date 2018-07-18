package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.ImageView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ItemGridImageAdd extends AbstractFlexibleItem<ItemGridImageAdd.GridImageAddVH>{

  public ItemGridImageAdd() {

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record_img_add;
  }

  @Override public GridImageAddVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new GridImageAddVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, GridImageAddVH holder, int position,
    List payloads) {
    holder.img.setImageResource(R.drawable.vd_add_grey_40dp);
    holder.img.setBackgroundResource(R.drawable.bg_rect_square_img);
  }

  public class GridImageAddVH extends FlexibleViewHolder{
    ImageView img;
    public GridImageAddVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = view.findViewById(R.id.img);
    }

  }
}
