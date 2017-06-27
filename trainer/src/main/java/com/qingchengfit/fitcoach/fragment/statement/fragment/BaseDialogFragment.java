package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import butterknife.Unbinder;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.PresenterDelegate;
import cn.qingchengfit.views.activity.BaseActivity;
import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;

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
 * Created by Paper on 16/1/29 2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {
    //    abstract   void start(Fragment fragment, int requestCode);

    public Unbinder unbinder;
    List<Subscription> sps = new ArrayList<>();
    private PresenterDelegate delegate;
    private boolean isVisible;

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

    @Override public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public void RxRegiste(Subscription subscription) {
        sps.add(subscription);
    }

    protected void delegatePresenter(Presenter presenter, PView pView) {
        delegate = new PresenterDelegate(presenter);
        delegate.attachView(pView);
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

    @Override public void onDestroyView() {
        for (int i = 0; i < sps.size(); i++) {
            sps.get(i).unsubscribe();
        }
        if (delegate != null) delegate.unattachView();
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }
}
