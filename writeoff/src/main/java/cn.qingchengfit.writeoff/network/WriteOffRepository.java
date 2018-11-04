package cn.qingchengfit.writeoff.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import cn.qingchengfit.writeoff.vo.impl.TicketWrapper;
import io.reactivex.Flowable;
import javax.inject.Inject;

public class WriteOffRepository implements IWriteOffRepository {
  @Inject ITicketModel remoteService;

  @Inject WriteOffRepository() {
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  @Override public LiveData<Resource<TicketWrapper>> qcQueryTicket(String code) {
    return toLiveData(remoteService.qcQueryTicket(code));
  }

  @Override public LiveData<Resource<SimpleSuccessResponse>> qcVerifyTicket(TicketPostBody ticket) {
    return toLiveData(remoteService.qcVerifyTicket(ticket));
  }

  @Override public LiveData<Resource<TicketListWrapper>> qcGetVerifyTickets() {
    return toLiveData(remoteService.qcGetVerifyTickets());
  }

  @Override public LiveData<Resource<TicketWrapper>> qcGetTicketDetail(String ticket_id) {
    return toLiveData(remoteService.qcGetTicketDetail(ticket_id));
  }
}
