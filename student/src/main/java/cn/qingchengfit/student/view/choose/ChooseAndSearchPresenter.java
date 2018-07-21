package cn.qingchengfit.student.view.choose;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseAndSearchPresenter extends BasePresenter {
  @Inject IStudentModel studentModel;
  private MVPView view;

  @Inject public ChooseAndSearchPresenter() {
  }

  public void getAllStudents() {
    RxRegiste(studentModel.getAllStudentNoPermission()
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
