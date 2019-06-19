package cn.qingchengfit.student.view.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class ClassRecordTempVM extends BaseViewModel {
  @Inject StudentRepository studentRepository;


  @Inject public ClassRecordTempVM() {
  }

  public LiveData<ClassRecords> loadParams(String id, Map<String, Object> params) {
    return Transformations.map(studentRepository.qcGetStudentClassRecords(id, new HashMap<>(params)),
            input -> {
              ClassRecords classRecords = dealResource(input);
              return classRecords;
            });


  }
}
