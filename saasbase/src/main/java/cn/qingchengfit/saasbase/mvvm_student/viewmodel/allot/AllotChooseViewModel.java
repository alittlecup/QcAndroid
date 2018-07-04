package cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRespository;

/**
 * Created by huangbaole on 2017/12/1.
 */

public class AllotChooseViewModel extends BaseViewModel {

    private MutableLiveData<List<String>> staffIds = new MutableLiveData<>();
    private MutableLiveData<List<String>> salserStaffids = new MutableLiveData<>();

    private final LiveData<List<Staff>> staffs;
    private final LiveData<Boolean> responseSuccess;


    public LiveData<List<Staff>> getStaffs() {
        return staffs;
    }

    public LiveData<Boolean> getResponseSuccess() {
        return responseSuccess;
    }



    public LiveData<List<Staff>> getSalerStaffs() {
        return salerStaffs;
    }

    public LiveData<Boolean> getSalerResponse() {
        return salerResponse;
    }

    private final LiveData<List<Staff>> salerStaffs;
    private final LiveData<Boolean> salerResponse;

    LoginStatus loginStatus;
    GymWrapper gymWrapper;
    StudentRespository respository;

    List<String> students;

    public void setCurId(String curId) {
        this.curId = curId;
    }

    String curId;

    public void setStudents(List<String> students) {
        this.students = students;
    }

    @Inject
    public AllotChooseViewModel(LoginStatus loginStatus, GymWrapper gymWrapper,StudentRespository studentRespository) {
        this.loginStatus = loginStatus;
        this.gymWrapper = gymWrapper;
        this.respository=studentRespository;

        staffs = Transformations.map(loadData(), input -> input.teachers);
        responseSuccess = Transformations.switchMap(staffIds, this::allotCoachRemote);

        salerStaffs = Transformations.map(loadSalerData(), input -> input.users);
        salerResponse = Transformations.switchMap(salserStaffids, this::allotSalerRemote);
    }

    public LiveData<SalerTeachersListWrap> loadData() {
        return respository.qcGetAllAllocateCoaches(loginStatus.staff_id(), gymWrapper.getParams());
    }

    public LiveData<SalerUserListWrap> loadSalerData() {
        return respository.qcGetSalers(loginStatus.staff_id(), null, null, gymWrapper.id(), gymWrapper.model());
    }

    public void allotCoach(List<String> strings) {
        this.staffIds.setValue(strings);
    }

    public void allotSaler(List<String> strings) {
        this.salserStaffids.setValue(strings);
    }

    private LiveData<Boolean> allotCoachRemote(List<String> ids) {
        HashMap<String, Object> body = gymWrapper.getParams();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("coach_ids", StringUtils.List2Str(ids));
        return respository.qcAllocateCoach(loginStatus.staff_id(), body);
    }

    private LiveData<Boolean> allotSalerRemote(List<String> ids) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("seller_ids", StringUtils.List2Str(ids));
        body.put("seller_id", curId);
        return respository.qcModifySellers(loginStatus.staff_id(), gymWrapper.getParams(), body);
    }


}
