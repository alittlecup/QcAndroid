package cn.qingchengfit.saasbase.student.presenters.home;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.utils.GymUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/14 2016.
 */
public class StudentListPresenter extends BasePresenter {

    @Inject
    IStudentModel studentModel;
    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
//    @Inject
//    StudentAction studentAction;
    private Subscription spQuery;
    private Subscription spQueryNet;
    private Subscription spFilter;
    private int querytimes = 0;
    private Subscription spSave;
    private MVPView view;

    @Inject
    public StudentListPresenter() {

    }

    @Override
    public void attachView(PView v) {
        view = (MVPView) v;
    }


    @Override
    public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryStudentPermission(String staffid, String shopid) {
        HashMap<String, Object> params = GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand(), shopid);
        params.put("key", PermissionServerUtils.MANAGE_COSTS);
        params.put("method", "post");
        RxRegiste(studentModel
                .qcGetCardBundldStudents(staffid, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<QcDataResponse<StudentListWrapper>, Boolean>() {
                    @Override
                    public Boolean call(QcDataResponse<StudentListWrapper> qcResponseAllStudent) {

                        if (ResponseConstant.checkSuccess(qcResponseAllStudent)) {
                            for (QcStudentBean bean : qcResponseAllStudent.data.users) {
                                bean.setBrand_id(gymWrapper.brand_id());
                                if (bean.getShops() != null && bean.getShops().size() > 0) {
                                    String support = "";
                                    String support_ids = "";
                                    for (int i = 0; i < bean.getShops().size(); i++) {
                                        Shop shop = bean.getShops().get(i);
                                        support = TextUtils.concat(support, shop.name).toString();
                                        support_ids = TextUtils.concat(support_ids, shop.id).toString();
                                        if (i != bean.getShops().size() - 1) {
                                            support = TextUtils.concat(support, "，").toString();
                                            support_ids = TextUtils.concat(support_ids, "，").toString();
                                        }
                                    }
                                    bean.setSupoort_gym_ids(support_ids);
                                    bean.setSupport_gym(support);
                                }
                            }
                            //StudentAction.newInstance().saveStudent(qcResponseAllStudent.data.users, gymWrapper.brand_id());
                            handleData(qcResponseAllStudent.data.users, null, gymWrapper.id(), gymWrapper.model());
                        }
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {
                        if (b) {
                            view.onStopFresh();
                        } else {
                            view.onFaied();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onFaied();
                    }
                })

        );
    }

    public void queryStudent(String staffid, String shopid, StudentFilter filter) {

        HashMap<String, Object> params = GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand(), shopid);
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status_ids", filter.status);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);

        RxRegiste(studentModel
                        .qcGetAllStudents(staffid, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .flatMap(new Func1<QcDataResponse<StudentListWrapper>, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(QcDataResponse<StudentListWrapper> qcResponseAllStudent) {
                                if (ResponseConstant.checkSuccess(qcResponseAllStudent)) {
                                    for (QcStudentBean bean : qcResponseAllStudent.data.users) {
                                        bean.setBrand_id(gymWrapper.brand_id());
                                        if (bean.getShops() != null && bean.getShops().size() > 0) {
                                            String support = "";
                                            String support_ids = "";
                                            for (int i = 0; i < bean.getShops().size(); i++) {
                                                Shop shop = bean.getShops().get(i);
                                                support = TextUtils.concat(support, shop.name).toString();
                                                support_ids = TextUtils.concat(support_ids, shop.id).toString();
                                                if (i != bean.getShops().size() - 1) {
                                                    support = TextUtils.concat(support, "，").toString();
                                                    support_ids = TextUtils.concat(support_ids, ",").toString();
                                                }
                                            }
                                            bean.setSupoort_gym_ids(support_ids);
                                            bean.setSupport_gym(support);
                                        }
                                    }
//                                    studentAction.saveStudent(qcResponseAllStudent.data.users, gymWrapper.brand_id());
                                }

                                return Observable.just(true)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io());
                            }
                        })
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean b) {
                                if (b) {
                                    view.onStopFresh();
                                } else {
                                    view.onFaied();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
//                        Timber.e(throwable.getMessage());
                            }
                        })

        );
    }

    public void subsribeDb() {
//        if (gymWrapper.inBrand()) {//连锁运营
//            RxRegiste(studentAction
//                    .getStudentByBrand(gymWrapper.brand_id())
//                    .throttleLast(500, TimeUnit.MILLISECONDS)
//                    .onBackpressureBuffer()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<List<QcStudentBean>>() {
//                        @Override
//                        public void call(List<QcStudentBean> qcStudentBeen) {
//                            handleData(qcStudentBeen, gymWrapper.brand_id(), null, null);
//                        }
//                    }));
//        } else {//场馆
//            RxRegiste(studentAction
//                    .getStudentByGym(gymWrapper.id(), gymWrapper.model())
//                    .throttleLast(500, TimeUnit.MILLISECONDS)
//                    .onBackpressureBuffer()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<List<QcStudentBean>>() {
//                        @Override
//                        public void call(List<QcStudentBean> qcStudentBeen) {
//                            handleData(qcStudentBeen, gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model());
//                        }
//                    }));
//        }
    }

    private void handleData(List<QcStudentBean> data, String brand_id, String gymid, String gymmodel) {
//        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();
//
//        for (QcStudentBean student : data) {
//            StudentBean bean = student.toStudentBean(brand_id, gymid, gymmodel);
//            studentBeanList.add(bean);
//        }
//        if (TextUtils.isEmpty(brand_id) && TextUtils.isEmpty(gymid) && TextUtils.isEmpty(gymmodel)) {
//            if (view != null) view.onFilterStudentList(studentBeanList);
//        } else {
//            if (view != null) view.onStudentList(studentBeanList);
//        }
        view.onStudentList(data);
    }

    public void filter(String keyword) {
//        if (spFilter != null) spFilter.unsubscribe();
//        spFilter = studentAction
//                .getStudentByKeyWord(keyword)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<QcStudentBean>>() {
//                    @Override
//                    public void call(List<QcStudentBean> qcStudentBeen) {
//                        handleData(qcStudentBeen, "", "", "");
//                    }
//                });
    }

    public interface MVPView extends PView {
        void onStudentList(List<QcStudentBean> list);

//        void onFilterStudentList(List<QcStudentBean> list);

        void onFaied();

        void onStopFresh();
    }
}
