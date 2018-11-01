package cn.qingchengfit.writeoff.view;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;
import javax.inject.Inject;

public class WriteOffTicketViewModel extends BaseViewModel {
  public MutableLiveData<Boolean> isPrivate = new MutableLiveData<>();

  @Inject public WriteOffTicketViewModel() {
  }
}
