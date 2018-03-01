package cn.qingchengfit.saasbase.mvvm_student;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.crypto.interfaces.PBEKey;
import javax.inject.Inject;

import cn.qingchengfit.model.base.Staff;

/**
 * Created by huangbaole on 2017/11/28.
 */

public class StudentActivityViewModel extends ViewModel {
    private final MutableLiveData<Integer> allotType = new MutableLiveData<>();
    private final MutableLiveData<Staff> allotStaff = new MutableLiveData<>();



    public MutableLiveData<Staff> getAllotStaff() {
        return allotStaff;
    }

    @Inject
    public StudentActivityViewModel() {
        allotType.setValue(0);
    }

    public String getStaffId() {
        if (allotStaff.getValue() != null) {
            return allotStaff.getValue().id;
        } else {
            return "";
        }
    }
    public Integer getAllotType(){
        if(allotType.getValue()!=null){
            return allotType.getValue();
        }
        return 0;
    }
    public void setAllotType(Integer type){
        allotType.setValue(type);
    }
}
