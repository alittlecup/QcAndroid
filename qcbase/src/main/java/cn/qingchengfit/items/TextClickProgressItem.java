package cn.qingchengfit.items;

/**
 * Created by huangbaole on 2018/4/21.
 */

public class TextClickProgressItem extends SimpleTextItemItem {

  public TextClickProgressItem(String text) {
    super(text);
  }

  public TextClickProgressItem(String text, float textSize) {
    super(text, textSize);
  }

  public TextClickProgressItem(String text, Integer g) {
    super(text, g);
  }

  @Override public boolean equals(Object o) {
    return o instanceof TextClickProgressItem;
  }
}
