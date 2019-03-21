package cn.qingchengfit.gym.item;

import android.content.Context;
import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymSimpleListItemBinding;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymSimpleListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymSimpleListItemBinding>> {

  private String brandName;

  public Gym getGym() {
    return gym;
  }

  private Gym gym;

  public GymSimpleListItem(String brandName, Gym gym) {
    this.brandName = brandName;
    this.gym = gym;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_simple_list_item;
  }

  @Override public DataBindingViewHolder<GyGymSimpleListItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymSimpleListItemBinding> holder, int position, List payloads) {
    GyGymSimpleListItemBinding dataBinding = holder.getDataBinding();
    dataBinding.tvBrandGymName.setText(brandName + "-" + gym.name);
    Context context = dataBinding.getRoot().getContext();
    Glide.with(context)
        .load(PhotoUtils.getSmall(gym.photo))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(dataBinding.imgGymPhoto, context));
    dataBinding.tvGymAddress.setText(gym.address);
  }
}
