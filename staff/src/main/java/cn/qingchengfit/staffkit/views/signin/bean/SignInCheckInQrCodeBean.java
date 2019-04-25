package cn.qingchengfit.staffkit.views.signin.bean;

import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.saasbase.cards.bean.Card;

public class SignInCheckInQrCodeBean {
  public Data data;

  public static class Data {
    public Personage user;
    public Card card;
  }
}
