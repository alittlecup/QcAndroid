package cn.qingchengfit.notisetting.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.notisetting.bean.ShopOrder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SmsChargeItem extends AbstractFlexibleItem<SmsChargeItem.ShopOrderVH> {
  public Context mContext;
  ShopOrder shopOrder;

  public SmsChargeItem(ShopOrder shopOrder, Context mContext) {
    this.shopOrder = shopOrder;
    this.mContext = mContext;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_charge_sms_history;
  }

  @Override public ShopOrderVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ShopOrderVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ShopOrderVH holder, int position,
      List payloads) {
    holder.payTime.setText(DateUtils.getYYYYMMDDfromServer(shopOrder.created_at));
    holder.gymRealMoney.setText(StringUtils.getNumString(shopOrder.price) + "元");
    try {
      holder.gymValidTime.setText(shopOrder.extra.get("remarks").getAsString());
    } catch (Exception e) {
      holder.gymValidTime.setText("");
    }
    if (shopOrder.created_by != null && shopOrder.created_by.getUsername() != null) {
      holder.operaUser.setText(
          mContext.getString(R.string.control_person, shopOrder.created_by.getUsername()));
    } else {
      holder.operaUser.setText("青橙客服");
    }
    holder.payStatus.setText(mContext.getResources().getStringArray(R.array.renewal_status)[1]);
    holder.payStatus.setTextColor(CompatUtils.getColor(mContext, R.color.green));
    holder.payOnline.setText(BusinessUtils.getOrderPayChannel(shopOrder.channel));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ShopOrderVH extends FlexibleViewHolder {
    @BindView(R.id.pay_time) TextView payTime;
    @BindView(R.id.pay_status) TextView payStatus;
    @BindView(R.id.gym_valid_time) TextView gymValidTime;
    @BindView(R.id.gym_real_money) TextView gymRealMoney;
    @BindView(R.id.pay_online) TextView payOnline;
    @BindView(R.id.opera_user) TextView operaUser;

    public ShopOrderVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}