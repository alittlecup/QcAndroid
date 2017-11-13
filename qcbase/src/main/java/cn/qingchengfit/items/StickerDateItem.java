package cn.qingchengfit.items;

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
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StickerDateItem extends AbstractFlexibleItem<StickerDateItem.StickerDateVH>
    implements IHeader<StickerDateItem.StickerDateVH> {

  String date;

  public StickerDateItem(String date) {
    this.date = date;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_sticker_date;
  }

  @Override public StickerDateVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new StickerDateVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StickerDateVH holder, int position,
      List payloads) {
    holder.tvDate.setText(date);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof String){
      return ((String) o).equalsIgnoreCase(date);
    }else return false;

  }

  public class StickerDateVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_date) TextView tvDate;

    public StickerDateVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      ButterKnife.bind(this, view);
    }
  }
}