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

public class StickerHintItem extends AbstractFlexibleItem<StickerHintItem.StickerDateVH>
    implements IHeader<StickerHintItem.StickerDateVH> {

  String date;

  public StickerHintItem(String date) {
    this.date = date;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_sticker_hint;
  }

  @Override public StickerDateVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new StickerDateVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StickerDateVH holder, int position,
      List payloads) {
    holder.tvDate.setText(date);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class StickerDateVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_date) TextView tvDate;

    public StickerDateVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      ButterKnife.bind(this, view);
    }
  }
}

