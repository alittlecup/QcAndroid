package cn.qingchengfit.model.common;

import android.support.annotation.NonNull;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class BottomChooseData {
  CharSequence content;
  CharSequence subContent;

  public CharSequence getContent() {
    return content;
  }

  public CharSequence getSubContent() {
    return subContent;
  }

  public BottomChooseData(@NonNull CharSequence content) {
    this.content = content;
  }

  public BottomChooseData(@NonNull CharSequence content, CharSequence subContent) {
    this.content = content;
    this.subContent = subContent;
  }
}
