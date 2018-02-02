package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.views.gym.GymDetailFragment;
import cn.qingchengfit.staffkit.views.main.HomeFragment;
import cn.qingchengfit.staffkit.views.main.HomeUnLoginFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

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
 * Created by Paper on 2017/3/1.
 */

public class MainFirstFragment extends BaseFragment {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QCDbManagerImpl qcDbManager;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        changeView();
        RxBusAdd(EventLoginChange.class).subscribe(eventLoginChange -> changeView(),throwable -> {});
        return view;
    }

    /**
     * 已登录 -----》无场馆
     *              单场馆
     *              多场馆
     * 未登录
     */
    void changeView() {
        if (loginStatus.isLogined()) {//登录
            RxRegiste(qcDbManager.getAllCoachService()
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .subscribe(coachServices -> {
                    if (coachServices == null || coachServices.size() == 0) {
                        //无场馆，广告页面
                        getChildFragmentManager().beginTransaction()
                          .replace(R.id.course_batch_frag, new HomeUnLoginFragment())
                          .commitAllowingStateLoss();
                    } else if (coachServices.size() == 1) {
                        //单场馆
                        gymWrapper.setCoachService(coachServices.get(0));
                        gymWrapper.setBrand(
                          new Brand.Builder().id(coachServices.get(0).brand_id()).name(coachServices.get(0).getBrand_name()).build());
                        if (getChildFragmentManager().findFragmentByTag("gymDetail") != null) {
                            getChildFragmentManager().beginTransaction()
                              .show(getChildFragmentManager().findFragmentByTag("gymDetail"))
                              .commitAllowingStateLoss();
                        } else {
                            getChildFragmentManager().beginTransaction()
                              .replace(R.id.course_batch_frag, new GymDetailFragment(), "gymDetail")
                              .commitAllowingStateLoss();
                        }
                    } else {
                        //多场馆 连锁运营页面
                        getChildFragmentManager().beginTransaction()
                          .replace(R.id.course_batch_frag, new HomeFragment())
                          .commitAllowingStateLoss();
                    }
                }, throwable -> {}));
        } else {
            getChildFragmentManager().beginTransaction()
                .replace(R.id.course_batch_frag, new HomeUnLoginFragment())
                .commitAllowingStateLoss();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return MainFirstFragment.class.getName();
    }
}
