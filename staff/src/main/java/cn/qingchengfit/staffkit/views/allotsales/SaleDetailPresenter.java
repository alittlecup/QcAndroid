package cn.qingchengfit.staffkit.views.allotsales;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.AllotSaleStudents;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.widgets.AlphabetView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/10/11.
 */
public class SaleDetailPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;

    @Inject public SaleDetailPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getSellerUsers(String salerId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(salerId)) params.put("seller_id", salerId);
        RxRegiste(restRepository.getGet_api()
            .qcGetAllotSaleOwenUsers(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllotSaleStudents>>() {
                @Override public void call(QcDataResponse<AllotSaleStudents> allotSaleStudentsQcResponseData) {
                    if (allotSaleStudentsQcResponseData.status == 200) {
                        handleData(allotSaleStudentsQcResponseData.data.users, true);
                    } else {
                        view.onShowError(allotSaleStudentsQcResponseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void removeStudent(CoachService coachService, String salerId, String studId, final int posotion) {
        HashMap<String, Object> params = GymUtils.getParams(coachService, null);
        params.put("user_id", studId);
        params.put("seller_id", salerId);
        RxRegiste(restRepository.getPost_api()
            .qcDeleteStudent(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        view.onRemoveSucess(posotion);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void queryStudent(String staffid, StudentFilter filter, String salerId) {

        HashMap<String, Object> params = gymWrapper.getParams();

        if (!TextUtils.isEmpty(salerId)) {
            params.put("seller_id", salerId);
        }
        params.put("show_all", 1);
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status", filter.status);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        RxRegiste(restRepository.getGet_api()
            .qcGetAllotSaleOwenUsers(staffid, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllotSaleStudents>>() {
                @Override public void call(QcDataResponse<AllotSaleStudents> qcResponseAllSaleStudent) {

                    if (ResponseConstant.checkSuccess(qcResponseAllSaleStudent)) {
                        for (Student bean : qcResponseAllSaleStudent.data.users) {
                            bean.setBrand_id(gymWrapper.brand_id());
                            if (bean.getShops() != null && bean.getShops().size() > 0) {
                                String support = "";
                                String support_ids = "";
                                for (int i = 0; i < bean.getShops().size(); i++) {
                                    QcScheduleBean.Shop shop = bean.getShops().get(i);
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
                        //                            StudentAction.newInstance().saveStudent(qcResponseAllSaleStudent.data.users, brandid);
                        handleData(qcResponseAllSaleStudent.data.users, false);
                        view.stopRefresh();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            })

        );
    }

    private void handleData(List<Student> data, boolean isOrigin) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (Student student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();
            bean.joined_at = student.getJoin_at();

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
            sb.append("手机：").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            bean.status = student.getStatus();
            bean.sellers = student.getSellers();

            String support_ids = TextUtils.concat("", gymWrapper.shop_id()).toString();
            bean.setSupport_shop_ids(support_ids);
            studentBeanList.add(bean);
        }
        if (isOrigin) {
            view.onGetOrigin(studentBeanList);
        }
        view.onStudentList(studentBeanList);
    }

    public interface PresenterView extends PView {
        void onStudentList(List<StudentBean> list);

        void onShowError(String e);

        void onRemoveSucess(int position);

        void stopRefresh();

        void onGetOrigin(List<StudentBean> originList);
    }
}