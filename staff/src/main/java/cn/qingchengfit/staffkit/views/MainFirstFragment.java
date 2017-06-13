package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.rxbus.event.EventLoginChange;
import cn.qingchengfit.staffkit.views.gym.GymDetailFragment;
import cn.qingchengfit.staffkit.views.main.HomeFragment;
import cn.qingchengfit.staffkit.views.main.HomeUnLoginFragment;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
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
 * Created by Paper on 2017/3/1.
 */

public class MainFirstFragment extends BaseFragment {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QCDbManager qcDbManager;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        changeView();
        RxBusAdd(EventLoginChange.class).subscribe(new Action1<EventLoginChange>() {
            @Override public void call(EventLoginChange eventLoginChange) {
                changeView();
            }
        });
        return view;
    }

    void changeView() {
        if (loginStatus.isLogined()) {
            RxRegiste(QCDbManager.getAllCoachService()
                .observeOn(AndroidSchedulers.mainThread())
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<List<CoachService>>() {
                    @Override public void call(List<CoachService> coachServices) {
                        if (coachServices == null || coachServices.size() == 0) {
                            getChildFragmentManager().beginTransaction()
                                .replace(R.id.course_batch_frag, new HomeUnLoginFragment())
                                .commitAllowingStateLoss();
                        } else if (coachServices.size() == 1) {
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

                            getChildFragmentManager().beginTransaction()
                                .replace(R.id.course_batch_frag, new HomeFragment())
                                .commitAllowingStateLoss();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                }));
        } else {
            getChildFragmentManager().beginTransaction()
                .replace(R.id.course_batch_frag, new HomeUnLoginFragment())
                .commitAllowingStateLoss();
        }
    }

    @Override public String getFragmentName() {
        return MainFirstFragment.class.getName();
    }
}
