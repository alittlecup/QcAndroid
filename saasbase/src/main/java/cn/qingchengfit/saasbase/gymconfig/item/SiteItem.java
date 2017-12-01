package cn.qingchengfit.saasbase.gymconfig.item;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
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
    @BindView(R2.id.text1) TextView text1;
    @BindView(R2.id.text2) TextView text2;
    @BindView(R2.id.righticon) ImageView righticon;
    @BindView(R2.id.cb) CheckBox cb;
    public SiteItemVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}