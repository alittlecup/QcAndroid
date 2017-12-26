package cn.qingchengfit.saasbase.course.batch.views;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ListUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.Calendar;
import java.util.UUID;

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
@Leaf(module = "Course",path = "/batch/loop/add/")
public class AddBatchLoopFragment extends IBatchLoopFragment {
  @Need Object o;
  //public static AddBatchLoopFragment newInstance(boolean isPrivate,int courseLenth) {
  //  Bundle args = new Bundle();
  //  args.putBoolean("p",isPrivate);
  //  args.putInt("l",courseLenth);
  //  AddBatchLoopFragment fragment = new AddBatchLoopFragment();
  //  fragment.setArguments(args);
  //  return fragment;
  //}
  //
  //@Override public void onCreate(@Nullable Bundle savedInstanceState) {
  //  super.onCreate(savedInstanceState);
  //  if (getArguments() != null){
  //    isPrivate = getArguments().getBoolean("p");
  //    courseLength = getArguments().getInt("l");
  //  }
  //}


  @Override void initSelect() {
    mStart = DateUtils.getDateFromHHmm("8:00");
    if (isPrivate){
      mEnd = DateUtils.getDateFromHHmm("21:00");
      slice = 5*60;
    }else {
      Calendar c = Calendar.getInstance();
      int curD = c.get(Calendar.DATE);
      c.add(Calendar.SECOND,courseLength);
      if (c.get(Calendar.DATE) > curD)
        isCross = true;
      mEnd = c.getTime();
    }
    commonFlexAdapter.selectAll(R.layout.item_cm_bottom_list_choose);
  }

  @Override protected void confirm() {
    BatchLoop cmBean = new BatchLoop();
    cmBean.id = UUID.randomUUID().toString();
    cmBean.week = ListUtils.listAddNum(commonFlexAdapter.getSelectedPositions(),1);
    cmBean.dateStart = mStart;
    cmBean.dateEnd = mEnd;
    cmBean.position = -1;
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
    if (originBatchLoop != null && !BatchLoop.CheckCmBean(originBatchLoop,cmBean)){
      showAlert("与已有周期时间冲突");
      return;
    }
    RxBus.getBus().post(cmBean);
    popBack();
  }
}
