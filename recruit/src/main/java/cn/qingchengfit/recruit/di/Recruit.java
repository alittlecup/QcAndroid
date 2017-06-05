package cn.qingchengfit.recruit.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.recruit.views.DialogSendResumeFragment;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDescFragment;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsInGymFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsInvitedFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsSentFragment;
import cn.qingchengfit.recruit.views.RecruitPositionsStarredFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
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
 * Created by Paper on 2017/5/31.
 */
@Subcomponent(modules = Recruit.SeekPositionHomeFragmentModule.class) public interface Recruit extends AndroidInjector<RecruitActivity> {

    //@Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitActivity> {
    //}
    //@Module(subcomponents = RecruitSubComponent.class) abstract class RecruitModule {
    //    @Binds @IntoMap @ActivityKey(RecruitActivity.class)
    //    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(RecruitSubComponent.Builder builder);
    //}

    @Subcomponent() public interface SeekPositionHomeFragmentSubcomponent extends AndroidInjector<SeekPositionHomeFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SeekPositionHomeFragment> {
        }
    }

    @Subcomponent() public interface MyPositionsInfoFragmentSubcomponent extends AndroidInjector<MyPositionsInfoFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MyPositionsInfoFragment> {
        }
    }

    @Subcomponent() public interface DialogSendResumeFragmentSubcomponent extends AndroidInjector<DialogSendResumeFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<DialogSendResumeFragment> {
        }
    }

    @Subcomponent() public interface RecruitGymDescFragmentSubcomponent extends AndroidInjector<RecruitGymDescFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitGymDescFragment> {
        }
    }

    @Subcomponent() public interface RecruitGymDetailFragmentSubcomponent extends AndroidInjector<RecruitGymDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitGymDetailFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionDetailFragmentSubcomponent extends AndroidInjector<RecruitPositionDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionDetailFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionsFragmentSubcomponent extends AndroidInjector<RecruitPositionsFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionsFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionsInGymFragmentSubcomponent extends AndroidInjector<RecruitPositionsInGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionsInGymFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionsInvitedFragmentSubcomponent extends AndroidInjector<RecruitPositionsInvitedFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionsInvitedFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionsSentFragmentSubcomponent extends AndroidInjector<RecruitPositionsSentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionsSentFragment> {
        }
    }

    @Subcomponent() public interface RecruitPositionsStarredFragmentSubcomponent extends AndroidInjector<RecruitPositionsStarredFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitPositionsStarredFragment> {
        }
    }

    @Module(subcomponents = SeekPositionHomeFragmentSubcomponent.class) abstract class SeekPositionHomeFragmentModule {
        @Binds @IntoMap @FragmentKey(SeekPositionHomeFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            SeekPositionHomeFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = MyPositionsInfoFragmentSubcomponent.class) abstract class MyPositionsInfoFragmentModule {
        @Binds @IntoMap @FragmentKey(MyPositionsInfoFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(MyPositionsInfoFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = DialogSendResumeFragmentSubcomponent.class) abstract class DialogSendResumeFragmentModule {
        @Binds @IntoMap @FragmentKey(DialogSendResumeFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(DialogSendResumeFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitGymDescFragmentSubcomponent.class) abstract class RecruitGymDescFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitGymDescFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitGymDescFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitGymDetailFragmentSubcomponent.class) abstract class RecruitGymDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitGymDetailFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitGymDetailFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionDetailFragmentSubcomponent.class) abstract class RecruitPositionDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionDetailFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionDetailFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionsFragmentSubcomponent.class) abstract class RecruitPositionsFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionsFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionsFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionsInGymFragmentSubcomponent.class) abstract class RecruitPositionsInGymFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionsInGymFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionsInGymFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionsInvitedFragmentSubcomponent.class) abstract class RecruitPositionsInvitedFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionsInvitedFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionsInvitedFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionsSentFragmentSubcomponent.class) abstract class RecruitPositionsSentFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionsSentFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionsSentFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = RecruitPositionsStarredFragmentSubcomponent.class) abstract class RecruitPositionsStarredFragmentModule {
        @Binds @IntoMap @FragmentKey(RecruitPositionsStarredFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(RecruitPositionsStarredFragmentSubcomponent.Builder builder);
    }
}
