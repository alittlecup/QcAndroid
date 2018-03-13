package cn.qingchengfit.shop;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);

    checkPrice("1000000");

    checkPrice("0");

    checkPrice("0.4");

    checkPrice("0.456");

    checkPrice("0.45");

    checkPrice("0..");

    checkPrice(".");
    try {
      checkPrice("");
      System.out.println("adfadf");return;
    } finally {
      System.out.println("finially");
    }
  }

  private void checkPrice(String price) {
    String format = price;
    boolean change = false;
    if (price == null || price.isEmpty()) return;
    try {
      Double doubleprice = Double.valueOf(price);
      if ((doubleprice > 999999)) {
        System.out.println(price + "-->商品价格最大值为999999");
        change = true;
      } else {
        int i = price.lastIndexOf(".");
        if (i != -1 && price.length() - i > 3) {
          System.out.println(price + "最多支持两位小数");
          change = true;
        }
      }
    } catch (NumberFormatException ex) {
      System.out.println(price + "-->请输入正确的价格");
      change = true;
    } finally {
      if (change) {
        format = price.subSequence(0, price.length() - 1).toString();
      }
      System.out.println(price + "-->" + format);
    }
  }
}