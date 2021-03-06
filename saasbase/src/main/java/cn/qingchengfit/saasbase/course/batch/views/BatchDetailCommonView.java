package cn.qingchengfit.saasbase.course.batch.views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.cards.views.BatchPayCardParams;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseParams;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlan;
import cn.qingchengfit.saasbase.course.course.views.AddCourseParams;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseParams;
import cn.qingchengfit.saasbase.events.EventPayOnline;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.model.body.BatchPayResponse;
import cn.qingchengfit.saascommon.events.EventCourse;
import cn.qingchengfit.saascommon.events.EventSiteSelected;
import cn.qingchengfit.saascommon.events.EventStaffWrap;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import cn.qingchengfit.widgets.BottomChooseInverseDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/9/11.
 */
public class BatchDetailCommonView extends BaseFragment {

  ImageView img;
  FrameLayout imgLayout;
  ImageView imgFoot;
  TextView text1;
  TextView text3;
  ImageView righticon;
  RelativeLayout courseLayout;
  CommonInputView coach;
  CommonInputView space;
  CommonInputView orderSutdentCount;
  CommonInputView payOnline;
  CommonInputView payCard;
  CommonInputView priceSetting;
  ExpandedLayout elMultiSupport;//这里是是否支持多人私教， 只有isPrivate==true的情况下是可用的否则为空
  LinearLayout llPayContent;

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject IPermissionModel serPermisAction;
  @Inject public ICourseModel courseApi;
  @Inject ViewModelProvider.Factory factory;
  BatchFragmentVM mViewModel;
  @Inject IStaffModel staffModel;

  private Course course;
  private Staff trainer;
  private ArrayList<Rule> rulesPayCards = new ArrayList<>();
  public Rule payOnlineRule;
  private List<Space> spaces = new ArrayList<>();
  private ArrayList<CardTplBatchShip> cardtplships;
  private boolean numHasChange = false;//人数已经修改
  private String mSource;
  private boolean hasOrder;
  private boolean isPrivate = true; //true -私教，false-团课
  private boolean isStaff = true; //true -管理端 false -教练端

  public static BatchDetailCommonView newInstance(Course course, Staff trainer, String source,
      boolean isPrivate) {
    Bundle args = new Bundle();
    args.putParcelable("course", course);
    args.putParcelable("trainer", trainer);
    args.putString("source", source);
    args.putBoolean("private", isPrivate);
    BatchDetailCommonView fragment = new BatchDetailCommonView();
    fragment.setArguments(args);
    return fragment;
  }

  public static BatchDetailCommonView newInstance(Course course, Staff trainer, String source,
      boolean isPrivate, boolean isStaff) {
    Bundle args = new Bundle();
    args.putParcelable("course", course);
    args.putParcelable("trainer", trainer);
    args.putString("source", source);
    args.putBoolean("private", isPrivate);
    args.putBoolean("staff", isStaff);
    BatchDetailCommonView fragment = new BatchDetailCommonView();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      course = getArguments().getParcelable("course");
      trainer = getArguments().getParcelable("trainer");
      mSource = getArguments().getString("source");
      isPrivate = getArguments().getBoolean("private");
      isStaff = getArguments().getBoolean("staff");

      if (course == null) {
        course = new Course();
        course.is_private = isPrivate;
      }
    }
  }

  private void loadBatchPayStatus() {
    RxRegiste(staffModel.qcGetBatchPayMethod()
        .compose(RxHelper.schedulersTransformer())
        .subscribe(batchPayResponseQcDataResponse -> {
          if (ResponseConstant.checkSuccess(batchPayResponseQcDataResponse)) {
            updateView(batchPayResponseQcDataResponse.data);
          }else{
            updateView(data);
          }
        }, throwable -> {
          updateView(data);

        }));
  }

  private BatchPayResponse data = new BatchPayResponse();

  private void updateView(BatchPayResponse data) {
    this.data = data;
    View icon1 = payOnline.findViewById(R.id.im_icon1);
    ViewGroup.LayoutParams layoutParams = icon1.getLayoutParams();
    ImageView icon3 = new ImageView(getContext());
    ImageView icon4 = new ImageView(getContext());
    icon3.setImageResource(
        data.alisp ? R.drawable.icon_alisp_circle_enable : R.drawable.icon_alisp_circle_disable);
    icon4.setImageResource(
        data.corpCard ? R.drawable.icon_corp_circle : R.drawable.icon_crop_circle_disable);
    ViewParent parent = icon1.getParent();
    if (parent instanceof LinearLayout) {
      ((LinearLayout) parent).addView(icon3, 3, layoutParams);
      ((LinearLayout) parent).addView(icon4, 4, layoutParams);
    }
  }

  LinearLayout llCourseWorkout;
  CommonInputView civWorkout, civWorkoutPlan;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(isPrivate ? R.layout.fragment_batch_detail_common_private
        : R.layout.fragment_batch_detail_common_group, container, false);
    img = (ImageView) view.findViewById(R.id.img);
    imgLayout = (FrameLayout) view.findViewById(R.id.img_layout);
    imgFoot = (ImageView) view.findViewById(R.id.img_foot);
    text1 = (TextView) view.findViewById(R.id.text1);
    text3 = (TextView) view.findViewById(R.id.text3);
    righticon = (ImageView) view.findViewById(R.id.righticon);
    courseLayout = (RelativeLayout) view.findViewById(R.id.course_layout);
    coach = (CommonInputView) view.findViewById(R.id.coach);
    space = (CommonInputView) view.findViewById(R.id.space);
    orderSutdentCount = (CommonInputView) view.findViewById(R.id.order_sutdent_count);
    payOnline = (CommonInputView) view.findViewById(R.id.pay_online);
    payCard = (CommonInputView) view.findViewById(R.id.pay_card);
    priceSetting = (CommonInputView) view.findViewById(R.id.price_setting);
    llPayContent = view.findViewById(R.id.ll_price_content);
    civWorkout = view.findViewById(R.id.civ_course_system);
    civWorkoutPlan = view.findViewById(R.id.civ_course_system_class);
    if ((mSource.equals("addbatch") || mSource.equals("editbatch")) && !isPrivate) {
      llCourseWorkout = view.findViewById(R.id.ll_course_system);
      llCourseWorkout.setVisibility(View.VISIBLE);
      mViewModel = ViewModelProviders.of(this, factory).get(BatchFragmentVM.class);
      mViewModel.selectWork.observe(this, workout -> {
        if (workout != null) {
          civWorkout.setContent(workout.getName());
        } else {
          civWorkout.setContent("");
        }
      });
      mViewModel.selectWorkPlan.observe(this, workoutPlan -> {
        if (workoutPlan != null) {
          civWorkoutPlan.setContent(workoutPlan.getName());
        } else {
          civWorkoutPlan.setContent("");
        }
      });
      civWorkout.setOnClickListener(v -> onWorkoutClick());
      civWorkoutPlan.setOnClickListener(v -> onWorkoutPlanClick());
    }
    view.findViewById(R.id.course_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isPrivate) {
          onCoachClicked();
        } else {
          onCourseLayoutClicked();
        }
      }
    });
    view.findViewById(R.id.coach).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isPrivate) {
          onCourseLayoutClicked();
        } else {
          onCoachClicked();
        }
      }
    });
    view.findViewById(R.id.space).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSpaceClicked();
      }
    });
    view.findViewById(R.id.order_sutdent_count).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onOrderSutdentCountClicked();
      }
    });
    view.findViewById(R.id.pay_online).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onPayOnlineClicked();
      }
    });
    view.findViewById(R.id.pay_card).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onPayCardClicked();
      }
    });
    priceSetting.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showBatchPriceSettingDialog();
      }
    });

    setCourse(course);
    setTrainer(trainer);
    initRxbus();

    if (isPrivate) {
      elMultiSupport = view.findViewById(R.id.el_multi_support);
      elMultiSupport.setSwitchClickListenr(view1 -> numHasChange = true);
      elMultiSupport.setOnCheckedChangeListener((compoundButton, b) -> {
        payCard.setContent(b ? "已开启多人支持，请重新设置" : "已关闭多人支持，请重新设置");
      });
    }
    loadBatchPayStatus();
    payOnline.setLabelDrawable(R.drawable.vd_payment_wechat, R.drawable.vd_payment_alipay);
    return view;
  }

  private int llPriceContentVisibility = View.INVISIBLE;

  private void onWorkoutPlanClick() {
    if (mViewModel == null) return;
    List<WorkoutPlan.Workout> value = mViewModel.getWorks().getValue();
    if (value == null) {
      ToastUtils.show("请选择课程体系");
      mViewModel.loadWorkout();
    } else if (value.isEmpty()) {
      ToastUtils.show("课程体系内容为空");
    } else if (civWorkout.getContent().isEmpty() || mViewModel.selectWork.getValue() == null) {
      ToastUtils.show("请选择课程体系");
    } else {
      Map<String, List<WorkoutPlan>> plans = mViewModel.getWorkoutPlans();
      String id = mViewModel.selectWork.getValue().getId();
      if (!plans.containsKey(id)) {
        mViewModel.loadPlans();
      } else if (plans.get(id) == null || plans.get(id).isEmpty()) {
        ToastUtils.show("训练计划内容为空");
      } else {
        List<WorkoutPlan> workoutPlans = plans.get(id);
        WorkoutPlan selectWorkPlanValue = mViewModel.selectWorkPlan.getValue();
        List<BottomChooseData> datas = new ArrayList<>();
        int selePosition = -1;
        for (WorkoutPlan workout : workoutPlans) {
          datas.add(new BottomChooseData(workout.getName()));
          if (selectWorkPlanValue != null && selectWorkPlanValue.equals(workout)) {
            selePosition = workoutPlans.indexOf(workout);
          }
        }
        BottomChooseDialog planChooseDialog =
            new BottomChooseInverseDialog(getContext(), "选择训练计划", datas);
        planChooseDialog.addSeleced(selePosition);
        planChooseDialog.setOnItemClickListener(position -> {
          WorkoutPlan plan = workoutPlans.get(position);
          WorkoutPlan selectWorkPlanValue1 = mViewModel.selectWorkPlan.getValue();
          if (plan.equals(selectWorkPlanValue1)) {
            mViewModel.selectWorkPlan.setValue(null);
          } else {
            mViewModel.selectWorkPlan.setValue(plan);
          }
          return true;
        });
        planChooseDialog.show();
      }
    }
  }

  BottomChooseDialog workoutChooseDialog;

  private void onWorkoutClick() {
    if (mViewModel == null) return;
    List<WorkoutPlan.Workout> value = mViewModel.getWorks().getValue();
    if (value == null) {
      mViewModel.loadWorkout();
    } else if (value.isEmpty()) {
      ToastUtils.show("课程体系内容为空");
    } else {
      if (workoutChooseDialog == null) {
        List<BottomChooseData> datas = new ArrayList<>();
        WorkoutPlan.Workout selectWorkValue = mViewModel.selectWork.getValue();
        int selectPosition = -1;
        for (WorkoutPlan.Workout workout : value) {
          datas.add(new BottomChooseData(workout.getName()));
          if (selectWorkValue != null && selectWorkValue.getId().equals(workout.getId())) {
            selectPosition = value.indexOf(workout);
          }
        }
        workoutChooseDialog = new BottomChooseInverseDialog(getContext(), "选择课程体系", datas);
        workoutChooseDialog.addSeleced(selectPosition);

        workoutChooseDialog.setOnItemClickListener(position -> {
          WorkoutPlan.Workout workout = value.get(position);
          if (workout.equals(mViewModel.selectWork.getValue())) {
            mViewModel.setSelectWork(null);
          } else {
            mViewModel.setSelectWork(workout);
          }
          mViewModel.selectWorkPlan.setValue(null);
          return true;
        });
      }
      workoutChooseDialog.show();
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (llPriceContentVisibility != View.INVISIBLE) {
      llPayContent.setVisibility(llPriceContentVisibility);
    }
  }

  @Override public void onPause() {
    super.onPause();
    if (null != llPayContent) {
      llPriceContentVisibility = llPayContent.getVisibility();
    }
  }

  public void initRxbus() {
    RxBusAdd(EventBatchPayCard.class).onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventBatchPayCard>() {
          @Override public void onNext(EventBatchPayCard eventBatchPayCard) {
            numHasChange = false;
            setCardRule(eventBatchPayCard.getRules(), null);
          }
        });
    RxBusAdd(EventStaffWrap.class).onBackpressureDrop()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventStaffWrap>() {
          @Override public void onNext(EventStaffWrap eventStaffWrap) {
            setTrainer(eventStaffWrap.getStaff());
          }
        });
    RxBusAdd(EventSiteSelected.class).onBackpressureDrop()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventSiteSelected>() {
          @Override public void onNext(EventSiteSelected eventSiteSelected) {
            setSpace(eventSiteSelected.getSpaces());
          }
        });
    RxBus.getBus()
        .register(EventPayOnline.class)
        .compose(bindToLifecycle())
        .compose(doWhen(FragmentEvent.CREATE_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventPayOnline>() {
          @Override public void onNext(EventPayOnline e) {
            payOnlineRule = e.getRule();
            openPayOnline(e.getRule() != null);
          }
        });
    RxBus.getBus()
        .register(EventCourse.class)
        .compose(bindToLifecycle())
        .compose(doWhen(FragmentEvent.CREATE_VIEW))
        .observeOn(AndroidSchedulers.mainThread())
        .filter(
            eventCourse -> CmStringUtils.isEmpty(eventCourse.getSrc()) || mSource.equalsIgnoreCase(
                eventCourse.getSrc()))
        .subscribe(new BusSubscribe<EventCourse>() {
          @Override public void onNext(EventCourse course) {
            BatchDetailCommonView.this.course = course.getCourse();
            setCourse(course.getCourse());
          }
        });
  }

  public boolean isHasOrder() {
    return hasOrder;
  }

  public void setHasOrder(boolean hasOrder) {
    this.hasOrder = hasOrder;
  }

  /**
   * 设置课程信息
   */
  public void setCourse(Course course) {
    if (course == null) return;
    if (text1 == null) return;
    if (isPrivate) {
      if (coach == null) return;
      coach.setLabel("课程");
      coach.setContent(course.getName());
    } else {
      PhotoUtils.small(img, course.getPhoto(), R.drawable.img_default_course);
      if (TextUtils.isEmpty(course.id)) {
        text1.setText("请选择课程");
      } else {
        text1.setText(course.getName());
        text3.setText(getString(R.string.course_d_lenght, course.getLength() / 60));
      }
    }
    queryTemple();
  }

  public String getCourseId() {
    if (course != null) {
      return course.id;
    } else {
      return "";
    }
  }

  /**
   * 设置教练
   */
  public void setTrainer(Staff staff) {
    if (staff == null) {
      if (isPrivate) {
        if (text1 == null || text3 == null) return;
        PhotoUtils.small(img, "", R.drawable.img_default_teacher_male);
        img.setImageResource(R.drawable.img_default_teacher_male);
        text1.setText("请选择教练");
      }
      return;
    }
    if (coach == null) return;
    this.trainer = staff;
    if (isPrivate) {
      if (text1 == null || text3 == null) return;
      PhotoUtils.small(img, staff.getAvatar(),
          staff.gender == 0 ? R.drawable.img_default_teacher_male
              : R.drawable.img_default_teacher_female);
      text1.setText(staff.getUsername());
      text3.setVisibility(View.GONE);
    } else {
      String trainerName = staff.getUsername();
      if ((trainerName != null && mSource.equals("addbatch")) || (mSource.equals("editbatch")
          && (!isStaff))) {
        coach.setShowRight(false);
      }
      coach.setContent(trainerName);
    }
    queryTemple();
  }

  protected void queryTemple() {
    if (trainer != null
        && this.course != null
        && !CmStringUtils.isEmpty(this.course.getId())
        && listener != null) {
      listener.onBatchTemple();
    }
  }

  public String getTrainerId() {
    if (trainer != null) {
      return trainer.getId();
    } else {
      return "";
    }
  }

  public void setSpace(List<Space> spaces) {
    if (spaces == null) return;
    this.spaces.clear();
    this.spaces.addAll(spaces);
    if ((mSource.equals("addbatch") || mSource.equals("editbatch")) && isPrivate == true) {
      if (spaces.size() == 1) {
        space.setContent(ListUtils.ListObj2StrCN(spaces));
      } else {
        space.setContent(spaces.size() + "处场地");
      }
    } else {
      space.setContent(ListUtils.ListObj2StrCN(spaces));
    }
  }

  /**
   * 设置可约人数
   */
  private int max_user = 1;

  public void setOrderSutdentCount(@IntRange(from = 1) int x) {
    max_user = x;
    if (orderSutdentCount != null) orderSutdentCount.setContent(Integer.toString(x));
  }

  public void setMutlSupport(boolean support) {
    if (isPrivate) {
      elMultiSupport.setExpanded(support);
      numHasChange = false;
    }
  }

  public boolean mutilSupportble() {
    if (isPrivate) {
      return elMultiSupport != null && elMultiSupport.isExpanded();
    }
    return prePriceChoosePos == 2;
  }

  public boolean needPay() {
    return prePriceChoosePos != 0;
  }

  int asTeacher = -1;

  public void openPay(boolean open) {
    if (llPayContent != null) {
      llPayContent.setVisibility(open ? View.VISIBLE : View.GONE);
    }
    if (priceSetting != null) {
      priceSetting.setContent(open ? "收费" : "免费");
      if (open) {
        prePriceChoosePos = 1;
      } else {
        prePriceChoosePos = 0;
      }
    }
  }

  public void openPayOnline(boolean open) {
    if (open) {
      payOnline.setContent("已设置");
    } else {
      payOnline.setContent("未设置");
    }
  }

  public void openMultiCardPay(boolean open) {
    if (open) {
      payCard.setContent("");
    }
  }

  public void setRules(List<Rule> rules, ArrayList<CardTplBatchShip> ships) {
    List<Rule> rules1 = new ArrayList<>();
    if ((rules != null) && !rules.isEmpty()) {
      for (Rule rule : rules) {
        if (rule.channel.equalsIgnoreCase("ONLINE")) {
          payOnlineRule = rule;
        } else {
          rules1.add(rule);
          if (rule.to_number != (max_user + 1)) {
            if (!isPrivate) {
              prePriceChoosePos = 2;
              priceSetting.setContent("团课动态价格");
            }
          }
        }
      }
    }
    openPayOnline(payOnlineRule != null);
    setCardRule(rules1, ships);
  }

  private void setCardRule(List<Rule> rules, ArrayList<CardTplBatchShip> ships) {
    rulesPayCards.clear();
    if (rules != null && rules.size() > 0) {
      rulesPayCards.addAll(rules);
      List<String> cardids = new ArrayList<>();
      for (Rule rule : rules) {
        if (!cardids.contains(rule.card_tpl_id)) {
          cardids.add(rule.card_tpl_id);
        }
      }
      payCard.setContent(getString(R.string.batch_can_pay_card_count, cardids.size()));
    } else {
      payCard.setContent(getString(R.string.common_un_setting));
    }
    if (ships != null) cardtplships = ships;
  }

  /**
   * 获取课预约人数
   */
  @IntRange(from = 1) public int getOrderStudentCount() {
    if (orderSutdentCount != null) {
      try {
        return Integer.parseInt(orderSutdentCount.getContent());
      } catch (Exception e) {
        return 8;
      }
    } else {
      return 8;
    }
  }

  @Override public String getFragmentName() {
    return BatchDetailCommonView.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void initData() {
    showLoading();
    RxRegiste(staffModel.getTrainers()
        .compose(RxHelper.schedulersTransformer())
        .doOnTerminate(this::hideLoading)
        .subscribe(new NetSubscribe<QcDataResponse<StaffShipsListWrap>>() {
          @Override public void onNext(QcDataResponse<StaffShipsListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              List<StaffShip> staffships = qcResponse.data.staffships;
              if (staffships != null && !staffships.isEmpty()) {
                asTeacher = -1;
                routeTo("staff", "/trainer/choose/",
                    new TrainerChooseParams().selectedId(trainer != null ? trainer.getId() : null)
                        .staffs(new ArrayList<>(staffships))
                        .build());
              } else {
                if (!serPermisAction.check(PermissionServerUtils.COACHSETTING_CAN_WRITE)) {
                  showAlert(R.string.alert_permission_forbid);
                  return;
                }
                showNoCoachDialog();
              }
            } else {
              onShowError(qcResponse.getMsg());
              asTeacher = -1;
            }
          }
        }));
  }

  private void showNoCoachDialog() {
    List<BottomChooseData> data = new ArrayList<>();
    data.add(new BottomChooseData(loginStatus.staff_name() + "(我自己)"));
    data.add(new BottomChooseData("邀请其他教练"));
    BottomChooseDialog dialog = new BottomChooseDialog(getContext(), "选择教练", data);
    dialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
      @Override public boolean onItemClick(int position) {
        if (position == 0) {
          RxBus.getBus()
              .post(new EventStaffWrap.Builder().staff(loginStatus.getLoginUser())
                  .type(EventStaffWrap.TRAINER)
                  .build());
          asTeacher = 1;
        } else {
          routeTo("staff", "/trainer/add/", null);
          asTeacher = -1;
        }
        return false;
      }
    });
    dialog.show();
  }

  public void loadCourse() {
    showLoading();
    RxRegiste(courseApi.qcGetCourses(isPrivate)
        .compose(RxHelper.schedulersTransformer())
        .doOnTerminate(this::hideLoading)
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data != null
                && qcResponse.data.courses != null
                && !qcResponse.data.courses.isEmpty()) {
              routeTo("course", "/choose/", CourseChooseParams.builder()
                  .src(mSource)
                  .mIsPrivate(isPrivate)
                  .courses(new ArrayList<>(qcResponse.data.courses))
                  .build());
            } else {
              if (!serPermisAction.checkInBrand(
                  isPrivate ? PermissionServerUtils.PRISETTING_CAN_WRITE
                      : PermissionServerUtils.TEAMSETTING_CAN_WRITE)) {
                showAlert(R.string.sorry_for_no_permission);
                return;
              }
              routeTo("/add/",
                  new AddCourseParams().isPrivate(isPrivate).notFromList(true).build());
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 更改课程
   */
  public void onCourseLayoutClicked() {
    loadCourse();
  }

  /**
   * 更改教练
   */
  public void onCoachClicked() {
    if (AppUtils.getCurApp(getContext()) == 0) return;
    initData();
  }

  /**
   * 更改场地
   */
  public void onSpaceClicked() {
    routeTo("gym", "/site/choose/", new BundleBuilder().withBoolean("isPrivate", isPrivate)
        .withStringArrayList("selectIds", ListUtils.getIdList(spaces))
        .build());
  }

  /**
   * 更改上课人数
   */
  public void onOrderSutdentCountClicked() {
    new DialogList(getContext()).list(
        isPrivate ? CmStringUtils.getNums(1, 10) : CmStringUtils.getNums(1, 300),
        (parent, view, position, id) -> {
          int before = 8;
          try {
            before = Integer.parseInt(orderSutdentCount.getContent());
          } catch (Exception e) {
            LogUtil.e("排课时上课人数不为int类型");
          }
          if (before != (position + 1)) {
            orderSutdentCount.setContent(Integer.toString(position + 1));
            if (mutilSupportble()) {
              numHasChange = true;
              payCard.setContent("已修改可约人数，请重新设置");
            }
          }
        }).title("选择人数").show();
  }

  /**
   * 在线支付 动态价格，私教需要1对多私教，团课需要开启动态价格
   */
  public void onPayOnlineClicked() {
    if (isPrivate) {
      routeTo("/batch/pay/online/",
          new cn.qingchengfit.saasbase.course.batch.views.BatchPayOnlineParams().rule(payOnlineRule)
              .maxPeople(elMultiSupport.isExpanded() ? getOrderStudentCount() : 1)
              .payResponse(data)
              .multiPrice(elMultiSupport.isExpanded())
              .build());
    } else {
      routeTo("/batch/pay/online/",
          new cn.qingchengfit.saasbase.course.batch.views.BatchPayOnlineParams().rule(payOnlineRule)
              .maxPeople(getOrderStudentCount())
              .payResponse(data)
              .multiPrice(prePriceChoosePos == 2)
              .build());
    }
  }

  /**
   * 卡支付设置
   */
  public void onPayCardClicked() {
    boolean isAdd;
    if (mSource != null && mSource.contains("add")) {
      isAdd = true;
    } else {
      isAdd = false;
    }
    routeTo("card", "/card/batch/choose/", new BatchPayCardParams().rules(rulesPayCards)
        .isPrivate(isPrivate)
        .isAdd(isAdd)
        .cardTplBatchShips(cardtplships)
        .multiPrice(mutilSupportble())
        .maxCount(getOrderStudentCount())
        .build());
  }

  public void onPayCardRules(int size) {
    if (size > 0) {
      payCard.setHint(getString(R.string.batch_can_pay_card_count, size));
    } else {
      payCard.setHint(getString(R.string.common_un_setting));
    }
  }

  private BottomChooseDialog dialog;

  private void showBatchPriceSettingDialog() {
    if (dialog != null) {
      dialog.show();
      return;
    }
    List<BottomChooseData> datas = new ArrayList<>();
    if (isPrivate) {
      datas.add(new BottomChooseData("免费", "无需支付"));
      datas.add(new BottomChooseData("收费", "使用会员卡或在线支付约课"));
    } else {
      datas.add(new BottomChooseData("免费", "无需支付"));
      datas.add(new BottomChooseData("收费", "使用会员卡或在线支付约课"));
      datas.add(new BottomChooseData("团课动态价格", "根据预约人数设置不同价格"));
    }
    dialog = new BottomChooseDialog(getContext(), "课程价格", datas);
    dialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
      @Override public boolean onItemClick(int position) {
        if (AppUtils.getCurApp(getContext()) != 0 && hasOrder) {//非教练APP
          showAlert(R.string.alert_batch_has_ordered);
          return false;
        }
        changePaySetting(position);
        return true;
      }
    });
    dialog.show();
  }

  /**
   * 修改
   *
   * @param position 0-免费，1-付费/统一价格，2-团课动态价格
   */
  private void changePaySetting(int position) {
    if (isPrivate) {
      switch (position) {
        case 0:
          priceSetting.setContent("免费");
          llPayContent.setVisibility(View.GONE);
          break;
        case 1:
          priceSetting.setContent("收费");
          llPayContent.setVisibility(View.VISIBLE);
          break;
      }
    } else {
      switch (position) {
        case 0:
          priceSetting.setContent("免费");
          llPayContent.setVisibility(View.GONE);
          break;
        case 1:
          priceSetting.setContent("收费");
          llPayContent.setVisibility(View.VISIBLE);

          if (prePriceChoosePos == 2) {
            payCard.setContent("已关闭多人支持，请重新设置");
            numHasChange = true;
          }
          break;
        case 2:
          priceSetting.setContent("团课动态价格");
          llPayContent.setVisibility(View.VISIBLE);
          if (prePriceChoosePos == 1) {
            payCard.setContent("已开启多人支持，请重新设置");
            numHasChange = true;
          }
          break;
      }
    }
    prePriceChoosePos = position;
  }

  private int prePriceChoosePos = 0;

  public List<String> getSupportSpace() {
    return ListUtils.getIdList(spaces);
  }

  public List<Rule> getRules() {
    List<Rule> rules = new ArrayList<>();
    if (!numHasChange) {
      if (payOnlineRule != null) {
        if (prePriceChoosePos == 1) {
          payOnlineRule.to_number = getOrderStudentCount() + 1;
        }
        rules.add(payOnlineRule);
      }
      if (rulesPayCards != null && rulesPayCards.size() > 0) {
        if (prePriceChoosePos == 1) {
          for (Rule rule : rulesPayCards) {
            rule.to_number = getOrderStudentCount() + 1;
          }
        }
        rules.addAll(rulesPayCards);
      }
    }
    return rules;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  BatchTempleListener listener;

  public BatchTempleListener getListener() {
    return listener;
  }

  public void setListener(BatchTempleListener listener) {
    this.listener = listener;
  }

  public void setWorkOutPlan(WorkoutPlan workoutPlan) {
    mViewModel.selectWork.setValue(workoutPlan.getWorkout());
    mViewModel.selectWorkPlan.setValue(workoutPlan);
  }

  public interface BatchTempleListener {
    void onBatchTemple();
  }
}
