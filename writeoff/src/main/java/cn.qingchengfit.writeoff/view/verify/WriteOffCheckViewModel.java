package cn.qingchengfit.writeoff.view.verify;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import cn.qingchengfit.writeoff.vo.impl.TicketPostBody;
import javax.inject.Inject;

public class WriteOffCheckViewModel extends BaseViewModel {
  @Inject IWriteOffRepository repository;
  public MutableLiveData<Ticket> ticketMutableLiveData = new MutableLiveData<>();
  public  MutableLiveData<TicketPostBody> ticketPostBody = new MutableLiveData<>();

  @Inject public WriteOffCheckViewModel() {
    ticketPostBody.setValue(new TicketPostBody());
  }

  public void queryTicket(String number) {
    Transformations.switchMap(repository.qcQueryTicket(number), input -> {
      dealResource(input, ticket -> ticketMutableLiveData.setValue(ticket));
      return ticketMutableLiveData;
    });
  }
}
