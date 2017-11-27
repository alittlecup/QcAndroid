package cn.qingchengfit.saasbase.export.presenter;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.export.bean.ExportRecord;
import cn.qingchengfit.saasbase.export.network.response.ExportRecordWrapper;
import cn.qingchengfit.saasbase.repository.IExportModel;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImportExportPresenter extends BasePresenter<ImportExportPresenter.MVPView> {

  @Inject IExportModel exportModel;
  @Inject
  public ImportExportPresenter() {
  }


  public void qcGetExportRecord(){
    RxRegiste(exportModel
        .qcGetExportRecord()
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<ExportRecordWrapper>>() {
          @Override public void call(QcDataResponse<ExportRecordWrapper> recordWrapQcResponseData) {
            if (ResponseConstant.checkSuccess(recordWrapQcResponseData)){
              mvpView.onExportRecord(recordWrapQcResponseData.data.export_records);
            }else{
              mvpView.onShowError(recordWrapQcResponseData.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  //发送邮件
  public void qcSendEmail(String excel, String mail){
    HashMap<String, Object> params = new HashMap<>();
    if (!TextUtils.isEmpty(excel)) {
      params.put("excel_url", excel);
    }
    if (!TextUtils.isEmpty(mail)) {
      params.put("mail", mail);
    }

    RxRegiste(exportModel
        .qcSendMail(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)){
              mvpView.onSendEmailSuccess();
            }else{
              mvpView.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  //导出
  public void qcPostExport(String type){
    HashMap<String, Object> params = new HashMap<>();
    if (!TextUtils.isEmpty(type)) {
      params.put("type", type);
    }
    RxRegiste(exportModel
        .qcDataImport(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)){
              mvpView.onExportSuccess();
            }else{
              mvpView.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onExportRecord(List<ExportRecord> record);

    void onExportSuccess();

    void onSendEmailSuccess();
  }

}