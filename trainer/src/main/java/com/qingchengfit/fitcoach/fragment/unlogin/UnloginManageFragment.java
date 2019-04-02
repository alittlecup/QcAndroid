package com.qingchengfit.fitcoach.fragment.unlogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saascommon.model.GymBaseInfoAction;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.LazyloadFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.manage.Manage2Fragment;
import com.trello.rxlifecycle.android.FragmentEvent;
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
 * Created by Paper on 2017/5/17.
 */
public class UnloginManageFragment extends LazyloadFragment {
  Manage2Fragment manageFragment;
  HomeBannerFragment homeBannerFragment;
  @Inject LoginStatus loginStatus;
  @Inject RepoCoachServiceImpl repoCoachService;
  @Inject GymBaseInfoAction gymBaseInfoAction;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    manageFragment = new Manage2Fragment();
    homeBannerFragment = new HomeBannerFragment();
    RxBus.getBus()
        .register(EventLoginChange.class)
        .onBackpressureDrop()
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .compose(bindToLifecycle())
        .compose(doWhen(FragmentEvent.CREATE_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventLoginChange>() {
          @Override public void onNext(EventLoginChange eventLoginChange) {
            changeView();
          }
        });
    getChildFragmentManager().beginTransaction()
        .add(getLayoutRes(), manageFragment)
        .add(getLayoutRes(), homeBannerFragment)
        .hide(manageFragment)
        .hide(homeBannerFragment)
        .commitAllowingStateLoss();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_unlogin_home, container, false);
    changeView();
    return view;
  }
  @Override protected void onVisible() {
    changeView();
  }

  public void changeView() {
    if (loginStatus.isLogined()) {
      RxRegiste(repoCoachService.readAllServices()
          .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
          .subscribe(coachServices -> {
            if (coachServices.size() == 0) {
              //无场馆
              getChildFragmentManager().beginTransaction()
                  .show(homeBannerFragment)
                  .hide(manageFragment)
                  .commitAllowingStateLoss();
            } else {
              //有场馆
              getChildFragmentManager().beginTransaction()
                  .show(manageFragment)
                  .hide(homeBannerFragment)
                  .commitAllowingStateLoss();
            }
          }, new HttpThrowable()));
    } else {
      getChildFragmentManager().beginTransaction()
          .show(homeBannerFragment)
          .hide(manageFragment)
          .commitAllowingStateLoss();
    }
  }

  @Override public int getLayoutRes() {
    return R.id.frag_home;
  }

  @Override public String getFragmentName() {
    return UnloginManageFragment.class.getName();
  }
}
