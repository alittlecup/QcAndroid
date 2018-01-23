package cn.qingchengfit.shop.repository.response;

import cn.qingchengfit.shop.vo.Record;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/22.
 */

public class RecordListResponse {
  public List<Record> records;
  public int total_inventory;
  public List<Record.Stat> stat;
}
