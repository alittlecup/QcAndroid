package cn.qingchengfit.writeoff.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;

public interface IWriteOffRepository {
  LiveData<Resource<Ticket>> qcQueryTicket(String code);

  LiveData<Resource<SimpleSuccessResponse>> qcVerifyTicket(TicketPostBody ticket);

  LiveData<Resource<TicketListWrapper>> qcGetVerifyTickets();

  LiveData<Resource<Ticket>> qcGetTicketDetail(String ticket_id);
}
