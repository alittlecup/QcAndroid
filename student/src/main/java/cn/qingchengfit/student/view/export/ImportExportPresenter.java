package cn.qingchengfit.student.view.export;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.respository.IStudentModel;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import javax.inject.Inject;

public class ImportExportPresenter extends BasePresenter<ImportExportPresenter.MVPView> {

  @Inject IStudentModel studentModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public ImportExportPresenter() {
  }

  //导出
  public void qcPostExport(String type) {
    HashMap<String, Object> params = new HashMap<>();
    if (!TextUtils.isEmpty(type)) {
      params.put("type", type);
    }
    params.putAll(gymWrapper.getParams());
    RxRegiste(studentModel.qcDataImport(loginStatus.staff_id(), params)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(new Consumer<QcDataResponse>() {
          @Override public void accept(QcDataResponse qcDataResponse) {
            if (ResponseConstant.checkSuccess(qcDataResponse)) {
              mvpView.onExportSuccess();
            } else {
              mvpView.onShowError(qcDataResponse.getMsg());
            }
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) {
            mvpView.onShowError(throwable.getMessage());
          }
        }));
  }

  public interface MVPView extends CView {

    void onExportSuccess();

  }
}