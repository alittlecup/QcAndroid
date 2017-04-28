package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import cn.qingchengfit.utils.AppUtils;
import com.anbillon.qcmvplib.PView;
import com.anbillon.qcmvplib.Presenter;
import com.anbillon.qcmvplib.PresenterDelegate;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import dagger.android.support.AndroidSupportInjection;
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
    public Unbinder unbinder;
    private List<PresenterDelegate> delegates = new ArrayList<>();

    @Override public void onAttach(Context context) {
        try{
            AndroidSupportInjection.inject(this);
        }catch (Exception e){

        }
        super.onAttach(context);
    }

    protected void delegatePresenter(Presenter presenter, PView pView) {
        PresenterDelegate delegate = new PresenterDelegate(presenter);
        delegate.attachView(pView);
        delegates.add(delegate);
    }

    public void initToolbar(@NonNull Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
        if (delegates.size() > 0) {
            for (int i = 0; i < delegates.size(); i++) {
                delegates.get(i).unattachView();
            }
        }
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
