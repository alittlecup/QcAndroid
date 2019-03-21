package cn.qingchengfit.gym.item;

import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyMyGymsItemBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class MyGymsItem extends AbstractFlexibleItem<DataBindingViewHolder<GyMyGymsItemBinding>> {
  private Brand data;

  public Brand getData() {
    return data;
  }

  boolean isCreated;

  public MyGymsItem(Brand data, boolean isCreated) {
    this.data = data;
    this.isCreated = isCreated;
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
    dataBinding.tvManage.setVisibility(isCreated ? View.VISIBLE : View.GONE);
    dataBinding.imgArrowRight.setVisibility(isCreated ? View.GONE : View.VISIBLE);
    Glide.with(dataBinding.getRoot().getContext())
        .load(PhotoUtils.getSmall(data.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(dataBinding.imgPhoto, dataBinding.getRoot().getContext()));
  }
}
