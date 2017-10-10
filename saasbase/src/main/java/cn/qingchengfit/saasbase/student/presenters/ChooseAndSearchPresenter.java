package cn.qingchengfit.saasbase.student.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseAndSearchPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject IStudentModel studentModel;
  private MVPView view;

  @Inject public ChooseAndSearchPresenter() {
  }

  public void getAllStudents() {
    RxRegiste(studentModel.getAllStudentNoPermission()
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<StudentListWrapper>>() {
          @Override public void onNext(QcDataResponse<StudentListWrapper> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onStudentList(qcResponse.data.users);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));

  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onStudentList(List<QcStudentBean> stus);
  }
}
