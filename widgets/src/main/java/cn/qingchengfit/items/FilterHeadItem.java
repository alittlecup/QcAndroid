package cn.qingchengfit.items;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.Arrays;
import java.util.List;

public class FilterHeadItem extends AbstractFlexibleItem<FilterHeadItem.FilterHeadVH>
    implements IHeader<FilterHeadItem.FilterHeadVH> {

  public List<String> strings;

  public FilterHeadItem(List<String> strings) {
    this.strings = strings;
  }

  public FilterHeadItem(String... strings) {
    this.strings = Arrays.asList(strings);
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_linear_horizon;
  }

  @Override public FilterHeadVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new FilterHeadVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FilterHeadVH holder, int position,
      List payloads) {
    if (strings != null) {
      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
      for (String s : strings) {
        TextView textView = new TextView(holder.itemView.getContext());
        CompatUtils.setTextStyle(textView, R.style.QcTextStyleMediumDark);
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
            ContextCompat.getDrawable(holder.itemView.getContext(),
                R.drawable.ic_arrow_down_black_16dp), null);
        textView.setCompoundDrawablePadding(
            (int) holder.itemView.getContext().getResources().getDimension(R.dimen.little));
        holder.layoutHorizon.addView(textView, lp);
      }
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof FilterHeadItem;
  }

  public class FilterHeadVH extends FlexibleViewHolder {
    @BindView(R2.id.layout_horizon) LinearLayout layoutHorizon;

    public FilterHeadVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      ButterKnife.bind(this, view);
    }
  }
}