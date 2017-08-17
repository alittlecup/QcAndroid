package cn.qingchengfit.staffkit.views.student;

import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.widgets.AlphabetView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MutiChooseStudentPresenterPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;
    private Subscription spFilter;

    @Inject public MutiChooseStudentPresenterPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void unattachView() {
        super.unattachView();
        if (spFilter != null) spFilter.unsubscribe();
    }

    private void handleData(List<QcStudentBean> data, String brand_id, String gymid, String gymmodel) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            bean.brandid = brand_id;
            bean.modelid = gymid;
            bean.model = gymmodel;
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        if (view != null) view.onStudentList(studentBeanList);
    }

    public void queryStudent(String staffid, String shopid, String method) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.LOCKER_SETTING);
        params.put("method", method);
        RxRegiste(restRepository.getGet_api()
            .qcGetCardBundldStudents(staffid, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .map(new Func1<QcResponseData<Students>, List<QcStudentBean>>() {
                @Override public List<QcStudentBean> call(QcResponseData<Students> qcResponseAllStudent) {

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
                                        support = TextUtils.concat(support, ",").toString();
                                        support_ids = TextUtils.concat(support_ids, ",").toString();
                                    }
                                }
                                bean.setSupoort_gym_ids(support_ids);
                                bean.setSupport_gym(support);
                            }
                        }
                        StudentAction.newInstance().saveStudent(qcResponseAllStudent.data.users, gymWrapper.brand_id());
                        return qcResponseAllStudent.data.users;
                    } else {
                        return null;
                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> studens) {
                    if (studens != null) {
                        handleData(studens, gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model());
                        view.onStopFresh();
                    } else {
                        view.onShowError("");
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            })

        );
    }

    public void subsribeDb() {
        if (gymWrapper.inBrand()) {//连锁运营
            RxRegiste(StudentAction.newInstance()
                .getStudentByBrand(gymWrapper.brand_id())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<QcStudentBean>>() {
                    @Override public void call(List<QcStudentBean> qcStudentBeen) {
                        handleData(qcStudentBeen, gymWrapper.brand_id(), null, null);
                    }
                }));
        } else {//场馆
            RxRegiste(StudentAction.newInstance()
                .getStudentByGym(gymWrapper.id(), gymWrapper.model())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<QcStudentBean>>() {
                    @Override public void call(List<QcStudentBean> qcStudentBeen) {
                        handleData(qcStudentBeen, gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model());
                    }
                }));
        }
    }

    public void filter(String keyword) {
        spFilter = StudentAction.newInstance()
            .getStudentByKeyWord(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> qcStudentBeen) {
                    handleFilter(qcStudentBeen, "", "", "");
                }
            })

        ;
    }

    private void handleFilter(List<QcStudentBean> data, String brand_id, String gymid, String gymmodel) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            bean.brandid = brand_id;
            bean.modelid = gymid;
            bean.model = gymmodel;
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        if (view != null) view.onFilterStudentList(studentBeanList);
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onStudentList(List<StudentBean> studentBeen);

        void onFilterStudentList(List<StudentBean> studentBeen);

        void onStopFresh();
    }
}