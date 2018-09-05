package cn.qingchengfit.saascommon.widget;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberInputFilter implements InputFilter {
  private String numbers = "1234567890.";

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
      int dend) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = start; i < end; i++) {
      String s = String.valueOf(source.charAt(i));
      if (!numbers.contains(s)) {
        continue;
      }
      stringBuilder.append(s);
    }
    if (stringBuilder.length() == 0) return "";
    int i = dest.toString().indexOf(".");
    if (i == -1) {
      int i1 = stringBuilder.indexOf(".");
      if (i1 == -1) {
        return stringBuilder;
      } else {
        if (stringBuilder.length() - i1 > 3) {
          return stringBuilder.subSequence(0, i1 + 2);
        } else {
          return stringBuilder;
        }
      }
    } else {
      int dis = dest.length() - i;
      String replace = stringBuilder.toString().replace(".", "");
      return replace.length() > 3 - dis ? replace.substring(0, 3 - dis) : replace;
    }
  }
}
