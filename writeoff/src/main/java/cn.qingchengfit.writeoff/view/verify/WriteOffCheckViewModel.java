package cn.qingchengfit.writeoff.view.verify;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.writeoff.network.ITicketModel;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

public class WriteOffCheckViewModel extends BaseViewModel {
  @Inject IWriteOffRepository repository;
  @Inject ITicketModel ticketModel;
  MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
  public MutableLiveData<Ticket> ticketMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<TicketPostBody> ticketPostBody = new MutableLiveData<>();
  public MutableLiveData<Boolean> ticketVerifyResult=new MutableLiveData<>();

  @Inject public WriteOffCheckViewModel() {
    ticketPostBody.setValue(new TicketPostBody());
  }

  public void queryTicket(String number) {
    showLoading.setValue(true);
    ticketModel.qcQueryTicket(number)
        .compose(RxHelper.schedulersTransformerFlow())
        .doOnTerminate(()->showLoading.setValue(false))
        .subscribe(ticketQcDataResponse -> {
          if (ticketQcDataResponse.status == 200 && ticketQcDataResponse.getData() != null) {
            ticketMutableLiveData.setValue(ticketQcDataResponse.data.eticket);
          }else {
            ToastUtils.show(ticketQcDataResponse.getMsg());
          }
        },throwable -> {
          ToastUtils.show(throwable.getMessage());
        });

  }
  public void postTicketBody(){
    showLoading.setValue(true);
    ticketModel.qcVerifyTicket(ticketPostBody.getValue())
        .compose(RxHelper.schedulersTransformerFlow())
        .doOnTerminate(()->showLoading.setValue(false))
        .subscribe(response -> {
          if (response.status == 200 && response.getData() .isSuccessful()) {
            ticketVerifyResult.setValue(true);
          }else {
            ToastUtils.show(response.getMsg());
            ticketVerifyResult.setValue(false);
          }
        },throwable -> {
          ticketVerifyResult.setValue(false);
          ToastUtils.show(throwable.getMessage());
        });
  }
}
