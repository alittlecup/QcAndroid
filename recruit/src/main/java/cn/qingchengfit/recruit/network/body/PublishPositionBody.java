package cn.qingchengfit.recruit.network.body;

import java.util.HashMap;

/**
 * Created by fb on 2017/7/3.
 */

public class PublishPositionBody {
  public HashMap<String, Object> params = new HashMap<>();

  public PublishPositionBody(HashMap<String, Object> params) {
    this.params = params;
  }
}
