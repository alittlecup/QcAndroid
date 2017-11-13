package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ImageActionItem extends AbstractFlexibleItem<ImageActionItem.ImageActionVH> {

  @DrawableRes int resDrawable;
  String txt;

  public ImageActionItem(int resDrawable, String txt) {
    this.resDrawable = resDrawable;
    this.txt = txt;
  }

  public int getResDrawable() {
    return resDrawable;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_image_action;
  }

  @Override public ImageActionVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ImageActionVH(view,adapter);
  }


  @Override public void bindViewHolder(FlexibleAdapter adapter, ImageActionVH holder, int position,
      List payloads) {
    holder.tv.setText(txt);
    holder.img.setImageResource(resDrawable);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ImageActionVH extends FlexibleViewHolder {
    @BindView(R2.id.tv) TextView tv;
    @BindView(R2.id.img) ImageView img;

    public ImageActionVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }


}