package cn.qingchengfit.saasbase.bill.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/29.
 */

public class ItemMoreFooter extends AbstractFlexibleItem<ItemMoreFooter.ItemMoreVH> {

  private OnFooterClickListener onFooterClickListener;

  public ItemMoreFooter(OnFooterClickListener onFooterClickListener) {
    this.onFooterClickListener = onFooterClickListener;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_more_footer;
  }

  @Override public ItemMoreVH createViewHolder(View view, FlexibleAdapter adapter) {
    ItemMoreVH holder = new ItemMoreVH(view, adapter);
    holder.textLoadMore.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (onFooterClickListener != null){
          onFooterClickListener.onLoadMore();
        }
      }
    });
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemMoreVH holder, int position,
      List payloads) {
  }

  class ItemMoreVH extends FlexibleViewHolder {

    @BindView(R2.id.text_load_more) TextView textLoadMore;

    public ItemMoreVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnFooterClickListener{
    void onLoadMore();
  }

}
