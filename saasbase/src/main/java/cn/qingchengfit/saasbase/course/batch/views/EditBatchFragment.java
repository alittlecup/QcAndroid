package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchEditPresenter;
import cn.qingchengfit.saasbase.items.CmLRTxtItem;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePeriodChooser;
import com.bigkoo.pickerview.TimePopupWindow;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * Created by Paper on 2017/9/22.
 */
@Leaf(module = "course", path = "/batch/edit/")
public class EditBatchFragment extends BaseFragment implements BatchEditPresenter.MVPView,
  FlexibleAdapter.OnItemClickListener{

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.frag_course_info) FrameLayout fragCourseInfo;
  @BindView(R2.id.tv_batch_loop_hint) TextView tvBatchLoopHint;
  @BindView(R2.id.tv_clear_auto_batch) TextView tvClearAutoBatch;
  @BindView(R2.id.recyclerview) RecyclerView recyclerview;
  @BindView(R2.id.btn_all_schedule) CompatTextView btnAllSchedule;
  @BindView(R2.id.btn_del) Button btnDel;

  @Inject BatchEditPresenter presenter;
  CommonFlexAdapter commonFlexAdapter;
  @Need String batchId;

  private BatchDetailCommonView batchBaseFragment;
  private TimeDialogWindow timeWindow;
  private TimePeriodChooser timeDialogWindow;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(),this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater,container,savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_edit_batch, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    presenter.setBatchId(batchId);

    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryData();
  }

  @Override public String getFragmentName() {
    return EditBatchFragment.class.getName();
  }


  /**
   * 查看所有排期批次
   */
  @OnClick(R2.id.btn_all_schedule) public void onBtnAllScheduleClicked() {
    routeTo("/batch/schedule/list/",new cn.qingchengfit.saasbase.course.batch.views.BatchScheduleListParams()
      .batchId(presenter.getBatchId())
      .build());
  }


  /**
   * 删除当前批次
   */
  @OnClick(R2.id.btn_del) public void onBtnDelClicked() {
    presenter.delBatch();
  }

  @Override public void onBatchDetail(BatchDetail batchDetail) {
    if (batchBaseFragment == null)
      batchBaseFragment = BatchDetailCommonView.newInstance(batchDetail.course, batchDetail.teacher);
    else {
      batchBaseFragment.setTrainer(batchDetail.teacher);
      batchBaseFragment.setCourse(batchDetail.course);
    }
    if (!batchBaseFragment.isAdded())
      stuff(R.id.frag_course_info,batchBaseFragment);
    if (batchDetail.time_repeats != null){
      List<CmLRTxtItem> items = new ArrayList<>();
      for (Time_repeat time_repeat : batchDetail.time_repeats) {
        time_repeat.setPrivate(batchDetail.course.is_private);
        items.add(new CmLRTxtItem(time_repeat));
      }
      commonFlexAdapter.updateDataSet(items);
    }
  }


  /**
   *
   * 点击周几上课时间，以便
   */
  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CmLRTxtItem){
      //根据课程类型 弹窗-修改时间
      if (!presenter.isPrivate()) {
        if (timeWindow == null) {
          timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
        }
        timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
          @Override public void onTimeSelect(Date date) {
            if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
              Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
              tr.setStart(DateUtils.getTimeHHMM(date));
              ((CmLRTxtItem) item).setCmLRTxt(tr);
              commonFlexAdapter.notifyItemChanged(position);
            }
          }
        });
        Date d = new Date();
        if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
          Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
          d = DateUtils.getDateFromHHmm(tr.getStart());
        }
        timeWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
      } else {
        if (timeDialogWindow == null) {
          timeDialogWindow =
            new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
        }
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
          @Override public void onTimeSelect(Date start, Date end) {
            if (start.getTime() >= end.getTime()) {
              ToastUtils.showDefaultStyle("开始时间不能小于结束时间");
              return;
            }
            if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
              Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
              tr.setStart(DateUtils.getTimeHHMM(start));
              tr.setEnd(DateUtils.getTimeHHMM(end));
              commonFlexAdapter.notifyItemChanged(position);
            }
          }
        });
        Date s = new Date();
        Date e = new Date();
        if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
          Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
          s = DateUtils.getDateFromHHmm(tr.getStart());
          e = DateUtils.getDateFromHHmm(tr.getEnd());
        }
        timeDialogWindow.setTime(s, e);
        timeDialogWindow.showAtLocation();
      }
    }
    return true;
  }



}
