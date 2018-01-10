package cn.qingchengfit.staffkit.permission;

import cn.qingchengfit.saasbase.repository.IPermissionModel;
import java.util.List;

/**
 * Created by fb on 2017/12/19.
 */

public class PermissionModel implements IPermissionModel {

  @Override public boolean check(String permission) {
    //qcDbManager.check(gymWrapper.shop_id(), permission);
    return true;
  }

  @Override public boolean checkInBrand(String permission) {
    return false;
  }

  @Override public boolean check(String permission, List<String> shopids) {
    return false;
  }
}
