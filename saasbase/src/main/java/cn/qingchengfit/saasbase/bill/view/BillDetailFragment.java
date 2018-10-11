package cn.qingchengfit.saasbase.bill.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.bill.beans.BillScheduleOrder;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.items.BillKvCommonItem;
import cn.qingchengfit.saasbase.bill.presenter.BillDetailPresenterPresenter;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.views.CardDetailParams;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/10/19.
 */
@Leaf(module = "bill", path = "/detail/") public class BillDetailFragment extends SaasBaseFragment
  implements BillDetailPresenterPresenter.MVPView {

	public TextView tvBillAmount;
	TextView tvBillStatus;
	public TextView tvRemarks;
	View dividerRemark;
	RecyclerView rvCommon;
	RecyclerView rvExtra;
	Toolbar toolbar;
	TextView toolbarTitle;
	String[] arrayOrderType;
	Button btnPrint;
	Button btnRemarks;
	Button btnCard;
	View dividerExtra;
  protected CommonFlexAdapter commonAdapter;
  protected CommonFlexAdapter extraAdapter;

  @Need public String orderNo;
  @Inject public BillDetailPresenterPresenter presenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonAdapter = new CommonFlexAdapter(new ArrayList());
    extraAdapter = new CommonFlexAdapter(new ArrayList());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bill_detail, container, false);
    tvBillAmount = (TextView) view.findViewById(R.id.tv_bill_amount);
    tvBillStatus = (TextView) view.findViewById(R.id.tv_bill_status);
    tvRemarks = (TextView) view.findViewById(R.id.tv_remarks);
    dividerRemark = (View) view.findViewById(R.id.divider_remarks);
    rvCommon = (RecyclerView) view.findViewById(R.id.rv_common);
    rvExtra = (RecyclerView) view.findViewById(R.id.rv_extra);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    btnPrint = (Button) view.findViewById(R.id.btn_print);
    btnRemarks = (Button) view.findViewById(R.id.btn_remarks);
    btnCard = (Button) view.findViewById(R.id.btn_card);
    dividerExtra = (View) view.findViewById(R.id.divider_extra);
    arrayOrderType=getResources().getStringArray(R.array.bill_type);
    view.findViewById(R.id.btn_print).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnPrintClicked();
      }
    });
    view.findViewById(R.id.btn_remarks).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnRemarksClicked();
      }
    });
    view.findViewById(R.id.btn_card).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnCardClicked();
      }
    });

    delegatePresenter(presenter,this);
    presenter.setBillId(orderNo);
    initToolbar(toolbar);
    initView();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("账单详情");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    queryDetail();
  }

  void initView() {
    rvCommon.setLayoutManager(new LinearLayoutManager(getContext()));
    rvExtra.setLayoutManager(new LinearLayoutManager(getContext()));
    rvExtra.setAdapter(extraAdapter);
    rvCommon.setAdapter(commonAdapter);
  }

  protected void queryDetail() {
    presenter.queryBill();
  }

  /**
   * 账单常规信息
   * @param order 账单
   */
  public void onOrderDetail(BusinessBill order) {
    tvBillAmount.setText(getString(R.string.pay_money, StringUtils.getFloatDot2((float) order.price / 100)));//单位从分转换为元
    tvBillStatus.setText(order.status == 1 ? getResources().getString(R.string.bill_detail_success,
        order.getStatus(getContext(), order.status)) : order.getStatus(getContext(), order.status));

    commonAdapter.clear();
    commonAdapter.addItem(new BillKvCommonItem("交易类型", arrayOrderType[order.type%7]));
    commonAdapter.addItem(
      new BillKvCommonItem("收款方式", getString(presenter.getPayType(order.pay_type))));
    if (order.type < 5) commonAdapter.addItem(new BillKvCommonItem("流水号", order.order_no));
    if (order.type == 6) {
      commonAdapter.addItem(
        new BillKvCommonItem("提现账户", order.bank_no + " " + order.bank_username));
    }
    commonAdapter.addItem(
      new BillKvCommonItem("交易时间", DateUtils.formatToMMFromServer(order.pay_time)));
    commonAdapter.addItem(
      new BillKvCommonItem("操作人", order.created_by == null ? "" : order.created_by.getUsername()));
    commonAdapter.addItem(new BillKvCommonItem("平台", order.getOrigin(getContext(), order.origin)));
    commonOrder(order);
    dividerRemark.setVisibility(order.extra == null ? View.GONE : View.VISIBLE);
    tvRemarks.setText("备注: " + order.remarks);
  }

  @Override public void setReMarks(String s) {
    tvRemarks.setText("备注: " + s);
  }

  /**
   * 其他信息
   */
  void commonOrder(BusinessBill order) {

    extraAdapter.clear();
    if (order.type == 1 || order.type == 2) {
      //card
      Card card = order.extra.card;
      if (card != null) {
        btnCard.setVisibility(View.VISIBLE);
        dividerExtra.setVisibility(View.VISIBLE);
        extraAdapter.addItem(
          new BillKvCommonItem("会员卡", card.getName() + "(" + card.getId() + ")"));
        extraAdapter.addItem(new BillKvCommonItem("当前余额", CardBusinessUtils.getCardBlance(card)));
        if (card.getType() == 3) {
          extraAdapter.addItem(new BillKvCommonItem("有效期",
            DateUtils.getDuringFromServer(card.getStart(), card.getEnd())));
        } else if (card.isCheck_valid()) {
          extraAdapter.addItem(new BillKvCommonItem("有效期",
            DateUtils.getDuringFromServer(card.getValid_from(), card.getValid_to())));
        }
        extraAdapter.addItem(new BillKvCommonItem("绑定会员", card.getFirstUserStr(getContext())));
        if (!TextUtils.isEmpty(order.seller.username))
          extraAdapter.addItem(new BillKvCommonItem("销售", order.seller.username));
      }
    } else if (order.type == 3) {

    } else if (order.type == 4) {
      //schedule_order
      BillScheduleOrder scheduleOrder = order.extra.schedule_order;
      if (scheduleOrder != null) {
        extraAdapter.addItem(
          new BillKvCommonItem("课程", scheduleOrder.teacher_name + " " + scheduleOrder.course_name));
        extraAdapter.addItem(new BillKvCommonItem("开始时间",
          DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(scheduleOrder.start))));
        String u = "";
        if (scheduleOrder.users != null && scheduleOrder.users.size() > 0) {
          u = u.concat(scheduleOrder.users.get(0).getUsername());
          if (scheduleOrder.users.size() > 1) u = u.concat("(" + scheduleOrder.users.size() + "）");
        }
        extraAdapter.addItem(new BillKvCommonItem("预约会员", u));
        extraAdapter.addItem(new BillKvCommonItem("金额", scheduleOrder.price + "元"));
      }
    }
  }

  @Override public String getFragmentName() {
    return BillDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

  }

 public void onBtnPrintClicked() {
    //打印
  }

 public void onBtnRemarksClicked() {
    //填写备注
    routeTo("common","/input/",new CommonInputParams()
      .title("填写备注")
      .content(presenter.getBill() == null ?"":presenter.getBill().remarks)
      .hint("填写账单备注")
      .build());
  }

 public void onBtnCardClicked() {
    if (TextUtils.isEmpty(presenter.getCardId())) {
      showAlert("没有会员卡信息");
      return;
    }
    routeTo("card","/detail/",new CardDetailParams().cardid(presenter.getCardId()).build());
    if (getActivity() != null)
      getActivity().finish();
  }
}
