package cn.qingchengfit.di;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import cn.qingchengfit.RxBus;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

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
 * Created by Paper on 16/5/13 2016.
 */
public class BasePresenter implements Presenter {


    CompositeSubscription sps ;
    private List<Pair<String, Observable>> observables = new ArrayList<>();

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
    }

    @Override public void onNewSps() {
        sps = new CompositeSubscription();
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @CallSuper @Override public void unattachView() {
        sps.unsubscribe();
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
}
