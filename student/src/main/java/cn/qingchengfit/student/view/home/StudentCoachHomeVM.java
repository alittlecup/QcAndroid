package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.CoachStudentOverview;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.LogUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class StudentCoachHomeVM extends BaseViewModel {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject StudentRepository repository;

  @Inject public StudentCoachHomeVM(){}

  public MutableLiveData<CoachStudentOverview> overview = new MutableLiveData<>();

  private Disposable disposable;

  public void getStudentData(){
    disposable = repository.qcGetCoachStudentOverview(loginStatus.staff_id(), gymWrapper.getParams()).observeOn(
        AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
        coachStudentOverviewQcDataResponse -> {
          if (coachStudentOverviewQcDataResponse.data != null){
            overview.setValue(coachStudentOverviewQcDataResponse.data);
          }
        }, throwable -> LogUtil.e(throwable.getMessage()));
  }

  @Override protected void onCleared() {
    super.onCleared();
    if (!disposable.isDisposed())
      disposable.dispose();
  }
}
