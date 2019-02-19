package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingcheng.gym.vo.IMyGymsItemData;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyMyGymsItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class MyGymsItem extends AbstractFlexibleItem<DataBindingViewHolder<GyMyGymsItemBinding>> {
  private IMyGymsItemData data;

  public MyGymsItem(IMyGymsItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_my_gyms_item;
  }

  @Override public DataBindingViewHolder<GyMyGymsItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyMyGymsItemBinding> holder, int position, List payloads) {
    GyMyGymsItemBinding dataBinding = holder.getDataBinding();
    dataBinding.setData(data);
    if (data.getBrandPhoto() != null) {
      PhotoUtils.smallCircle(dataBinding.imgPhoto, data.getBrandPhoto());
    }
  }
}
