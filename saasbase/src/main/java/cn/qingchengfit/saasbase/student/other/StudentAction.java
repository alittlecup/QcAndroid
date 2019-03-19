package cn.qingchengfit.saasbase.student.other;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.model.GymBaseInfoAction;
import cn.qingchengfit.saascommon.model.QcDbManager;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
    @Inject QcDbManager qcDbManager;
    @Inject GymBaseInfoAction gymBaseInfoAction;

    @Inject
    public StudentAction() {
    }


    public void saveStudent(final List<QcStudentBean> studentBeens, final String brandid) {
        qcDbManager.delByBrandId(brandid);
        qcDbManager.saveStudent(studentBeens, brandid);
    }

    public void updateStudentCheckin(String id, String avatar) {
        qcDbManager.updateStudentCheckin(avatar, id);
    }

    public void saveStudent(final QcStudentBean studentBeens) {

        qcDbManager.saveStudent(studentBeens);
    }

    public Flowable<QcStudentBean> getStudentById(String id) {
        return qcDbManager.getStudentById(id);
    }

    public Flowable<List<QcStudentBean>> getAllStudent() {
        return qcDbManager.getAllStudent();
    }

    public Flowable<List<QcStudentBean>> getStudentByBrand(String brandid) {
        return qcDbManager.getStudentByBrand(brandid);
    }

    public Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword, String brandid) {
        return qcDbManager.getStudentByKeyWord(keyword, brandid);
    }

    public Maybe<List<QcStudentBean>> getStudentByKeyWord(final String keyword) {

        return qcDbManager.getStudentByKeyWord(keyword);
    }

    public Maybe<List<QcStudentBean>> getStudentByKeyWord(String brandid, String shopid, String keyword) {

        return qcDbManager.getStudentByKeyWordShop(brandid, shopid, keyword);
    }

    public Flowable<List<QcStudentBean>> getStudentByGym(String gymid, String gymModel) {
        String shopid = gymBaseInfoAction.getShopId(gymid, gymModel);
        if (shopid == null) {
            List<QcStudentBean> emp = new ArrayList<>();
            return Flowable.just(emp);
        }
        return qcDbManager.getStudentByGym(shopid);
    }

    public void delStudentByBrand(String brandid, final String id) {
        qcDbManager.delStudentByBrand(brandid, id);
    }

    public void delAllStudent() {
        qcDbManager.delAllStudent();
    }

    public void delStudentByGym(final String gymid, final String gymmodel, final String id) {
        qcDbManager.delStudentByGym(gymid, gymmodel, id);
    }
}
