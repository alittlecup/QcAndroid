package cn.qingchengfit.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import cn.qingchengfit.utils.LogUtil;
import java.util.ArrayList;
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
 * Created by Paper on 2017/10/29.
 */

public class RongPrinter {
  private static final String ACTION_PRINT = "com.rongcapital.print";
  private List<Pair<String, String>> first, secoud;
  private String title = "账单";
  private RongPrinter(Builder builder) {
    first = builder.first;
    secoud = builder.secoud;
    title = builder.title;
  }



  public Intent print(Context context) {
    String msg = getBillDetail(first, secoud);
    LogUtil.d(msg);
    try {
      Intent intent = new Intent();
      Bundle args = new Bundle();
      args.putString("msg", msg);   // 打印内容规范 请参考《全民付收银台线下插件商户销售单据打印规范》
      args.putString("packageName", context.getPackageName());  // 应用包名
      intent.putExtra("data", args);
      intent.setAction(ACTION_PRINT);  // 操作类型
      return intent;
    } catch (Exception e) {
      Log.e("TAG", e.getMessage());
      return new Intent();
    }
  }

  public String getBillDetail(List<Pair<String, String>> firstPard,
    List<Pair<String, String>> second) {
    StringBuilder sb = new StringBuilder("!hz l\n"
      + "!asc l\n"
      + "!gray 6\n"
      + "!yspace 4\n"
      + "*text c "+title+" \n"
      + "*line\n"
      + "!hz s\n"
      + "!asc s\n"
      + "!gray 2\n");
    if (firstPard != null && firstPard.size() > 0) sb.append(getPrintArray(firstPard));
    if (second != null && second.size() > 0) sb.append(getPrintArray(second));
    return sb.append("*line\n*line\n*line\n*line\n*line\n").toString();
  }

  private String getPrintFormatMsg(String orderId) {
    String text = "!hz l\n"
      + "!asc l\n"
      + "!gray 6\n"
      + "!yspace 4\n"
      + "*text c 账单 \n"
      + "*line\n"
      + "!hz s\n"
      + "!asc s\n"
      + "!gray 2\n"
      + "*text c 消费名称："
      + "title"
      + "\n"
      + "*text c"
      + " 订单号："
      + orderId
      + "\n"
      + "*text c"
      + " 支付方式："
      + "1111"
      + "\n"
      + "*text c"
      + " 支付时间："
      + "11111"
      + "\n"
      + "*text c"
      + " 交易类型：消费"
      + "\n"
      + "*text c"
      + " 交易金额："
      + "1111.11"
      + "元"
      + "\n"
      + "*text c"
      + " 本人确认以上交易"
      + "\n"
      + "*line\n"
      + "*text c"
      + " 持卡人签名\n"
      + "*text c"
      + "\n"
      + "*line\n";
    return text;
  }

  private String getPrintArray(List<Pair<String, String>> datas) {
    StringBuffer sb = new StringBuffer();
    for (Pair<String, String> data : datas) {
      sb.append("*text l ").append(data.first).append("\t").append(data.second).append("\n");
    }
    return sb.toString();
  }

  public static final class Builder {
    private List<Pair<String, String>> first = new ArrayList<>();
    private List<Pair<String, String>> secoud = new ArrayList<>();
    private String title;
    public Builder() {
    }

    public Builder first(String key, String value) {
      first.add(new Pair<String, String>(key, value));
      return this;
    }
    public Builder title(String value) {
      title = value;
      return this;
    }

    public Builder secoud(String key, String value) {
      first.add(new Pair<String, String>(key, value));
      return this;
    }

    public RongPrinter build() {
      return new RongPrinter(this);
    }
  }
}
