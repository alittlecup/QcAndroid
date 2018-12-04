package cn.qingchengfit.card;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.card.di.BindStaffCardActivity;
import cn.qingchengfit.card.network.CardRespository;
import cn.qingchengfit.card.network.CardRespositoryImpl;
import cn.qingchengfit.card.view.coupons.ChooseCouponsVM;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = { BindStaffCardActivity.class }) public abstract class CardModule {

  @Binds @IntoMap @ViewModelKey(ChooseCouponsVM.class)
  abstract ViewModel bindChooseCouponsVM(ChooseCouponsVM chooseCouponsVM);

  @Binds abstract CardRespository bingCardRespository(CardRespositoryImpl cardRespository);
}
