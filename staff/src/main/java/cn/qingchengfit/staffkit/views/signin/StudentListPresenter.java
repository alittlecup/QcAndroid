package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class StudentListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentAction studentAction;
    private Subscription spFilter;
    private StaffRespository restRepository;
    private StudentListView view;

    @Inject public StudentListPresenter(StaffRespository restRepository) {
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
      HashMap<String, Object> params = gymWrapper.getShopParams();
      if (keyword != null) {
        params.put("q", keyword);
      }
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllStudents(staffid, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .map(new Func1<QcDataResponse<Students>, List<QcStudentBean>>() {
              @Override
              public List<QcStudentBean> call(QcDataResponse<Students> qcResponseAllStudent) {

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
                        studentAction.saveStudent(qcResponseAllStudent.data.users, gymWrapper.brand_id());
                      return qcResponseAllStudent.data.users;
                    } else {
                      return null;
                    }


                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(b -> {
              if (b == null) view.onFaied();
              else {
                handleData(b);
              }
              }, throwable -> Timber.e(throwable.getMessage()))

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
      queryStudent(App.staffId, gymWrapper.shop_id(), keyword);
      //妈的 这逻辑无敌了
        // 先找本地,没检索到的话,网络请求学员列表
      //if (spFilter != null) spFilter.unsubscribe();
      //spFilter = StudentAction.newInstance()
      //    .getStudentByKeyWord(gymWrapper.brand_id(), gymWrapper.shop_id(), keyword)
      //    .observeOn(AndroidSchedulers.mainThread())
      //    .subscribe(new Action1<List<QcStudentBean>>() {
      //        @Override public void call(List<QcStudentBean> qcStudentBeen) {
      //            if (qcStudentBeen.size() > 0) {
      //                handleData(qcStudentBeen);
      //            } else if (queryNet) {
      //
      //            } else {
      //                handleData(new ArrayList<QcStudentBean>());
      //            }
      //        }
      //    });
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
