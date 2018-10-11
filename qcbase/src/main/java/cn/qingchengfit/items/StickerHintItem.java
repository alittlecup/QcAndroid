package cn.qingchengfit.items;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StickerHintItem extends AbstractFlexibleItem<StickerHintItem.StickerDateVH>
    implements IHeader<StickerHintItem.StickerDateVH> {

  String date;

  public StickerHintItem(String date) {
    this.date = date;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_sticker_hint;
  }

  @Override public StickerDateVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new StickerDateVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StickerDateVH holder, int position,
      List payloads) {
    holder.tvDate.setText(date);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class StickerDateVH extends FlexibleViewHolder {
	TextView tvDate;

    public StickerDateVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      tvDate = (TextView) view.findViewById(R.id.tv_date);
    }
  }
}

