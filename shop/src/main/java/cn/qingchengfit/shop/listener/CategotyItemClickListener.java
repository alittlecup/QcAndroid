package cn.qingchengfit.shop.listener;

import cn.qingchengfit.shop.ui.items.category.ICategotyItemData;

/**
 * Created by huangbaole on 2018/1/21.
 */

public interface CategotyItemClickListener {
  void onDeleteClick(ICategotyItemData category);
  void onPutClick(ICategotyItemData category);

}
