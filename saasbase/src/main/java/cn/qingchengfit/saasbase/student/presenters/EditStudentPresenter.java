package cn.qingchengfit.saasbase.student.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.SaasModel;
import cn.qingchengfit.saasbase.student.network.body.EditStudentBody;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditStudentPresenter extends BasePresenter {
  QcStudentBean qcStudentBean;
  @Inject GymWrapper gymWrapper;
  @Inject SaasModel saasModel;
  private MVPView view;

  @Inject public EditStudentPresenter() {
  }

  public QcStudentBean getQcStudent() {
    if (qcStudentBean != null) {
      return qcStudentBean;
    } else {
      return new QcStudentBean.Builder().build();
    }
  }



  /**
   * 保存状态
   */
  public void saveInfo() {
    view.showLoading();
    RxRegiste(saasModel.qcUpdateStudent(qcStudentBean.id(),
        EditStudentBody.instanteFromeQcStudent(qcStudentBean))
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.hideLoading();
              view.onSaveOK();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onSaveOK();
  }
}
