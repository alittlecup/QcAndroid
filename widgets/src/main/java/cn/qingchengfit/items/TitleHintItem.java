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
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class TitleHintItem extends AbstractFlexibleItem<TitleHintItem.TitleHintVH> {

  protected String content;

  public TitleHintItem(String content) {
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_title_hint;
  }

  @Override public TitleHintVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new TitleHintVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, TitleHintVH holder, int position,
      List payloads) {
    holder.tv.setText(holder.tv.getContext().getString(R.string.title_hint_wrap, content));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class TitleHintVH extends FlexibleViewHolder {
    @BindView(R2.id.tv) TextView tv;

    public TitleHintVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}