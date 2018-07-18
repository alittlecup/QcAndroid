package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.ImageView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ItemGridImage extends AbstractFlexibleItem<ItemGridImage.GridImageVH>{
  String url;

  public ItemGridImage(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override public boolean equals(Object o) {
    return o instanceof ItemGridImage && ((ItemGridImage) o).getUrl().equalsIgnoreCase(url);
  }

  @Override public int getLayoutRes() {
    return R.layout.st_item_follow_record_img;
  }

  @Override public GridImageVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new GridImageVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, GridImageVH holder, int position,
    List payloads) {
    PhotoUtils.small(holder.img,url);
  }

  public class GridImageVH extends FlexibleViewHolder{
    ImageView img;
    public GridImageVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = view.findViewById(R.id.img);
    }

  }
}
