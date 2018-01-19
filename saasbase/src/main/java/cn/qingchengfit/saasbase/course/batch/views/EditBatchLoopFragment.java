package cn.qingchengfit.saasbase.course.batch.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.utils.ListUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.Calendar;

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
 * Created by Paper on 2017/9/21.
 */
@Leaf(module = "Course",path = "/batch/loop/edit/")
public class EditBatchLoopFragment extends IBatchLoopFragment {

  @Need BatchLoop batchLoop;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(isPrivate==null || isPrivate?"编辑开放时间":"编辑周期");
  }

  @Override void initSelect() {
    mStart = batchLoop.dateStart;
    mEnd = batchLoop.dateEnd;
    slice = batchLoop.slice;
    isCross = batchLoop.isCross;
    civOrderInterval.setContent(slice/60+"分钟");
    for (Integer integer : batchLoop.week) {
      commonFlexAdapter.addSelection(integer-1);
    }
    commonFlexAdapter.notifyDataSetChanged();
  }


  @Override protected void confirm() {
    BatchLoop cmBean = new BatchLoop();
    cmBean.id = batchLoop.id;
    cmBean.week = ListUtils.listAddNum(commonFlexAdapter.getSelectedPositions(),1);
    cmBean.dateStart = mStart;
    cmBean.dateEnd = mEnd;
    cmBean.position = batchLoop.position;
    cmBean.slice = this.slice;
    cmBean.isCross = isCross;
    if (!isPrivate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(mStart);
      int curD = calendar.get(Calendar.DATE);
      calendar.add(Calendar.SECOND, courseLength);
      cmBean.dateEnd = calendar.getTime();
      if (calendar.get(Calendar.DATE) > curD)
        cmBean.isCross = true;
    }
    if (originBatchLoop != null && !BatchLoop.CheckCmBean(originBatchLoop,cmBean)){
      showAlert("与已有周期时间冲突");
      return;
    }
    RxBus.getBus().post(cmBean);
    popBack();
  }
}
