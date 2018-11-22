package cn.qingchengfit.staffkit.dianping.vo;

import java.util.List;

public final class DianPingChooseDataEvent {
  private List<ISimpleChooseData> datas;
  @DianPingChooseType int type;

  public DianPingChooseDataEvent(List<ISimpleChooseData> datas, @DianPingChooseType int type) {
    this.datas = datas;
    this.type = type;
  }

  public List<ISimpleChooseData> getDatas() {
    return datas;
  }

  public int getType() {
    return type;
  }
}
