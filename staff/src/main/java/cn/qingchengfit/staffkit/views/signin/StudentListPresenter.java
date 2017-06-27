package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class StudentListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription spFilter;
    private RestRepository restRepository;
    private StudentListView view;

    @Inject public StudentListPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (StudentListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryStudent(String staffid, String shopid, final String keyword) {
        RxRegiste(restRepository.getGet_api()
            .qcGetAllStudents(staffid, gymWrapper.getShopParams())
            .subscribeOn(Schedulers.io())
            .map(new Func1<QcResponseData<Students>, Boolean>() {
                @Override public Boolean call(QcResponseData<Students> qcResponseAllStudent) {

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
                    }

                    return true;
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean b) {
                    if (b) {
                        filter(keyword, false);
                    } else {
                        view.onFaied();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            })

        );
    }

    private void handleData(List<QcStudentBean> data) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.setId(student.getId());
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.checkin_avatar = student.getCheckin_avatar();
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

    /**
     * @param queryNet 没检索到时是否去网络获取列表
     */
    public void filter(final String keyword, final boolean queryNet) {
        // 先找本地,没检索到的话,网络请求学员列表
        if (spFilter != null) spFilter.unsubscribe();
        spFilter = StudentAction.newInstance()
            .getStudentByKeyWord(gymWrapper.brand_id(), gymWrapper.shop_id(), keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> qcStudentBeen) {
                    if (qcStudentBeen.size() > 0) {
                        handleData(qcStudentBeen);
                    } else if (queryNet) {
                        queryStudent(App.staffId, gymWrapper.shop_id(), keyword);
                    } else {
                        handleData(new ArrayList<QcStudentBean>());
                    }
                }
            });
    }

    /**
     * Created by yangming on 16/8/30.
     */
    public interface StudentListView extends PView {
        void onStudentList(List<StudentBean> list);

        void onFilterStudentList(List<StudentBean> list);

        void onFaied();
    }
}
