package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class Image140Item extends AbstractFlexibleItem<Image140Item.Image140VH> {

  String url;

  public Image140Item(String url) {
    this.url = url;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_imgs_show_item;
  }

  @Override public Image140VH createViewHolder(View view, FlexibleAdapter adapter) {
    return new Image140VH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, Image140VH holder, int position, List payloads) {
    PhotoUtils.middle(holder.img, url);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class Image140VH extends FlexibleViewHolder {
	ImageView img;

    public Image140VH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = (ImageView) view.findViewById(R.id.img);
    }
  }
}