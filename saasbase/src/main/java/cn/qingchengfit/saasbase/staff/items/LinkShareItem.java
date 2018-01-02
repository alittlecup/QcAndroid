package cn.qingchengfit.saasbase.staff.items;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class LinkShareItem extends AbstractFlexibleItem<LinkShareItem.LinkShareVH> {

  int resOn;
  int resOff;
  int txt;

  public LinkShareItem(int resOn, int resOff, int txt) {
    this.resOn = resOn;
    this.resOff = resOff;
    this.txt = txt;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_link_share;
  }

  @Override public LinkShareVH createViewHolder(View v, FlexibleAdapter adapter) {
    return new LinkShareVH(v, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, LinkShareVH holder, int position,
    List payloads) {
    boolean isSelected = adapter.isSelected(position);
    holder.img.setImageResource(isSelected?resOn:resOff);
    holder.imgCheck.setVisibility(isSelected?View.VISIBLE:View.GONE);
    holder.tv.setText(txt);
    holder.tv.setTextColor(ContextCompat.getColor(holder.tv.getContext(),isSelected?R.color.btn_text_primary_color:R.color.text_dark));
    holder.itemView.setBackgroundResource(isSelected?R.drawable.bg_rect_prime:R.drawable.bg_rect_grey);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class LinkShareVH extends FlexibleViewHolder {
    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.tv) TextView tv;
    @BindView(R2.id.layout) LinearLayout layout;
    @BindView(R2.id.img_check) ImageView imgCheck;
    public LinkShareVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}