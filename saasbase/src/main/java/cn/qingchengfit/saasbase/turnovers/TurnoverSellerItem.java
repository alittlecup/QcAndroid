package cn.qingchengfit.saasbase.turnovers;

import android.view.View;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.databinding.ItemChooseSalerBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoverSellerItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemChooseSalerBinding>> {
  public ICommonUser getData() {
    return data;
  }

  private ICommonUser data;

  public TurnoverSellerItem(ICommonUser data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_choose_saler;
  }

  @Override public DataBindingViewHolder<ItemChooseSalerBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemChooseSalerBinding> holder, int position, List payloads) {
    ItemChooseSalerBinding dataBinding = holder.getDataBinding();
    int da = data.getGender()==0 ? R.drawable.default_manage_male : R.drawable.default_manager_female;
    if(data.getId()==null){
      Glide.with(dataBinding.salerHeaderImg.getContext())
          .load(R.drawable.ic_all_normal)
          .asBitmap()
          .into(new CircleImgWrapper(dataBinding.salerHeaderImg, holder.itemView.getContext()));
    }else{
      PhotoUtils.smallCircle(dataBinding.salerHeaderImg, data.getAvatar(), da, da);
    }
    dataBinding.salerNameTv.setText(data.getTitle());

    if (adapter.isSelected(position)) {
      dataBinding.salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
      dataBinding.chooseImg.setVisibility(View.VISIBLE);
    } else {
      dataBinding.salerHeaderImg.setBackgroundResource(R.drawable.circle_outside);
      dataBinding.chooseImg.setVisibility(View.GONE);
    }
  }
}
