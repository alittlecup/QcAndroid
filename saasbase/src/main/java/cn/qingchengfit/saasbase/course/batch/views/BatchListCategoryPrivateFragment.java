package cn.qingchengfit.saasbase.course.batch.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;
import cn.qingchengfit.saasbase.course.batch.items.BatchCateItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.DateUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 2017/11/29.
 */
@Leaf(module = "course", path = "/batch/cate/private/")
public class BatchListCategoryPrivateFragment extends IBatchListCategoryFragment {
  @Need String trainer_id;
  private QcResponsePrivateDetail.PrivateCoach mCoach;
  @Inject IPermissionModel permissionModel;
  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("私教排期");
  }

  @Override public void onRefresh() {
    RxRegiste(courseModel.qcGetPrivateCoaches(trainer_id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<QcResponsePrivateDetail>>() {
        @Override public void onNext(QcDataResponse<QcResponsePrivateDetail> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.batches != null) {
              List<AbstractFlexibleItem> datas = new ArrayList<>();
              mCoach = qcResponse.data.coach;
              if(mCoach.getAvatar() == "") {
                if(mCoach.getGender() == 0) {
                  mCoach.setAvatar("DefaultMale");
                }else if (mCoach.getGender() == 1) {
                  mCoach.setAvatar("DefaultFemale");
                }
              }
              datas.add(new BatchItem(mCoach));
              for (QcResponsePrivateDetail.PrivateBatch coach : qcResponse.data.batches) {
                try {
                  datas.add(
                    new BatchCateItem(DateUtils.getDuringFromServer(coach.from_date, coach.to_date),
                      coach.course.getName(), coach.id, coach.course.getPhoto()));
                } catch (Exception e) {
                  CrashUtils.sendCrash(e);
                }
              }
              setDatas(datas, 1);
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override public boolean onItemClick(int i) {
    if ( !permissionModel.check(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE)){
      showAlert(R.string.sorry_for_no_permission);
      return true;
    }
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item == null) return true;
    if (item instanceof BatchCateItem) {
      routeTo("/batch/edit/",
        new cn.qingchengfit.saasbase.course.batch.views.EditBatchParams().batchId(
          ((BatchCateItem) item).getId()).isPrvite(true).isStaff(true).build());
    }
    return true;
  }

  @Override public void onClickAddBatch() {
    if ( !permissionModel.check(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    Staff staff = new Staff();
    staff.id = trainer_id;
    if (mCoach != null) {
      staff.username = mCoach.username;
      staff.avatar = mCoach.avatar;
    }
    routeTo("/batch/add/", AddBatchParams.builder().mTeacher(staff).build());
  }

  @Override public void onClickCopyBatch() {
    BatchCopyCoach coach = new BatchCopyCoach();
    coach.setId(mCoach.getId());
    coach.setAvatar(mCoach.getAvatar());
    coach.setGender(mCoach.getGender());
    coach.setName(mCoach.username);
    routeTo(AppUtils.getRouterUri(getContext(), "/course/batch/copy/"),
        BatchCopyParams.builder().isPrivate(Boolean.TRUE).coach(coach).build());
  }

  @Override public void onClickFab() {
    if ( !permissionModel.check(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    Staff staff = new Staff();
    staff.id = trainer_id;
    if (mCoach != null) {
      staff.username = mCoach.username;
      staff.avatar = mCoach.avatar;
    }
    routeTo("/batch/add/", AddBatchParams.builder().mTeacher(staff).build());
  }
}
