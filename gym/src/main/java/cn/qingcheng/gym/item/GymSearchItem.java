package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingcheng.gym.bean.BrandWithGyms;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymSearchItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymSearchItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymSearchItemBinding>> {
  public BrandWithGyms getData() {
    return data;
  }

  private BrandWithGyms data;

  public GymSearchItem(BrandWithGyms data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_search_item;
  }

  @Override public DataBindingViewHolder<GyGymSearchItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymSearchItemBinding> holder, int position, List payloads) {
    GyGymSearchItemBinding dataBinding = holder.getDataBinding();
    dataBinding.setData(data.brand);
    Glide.with(dataBinding.getRoot().getContext())
        .load(PhotoUtils.getSmall(data.brand.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(dataBinding.imgGymPhoto, dataBinding.getRoot().getContext()));
    dataBinding.tvGmyCount.setText(data.gyms.size() + "家场馆");
  }
}
