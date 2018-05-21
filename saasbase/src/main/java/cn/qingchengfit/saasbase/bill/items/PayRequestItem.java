package cn.qingchengfit.saasbase.bill.items;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
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

  @Override public PayRequestVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PayRequestVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PayRequestVH holder, int position,
      List payloads) {
    holder.tvOrderNo.setText(payRequest.task_no);
    holder.tvTitle.setText(payRequest.title);
    holder.tvContent.setText(payRequest.content);
    holder.tvPayMoney.setText("￥"+CmStringUtils.getFloatDot2((float) payRequest.price/100));
    holder.tvStaff.setText("操作人: "+payRequest.created_by == null?"":payRequest.created_by.getUsername());
    holder.tvTime.setText(DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(payRequest.created_at)));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PayRequestVH extends FlexibleViewHolder {
	TextView tvOrderNo;
	TextView tvStatus;
	TextView tvTitle;
	TextView tvContent;
	TextView tvPayMoney;
    //@BindView(R2.id.tv_charge_money) TextView tvChargeMoney;
    //@BindView(R2.id.tv_valid_day) TextView tvValidDay;
	TextView tvTime;
	TextView tvStaff;
	Button btnCancel;
	Button btnPay;

    public PayRequestVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvOrderNo = (TextView) view.findViewById(R.id.tv_order_no);
      tvStatus = (TextView) view.findViewById(R.id.tv_status);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
      tvPayMoney = (TextView) view.findViewById(R.id.tv_pay_money);
      tvTime = (TextView) view.findViewById(R.id.tv_time);
      tvStaff = (TextView) view.findViewById(R.id.tv_staff);
      btnCancel = (Button) view.findViewById(R.id.btn_cancel);
      btnPay = (Button) view.findViewById(R.id.btn_pay);
      view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          cancleRequest();
        }
      });
      view.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          pay();
        }
      });
    }


    public void cancleRequest(){
      IFlexible iFlexible = mAdapter.getItem(getAdapterPosition());
      if (iFlexible instanceof  PayRequestItem){
        RxBus.getBus().post(new EventPayRequest(1,((PayRequestItem) iFlexible).getPayRequest()));
      }
    }


    public void pay(){
      IFlexible iFlexible = mAdapter.getItem(getAdapterPosition());
      if (iFlexible instanceof  PayRequestItem){
        RxBus.getBus().post(new EventPayRequest(0,((PayRequestItem) iFlexible).getPayRequest()));
      }
    }



  }
}