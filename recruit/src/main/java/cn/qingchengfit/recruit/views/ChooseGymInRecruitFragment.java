package cn.qingchengfit.recruit.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.OnePermissionWrap;
import cn.qingchengfit.saas.views.fragments.ChooseGymFragment;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
 * Created by Paper on 2017/7/4.
 */

public class ChooseGymInRecruitFragment extends ChooseGymFragment {

  @Inject QcRestRepository qcRestRepository;
  @Inject RecruitRouter recruitRouter;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择场馆");
  }

  @Override public void onViewClicked() {
    try {
      Intent toChooseBrand = new Intent(getContext().getPackageName() + ".chooseBrand");
      toChooseBrand.putExtra("trainer", true);
      startActivityForResult(toChooseBrand, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override protected void queryPermiss(final Gym gym) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryOnepermission(gym.id, "job").onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<OnePermissionWrap>>() {
          @Override public void call(QcDataResponse<OnePermissionWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.has_permission) {
                hasPermission(gym);
              } else {
                hasNoPermission();
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override protected void goEditGym(Gym gym) {
    recruitRouter.editGymInfo(gym.id);
  }
}
