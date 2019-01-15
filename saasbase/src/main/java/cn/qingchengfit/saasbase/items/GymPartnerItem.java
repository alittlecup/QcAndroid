package cn.qingchengfit.saasbase.items;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemGymPartnerItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymPartnerItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemGymPartnerItemBinding>> {

  @IntDef() public @interface GymPartnerType {
    int PARTNER_ALI = 0;
    int PARTNER_MEITUAN = 1;
    int PARTNER_KOUBEI = 2;
    int PARTNER_TAOBAO = 3;
  }

  private int type;
  private boolean status;

  public GymPartnerItem(@GymPartnerType int type, boolean status) {
    this.type = type;
    this.status = status;
  }

  public int getType() {
    return type;
  }

  public boolean getStatus() {
    return status;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_gym_partner_item;
  }

  @Override public DataBindingViewHolder<ItemGymPartnerItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemGymPartnerItemBinding> holder, int position, List payloads) {
    ItemGymPartnerItemBinding dataBinding = holder.getDataBinding();
    int drawableResourse = -1;
    switch (type) {
      case 0:
        drawableResourse = status ? R.drawable.ic_gym_al : R.drawable.ic_gym_ali_line;
        dataBinding.tvPartnerText.setText("支付宝");
        break;
      case 1:
        drawableResourse = status ? R.drawable.ic_gym_mt : R.drawable.ic_gym_mt_line;
        dataBinding.tvPartnerText.setText("美团点评");

        break;
      case 2:
        drawableResourse = status ? R.drawable.ic_gym_kb : R.drawable.ic_gym_kb_line;
        dataBinding.tvPartnerText.setText("口碑");
        break;
      case 3:
        drawableResourse = status ? R.drawable.ic_gym_tb : R.drawable.ic_gym_tb_line;
        dataBinding.tvPartnerText.setText("淘宝");

        break;
    }
    dataBinding.imgGymPartner.setImageResource(drawableResourse);
    dataBinding.tvPartnerStatus.setText(status ? "已认证" : "未认证");
    dataBinding.tvPartnerStatus.setTextColor(dataBinding.getRoot()
        .getResources()
        .getColor(status ? R.color.primary : R.color.white));
    Drawable drawable = DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(),
        R.drawable.vd_arrow_right_grey_small, status ? R.color.primary : R.color.white);
    dataBinding.tvPartnerStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
  }
}
