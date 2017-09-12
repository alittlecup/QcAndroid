package cn.qingchengfit.staffkit.views.export.presenter;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.views.export.model.ExportRecord;
import cn.qingchengfit.staffkit.views.export.model.ExportRecordWrapper;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/6.
 */

public class ImportExportPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;

  @Inject
  public ImportExportPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView)v;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null)
      view = null;
  }

  public void qcGetExportRecord(){
    RxRegiste(qcRestRepository.createGetApi(Get_Api.class)
        .qcGetExportRecord(App.staffId, gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponseData<ExportRecordWrapper>>() {
          @Override public void call(QcResponseData<ExportRecordWrapper> recordWrapQcResponseData) {
            if (ResponseConstant.checkSuccess(recordWrapQcResponseData)){
              view.onExportRecord(recordWrapQcResponseData.data.export_records);
            }else{
              view.onShowError(recordWrapQcResponseData.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  //发送邮件
  public void qcSendEmail(String excel, String mail){
    HashMap<String, Object> params = gymWrapper.getParams();
    if (!TextUtils.isEmpty(excel)) {
      params.put("excel_url", excel);
    }
    if (!TextUtils.isEmpty(mail)) {
      params.put("mail", mail);
    }

    RxRegiste(qcRestRepository.createPostApi(Post_Api.class)
        .qcSendMail(App.staffId, params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200){
              view.onSendEmailSuccess();
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  //导出
  public void qcPostExport(String type){
    HashMap<String, Object> params = gymWrapper.getParams();
    if (!TextUtils.isEmpty(type)) {
      params.put("type", type);
    }
    RxRegiste(qcRestRepository.createPostApi(Post_Api.class)
        .qcDataImport(App.staffId, params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200){
              view.onExportSuccess();
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView{
    void onExportRecord(List<ExportRecord> record);

    void onExportSuccess();

    void onSendEmailSuccess();
  }

}
