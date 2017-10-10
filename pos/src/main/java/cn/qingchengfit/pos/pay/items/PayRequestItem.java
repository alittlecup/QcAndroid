package cn.qingchengfit.pos.pay.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.pay.beans.PayRequest;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PayRequestItem extends AbstractFlexibleItem<PayRequestItem.PayRequestVH> {

  PayRequest payRequest;

  public PayRequestItem(PayRequest payRequest) {
    this.payRequest = payRequest;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_pay_request;
  }

  @Override public PayRequestVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new PayRequestVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PayRequestVH holder, int position,
      List payloads) {

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PayRequestVH extends FlexibleViewHolder {
    @BindView(R.id.tv_order_no) TextView tvOrderNo;
    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_type_and_cardtpl) TextView tvTypeAndCardtpl;
    @BindView(R.id.tv_pay_money) TextView tvPayMoney;
    @BindView(R.id.tv_charge_money) TextView tvChargeMoney;
    @BindView(R.id.tv_valid_day) TextView tvValidDay;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.tv_staff) TextView tvStaff;
    @BindView(R.id.btn_cancel) Button btnCancel;
    @BindView(R.id.btn_pay) Button btnPay;
    public PayRequestVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}