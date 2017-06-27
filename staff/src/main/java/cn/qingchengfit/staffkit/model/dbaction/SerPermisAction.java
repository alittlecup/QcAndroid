package cn.qingchengfit.staffkit.model.dbaction;

import android.support.annotation.NonNull;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.usecase.bean.Permission;
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
public class SerPermisAction {

    @Inject GymWrapper gymWrapper;

    @Inject public SerPermisAction() {
    }

    public static boolean checkNoOne(String key) {
        return QCDbManager.checkNoOne(key);
    }

    public static void writePermiss(final List<Permission> permissions) {
        QCDbManager.writePermiss(permissions);
    }

    public static void delPermiss() {
        QCDbManager.delPermiss();
    }

    ;

    public static boolean check(String shopid, String key) {
        return QCDbManager.check(shopid, key);
    }

    public static boolean checkMuti(String key, @NonNull List<String> shopids) {

        return QCDbManager.checkMuti(key, shopids);
    }

    public static List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {

        return QCDbManager.checkMutiTrue(key, shopids);
    }

    public static boolean check(String id, String model, String key) {
        String shopid = GymBaseInfoAction.getShopIdNow(id, model);
        try {
            return QCDbManager.check(shopid, key);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkAtLeastOne(String key) {

        return QCDbManager.checkAtLeastOne(key);
    }

    public static boolean checkAll(String key) {

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
