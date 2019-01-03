package cn.qingchengfit.saasbase.cards.network.body;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/31 2016.
 */
public class AddDayOffBody {
  public String model;
  public String id;
  public String card_id;
  public String start;
  public String end;
  public String message;
  public String price;
  public String remarks;
  public Integer charge_type;

  public AddDayOffBody(String model, String id, String card_id) {
    this.model = model;
    this.id = id;
    this.card_id = card_id;
  }
}
