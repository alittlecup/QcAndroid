package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.items.BatchLoopItem;
import cn.qingchengfit.saasbase.course.batch.presenters.AddBatchPresenter;
import cn.qingchengfit.saasbase.course.batch.presenters.IBatchPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
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
@Leaf(module = "course", path = "/batch/add/" )
public class AddBatchFragment extends SaasBaseFragment
    implements IBatchPresenter.MVPView, FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.add) TextView add;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar) Toolbar toolbar;

  @BindView(R2.id.tv_batch_loop_hint) TextView tvBatchLoopHint;
  @BindView(R2.id.starttime) CommonInputView starttime;
  @BindView(R2.id.endtime) CommonInputView endtime;
  @BindView(R2.id.recyclerview) RecyclerView recyclerview;

  @BindView(R2.id.tv_clear_auto_batch) TextView tvClearAutoBatch;
  @BindView(R2.id.civ_to_open_time) CommonInputView civOpenTime;
  @BindArray(R2.array.order_open_time) String[] arrayOpenTime;

  @Inject AddBatchPresenter presenter;

  /**
   * 选择准确的时间
   */
  TimeDialogWindow chooseOpenTimeDialog;
  private TimeDialogWindow pwTime;

  private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
    @Override public boolean onMenuItemClick(MenuItem item) {

      //body.open_rule = presenter.getBatchOpenRule();
      //
      //if (body.max_users != Integer.valueOf(orderSutdentCount.getContent())
      //    && !isChangeCardPay
      //    && cardRules != null
      //    && cardRules.size() > 0) {
      //  showAlert("会员卡结算未填写完整");
      //  return true;
      //}
      //body.max_users = Integer.valueOf(orderSutdentCount.getContent());
      //body.is_free = !swNeedPay.isChecked();
      //
      //try {
      //  body.max_users = Integer.parseInt(orderSutdentCount.getContent());
      //} catch (Exception e) {
      //  body.max_users = 8;
      //}
      //if (!body.is_free) {
      //  if (body.rules == null) body.rules = new ArrayList<>();
      //  body.rules.clear();
      //  body.rules.addAll(cardRules);
      //  if (rulePayOnline != null) {
      //    rulePayOnline.to_number = body.max_users + 1;
      //    body.rules.add(rulePayOnline);
      //  }
      //}
      //body.from_date = starttime.getContent();
      //body.to_date = endtime.getContent();
      //body.time_repeats = BatchLoop.geTimeRepFromBean(datas);
      //if (body.time_repeats == null || body.time_repeats.size() == 0) {
      //  ToastUtils.show("请添加课程周期");
      //  return true;
      //}

      return true;
    }
  };
  private BatchDetailCommonView batchBaseFragment;
  private MaterialDialog exitDialg;

  private DialogList openDialog;
  private CommonFlexAdapter commonFlexAdapter;
  @Need public Staff mTeacher;
  @Need public Course mCourse;
  private List<BatchLoop> batchLoops = new ArrayList<>();


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    batchBaseFragment = BatchDetailCommonView.newInstance(mCourse, mTeacher);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    RxBus.getBus().register(BatchLoop.class)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<BatchLoop>() {
        @Override public void onNext(BatchLoop batchLoop) {
          BatchLoopItem item = new BatchLoopItem(batchLoop, presenter.isPrivate());
          if (commonFlexAdapter.contains(item)){
            int pos = commonFlexAdapter.index(item);
            ((BatchLoopItem)commonFlexAdapter.getItem(pos)).setBatchLoop(batchLoop);
            commonFlexAdapter.notifyItemChanged(pos);
          }else {
            commonFlexAdapter.addItem(item);
          }
        }
      });
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_add_batch, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.setPrivate(mCourse == null);

    initToolbar(toolbar);

    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.setAdapter(commonFlexAdapter);

    Calendar calendar = Calendar.getInstance();
    starttime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));
    calendar.add(Calendar.MONTH, 2);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.add(Calendar.DATE, -1);
    endtime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(
        presenter.isPrivate() ? R.string.t_add_private_course : R.string.t_add_group_course);
    toolbar.inflateMenu(R.menu.menu_compelete);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        presenter.checkBatch();
        return true;
      }
    });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(batchBaseFragment);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof BatchDetailCommonView) {
      batchBaseFragment.openPay(presenter.isPro());
    }
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
    popBack();
  }

  @Override public void onTemplete(boolean isFree, boolean openOnline, int maxuer) {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      batchBaseFragment.setOrderSutdentCount(maxuer);
      batchBaseFragment.openPayOnline(openOnline);
    }
    ToastUtils.showS("已自动填充排期");
    tvBatchLoopHint.setText("课程周期 (已根据历史信息自动填充)");
    tvClearAutoBatch.setVisibility(View.VISIBLE);
  }

  @Override public void onLoppers(List<BatchLoop> loopers) {
    setBackPress();
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

  @Override public int suportMemberNum() {
    if (batchBaseFragment != null && batchBaseFragment.isAdded()) {
      return batchBaseFragment.getOrderStudentCount();
    }
    return 8;
  }

  @Override public boolean needPay() {
    return batchBaseFragment != null && batchBaseFragment.isAdded() && batchBaseFragment.needPay();
  }

  /**
   * 选择开始时间
   */
  @OnClick(R2.id.starttime) public void onStartTime() {
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
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
  }

  /**
   * 选择结束时间
   */
  @OnClick(R2.id.endtime) public void onEndTime() {
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
            > 92 * DateUtils.DAY_TIME) {
          Toast.makeText(getContext(), R.string.alert_batch_greater_three_month, Toast.LENGTH_SHORT)
              .show();
          return;
        }
        endtime.setContent(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
  }

  @OnClick(R2.id.civ_to_open_time) public void onOpenTime() {
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
  @OnClick(R2.id.tv_clear_auto_batch) public void clearBatch() {
    commonFlexAdapter.clear();
    tvBatchLoopHint.setText("课程周期");
    tvClearAutoBatch.setVisibility(View.GONE);
  }

  @Override public boolean onItemClick(int position) {
    // TODO: 2017/9/15 跳去looper
    return true;
  }

  /**
   * 添加课程周期
   */
  @OnClick(R2.id.add) public void addBatchLoop() {
    setBackPressNull();
    routeTo("/batch/loop/add/",null);
  }
  //
  //// TODO: 2017/9/12 非高级版本
  //@OnClick(R2.id.can_not_close) public void canNotClose() {
  //  if (proGym) {
  //    showAlert(R.string.alert_batch_has_ordered);
  //  } else {
  //    new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
  //  }
  //}
}
