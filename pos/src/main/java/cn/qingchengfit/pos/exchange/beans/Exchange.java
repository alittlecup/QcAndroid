package cn.qingchengfit.pos.exchange.beans;

import cn.qingchengfit.utils.CmStringUtils;

/**
 * Created by fb on 2017/10/31.
 */

public class Exchange {

  public String start;
  public String end;
  public int bills_numbers;
  public long bills_price;

  public String getBillPriceYuan(){
    return CmStringUtils.getMoneyStr(bills_price/100f);
  }

}
