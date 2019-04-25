package cn.qingchengfit.staffkit.views.signin.bean;

public class SignInTimePayOnceEvent {
  public SignInTimePayOnceEvent(boolean isOpen) {
    this(isOpen, 0);
  }

  public boolean isOpen() {
    return isOpen;
  }

  public float getPrice() {
    return price;
  }

  private boolean isOpen;
  private float price;

  public SignInTimePayOnceEvent(boolean isOpen, float price) {
    this.isOpen = isOpen;
    this.price = price;
  }
}
