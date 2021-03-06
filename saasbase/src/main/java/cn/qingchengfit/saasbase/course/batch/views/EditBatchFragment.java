package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlan;
import cn.qingchengfit.saasbase.course.batch.items.CmLRTxt90Item;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchEditPresenter;
import cn.qingchengfit.saasbase.course.batch.presenters.IBatchPresenter;
import cn.qingchengfit.saasbase.items.CmLRTxtItem;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePeriodChooser;
import com.bigkoo.pickerview.TimePopupWindow;
import com.jakewharton.rxbinding.view.RxMenuItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
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
@Leaf(module = "course", path = "/batch/edit/") public class EditBatchFragment
    extends SaasBaseFragment
    implements IBatchPresenter.MVPView, FlexibleAdapter.OnItemClickListener {

  Toolbar toolbar;
  TextView toolbarTitle;
  FrameLayout fragCourseInfo;
  TextView tvBatchLoopHint;
  TextView tvClearAutoBatch;
  RecyclerView recyclerview;
  CompatTextView btnAllSchedule;
  CommonInputView starttime;
  CommonInputView endtime;
  CommonInputView civOpenTime;
  Button btnDel;

  @Inject BatchEditPresenter presenter;
  CommonFlexAdapter commonFlexAdapter;
  @Need public String batchId;
  @Need Staff staff;
  @Need public Boolean isPrvite = false;
  @Need public Boolean isStaff = false;

  private BatchDetailCommonView batchBaseFragment;
  private TimeDialogWindow timeWindow;
  private TimePeriodChooser timeDialogWindow;
  private TimeDialogWindow pwTime;
  /**
   * 选择准确的时间
   */
  TimeDialogWindow chooseOpenTimeDialog;
  private DialogList openDialog;
  private String[] arrayOpenTime;
  private int courseLength;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    arrayOpenTime = getResources().getStringArray(R.array.order_open_time);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_edit_batch, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    fragCourseInfo = (FrameLayout) view.findViewById(R.id.frag_course_info);
    tvBatchLoopHint = (TextView) view.findViewById(R.id.tv_batch_loop_hint);
    tvClearAutoBatch = (TextView) view.findViewById(R.id.tv_clear_auto_batch);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    btnAllSchedule = (CompatTextView) view.findViewById(R.id.btn_all_schedule);
    starttime = (CommonInputView) view.findViewById(R.id.starttime);
    endtime = (CommonInputView) view.findViewById(R.id.endtime);
    civOpenTime = (CommonInputView) view.findViewById(R.id.civ_to_open_time);
    btnDel = (Button) view.findViewById(R.id.btn_del);
    view.findViewById(R.id.btn_all_schedule).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnAllScheduleClicked();
      }
    });
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnDelClicked();
      }
    });
    view.findViewById(R.id.starttime).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onStartTime();
      }
    });
    view.findViewById(R.id.endtime).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onEndTime();
      }
    });
    view.findViewById(R.id.civ_to_open_time).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onOpenTime();
      }
    });

    delegatePresenter(presenter, this);
    presenter.setBatchId(batchId);
    presenter.setPrivate(isPrvite);
    initView();
    initToolbar(toolbar);
    RxBusAdd(Time_repeat.class).onBackpressureDrop().subscribe(new BusSubscribe<Time_repeat>() {
      @Override public void onNext(Time_repeat time_repeat) {
        commonFlexAdapter.updateItem(new CmLRTxt90Item(time_repeat));
      }
    });

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("详情");
    toolbar.inflateMenu(R.menu.menu_save);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {
            presenter.buildBody();
            presenter.arrangeBatch();
          }
        });
  }

  private void initView() {
    recyclerview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withOffset(1).withBottomEdge(true));
    recyclerview.setNestedScrollingEnabled(false);
    recyclerview.setItemAnimator(new FadeInUpItemAnimator());
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerview.setAdapter(commonFlexAdapter);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryData();
  }

  @Override public String getFragmentName() {
    return EditBatchFragment.class.getName();
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof BatchDetailCommonView && !hadSetData) {
      inflateBatchInfo(presenter.getBatchDetail());
    }
  }

  /**
   * 查看所有排期批次
   */
  public void onBtnAllScheduleClicked() {
    routeTo("/batch/schedule/list/",
        new cn.qingchengfit.saasbase.course.batch.views.BatchScheduleListParams().batchId(
            presenter.getBatchId()).isPrivate(presenter.isPrivate()).build());
  }

  @Override public boolean onFragmentBackPress() {
    DialogUtils.instanceDelDialog(getContext(), "是否放弃本次更改？",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            popBack();
          }
        }).show();
    return true;
  }

  /**
   * 删除当前批次
   */
  public void onBtnDelClicked() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认删除当前排课？", (dialog, which) -> {
      presenter.delBatch();
    }).show();
  }

  @Override public void onSuccess() {

  }

  @Override public void onTemplete(boolean isFree, boolean oepnOnlie, int maxuer, Rule onLineRule) {

  }

  @Override public void onBatchDetail(BatchDetail batchDetail) {
    if (batchDetail == null) {
      return;
    }
    courseLength = batchDetail.course.length;
    if (batchBaseFragment == null) {
      batchBaseFragment =
          BatchDetailCommonView.newInstance(batchDetail.course, batchDetail.teacher, "editbatch",
              isPrvite, isStaff);
    } else {
      batchBaseFragment.setTrainer(batchDetail.teacher);
      batchBaseFragment.setCourse(batchDetail.course);
    }
    if (!batchBaseFragment.isAdded()) {
      stuff(R.id.frag_course_info, batchBaseFragment);
    } else {
      inflateBatchInfo(batchDetail);
    }

    if (batchDetail.time_repeats != null) {
      List<IFlexible> items = new ArrayList<>();
      for (Time_repeat time_repeat : batchDetail.time_repeats) {
        time_repeat.setPrivate(presenter.isPrivate());
        items.add(generateRepeatItem(time_repeat));
      }
      commonFlexAdapter.updateDataSet(items);
    }
    starttime.setContent(DateUtils.getYYYYMMDDfromServer(batchDetail.from_date));
    endtime.setContent(DateUtils.getYYYYMMDDfromServer(batchDetail.to_date));
    onOpenRule(batchDetail.open_rule);
  }

  public void onOpenRule(BatchOpenRule rule) {
    if (rule != null) {
      if (rule.type == 1) {
        civOpenTime.setContent(arrayOpenTime[0]);
      } else if (rule.type == 2) {
        civOpenTime.setContent(
            DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(rule.open_datetime)));
      } else if (rule.type == 3) {
        civOpenTime.setContent("提前" + rule.advance_hours + "小时开放");
      }
    }
  }

  private IFlexible generateRepeatItem(Time_repeat tr) {
    if (presenter.isPrivate()) {
      return new CmLRTxt90Item(tr);
    } else {
      return new CmLRTxtItem(tr);
    }
  }

  private boolean hadSetData;

  private void inflateBatchInfo(BatchDetail batchDetail) {
    batchBaseFragment.setOrderSutdentCount(batchDetail.max_users);
    batchBaseFragment.setMutlSupport(batchDetail.supportMulti());
    batchBaseFragment.openPay(!batchDetail.is_free);
    batchBaseFragment.setSpace(batchDetail.getSpaces());
    batchBaseFragment.setRules(batchDetail.rule,
        (ArrayList<CardTplBatchShip>) batchDetail.card_tpls);
    if (!isPrvite) {
      batchBaseFragment.setWorkOutPlan(batchDetail.workoutPlan);
    }
    hadSetData = true;
  }

  @Override public void onLoppers(List<BatchLoop> loopers) {

  }

  @Override public void addBatchLooper(BatchLoop loop) {

  }

  @Override public String getCourseId() {
    return batchBaseFragment.getCourseId();
  }

  @Override public String getTrainerId() {
    return batchBaseFragment.getTrainerId();
  }

  @Override public boolean supportMutiMember() {
    return batchBaseFragment.mutilSupportble();
  }

  @Override public String getStart() {
    return starttime.getContent();
  }

  @Override public String getEnd() {
    return endtime.getContent();
  }

  @Override public List<String> getSupportSpace() {
    return batchBaseFragment.getSupportSpace();
  }

  @Override public List<BatchLoop> getBatchLoops() {
    return null;
  }

  @Override public List<Rule> getRules() {
    return batchBaseFragment.getRules();
  }

  @Override public ArrayList<Time_repeat> getTimeRepeats() {
    ArrayList<Time_repeat> tp = new ArrayList<>();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof CmLRTxtItem) {
        tp.add(((Time_repeat) ((CmLRTxtItem) item).getData()));
      }
    }
    return tp;
  }

  @Override public int suportMemberNum() {
    return batchBaseFragment.getOrderStudentCount();
  }

  @Override public boolean needPay() {
    return batchBaseFragment.needPay();
  }

  @Override public int isStaffAsTeather() {
    return -1;
  }

  @Override public String getWorkoutPlanID() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      WorkoutPlan value = batchBaseFragment.mViewModel.selectWorkPlan.getValue();
      if (value != null) {
        return value.getId();
      }
    }
    return "";
  }

  /**
   * 选择开始时间
   */
  public void onStartTime() {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
        Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        starttime.setContent(DateUtils.Date2YYYYMMDD(date));
        if (endtime.isEmpty()) endtime.setContent(DateUtils.getEndDayOfMonthNew(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, starttime.isEmpty() ? new Date()
        : DateUtils.formatDateFromYYYYMMDD(starttime.getContent()));
  }

  /**
   * 选择结束时间
   */
  public void onEndTime() {
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
        Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()) {
          Toast.makeText(getContext(), R.string.alert_endtime_greater_starttime, Toast.LENGTH_SHORT)
              .show();
          return;
        }
        if (date.getTime() - DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()
            > 366 * DateUtils.DAY_TIME) {
          Toast.makeText(getContext(), R.string.alert_batch_greater_three_month, Toast.LENGTH_SHORT)
              .show();
          return;
        }
        endtime.setContent(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0,
        endtime.isEmpty() ? new Date() : DateUtils.formatDateFromYYYYMMDD(endtime.getContent()));
  }

  public void onOpenTime() {
    if (openDialog == null) {
      openDialog = DialogList.builder(getContext())
          .list(arrayOpenTime, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if (position == 0) {
                presenter.setOpenRuleType(1);
                civOpenTime.setContent(arrayOpenTime[0]);
              } else if (position == 1) {
                chooseOpenTime();
              } else {
                chooseAheadOfHour();
              }
            }
          });
    }
    openDialog.show();
  }

  /**
   * 选择开放时间
   */
  public void chooseOpenTime() {
    if (chooseOpenTimeDialog == null) {
      chooseOpenTimeDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.ALL);
      chooseOpenTimeDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(date));
          presenter.setOpenRuleType(2);
          presenter.setOpenRuleTime(DateUtils.Date2YYYYMMDDHHmmss(date), null);
        }
      });
    }
    chooseOpenTimeDialog.setRange(DateUtils.getYear(new Date()) - 1,
        DateUtils.getYear(new Date()) + 1);
    Date d = new Date();
    if (!TextUtils.isEmpty(civOpenTime.getContent())) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm", Locale.CHINA);
        d = formatter.parse(civOpenTime.getContent());
      } catch (Exception e) {
        LogUtil.e(e.getMessage());
      }
    }

    chooseOpenTimeDialog.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
  }

  /**
   * 提前x小时开放预约
   */
  public void chooseAheadOfHour() {
    SimpleScrollPicker simpleScrollPicker = new SimpleScrollPicker(getContext());
    simpleScrollPicker.setLabel("小时");
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civOpenTime.setContent("提前" + pos + "小时预约");
        presenter.setOpenRuleType(3);
        presenter.setOpenRuleTime(null, pos);
      }
    });
    simpleScrollPicker.show(0, 240, 4);
  }

  /**
   * 点击周几上课时间，以便
   */
  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CmLRTxtItem) {
      //根据课程类型 弹窗-修改时间
      if (!presenter.isPrivate()) {
        //团课
        if (timeWindow == null) {
          timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
        }
        timeWindow.setOnTimeSelectListener(date -> {
          if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
            Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
            tr.setStart(DateUtils.getTimeHHMM(date));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int curD = calendar.get(Calendar.DATE);
            calendar.add(Calendar.SECOND, courseLength);
            tr.setEnd(DateUtils.getTimeHHMM(calendar.getTime()));
            if (calendar.get(Calendar.DATE) > curD) {
              tr.setIs_cross(true);
            }
            ((CmLRTxtItem) item).setCmLRTxt(tr);
            commonFlexAdapter.notifyItemChanged(position);
          }
        });
        Date d = new Date();
        if (((CmLRTxtItem) item).getData() instanceof Time_repeat) {
          Time_repeat tr = (Time_repeat) ((CmLRTxtItem) item).getData();
          d = DateUtils.getDateFromHHmm(tr.getStart());
        }
        timeWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
      } else {
        //私教
        EditPrivateOpenTimeFragment.newInstance((Time_repeat) ((CmLRTxtItem) item).getData())
            .show(getChildFragmentManager(), EditPrivateOpenTimeFragment.class.getSimpleName());
      }
    }
    return true;
  }
}
