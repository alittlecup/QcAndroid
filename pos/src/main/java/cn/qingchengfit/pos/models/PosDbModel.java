package cn.qingchengfit.pos.models;

import android.support.annotation.NonNull;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.permission.QcDbManager;
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
 * Created by Paper on 2017/10/18.
 */

public class PosDbModel implements QcDbManager {
  @Override public Observable<List<QcStudentBean>> getAllStudent() {
    return null;
  }

  @Override public Observable<QcStudentBean> getStudentById(String id) {
    return null;
  }

  @Override public Observable<QcStudentBean> getStudentByPhone(String phone) {
    return null;
  }

  @Override public Observable<List<QcStudentBean>> getStudentByBrand(String brandid) {
    return null;
  }

  @Override
  public Observable<List<QcStudentBean>> getStudentByKeyWord(String keyword, String brandid) {
    return null;
  }

  @Override public Observable<List<QcStudentBean>> getStudentByKeyWord(String keyword) {
    return null;
  }

  @Override
  public Observable<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid,
    String keyword) {
    return null;
  }

  @Override public Observable<List<QcStudentBean>> getStudentByGym(String shopid) {
    return null;
  }

  @Override public void delStudentByBrand(String brandid, String id) {

  }

  @Override public void delAllStudent() {

  }

  @Override public void delStudentByGym(String gymid, String gymmodel, String id) {

  }

  @Override public void saveStudent(QcStudentBean qcStudent) {

  }

  @Override public void saveStudent(List<QcStudentBean> studentBeens, String brandid) {

  }

  @Override public void updateStudentCheckin(String avatar, String id) {

  }

  @Override public void delByBrandId(String brandId) {

  }

  @Override public Observable<List<CoachService>> getAllCoachService() {
    return null;
  }

  @Override public Observable<List<CoachService>> getAllCoachServiceByBrand(String brandId) {
    return null;
  }

  @Override public void writeGyms(List<CoachService> services) {

  }

  @Override public CoachService getGymNow(String gymid, String gymmodel) {
    return null;
  }

  @Override public CoachService getShopNameById(String brandId, String shopId) {
    return null;
  }

  @Override public Observable<List<CoachService>> getGymByModel(String id, String model) {
    return null;
  }

  @Override public Observable<List<CoachService>> getGymByGymId(String gymid) {
    return null;
  }

  @Override
  public Observable<List<CoachService>> getGymByShopIds(String brandid, List<String> shopid) {
    return null;
  }

  @Override public void writePermiss(List<Permission> permissions) {

  }

  @Override public void delPermiss() {

  }

  @Override public boolean check(String shopid, String key) {
    return true;
  }

  @Override public boolean checkMuti(String key, @NonNull List<String> shopids) {
    return true;
  }

  @Override public List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {
    return null;
  }

  @Override public boolean checkAtLeastOne(String key) {
    return true;
  }

  @Override public boolean checkNoOne(String key) {
    return true;
  }

  @Override public boolean checkAll(String key) {
    return true;
  }

  @Override public Observable<List<String>> queryAllFunctions() {
    return null;
  }

  @Override public void insertFunction(List<String> module) {

  }
}
