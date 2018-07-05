package cn.qingchengfit.student.inter;

import java.util.Map;

public interface LoadDataListener {
  void loadData(Map<String, ? extends Object> params);
}
