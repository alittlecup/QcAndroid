package cn.qingchengfit.saascommon.item;

import cn.qingchengfit.model.base.QcStudentBean;
import eu.davidea.flexibleadapter.items.IHeader;

public interface IItemData {
  QcStudentBean getData();
  void setHeader(IHeader header);
  IHeader getHeader();
}
