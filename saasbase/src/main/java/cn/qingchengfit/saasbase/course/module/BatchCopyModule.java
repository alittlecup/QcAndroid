package cn.qingchengfit.saasbase.course.module;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by fb on 2018/4/8.
 */

@Module
public abstract class BatchCopyModule {

  @Binds @IntoMap @ViewModelKey(BatchCopyViewModel.class)
  abstract ViewModel bindPlanViewModel(BatchCopyViewModel planViewModel);

}
