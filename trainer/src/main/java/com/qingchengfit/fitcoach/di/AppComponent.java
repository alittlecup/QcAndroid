package com.qingchengfit.fitcoach.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.qingchengfit.recruit.di.BindRecruitModule;
import cn.qingchengfit.recruit.di.BindSeacherOrgModule;
import cn.qingchengfit.recruit.views.JobSearchChatActivity;
import cn.qingchengfit.saasbase.article.ArticleCommentsListFragment;
import cn.qingchengfit.saasbase.article.ArticleReplyFragment;
import cn.qingchengfit.saasbase.chat.ChatChooseInGymFragment;
import cn.qingchengfit.saasbase.chat.ChatFriendAllChooseFragment;
import cn.qingchengfit.saasbase.chat.ConversationFriendsFragment;
import cn.qingchengfit.saasbase.chat.RecruitMessageListFragment;
import cn.qingchengfit.saasbase.di.BindGymConfigAcitivty;
import cn.qingchengfit.saasbase.di.BindLoginActivity;
import cn.qingchengfit.saasbase.di.BindSaasCommonActivity;
import cn.qingchengfit.saasbase.di.BindUserActivity;
import cn.qingchengfit.saasbase.mvvm_student.di.ViewModelModule;
import cn.qingchengfit.saasbase.student.other.ChooseStaffFragment;
import cn.qingchengfit.staff.di.BindTrainerCourseActivity;
import cn.qingchengfit.views.container.ContainerActivity;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.fragment.CustomSaleFragment;
import com.qingchengfit.fitcoach.fragment.CustomStatmentFragment;
import com.qingchengfit.fitcoach.fragment.MyCoursePlanFragment;
import com.qingchengfit.fitcoach.fragment.SaleDetailFragment;
import com.qingchengfit.fitcoach.fragment.StatementDetailFragment;
import com.qingchengfit.fitcoach.fragment.SyncGymFragment;
import com.qingchengfit.fitcoach.fragment.guide.GuideSetGymFragment;
import com.qingchengfit.fitcoach.fragment.main.MainMsgFragment;
import com.qingchengfit.fitcoach.fragment.main.SettingFragment;
import com.qingchengfit.fitcoach.fragment.manage.ManageFragment;
import com.qingchengfit.fitcoach.fragment.mine.MineFragmentFragment;
import com.qingchengfit.fitcoach.fragment.schedule.MainScheduleFragment;
import com.qingchengfit.fitcoach.fragment.statement.CardTypeChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseReverseFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.SalerChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.HomeBannerFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnLoginHomeFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnLoginScheduleAdFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnloginManageFragment;
import dagger.Binds;
import dagger.Component;
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
 * Created by Paper on 2017/4/17.
 */
@Component(modules ={ AppModule.class, BindRecruitModule.class, BindSeacherOrgModule.class,
  BindTrainerCourseActivity.class, BindGymConfigAcitivty.class, BindUserActivity.class,
  BindLoginActivity.class, BindSaasCommonActivity.class, ViewModelModule.class,
    AppComponent.MainMsgFragmentModule.class,
    AppComponent.ContainerModule.class,
    AppComponent.ArticleCommentsListFragmentModule.class,
    AppComponent.ArticleReplyFragmentModule.class,
    AppComponent.ChatFriendAllChooseFragmentModule.class,
    AppComponent.ChatChooseInGymFragmentModule.class,
    AppComponent.SplashModule.class, AppComponent.StatementDetailFragmentModule.class,
    AppComponent.CourseChooseDialogFragmentModule.class, AppComponent.CourseReverseFragmentModule.class,
    AppComponent.SaleDetailFragmentModule.class, AppComponent.SalerChooseDialogFragmentModule.class,
    AppComponent.ManageFragmentModule.class, AppComponent.CustomStatmentFragmentModule.class,
    AppComponent.BaseDialogFragmentModule.class, AppComponent.CustomSaleFragmentModule.class,
    AppComponent.CardTypeChooseDialogFragmentModule.class,
    AppComponent.Main2Module.class,
    AppComponent.UnLoginHomeFragmentModule.class,
    AppComponent.HomeBannerFragmentModule.class,
    AppComponent.MineFragmentFragmentModule.class,
    AppComponent.UnloginManageFragmentModule.class,
    AppComponent.SyncGymFragmentModule.class,
    AppComponent.ManageFragmentModule.class,
    AppComponent.SettingFragmentModule.class,
    AppComponent.ChooseGymTrainerFragmentModule.class,
    AppComponent.MyCoursePlanFragmentModule.class,
  AppComponent.ChooseModule.class,
  AppComponent.JobSearchChatModule.class,
    AppComponent.ConversationFriendsFragmentModule.class,
    AppComponent.ChooseStaffFragmentModule.class,
    AppComponent.UnLoginScheduleAdFragmentModule.class,
    AppComponent.MainScheduleFragmentModule.class, AppComponent.GuideSetGymFragmentModule.class,
    AppComponent.RecruitMessageListFragmentModule.class
}) public interface AppComponent {
    void inject(App app);

    @Subcomponent() public interface ChooseStaffFragmentSubcomponent
        extends AndroidInjector<ChooseStaffFragment> {
        @Subcomponent.Builder public abstract class Builder
            extends AndroidInjector.Builder<ChooseStaffFragment> {
        }
    }
    @Subcomponent() public interface ConversationFriendsFragmentSubcomponent extends AndroidInjector<ConversationFriendsFragment> {
        @Subcomponent.Builder public abstract class Builder
            extends AndroidInjector.Builder<ConversationFriendsFragment> {
        }
    }
    @Subcomponent() public interface SplashSubcomponent extends AndroidInjector<com.qingchengfit.fitcoach.activity.SplashActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<com.qingchengfit.fitcoach.activity.SplashActivity> {
        }
    }
    @Subcomponent() public interface ChatChooseInGymFragmentSubcomponent extends AndroidInjector<ChatChooseInGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChatChooseInGymFragment> {
        }
    }


    @Subcomponent() public interface ChatFriendAllChooseFragmentSubcomponent extends AndroidInjector<ChatFriendAllChooseFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChatFriendAllChooseFragment> {
        }
    }

    @Subcomponent() public interface MainMsgFragmentSubcomponent extends AndroidInjector<MainMsgFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MainMsgFragment> {
        }
    }

    @Subcomponent() public interface ContainerSubcomponent extends AndroidInjector<ContainerActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ContainerActivity> {
        }
    }

    @Subcomponent() public interface ArticleReplyFragmentSubcomponent extends AndroidInjector<ArticleReplyFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ArticleReplyFragment> {
        }
    }

    @Subcomponent() public interface ArticleCommentsListFragmentSubcomponent extends AndroidInjector<ArticleCommentsListFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ArticleCommentsListFragment> {
        }
    }

    @Subcomponent() public interface CourseChooseDialogFragmentSubcomponent extends AndroidInjector<CourseChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CourseChooseDialogFragment> {
        }
    }

    @Subcomponent() public interface CourseReverseFragmentSubcomponent extends AndroidInjector<CourseReverseFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CourseReverseFragment> {
        }
    }

    @Subcomponent() public interface SaleDetailFragmentSubcomponent extends AndroidInjector<SaleDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SaleDetailFragment> {
        }
    }

    @Subcomponent() public interface SalerChooseDialogFragmentSubcomponent extends AndroidInjector<SalerChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SalerChooseDialogFragment> {
        }
    }

    @Subcomponent() public interface StatementDetailFragmentSubcomponent extends AndroidInjector<StatementDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<StatementDetailFragment> {
        }
    }

    @Subcomponent() public interface ManageFragmentSubcomponent extends AndroidInjector<ManageFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ManageFragment> {
        }
    }

    @Subcomponent() public interface CustomStatmentFragmentSubcomponent extends AndroidInjector<CustomStatmentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CustomStatmentFragment> {
        }
    }

    @Subcomponent() public interface BaseDialogFragmentSubcomponent extends AndroidInjector<BaseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<BaseDialogFragment> {
        }
    }

    @Subcomponent() public interface CustomSaleFragmentSubcomponent extends AndroidInjector<CustomSaleFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CustomSaleFragment> {
        }
    }

    @Subcomponent() public interface CardTypeChooseDialogFragmentSubcomponent extends AndroidInjector<CardTypeChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CardTypeChooseDialogFragment> {
        }
    }

    @Subcomponent() public interface Main2Subcomponent extends AndroidInjector<Main2Activity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<Main2Activity> {
        }
    }

    @Subcomponent() public interface UnLoginHomeFragmentSubcomponent extends AndroidInjector<UnLoginHomeFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<UnLoginHomeFragment> {
        }
    }

    @Subcomponent() public interface HomeBannerFragmentSubcomponent extends AndroidInjector<HomeBannerFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<HomeBannerFragment> {
        }
    }

    @Subcomponent() public interface UnloginManageFragmentSubcomponent extends AndroidInjector<UnloginManageFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<UnloginManageFragment> {
        }
    }

    @Subcomponent() public interface MineFragmentFragmentSubcomponent extends AndroidInjector<MineFragmentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MineFragmentFragment> {
        }
    }

    @Subcomponent() public interface SyncGymFragmentSubcomponent extends AndroidInjector<SyncGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SyncGymFragment> {
        }
    }

    @Subcomponent() public interface SettingFragmentSubcomponent extends AndroidInjector<SettingFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SettingFragment> {
        }
    }



    @Subcomponent() public interface UnLoginScheduleAdFragmentSubcomponent extends AndroidInjector<UnLoginScheduleAdFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<UnLoginScheduleAdFragment> {
        }
    }


    @Subcomponent() public interface MainScheduleFragmentSubcomponent extends AndroidInjector<MainScheduleFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MainScheduleFragment> {
        }
    }

    @Subcomponent() public interface GuideSetGymFragmentSubcomponent extends AndroidInjector<GuideSetGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<GuideSetGymFragment> {
        }
    }

    @Subcomponent() public interface ChooseSubcomponent extends AndroidInjector<ChooseActivity> {
        @Subcomponent.Builder public abstract class Builder
            extends AndroidInjector.Builder<ChooseActivity> {
        }
    }

    @Module(subcomponents = ChooseStaffFragmentSubcomponent.class)
    abstract class ChooseStaffFragmentModule {
        @Binds @IntoMap @FragmentKey(ChooseStaffFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ChooseStaffFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ConversationFriendsFragmentSubcomponent.class) abstract class ConversationFriendsFragmentModule {
        @Binds @IntoMap @FragmentKey(ConversationFriendsFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ConversationFriendsFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = SplashSubcomponent.class) abstract class SplashModule {
        @Binds @IntoMap @ActivityKey(com.qingchengfit.fitcoach.activity.SplashActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(SplashSubcomponent.Builder builder);
    }

    @Module(subcomponents = ChatChooseInGymFragmentSubcomponent.class) abstract class ChatChooseInGymFragmentModule {
        @Binds @IntoMap @FragmentKey(ChatChooseInGymFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ChatChooseInGymFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ChatFriendAllChooseFragmentSubcomponent.class) abstract class ChatFriendAllChooseFragmentModule {
        @Binds @IntoMap @FragmentKey(ChatFriendAllChooseFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ChatFriendAllChooseFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = MainMsgFragmentSubcomponent.class) abstract class MainMsgFragmentModule {
        @Binds @IntoMap @FragmentKey(MainMsgFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(MainMsgFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ContainerSubcomponent.class) abstract class ContainerModule {
        @Binds @IntoMap @ActivityKey(ContainerActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(ContainerSubcomponent.Builder builder);
    }

    @Module(subcomponents = ArticleReplyFragmentSubcomponent.class) abstract class ArticleReplyFragmentModule {
        @Binds @IntoMap @FragmentKey(ArticleReplyFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ArticleReplyFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ArticleCommentsListFragmentSubcomponent.class) abstract class ArticleCommentsListFragmentModule {
        @Binds @IntoMap @FragmentKey(ArticleCommentsListFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            ArticleCommentsListFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CourseChooseDialogFragmentSubcomponent.class) abstract class CourseChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(CourseChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            CourseChooseDialogFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CourseReverseFragmentSubcomponent.class) abstract class CourseReverseFragmentModule {
        @Binds @IntoMap @FragmentKey(CourseReverseFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            CourseReverseFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = SaleDetailFragmentSubcomponent.class) abstract class SaleDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(SaleDetailFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            SaleDetailFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = SalerChooseDialogFragmentSubcomponent.class) abstract class SalerChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(SalerChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            SalerChooseDialogFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = StatementDetailFragmentSubcomponent.class) abstract class StatementDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(StatementDetailFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            StatementDetailFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ManageFragmentSubcomponent.class) abstract class ManageFragmentModule {
        @Binds @IntoMap @FragmentKey(ManageFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ManageFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CustomStatmentFragmentSubcomponent.class) abstract class CustomStatmentFragmentModule {
        @Binds @IntoMap @FragmentKey(CustomStatmentFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            CustomStatmentFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = BaseDialogFragmentSubcomponent.class) abstract class BaseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(BaseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            BaseDialogFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CustomSaleFragmentSubcomponent.class) abstract class CustomSaleFragmentModule {
        @Binds @IntoMap @FragmentKey(CustomSaleFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            CustomSaleFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = CardTypeChooseDialogFragmentSubcomponent.class) abstract class CardTypeChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(CardTypeChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            CardTypeChooseDialogFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = Main2Subcomponent.class) abstract class Main2Module {
        @Binds @IntoMap @ActivityKey(Main2Activity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(Main2Subcomponent.Builder builder);
    }

    @Module(subcomponents = UnLoginHomeFragmentSubcomponent.class) abstract class UnLoginHomeFragmentModule {
        @Binds @IntoMap @FragmentKey(UnLoginHomeFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            UnLoginHomeFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = HomeBannerFragmentSubcomponent.class) abstract class HomeBannerFragmentModule {
        @Binds @IntoMap @FragmentKey(HomeBannerFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            HomeBannerFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = UnloginManageFragmentSubcomponent.class) abstract class UnloginManageFragmentModule {
        @Binds @IntoMap @FragmentKey(UnloginManageFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            UnloginManageFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = MineFragmentFragmentSubcomponent.class) abstract class MineFragmentFragmentModule {
        @Binds @IntoMap @FragmentKey(MineFragmentFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            MineFragmentFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = SyncGymFragmentSubcomponent.class) abstract class SyncGymFragmentModule {
        @Binds @IntoMap @FragmentKey(SyncGymFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SyncGymFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = SettingFragmentSubcomponent.class) abstract class SettingFragmentModule {
        @Binds @IntoMap @FragmentKey(SettingFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SettingFragmentSubcomponent.Builder builder);
    }



    @Module(subcomponents = UnLoginScheduleAdFragmentSubcomponent.class) abstract class UnLoginScheduleAdFragmentModule {
        @Binds @IntoMap @FragmentKey(UnLoginScheduleAdFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(UnLoginScheduleAdFragmentSubcomponent.Builder builder);
    }


    @Module(subcomponents = MainScheduleFragmentSubcomponent.class) abstract class MainScheduleFragmentModule {
        @Binds @IntoMap @FragmentKey(MainScheduleFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(MainScheduleFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = GuideSetGymFragmentSubcomponent.class) abstract class GuideSetGymFragmentModule {
        @Binds @IntoMap @FragmentKey(GuideSetGymFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
            GuideSetGymFragmentSubcomponent.Builder builder);
    }

    @Module(subcomponents = ChooseSubcomponent.class) abstract class ChooseModule {
        @Binds @IntoMap @ActivityKey(ChooseActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
            ChooseSubcomponent.Builder builder);
    }

   @Subcomponent() public interface JobSearchChatSubcomponent extends AndroidInjector<JobSearchChatActivity> {
       @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<JobSearchChatActivity> {
       }
   }

   @Module(subcomponents = JobSearchChatSubcomponent.class) abstract class JobSearchChatModule {
       @Binds @IntoMap @ActivityKey(JobSearchChatActivity.class)
       abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(JobSearchChatSubcomponent.Builder builder);
   }

  @Module(subcomponents = RecruitMessageListFragmentSubcomponent.class)
  abstract class RecruitMessageListFragmentModule {
    @Binds @IntoMap @FragmentKey(RecruitMessageListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        RecruitMessageListFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface RecruitMessageListFragmentSubcomponent extends AndroidInjector<RecruitMessageListFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitMessageListFragment> {}
    }

    @Subcomponent() public interface MyCoursePlanFragmentSubcomponent extends AndroidInjector<MyCoursePlanFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MyCoursePlanFragment> {}
    }
    @Module(subcomponents = MyCoursePlanFragmentSubcomponent.class) abstract class MyCoursePlanFragmentModule {
        @Binds @IntoMap @FragmentKey(MyCoursePlanFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(MyCoursePlanFragmentSubcomponent.Builder builder);
    }
    @Subcomponent() public interface ChooseGymTrainerFragmentSubcomponent extends AndroidInjector<com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragment> {}
    }
    @Module(subcomponents = ChooseGymTrainerFragmentSubcomponent.class) abstract class ChooseGymTrainerFragmentModule {
        @Binds @IntoMap @FragmentKey(com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ChooseGymTrainerFragmentSubcomponent.Builder builder);
    }

}
