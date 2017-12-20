package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.AppUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.gson.JsonObject;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/18.
 */

@Leaf(module = "card", path = "/charge/")
public class NewCardChargeFragment extends CardBuyFragment {

  @Need Card card;
  @Inject LoginStatus loginStatus;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    cardTpl = card.getCard_tpl();
    View view = super.onCreateView(inflater, container, savedInstanceState);
    initView();
    return view;
  }

  private void initView(){
    tvCardValidateTotal.setVisibility(View.VISIBLE);
    elAutoOpen.setExpanded(card.is_auto_start());
    //TODO 支付方式
    //civPayMethod.setContent(getResources().getStringArray(R.array.pay_method)[]);
    //civBindMenbers.setContent(card.getUsers().size() + "  人");
    civSaler.setContent("本人");
    presenter.setSaler(loginStatus.staff_id());
    civBindMenbers.setVisibility(View.GONE);
    civMark.setContentColor(getResources().getColor(R.color.text_warm));
    if (TextUtils.isEmpty(card.getRemarks())){
      civMark.setContent("选填");
    }else{
      civMark.setContent("查看");
    }
    //TODO 业绩归属
    //civSaler.setContent(card);
  }

  @Override public void showInputMoney(boolean other, CardTplOption option, boolean validDay) {
    super.showInputMoney(other, option, validDay);
    if(cardTpl.getType() == Configs.CATEGORY_DATE){
      tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_time_card_validate,
          card.getBalance() + Float.parseFloat(option.charge)));
    }else{
      tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_card_validate,
          card.getBalance() + Float.parseFloat(option.charge),
          option.isLimit_days() ? String.valueOf(card.getTrial_days() + option.days) : "不限"));
    }
  }

  @Override public void onShowDetail() {
    super.onShowDetail();
  }

  @Override public void onClickCardId() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("修改实体卡号")
            .hint(presenter.getmCard().getCard_no())
            .content(presenter.getmCard().getCard_no())
            .build());
  }

  @Override public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("会员卡备注")
            .content(presenter.getRemarks())
            .hint(presenter.getRemarks())
            .build());
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  @Override public void onConfirmPay() {
    presenter.chargeCard();
  }

  @Override public void setCardInfo() {
    presenter.setmCard(card);
    presenter.setmCardTplId(card.getCard_tpl_id());
    presenter.setCardCate(card.getCard_tpl().getType());
  }

  @Override public void setPayMoney(String m) {
    tvPayMoney.setText(getString(R.string.pay_money, m));
  }

  @Override public void bindSaler(String saler) {
    civSaler.setContent(saler);
  }

  @Override public void remark(boolean remark) {
    civMark.setContent(remark ? "已设置" : "未设置");
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {

  }

}
