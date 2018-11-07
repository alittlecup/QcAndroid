package cn.qingchengfit.staffkit.views.allotsales;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.AllotSaleStudents;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.widgets.AlphabetView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/10/14.
 */
public class MultiModifyPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription spFilter;
    private StaffRespository restRepository;

    @Inject public MultiModifyPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
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

    /**
     * 添加名下会员页:有权限查看的会员列表接口(名下会员+无销售会员)
     *
     * @param salerId salerId
     */
    public void getSellerUsers(String salerId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("show_all", "1");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllotSaleUsersOfAddStudents(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllotSaleStudents>>() {
                @Override public void call(QcDataResponse<AllotSaleStudents> allotSaleStudentsQcResponseData) {
                    if (allotSaleStudentsQcResponseData.status == 200) {
                        handleData(allotSaleStudentsQcResponseData.data.users);
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

    public void queryStudent(StudentFilter filter) {

        HashMap<String, Object> params = gymWrapper.getParams();
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
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllotSaleOwenUsers(loginStatus.staff_id(), params)
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
                        //                            StudentAction.newInstance().saveStudent(qcResponseAllSaleStudent.data.users, brandid);
                        handleData(qcResponseAllSaleStudent.data.users);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            })

        );
    }

    private void handleData(List<Student> data) {
        view.onStopLoading();
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
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            bean.status = student.getStatus();
            bean.sellers = student.getSellers();
            studentBeanList.add(bean);
        }
        view.onStudentList(studentBeanList);
    }

    /**
     * 批量添加会员
     *
     * @param sellerId sellerId
     * @param list list
     */
    public void AddStudents(String sellerId, List<StudentBean> list) {
        HashMap<String, Object> params = gymWrapper.getParams();
        HashMap<String, Object> body = new HashMap<>();
        params.put("seller_id", sellerId);

        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i).id, ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i).id).toString();
            }
        }

        body.put("user_ids", ret);
        RxRegiste(restRepository.getStaffAllApi()
            .qcAddStudents(App.staffId, params, body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        view.onAddSuccess();
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

    /**
     * 批量移除会员
     *
     * @param sellerId sellerId
     * @param list list
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    public void removeStudents(String sellerId, List<StudentBean> list) {
        HashMap<String, Object> params = gymWrapper.getParams();
        HashMap<String, Object> body = new HashMap<>();
        params.put("seller_id", sellerId);

        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i).id, ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i).id).toString();
            }
        }

        params.put("user_ids", ret);
        RxRegiste(restRepository.getStaffAllApi()
            .qcDeleteStudents(App.staffId, params, body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        if (view != null) view.onRemoveSuccess();
                    } else {
                        if (view != null) view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.onShowError(throwable.getMessage());
                }
            }));
    }

    public interface PresenterView extends PView {
        void onStudentList(List<StudentBean> list);

        void onShowError(String e);

        void onAddSuccess();

        void onRemoveSuccess();

        void onStopLoading();
    }
}