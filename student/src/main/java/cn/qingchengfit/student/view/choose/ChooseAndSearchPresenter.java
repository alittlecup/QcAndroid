package cn.qingchengfit.student.view.choose;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.respository.IStudentModel;
import java.util.List;
import javax.inject.Inject;

public class ChooseAndSearchPresenter extends BasePresenter {
  @Inject IStudentModel studentModel;
  private MVPView view;

  @Inject public ChooseAndSearchPresenter() {
  }

  public void getAllStudents(String method) {
    RxRegiste(studentModel.getAllStudentNoPermission(method)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onStudentList(qcResponse.data.users);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }, throwable -> {
        }));
  }
  public void getCardBindStudents(String cardId){
    RxRegiste(studentModel.qcGetCardBundldStudents(cardId)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onStudentList(qcResponse.data.users);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }, throwable -> {
        }));
  }

  public void loadStudentByPhoneStart(String phone) {
    RxRegiste(studentModel.loadStudentsByPhone(phone)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onStudentList(qcResponse.data.users);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        },throwable ->{} ));
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
