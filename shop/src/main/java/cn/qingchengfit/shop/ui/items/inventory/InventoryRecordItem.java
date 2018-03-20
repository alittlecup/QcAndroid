package cn.qingchengfit.shop.ui.items.inventory;

import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemInventoryRecordBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.RecordAction;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/16.
 */

public class InventoryRecordItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemInventoryRecordBinding>> {
  private IInventoryRecordData data;

  public InventoryRecordItem(IInventoryRecordData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_inventory_record;
  }

  @Override public DataBindingViewHolder<ItemInventoryRecordBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemInventoryRecordBinding> holder, int position, List payloads) {
    ItemInventoryRecordBinding dataBinding = holder.getDataBinding();
    dataBinding.productName.setText(data.getProductName());
    if (!TextUtils.isEmpty(data.getGoodName())) {
      dataBinding.goodName.setText("(" + data.getGoodName() + ")");
    }
    dataBinding.operatorDate.setText(cn.qingchengfit.utils.DateUtils.Date2YYYYMMDDHHmm(
        cn.qingchengfit.utils.DateUtils.formatDateFromServer(data.getCreateTime())));
    dataBinding.operatorName.setText(data.getCreateName());
    ViewUtil.setOperatorType(data.getOperatorType(), dataBinding.operatorWay);
    dataBinding.inventoryCount.setText("当前库存为:" + data.getInventorCount());
    switch (data.getOperatorType()) {
      case RecordAction.ADD:
      case RecordAction.RETURN:
        dataBinding.countOffset.setText("+" + data.getOffset());
        dataBinding.countOffset.setTextColor(
            dataBinding.countOffset.getContext().getColor(R.color.primary));
        break;
      case RecordAction.SALED:
        dataBinding.countOffset.setText( data.getOffset());
        dataBinding.countOffset.setTextColor(
            dataBinding.countOffset.getContext().getColor(R.color.inventory_reduce));
      case RecordAction.REDUCE:
        dataBinding.countOffset.setText("—" + data.getOffset());
        dataBinding.countOffset.setTextColor(
            dataBinding.countOffset.getContext().getColor(R.color.inventory_reduce));
        break;
    }
  }
}
