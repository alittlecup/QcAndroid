package cn.qingchengfit.recruit.views.organization;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/24 2015.
 */
public class SearchItemBean {
  String name;
  String city;
  String img;
  boolean isAuthor;

  public SearchItemBean(String name, String img, String city, boolean isAuthor) {
    this.name = name;
    this.img = img;
    this.city = city;
    this.isAuthor = isAuthor;
  }

  public SearchItemBean() {
  }

  public SearchItemBean(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public boolean isAuthor() {
    return isAuthor;
  }

  public void setIsAuthor(boolean isAuthor) {
    this.isAuthor = isAuthor;
  }
}
