package cn.qingchengfit.writeoff.di;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.writeoff.network.ITicketModel;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.network.TicketModel;
import cn.qingchengfit.writeoff.network.WriteOffRepository;
import cn.qingchengfit.writeoff.routers.Iwriteoff;
import cn.qingchengfit.writeoff.routers.WriteoffRouterCenter;
import cn.qingchengfit.writeoff.routers.writeoffImpl;
import cn.qingchengfit.writeoff.view.detail.WriteOffTicketDetailViewModel;
import cn.qingchengfit.writeoff.view.list.WriteOffListViewModel;
import cn.qingchengfit.writeoff.view.verify.WriteOffCheckViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = {
    BindWriteOffActivity.class
}) public abstract class WriteOffViewModule {
  @Binds abstract IWriteOffRepository bindIWriteOffRepository(WriteOffRepository repository);
  @Binds abstract ITicketModel bindITicketModel(TicketModel ticketModel);

  @Binds @IntoMap @ViewModelKey(WriteOffCheckViewModel.class)
  abstract ViewModel bindWriteOffCheckViewModel(WriteOffCheckViewModel writeOffCheckViewModel);

  @Binds @IntoMap @ViewModelKey(WriteOffTicketDetailViewModel.class)
  abstract ViewModel bindWriteOffTicketDetailViewModel(
      WriteOffTicketDetailViewModel writeOffTicketDetailViewModel);

  @Binds @IntoMap @ViewModelKey(WriteOffListViewModel.class)
  abstract ViewModel bindWriteOffListViewModel(WriteOffListViewModel writeOffListViewModel);

  @Provides static WriteoffRouterCenter providWriteoffRouterCenter() {
    return new WriteoffRouterCenter().registe(new writeoffImpl());
  }
}
