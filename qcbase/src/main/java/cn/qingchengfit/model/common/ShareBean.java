package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

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
 * Created by Paper on 15/10/31 2015.
 */
public class ShareBean implements Parcelable {
  public String title;
  public String desc;
  public String link;
  public String imgUrl;
  public List<ShareExtends> extra;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.desc);
    dest.writeString(this.link);
    dest.writeString(this.imgUrl);
    dest.writeTypedList(this.extra);
  }

  public ShareBean() {
  }

  protected ShareBean(Parcel in) {
    this.title = in.readString();
    this.desc = in.readString();
    this.link = in.readString();
    this.imgUrl = in.readString();
    this.extra = in.createTypedArrayList(ShareExtends.CREATOR);
  }

  public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
    @Override public ShareBean createFromParcel(Parcel source) {
      return new ShareBean(source);
    }

    @Override public ShareBean[] newArray(int size) {
      return new ShareBean[size];
    }
  };
}
