package cn.qingchengfit.saasbase.permission;

import android.support.annotation.NonNull;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.QcStudentBean;
import java.util.List;
import rx.Observable;

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
 * Created by Paper on 2017/8/14.
 */

public interface QcDbManager {
  //学员相关
  Observable<List<QcStudentBean>> getAllStudent();
  Observable<QcStudentBean> getStudentById(String id);
  Observable<QcStudentBean> getStudentByPhone(String phone);
  Observable<List<QcStudentBean>> getStudentByBrand(String brandid);
  Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid);
  Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword);
  Observable<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid, final String keyword);
  Observable<List<QcStudentBean>> getStudentByGym(String shopid);
  void delStudentByBrand(String brandid, final String id);
  void delAllStudent();
  void delStudentByGym(final String gymid, final String gymmodel, final String id);
  void saveStudent(final QcStudentBean qcStudent);
  void saveStudent(List<QcStudentBean> studentBeens, String brandid);
  void updateStudentCheckin(String avatar, String id);
  void delByBrandId(String brandId);

  //场馆相关
  Observable<List<CoachService>> getAllCoachService();
  Observable<List<CoachService>> getAllCoachServiceByBrand(String brandId);
  void writeGyms(final List<CoachService> services);
  CoachService getGymNow(String gymid, String gymmodel);
  CoachService getShopNameById(String brandId, String shopId);
  Observable<List<CoachService>> getGymByModel(String id, String model);
  Observable<List<CoachService>> getGymByGymId(String gymid);
  Observable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid);

  //权限
  void writePermiss(final List<Permission> permissions);
  void delPermiss();
  boolean check(String shopid, String key);
  boolean checkMuti(String key, @NonNull List<String> shopids);
  List<String> checkMutiTrue(String key, @NonNull List<String> shopids);
  boolean checkAtLeastOne(String key);
  boolean checkNoOne(String key);
  boolean checkAll(String key);
  Observable<List<String>> queryAllFunctions();
  void insertFunction(List<String> module);

}
