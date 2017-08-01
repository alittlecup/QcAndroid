package cn.qingchengfit.staffkit.views.notification.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventLatestNoti;
import cn.qingchengfit.staffkit.views.adapter.NotiFragmentAdater;
import cn.qingchengfit.staffkit.views.custom.PagerSlidingTabStrip;
import cn.qingchengfit.staffkit.views.notification.page.NotificationFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
 * Created by Paper on 16/9/21.
 */
public class NotificationMainFragment extends BaseFragment {

    @BindView(R.id.strip) PagerSlidingTabStrip strip;
    @BindView(R.id.viewpager) ViewPager viewpager;

    private long mlatestNotitime = 0l;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        List<Fragment> fs = new ArrayList<>();
        fs.add(NotificationFragment.newInstance(0));
        fs.add(NotificationFragment.newInstance(1));

        NotiFragmentAdater adater = new NotiFragmentAdater(getChildFragmentManager(), fs);
        viewpager.setAdapter(adater);
        strip.setViewPager(viewpager);
        mlatestNotitime = 0;
        RxBusAdd(EventLatestNoti.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EventLatestNoti>() {
            @Override public void call(EventLatestNoti eventLatestNoti) {
                strip.notifyDataSetChanged();
                if (eventLatestNoti.time > mlatestNotitime && viewpager != null) {
                    try {
                        viewpager.setCurrentItem(eventLatestNoti.pos);
                    } catch (Exception e) {

                    }
                }
                mlatestNotitime = eventLatestNoti.time;
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return NotificationMainFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
