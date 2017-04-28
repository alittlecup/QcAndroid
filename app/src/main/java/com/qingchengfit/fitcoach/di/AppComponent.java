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
import com.qingchengfit.fitcoach.fragment.main.MainMsgFragment;
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
    AppComponent.SplashModule.class,
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

}
