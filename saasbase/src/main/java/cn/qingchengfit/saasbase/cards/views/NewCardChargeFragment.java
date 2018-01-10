package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.StringRes;
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
import cn.qingchengfit.utils.DialogUtils;
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
    civRealCardNum.setVisibility(View.GONE);
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
          String.valueOf((card.getBalance() > 0 ? card.getBalance() : 0) + Float.parseFloat(option.charge))));
      interval = (int)(Float.parseFloat(option.charge)+ (card.getBalance() > 0 ? card.getBalance() : 0));
    }else{
      @StringRes int stringId = R.string.text_charge_card_validate;
      if (card.getType() == Configs.CATEGORY_TIMES){
        stringId = R.string.text_charge_card_validate_times;
      }
      tvCardValidateTotal.setText(getResources().getString(stringId,
          String.valueOf(card.getBalance() + Float.parseFloat(option.charge)),
          option.isLimit_days() ? String.valueOf(
              card.isCheck_valid() ? (DateUtils.interval(card.getValid_from(), card.getValid_to())
                  + option.days + 1) : option.days) : "不限"));
      interval =
          card.isCheck_valid() ? (!card.isExpired() ? (DateUtils.interval(card.getValid_from(),
              card.getValid_to()) + option.days + 1) : option.days) : option.days;
    }
    civStartTime.setContent(setTimeFormat(DateUtils.Date2YYYYMMDD(new Date())));
    civEndTime.setContent(DateUtils.Date2YYYYMMDD(
        DateUtils.addDay(DateUtils.formatDateFromServer(DateUtils.Date2YYYYMMDD(new Date())),
            interval - 1)));
  }

  @Override public void onCardProrocol() {
    if (card.card_tpl_service_term != null){
      CardProtocolActivity.startWeb(card.card_tpl_service_term.content_link, getContext(), false);
    }
  }

  @Override public void checkValidate(Date date) {
    if (cardTpl.getType() != Configs.CATEGORY_DATE){
      if (cardOptionCustom.isLimit_days()) {
        if (date.before(new Date())) {
          int interval = cardOptionCustom.days;
          if (interval < 0) {
            interval = 0;
          }
          tvCardValidateTotal.setText(getResources().getString(R.string.text_charge_card_validate,
              String.valueOf(card.getBalance() + Float.parseFloat(cardOptionCustom.charge)),
              String.valueOf(interval)));
          civEndTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.addDay(date,
              (card.isExpired() ? cardOptionCustom.getDays() - 1
                  : cardOptionCustom.getDays() + card.getTrial_days() - 1))));
        }
      }
    }else if (cardTpl.getType() == Configs.CATEGORY_DATE){
      civEndTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.addDay(date,
          (int)(Float.parseFloat(cardOptionCustom.getCharge()) + (card.getBalance() > 0 ? card.getBalance() : 0) - 1))));
    }
  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(card.getId());
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type, getContext()));
    tvGymName.setText(cardTpl.getShopNames());
    cardview.setBackground(
        DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
  }


  @Override public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("会员卡备注")
            .content(presenter.getRemarks())
            .hint(presenter.getRemarks())
            .type(TYPE_CARD_BUY_REMARK)
            .build());
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  @Override public void onConfirmPay() {
    if(cardOptionCustom == null) {
      DialogUtils.showAlert(getContext(), "请选择正确的会员卡规格");
      return;
    }
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
    civMark.setContent(remark ? "已填写" : "未填写");
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {

  }

}
