package com.qingchengfit.fitcoach.fragment.unlogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.views.fragments.LazyloadFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.manage.ManageFragment;
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
    ManageFragment manageFragment;
    HomeBannerFragment homeBannerFragment;
    @Inject LoginStatus loginStatus;
    @Inject RepoCoachServiceImpl repoCoachService;
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageFragment = new ManageFragment();
        homeBannerFragment = new HomeBannerFragment();
    }

    @Override protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlogin_home, container, false);
        RxBusAdd(EventLoginChange.class).delay(500, TimeUnit.MILLISECONDS).subscribe(eventLoginChange -> changeView());
        return view;
    }

    @Override protected void onVisible() {
        changeView();
    }


    public void changeView() {
        if (loginStatus.isLogined()) {
            RxRegiste(repoCoachService.readAllServices().observeOn(AndroidSchedulers.mainThread()).subscribe(coachServices -> {
                if (coachServices.size() == 0) {
                    //无场馆
                    stuff(homeBannerFragment, null);
                } else {
                    //有场馆
                    stuff(manageFragment);
                }
            }));

        } else {
            stuff(homeBannerFragment, null);
        }
    }

    @Override public int getLayoutRes() {
        return R.id.frag_home;
    }

    @Override public String getFragmentName() {
        return UnloginManageFragment.class.getName();
    }
}
