package cn.qingchengfit.wxpreview.old.newa;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.wxpreview.di.BindWebActivityForGuide;
import cn.qingchengfit.wxpreview.di.BindWxMiniActivity;
import cn.qingchengfit.wxpreview.di.BindWxPreviewEmptyActivity;
import cn.qingchengfit.wxpreview.routers.WxpreviewRouterCenter;
import cn.qingchengfit.wxpreview.routers.wxminiImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = {
    BindWebActivityForGuide.class, BindWxMiniActivity.class,BindWxPreviewEmptyActivity.class
}) public abstract class WxPreviewModule {
  @Binds @IntoMap @ViewModelKey(WebActivityForGuideViewModel.class)
  abstract ViewModel bindWebActivityForGuideViewModel(
      WebActivityForGuideViewModel webActivityForGuideViewModel);

  @Provides static WxpreviewRouterCenter provideIWxmini() {
    return new WxpreviewRouterCenter().registe(new wxminiImpl());
  }
}