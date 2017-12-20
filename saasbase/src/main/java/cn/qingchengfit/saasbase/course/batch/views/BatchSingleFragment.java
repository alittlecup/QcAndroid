package cn.qingchengfit.saasbase.course.batch.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.SingleBatch;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchSinglePresenter;
import cn.qingchengfit.saasbase.databinding.FragmentSaasSingleBatchBinding;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.jakewharton.rxbinding.view.RxMenuItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@Leaf(module = "course", path = "/batch/schedule/single/")
public class BatchSingleFragment extends SaasBaseFragment implements BatchSinglePresenter.MVPView{


  @Inject BatchSinglePresenter presenter;

  @Need public String scheduleId;
  @Need public Boolean isPrivate = false;

  FragmentSaasSingleBatchBinding db;
  private BatchDetailCommonView batchBaseFragment;
  private DialogList openDialog;
  private String[] arrayOpenTime;
  private TimeDialogWindow chooseOpenTimeDialog;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.setScheduleId(scheduleId);
    presenter.setPrivate(isPrivate);
    arrayOpenTime = getResources().getStringArray(R.array.order_open_time);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater,container,savedInstanceState);
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_saas_single_batch, container, false);
    //unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(db.toolbarLayout.toolbar);
    db.civOpenTime.setOnClickListener(view -> onOpenTime());
    db.civDate.setOnClickListener(view -> onCivDateClicked());
    db.civCourseTime.setOnClickListener(view -> onCivCourseTimeClicked());
    db.btnDel.setOnClickListener(view -> onBtnDelClicked());
    return db.getRoot();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    //((TextView)db.loToolbar.findViewById(R.id.toolbar_title)).setText("课程");
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          presenter.editSchedule();
        }
      });
  }



  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
    Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof BatchDetailCommonView){
      inflateBatchInfo(presenter.getBatchDetail());
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryData();
  }

  @Override public boolean onFragmentBackPress() {
    DialogUtils.instanceDelDialog(getContext(), "确认放弃本次编辑?", new MaterialDialog.SingleButtonCallback() {
      @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        popBack();
      }
    }).show();
    return true;
  }

  @Override public String getFragmentName() {
    return BatchSingleFragment.class.getName();
  }


  public void onCivDateClicked() {
    chooseTime(db.civDate);
  }

  /**
   * 课程时间修改
   */
  public void onCivCourseTimeClicked() {
    choosTime(TimePopupWindow.Type.HOURS_MINS, 0, 0, DateUtils.HHMM2date(db.civCourseTime.getContent()), db.civCourseTime);
  }


  /**
   * 删除排期
   */
  public void onBtnDelClicked() {
    DialogUtils.instanceDelDialog(getContext(), "确认删除此排期？", (dialog, which) -> {
      presenter.delSchedule();
    }).show();
  }

  @Override public void onDetail(SingleBatch batchDetail) {
    if (batchBaseFragment == null)
      batchBaseFragment = BatchDetailCommonView.newInstance(batchDetail.course, batchDetail.teacher);
    else {
      batchBaseFragment.setTrainer(batchDetail.teacher);
      batchBaseFragment.setCourse(batchDetail.course);
    }
    if (!batchBaseFragment.isAdded())
      stuff(R.id.frag_course_info,batchBaseFragment);
    else {
      inflateBatchInfo(batchDetail);
    }

    db.civDate.setContent(DateUtils.getYYYYMMDDfromServer(batchDetail.start));
    db.civCourseTime.setContent(isPrivate?DateUtils.getHHMMDuringFromServer(batchDetail.start,batchDetail.end, batchDetail.is_cross):DateUtils.getYYMMfromServer(batchDetail.start));
    onOpenRule(batchDetail.open_rule,DateUtils.formatDateFromServer(batchDetail.start));
  }

  public void onOpenRule(BatchOpenRule rule,Date start) {
    if (rule != null ){
      if (rule.type <3 && !TextUtils.isEmpty(rule.open_datetime))
        db.civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(rule.open_datetime)));
      else if (rule.type == 3 && rule.advance_hours != null){
        if (start != null)
          db.civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.addHour(start,-rule.advance_hours)));
      }
    }
  }

  private void inflateBatchInfo(BatchDetail batchDetail){
    batchBaseFragment.setOrderSutdentCount(batchDetail.max_users);
    batchBaseFragment.openPayOnline(!batchDetail.is_free);
    batchBaseFragment.setSpace(batchDetail.getSpaces());
    batchBaseFragment.setRules(batchDetail.rule, (ArrayList<CardTplBatchShip>) batchDetail.card_tpls);
  }

  public void onOpenTime() {
    if (openDialog == null) {
      openDialog =
        DialogList.builder(getContext()).list(arrayOpenTime, new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
              presenter.setOpenRuleType(1);
              db.civOpenTime.setContent(arrayOpenTime[0]);
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
          db.civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(date));
          presenter.setOpenRuleType(2);
          presenter.setOpenRuleTime(DateUtils.Date2YYYYMMDDHHmmss(date), null);
        }
      });
    }
    chooseOpenTimeDialog.setRange(DateUtils.getYear(new Date()) - 1,
      DateUtils.getYear(new Date()) + 1);
    Date d = new Date();
    if (!TextUtils.isEmpty(db.civOpenTime.getContent())) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm", Locale.CHINA);
        d = formatter.parse(db.civOpenTime.getContent());
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
        db.civOpenTime.setContent("提前" + pos + "小时预约");
        presenter.setOpenRuleType(3);
        presenter.setOpenRuleTime(null, pos);
      }
    });
    simpleScrollPicker.show(0, 240, 4);
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
    return null;
  }

  @Override public String getEnd() {
    return null;
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



  @Override public int suportMemberNum() {
    return batchBaseFragment.getOrderStudentCount();
  }

  @Override public boolean needPay() {
    return batchBaseFragment.needPay();
  }


}
