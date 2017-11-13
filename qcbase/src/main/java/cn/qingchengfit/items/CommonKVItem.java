package cn.qingchengfit.items;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * j简单的KEyVale item
 */
public class CommonKVItem extends AbstractFlexibleItem<CommonKVItem.CommonKVVH> {

  protected String key, value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public CommonKVItem(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_common_kv;
  }

  @Override public CommonKVVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CommonKVVH(view,adapter);
  }


  @Override public void bindViewHolder(FlexibleAdapter adapter, CommonKVVH holder, int position,
    List payloads) {
    holder.tvKey.setText(key);
    holder.tvValue.setText(value);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CommonKVItem){
      return key.equalsIgnoreCase(((CommonKVItem) o).key);
    }else {
      return false;
    }

  }

  public class CommonKVVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_key) TextView tvKey;
    @BindView(R2.id.tv_value) TextView tvValue;
    public CommonKVVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}