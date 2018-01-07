package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.bean.PayMethod;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2018/1/5.
 */

public class ItemPayMethod extends AbstractFlexibleItem<ItemPayMethod.PayMethodVH> {

  private PayMethod payMethod;

  public ItemPayMethod(PayMethod payMethod) {
    this.payMethod = payMethod;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public PayMethod getPayMethod() {
    return payMethod;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_select_pay_method;
  }

  @Override public PayMethodVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PayMethodVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PayMethodVH holder, int position,
      List payloads) {
    holder.imgPayMethod.setImageResource(payMethod.icon);
    holder.textPayName.setText(payMethod.name);
    if (adapter.isSelected(position)){
      holder.imgHook.setVisibility(View.VISIBLE);
    }else{
      holder.imgHook.setVisibility(View.GONE);
    }
  }

  class PayMethodVH extends FlexibleViewHolder {

    @BindView(R2.id.img_pay_method) ImageView imgPayMethod;
    @BindView(R2.id.text_pay_name) TextView textPayName;
    @BindView(R2.id.img_hook) ImageView imgHook;

    public PayMethodVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
