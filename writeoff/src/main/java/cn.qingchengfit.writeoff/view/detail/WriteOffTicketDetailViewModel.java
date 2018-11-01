package cn.qingchengfit.writeoff.view.detail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import javax.inject.Inject;

public class WriteOffTicketDetailViewModel extends BaseViewModel {
  public MutableLiveData<Ticket> ticketMutableLiveData=new MutableLiveData<>();
  @Inject IWriteOffRepository repository;
  @Inject public WriteOffTicketDetailViewModel(){}

  public void loadTicketDetail(String number){
  }
}
