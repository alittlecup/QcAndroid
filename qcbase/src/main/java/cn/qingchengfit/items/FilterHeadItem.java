package cn.qingchengfit.items;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.widgets.QcFilterToggle;
import cn.qingchengfit.widgets.QcRadioGroup;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterHeadItem extends AbstractFlexibleItem<FilterHeadItem.FilterHeadVH>
    implements IHeader<FilterHeadItem.FilterHeadVH> {

  public List<String> strings;
  public List<Boolean> highLight = new ArrayList<>();
  private FilterHeadListener listener;
  private boolean clearToggle = false;

  public FilterHeadItem(List<String> strings) {
    this.strings = strings;
  }

  public FilterHeadItem(String... strings) {
    this.strings = Arrays.asList(strings);
  }

  public List<Boolean> getHighLight() {
    return highLight;
  }

  public void setHighLight(List<Boolean> highLight) {
    this.highLight = highLight;
  }

  public void setHighLight(Boolean... highLight) {
    this.highLight = Arrays.asList(highLight);
  }

  public List<String> getStrings() {
    return strings;
  }

  public void setStrings(List<String> strings) {
    this.strings = strings;
  }

  public void setStrings(String... strings) {
    this.strings = Arrays.asList(strings);
  }

  @Override public int getLayoutRes() {
    return R.layout.item_horizon_qcradiogroup;
  }

  @Override public FilterHeadVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FilterHeadVH(view,adapter);
  }


  @Override public void bindViewHolder(FlexibleAdapter adapter, FilterHeadVH holder, int position,
      List payloads) {
    if (strings != null) {
      holder.qrg.removeAllViews();
      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
      for (int i = 0; i < strings.size(); i++) {
        String s = strings.get(i);
        QcFilterToggle qcFilterToggle = new QcFilterToggle(holder.itemView.getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          qcFilterToggle.setId(View.generateViewId());
        }
        qcFilterToggle.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            //并没有什么用，只是为了 避免出发自定义方法中的 click
          }
        });
        qcFilterToggle.setStyle(R.style.QcFilterStyle);
        if (highLight.size() > i && highLight.get(i)) {
          qcFilterToggle.setTextColorRes(R.color.colorPrimary);
        } else {
          qcFilterToggle.setTextColorRes(R.color.text_black);
        }
        qcFilterToggle.setText(s);
        holder.qrg.addView(qcFilterToggle, lp);
      }
    }
    if (clearToggle) {
      holder.qrg.clearCheck();
      clearToggle = false;
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof FilterHeadItem;
  }

  public FilterHeadListener getListener() {
    return listener;
  }

  public void setListener(FilterHeadListener listener) {
    this.listener = listener;
  }

  public void clearAll() {
    clearToggle = true;
  }

  public interface FilterHeadListener {
    void onPositionClick(int pos);
  }

  public class FilterHeadVH extends FlexibleViewHolder {
	QcRadioGroup qrg;

    public FilterHeadVH(View view, FlexibleAdapter adapter) {
      super(view, adapter, true);
      qrg = (QcRadioGroup) view.findViewById(R.id.qrg);

      qrg.setCheckedChange(new QcRadioGroup.CheckedChange() {
        @Override public void onCheckedChange() {
          if (listener != null) listener.onPositionClick(qrg.getCheckedPos());
        }
      });
    }
  }
}