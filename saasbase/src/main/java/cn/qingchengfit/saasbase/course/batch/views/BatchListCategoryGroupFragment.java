package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.items.BatchCateItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseSchedule;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseScheduleDetail;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
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
@Leaf(module = "course", path = "/batch/cate/group/") public class BatchListCategoryGroupFragment
    extends IBatchListCategoryFragment {

  @Need String course_id;
  private Course course;
  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    return view;
  }

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
              if (qcResponse.data.batches != null) {
                //qcResponse.data.batches.add(create());
                List<AbstractFlexibleItem> datas = new ArrayList<>();
                datas.add(new BatchItem(qcResponse.data.course));
                course = qcResponse.data.course;
                for (GroupCourseSchedule coach : qcResponse.data.batches) {
                  try {
                    datas.add(new BatchCateItem(
                        DateUtils.getDuringFromServer(coach.from_date, coach.to_date),
                        coach.teacher.getUsername(), coach.id,
                        TextUtils.isEmpty(coach.teacher.getAvatar()) ? (coach.teacher.gender == 0
                            ? Constants.AVATAR_COACH_FEMALE : Constants.AVATAR_COACH_MALE)
                            : coach.teacher.getAvatar()));
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

  @Override public void onClickAddBatch() {
    if (!permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    fabMutiBatch.setLabelColors(R.color.white, R.color.white, R.color.white);
    fabMutiBatch.setLabelTextColor(R.color.white);
    if (course != null) {

      routeTo("/batch/add/", null);
    }
  }

  @Override public void onClickCopyBatch() {
    CourseType courseType = new CourseType();
    courseType.id = course.id;
    courseType.name = course.name;
    routeTo(AppUtils.getRouterUri(getContext(), "/course/batch/copy/"),
        BatchCopyParams.builder().isPrivate(Boolean.FALSE).course(courseType).build());
  }

  @Override public boolean onItemClick(int i) {
    if (!permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)) {
      showAlert(R.string.sorry_for_no_permission);
      return true;
    }
    IFlexible item = commonFlexAdapter.getItem(i);
    if (item == null) return true;
    if (item instanceof BatchCateItem) {
      routeTo("/batch/edit/",
          new cn.qingchengfit.saasbase.course.batch.views.EditBatchParams().batchId(
              ((BatchCateItem) item).getId()).isPrvite(false).build());
    }
    return true;
  }

  @Override public void onClickFab() {

  }
}
