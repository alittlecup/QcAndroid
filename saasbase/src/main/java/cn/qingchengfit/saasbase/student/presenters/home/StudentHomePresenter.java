package cn.qingchengfit.saasbase.student.presenters.home;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.GymUtils;

/**
 * Created by huangbaole on 2017/10/26.
 */

public class StudentHomePresenter extends BasePresenter<StudentHomePresenter.MVPView> {
    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    QcRestRepository qcRestRepository;
    MVPView view;

    @Inject
    public StudentHomePresenter() {

    }


    @Override
    public void attachView(PView v) {
        super.attachView(v);
        this.view = (MVPView) v;
        Brand brand = new Brand("2");
        brand.setName("青橙健身.Feature2");
        CoachService coachService = new CoachService();
        gymWrapper.setBrand(brand);
        coachService.setName("安徒生花园feature2");
        coachService.setAddress("西城区什刹海街道地安门外大街7453号");
        coachService.setBrand_id("2");
        coachService.setBrand_id("青橙健身.Feature2");
        coachService.setColor("#F5A623");
        DistrictEntity districtEntity = new DistrictEntity();
        districtEntity.city = new CityBean("110100", "北京市");
        districtEntity.id = "110102";
        districtEntity.name = "西城区";
        coachService.setGd_district(districtEntity);
        coachService.setGym_id("172");
        coachService.has_permission = true;
        coachService.setHost("https://feature2.qingchengfit.cn");
        coachService.setId("10548");
        coachService.setModel("staff_gym");
        coachService.setPhone("010-52089816");
        coachService.setPhoto("http://zoneke-img.b0.upaiyun.com/60d25dd5afd2098562b192e38e4a75cf.jpeg!120x120");
        coachService.setPosition("管理员");
        coachService.setShop_id("1");
        coachService.setSystem_end("2043-04-02");
        gymWrapper.setCoachService(coachService);
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
                .qcGetAllStudents(staffid, params)
                .compose(RxHelper.schedulersTransformer())
                .doOnTerminate(() -> view.onStopFresh())
                .map(studentListWrapperQcDataResponse -> studentListWrapperQcDataResponse.getData().users)
                .subscribe(qcStudentBeen -> view.onStudentList(qcStudentBeen), throwable -> view.onShowError(throwable.getMessage()))
        );

    }

    public interface MVPView extends CView {
        void onStudentList(List<QcStudentBean> list);

        void onShowError(String e);

        void onStopFresh();
    }
}
