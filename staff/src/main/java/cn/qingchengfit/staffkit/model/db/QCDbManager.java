package cn.qingchengfit.staffkit.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.usecase.bean.Permission;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class QCDbManager {
    private static final String TAG = "QCDbManager";
    // rx响应式数据库,
    public static BriteDatabase briteDatabase;
  static QCDbOpenHelper dbOpenHelper;
    @Inject public QCDbManager(App application) {


        // sqlbrite 初始化,构造出响应式数据库,添加log
        SqlBrite sqlBrite;
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override public void log(String message) {
                //                Logger.wtf(TAG, "log: >>>>" + message);
                Timber.d("db" + message);
            }
        });
        // 原生的sqllitehelper 用来建立数据库和数据表,以及构造,rx响应式数据库
        dbOpenHelper = new QCDbOpenHelper(application);
        // 执行slqbirte 构造数据库的语句
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
        briteDatabase.setLoggingEnabled(false);
    }

    public static String getLikeString(String keyword) {
        return "%" + keyword + "%";
    }

    ////////////////////////////////////////////////////////

    public static Observable<List<QcStudentBean>> getAllStudent() {
        return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.getAllStudent().statement)
            .mapToList(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<QcStudentBean> getStudentById(String id) {
      SqlDelightStatement statement = QcStudentBean.FACTORY.getStudentById(id);
      return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, statement.statement,
          statement.args)
            .mapToOne(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<QcStudentBean> getStudentByPhone(String phone) {
      SqlDelightStatement statement = QcStudentBean.FACTORY.getStudentByPhone(phone);
      return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, statement.statement,
          statement.args)
            .mapToOne(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<List<QcStudentBean>> getStudentByBrand(String brandid) {
      SqlDelightStatement statement = QcStudentBean.FACTORY.getStudentByBrand(brandid);
      return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, statement.statement,
          statement.args)
            .mapToList(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid) {
      String sqlStr = "SELECT *\n"
          + "FROM QcStudentBean WHERE (username LIKE ? or phone LIKE ?) and brand_id = ? ";
        return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, sqlStr, getLikeString(keyword),
            getLikeString(keyword), brandid)
            .mapToList(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword) {
      String sqlStr = "SELECT *\n" + "FROM QcStudentBean WHERE (username LIKE ? or phone LIKE ?)";
        return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, sqlStr, getLikeString(keyword),
            getLikeString(keyword)).mapToList(new Func1<Cursor, QcStudentBean>() {
            @Override public QcStudentBean call(Cursor cursor) {
                return QcStudentBean.MAPPER.map(cursor);
            }
        });
    }

    public static Observable<List<QcStudentBean>> getStudentByKeyWordShop(String brandid, String shopid, final String keyword) {
      String sqlStr = "SELECT *\n"
          + "FROM QcStudentBean WHERE (username LIKE ? or phone LIKE ?) and brand_id = ? and supoort_gym_ids LIKE ?";
      return briteDatabase.createQuery(QcStudentBean.TABLE_NAME, sqlStr, getLikeString(keyword),
          getLikeString(keyword), brandid, "%," + shopid + ",%")
            .mapToList(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    public static Observable<List<QcStudentBean>> getStudentByGym(String shopid) {
      return briteDatabase.createQuery(QcStudentBean.TABLE_NAME,
          QcStudentBean.FACTORY.getStudentByGym(getLikeString(shopid)).statement,
          QcStudentBean.FACTORY.getStudentByGym(getLikeString(shopid)).args)
            .mapToList(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            });
    }

    //
    public static void delStudentByBrand(String brandid, final String id) {
        briteDatabase.delete(QcStudentBean.TABLE_NAME, QcStudentBean.ID + " = ? ", id);
    }

    public static void delAllStudent() {
        briteDatabase.execute(QcStudentBean.DELALLSTUDENT);
    }

    public static void delStudentByGym(final String gymid, final String gymmodel, final String id) {
        getStudentById(id).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<QcStudentBean>() {
            @Override public void onCompleted() {
                this.unsubscribe();
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(QcStudentBean bean) {
                String shopid = GymBaseInfoAction.getShopIdNow(gymid, gymmodel);
                String shopname = GymBaseInfoAction.getShopNow(gymid, gymmodel).getName();
                String spids = bean.getSupoort_gym_ids();
                String spNas = bean.getSupport_gym();
                if (spids.contains(shopid)) {
                    spids = spids.replace(shopid, "");
                    spids = spids.replace(",,", ",");
                    spNas = spNas.replace(shopname, "");
                    spNas = spNas.replace(",,", ",");
                }
                bean.setSupoort_gym_ids(spids);
                bean.setSupport_gym(spNas);
                briteDatabase.update(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.marshal(bean).asContentValues(), QcStudentBean.ID,
                    bean.getId());
            }
        });
    }

    public static void saveStudent(final QcStudentBean qcStudent) {
        briteDatabase.createQuery(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.getStudentById(qcStudent.getId()).statement)
            .mapToOne(new Func1<Cursor, QcStudentBean>() {
                @Override public QcStudentBean call(Cursor cursor) {
                    return QcStudentBean.MAPPER.map(cursor);
                }
            }).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcStudentBean>() {
                @Override public void onCompleted() {
                    this.unsubscribe();
                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcStudentBean qcStudentBean) {

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
                        briteDatabase.update(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.marshal(qcStudent).asContentValues(),
                            QcStudentBean.ID + " = ? ", qcStudent.getId());
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
                        briteDatabase.insert(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.marshal(qcStudent).asContentValues());
                    }
                }
            });
    }

    public static void saveStudent(List<QcStudentBean> studentBeens, String brandid) {
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            delAllStudent();
            for (QcStudentBean studentBean : studentBeens) {
                briteDatabase.insert(QcStudentBean.TABLE_NAME, QcStudentBean.FACTORY.marshal(studentBean).asContentValues());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public static void updateStudentCheckin(String avatar, String id) {
        briteDatabase.execute(QcStudentBean.FACTORY.updateStudentById(avatar, id).statement);
    }

    public static void delByBrandId(String brandId) {
        briteDatabase.delete(QcStudentBean.TABLE_NAME, QcStudentBean.BRAND_ID + " = ? ", brandId);
    }

    ////////////////////////////////////////////////////////////////

    public static Observable<List<CoachService>> getAllCoachService() {
        return briteDatabase.createQuery(CoachService.TABLE_NAME, CoachService.FACTORY.getAllCoachService().statement)
            .mapToList(new Func1<Cursor, CoachService>() {
                @Override public CoachService call(Cursor cursor) {
                    return CoachService.FACTORY.getAllCoachServiceMapper().map(cursor);
                }
            });
    }

    public static Observable<List<CoachService>> getAllCoachServiceByBrand(String brandId) {
      return briteDatabase.createQuery(CoachService.TABLE_NAME,
          CoachService.FACTORY.getAllCoachServiceByBrand(brandId).statement,
          CoachService.FACTORY.getAllCoachServiceByBrand(brandId).args)
            .mapToList(new Func1<Cursor, CoachService>() {
                @Override public CoachService call(Cursor cursor) {
                    return CoachService.FACTORY.getAllCoachServiceByBrandMapper().map(cursor);
                }
            });
    }

    public static void writeGyms(final List<CoachService> services) {

        //插入数据

        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            //删除所有行
            briteDatabase.execute(CoachService.DELETEALL);
            for (CoachService service : services) {
                //service.setAutoId(service.getId() + "_" + service.getModel());
                briteDatabase.insert(CoachService.TABLE_NAME, CoachService.FACTORY.marshal(service).asContentValues());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public static CoachService getGymNow(String gymid, String gymmodel) {
      SqlDelightStatement statement = CoachService.FACTORY.getByIdModel(gymid, gymmodel);
      Cursor cursor =
          dbOpenHelper.getReadableDatabase().rawQuery(statement.statement, statement.args);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        cursor.moveToFirst();
        CoachService coachService = CoachService.FACTORY.getByIdModelMapper().map(cursor);
        cursor.close();
        return coachService;
    }

    public static CoachService getShopNameById(String brandId, String shopId) {
      SqlDelightStatement statement = CoachService.FACTORY.getByBrandIdAndShops(shopId, brandId);
      Cursor cursor =
          dbOpenHelper.getReadableDatabase().rawQuery(statement.statement, statement.args);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        cursor.moveToFirst();
        CoachService coachService = CoachService.FACTORY.getByBrandIdAndShopIdMapper().map(cursor);
        cursor.close();
        return coachService;
    }

    public static Observable<List<CoachService>> getGymByModel(String id, String model) {
      return briteDatabase.createQuery(CoachService.TABLE_NAME,
          CoachService.FACTORY.getByIdModel(id, model).statement,
          CoachService.FACTORY.getByIdModel(id, model).args)
            .mapToList(new Func1<Cursor, CoachService>() {
                @Override public CoachService call(Cursor cursor) {
                    return CoachService.FACTORY.getByIdModelMapper().map(cursor);
                }
            })
            .first();
    }

  public static Observable<List<CoachService>> getGymByGymId(String gymid) {
    return briteDatabase.createQuery(CoachService.TABLE_NAME,
        CoachService.FACTORY.getByGymId(gymid).statement,
        CoachService.FACTORY.getByGymId(gymid).args).mapToList(new Func1<Cursor, CoachService>() {
      @Override public CoachService call(Cursor cursor) {
        return CoachService.FACTORY.getByIdModelMapper().map(cursor);
      }
    }).first();
  }

    public static Observable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM CoachService WHERE brand_id = " + brandid + " and ");
        if (shopid.size() > 0) stringBuffer.append("shop_id IN (");
        for (int i = 0; i < shopid.size(); i++) {
            stringBuffer.append("\'" + shopid.get(i) + "\'");
            if (i < shopid.size() - 1) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(");");
        return briteDatabase.createQuery(CoachService.TABLE_NAME, stringBuffer.toString())
            .mapToList(new Func1<Cursor, CoachService>() {
                @Override public CoachService call(Cursor cursor) {
                    return CoachService.FACTORY.getByBrandIdAndShopsMapper().map(cursor);
                }
            });
    }

    ///////////////////////////////////////

    public static void writePermiss(final List<Permission> permissions) {
        briteDatabase.execute(Permission.DELETEALLPERMISSION);
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            for (Permission permission : permissions) {
                briteDatabase.insert(Permission.TABLE_NAME, Permission.FACTORY.marshal(permission).asContentValues());
            }
            transaction.markSuccessful();
        } catch (Exception e) {
            Timber.e(e.getMessage());
        } finally {
            transaction.end();
        }
    }

    public static void delPermiss() {
        try {
            briteDatabase.execute(Permission.DELETEALLPERMISSION);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

    public static boolean check(String shopid, String key) {
      Cursor cursor = briteDatabase.getReadableDatabase()
          .rawQuery(Permission.FACTORY.getByShopIdAndKey(shopid, key).statement,
              Permission.FACTORY.getByShopIdAndKey(shopid, key).args);
        if (cursor == null || cursor.getCount() <= 0) {
            return false;
        }
        cursor.moveToFirst();
        Permission permission = Permission.MAPPER.map(cursor);
        return permission != null && permission.isValue();
    }

    public static boolean checkMuti(String key, @NonNull List<String> shopids) {
        for (int i = 0; i < shopids.size(); i++) {
          SqlDelightStatement statement = Permission.FACTORY.getByShopIdAndKey(shopids.get(i), key);
          Cursor cursor =
              briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
            if (cursor == null || cursor.getCount() <= 0) {
                return false;
            }
            cursor.moveToFirst();
            Permission permission = Permission.MAPPER.map(cursor);
          cursor.close();
            if (permission == null || !permission.isValue()) {
                return false;
            }

        }
        return true;
    }

    public static List<String> checkMutiTrue(String key, @NonNull List<String> shopids) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < shopids.size(); i++) {
          SqlDelightStatement statement =
              Permission.FACTORY.getByShopIdAndKeyValue(shopids.get(i), key, true);
          Cursor cursor =
              briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
            if (cursor == null || cursor.getCount() <= 0) {
                return ret;
            }
            cursor.moveToFirst();
            Permission permission = Permission.MAPPER.map(cursor);
            if (permission != null && permission.isValue()) {
                ret.add(shopids.get(i));
            }
        }
        return ret;
    }

    public static boolean checkAtLeastOne(String key) {
      SqlDelightStatement statement = Permission.FACTORY.getByKeyValue(key, true);
      Cursor cursor =
          briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
        return cursor != null && cursor.getCount() > 0;
    }

    public static boolean checkNoOne(String key) {
      SqlDelightStatement statement = Permission.FACTORY.getByKeyValue(key, true);
      Cursor cursor =
          briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
        return cursor == null || cursor.getCount() <= 0;
    }

    public static boolean checkAll(String key) {

      SqlDelightStatement statement = Permission.FACTORY.getByKey(key);
      Cursor cursor =
          briteDatabase.getReadableDatabase().rawQuery(statement.statement, statement.args);
        if (cursor == null || cursor.getCount() <= 0) {
            return false;
        }
        cursor.moveToFirst();
        do {
            Permission permission = Permission.MAPPER.map(cursor);
            if (permission == null || !permission.isValue()) return false;
        } while (cursor.moveToNext());
      cursor.close();
        return true;
    }

    public static Observable<List<String>> queryAllFunctions() {
        return briteDatabase.createQuery(MyFunctionModel.TABLE_NAME, "SELECT * FROM qc_function_module")
            .mapToList(new Func1<Cursor, String>() {
                @Override public String call(Cursor cursor) {
                    return cursor.getString(cursor.getColumnIndex("module_name"));
                }
            });
    }

    public static void insertFunction(List<String> module) {

        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            briteDatabase.delete(MyFunctionModel.TABLE_NAME, null);
            if (module != null) {
                for (int i = 0; i < module.size(); i++) {
                    String s = module.get(i);
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MyFunctionModel.MODULE_NAME, s);
                    briteDatabase.insert(MyFunctionModel.TABLE_NAME, contentValues, CONFLICT_REPLACE);
                }
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }
}
