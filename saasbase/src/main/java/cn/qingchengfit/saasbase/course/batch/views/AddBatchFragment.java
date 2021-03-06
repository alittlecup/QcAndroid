package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlan;
import cn.qingchengfit.saasbase.course.batch.items.BatchLoopItem;
import cn.qingchengfit.saasbase.course.batch.presenters.AddBatchPresenter;
import cn.qingchengfit.saasbase.course.batch.presenters.IBatchPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/4 2016.
 */
 @Leaf(module = "course", path = "/batch/add/")
public class AddBatchFragment extends SaasBaseFragment
    implements IBatchPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    BatchDetailCommonView.BatchTempleListener {

  TextView add;
  TextView toolbarTitile;
  Toolbar toolbar;

  TextView tvBatchLoopHint;
  CommonInputView starttime;
  CommonInputView endtime;
  RecyclerView recyclerview;
  LinearLayout linearLayout;
  TextView tvClearAutoBatch;
  CommonInputView civOpenTime;
  NestedScrollView scrollRoot;
  String[] arrayOpenTime;
  boolean loadTemplate = false;
  @Inject AddBatchPresenter presenter;

  /**
   * 选择准确的时间
   */
  TimeDialogWindow chooseOpenTimeDialog;
  private TimeDialogWindow pwTime;

  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {
      presenter.buildBody();
      presenter.checkBatch();
      return true;
    }
  };
  protected BatchDetailCommonView batchBaseFragment;
  private MaterialDialog exitDialg;

  private DialogList openDialog;
  private CommonFlexAdapter commonFlexAdapter;
  @Need public Staff mTeacher;
  @Need public Course mCourse;
  @Need public Boolean isGroup;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    batchBaseFragment =
        BatchDetailCommonView.newInstance(mCourse, mTeacher, "addbatch", isPrivate());
    batchBaseFragment.setListener(this);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    arrayOpenTime = getResources().getStringArray(R.array.order_open_time);
    RxBus.getBus()
        .register(BatchLoop.class)
        .compose(bindToLifecycle())
        .compose(doWhen(FragmentEvent.CREATE_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<BatchLoop>() {
          @Override public void onNext(BatchLoop batchLoop) {
            BatchLoopItem item = new BatchLoopItem(batchLoop, presenter.isPrivate());
            if (commonFlexAdapter.contains(item)) {
              int pos = commonFlexAdapter.index(item);
              ((BatchLoopItem) commonFlexAdapter.getItem(pos)).setBatchLoop(batchLoop);
              commonFlexAdapter.notifyItemChanged(pos);
            } else {
              commonFlexAdapter.addItem(item);
            }
          }
        });
  }

  private boolean isPrivate() {
    if (isGroup != null && isGroup) return false;
    return (mCourse == null || mCourse.is_private());
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_add_batch, container, false);
    add = (TextView) view.findViewById(R.id.add);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    tvBatchLoopHint = (TextView) view.findViewById(R.id.tv_batch_loop_hint);
    starttime = (CommonInputView) view.findViewById(R.id.starttime);
    endtime = (CommonInputView) view.findViewById(R.id.endtime);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    tvClearAutoBatch = (TextView) view.findViewById(R.id.tv_clear_auto_batch);
    civOpenTime = (CommonInputView) view.findViewById(R.id.civ_to_open_time);
    scrollRoot = (NestedScrollView) view.findViewById(R.id.scroll_root);
    linearLayout = view.findViewById(R.id.ll_auto_container);
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
    view.findViewById(R.id.tv_clear_auto_batch).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clearBatch();
      }
    });
    view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        addBatchLoop();
      }
    });

    delegatePresenter(presenter, this);
    presenter.setPrivate(isPrivate());

    initToolbar(toolbar);

    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withOffset(1).withBottomEdge(true));
    recyclerview.setAdapter(commonFlexAdapter);

    Calendar calendar = Calendar.getInstance();
    starttime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));
    calendar.add(Calendar.MONTH, 2);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.add(Calendar.DATE, -1);
    endtime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));
    if (savedInstanceState != null && savedInstanceState.containsKey("p")) {
      scrollRoot.scrollTo(0, savedInstanceState.getInt("p", 0));
    }
    add.setText(!presenter.isPrivate() ? "+ 新增课程时间" : "+ 新增可约时间段");
    return view;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    try {
      outState.putInt("p", scrollRoot.getScrollY());
    } catch (Exception e) {
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(presenter.isPrivate() ? "新增私教排期" : "新增团课排课");
    toolbar.inflateMenu(R.menu.menu_compelete);
    toolbar.setOnMenuItemClickListener(item -> {
      presenter.checkBatch();
      return true;
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(batchBaseFragment);
  }

  @Override public int getLayoutRes() {
    return R.id.frag_course_info;
  }

  @Override public boolean onFragmentBackPress() {
    if (exitDialg == null) {
      exitDialg = DialogUtils.instanceDelDialog(getContext(),
          presenter.isPrivate() ? "确定放弃本次排期？" : "确定放弃本次排课", (dialog, which) -> {
            dialog.dismiss();
            popBack();
          });
    }
    if (!exitDialg.isShowing()) exitDialg.show();
    return true;
  }

  @Override public String getFragmentName() {
    return AddBatchFragment.class.getName();
  }

  @Override public void onSuccess() {
    hideLoading();
    if (PreferenceUtils.getPrefBoolean(getContext(), "isFirstSettingGym", false)) {
      getActivity().finish();
      routeTo("staff", "/gym/setting/success",
          new BundleBuilder().withInt("type", presenter.isPrivate() ? 2 : 1).build());
    } else {
      popBack();
    }
  }

  @Override
  public void onTemplete(boolean isFree, boolean openOnline, int maxuer, Rule onLineRule) {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      batchBaseFragment.setOrderSutdentCount(maxuer);
      batchBaseFragment.openPayOnline(openOnline);
      if (openOnline) {
        batchBaseFragment.payOnlineRule = onLineRule;
      }
    }
    ToastUtils.showS("已自动填充排期");
    tvBatchLoopHint.setText("已根据历史信息自动填充课程时间");
    linearLayout.setVisibility(View.VISIBLE);
  }

  @Override public void onBatchDetail(BatchDetail batchDetail) {

  }

  @Override public void onResume() {
    super.onResume();
    setBackPress();
  }

  @Override public void onPause() {
    super.onPause();
    setBackPressNull();
  }

  @Override public void onLoppers(List<BatchLoop> loopers) {
    commonFlexAdapter.clear();
    for (BatchLoop looper : loopers) {
      commonFlexAdapter.addItem(new BatchLoopItem(looper, presenter.isPrivate()));
    }
  }

  @Override public String getCourseId() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      return batchBaseFragment.getCourseId();
    }
    return null;
  }

  @Override public String getTrainerId() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      return batchBaseFragment.getTrainerId();
    }
    return null;
  }

  @Override public void addBatchLooper(BatchLoop loop) {

  }

  @Override public boolean supportMutiMember() {
    return batchBaseFragment != null
        && batchBaseFragment.isAdded()
        && batchBaseFragment.mutilSupportble();
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
    List<BatchLoop> batchLoops = new ArrayList<>();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      if (commonFlexAdapter.getItem(i) instanceof BatchLoopItem) {
        batchLoops.add(((BatchLoopItem) commonFlexAdapter.getItem(i)).getBatchLoop());
      }
    }
    return batchLoops;
  }

  @Override public List<Rule> getRules() {
    return batchBaseFragment.getRules();
  }

  @Override public ArrayList<Time_repeat> getTimeRepeats() {
    return null;
  }

  @Override public int suportMemberNum() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      return batchBaseFragment.getOrderStudentCount();
    }
    return 8;
  }

  @Override public boolean needPay() {
    return batchBaseFragment != null && batchBaseFragment.isAdded() && batchBaseFragment.needPay();
  }

  @Override public int isStaffAsTeather() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      return batchBaseFragment.asTeacher;
    } else {
      return -1;
    }
  }

  @Override public String getWorkoutPlanID() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      WorkoutPlan value = batchBaseFragment.mViewModel.selectWorkPlan.getValue();
      if(value!=null){
        return value.getId();
      }
      return "";
    } else {
      return "";
    }
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
   * 清除自动填充排期
   */
  public void clearBatch() {
    commonFlexAdapter.clear();
    tvBatchLoopHint.setText(presenter.isPrivate() ? "课程周期" : "课程周期");
    linearLayout.setVisibility(View.GONE);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof BatchLoopItem) {
      routeTo("/batch/loop/edit/",
          new cn.qingchengfit.saasbase.course.batch.views.EditBatchLoopParams().batchLoop(
              ((BatchLoopItem) item).getBatchLoop())
              .isPrivate(mCourse == null || mCourse.is_private)
              .originBatchLoop(getCurBatchLoop())
              .courseLength(mCourse == null ? 0 : mCourse.getLength())
              .slice(10)
              .build());
    }
    return true;
  }

  protected ArrayList<BatchLoop> getCurBatchLoop() {
    ArrayList<BatchLoop> batchLoops = new ArrayList<>();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof BatchLoopItem) {
        batchLoops.add(((BatchLoopItem) item).getBatchLoop());
      }
    }
    return batchLoops;
  }

  /**
   * 添加课程周期
   */
  public void addBatchLoop() {
    routeTo("/batch/loop/add/", AddBatchLoopParams.builder()
        .courseLength((presenter.isPrivate() || mCourse == null) ? 0 : mCourse.getLength())
        .slice(10)
        .isPrivate(mCourse == null || mCourse.is_private)
        .originBatchLoop(getCurBatchLoop())
        .build());
  }

  @Override public void onBatchTemple() {
    if (!loadTemplate
        && batchBaseFragment != null
        && batchBaseFragment.isAdded()
        && batchBaseFragment.getRules().size() == 0
        && commonFlexAdapter.getItemCount() == 0) {
      presenter.getBatchTemplete(presenter.isPrivate(), batchBaseFragment.getTrainerId(),
          batchBaseFragment.getCourseId());
      loadTemplate = true;
    }
  }
}
