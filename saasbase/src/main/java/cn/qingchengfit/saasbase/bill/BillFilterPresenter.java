package cn.qingchengfit.saasbase.bill;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.saasbase.repository.IBillModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/12.
 */

public class BillFilterPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject IBillModel billModel;

  @Inject
  public BillFilterPresenter(){
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView)v;
  }

  public void getFilterList(){
    RxRegiste(billModel.getBillFilterList(gymWrapper.id())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .onBackpressureBuffer()
        .subscribe(new Action1<QcDataResponse<FilterWrapper>>() {
          @Override public void call(QcDataResponse<FilterWrapper> filterWrapperQcDataResponse) {
            if (ResponseConstant.checkSuccess(filterWrapperQcDataResponse)){
              if (view != null){
                view.onGetFilter(filterWrapperQcDataResponse.data.filters);
              }
            }else{
              view.onShowError(filterWrapperQcDataResponse.getMsg());
            }
          }
        }));
  }

  public interface MVPView extends CView {
    void onGetFilter(List<FilterModel> filters);
  }

}
