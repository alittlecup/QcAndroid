package cn.qingchengfit.writeoff.network;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import cn.qingchengfit.writeoff.vo.impl.TicketWrapper;
import io.reactivex.Flowable;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ITicketModel {
  Flowable<QcDataResponse<TicketWrapper>> qcQueryTicket(String code);

  Flowable<QcDataResponse<SimpleSuccessResponse>> qcVerifyTicket(TicketPostBody ticket);

  Flowable<QcDataResponse<TicketListWrapper>> qcGetVerifyTickets();

  Flowable<QcDataResponse<TicketWrapper>> qcGetTicketDetail(String ticket_id);
}
