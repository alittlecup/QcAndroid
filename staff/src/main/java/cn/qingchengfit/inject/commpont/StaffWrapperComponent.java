package cn.qingchengfit.inject.commpont;

import android.app.Activity;
import android.support.v4.app.Fragment;

import cn.qingchengfit.inject.moudle.StaffWrapperMoudle;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.allotsales.MultiModifyFragment;
import cn.qingchengfit.staffkit.views.allotsales.SaleDetailFragment;
import cn.qingchengfit.staffkit.views.allotsales.SalesListFragment;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpActivity;
import cn.qingchengfit.staffkit.views.student.score.ConfigFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreActivity;
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
 * Created by Paper on 2017/3/4.
 */
@Subcomponent(modules = StaffWrapperMoudle.class) public interface StaffWrapperComponent {

    void inject(StudentActivity activity);

    void inject(ChooseReferrerActivity activity);

    void inject(FollowUpActivity activity);

    void inject(ScoreActivity activity);

    void inject(AllotSalesActivity activity);

    void inject(MultiModifyFragment i);

    void inject(cn.qingchengfit.staffkit.views.allotsales.SaleDetailFragment i);

    void inject(SalesListFragment fragment);

    void inject(ConfigFragment fragment);

    @Subcomponent() public interface StudentSubcomponent extends AndroidInjector<StudentActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<StudentActivity> {
        }
    }

    @Subcomponent() public interface ChooseReferrerSubcomponent extends AndroidInjector<ChooseReferrerActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChooseReferrerActivity> {
        }
    }

    @Subcomponent() public interface FollowUpSubcomponent extends AndroidInjector<FollowUpActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<FollowUpActivity> {
        }
    }

    @Subcomponent() public interface ScoreSubcomponent extends AndroidInjector<ScoreActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ScoreActivity> {
        }
    }

    @Subcomponent() public interface AllotSalesSubcomponent extends AndroidInjector<AllotSalesActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<AllotSalesActivity> {
        }
    }

    @Subcomponent() public interface MultiModifySubcomponent extends AndroidInjector<MultiModifyFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MultiModifyFragment> {
        }
    }

    @Subcomponent() public interface SaleDetailFSubcomponent extends AndroidInjector<SaleDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SaleDetailFragment> {
        }
    }

    @Subcomponent() public interface SalesListSubcomponent extends AndroidInjector<SalesListFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SalesListFragment> {
        }
    }

    @Subcomponent() public interface ConfigSubcomponent extends AndroidInjector<ConfigFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ConfigFragment> {
        }
    }

    @Module(subcomponents = StudentSubcomponent.class) abstract class StudentModule {
        @Binds @IntoMap @ActivityKey(StudentActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(StudentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ChooseReferrerSubcomponent.class) abstract class ChooseReferrerModule {
        @Binds @IntoMap @ActivityKey(ChooseReferrerActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(ChooseReferrerSubcomponent.Builder builder);
    }

    @Module(subcomponents = FollowUpSubcomponent.class) abstract class FollowUpModule {
        @Binds @IntoMap @ActivityKey(FollowUpActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(FollowUpSubcomponent.Builder builder);
    }

    @Module(subcomponents = ScoreSubcomponent.class) abstract class ScoreModule {
        @Binds @IntoMap @ActivityKey(ScoreActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(ScoreSubcomponent.Builder builder);
    }

    @Module(subcomponents = AllotSalesSubcomponent.class) abstract class AllotSalesModule {
        @Binds @IntoMap @ActivityKey(AllotSalesActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(AllotSalesSubcomponent.Builder builder);
    }

    @Module(subcomponents = MultiModifySubcomponent.class) abstract class MultiModifyModule {
        @Binds @IntoMap @FragmentKey(MultiModifyFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(MultiModifySubcomponent.Builder builder);
    }

    @Module(subcomponents = SaleDetailFSubcomponent.class) abstract class SaleDetailFModule {
        @Binds @IntoMap @FragmentKey(SaleDetailFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SaleDetailFSubcomponent.Builder builder);
    }

    @Module(subcomponents = SalesListSubcomponent.class) abstract class SalesListModule {
        @Binds @IntoMap @FragmentKey(SalesListFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SalesListSubcomponent.Builder builder);
    }

    @Module(subcomponents = ConfigSubcomponent.class) abstract class ConfigModule {
        @Binds @IntoMap @FragmentKey(ConfigFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ConfigSubcomponent.Builder builder);
    }
}
