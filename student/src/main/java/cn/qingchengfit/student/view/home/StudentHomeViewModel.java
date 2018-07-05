package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.usercase.FilterUserCase;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import cn.qingchengfit.utils.GymUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/5.
 */

public class StudentHomeViewModel extends FlexibleViewModel<List<QcStudentBean>, StudentItem, Map<String, ? extends Object>> {
    public final ObservableField<List<StudentItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public SortViewModel getSortViewModel() {
        return sortViewModel;
    }

    SortViewModel sortViewModel;
    @Inject
    LoginStatus loginStatus;
    @Inject StudentRepository respository;
    @Inject
    GymWrapper gymWrapper;

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    String shopid;

    @Inject FilterUserCase filterUserCase;

    @Inject
    public StudentHomeViewModel(GymWrapper gymWrapper) {
        this.gymWrapper = gymWrapper;
        initData();
        sortViewModel = new SortViewModel();
        sortViewModel.setListener(items::set);

    }

    private void initData() {
        Brand brand = new Brand("3");
        brand.setName("青橙健身.Feature3");
        CoachService coachService = new CoachService();
        gymWrapper.setBrand(brand);
        coachService.setName("安徒生花园feature2");
        coachService.setAddress("西城区什刹海街道地安门外大街7453号");
        coachService.setBrand_id("3");
        coachService.setBrand_id("青橙健身.Feature3");
        coachService.setColor("#F5A623");
        DistrictEntity districtEntity = new DistrictEntity();
        districtEntity.city = new CityBean("110100", "北京市");
        districtEntity.id = "110102";
        districtEntity.name = "西城区";
        coachService.setGd_district(districtEntity);
        coachService.setGym_id("172");
        coachService.has_permission = true;
        coachService.setHost("https://feature3.qingchengfit.cn");
        coachService.setId("10548");
        coachService.setModel("staff_gym");
        coachService.setPhone("010-52089816");
        coachService.setPhoto("http://zoneke-img.b0.upaiyun.com/60d25dd5afd2098562b192e38e4a75cf.jpeg!120x120");
        coachService.setPosition("管理员");
        coachService.setShop_id("1");
        coachService.setSystem_end("2043-04-02");
        gymWrapper.setCoachService(coachService);
    }

    @NonNull
    @Override
    protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, ? extends Object> map) {
        isLoading.set(true);
        HashMap<String, Object> params = GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand(), shopid);

//        if (!TextUtils.isEmpty(filter.status)) {
//            params.put("status_ids", filter.status);
//        }
//        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
//            params.put("start", filter.registerTimeStart);
//            params.put("end", filter.registerTimeEnd);
//        }
//        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);
//
//        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);
//
//        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        if (!map.isEmpty()) {
            params.putAll(map);
        }


        //return Transformations.map(respository.qcGetAllStudents(loginStatus.staff_id(), params), input -> input.users);
        return null;
    }

    @Override
    public void loadSource(@NonNull Map<String, ?> stringMap) {
        this.identifier.setValue(stringMap);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<QcStudentBean> studentBeans) {
        return studentBeans != null && !studentBeans.isEmpty();
    }

    public LiveData<List<FilterModel>> getTest() {
        return filterUserCase.getFilterModel();

    }

    /**
     * 刷新数据
     */
    public void refresh() {
        this.identifier.setValue(identifier.getValue());
    }

    /**
     * 浮动按钮点击
     */
    public void onFABClick() {

    }


    @Override
    protected List<StudentItem> map(@NonNull List<QcStudentBean> studentBeans) {
        return FlexibleItemProvider.with(new StudentItemFactory()).from(studentBeans);
    }

    public boolean checkPro() {
        return gymWrapper.isPro();
    }

    static class StudentItemFactory implements FlexibleItemProvider.Factory<QcStudentBean, StudentItem> {

        @NonNull
        @Override
        public StudentItem create(QcStudentBean qcStudentBean) {
            return FlexibleFactory.create(StudentItem.class, qcStudentBean);
        }
    }
}
