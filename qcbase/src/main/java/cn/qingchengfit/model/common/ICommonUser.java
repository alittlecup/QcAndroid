package cn.qingchengfit.model.common;

public interface ICommonUser {
  String getAvatar();
  String getTitle();
  String getSubTitle();
  String getContent();
  String getId();
  String getRight();
  int getRightColor();
  int getGender();
  boolean filter(String str);
}
