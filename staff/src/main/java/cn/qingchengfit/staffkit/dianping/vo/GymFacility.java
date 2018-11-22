package cn.qingchengfit.staffkit.dianping.vo;

public class GymFacility implements ISimpleChooseData {
  private int id;
  private String key;

  public int getId() {
    return id;
  }

  @Override public String getSign() {
    return String.valueOf(id);
  }

  @Override public String getText() {
    return GymFacilityConvert.convertFacilityKeyToString(key);
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof GymFacility)) {
      return false;
    }
    return ((GymFacility) obj).getSign().equals(getSign());
  }

}
