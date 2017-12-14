package cn.qingchengfit.saasbase.course.batch.views;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.SingleBatch;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchSinglePresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.frag_course_info) FrameLayout fragCourseInfo;
  @BindView(R2.id.civ_date) CommonInputView civDate;
  @BindView(R2.id.civ_course_time) CommonInputView civCourseTime;
  @BindView(R2.id.civ_open_time) CommonInputView civOpenTime;
  @BindView(R2.id.btn_del) Button btnDel;

  @Inject BatchSinglePresenter presenter;

  @Need public String scheduleId;
  @Need public Boolean isPrivate = false;
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
    View view = inflater.inflate(R.layout.fragment_saas_single_batch, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("课程");
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


  @OnClick(R2.id.civ_date) public void onCivDateClicked() {
    chooseTime(civDate);
  }

  /**
   * 课程时间修改
   */
  @OnClick(R2.id.civ_course_time) public void onCivCourseTimeClicked() {

  }

  /**
   * 开放时间
   */
  @OnClick(R2.id.civ_open_time) public void onCivOpenTimeClicked() {

  }

  /**
   * 删除排期
   */
  @OnClick(R2.id.btn_del) public void onBtnDelClicked() {
    presenter.delSchedule();
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

    civDate.setContent(DateUtils.getYYYYMMDDfromServer(batchDetail.start));
    civCourseTime.setContent(isPrivate?DateUtils.getHHMMDuringFromServer(batchDetail.start,batchDetail.end, batchDetail.is_cross):DateUtils.getYYMMfromServer(batchDetail.start));
    onOpenRule(batchDetail.open_rule,DateUtils.formatDateFromServer(batchDetail.start));
  }

  public void onOpenRule(BatchOpenRule rule,Date start) {
    if (rule != null ){
      if (rule.type <3 && !TextUtils.isEmpty(rule.open_datetime))
        civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(rule.open_datetime)));
      else if (rule.type == 3 && rule.advance_hours != null){
        if (start != null)
          civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.addHour(start,-rule.advance_hours)));
      }
    }
  }

  private void inflateBatchInfo(BatchDetail batchDetail){
    batchBaseFragment.setOrderSutdentCount(batchDetail.max_users);
    batchBaseFragment.openPayOnline(!batchDetail.is_free);
    batchBaseFragment.setSpace(batchDetail.spaces);
    batchBaseFragment.setRules(batchDetail.rule, (ArrayList<CardTplBatchShip>) batchDetail.card_tpls);
  }

  @OnClick(R2.id.civ_to_open_time) public void onOpenTime() {
    if (openDialog == null) {
      openDialog =
        DialogList.builder(getContext()).list(arrayOpenTime, new AdapterView.OnItemClickListener() {
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


}
