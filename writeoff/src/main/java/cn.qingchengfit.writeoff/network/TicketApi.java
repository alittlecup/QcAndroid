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
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TicketApi {
  @GET("/api/staffs/{id}/partner/eticket/query/") Flowable<QcDataResponse<TicketWrapper>> qcQueryTicket(@Path("id") String shop_id,
      @Query("e_code") String code, @QueryMap HashMap<String, Object> params);

  @POST("/api/staffs/{id}/partner/eticket/verify/") Flowable<QcDataResponse<SimpleSuccessResponse>> qcVerifyTicket(@Path("id") String id,
      @Body TicketPostBody ticket,@QueryMap HashMap<String,Object> params);

  @GET("/api/staffs/{id}/partner/etickets/?show_all=1") Flowable<QcDataResponse<TicketListWrapper>> qcGetVerifyTickets(@Path("id") String id,@QueryMap HashMap<String,Object> params);

  @GET("/api/staffs/{id}/partner/eticket/{ticket_id}/") Flowable<QcDataResponse<TicketWrapper>> qcGetTicketDetail(@Path("id") String id,
      @Path("ticket_id") String ticket_id, @QueryMap HashMap<String, Object> params);

}
