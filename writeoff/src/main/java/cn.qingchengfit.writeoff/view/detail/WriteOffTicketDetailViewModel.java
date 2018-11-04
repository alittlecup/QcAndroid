package cn.qingchengfit.writeoff.view.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.writeoff.network.ITicketModel;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketWrapper;
import javax.inject.Inject;

public class WriteOffTicketDetailViewModel extends BaseViewModel {
  public MutableLiveData<Ticket> ticketMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
  @Inject IWriteOffRepository repository;
  @Inject ITicketModel model;

  @Inject public WriteOffTicketDetailViewModel() {
  }

  public void loadTicketDetail(String number) {
    showLoading.setValue(true);
    model.qcGetTicketDetail(number)
        .compose(RxHelper.schedulersTransformerFlow())
        .doOnTerminate(()->showLoading.setValue(false))
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            ticketMutableLiveData.setValue(response.data.eticket);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
          ToastUtils.show(throwable.getMessage());
        });
  }
}
