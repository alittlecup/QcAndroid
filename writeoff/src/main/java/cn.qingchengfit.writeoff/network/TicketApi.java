package cn.qingchengfit.writeoff.network;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import io.reactivex.Flowable;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TicketApi {
  @GET("/api/staffs/{id}/eticket/query/") Flowable<QcDataResponse<Ticket>> qcQueryTicket(@Path("id") String shop_id,
      @Query("e_code") String code, @QueryMap HashMap<String, Object> params);

  @POST("/api/staffs/{id}/eticket/verify/") Flowable<QcDataResponse<SimpleSuccessResponse>> qcVerifyTicket(@Path("id") String id,
      @Body TicketPostBody ticket,@QueryMap HashMap<String,Object> params);

  @GET("/api/staffs/{id}/tmall/eticket/") Flowable<QcDataResponse<TicketListWrapper>> qcGetVerifyTickets(@Path("id") String id,@QueryMap HashMap<String,Object> params);

  @GET("/api/staffs/{id}/eticket/{ticket_id}/") Flowable<QcDataResponse<Ticket>> qcGetTicketDetail(@Path("id") String id,
      @Path("ticket_id") String ticket_id, @QueryMap HashMap<String, Object> params);

}
