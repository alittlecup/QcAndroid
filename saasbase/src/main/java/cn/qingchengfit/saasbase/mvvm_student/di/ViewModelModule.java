package cn.qingchengfit.saasbase.mvvm_student.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import cn.qingchengfit.saasbase.turnovers.TurnoverOrderDetailVM;
import cn.qingchengfit.saasbase.turnovers.TurnoversVM;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by huangbaole on 2017/11/15.
 */

@Module public abstract class ViewModelModule {

  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


  @Binds @IntoMap @ViewModelKey(BatchCopyViewModel.class)
  abstract ViewModel bindBatchCopyViewModel(BatchCopyViewModel model);

  @Binds @IntoMap @ViewModelKey(TurnoversVM.class)
  abstract ViewModel bindTurnoversVM(TurnoversVM model);

  @Binds @IntoMap @ViewModelKey(TurnoverOrderDetailVM.class)
  abstract ViewModel bindTurnoverOrderDetailVM(TurnoverOrderDetailVM model);


  /**
   * new
   */

}
