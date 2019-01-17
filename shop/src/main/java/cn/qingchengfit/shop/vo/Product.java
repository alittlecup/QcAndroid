package cn.qingchengfit.shop.vo;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.shop.ui.items.inventory.IInventoryItemData;
import android.text.TextUtils;
import cn.qingchengfit.shop.ui.items.product.IProductItemData;
import cn.qingchengfit.views.fragments.DoubleListFilterFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class Product
    implements IProductItemData, IInventoryItemData, DoubleListFilterFragment.IDoubleListData,
    Parcelable {
  /**
   * {
   * "products": [
   * {
   * "shop": {
   * "id": 1,
   * "name": "引力工场-阳光上东店"
   * },
   * "status": true/false,
   * "off_sale_at": "2018-01-13T18:26:22",
   * "on_sale_at": "2018-01-13T18:26:22",
   * "delivery_types": [1,2],
   * "image": "",
   * "images": [],
   * "id": 1,
   * "unit": "个",
   * "desc": "balala...",
   * "category": {
   * "id": 7,
   * "name": "分类7"
   * },
   * "goods": [{
   * "name": "规格32",
   * "rule": [{
   * "channel": "CARD",
   * "cost": 10,
   * }],
   * "created_at": "2018-01-13T18:26:22",
   * "inventory": 32,
   * "id": 32
   * },],
   * "is_free": true/false,
   * "channels": ["CARD", "OFFLINE", "ONLINE"],
   * "name": "商品1",
   * "created_at": "2018-01-13T18:26:22",
   * "created_by": {
   * "username": "陈驰远",
   * "id": 1
   * },
   * "month_sales": 0,
   * "total_sales": 0,
   * "priority": 1
   * },
   * ]
   * }
   */
  private Shop shop;
  private Boolean status;
  private String off_sale_at;
  private String on_sale_at;
  private List<Integer> delivery_types;
  private String image;
  private List<String> images;
  private String id;
  private String unit;
  private String desc;
  private Category category;
  private String category_id;

  public String getCategory_id() {
    return category_id;
  }

  public void setCategory_id(String category_id) {
    this.category_id = category_id;
  }

  private List<Good> goods;
  private Boolean is_free;
  private List<String> channels;
  private String name;
  private String created_at;
  private CreateBy create_by;
  private Integer month_sales;
  private Integer total_sales;
  private Integer priority;

  public List<Integer> getCard_tpl_ids() {
    return card_tpl_ids;
  }

  public void setCard_tpl_ids(List<Integer> card_tpl_ids) {
    this.card_tpl_ids = card_tpl_ids;
  }

  private List<Integer> card_tpl_ids;

  public boolean getSupport_card() {
    return support_card;
  }

  public void setSupport_card(boolean support_card) {
    this.support_card = support_card;
  }

  private boolean support_card;

  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getOff_sale_at() {
    return off_sale_at;
  }

  public void setOff_sale_at(String off_sale_at) {
    this.off_sale_at = off_sale_at;
  }

  public String getOn_sale_at() {
    return on_sale_at;
  }

  public void setOn_sale_at(String on_sale_at) {
    this.on_sale_at = on_sale_at;
  }

  public List<Integer> getDelivery_types() {
    return delivery_types;
  }

  public void setDelivery_types(List<Integer> delivery_types) {
    this.delivery_types = delivery_types;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<Good> getGoods() {
    return goods;
  }

  public void setGoods(List<Good> goods) {
    this.goods = goods;
  }

  public boolean isIs_free() {
    return is_free;
  }

  public void setIs_free(boolean is_free) {
    this.is_free = is_free;
  }

  public List<String> getChannels() {
    return channels;
  }

  public void setChannels(List<String> channels) {
    this.channels = channels;
  }

  public String getName() {
    return name;
  }

  @Override public String getImageUri() {
    return image;
  }

  @Override public boolean getProductStatus() {
    return status;
  }

  @Override public String getInventoryCount() {

    return String.valueOf(getTotalInvetoryCount());
  }

  @Override public String getProductUnit() {
    return unit;
  }

  @Override public CharSequence getCategoryDetail() {

    return getGoodsDetail();
  }

  private CharSequence getGoodsDetail() {
    SpanUtils spanUtils = new SpanUtils();
    for (Good good : goods) {
      spanUtils.append(good.getInventory() + "")
          .setFontSize(14, true)
          .append(unit)
          .setFontSize(12, true)
          .append("(" + (TextUtils.isEmpty(good.getName()) ? "常规" : good.getName()) + ")")
          .setFontSize(10, true)
          .setForegroundColor(Color.rgb(204, 204, 204));
    }
    return spanUtils.create();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public CreateBy getCreate_by() {
    return create_by;
  }

  public void setCreate_by(CreateBy create_by) {
    this.create_by = create_by;
  }

  public int getMonth_sales() {
    return month_sales;
  }

  public void setMonth_sales(int month_sales) {
    this.month_sales = month_sales;
  }

  public int getTotal_sales() {
    return total_sales;
  }

  public void setTotal_sales(int total_sales) {
    this.total_sales = total_sales;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override public String getProductName() {
    return name;
  }

  @Override public String getProductImage() {
    return image;
  }

  @Override public String getProductAddTime() {
    return created_at;
  }

  @Override public String getProductPrices() {
    double min = Integer.MAX_VALUE;
    if (goods != null && !goods.isEmpty()) {
      for (Good good : goods) {
        if (good.getRule() != null && !good.getRule().isEmpty()) {
          for (Good.Rule rule : good.getRule()) {
            Double aDouble = Double.valueOf(rule.getCost());
            if (aDouble < min) {
              min = aDouble;
            }
          }
        }
      }
    }
    return String.valueOf(min == Integer.MAX_VALUE ? 0 : min);
  }

  @Override public boolean isSinglePrices() {
    double prices = -1d;
    if (goods != null && !goods.isEmpty()) {
      for (Good good : goods) {
        if (good.getRule() != null && !good.getRule().isEmpty()) {
          for (Good.Rule rule : good.getRule()) {
            Double aDouble = Double.valueOf(rule.getCost());
            if (prices != -1d && prices != aDouble) {
              return false;
            }
            prices = aDouble;
          }
        }
      }
    }
    return true;
  }

  @Override public String getProductId() {
    return id;
  }

  @Override public int getProductSales() {
    return total_sales;
  }

  @Override public long getProductInventory() {
    return getTotalInvetoryCount();
  }

  private long getTotalInvetoryCount() {
    long total = 0;
    if (goods != null && !goods.isEmpty()) {
      for (Good good : goods) {
        total += good.getInventory();
      }
    }
    return total;
  }

  @Override public int getProductPriority() {
    return priority;
  }

  @Override public String getText() {
    return name;
  }

  @Override public List<String> getChildText() {
    List<String> childNames = new ArrayList<>();
    if (goods != null && !goods.isEmpty()) {
      for (Good good : goods) {
        childNames.add(good.getName());
      }
    }
    return childNames;
  }

  public Product() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.shop, flags);
    dest.writeValue(this.status);
    dest.writeString(this.off_sale_at);
    dest.writeString(this.on_sale_at);
    dest.writeList(this.delivery_types);
    dest.writeString(this.image);
    dest.writeStringList(this.images);
    dest.writeString(this.id);
    dest.writeString(this.unit);
    dest.writeString(this.desc);
    dest.writeParcelable(this.category, flags);
    dest.writeString(this.category_id);
    dest.writeTypedList(this.goods);
    dest.writeValue(this.is_free);
    dest.writeStringList(this.channels);
    dest.writeString(this.name);
    dest.writeString(this.created_at);
    dest.writeParcelable(this.create_by, flags);
    dest.writeValue(this.month_sales);
    dest.writeValue(this.total_sales);
    dest.writeValue(this.priority);
    dest.writeList(this.card_tpl_ids);
    dest.writeByte(this.support_card ? (byte) 1 : (byte) 0);
  }

  protected Product(Parcel in) {
    this.shop = in.readParcelable(Shop.class.getClassLoader());
    this.status = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.off_sale_at = in.readString();
    this.on_sale_at = in.readString();
    this.delivery_types = new ArrayList<Integer>();
    in.readList(this.delivery_types, Integer.class.getClassLoader());
    this.image = in.readString();
    this.images = in.createStringArrayList();
    this.id = in.readString();
    this.unit = in.readString();
    this.desc = in.readString();
    this.category = in.readParcelable(Category.class.getClassLoader());
    this.category_id = in.readString();
    this.goods = in.createTypedArrayList(Good.CREATOR);
    this.is_free = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.channels = in.createStringArrayList();
    this.name = in.readString();
    this.created_at = in.readString();
    this.create_by = in.readParcelable(CreateBy.class.getClassLoader());
    this.month_sales = (Integer) in.readValue(Integer.class.getClassLoader());
    this.total_sales = (Integer) in.readValue(Integer.class.getClassLoader());
    this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
    this.card_tpl_ids = new ArrayList<Integer>();
    in.readList(this.card_tpl_ids, Integer.class.getClassLoader());
    this.support_card = in.readByte() != 0;
  }

  public static final Creator<Product> CREATOR = new Creator<Product>() {
    @Override public Product createFromParcel(Parcel source) {
      return new Product(source);
    }

    @Override public Product[] newArray(int size) {
      return new Product[size];
    }
  };
}
