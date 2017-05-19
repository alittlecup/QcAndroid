package com.qingchengfit.fitcoach.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.qingchengfit.article.ArticleCommentsListFragment;
import cn.qingchengfit.article.ArticleReplyFragment;
import cn.qingchengfit.chat.ChatChooseInGymFragment;
import cn.qingchengfit.chat.ChatFriendAllChooseFragment;
import cn.qingchengfit.chat.ConversationFriendsFragment;
import cn.qingchengfit.views.container.ContainerActivity;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.activity.SplashActivity;
import com.qingchengfit.fitcoach.fragment.CustomSaleFragment;
import com.qingchengfit.fitcoach.fragment.CustomStatmentFragment;
import com.qingchengfit.fitcoach.fragment.SaleDetailFragment;
import com.qingchengfit.fitcoach.fragment.StatementDetailFragment;
import com.qingchengfit.fitcoach.fragment.main.MainMsgFragment;
import com.qingchengfit.fitcoach.fragment.manage.ManageFragment;
import com.qingchengfit.fitcoach.fragment.statement.CardTypeChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.BaseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseChooseDialogFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.CourseReverseFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.SalerChooseDialogFragment;
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
@Component(modules ={ AppModule.class,
    AppComponent.MainMsgFragmentModule.class,
    AppComponent.ContainerModule.class,
    AppComponent.ArticleCommentsListFragmentModule.class,
    AppComponent.ArticleReplyFragmentModule.class,
    AppComponent.ChatFriendAllChooseFragmentModule.class,
    AppComponent.ChatChooseInGymFragmentModule.class,
    AppComponent.ConversationFriendsFragmentModule.class,
    AppComponent.SplashModule.class, AppComponent.StatementDetailFragmentModule.class,
    AppComponent.CourseChooseDialogFragmentModule.class, AppComponent.CourseReverseFragmentModule.class,
    AppComponent.SaleDetailFragmentModule.class, AppComponent.SalerChooseDialogFragmentModule.class,
    AppComponent.ManageFragmentModule.class, AppComponent.CustomStatmentFragmentModule.class,
    AppComponent.BaseDialogFragmentModule.class, AppComponent.CustomSaleFragmentModule.class,
    AppComponent.CardTypeChooseDialogFragmentModule.class,
})
public interface AppComponent {
    void inject(App app);
    @Subcomponent() public interface ConversationFriendsFragmentSubcomponent extends AndroidInjector<ConversationFriendsFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ConversationFriendsFragment> {}
    }
    @Module(subcomponents = ConversationFriendsFragmentSubcomponent.class) abstract class ConversationFriendsFragmentModule {
        @Binds @IntoMap @FragmentKey(ConversationFriendsFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ConversationFriendsFragmentSubcomponent.Builder builder);
    }
    @Subcomponent() public interface SplashSubcomponent extends AndroidInjector<SplashActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SplashActivity> {
        }
    }
    @Module(subcomponents = SplashSubcomponent.class) abstract class SplashModule {
        @Binds @IntoMap @ActivityKey(SplashActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(SplashSubcomponent.Builder builder);
    }

    @Subcomponent() public interface ChatChooseInGymFragmentSubcomponent extends AndroidInjector<ChatChooseInGymFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChatChooseInGymFragment> {}
    }
    @Module(subcomponents = ChatChooseInGymFragmentSubcomponent.class) abstract class ChatChooseInGymFragmentModule {
        @Binds @IntoMap @FragmentKey(ChatChooseInGymFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ChatChooseInGymFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface ChatFriendAllChooseFragmentSubcomponent extends AndroidInjector<ChatFriendAllChooseFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChatFriendAllChooseFragment> {}
    }
    @Module(subcomponents = ChatFriendAllChooseFragmentSubcomponent.class) abstract class ChatFriendAllChooseFragmentModule {
        @Binds @IntoMap @FragmentKey(ChatFriendAllChooseFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ChatFriendAllChooseFragmentSubcomponent.Builder builder);
    }
    @Subcomponent() public interface MainMsgFragmentSubcomponent extends AndroidInjector<MainMsgFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MainMsgFragment> {}
    }

    @Module(subcomponents = MainMsgFragmentSubcomponent.class) abstract class MainMsgFragmentModule {
        @Binds @IntoMap @FragmentKey(MainMsgFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(MainMsgFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface ContainerSubcomponent extends AndroidInjector<ContainerActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ContainerActivity> {
        }
    }
    @Module(subcomponents = ContainerSubcomponent.class) abstract class ContainerModule {
        @Binds @IntoMap @ActivityKey(ContainerActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(ContainerSubcomponent.Builder builder);
    }
    @Subcomponent() public interface ArticleReplyFragmentSubcomponent extends AndroidInjector<ArticleReplyFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ArticleReplyFragment> {}
    }
    @Module(subcomponents = ArticleReplyFragmentSubcomponent.class) abstract class ArticleReplyFragmentModule {
        @Binds @IntoMap @FragmentKey(ArticleReplyFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ArticleReplyFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface ArticleCommentsListFragmentSubcomponent extends AndroidInjector<ArticleCommentsListFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ArticleCommentsListFragment> {}
    }
    @Module(subcomponents = ArticleCommentsListFragmentSubcomponent.class) abstract class ArticleCommentsListFragmentModule {
        @Binds @IntoMap @FragmentKey(ArticleCommentsListFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ArticleCommentsListFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface StatementDetailFragmentSubcomponent extends AndroidInjector<StatementDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<StatementDetailFragment> {}
    }
    @Module(subcomponents = StatementDetailFragmentSubcomponent.class) abstract class StatementDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(StatementDetailFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(StatementDetailFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface CourseChooseDialogFragmentSubcomponent extends AndroidInjector<CourseChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CourseChooseDialogFragment> {}
    }
    @Module(subcomponents = CourseChooseDialogFragmentSubcomponent.class) abstract class CourseChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(CourseChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CourseChooseDialogFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface CourseReverseFragmentSubcomponent extends AndroidInjector<CourseReverseFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CourseReverseFragment> {}
    }
    @Module(subcomponents = CourseReverseFragmentSubcomponent.class) abstract class CourseReverseFragmentModule {
        @Binds @IntoMap @FragmentKey(CourseReverseFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CourseReverseFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface SaleDetailFragmentSubcomponent extends AndroidInjector<SaleDetailFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SaleDetailFragment> {}
    }
    @Module(subcomponents = SaleDetailFragmentSubcomponent.class) abstract class SaleDetailFragmentModule {
        @Binds @IntoMap @FragmentKey(SaleDetailFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SaleDetailFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface SalerChooseDialogFragmentSubcomponent extends AndroidInjector<SalerChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SalerChooseDialogFragment> {}
    }
    @Module(subcomponents = SalerChooseDialogFragmentSubcomponent.class) abstract class SalerChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(SalerChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(SalerChooseDialogFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface ManageFragmentSubcomponent extends AndroidInjector<ManageFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ManageFragment> {}
    }
    @Module(subcomponents = ManageFragmentSubcomponent.class) abstract class ManageFragmentModule {
        @Binds @IntoMap @FragmentKey(ManageFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ManageFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface CustomStatmentFragmentSubcomponent extends AndroidInjector<CustomStatmentFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CustomStatmentFragment> {}
    }
    @Module(subcomponents = CustomStatmentFragmentSubcomponent.class) abstract class CustomStatmentFragmentModule {
        @Binds @IntoMap @FragmentKey(CustomStatmentFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CustomStatmentFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface BaseDialogFragmentSubcomponent extends AndroidInjector<BaseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<BaseDialogFragment> {}
    }
    @Module(subcomponents = BaseDialogFragmentSubcomponent.class) abstract class BaseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(BaseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(BaseDialogFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface CustomSaleFragmentSubcomponent extends AndroidInjector<CustomSaleFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CustomSaleFragment> {}
    }
    @Module(subcomponents = CustomSaleFragmentSubcomponent.class) abstract class CustomSaleFragmentModule {
        @Binds @IntoMap @FragmentKey(CustomSaleFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CustomSaleFragmentSubcomponent.Builder builder);
    }

    @Subcomponent() public interface CardTypeChooseDialogFragmentSubcomponent extends AndroidInjector<CardTypeChooseDialogFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CardTypeChooseDialogFragment> {}
    }
    @Module(subcomponents = CardTypeChooseDialogFragmentSubcomponent.class) abstract class CardTypeChooseDialogFragmentModule {
        @Binds @IntoMap @FragmentKey(CardTypeChooseDialogFragment.class)
        abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CardTypeChooseDialogFragmentSubcomponent.Builder builder);
    }

}
