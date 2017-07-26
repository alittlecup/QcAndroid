package cn.qingchengfit.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.PresenterDelegate;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.R;
import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import rx.Observable;
import rx.Subscription;

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
 * Created by Paper on 15/9/22 2015.
 */
public abstract class BaseFragment extends Fragment
    implements BaseActivity.FragmentBackPress, CView {

    public Unbinder unbinder;
    // 标志位，标志已经初始化完成
    protected boolean isVisible;
    protected  boolean isInit = false;
    protected boolean isLazyLoad = true;
    protected boolean isPrepared;
    List<Subscription> sps = new ArrayList<>();
    private List<PresenterDelegate> delegates = new ArrayList<>();
    private List<Pair<String, Observable>> observables = new ArrayList<>();
    private FragmentManager.FragmentLifecycleCallbacks childrenCB = new FragmentManager.FragmentLifecycleCallbacks() {

        @Override public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            onChildViewCreated(fm, f, v, savedInstanceState);
        }
    };

    protected void onChildViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getChildFragmentManager().registerFragmentLifecycleCallbacks(childrenCB, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return isBlockTouch();
            }
        });
    }

    public boolean isBlockTouch() {
        return true;
    }

    @Override public void onAttach(Context context) {
        try {
            AndroidSupportInjection.inject(this);
        } catch (Exception e) {
            LogUtil.e("not find fragment:"+getFragmentName());
        }
        super.onAttach(context);
    }

    protected void delegatePresenter(Presenter presenter, PView pView) {
        PresenterDelegate delegate = new PresenterDelegate(presenter);
        delegate.attachView(pView);
        delegates.add(delegate);

      if (getActivity() instanceof BaseActivity) {
        ((BaseActivity) getActivity()).setBackPress(this);
      }
    }

  public void setBackPress() {
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).setBackPress(this);
    }
  }

  public void setBackPressNull() {
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).setBackPress(null);
    }
  }

    public void initToolbar(@NonNull Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
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

  @Override public boolean onFragmentBackPress() {
    return false;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (getUserVisibleHint()) {
                isVisible = true;
                onVisible();
            } else {
                isVisible = false;
                onInVisible();
            }
        }
    }

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

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    public void showAlert(int res) {
        showAlert(getString(res));
    }

    public void showAlert(String res) {
      if (getActivity() instanceof BaseActivity) {
        ((BaseActivity) getActivity()).showAlert(res);
        }
    }

    protected void onInVisible() {
    }

    @Override public void onDestroyView() {
        getChildFragmentManager().unregisterFragmentLifecycleCallbacks(childrenCB);
        if (getActivity() != null) {
            AppUtils.hideKeyboard(getActivity());
        }
        if (delegates.size() > 0) {
            for (int i = 0; i < delegates.size(); i++) {
                delegates.get(i).unattachView();
            }
        }
        unattachView();
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();
    }

    public void unattachView() {
        for (int i = 0; i < sps.size(); i++) {
            sps.get(i).unsubscribe();
        }
        for (int i = 0; i < observables.size(); i++) {
            RxBus.getBus().unregister(observables.get(i).first, observables.get(i).second);
        }
    }

    public String getFragmentName() {
        return "fragment";
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

    protected void stuff(Fragment fragment) {
        String tag = UUID.randomUUID().toString();
        if (fragment instanceof BaseFragment) {
            tag = ((BaseFragment) fragment).getFragmentName();
        }
        stuff(getLayoutRes(), fragment, tag, R.anim.slide_hold, R.anim.slide_hold);
    }

    protected void stuff(Fragment fragment, String tag) {
        stuff(getLayoutRes(), fragment, tag, R.anim.slide_hold, R.anim.slide_hold);
    }

    public int getLayoutRes() {
        return 0;
    }

    protected void stuff(int res, Fragment fragment, String tag, int resIn, int resOut) {
        Fragment fragment1 = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment1 != null) {
            getChildFragmentManager().beginTransaction().show(fragment1).commitAllowingStateLoss();
        } else {
            getChildFragmentManager().beginTransaction()
                .setCustomAnimations(resIn, resOut)
                .replace(res, fragment)
                .commitAllowingStateLoss();
        }
    }

  /**
   * 用语页面为空的状态
   */
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

    @Override public void onShowError(String e) {
        hideLoading();
        hideLoadingTrans();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
