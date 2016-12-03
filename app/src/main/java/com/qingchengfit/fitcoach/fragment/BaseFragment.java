package com.qingchengfit.fitcoach.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.AppUtils;
import com.anbillon.qcmvplib.PView;
import com.anbillon.qcmvplib.Presenter;
import com.anbillon.qcmvplib.PresenterDelegate;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.RxBus;
import java.util.ArrayList;
import java.util.List;
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
public abstract class BaseFragment extends Fragment {

    // 标志位，标志已经初始化完成
    protected boolean isVisible;
    boolean isPrepared;
    private PresenterDelegate delegate;
    public Unbinder unbinder;


    protected void delegatePresenter(Presenter presenter, PView pView) {
        delegate = new PresenterDelegate(presenter);
        delegate.attachView(pView);
    }


    public void showLoading() {
        if (getActivity() instanceof BaseAcitivity) {
            ((BaseAcitivity) getActivity()).showLoading();
        }
    }

    public void hideLoading() {
        if (getActivity() instanceof BaseAcitivity) {
            ((BaseAcitivity) getActivity()).hideLoading();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
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
       if (getActivity() instanceof BaseAcitivity){
           ((BaseAcitivity) getActivity()).showAlert(res);
       }
    }

    protected void onInVisible() {
    }

    private List<Pair<String, Observable>> observables = new ArrayList<>();

    @Override
    public void onDestroyView() {
        if (getActivity() != null) {
            AppUtils.hideKeyboard(getActivity());
        }
        if (delegate != null)
            delegate.unattachView();
        unattachView();
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    List<Subscription> sps = new ArrayList<>();

    public void unattachView() {
        for (int i = 0; i < sps.size(); i++) {
            sps.get(i).unsubscribe();
        }
        for (int i = 0; i < observables.size(); i++) {
            RxBus.getBus().unregister(observables.get(i).first, observables.get(i).second);
        }

    }
    public String getFragmentName(){
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

}
