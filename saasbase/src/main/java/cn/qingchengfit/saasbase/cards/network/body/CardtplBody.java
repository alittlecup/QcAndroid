package cn.qingchengfit.saasbase.cards.network.body;

import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.List;

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
 * Created by Paper on 2017/8/23.
 */

public class CardtplBody {
  public String name;
  public String id;
  public Integer type;
  public String description;
  public boolean is_limit;
  //    public int count;
  public Integer pre_times;//可提前上课
  public Integer day_times;//
  public Integer week_times;//
  public Integer month_times;//
  public Integer buy_limit;
  public String shops;//支持场馆 ,隔开
  public List<CardTplOption> options;

  /**
   * 在pos机内检查数据
   * @return
   */
  public int checkInPos() {
    if (CmStringUtils.isEmpty(name)) return R.string.e_card_name_empty;
    if (!CmStringUtils.isEmpty(id)) {
      id = null;
    }
    if (options != null && options.size() > 0){
      for (CardTplOption option : options) {
        option.id = null;
      }
    }
    return 0;
  }

  public CardtplBody() {
  }

  private CardtplBody(Builder builder) {
    name = builder.name;
    id = builder.id;
    type = builder.type;
    description = builder.description;
    is_limit = builder.is_limit;
    pre_times = builder.pre_times;
    day_times = builder.day_times;
    week_times = builder.week_times;
    month_times = builder.month_times;
    buy_limit = builder.buy_limit;
    shops = builder.shops;
    options = builder.options;
  }

  public CardtplBody clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {

    }
    CardtplBody b = new CardtplBody();
    b.name = this.name;
    b.id = this.id;
    b.type = this.type;
    b.description = this.description;
    b.is_limit = this.is_limit;
    b.pre_times = this.pre_times;
    b.day_times = this.day_times;
    b.week_times = this.week_times;
    b.month_times = this.month_times;
    b.shops = this.shops;
    b.buy_limit = this.buy_limit;
    return b;
  }

  public static final class Builder {
    private String name;
    private String id;
    private int type;
    private String description;
    private boolean is_limit;
    private Integer pre_times;
    private Integer day_times;
    private Integer week_times;
    private Integer month_times;
    private Integer buy_limit;
    private String shops;
    private List<CardTplOption> options;

    public Builder() {
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder type(int val) {
      type = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder is_limit(boolean val) {
      is_limit = val;
      return this;
    }

    public Builder pre_times(Integer val) {
      pre_times = val;
      return this;
    }

    public Builder day_times(Integer val) {
      day_times = val;
      return this;
    }

    public Builder week_times(Integer val) {
      week_times = val;
      return this;
    }

    public Builder month_times(Integer val) {
      month_times = val;
      return this;
    }

    public Builder buy_limit(Integer val) {
      buy_limit = val;
      return this;
    }

    public Builder shops(String val) {
      shops = val;
      return this;
    }

    public Builder options(List<CardTplOption> val) {
      options = val;
      return this;
    }

    public CardtplBody build() {
      return new CardtplBody(this);
    }
  }
}
