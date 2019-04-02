package cn.qingchengfit.staffkit.debug;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import cn.qingchengfit.staffkit.App;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/18.
 */
public class LogView {


    public LogView(App app) {
        Flowable.just(1)
            .subscribe(new Consumer<Integer>() {
                @Override public void accept(Integer integer) throws Exception {

                }
            }, new Consumer<Throwable>() {
                @Override public void accept(Throwable throwable) throws Exception {

                }
            });
    }

    public void setLogview(final Context context, @LayoutRes int layoutResID) {
        ((Activity) context).setContentView(LayoutInflater.from(context).inflate(layoutResID, null));
        //        DrawerLayout drawerLayout = new DrawerLayout(context);
        //        final LogAdapter adapter = new LogAdapter(context);
        //        ListView listView = new ListView(context);
        //        listView.setBackgroundResource(R.color.white);
        //        listView.setAdapter(adapter);
        //        View view = LayoutInflater.from(context).inflate(layoutResID, null);
        //        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(MeasureUtils.dpToPx(300f, context.getResources()), ViewGroup.LayoutParams.MATCH_PARENT);
        //        layoutParams.gravity = Gravity.START;
        //        drawerLayout.addView(view);
        //        drawerLayout.addView(listView, layoutParams);
        //        ((Activity)context).setContentView(drawerLayout);
        //        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
        //            @Override
        //            public void onDrawerSlide(View drawerView, float slideOffset) {
        //
        //            }
        //
        //            @Override
        //            public void onDrawerOpened(View drawerView) {
        //                adapter.setLogs(lumberYard.bufferedLogs());
        //
        //                subscriptions = new CompositeSubscription();
        //                subscriptions.add(lumberYard.logs() //
        //                        .observeOn(AndroidSchedulers.mainThread()) //
        //                        .subscribe(adapter));
        //            }
        //
        //            @Override
        //            public void onDrawerClosed(View drawerView) {
        //
        //                subscriptions.unsubscribe();
        //            }
        //
        //            @Override
        //            public void onDrawerStateChanged(int newState) {
        //
        //            }
        //        });
    }

    public void unRegist() {

    }
}
