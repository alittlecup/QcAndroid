package cn.qingchengfit.saasbase.course.batch.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.items.BatchCateItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseSchedule;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseScheduleDetail;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.subscribes.NetSubscribe;
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
@Leaf(module = "course",path = "/batch/cate/group/")
public class BatchListCategoryGroupFragment extends IBatchListCategoryFragment {

  @Need String course_id;
  private Course course;
  @Inject IPermissionModel permissionModel;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("团课排期");
  }

  @Override public void onRefresh() {
    RxRegiste(courseModel.qcGetGroupCourses(course_id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<GroupCourseScheduleDetail>>() {
        @Override public void onNext(QcDataResponse<GroupCourseScheduleDetail> qcResponse) {
          stopRefresh();
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.batches != null){
              List<AbstractFlexibleItem> datas = new ArrayList<>();
              datas.add(new BatchItem(qcResponse.data.course));
              course = qcResponse.data.course;
              for (GroupCourseSchedule coach : qcResponse.data.batches) {
                try {
                  datas.add(new BatchCateItem(DateUtils.getDuringFromServer(coach.from_date,coach.to_date)
                    ,coach.teacher.getUsername(),coach.id,coach.teacher.getAvatar()
                  ));
                }catch (Exception e){
                  CrashUtils.sendCrash(e);
                }
              }
              setDatas(datas,1);
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override public boolean onItemClick(int i) {
    if ( !permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)){
      showAlert(R.string.sorry_for_no_permission);
      return true;
    }
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item == null) return true;
    if (item instanceof BatchCateItem){
      routeTo("/batch/edit/",new cn.qingchengfit.saasbase.course.batch.views.EditBatchParams()
        .batchId(((BatchCateItem) item).getId())
        .isPrvite(false)
        .build()
      );
    }
    return true;
  }

  @Override public void onClickFab() {
    if ( !permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    if (course != null ) {
      routeTo("/batch/add/", AddBatchParams.builder().mCourse(course).build());
    }
  }
}