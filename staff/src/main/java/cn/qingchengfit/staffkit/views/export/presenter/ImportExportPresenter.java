package cn.qingchengfit.staffkit.views.export.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponseData;
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

  MVPView view;
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
        .qcGetExportRecord(gymWrapper.shop_id())
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

  public void qcPostExport(String type){
    HashMap<String, Object> params = new HashMap<>();
    params.put("type", type);
    RxRegiste(qcRestRepository.createPostApi(Post_Api.class)
        .qcDataImport(gymWrapper.shop_id(), params)
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
  }

}
