package cn.qingchengfit.saasbase.bill.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.beans.PayRequest;
import cn.qingchengfit.saasbase.events.EventPayRequest;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PayRequestItem extends AbstractFlexibleItem<PayRequestItem.PayRequestVH> {

  PayRequest payRequest;

  public PayRequest getPayRequest() {
    return payRequest;
  }

  public void setPayRequest(PayRequest payRequest) {
    this.payRequest = payRequest;
  }

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
    holder.tvOrderNo.setText(payRequest.task_no);
    holder.tvTitle.setText(payRequest.title);
    holder.tvContent.setText(payRequest.content);
    holder.tvStatus.setText("");
    holder.tvPayMoney.setText("￥"+CmStringUtils.getFloatDot2((float) payRequest.price/100));
    holder.tvStaff.setText("操作人: "+payRequest.created_by == null?"":payRequest.created_by.getUsername());
    holder.tvTime.setText(DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(payRequest.created_at)));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PayRequestVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_order_no) TextView tvOrderNo;
    @BindView(R2.id.tv_status) TextView tvStatus;
    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.tv_content) TextView tvContent;
    @BindView(R2.id.tv_pay_money) TextView tvPayMoney;
    //@BindView(R2.id.tv_charge_money) TextView tvChargeMoney;
    //@BindView(R2.id.tv_valid_day) TextView tvValidDay;
    @BindView(R2.id.tv_time) TextView tvTime;
    @BindView(R2.id.tv_staff) TextView tvStaff;
    @BindView(R2.id.btn_cancel) Button btnCancel;
    @BindView(R2.id.btn_pay) Button btnPay;

    public PayRequestVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }

    @OnClick(R2.id.btn_cancel)
    public void cancleRequest(){
      IFlexible iFlexible = mAdapter.getItem(getAdapterPosition());
      if (iFlexible instanceof  PayRequestItem){
        RxBus.getBus().post(new EventPayRequest(1,((PayRequestItem) iFlexible).getPayRequest()));
      }
    }

    @OnClick(R2.id.btn_pay)
    public void pay(){
      IFlexible iFlexible = mAdapter.getItem(getAdapterPosition());
      if (iFlexible instanceof  PayRequestItem){
        RxBus.getBus().post(new EventPayRequest(0,((PayRequestItem) iFlexible).getPayRequest()));
      }
    }



  }
}