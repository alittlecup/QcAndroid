package cn.qingchengfit.pos.cash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.RongPay;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
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
@Leaf(module = "desk", path = "/home/") public class CashierDeskFragment extends BaseFragment
  implements CashierDeskPresenterPresenter.MVPView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.btn_7) TextView btn7;
  @BindView(R.id.btn_8) TextView btn8;
  @BindView(R.id.btn_9) TextView btn9;
  @BindView(R.id.btn_back) ImageView btnBack;
  @BindView(R.id.btn_4) TextView btn4;
  @BindView(R.id.btn_5) TextView btn5;
  @BindView(R.id.btn_6) TextView btn6;
  @BindView(R.id.btn_1) TextView btn1;
  @BindView(R.id.btn_2) TextView btn2;
  @BindView(R.id.btn_3) TextView btn3;
  @BindView(R.id.btn_0) TextView btn0;
  @BindView(R.id.btn_clear) TextView btnClear;
  @BindView(R.id.btn_add) ImageView btnAdd;
  @BindView(R.id.btn_pay) TextView btnPay;
  @BindView(R.id.tv_total) TextView tvTotal;
  @BindView(R.id.tv_cmd_line) TextView tvCmdLine;

  @Inject CashierDeskPresenterPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cashier_desk, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("收银台");
  }

  @Override public String getFragmentName() {
    return CashierDeskFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick({
    R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_1, R.id.btn_2,
    R.id.btn_3, R.id.btn_0
  }) public void onViewClicked(View view) {
    int addNum = 0;
    switch (view.getId()) {
      case R.id.btn_7:
        addNum = 7;
        break;
      case R.id.btn_8:
        addNum = 8;
        break;
      case R.id.btn_9:
        addNum = 9;
        break;
      case R.id.btn_4:
        addNum = 4;
        break;
      case R.id.btn_5:
        addNum = 5;
        break;
      case R.id.btn_6:
        addNum = 6;
        break;
      case R.id.btn_1:
        addNum = 1;
        break;
      case R.id.btn_2:
        addNum = 2;
        break;
      case R.id.btn_3:
        addNum = 3;
        break;
      case R.id.btn_0:
        break;
    }
    presenter.pressNum(addNum);
  }

  @OnClick(R.id.btn_back) public void onBtnBackClicked() {
    presenter.pressBackSpace();
  }

  @OnClick(R.id.btn_clear) public void onBtnClearClicked() {
    presenter.pressClear();
  }

  @OnClick(R.id.btn_add) public void onBtnAddClicked() {
    presenter.pressAdd();
  }

  @OnClick(R.id.btn_pay) public void onBtnPayClicked() {
    presenter.pay();
  }

  @Override public void onBusinessOrder(PayBusinessResponse payBusinessResponse) {
    Intent toBuy = new RongPay.Builder()
      .amount(payBusinessResponse.order_amount)
      .title(payBusinessResponse.order_title)
      .merOrderId(payBusinessResponse.order_no)
      .customerNo(gymWrapper.getCustumNo())
        .operator(getResources().getString(R.string.pay_to_operator, loginStatus.staff_name(),
            loginStatus.getLoginUser().user_id))
      .build().pay(getContext());
    if (getActivity() != null)
      getActivity().startActivityForResult(toBuy,100);
  }


  //@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
  //  super.onActivityResult(requestCode, resultCode, data);
  //  if (resultCode == Activity.RESULT_OK){
  //    if (requestCode == 100){
  //
  //      if (data.getBundleExtra("data") == null) {
  //      } else {
  //        Bundle args = data.getBundleExtra("data");
  //        Long amount = args.getLong("amount", -1);
  //        String merorderId = args.getString("merOrderId");
  //        String payStatus = args.getString("payStatus");
  //        String title = args.getString("title");
  //        String operator = args.getString("operator");
  //        String packageName = args.getString("packageName");
  //        int payType = args.getInt("payType", 0);
  //        String tradeFlowId = args.getString("tradeFlowId");                // 交易流水号
  //        String dealTime = args.getString("dealTime");               // 交易时间
  //        LogUtil.d("PosPay","amount:" + amount + "|merorderId:" + merorderId + "|title:" + title + "|operator:" +
  //          operator + "|packageName:" + packageName + "|payType:" + payType
  //          + "|tradeFlowId:" + tradeFlowId + "|dealTime:" + dealTime + "|payStatus:" + payStatus);
  //        onPayDone(merorderId);
  //      }
  //    }
  //  }else {
  //    onShowError("支付取消");
  //  }
  //}
  //
  //protected void onPayDone(String orderNo){
  //  routeTo("/pay/done/",new BillDetailParams().orderNo(orderNo).build());
  //}



  @Override public void showTotal(float f) {
    tvTotal.setText(getString(R.string.pay_money, StringUtils.getFloatDot2(f)));
  }

  @Override public void showCurrent(String s) {
    SpannableString str = new SpannableString(s);
    int start = 0;
    if (s.contains("+")){
      start = s.lastIndexOf("+")+1;
    }
    str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.colorPrimary)),start,s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    tvCmdLine.setText(str);
  }
}
