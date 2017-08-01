package cn.qingchengfit.notisetting.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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

  @Override public NotiFooterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new NotiFooterVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, NotiFooterVH holder, int position,
      List payloads) {
    holder.tv.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class NotiFooterVH extends FlexibleViewHolder {
    @BindView(R.id.tv) TextView tv;

    public NotiFooterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}