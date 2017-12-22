package cn.qingchengfit.pos.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.RongPay;
import cn.qingchengfit.saasbase.bill.view.BillDetailParams;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
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
 * Created by Paper on 2017/10/12.
 */

public class PosCardBuyFragment extends CardBuyFragment {
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  public static PosCardBuyFragment newInstance(CardTpl cardTpl) {
    Bundle args = new Bundle();
    args.putParcelable("tpl", cardTpl);
    PosCardBuyFragment fragment = new PosCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void  onBusinessOrder(Object payBusinessResponse) {
    if (((PayBusinessResponseWrap)payBusinessResponse).order.order_amount > 0) {
      Intent toBuy = new RongPay.Builder().amount(((PayBusinessResponseWrap)payBusinessResponse).order.order_amount)
        .title(((PayBusinessResponseWrap)payBusinessResponse).order.order_title)
        .merOrderId(((PayBusinessResponseWrap)payBusinessResponse).order.order_no)
        .customerNo(gymWrapper.getCustumNo())
        .operator(loginStatus.staff_id())
        .build()
        .pay(getActivity());
      if (getActivity() != null) {
        getActivity().startActivityForResult(toBuy, 100);
      }
    }else {
      routeTo("bill", "/pay/done/", new BillDetailParams().orderNo(
          ((PayBusinessResponseWrap) payBusinessResponse).order.order_no).build());
    }
  }

}
