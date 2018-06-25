package cn.qingchengfit.saasbase.mvvm_student.inter;

import java.util.Map;

public interface LoadDataListener {
  void loadData(Map<String, ? extends Object> params);
}
