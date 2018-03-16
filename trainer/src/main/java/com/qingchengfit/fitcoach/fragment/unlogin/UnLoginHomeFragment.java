package com.qingchengfit.fitcoach.fragment.unlogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.schedule.MainScheduleFragment;
import com.trello.rxlifecycle.android.FragmentEvent;
import io.reactivex.Flowable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
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
    private Flowable<List<CoachService>> spGetService;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainScheduleFragment = new MainScheduleFragment();
        homeBannerFragment = new UnLoginScheduleAdFragment();
        RxBus.getBus().register(EventLoginChange.class)
          .onBackpressureDrop()
          .throttleFirst(500, TimeUnit.MILLISECONDS)
          .compose(bindToLifecycle())
          .compose(doWhen(FragmentEvent.CREATE_VIEW))
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new BusSubscribe<EventLoginChange>() {
              @Override public void onNext(EventLoginChange eventLoginChange) {
                  changeLogin();
              }
          });
        getChildFragmentManager().beginTransaction()
          .add(getLayoutRes(),mainScheduleFragment)
          .add(getLayoutRes(),homeBannerFragment)
          .hide(homeBannerFragment)
          .commitAllowingStateLoss();

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlogin_home, container, false);
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        changeLogin();
    }

    @Override protected void onVisible() {
        super.onVisible();
        changeLogin();
    }

    private void changeLogin(){
        //if (!isAdded()) return;
        if (loginStatus.isLogined()) {
            //已登录
            //if (spGetService == null) {
                spGetService = repoCoachService.readAllServices();
                RxRegiste(spGetService.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).subscribe(coachServices -> {
                    if (coachServices.size() == 0) {
                        //无场馆
                        getChildFragmentManager().beginTransaction()
                          .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
                          .show(homeBannerFragment).hide(mainScheduleFragment).commitAllowingStateLoss();
                    } else {
                        //有场馆
                        getChildFragmentManager().beginTransaction()
                          .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
                          .show(mainScheduleFragment).hide(homeBannerFragment).commitAllowingStateLoss();
                    }
                }));
            //}
        } else {
            //未登录
            getChildFragmentManager().beginTransaction()
              .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
              .show(homeBannerFragment).hide(mainScheduleFragment).commitAllowingStateLoss();
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
