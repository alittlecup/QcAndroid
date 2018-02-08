package cn.qingchengfit.saasbase.permission;

import android.support.annotation.NonNull;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.QcStudentBean;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import java.util.List;

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
  Flowable<List<QcStudentBean>> getAllStudent();
  Flowable<QcStudentBean> getStudentById(String id);
  Flowable<QcStudentBean> getStudentByPhone(String phone);
  Flowable<List<QcStudentBean>> getStudentByBrand(String brandid);
  Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid);
  Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword);
  Maybe<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid, final String keyword);
  Flowable<List<QcStudentBean>> getStudentByGym(String shopid);
  void delStudentByBrand(String brandid, final String id);
  void delAllStudent();
  void delStudentByGym(final String gymid, final String gymmodel, final String id);
  void saveStudent(final QcStudentBean qcStudent);
  void saveStudent(List<QcStudentBean> studentBeens, String brandid);
  void updateStudentCheckin(String avatar, String id);
  void delByBrandId(String brandId);

  //场馆相关
  Flowable<List<CoachService>> getAllCoachService();
  List<CoachService> getAllCoachServiceNow();
  Flowable<List<CoachService>> getAllCoachServiceByBrand(String brandId);
  void writeGyms(final List<CoachService> services);
  void delGyms(final List<CoachService> services);
  void delGym(CoachService services);
  CoachService getGymNow(String gymid, String gymmodel);
  CoachService getShopNameById(String brandId, String shopId);
  Flowable<CoachService> getGymByModel(String id, String model);
  Flowable<CoachService> getGymByGymId(String gymid);
  CoachService getGymByGymIdNow(String gymid);
  Flowable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid);

  //权限
  void writePermiss(final List<Permission> permissions);
  void delPermiss();
  boolean check(String shopid, String key);
  boolean checkMuti(String key, @NonNull List<String> shopids);
  List<String> checkMutiTrue(String key, @NonNull List<String> shopids);
  boolean checkAtLeastOne(String key);
  boolean checkNoOne(String key);
  boolean checkAll(String key);
  Flowable<List<String>> queryAllFunctions();
  void insertFunction(List<String> module);

}
