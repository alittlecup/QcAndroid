package cn.qingchengfit.writeoff.network;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import cn.qingchengfit.writeoff.vo.impl.TicketWrapper;
import io.reactivex.Flowable;
import java.util.HashMap;
import javax.inject.Inject;

public class TicketModel implements ITicketModel {
  TicketApi ticketApi;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  @Inject public TicketModel(QcRestRepository qcRestRepository) {
    ticketApi = qcRestRepository.createRxJava2Api(TicketApi.class);
  }

  @Override public Flowable<QcDataResponse<TicketWrapper>> qcQueryTicket(String code) {
    return ticketApi.qcQueryTicket(loginStatus.staff_id(), code, gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<SimpleSuccessResponse>> qcVerifyTicket(TicketPostBody ticket) {
    HashMap<String, Object> params = gymWrapper.getParams();
    ticket.setId((String) params.get("id"));
    ticket.setModel((String) params.get("model"));
    return ticketApi.qcVerifyTicket(loginStatus.staff_id(), ticket, gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<TicketListWrapper>> qcGetVerifyTickets() {
    return ticketApi.qcGetVerifyTickets(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<TicketWrapper>> qcGetTicketDetail(String ticket_id) {
    return ticketApi.qcGetTicketDetail(loginStatus.staff_id(), ticket_id, gymWrapper.getParams());
  }
}
