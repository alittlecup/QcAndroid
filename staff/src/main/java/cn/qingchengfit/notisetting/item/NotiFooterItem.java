package cn.qingchengfit.notisetting.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class NotiFooterItem extends AbstractFlexibleItem<NotiFooterItem.NotiFooterVH> {

  String content;

  public NotiFooterItem(String content) {
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_noti_footer;
  }

  @Override public NotiFooterVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new NotiFooterVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, NotiFooterVH holder, int position,
      List payloads) {
    holder.tv.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class NotiFooterVH extends FlexibleViewHolder {
	TextView tv;

    public NotiFooterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tv = (TextView) view.findViewById(R.id.tv);
    }
  }
}