package cn.qingchengfit.staffkit.model.dbaction;

import android.content.Context;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/16 2016.
 */
public class StudentAction {

    public static StudentAction instance;
    Context context;

    public StudentAction(Context context) {
        this.context = context;
    }

    public static StudentAction newInstance() {
        if (instance == null) {
            instance = new StudentAction(App.context);
        }
        return instance;
    }

    public Observable<QcStudentBean> getByPhone(final String phone) {
        return QCDbManager.getStudentByPhone(phone);
    }

    public void saveStudent(final List<QcStudentBean> studentBeens, final String brandid) {
        QCDbManager.delByBrandId(brandid);
        QCDbManager.saveStudent(studentBeens, brandid);
    }

    public void updateStudentCheckin(String id, String avatar) {
        QCDbManager.updateStudentCheckin(avatar, id);
    }

    public void saveStudent(final QcStudentBean studentBeens) {

        QCDbManager.saveStudent(studentBeens);
    }

    public Observable<QcStudentBean> getStudentById(String id) {
        return QCDbManager.getStudentById(id);
    }

    public Observable<List<QcStudentBean>> getAllStudent() {
        return QCDbManager.getAllStudent();
    }

    public Observable<List<QcStudentBean>> getStudentByBrand(String brandid) {
        return QCDbManager.getStudentByBrand(brandid);
    }

    public Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid) {
        return QCDbManager.getStudentByKeyWord(keyword, brandid);
    }

    public Observable<List<QcStudentBean>> getStudentByKeyWord(final String keyword) {

        return QCDbManager.getStudentByKeyWord(keyword);
    }

    public Observable<List<QcStudentBean>> getStudentByKeyWord(String brandid, String shopid, String keyword) {

        return QCDbManager.getStudentByKeyWordShop(brandid, shopid, keyword);
    }

    public Observable<List<QcStudentBean>> getStudentByGym(String gymid, String gymModel) {
        String shopid = GymBaseInfoAction.getShopId(gymid, gymModel);
        if (shopid == null) {
            List<QcStudentBean> emp = new ArrayList<>();
            return Observable.just(emp);
        }
        return QCDbManager.getStudentByGym(shopid);
    }

    public void delStudentByBrand(String brandid, final String id) {
        QCDbManager.delStudentByBrand(brandid, id);
    }

    public void delAllStudent() {
        QCDbManager.delAllStudent();
    }

    public void delStudentByGym(final String gymid, final String gymmodel, final String id) {
        QCDbManager.delStudentByGym(gymid, gymmodel, id);
    }
}
