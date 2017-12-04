package cn.qingchengfit.student;

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

    public MutableLiveData<Integer> getAllotType() {
        return allotType;
    }

    public MutableLiveData<Staff> getAllotStaff() {
        return allotStaff;
    }

    @Inject
    public StudentActivityViewModel() {
        allotType.setValue(0);
    }
}
