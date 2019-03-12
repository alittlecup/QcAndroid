package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymBrandItemBinding;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymBrandItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymBrandItemBinding>> {
  public Shop getData() {
    return data;
  }

  Shop data;

  public GymBrandItem(Shop data) {
    this.data = data;
  }

  public boolean isEditAble() {
    return editAble;
  }

  public void setEditAble(boolean editAble) {
    this.editAble = editAble;
  }

  private boolean editAble = false;

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_brand_item;
  }

  @Override public DataBindingViewHolder<GyGymBrandItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymBrandItemBinding> holder, int position, List payloads) {
    GyGymBrandItemBinding dataBinding = holder.getDataBinding();
    dataBinding.setBrandShop(data);

    Glide.with(dataBinding.getRoot().getContext())
        .load(data.photo)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(dataBinding.imgGymPhoto, dataBinding.getRoot().getContext()));
    dataBinding.imgArrowRight.setVisibility(editAble ? View.GONE : View.VISIBLE);
    dataBinding.tvGymEdit.setVisibility(editAble ? View.VISIBLE : View.GONE);
  }
}
