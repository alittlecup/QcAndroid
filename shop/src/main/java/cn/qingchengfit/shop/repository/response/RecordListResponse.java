package cn.qingchengfit.shop.repository.response;

import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.shop.vo.Record;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/22.
 */

public class RecordListResponse extends QcListData{
  public List<Record> records;
  public List<Record.Stat> stat;
}
