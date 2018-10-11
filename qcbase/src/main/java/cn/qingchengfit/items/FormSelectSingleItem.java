package cn.qingchengfit.items;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import com.bigkoo.pickerview.lib.DensityUtil;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/8.
 */

public class FormSelectSingleItem
    extends AbstractFlexibleItem<FormSelectSingleItem.FormSelectSingleVH> {
  String data;

  public FormSelectSingleItem(String data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_linear_pop;
  }

  @Override public FormSelectSingleVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FormSelectSingleVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, FormSelectSingleVH holder, int position,
      List payloads) {
    holder.textPopFilterCommon.setText(data);
    Context context = holder.textPopFilterCommon.getContext();
    if (isEnabled()) {
      holder.textPopFilterCommon.setCompoundDrawablesWithIntrinsicBounds(null, null,
          (adapter.isSelected(position)) ? ContextCompat.getDrawable(context,
              R.drawable.vd_hook_primary) : null, null);
      holder.textPopFilterCommon.setTextColor(context.getResources().getColor(R.color.text_dark));
    } else {
      holder.textPopFilterCommon.setTextColor(context.getResources().getColor(R.color.text_grey));
    }
    ViewGroup.LayoutParams layoutParams = holder.textPopFilterCommon.getLayoutParams();
    layoutParams.height = DensityUtil.dip2px(context, 50);
    holder.textPopFilterCommon.setLayoutParams(layoutParams);
  }

  public class FormSelectSingleVH extends FlexibleViewHolder {

    TextView textPopFilterCommon;

    public FormSelectSingleVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textPopFilterCommon = view.findViewById(R.id.tv_filter_pop_linear_common);
    }
  }
}
