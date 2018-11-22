package cn.qingchengfit.staffkit.dianping.item;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemSimpleChooseDataBinding;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SimpleChooseDataItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSimpleChooseDataBinding>> {
  private ISimpleChooseData data;

  public SimpleChooseDataItem(ISimpleChooseData data) {
    this.data = data;
  }

  public ISimpleChooseData getData() {
    return data;
  }

  @Override public boolean equals(Object o) {
    return o != null && o instanceof ISimpleChooseData && ((ISimpleChooseData) o).getSign()
        .equals(data.getSign());
  }

  @Override public int getLayoutRes() {
    return R.layout.item_simple_choose_data;
  }

  @Override public DataBindingViewHolder<ItemSimpleChooseDataBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemSimpleChooseDataBinding> holder, int position, List payloads) {
    ItemSimpleChooseDataBinding mBinding = holder.getDataBinding();
    mBinding.cb.setClickable(false);
    mBinding.cb.setChecked(adapter.isSelected(position));
    mBinding.tvText.setText(data.getText());
  }
}
