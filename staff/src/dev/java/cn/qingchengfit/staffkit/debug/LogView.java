package cn.qingchengfit.staffkit.debug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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

    private CompositeSubscription subscriptions;

    private LumberYard lumberYard;

    public LogView(App app) {
        this.lumberYard = new LumberYard(app);
        Timber.plant(new Timber.DebugTree());
        lumberYard.cleanUp();
        Timber.plant(lumberYard.tree());
    }

    public void setLogview(final Context context, @LayoutRes int layoutResID) {
        final DrawerLayout drawerLayout = new DrawerLayout(context);
        final LogAdapter adapter = new LogAdapter(context);
        ListView listView = new ListView(context);
        listView.setBackgroundResource(R.color.white);
        listView.setAdapter(adapter);
        View view = LayoutInflater.from(context).inflate(layoutResID, null);
        DrawerLayout.LayoutParams layoutParams =
            new DrawerLayout.LayoutParams(MeasureUtils.dpToPx(300f, context.getResources()), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.START;
        drawerLayout.addView(view);
        drawerLayout.addView(listView, layoutParams);
        ((Activity) context).setContentView(drawerLayout);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                shareText(context, drawerLayout, adapter.getItem(i).message);
                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override public void onDrawerOpened(View drawerView) {
                adapter.setLogs(lumberYard.bufferedLogs());

                subscriptions = new CompositeSubscription();
                subscriptions.add(lumberYard.logs() //
                    .observeOn(AndroidSchedulers.mainThread()) //
                    .subscribe(adapter));
            }

            @Override public void onDrawerClosed(View drawerView) {

                subscriptions.unsubscribe();
            }

            @Override public void onDrawerStateChanged(int newState) {

            }
        });
    }

    //分享文字
    public void shareText(Context context, View view, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //        ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //        shareIntent.setComponent(comp);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        shareIntent.setType("image/*");
        shareIntent.setType("text/*");
        //        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://cn.qingchengfit.staffkit/" + R.mipmap.ic_launcher));
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    public void unRegist() {

    }
}
