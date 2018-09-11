package cn.qingchengfit.student;

public class StudentListSelectEvent {
  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  boolean isSelected;
  public StudentListSelectEvent(boolean isSelectAll){
    this.isSelected=isSelectAll;
  }
}
