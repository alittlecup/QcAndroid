package cn.qingchengfit.saasbase.permission;

import android.support.annotation.NonNull;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/11.
 */
@Deprecated
public class SerPermisAction {

  @Inject GymWrapper gymWrapper;
  @Inject QcDbManager QCDbManager;
  @Inject GymBaseInfoAction gymBaseInfoAction;

  @Inject public SerPermisAction() {
  }

  public boolean checkNoOne(String key) {
    return QCDbManager.checkNoOne(key);
  }

  public void writePermiss(final List<Permission> permissions) {
    QCDbManager.writePermiss(permissions);
  }

  public void delPermiss() {
    QCDbManager.delPermiss();
  }

  public boolean check(String shopid, String key) {
    return QCDbManager.check(shopid, key);
  }

  public boolean checkMuti(String key, @NonNull List<String> shopids) {

    return QCDbManager.checkMuti(key, shopids);
  }

  public List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {

    return QCDbManager.checkMutiTrue(key, shopids);
  }

  public boolean check(String id, String model, String key) {
    String shopid = gymBaseInfoAction.getShopIdNow(id, model);
    try {
      return QCDbManager.check(shopid, key);
    } catch (Exception e) {
      return false;
    }
  }

  public boolean checkAtLeastOne(String key) {

    return QCDbManager.checkAtLeastOne(key);
  }

  public boolean checkAll(String key) {

    return QCDbManager.checkAll(key);
  }

  public boolean check(String key) {
    return QCDbManager.checkAll(key);
  }

  public boolean checkNoOnePer(String key) {
    return QCDbManager.checkNoOne(key);
  }

  public boolean checkHasAllMember() {
    return QCDbManager.check(gymWrapper.shop_id(), PermissionServerUtils.MANAGE_MEMBERS_IS_ALL);
  }
}
