package cn.qingchengfit.recruit.model;

import java.util.List;

/**
 * Created by fb on 2017/7/3.
 */

public class PublishPosition {
  public List<String> names;
  public List<Object> values;

  public PublishPosition(List<String> names, List<Object> values) {
    this.names = names;
    this.values = values;
  }

  public static PublishPosition build(List<String> names, List<Object> values) {
    PublishPosition publishPosition = new PublishPosition(names, values);
    return publishPosition;
  }
}
