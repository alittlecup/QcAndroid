package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qingchengfit.saasbase.R;

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
    if (adapter.isSelected(position)) {
      holder.imgHook.setVisibility(View.VISIBLE);
    } else {
      holder.imgHook.setVisibility(View.GONE);
    }
    holder.imgPayMethod.setAlpha(payMethod.isPro ? 1 : 0.4f);
    holder.textPayName.setAlpha(payMethod.isPro ? 1 : 0.4f);
    holder.imgPro.setVisibility(payMethod.isPro ? View.GONE : View.VISIBLE);
    holder.itemView.setClickable(payMethod.isPro);
    if(payMethod.payType==12){//支付宝付款的话
      holder.tvAd.setVisibility(View.VISIBLE);
    }else{
      holder.tvAd.setVisibility(View.GONE);
    }
  }

  class PayMethodVH extends FlexibleViewHolder {

    ImageView imgPayMethod;
    TextView textPayName;
    TextView tvAd;
    ImageView imgHook;
    ImageView imgPro;

    public PayMethodVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgPayMethod = (ImageView) view.findViewById(R.id.img_pay_method);
      textPayName = (TextView) view.findViewById(R.id.text_pay_name);
      imgHook = (ImageView) view.findViewById(R.id.img_hook);
      imgPro = (ImageView) view.findViewById(R.id.img_pro);
      tvAd=view.findViewById(R.id.tv_ad);
    }
  }
}
