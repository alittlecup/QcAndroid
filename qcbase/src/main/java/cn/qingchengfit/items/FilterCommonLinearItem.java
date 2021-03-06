package cn.qingchengfit.items;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/3/6.
 */

public class FilterCommonLinearItem
    extends AbstractFlexibleItem<FilterCommonLinearItem.FilterCommonLinearVH> {

  private String data;
  private boolean isShowState = true;

  public FilterCommonLinearItem(String data) {
    this.data = data;
  }

  public FilterCommonLinearItem(String data, boolean isShowState) {
    this.data = data;
    this.isShowState = isShowState;
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_linear_pop;
  }

  @Override public FilterCommonLinearVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FilterCommonLinearVH(view, adapter);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public String getData() {
    return data;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, FilterCommonLinearVH holder, int position,
      List payloads) {
    holder.textPopFilterCommon.setText(data);
    holder.textPopFilterCommon.setCompoundDrawablesWithIntrinsicBounds(null, null,
        (adapter.isSelected(position) && isShowState) ? ContextCompat.getDrawable(
            holder.textPopFilterCommon.getContext(), R.drawable.vd_hook_primary) : null, null);
    if ((adapter.isSelected(position) && isShowState)) {
      holder.textPopFilterCommon.setTextColor(
          holder.itemView.getResources().getColor(R.color.colorPrimary));
    }else{
      holder.textPopFilterCommon.setTextColor(
          holder.itemView.getResources().getColor(R.color.text_black));
    }
  }

  public class FilterCommonLinearVH extends FlexibleViewHolder {

    TextView textPopFilterCommon;
    //@BindView(R2.id.image_filter_pop_linear_confirm) ImageView imageViewFilterCommon;

    public FilterCommonLinearVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textPopFilterCommon = (TextView) view.findViewById(R.id.tv_filter_pop_linear_common);
    }
  }
}
