package cn.qingchengfit.events;

/**
 * Created by fb on 2017/8/30.
 */

public class NetWorkDialogEvent {

  public static final int EVENT_GET = 11;
  public static final int EVENT_POST = 12;
  public static final int EVENT_HIDE_DIALOG = 13;

  public int type;

  public NetWorkDialogEvent(int type) {
    this.type = type;
  }
}
