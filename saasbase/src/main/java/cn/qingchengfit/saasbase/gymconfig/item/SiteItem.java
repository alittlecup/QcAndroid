package cn.qingchengfit.saasbase.gymconfig.item;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.saasbase.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SiteItem extends AbstractFlexibleItem<SiteItem.SiteItemVH> {

  Space space;

  public SiteItem(Space space) {
    this.space = space;
  }

  public Space getSpace() {
    return space;
  }

  public void setSpace(Space space) {
    this.space = space;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_site;
  }

  @Override public SiteItemVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new SiteItemVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, SiteItemVH holder, int position,
    List payloads) {
    holder.text1.setText(space.getName());
    holder.text2.setText(space.getSupportString());
  }

  @Override public boolean equals(Object o) {
    return o instanceof SiteItem && ((SiteItem) o).getSpace()
      .getId()
      .equalsIgnoreCase(getSpace().getId());
  }

  public class SiteItemVH extends FlexibleViewHolder {
	TextView text1;
	TextView text2;
	ImageView righticon;
	CheckBox cb;
    public SiteItemVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      text1 = (TextView) view.findViewById(R.id.text1);
      text2 = (TextView) view.findViewById(R.id.text2);
      righticon = (ImageView) view.findViewById(R.id.righticon);
      cb = (CheckBox) view.findViewById(R.id.cb);

      cb.setClickable(false);
    }
  }
}