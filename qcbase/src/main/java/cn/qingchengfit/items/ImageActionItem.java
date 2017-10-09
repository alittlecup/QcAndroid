package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  @Override public ImageActionVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ImageActionVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ImageActionVH holder, int position,
      List payloads) {
    holder.tv.setText(txt);
    holder.tv.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(holder.itemView.getContext(),resDrawable),null,null);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ImageActionVH extends FlexibleViewHolder {
    @BindView(R2.id.tv) TextView tv;

    public ImageActionVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }


}