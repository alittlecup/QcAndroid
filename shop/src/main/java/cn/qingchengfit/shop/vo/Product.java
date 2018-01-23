package cn.qingchengfit.shop.vo;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import cn.qingchengfit.shop.common.DoubleListFilterFragment;
import cn.qingchengfit.shop.ui.items.inventory.IInventoryItemData;
import cn.qingchengfit.shop.ui.items.product.IProductItemData;
import cn.qingchengfit.shop.util.SpanUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class Product
    implements IProductItemData, IInventoryItemData, DoubleListFilterFragment.IDoubleListData {
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
  private boolean status;
  private String off_sale_at;
  private String on_sale_at;
  private List<Integer> delivery_types;
  private String image;
  private List<String> images;
  private int id;
  private String unit;
  private String desc;
  private Category category;
  private List<Good> goods;
  private boolean is_free;
  private List<String> channels;
  private String name;
  private String created_at;
  private CreateBy create_by;
  private int month_sales;
  private int total_sales;
  private int priority;

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
    for (Good good : goods) {
      spannableStringBuilder = new SpanUtils().append(good.getInventory() + "")
          .setFontSize(14, true)
          .append("/" + unit)
          .setFontSize(12, true)
          .append("(" + good.getName() + ")")
          .setFontSize(10, true)
          .setForegroundColor(Color.rgb(204, 204, 204))
          .create();
    }
    return spannableStringBuilder;
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

  public int getPriority() {
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
    int min = Integer.MAX_VALUE;
    if (goods != null && !goods.isEmpty()) {
      for (Good good : goods) {
        if (good.getRule() != null && !good.getRule().isEmpty()) {
          for (Good.Rule rule : good.getRule()) {
            if (rule.getCost() < min) {
              min = rule.getCost();
            }
          }
        }
      }
    }
    return String.valueOf(min == Integer.MAX_VALUE ? 0 : min);
  }

  @Override public int getProductSales() {
    return total_sales;
  }

  @Override public int getProductInventory() {
    return getTotalInvetoryCount();
  }

  private int getTotalInvetoryCount() {
    int total = 0;
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
}
