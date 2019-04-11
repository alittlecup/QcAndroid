package cn.qingchengfit.saasbase.course.course.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCoursePresenter extends BasePresenter<AddCoursePresenter.MVPview> {

  @Inject ICourseModel courseModel;
  @Inject GymWrapper gymWrapper;

  @Inject public AddCoursePresenter() {
  }

  @Override public void attachView(PView v) {
    super.attachView(v);
    if (gymWrapper.inBrand())
      mvpView.showSuitGym();
  }


  public void addCourse(CourseBody body) {
    RxRegiste(courseModel.qcCreateCourse(body)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcDataResponse) {
          if (ResponseConstant.checkSuccess(qcDataResponse)){
            mvpView.createSuccess("添加成功");
          }else {
            mvpView.onShowError(qcDataResponse.getMsg());
          }
        }
      }));
  }

  public void setSupportGyms() {

  }

  public interface MVPview extends CView {
    void showSuitGym();
    void createSuccess(String string);
  }
}
