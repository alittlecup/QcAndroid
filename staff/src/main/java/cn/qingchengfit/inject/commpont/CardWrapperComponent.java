package cn.qingchengfit.inject.commpont;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.qingchengfit.inject.moudle.RealcardModule;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.card.FixRealCardBindStudentFragment;
import cn.qingchengfit.staffkit.views.card.FixRealcardStudentFragment;
import cn.qingchengfit.staffkit.views.card.charge.CompletedChargeFragment;
import cn.qingchengfit.staffkit.views.card.spendrecord.SpendRecordListFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/12.
 */
@Subcomponent(modules = RealcardModule.class) public interface CardWrapperComponent {
    void inject(CardDetailActivity activity);

    void inject(FixRealcardStudentFragment activity);

    void inject(FixRealCardBindStudentFragment activity);

    void inject(CompletedChargeFragment activity);

    void inject(SpendRecordListFragment activity);

    @Subcomponent() public interface CardDetailSubcomponent extends AndroidInjector<CardDetailActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CardDetailActivity> {
        }
    }

    @Subcomponent() public interface FixRealcardStudentSubcomponent extends AndroidInjector<FixRealcardStudentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<FixRealcardStudentFragment> {
        }
    }

    @Subcomponent() public interface FixRealCardBindStudentSubcomponent extends AndroidInjector<FixRealCardBindStudentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<FixRealCardBindStudentFragment> {
        }
    }

    @Subcomponent() public interface CompletedChargeSubcomponent extends AndroidInjector<CompletedChargeFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CompletedChargeFragment> {
        }
    }

    @Subcomponent() public interface SpendRecordListSubcomponent extends AndroidInjector<SpendRecordListFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SpendRecordListFragment> {
        }
    }

    @Module(subcomponents = CardDetailSubcomponent.class) abstract class CardDetailModule {
        @Binds @IntoMap @ActivityKey(CardDetailActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(CardDetailSubcomponent.Builder builder);
    }

    @Module(subcomponents = FixRealcardStudentSubcomponent.class) abstract class FixRealcardStudentModule {
        @Binds @IntoMap @FragmentKey(FixRealcardStudentFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            FixRealcardStudentSubcomponent.Builder builder);
    }

    @Module(subcomponents = FixRealCardBindStudentSubcomponent.class) abstract class FixRealCardBindStudentModule {
        @Binds @IntoMap @FragmentKey(FixRealCardBindStudentFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            FixRealCardBindStudentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CompletedChargeSubcomponent.class) abstract class CompletedChargeModule {
        @Binds @IntoMap @FragmentKey(CompletedChargeFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CompletedChargeSubcomponent.Builder builder);
    }

    @Module(subcomponents = SpendRecordListSubcomponent.class) abstract class SpendRecordListModule {
        @Binds @IntoMap @FragmentKey(SpendRecordListFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SpendRecordListSubcomponent.Builder builder);
    }
}
