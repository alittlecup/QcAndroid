package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.ToastUtils;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

public class TurnoverOrderDetailVM extends BaseViewModel {
  @Inject IStaffModel staffModel;

  public LiveData<TurOrderListData> getTurDetail() {
    return turDetail;
  }

  private LiveData<TurOrderListData> turDetail;

  public LiveData<List<TurOrderSellerHistory>> getOrderList() {
    return orderList;
  }

  private LiveData<List<TurOrderSellerHistory>> orderList;
  private MutableLiveData<String> turId = new MutableLiveData<>();
  public MutableLiveData<Staff> staffMutableLiveData = new MutableLiveData<>();

  @Inject public TurnoverOrderDetailVM() {
    turDetail = Transformations.switchMap(turId,
        input -> Transformations.map(loadTurnoveDetail(input),
            response -> dealResource(response) == null ? null
                : dealResource(response).shop_turnover));

    orderList = Transformations.switchMap(turId,
        input -> Transformations.map(loadTurOrderList(input),
            response -> dealResource(response) == null ? null
                : dealResource(response).shop_turnover_seller_historys));
    RxBus.getBus()
        .register(Staff.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
           putTurnoverSellerId(turId.getValue(),staff.getId());
          }
        });
  }

  private LiveData<Resource<TurOrderListDataWrapper>> loadTurnoveDetail(String turnover_id) {
    return LiveDataTransfer.fromPublisher(
        RxJavaInterop.toV2Flowable(staffModel.qcGetTurnoverOrderDetail(turnover_id))
            .compose(RxHelper.schedulersTransformerFlow()));
  }


  private LiveData<Resource<TurOrderSellerHistoryWrapper>> loadTurOrderList(String turnover_id) {
    return LiveDataTransfer.fromPublisher(
        RxJavaInterop.toV2Flowable(staffModel.qcGetOrderHistorty(turnover_id))
            .compose(RxHelper.schedulersTransformerFlow()));
  }


  public void loadTurDetail(String id) {
    turId.setValue(id);
  }

  private void putTurnoverSellerId(String turId, String sellerID) {
    staffModel.qcPutTurnoverOrderDetail(turId, sellerID)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if(ResponseConstant.checkSuccess(response)){
            loadTurDetail(turId);
          }else{
            ToastUtils.show("修改业绩归属失败");
          }
        }, throwable -> {
          ToastUtils.show("修改业绩归属失败");
        });
  }

}
