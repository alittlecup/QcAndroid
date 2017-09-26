package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.utils.ListUtils;
import com.anbillon.flabellum.annotations.Leaf;
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

  private BatchLoop batchLoop;

  public static EditBatchLoopFragment newInstance(boolean isPrivate,BatchLoop batchLoop) {
    Bundle args = new Bundle();
    args.putParcelable("batchloop",batchLoop);
    args.putBoolean("p",isPrivate);
    EditBatchLoopFragment fragment = new EditBatchLoopFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null){
      isPrivate = getArguments().getBoolean("p");
      batchLoop = getArguments().getParcelable("batchloop");
    }
  }

  @Override void initSelect() {
    mStart = batchLoop.dateStart;
    mEnd = batchLoop.dateEnd;
    slice = batchLoop.slice;
    isCross = batchLoop.isCross;
    for (Integer integer : batchLoop.week) {
      commonFlexAdapter.addSelection(integer-1);
    }
    commonFlexAdapter.notifyDataSetChanged();
  }

  @Override protected void confirm() {
    BatchLoop cmBean = new BatchLoop();
    cmBean.week = ListUtils.listAddNum(commonFlexAdapter.getSelectedPositions(),1);
    cmBean.dateStart = mStart;
    cmBean.dateEnd = mEnd;
    cmBean.position = batchLoop.position;
    cmBean.slice = this.slice;
    if (!isPrivate) {
      Calendar calendar = Calendar.getInstance();
      int curD = calendar.get(Calendar.DATE);
      calendar.setTime(mStart);
      calendar.add(Calendar.SECOND, courseLength);
      cmBean.dateEnd = calendar.getTime();
      if (calendar.get(Calendar.DATE) > curD)
        cmBean.isCross = true;
    }
    RxBus.getBus().post(cmBean);
  }
}
