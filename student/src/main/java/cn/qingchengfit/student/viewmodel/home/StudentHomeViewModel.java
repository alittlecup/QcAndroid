package cn.qingchengfit.student.viewmodel.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.student.common.flexble.FlexibleFactory;
import cn.qingchengfit.student.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.student.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.respository.StudentRespository;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import cn.qingchengfit.utils.GymUtils;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2017/12/5.
 */

public class StudentHomeViewModel extends FlexibleViewModel<List<QcStudentBean>, StudentItem, StudentFilter> {
    public final ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public SortViewModel getSortViewModel() {
        return sortViewModel;
    }

    SortViewModel sortViewModel;
    @Inject
    LoginStatus loginStatus;
    @Inject
    StudentRespository respository;
    @Inject
    GymWrapper gymWrapper;

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    String shopid;

    @Inject
    public StudentHomeViewModel(GymWrapper gymWrapper) {
        this.gymWrapper = gymWrapper;
        initData();
        sortViewModel = new SortViewModel();
        sortViewModel.setListener(items::set);
    }

    private void initData() {
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

    @NonNull
    @Override
    protected LiveData<List<QcStudentBean>> getSource(@NonNull StudentFilter filter) {
        isLoading.set(true);
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

        return Transformations.map(respository.qcGetAllStudents(loginStatus.staff_id(), params), input -> input.users);
    }

    @Override
    protected boolean isSourceValid(@Nullable List<QcStudentBean> studentBeans) {
        return studentBeans != null && !studentBeans.isEmpty();
    }

    /**
     * 刷新数据
     */
    public void refresh() {

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
