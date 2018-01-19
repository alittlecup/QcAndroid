package cn.qingchengfit.pos.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/13.
 */

public class ItemCashier extends AbstractFlexibleItem<ItemCashier.ItemCashierVH> {

  Cashier cashier;

  public ItemCashier(Cashier cashier) {
    this.cashier = cashier;
  }

  @Override public ItemCashierVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ItemCashierVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemCashierVH holder, int position,
      List payloads) {
    Glide.with(holder.itemView.getContext())
        .load(PhotoUtils.getSmall(cashier.user.getAvatar()))
        .asBitmap()
        .into(new CircleImgWrapper(holder.imgCashierHead, holder.itemView.getContext()));
    holder.tvCashierName.setText(cashier.user.getUsername());
    holder.tvCashierPhone.setText(cashier.user.getPhone());
    holder.imgCashierGender.setImageResource(cashier.user.gender == 0 ? R.drawable.ic_gender_signal_male
        : R.drawable.ic_gender_signal_female);
  }

  public Cashier getData(){
    return cashier;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cashier;
  }

  class ItemCashierVH extends FlexibleViewHolder {

    @BindView(R.id.img_cashier_head) ImageView imgCashierHead;
    @BindView(R.id.tv_cashier_name) TextView tvCashierName;
    @BindView(R.id.tv_cashier_phone) TextView tvCashierPhone;
    @BindView(R.id.img_cashier_gender) ImageView imgCashierGender;

    public ItemCashierVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
