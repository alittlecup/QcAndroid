package com.qingchengfit.fitcoach.fragment.unlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.schedule.MainScheduleFragment;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/5/15.
 * 专门用来处理首页当前状态
 */
public class UnLoginHomeFragment extends BaseFragment {

    @Inject LoginStatus loginStatus;
    @Inject RepoCoachServiceImpl repoCoachService;

    MainScheduleFragment mainScheduleFragment;
    UnLoginScheduleAdFragment homeBannerFragment;
    private Observable<List<CoachService>> spGetService;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlogin_home, container, false);
        mainScheduleFragment = new MainScheduleFragment();
        homeBannerFragment = new UnLoginScheduleAdFragment();
        RxBusAdd(EventLoginChange.class).subscribe(eventLoginChange -> changeLogin());
        changeLogin();
        return view;
    }

    @Override protected void onInVisible() {
        super.onInVisible();
        if (mainScheduleFragment.isVisible()){
            mainScheduleFragment.setInvisible();
        }
    }

    @Override protected void onVisible() {
        super.onVisible();
        if (mainScheduleFragment.isVisible()){
            mainScheduleFragment.setVisible();
        }
    }

    private void changeLogin(){
        if (loginStatus.isLogined()) {
            //已登录
            //if (spGetService == null) {
                spGetService = repoCoachService.readAllServices();
                RxRegiste(spGetService.observeOn(AndroidSchedulers.mainThread()).subscribe(coachServices -> {
                    if (coachServices.size() == 0) {
                        //无场馆
                        stuff(homeBannerFragment, null);
                    } else {
                        //有场馆
                        stuff(mainScheduleFragment);
                    }
                }));
            //}
        } else {
            //未登录
            stuff(homeBannerFragment, null);
        }
    }

    @Override protected void lazyLoad() {
        super.lazyLoad();
    }

    @Override public int getLayoutRes() {
        return R.id.frag_home;
    }

    @Override public String getFragmentName() {
        return UnLoginHomeFragment.class.getName();
    }
}
