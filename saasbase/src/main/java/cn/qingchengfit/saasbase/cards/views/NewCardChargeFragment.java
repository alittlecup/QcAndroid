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
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/18.
 */

@Leaf(module = "card", path = "/charge/")
public class NewCardChargeFragment extends CardBuyFragment {

  @Need protected Card card;
  @Inject LoginStatus loginStatus;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    cardTpl = card.getCard_tpl();
    View view = super.onCreateView(inflater, container, savedInstanceState);
    toolbarTitle.setText("续卡");
    if (card.is_open_service_term) {
      cardProtocol.setVisibility(View.VISIBLE);
    }else{
      cardProtocol.setVisibility(View.GONE);
    }
    initView();
    return view;
  }

  private void initView(){
    if (card.isCheck_valid()) {
      civStartTime.setContent(setTimeFormat(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_from()))));
      civEndTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getValid_to())));
    }
    tvCardValidateTotal.setVisibility(View.VISIBLE);
    elAutoOpen.setExpanded(card.is_auto_start());
    presenter.setChoseStuIds((ArrayList<String>) card.getUserIds());
    //TODO 支付方式
    //civPayMethod.setContent(getResources().getStringArray(R.array.pay_method)[]);
    //civBindMenbers.setContent(card.getUsers().size() + "  人");
    civBindMenbers.setVisibility(View.GONE);
    civMark.setContentColor(getResources().getColor(R.color.text_warm));
    if (card.isCheck_valid() && card.getType() != Configs.CATEGORY_DATE) {
      tvCardAppend.setText("有效期："
          + DateUtils.getYYYYMMDDfromServer(card.getValid_from())
          + " - "
          + DateUtils.getYYYYMMDDfromServer(card.getValid_to()));
      tvCardExpandDesc.setContent(getResources().getString(R.string.cardtpl_remainder,
          String.valueOf(card.getBalance() < 0 ? 0 : card.getBalance())));
    } else if (card.getType() == Configs.CATEGORY_DATE) {
      tvCardAppend.setText("有效期："
          + DateUtils.getYYYYMMDDfromServer(card.getStart())
          + " - "
          + DateUtils.getYYYYMMDDfromServer(card.getEnd()));
      tvCardExpandDesc.setContent(getResources().getString(R.string.date_cardtpl_remainder,
          String.valueOf(card.getBalance() < 0 ? 0 : card.getBalance())));
    } else {
      tvCardAppend.setText("有效期：无");
      tvCardExpandDesc.setContent(getResources().getString(R.string.cardtpl_remainder,
          String.valueOf(card.getBalance() < 0 ? 0 : card.getBalance())));
    }

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
    int interval;
    if (other){
      return;
    }
    if(cardTpl.getType() == Configs.CATEGORY_DATE){
      tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_time_card_validate,
          String.valueOf(card.getBalance() + Float.parseFloat(option.charge))));
      interval = (int)(Float.parseFloat(option.charge)+ card.getBalance());
    }else{
      tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_card_validate,
          String.valueOf(card.getBalance() + Float.parseFloat(option.charge)),
          option.isLimit_days() ? String.valueOf(
              card.isCheck_valid() ? (DateUtils.interval(card.getValid_from(), card.getValid_to())
                  + option.days) : option.days) : "不限"));
      interval = card.isCheck_valid() ? (DateUtils.interval(card.getValid_from(), card.getValid_to())
          + option.days) : option.days;
    }
    civEndTime.setContent(DateUtils.Date2YYYYMMDD(
        DateUtils.addDay(DateUtils.formatDateFromServer(DateUtils.Date2YYYYMMDD(new Date())),
            interval)));
  }

  //@Override public void onShowDetail(CardTplOption cardOption) {
  //  civEndTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.addDay(
  //      DateUtils.formatDateFromServer(
  //          cardOption.created_at),
  //      (int)(Float.parseFloat(cardOption.charge) + card.getBalance()))));
  //}

  @Override public void onCardProrocol() {
    if (card.card_tpl_service_term != null){
      CardProtocolActivity.startWeb(card.card_tpl_service_term.content_link, getContext(), false);
    }
  }

  @Override public void checkValidate() {
    super.checkValidate();
    if (cardTpl.getType() != Configs.CATEGORY_DATE && card.isExpired()){
      Date start = DateUtils.formatDateFromYYYYMMDD(civStartTime.getContent());
      if (cardOptionCustom.isLimit_days()) {
        if (start.before(new Date())) {
          int interval = cardOptionCustom.days - DateUtils.interval(start, new Date());
          if (interval < 0) {
            interval = 0;
          }
          tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_card_validate,
              String.valueOf(card.getBalance() + Float.parseFloat(cardOptionCustom.charge)),
              String.valueOf(interval)));
        }
      }
    }
  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(cardTpl.getId());
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type, getContext()));
    tvGymName.setText(cardTpl.getShopNames());
    cardview.setBackground(
        DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
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

  @Override public void setPayMoney(float m) {
    tvPayMoney.setText(getString(R.string.pay_money, CmStringUtils.getMoneyStr(m)));
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
