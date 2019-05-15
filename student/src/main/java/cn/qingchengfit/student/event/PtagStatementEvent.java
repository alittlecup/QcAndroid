package cn.qingchengfit.student.event;

public class PtagStatementEvent {

  public static final int CLOSE_TYPE = 1;
  public static final int DOWNLOAD_PDF = 2;

  private int type;

  public PtagStatementEvent(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
