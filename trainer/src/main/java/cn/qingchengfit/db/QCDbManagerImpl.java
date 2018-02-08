package cn.qingchengfit.db;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saasbase.db.GymFunctionDao;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class QCDbManagerImpl implements QcDbManager {
  private static final String TAG = "QCDbManager";
  AppDatabase appDatabase;

  public QCDbManagerImpl(App application) {
    appDatabase = Room.databaseBuilder(application, AppDatabase.class, "qc_coach")
      .allowMainThreadQueries()
      .build();
  }

  public String getLikeString(String keyword) {
    return "%" + keyword + "%";
  }

  ////////////////////////////////////////////////////////

  public Flowable<List<QcStudentBean>> getAllStudent() {
    return appDatabase.studentDao().getAllStudent();
  }

  public Flowable<QcStudentBean> getStudentById(String id) {
    return appDatabase.studentDao().getStudentById(id);
  }

  public Flowable<QcStudentBean> getStudentByPhone(String phone) {
    return appDatabase.studentDao().getStudentByPhone(phone);
  }

  @Deprecated public Flowable<List<QcStudentBean>> getStudentByBrand(String brandid) {
    return appDatabase.studentDao().getStudentByBrand(brandid);
  }

  public Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid) {
    return appDatabase.studentDao().getStudentByKeyWord(getLikeString(keyword));
  }

  public Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword) {
    return appDatabase.studentDao().getStudentByKeyWord(getLikeString(keyword));
  }

  @Deprecated
  public Maybe<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid,
    final String keyword) {
    return appDatabase.studentDao().getStudentByKeyWord(getLikeString(keyword));
  }

  /**
   * 根据shopid 获取用户
   */
  public Flowable<List<QcStudentBean>> getStudentByGym(String shopid) {
    return appDatabase.studentDao().getStudentByGym(shopid);
  }

  //删除会员  brandid 没用 品牌下直接在本地删除
  public void delStudentByBrand(String brandid, final String id) {
    appDatabase.studentDao().getStudentById(id).subscribe(qcStudentBean -> {
      appDatabase.studentDao().delStudent(qcStudentBean);
    });
  }

  public void delAllStudent() {
    Maybe.just("")
      .observeOn(Schedulers.io())
      .subscribe(s -> appDatabase.studentDao().delStudentAll(),
        throwable -> LogUtil.e("db", throwable.getMessage()));
  }

  //删除会员 场馆下也直接删除
  public void delStudentByGym(final String gymid, final String gymmodel, final String id) {
    appDatabase.studentDao().getStudentById(id).subscribe(qcStudentBean -> {
      appDatabase.studentDao().delStudent(qcStudentBean);
    });
  }


  public void saveStudent(final QcStudentBean qcStudent) {
    appDatabase.studentDao().getStudentById(qcStudent.getId()).subscribe(qcStudentBean -> {
      if (qcStudentBean != null) {
        if (TextUtils.isEmpty(qcStudentBean.getSupport_gym())) {
          if (qcStudentBean.getShops() != null && qcStudentBean.getShops().size() > 0) {
            String support = "";
            String support_ids = "";
            for (int i = 0; i < qcStudentBean.getShops().size(); i++) {
              Shop shop = qcStudentBean.getShops().get(i);
              support = TextUtils.concat(support, shop.name).toString();
              support_ids = TextUtils.concat(support_ids, shop.id).toString();
              if (i != qcStudentBean.getShops().size() - 1) {
                support = TextUtils.concat(support, ",").toString();
                support_ids = TextUtils.concat(support_ids, ",").toString();
              }
            }
            qcStudent.setSupoort_gym_ids(support_ids);
            qcStudent.setSupport_gym(support);
            qcStudent.setBrand_id(qcStudentBean.getBrand_id());
          }
        } else {
          qcStudent.setSupport_gym(qcStudentBean.getSupport_gym());
          qcStudent.setBrand_id(qcStudentBean.getBrand_id());
        }
        appDatabase.studentDao().updataStudent(qcStudent);
      } else {
        if (qcStudent.getShops() != null && qcStudent.getShops().size() > 0) {
          String support = "";
          String support_ids = "";
          for (int i = 0; i < qcStudent.getShops().size(); i++) {
            Shop shop = qcStudent.getShops().get(i);
            support = TextUtils.concat(support, shop.name).toString();
            support_ids = TextUtils.concat(support_ids, shop.id).toString();
            if (i != qcStudent.getShops().size() - 1) {
              support = TextUtils.concat(support, ",").toString();
              support_ids = TextUtils.concat(support_ids, ",").toString();
            }
          }
          qcStudent.setSupoort_gym_ids(support_ids);
          qcStudent.setSupport_gym(support);
        }
        appDatabase.studentDao().insertStudent(qcStudent);
      }
    });
  }

  public void saveStudent(List<QcStudentBean> studentBeens, String brandid) {
    appDatabase.studentDao().getAllStudent().subscribe(qcStudentBeans -> {
      appDatabase.studentDao().delStudentAll();
      appDatabase.studentDao().insertStudent(studentBeens.toArray(new QcStudentBean[] {}));
    });
  }

  public void updateStudentCheckin(String avatar, String id) {
    appDatabase.studentDao().getStudentById(id).subscribe(qcStudentBean -> {
      qcStudentBean.setCheckin_avatar(avatar);
      appDatabase.studentDao().updataStudent(qcStudentBean);
    });
  }

  public void delByBrandId(String brandId) {
    delAllStudent();
  }

  ////////////////////////////////////////////////////////////////

  public Flowable<List<CoachService>> getAllCoachService() {
    return appDatabase.serviceDao().getAllCoachService();
  }

  @Override public List<CoachService> getAllCoachServiceNow() {
    return appDatabase.serviceDao().getAllCoachServiceNow();
  }

  public Flowable<List<CoachService>> getAllCoachServiceByBrand(String brandId) {
    return appDatabase.serviceDao().getAllCoachServiceByBrand(brandId);
  }

  public void writeGyms(final List<CoachService> services) {
    Maybe.just("")
      .observeOn(Schedulers.io())
      .subscribe(s -> {appDatabase.serviceDao().deleteAll();
        appDatabase.serviceDao().insertService(services.toArray(new CoachService[] {}));}
        ,throwable -> LogUtil.e("db",throwable.getMessage())
        );

  }

  @Override public void delGyms(List<CoachService> services) {
    appDatabase.serviceDao().deleteAll();
  }

  @Override public void delGym(CoachService services) {
    appDatabase.serviceDao().delete(services);
  }

  public CoachService getGymNow(String gymid, String gymmodel) {
    return appDatabase.serviceDao().getByIdModelNow(gymid, gymmodel);
  }

  public CoachService getShopNameById(String brandId, String shopId) {
    return appDatabase.serviceDao().getByBrandIdAndShopIdNow(brandId, shopId);
  }

  public Flowable<CoachService> getGymByModel(String id, String model) {
    return appDatabase.serviceDao().getByIdModel(id, model);
  }

  public Flowable<CoachService> getGymByGymId(String gymid) {
    return appDatabase.serviceDao().getByGymId(gymid);
  }

  @Override public CoachService getGymByGymIdNow(String gymid) {
    return appDatabase.serviceDao().getByGymIdNow(gymid);
  }

  public Flowable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid) {
    return appDatabase.serviceDao().getByBrandIdAndShops(brandid, shopid);
  }

  ///////////////////////////////////////

  public void writePermiss(final List<Permission> permissions) {
    Maybe.just("").observeOn(Schedulers.io()).subscribe(s -> {
      appDatabase.permissonDao().deletePermissonAll();
      appDatabase.permissonDao().writePermmission(permissions.toArray(new Permission[] {}));
    }, throwable -> LogUtil.e("qcdb", throwable.getMessage()));
  }

  public void delPermiss() {
    Maybe.just("").observeOn(Schedulers.io()).subscribe(s -> {
      appDatabase.permissonDao().deletePermissonAll();
    }, throwable -> LogUtil.e("qcdb", throwable.getMessage()));
  }

  public boolean check(String shopid, String key) {
    List<Permission> list = appDatabase.permissonDao().getByShopIdAndKey(shopid, key);
    return list != null && list.size() > 0 && list.get(0).isValue();
  }

  public boolean checkMuti(String key, @NonNull List<String> shopids) {
    for (int i = 0; i < shopids.size(); i++) {
      List<Permission> list = appDatabase.permissonDao().getByShopIdAndKey(shopids.get(i), key);
      if (list == null || list.size() == 0 || !list.get(0).isValue()) {
        return false;
      }
    }
    return true;
  }

  public List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {
    List<String> ret = new ArrayList<>();
    for (int i = 0; i < shopids.size(); i++) {
      List<Permission> list = appDatabase.permissonDao().getByShopIdAndKey(shopids.get(i), key);
      if (list != null && list.size() > 0 && list.get(0).isValue()) {
        ret.add(shopids.get(i));
      }
    }
    return ret;
  }

  public boolean checkAtLeastOne(String key) {
    return appDatabase.permissonDao().getByKeyValue(true, key).size() > 0;
  }

  public boolean checkNoOne(String key) {
    return appDatabase.permissonDao().getByKeyValue(false, key).size() == 0;
  }

  public boolean checkAll(String key) {

    return appDatabase.permissonDao().getByKey(key).size() == appDatabase.permissonDao()
      .getByKeyValue(true, key)
      .size();
  }

  public Flowable<List<String>> queryAllFunctions() {
    return appDatabase.functionDao().getAllFuntion().flatMap(fuctionModules -> {
      List<String> ret = new ArrayList<>();
      for (GymFunctionDao.FuctionModule fuctionModule : fuctionModules) {
        ret.add(fuctionModule.module_name);
      }
      return Flowable.just(ret);
    });
  }

  public void insertFunction(List<String> module) {
    if (module == null) return;
    Maybe.just("").observeOn(Schedulers.io()).subscribe(s -> {
      appDatabase.functionDao().deleteFunctionAll();
      List<GymFunctionDao.FuctionModule> list = new ArrayList<>();
      for (String s1 : module) {
        GymFunctionDao.FuctionModule f1 = new GymFunctionDao.FuctionModule();
        f1.module_name = s1;
        list.add(f1);
      }
      appDatabase.functionDao().insert(list);
    });
  }
}
