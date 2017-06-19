package cn.qingchengfit.recruit.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
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
 * Created by Paper on 2017/6/16.
 */
@Subcomponent(modules = SearchOrg.SearchModule.class) public interface SearchOrg extends AndroidInjector<RecruitActivity> {

    @Subcomponent() public interface SearchSubcomponent extends AndroidInjector<SearchActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SearchActivity> {
        }
    }

    @Subcomponent() public interface SearchFragmentSubcomponent extends AndroidInjector<SearchFragment> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<SearchFragment> {
        }
    }

    @Module(subcomponents = SearchSubcomponent.class) abstract class SearchModule {
        @Binds @IntoMap @ActivityKey(SearchActivity.class)
        abstract Factory<? extends Activity> bindYourFragmentInjectorFactory(SearchSubcomponent.Builder builder);
    }

    @Module(subcomponents = SearchFragmentSubcomponent.class) abstract class SearchFragmentModule {
        @Binds @IntoMap @FragmentKey(SearchFragment.class)
        abstract Factory<? extends Fragment> bindYourFragmentInjectorFactory(SearchFragmentSubcomponent.Builder builder);
    }
}
