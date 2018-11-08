package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.PermissionServerUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.event.EventScheduleAction;
import com.qingchengfit.fitcoach.fragment.manage.ChooseGymDialogFragment;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.items.GymItem;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2016/12/3.
 */
@FragmentWithArgs public class ChooseGymForPermissionFragment extends ChooseGymDialogFragment {
    @Arg int action;
    @Arg @Nullable CoachService mCoachService;
    @Arg @Nullable int from;
    private Subscription sp;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public void refresh() {
        String key = PermissionServerUtils.GROUP_ORDER_CAN_WRITE;
        switch (action) {
            case 2:
                key = PermissionServerUtils.PRIVATE_ORDER_CAN_WRITE;
                break;
            default:
                break;
        }
        Observable<QcCoachServiceResponse> s = null;
        if (action > 1) {
            s = TrainerRepository.getStaticTrainerAllApi().qcGetCoachServicePermission(App.coachid, key);
        } else {
            if (mCoachService == null) {
                s = TrainerRepository.getStaticTrainerAllApi().qcGetCoachService(App.coachid);
            } else {
                RxBus.getBus().post(new EventScheduleAction(mCoachService, action,from));
                dismiss();
                return;
            }
        }
      sp = s.observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(new Subscriber<QcCoachServiceResponse>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(QcCoachServiceResponse qcCoachServiceResponse) {
                if (qcCoachServiceResponse.status == 200) {

                    mDatas.clear();
                    List<CoachService> services = qcCoachServiceResponse.data.services;
                    if (services != null) {
                        if (services.size() == 1) {
                            RxBus.getBus().post(new EventScheduleAction(services.get(0), action,from));
                            dismiss();
                            return;
                        }
                        for (int i = 0; i < services.size(); i++) {
                            mDatas.add(new GymItem(services.get(i), !services.get(i).has_permission));
                        }
                    }
                    mAdapter.clear();
                    mAdapter.updateDataSet(mDatas);
                } else {
                    ToastUtils.showDefaultStyle(qcCoachServiceResponse.msg);
                }
            }
        });
    }

    @Override public void onDestroyView() {
        if (sp != null && !sp.isUnsubscribed()) sp.unsubscribe();
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof GymItem) {
            GymItem gymItem = (GymItem) mAdapter.getItem(position);
            RxBus.getBus().post(new EventScheduleAction(gymItem.coachService, action,from));
            dismiss();
        }
        return true;
    }
}
