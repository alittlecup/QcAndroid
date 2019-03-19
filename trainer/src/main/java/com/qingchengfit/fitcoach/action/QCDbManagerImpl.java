//package com.qingchengfit.fitcoach.action;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import cn.qingchengfit.model.base.CoachService;
//import cn.qingchengfit.model.base.Permission;
//import cn.qingchengfit.model.base.QcStudentBean;
//import cn.qingchengfit.model.base.Shop;
//import cn.qingchengfit.saascommon.model.QcDbManager;
//import com.qingchengfit.fitcoach.App;
//import com.squareup.sqlbrite.BriteDatabase;
//import com.squareup.sqlbrite.SqlBrite;
//import com.squareup.sqldelight.SqlDelightStatement;
//import java.util.ArrayList;
//import java.util.List;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
//import timber.log.Timber;
//
//public class QCDbManagerImpl implements QcDbManager {
//  private static final String TAG = "QCDbManager";
//  // rx响应式数据库,
//
//
//  public QCDbManagerImpl(App application) {
//
//  }
//
//  public String getLikeString(String keyword) {
//    return "%" + keyword + "%";
//  }
//
//  ////////////////////////////////////////////////////////
//
//
//  ///////////////////////////////////////
//
//  @Override public Observable<List<QcStudentBean>> getAllStudent() {
//    return null;
//  }
//
//  @Override public Observable<QcStudentBean> getStudentById(String id) {
//    return null;
//  }
//
//  @Override public Observable<QcStudentBean> getStudentByPhone(String phone) {
//    return null;
//  }
//
//  @Override public Observable<List<QcStudentBean>> getStudentByBrand(String brandid) {
//    return null;
//  }
//
//  @Override
//  public Observable<List<QcStudentBean>> getStudentByKeyWord(String keyword, String brandid) {
//    return null;
//  }
//
//  @Override public Observable<List<QcStudentBean>> getStudentByKeyWord(String keyword) {
//    return null;
//  }
//
//  @Override
//  public Observable<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid,
//    String keyword) {
//    return null;
//  }
//
//  @Override public Observable<List<QcStudentBean>> getStudentByGym(String shopid) {
//    return null;
//  }
//
//  @Override public void delStudentByBrand(String brandid, String id) {
//
//  }
//
//  @Override public void delAllStudent() {
//
//  }
//
//  @Override public void delStudentByGym(String gymid, String gymmodel, String id) {
//
//  }
//
//  @Override public void saveStudent(QcStudentBean qcStudent) {
//
//  }
//
//  @Override public void saveStudent(List<QcStudentBean> studentBeens, String brandid) {
//
//  }
//
//  @Override public void updateStudentCheckin(String avatar, String id) {
//
//  }
//
//  @Override public void delByBrandId(String brandId) {
//
//  }
//
//  @Override public Observable<List<CoachService>> getAllCoachService() {
//    return null;
//  }
//
//  @Override public Observable<List<CoachService>> getAllCoachServiceByBrand(String brandId) {
//    return null;
//  }
//
//  @Override public void writeGyms(List<CoachService> services) {
//
//  }
//
//  @Override public CoachService getGymNow(String gymid, String gymmodel) {
//    return null;
//  }
//
//  @Override public CoachService getShopNameById(String brandId, String shopId) {
//    return null;
//  }
//
//  @Override public Observable<List<CoachService>> getGymByModel(String id, String model) {
//    return null;
//  }
//
//  @Override public Observable<List<CoachService>> getGymByGymId(String gymid) {
//    return null;
//  }
//
//  @Override
//  public Observable<List<CoachService>> getGymByShopIds(String brandid, List<String> shopid) {
//    return null;
//  }
//
//  public void writePermiss(final List<Permission> permissions) {
//    briteDatabase.execute(Permission.DELETEALLPERMISSION);
//    BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
//    try {
//      for (Permission permission : permissions) {
//        briteDatabase.insert(Permission.TABLE_NAME, Permission.FACTORY.marshal(permission).asContentValues());
//      }
//      transaction.markSuccessful();
//    } catch (Exception e) {
//      Timber.e(e.getMessage());
//    } finally {
//      transaction.end();
//    }
//  }
//
//  public void delPermiss() {
//    try {
//      briteDatabase.execute(Permission.DELETEALLPERMISSION);
//    } catch (Exception e) {
//      Timber.e(e.getMessage());
//    }
//  }
//
//  public boolean check(String shopid, String key) {
//    Cursor cursor = briteDatabase.getReadableDatabase()
//      .rawQuery(Permission.FACTORY.getByShopIdAndKey(shopid, key).statement,
//        Permission.FACTORY.getByShopIdAndKey(shopid, key).args);
//    if (cursor == null || cursor.getCount() <= 0) {
//      return false;
//    }
//    cursor.moveToFirst();
//    Permission permission = Permission.MAPPER.map(cursor);
//    return permission != null && permission.isValue();
//  }
//
//  public boolean checkMuti(String key, @NonNull List<String> shopids) {
//    for (int i = 0; i < shopids.size(); i++) {
//      SqlDelightStatement statement = Permission.FACTORY.getByShopIdAndKey(shopids.get(i), key);
//      Cursor cursor = briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
//      if (cursor == null || cursor.getCount() <= 0) {
//        return false;
//      }
//      cursor.moveToFirst();
//      Permission permission = Permission.MAPPER.map(cursor);
//      cursor.close();
//      if (permission == null || !permission.isValue()) {
//        return false;
//      }
//    }
//    return true;
//  }
//
//  public List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {
//    List<String> ret = new ArrayList<>();
//    for (int i = 0; i < shopids.size(); i++) {
//      SqlDelightStatement statement =
//        Permission.FACTORY.getByShopIdAndKeyValue(shopids.get(i), key, true);
//      Cursor cursor = briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
//      if (cursor == null || cursor.getCount() <= 0) {
//        return ret;
//      }
//      cursor.moveToFirst();
//      Permission permission = Permission.MAPPER.map(cursor);
//      if (permission != null && permission.isValue()) {
//        ret.add(shopids.get(i));
//      }
//    }
//    return ret;
//  }
//
//  public boolean checkAtLeastOne(String key) {
//    SqlDelightStatement statement = Permission.FACTORY.getByKeyValue(key, true);
//    Cursor cursor = briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
//    return cursor != null && cursor.getCount() > 0;
//  }
//
//  public boolean checkNoOne(String key) {
//    //SqlDelightStatement statement = Permission.FACTORY.getByKeyValue(key, true);
//    //Cursor cursor = briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
//    //return cursor == null || cursor.getCount() <= 0;
//  }
//
//  public boolean checkAll(String key) {
//
//    //SqlDelightStatement statement = Permission.FACTORY.getByKey(key);
//    //Cursor cursor = briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
//    //if (cursor == null || cursor.getCount() <= 0) {
//    //  return false;
//    //}
//    //cursor.moveToFirst();
//    //do {
//    //  Permission permission = Permission.MAPPER.map(cursor);
//    //  if (permission == null || !permission.isValue()) return false;
//    //} while (cursor.moveToNext());
//    //cursor.close();
//    //return true;
//  }
//
//  public Observable<List<String>> queryAllFunctions() {
//    return null;
//    //return briteDatabase.createQuery(MyFunctionModel.TABLE_NAME, "SELECT * FROM qc_function_module")
//    //  .mapToList(new Func1<Cursor, String>() {
//    //    @Override public String call(Cursor cursor) {
//    //      return cursor.getString(cursor.getColumnIndex("module_name"));
//    //    }
//    //  });
//  }
//
//  public void insertFunction(List<String> module) {
//
//    //BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
//    //try {
//    //  briteDatabase.delete(MyFunctionModel.TABLE_NAME, null);
//    //  if (module != null) {
//    //    for (int i = 0; i < module.size(); i++) {
//    //      String s = module.get(i);
//    //      ContentValues contentValues = new ContentValues(1);
//    //      contentValues.put(MyFunctionModel.MODULE_NAME, s);
//    //      briteDatabase.insert(MyFunctionModel.TABLE_NAME, contentValues, CONFLICT_REPLACE);
//    //    }
//    //  }
//    //  transaction.markSuccessful();
//    //} finally {
//    //  transaction.end();
//    //}
//  }
//}