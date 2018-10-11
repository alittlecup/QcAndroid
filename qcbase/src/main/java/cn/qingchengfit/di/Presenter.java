package cn.qingchengfit.di;

import android.content.Context;
import android.content.Intent;

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
 * Created by Paper on 15/11/18 2015.
 */
public interface Presenter {
    void onStart();

    void onStop();

    void onPause();

    void attachView(PView v);
    void attachContext(Context context);

    void onNewSps();

    void attachIncomingIntent(Intent intent);

    void onCreate();

    void unattachView();
}
