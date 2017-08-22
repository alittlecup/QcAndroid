package cn.qingchengfit.staffkit.constant;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.commpont.AppComponent;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.mvpbase.PresenterDelegate;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/1/18 2016.
 */
public abstract class BaseFragment extends Fragment implements BaseActivity.FragmentBackPress, CView {

    public FragCallBack mCallbackActivity;
    public Unbinder unbinder;
    protected boolean isLoading = false;
    List<Subscription> sps = new ArrayList<>();
    private List<Pair<String, Observable>> observables = new ArrayList<>();
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private MaterialDialog mAlert;
    private List<PresenterDelegate> delegates = new ArrayList<>();
    private boolean isVisible;

    @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && nextAnim > 0) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            if (animation != null) {
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {

                    }

                    @Override public void onAnimationEnd(Animation animation) {
                        onFinishAnimation();
                    }

                    @Override public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            return animation;
        } else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    protected void onFinishAnimation() {

    }

    @CallSuper @Override public void onAttach(Context context) {
        try {
            AndroidSupportInjection.inject(this);
        } catch (Exception e) {
            LogUtil.e("AppCompat没有找到注入页面 :" + getFragmentName());
            //CrashUtils.sendCrash(e);
        }

        super.onAttach(context);
        mCallbackActivity = (FragCallBack) context;
        setHasOptionsMenu(true);
    }

    @CallSuper @Override public void onDetach() {
        this.mCallbackActivity = null;
        super.onDetach();
    }

    @Override public boolean onFragmentBackPress() {
        return false;
    }

    public void initToolbar(@NonNull Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    public void hideLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
        }
    }

    public void showLoadingTrans() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoadingTransparent();
        }
    }

    public void hideLoadingTrans() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoadingTransparent();
        }
    }

    public void showAlert(String content) {
        showAlert(null, content);
    }

    public void showAlert(String title, String content) {
        if (mAlert == null) ;
        mAlert = new MaterialDialog.Builder(getContext()).positiveText(R.string.common_i_konw)
            .autoDismiss(true)
            .canceledOnTouchOutside(true)
            .build();
        if (mAlert.isShowing()) mAlert.dismiss();
        if (StringUtils.isEmpty(title)) {
            mAlert.setTitle(title);
        } else {
            mAlert.setTitle("");
        }
        mAlert.setContent(content);
        mAlert.show();
    }

    public void showAlert(@StringRes int content) {
        showAlert(getString(content));
    }

    public abstract String getFragmentName();

    protected void delegatePresenter(Presenter presenter, PView pView) {
        PresenterDelegate delegate = new PresenterDelegate(presenter);
        delegate.attachView(pView);
        delegates.add(delegate);

        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setBackPress(this);
        }
    }

    @CallSuper @Override public void onDestroyView() {
        if (getActivity() != null) {
            AppUtils.hideKeyboard(getActivity());
        }
        if (delegates.size() > 0) {
            for (int i = 0; i < delegates.size(); i++) {
                delegates.get(i).unattachView();
            }
        }
        //if (getActivity() instanceof BaseActivity){
        //    ((BaseActivity) getActivity()).setBackPress(null);
        //}
        unattachView();
        if (unbinder != null) unbinder.unbind();
        hideLoading();
        super.onDestroyView();
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    protected void onInVisible() {
    }

    protected void onVisible() {
    }

    public void unattachView() {
        for (int i = 0; i < sps.size(); i++) {
            sps.get(i).unsubscribe();
        }
        for (int i = 0; i < observables.size(); i++) {
            RxBus.getBus().unregister(observables.get(i).first, observables.get(i).second);
        }
    }

    public Subscription RxRegiste(Subscription subscription) {
        sps.add(subscription);
        return subscription;
    }

    public <T> Observable<T> RxBusAdd(@NonNull Class<T> clazz) {
        Observable ob = RxBus.getBus().register(clazz);
        observables.add(new Pair<String, Observable>(clazz.getName(), ob));
        return ob;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    public AppComponent getAppComponent() {
        if (getActivity() != null) {
            return ((App) getActivity().getApplication()).getAppCompoent();
        } else {
            return null;
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        hideLoadingTrans();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
