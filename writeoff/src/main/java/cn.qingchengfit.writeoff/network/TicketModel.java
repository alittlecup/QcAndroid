package cn.qingchengfit.writeoff.network;

import android.util.Log;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.HashMap;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketModel implements ITicketModel {
  TicketApi ticketApi;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  @Inject public TicketModel(QcRestRepository qcRestRepository) {
    OkHttpClient client = qcRestRepository.getClient();
    OkHttpClient http = client.newBuilder().addInterceptor(new HttpLoggingInterceptor(message -> {
      Log.d("HTTP", message);
    }).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    Gson customGsonInstance = (new GsonBuilder()).enableComplexMapKeySerialization().create();
    Retrofit retrofit = new Retrofit.Builder().client(http)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(qcRestRepository.getHost())
        .build();
    ticketApi = retrofit.create(TicketApi.class);
  }

  @Override public Flowable<QcDataResponse<Ticket>> qcQueryTicket(String code) {
    return ticketApi.qcQueryTicket(loginStatus.staff_id(), code, gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<SimpleSuccessResponse>> qcVerifyTicket(TicketPostBody ticket) {
    return ticketApi.qcVerifyTicket(loginStatus.staff_id(), ticket, gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<TicketListWrapper>> qcGetVerifyTickets() {
    return ticketApi.qcGetVerifyTickets(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<Ticket>> qcGetTicketDetail(String ticket_id) {
    return ticketApi.qcGetTicketDetail(loginStatus.staff_id(), ticket_id, gymWrapper.getParams());
  }
}
