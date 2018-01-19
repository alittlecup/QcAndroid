//package cn.qingchengfit.saasbase.network.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * 健身房的场地
// * <p/>
// * Created by Paper on 16/2/23 2016.
// */
//public class Space implements Parcelable {
//    public static final Creator<Space> CREATOR = new Creator<Space>() {
//        @Override public Space createFromParcel(Parcel source) {
//            return new Space(source);
//        }
//
//        @Override public Space[] newArray(int size) {
//            return new Space[size];
//        }
//    };
//    String name;
//    String capacity;
//    String shop_id;
//    String id;
//    boolean is_support_private;
//    boolean is_support_team;
//
//    public Space() {
//    }
//
//    public Space(String name, String capacity, boolean is_support_private, boolean is_support_team) {
//        this.name = name;
//        this.capacity = capacity;
//        this.is_support_private = is_support_private;
//        this.is_support_team = is_support_team;
//    }
//
//    protected Space(Parcel in) {
//        this.name = in.readString();
//        this.capacity = in.readString();
//        this.shop_id = in.readString();
//        this.id = in.readString();
//        this.is_support_private = in.readByte() != 0;
//        this.is_support_team = in.readByte() != 0;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getShop_id() {
//        return shop_id;
//    }
//
//    public void setShop_id(String shop_id) {
//        this.shop_id = shop_id;
//    }
//
//    public boolean is_support_team() {
//        return is_support_team;
//    }
//
//    public void setIs_support_team(boolean is_support_team) {
//        this.is_support_team = is_support_team;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCapacity() {
//        return capacity;
//    }
//
//    public void setCapacity(String capacity) {
//        this.capacity = capacity;
//    }
//
//    public boolean is_support_private() {
//        return is_support_private;
//    }
//
//    public void setIs_support_private(boolean is_support_private) {
//        this.is_support_private = is_support_private;
//    }
//
//    @Override public int describeContents() {
//        return 0;
//    }
//
//    @Override public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.name);
//        dest.writeString(this.capacity);
//        dest.writeString(this.shop_id);
//        dest.writeString(this.id);
//        dest.writeByte(is_support_private ? (byte) 1 : (byte) 0);
//        dest.writeByte(is_support_team ? (byte) 1 : (byte) 0);
//    }
//}
