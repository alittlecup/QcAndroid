package cn.qingchengfit.staffkit.dianping.vo;

import java.util.List;

public class DianPingShop {
  /**
   * shop_images : ["https://zoneke-img.b0.upaiyun.com/349c647b63f963528b9b44e8aa4e304b.jpg","https://zoneke-img.b0.upaiyun.com/06390c38557b4b704705f39e733aad68.png","https://zoneke-img.b0.upaiyun.com/afc88be01f59f28b7ec57f0854f466bc.JPG","https://zoneke-img.b0.upaiyun.com/349c647b63f963528b9b44e8aa4e304b.jpg"]
   * image : https://zoneke-img.b0.upaiyun.com/4d51c29e3c5ba750fd0ee9c086c3d04d.jpg
   * system_end : 2037-04-28T14:51:00
   * gd_district : {"code":"110105","name":"朝阳区"}
   * id : 1
   * gd_city : {"code":"110100","name":"北京市市辖区"}
   * gd_province : {"code":"110000","name":"北京市"}
   * weixin_image : http://zoneke-img.b0.upaiyun.com/0d0d3827-be74-44e1-90ce-9f8dfc601b30.png
   * end : 22:00
   * area : 1024
   * start : 08:00
   * gd_lat : 39.987593
   * system_start : 2016-12-20T00:03:00
   * email : support@qingcheng.it
   * opentime : [{"start":"00:00","end":"23:59","id":1,"day":1},{"start":"00:00","end":"23:59","id":2,"day":2},{"start":"00:00","end":"23:59","id":3,"day":3},{"start":"08:00","end":"23:59","id":4,"day":4},{"start":"00:00","end":"23:59","id":5,"day":5},{"start":"00:00","end":"23:59","id":6,"day":6},{"start":"00:00","end":"23:59","id":7,"day":7}]
   * description : <p style="white-space: normal;"><span style="font-size:
   * 14px;">完成12周并不容易，需要自己强大的内心和伙伴们相互的鼓舞。毕竟每一次的12周都被参赛者赋予了一个神圣的使命～我要瘦到能穿进最瘦时穿过的牛仔裤里。</span></p><p
   * style="white-space: normal;"><span style="font-size: 14px;"><br/></span></p><p
   * style="white-space: normal; text-align: center;"><span style="font-size:
   * 14px;">今天的故事主角～娟姐</span></p><p style="white-space: normal;"><span style="font-size:
   * 14px;">&nbsp;</span></p><p style="white-space: normal;"><span style="font-size:
   * 14px;">第一次见到娟姐时觉得有身上她有一股子霸气，&nbsp;像个大姐大不过很亲切。熟悉之后才知道娟姐是重庆人做了一手好菜。每次看到娟姐在朋友圈秀自己的拿手菜时都把身边的邻里及每一位教练馋得够呛。</span></p><p
   * style="white-space: normal;"><span style="font-size: 14px;">&nbsp;</span></p><p
   * style="white-space: normal;"><span style="font-size: 14px;">报名12周前娟姐真的不瘦啊，生完两个孩子的她已经没有了曼妙的身姿，在很多朋友的鼓励下，娟姐下定决心报名2013年的12周。测试后，教练们在一起分析她各项的数据时刘姐看到果断地说：“她有可能会成为今年的冠军！”
   * 然后刘姐补充道：“我完全凭直觉，娟姐身上有股子倔犟劲头，我感觉她能行”。</span></p><p style="white-space: normal;"><span
   * style="font-size: 14px;">&nbsp;</span></p><p style="white-space: normal;"><span
   * style="font-size: 14px;"></span></p><p style="white-space: normal;"><span style="font-size:
   * 14px;">12周开始后，娟姐果然非常配合教练的安排，认真的上课，认真的做训练日记，在比赛进行的第七周时她已经彻底地变了一个人。</span><br/></p><p
   * style="text-align: center; white-space: normal;"><img alt="" src="https://zoneke-img.b0.upaiyun.com/c63b242fa4e8e8250242a690eb272a2f.JPG"/>&nbsp;</p>
   * tags : []
   * weixin : ceshi
   * brand : {"cloud_brand_id":2,"id":1,"name":"青橙健身"}
   * gym_id : 172
   * phone : 4008007986
   * shop_services : [{"id":25,"key":"shower"},{"id":27,"key":"air-conditioner"},{"id":28,"key":"air-cleaner"},{"id":30,"key":"water-bar"},{"id":33,"key":"faCard"},{"id":32,"key":"parking"},{"id":34,"key":"wifi"},{"id":31,"key":"leisure-area"},{"id":23,"key":"locker-room"},{"id":24,"key":"shop"},{"id":26,"key":"swimming-pool"},{"id":29,"key":"dd-water"}]
   * address : 朝阳区酒仙桥北路798艺术区706北三街
   * photo_url : https://zoneke-img.b0.upaiyun.com/4d51c29e3c5ba750fd0ee9c086c3d04d.jpg
   * gd_lng : 116.497347
   * photo_id :
   * name : 青橙第三弹
   * weixin_success : true
   * district_id :
   */

  private String image;
  private String system_end;
  private GdDistrictBean gd_district;
  private int id;
  private GdCityBean gd_city;
  private GdProvinceBean gd_province;
  private String weixin_image;
  private String end;
  private float area;
  private String start;
  private double gd_lat;
  private String system_start;
  private String email;
  private String description;
  private String weixin;
  private BrandBean brand;
  private int gym_id;
  private String phone;
  private String address;
  private String photo_url;
  private double gd_lng;
  private String photo_id;
  private String name;
  private boolean weixin_success;
  private String district_id;
  private List<String> shop_images;
  private List<OpentimeBean> opentime;
  private List<GymTag> tags;
  private List<GymFacility> shop_services;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getSystem_end() {
    return system_end;
  }

  public void setSystem_end(String system_end) {
    this.system_end = system_end;
  }

  public GdDistrictBean getGd_district() {
    return gd_district;
  }

  public void setGd_district(GdDistrictBean gd_district) {
    this.gd_district = gd_district;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public GdCityBean getGd_city() {
    return gd_city;
  }

  public void setGd_city(GdCityBean gd_city) {
    this.gd_city = gd_city;
  }

  public GdProvinceBean getGd_province() {
    return gd_province;
  }

  public void setGd_province(GdProvinceBean gd_province) {
    this.gd_province = gd_province;
  }

  public String getWeixin_image() {
    return weixin_image;
  }

  public void setWeixin_image(String weixin_image) {
    this.weixin_image = weixin_image;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public float getArea() {
    return area;
  }

  public void setArea(float area) {
    this.area = area;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public double getGd_lat() {
    return gd_lat;
  }

  public void setGd_lat(double gd_lat) {
    this.gd_lat = gd_lat;
  }

  public String getSystem_start() {
    return system_start;
  }

  public void setSystem_start(String system_start) {
    this.system_start = system_start;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWeixin() {
    return weixin;
  }

  public void setWeixin(String weixin) {
    this.weixin = weixin;
  }

  public BrandBean getBrand() {
    return brand;
  }

  public void setBrand(BrandBean brand) {
    this.brand = brand;
  }

  public int getGym_id() {
    return gym_id;
  }

  public void setGym_id(int gym_id) {
    this.gym_id = gym_id;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoto_url() {
    return photo_url;
  }

  public void setPhoto_url(String photo_url) {
    this.photo_url = photo_url;
  }

  public double getGd_lng() {
    return gd_lng;
  }

  public void setGd_lng(double gd_lng) {
    this.gd_lng = gd_lng;
  }

  public String getPhoto_id() {
    return photo_id;
  }

  public void setPhoto_id(String photo_id) {
    this.photo_id = photo_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isWeixin_success() {
    return weixin_success;
  }

  public void setWeixin_success(boolean weixin_success) {
    this.weixin_success = weixin_success;
  }

  public String getDistrict_id() {
    return district_id;
  }

  public void setDistrict_id(String district_id) {
    this.district_id = district_id;
  }

  public List<String> getShop_images() {
    return shop_images;
  }

  public void setShop_images(List<String> shop_images) {
    this.shop_images = shop_images;
  }

  public List<OpentimeBean> getOpentime() {
    return opentime;
  }

  public void setOpentime(List<OpentimeBean> opentime) {
    this.opentime = opentime;
  }

  public List<GymTag> getTags() {
    return tags;
  }

  public void setTags(List<GymTag> tags) {
    this.tags = tags;
  }

  public String getTagsString() {
    if (shop_services == null || shop_services.isEmpty()) {
      return "";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (GymTag tag : tags) {
      stringBuilder.append(tag.getText()).append(";");
    }
    return stringBuilder.toString();
  }

  public List<GymFacility> getShop_services() {
    return shop_services;
  }

  public String getFacilitiesString() {
    if (shop_services == null || shop_services.isEmpty()) {
      return "";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (GymFacility facility : shop_services) {
      stringBuilder.append(GymFacilityConvert.convertFacilityKeyToString(facility.getKey()))
          .append(";");
    }
    return stringBuilder.toString();
  }

  public void setShop_services(List<GymFacility> shop_services) {
    this.shop_services = shop_services;
  }

  public static class GdDistrictBean {
    /**
     * code : 110105
     * name : 朝阳区
     */

    private String code;
    private String name;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class GdCityBean {
    /**
     * code : 110100
     * name : 北京市市辖区
     */

    private String code;
    private String name;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class GdProvinceBean {
    /**
     * code : 110000
     * name : 北京市
     */

    private String code;
    private String name;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class BrandBean {
    /**
     * cloud_brand_id : 2
     * id : 1
     * name : 青橙健身
     */

    private int cloud_brand_id;
    private int id;
    private String name;

    public int getCloud_brand_id() {
      return cloud_brand_id;
    }

    public void setCloud_brand_id(int cloud_brand_id) {
      this.cloud_brand_id = cloud_brand_id;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class OpentimeBean {
    /**
     * start : 00:00
     * end : 23:59
     * id : 1
     * day : 1
     */

    private String start;
    private String end;
    private int id;
    private int day;

    public String getStart() {
      return start;
    }

    public void setStart(String start) {
      this.start = start;
    }

    public String getEnd() {
      return end;
    }

    public void setEnd(String end) {
      this.end = end;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getDay() {
      return day;
    }

    public void setDay(int day) {
      this.day = day;
    }
  }
}
