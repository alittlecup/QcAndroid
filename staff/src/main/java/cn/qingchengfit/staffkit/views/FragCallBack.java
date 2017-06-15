package cn.qingchengfit.staffkit.views;

import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/7 2016.
 */
public interface FragCallBack {
    @IdRes int getFragId();

    //<T> T getComponent();

    void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener);

    void cleanToolbar();

    void openSeachView(String hint, Action1<CharSequence> action1);

    void onChangeFragment(BaseFragment fragment);

    void setBar(ToolbarBean bar);
}
