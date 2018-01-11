package cn.qingchengfit.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.PresenterDelegate;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.R;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;
import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

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
public abstract class BaseFragment extends RxFragment
  implements BaseActivity.FragmentBackPress, CView {
  @Deprecated public FragCallBack mCallbackActivity;
  public Unbinder unbinder;
  // 标志位，标志已经初始化完成
  protected boolean isVisible;
  protected boolean isInit = false;
  protected boolean isLoading = false;
  protected boolean isLazyLoad = true;
  protected boolean isPrepared;
  //已经是否已经展示过，展示过说明是从栈中出来
  protected boolean hasShown;
  List<Subscription> sps = new ArrayList<>();
  private List<PresenterDelegate> delegates = new ArrayList<>();
  private List<Pair<String, Observable>> observables = new ArrayList<>();
  private List<Pair<String, Observable>> observablesAllLife = new ArrayList<>();

  private FragmentManager.FragmentLifecycleCallbacks childrenCB =
    new FragmentManager.FragmentLifecycleCallbacks() {

      @Override public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v,
        Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        onChildViewCreated(fm, f, v, savedInstanceState);
      }
    };

  protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
    Bundle savedInstanceState) {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  protected void doEventOnCreatView(Class classz ,Action1 action1){
    RxBus.getBus().register(classz)
      .throttleFirst(500,TimeUnit.MILLISECONDS)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(action1,throwable -> {LogUtil.e("doEventOnCreatView",throwable.toString());});
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    getChildFragmentManager().registerFragmentLifecycleCallbacks(childrenCB, false);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);
    //使fitsystem生效
    ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH) @Override
      public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        return insets.consumeSystemWindowInsets();
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return isBlockTouch();
      }
    });
  }

  /**
   * 是否拦截Fragment底层View的Touch事件
   */
  public boolean isBlockTouch() {
    return true;
  }

  @Override public void onAttach(Context context) {
    try {
      AndroidSupportInjection.inject(this);
    } catch (Exception e) {
      LogUtil.e("inject not find fragment:" + getFragmentName());
    }
    super.onAttach(context);
    if (context instanceof FragCallBack) {
      mCallbackActivity = (FragCallBack) context;
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mCallbackActivity = null;
  }

  protected void delegatePresenter(Presenter presenter, PView pView) {
    PresenterDelegate delegate = new PresenterDelegate(presenter);
    delegate.onNewSps();
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
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup  && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
        MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
    }
  }

  protected boolean isfitSystemPadding() {
    return true;
  }

  public void showLoading() {
    //if (getActivity() instanceof BaseActivity) {
    //  ((BaseActivity) getActivity()).showLoading();
    //  }
  }

  public void hideLoading() {
    //if (getActivity() instanceof BaseActivity) {
    //  ((BaseActivity) getActivity()).hideLoading();
    //  }
  }
  protected void setStatusTextColor(boolean dark){
    if (getActivity() != null && getActivity() instanceof BaseActivity){
      ((BaseActivity) getActivity()).setStatusTextColor(dark);
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

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (hidden) {
      onInVisible();
    } else {
      onVisible();
    }
  }

  @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
    if (enter && nextAnim > 0 && !hasShown) {
      Animation animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
      if (animation != null) {
        animation.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {

          }

          @Override public void onAnimationEnd(Animation animation) {
            if (isVisible()) {
              hasShown = true;
              onFinishAnimation();
            }
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

  @Override public void onDestroy() {
    for (int i = 0; i < observablesAllLife.size(); i++) {
      RxBus.getBus().unregister(observablesAllLife.get(i).first, observablesAllLife.get(i).second);
    }
    super.onDestroy();
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
    return "base_fragment";
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

  protected void stuff(int res, Fragment fragment) {
    String tag = UUID.randomUUID().toString();
    if (fragment instanceof BaseFragment) {
      tag = ((BaseFragment) fragment).getFragmentName();
    }
    stuff(res, fragment, tag, R.anim.slide_hold, R.anim.slide_hold);
  }

  public void showChild(Fragment f) {
    getChildFragmentManager().beginTransaction().show(f).commitAllowingStateLoss();
  }

  public void hideChild(Fragment f) {
    getChildFragmentManager().beginTransaction().hide(f).commitAllowingStateLoss();
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

  protected void routeTo(Fragment fragment, String tag) {
    if (getActivity() instanceof BaseActivity) {
      getActivity().getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
          R.anim.slide_right_out)
        .replace(((BaseActivity) getActivity()).getFragId(), fragment)
        .addToBackStack(tag)
        .commit();
    }
  }

  View searchRoot;

  protected void showSearch(ViewGroup tvToolbarLayout) {
    if (searchRoot != null) {
      FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);

      lp.setMargins(15, 15, 15, 15);
      searchRoot.setOnTouchListener(new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent motionEvent) {
          return true;
        }
      });
      tvToolbarLayout.addView(searchRoot, lp);
      searchRoot.requestFocus();
      AppUtils.showKeyboard(getContext(), searchRoot.findViewById(R.id.et_search));
      //searchRoot.findViewById(R.id.et_search).requestFocus();
    }
  }

  public void initSearch(final ViewGroup tvToolbarLayout, String hint) {
    initSearch(tvToolbarLayout, hint, 500);
  }

  // 初始化搜索
  public void initSearch(final ViewGroup tvToolbarLayout, String hint, long intervalD) {
    searchRoot = LayoutInflater.from(getContext()).inflate(R.layout.layout_action_search, null);
    final EditText searchEt = ((EditText) searchRoot.findViewById(R.id.et_search));
    searchEt.setHint(hint);
    RxTextView.afterTextChangeEvents(searchEt)
      .throttleLast(intervalD, TimeUnit.MILLISECONDS)
      .skip(1)
      .subscribe(new BusSubscribe<TextViewAfterTextChangeEvent>() {
        @Override public void onNext(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
          onTextSearch(textViewAfterTextChangeEvent.editable().toString());
        }
      });
    RxView.clicks(searchRoot.findViewById(R.id.btn_close))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          if (searchEt.getText().length() == 0) {
            tvToolbarLayout.removeView(searchRoot);
            tvToolbarLayout.clearFocus();
            if (getActivity() != null) AppUtils.hideKeyboard(getActivity());
          } else {
            searchEt.setText("");
          }
        }
      });
    /*
    SearchManager searchManager =
      (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
    SearchView mSearchView;

    mSearchView = (SearchView) searchItem.getActionView();
    if (mSearchView != null) {
      mSearchView.setSearchableInfo(
        searchManager.getSearchableInfo(getActivity().getComponentName()));
    }
    searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
      @Override public boolean onMenuItemActionExpand(MenuItem menuItem) {
        tvToolbar.setVisibility(View.GONE);
        //toolbar.setNavigationIcon(null);
        return true;
      }

      @Override public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        tvToolbar.setVisibility(View.VISIBLE);
        //toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
        return true;
      }
    });
    mSearchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
    mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_FULLSCREEN);
    mSearchView.setQueryHint(getString(R.string.action_search));
    mSearchView.setMaxWidth(MeasureUtils.getScreenWidth(getResources()));
    //mSearchView.setBackgroundResource(R.drawable.bg_searchview);
    //mSearchView.
    //mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getContext().getComponentName()));
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        onTextSearch(newText);
        return false;
      }
    });
    */
  }

  //每个搜索的不同实现，复写这个方法
  public void onTextSearch(String text) {

  }

  /**
   * 根据uri 跳转
   */
  protected void routeTo(Uri uri, Bundle bd) {
    routeTo(uri, bd, false);
  }

  protected void routeTo(Uri uri, Bundle bd, boolean newActivity) {
    try {
      Intent to = new Intent(Intent.ACTION_VIEW, uri);
      if (getActivity() instanceof BaseActivity) {
        if (((BaseActivity) getActivity()).getModuleName().equalsIgnoreCase(uri.getHost())
          //&& !uri.getPath().startsWith("/choose")
          ) {
          to.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
          to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
      }
      if (bd != null) {
        to.putExtras(bd);
      }
      startActivity(to);
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }

  /**
   * 时间选择器
   *
   * @param type 有哪些可选项
   * @param start 开始年份
   * @param end 结束年份
   * @param inputDate 初始时间
   * @param civ 接受结果的civ
   */
  protected void choosTime(final TimePopupWindow.Type type, int start, int end, Date inputDate,
    final CommonInputView civ) {
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).chooseTime(type, start, end, inputDate,
        new TimeDialogWindow.OnTimeSelectListener() {
          @Override public void onTimeSelect(Date date) {
            civ.setContent(DateUtils.date2TimePicker(date, type));
          }
        });
    }
  }

  protected void choosTime(final TimePopupWindow.Type type, int start, int end, Date inputDate,
      final CommonInputView civ, TimeDialogWindow.OnTimeSelectListener listener) {
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).chooseTime(type, start, end, inputDate, listener);
    }
  }

  protected void chooseTime(final CommonInputView civ) {
    choosTime(TimePopupWindow.Type.YEAR_MONTH_DAY, 0, 0, new Date(), civ);
  }

  protected void chooseTime(Date d, final CommonInputView civ) {
    choosTime(TimePopupWindow.Type.YEAR_MONTH_DAY, 0, 0, d, civ);
  }

  /**
   * 跳转当前模块
   */
  public void routeTo(String model, String path, Bundle bd) {
    String uri = model + path;
    if (!uri.startsWith("/")) uri = "/" + uri;
    if (getActivity() instanceof BaseActivity) {
      routeTo(AppUtils.getRouterUri(getContext(), uri), bd);
    }
  }

  /**
   * 跳转到其他模块
   */
  protected void routeTo(String uri, Bundle bd) {
    routeTo(uri, bd, false);
  }

  protected void routeTo(String uri, Bundle bd, boolean b) {
    if (!uri.startsWith("/")) uri = "/" + uri;
    if (getActivity() instanceof BaseActivity) {
      routeTo(Uri.parse(AppUtils.getCurAppSchema(getContext())
        + "://"
        + ((BaseActivity) getActivity()).getModuleName()
        + uri), bd, b);
    }
  }

  protected void rmAndTo(Fragment rm, Fragment fragment, String tag) {
    if (getActivity() instanceof BaseActivity) {
      getActivity().getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
          R.anim.slide_right_out)
        .remove(rm)
        .replace(((BaseActivity) getActivity()).getFragId(), fragment)
        .addToBackStack(tag)
        .commitAllowingStateLoss();
    }
  }

  protected void routeTo(Fragment fragment) {
    routeTo(fragment, null);
  }

  public final <T> Observable.Transformer<T, T> doWhen(final FragmentEvent fragmentEventDo) {
    return new Observable.Transformer<T, T>() {
      @Override public Observable<T> call(Observable<T> tObservable) {
        return tObservable.buffer(new Func0<Observable<?>>() {
          @Override public Observable<?> call() {
            return BaseFragment.this.lifecycle().filter(new Func1<FragmentEvent, Boolean>() {
              @Override public Boolean call(FragmentEvent fragmentEvent) {
                return fragmentEvent.equals(fragmentEventDo);
              }
            });
          }
        }).flatMap(new Func1<List<T>, Observable<T>>() {
          @Override public Observable<T> call(List<T> ts) {
            if (ts != null && ts.size() > 0) {
              return Observable.just(ts.get(0));
            } else {
              return Observable.just(null);
            }
          }
        }).filter(new Func1<T, Boolean>() {
          @Override public Boolean call(T t) {
            return t != null;
          }
        });
      }
    };
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

  @Override public void popBack() {
    if (getFragmentManager() != null) {
      if (!getFragmentManager().popBackStackImmediate()) {
        if (getActivity() != null) getActivity().finish();
      }
    }
  }

  @Override public void popBack(int count) {
    if (getFragmentManager() != null) {
      int stackCount = getFragmentManager().getBackStackEntryCount();
      if (count <= stackCount) {
        getFragmentManager().popBackStackImmediate(
          getFragmentManager().getBackStackEntryAt(stackCount - count).getId(), 1);
      } else {
        if (getActivity() != null) getActivity().finish();
      }
    }
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }

  @Override public void showSelectSheet(String title, List<String> strs,
    AdapterView.OnItemClickListener listener) {
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).showDialogList(title, strs, listener);
    }
  }
}
