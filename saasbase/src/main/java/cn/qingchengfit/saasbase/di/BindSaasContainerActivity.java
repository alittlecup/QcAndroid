//package cn.qingchengfit.saasbase.di;
//
//import android.support.v4.app.Fragment;
//import cn.qingchengfit.saasbase.SaasContainerActivity;
//import cn.qingchengfit.saasbase.course.batch.views.AddBatchFragment;
//import dagger.Binds;
//import dagger.Module;
//import dagger.Subcomponent;
//import dagger.android.AndroidInjector;
//import dagger.android.ContributesAndroidInjector;
//import dagger.android.support.FragmentKey;
//import dagger.multibindings.IntoMap;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/9/21.
// */
//@Module
//public abstract class BindSaasContainerActivity {
//  @ContributesAndroidInjector( modules = {
//      BindSaasContainerActivity.AddBatchFragmentModule.class,
//  })
//  abstract SaasContainerActivity contributeActivity();
//
//  @Subcomponent() public interface AddBatchFragmentSubcomponent extends AndroidInjector<AddBatchFragment> {
//      @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<AddBatchFragment> {}
//  }
//  @Module(subcomponents = AddBatchFragmentSubcomponent.class) abstract class AddBatchFragmentModule {
//      @Binds @IntoMap @FragmentKey(AddBatchFragment.class)
//      abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(AddBatchFragmentSubcomponent.Builder builder);
//  }
//}
