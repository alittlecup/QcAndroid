package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.cards.views.BatchPayCardParams;
import cn.qingchengfit.saasbase.coach.event.EventStaffWrap;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseParams;
import cn.qingchengfit.saasbase.common.views.UseStaffAppFragmentFragment;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.course.event.EventCourse;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseParams;
import cn.qingchengfit.saasbase.events.EventPayOnline;
import cn.qingchengfit.saasbase.gymconfig.event.EventSiteSelected;
import cn.qingchengfit.saasbase.gymconfig.views.SiteSelectedParams;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
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
	ExpandedLayout elPay;
	ExpandedLayout elMultiSupport;

  @Inject GymWrapper gymWrapper;

  private Course course;
  private Staff trainer;
  private ArrayList<Rule> rulesPayCards = new ArrayList<>();
  private Rule payOnlineRule;
  private List<Space> spaces = new ArrayList<>();
  private ArrayList<CardTplBatchShip> cardtplships;
  private boolean numHasChange = false;//人数已经修改
  private String mSource;
  private boolean hasOrder;
  private boolean isPrivate = true; //true -私教，false-团课

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

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      course = getArguments().getParcelable("course");
      trainer = getArguments().getParcelable("trainer");
      mSource = getArguments().getString("source");
      isPrivate = getArguments().getBoolean("private");

      if (course == null) {
        course = new Course();
        course.is_private = isPrivate;
      }
    }
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
    elPay = (ExpandedLayout) view.findViewById(R.id.el_pay);
    elMultiSupport = (ExpandedLayout) view.findViewById(R.id.el_multi_support);
    view.findViewById(R.id.course_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCourseLayoutClicked();
      }
    });
    view.findViewById(R.id.coach).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCoachClicked();
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

    setCourse(course);
    setTrainer(trainer);
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

    elMultiSupport.setSwitchClickListenr(view1 -> numHasChange = true);
    elMultiSupport.setOnCheckedChangeListener((compoundButton, b) -> {
      //if (b && getOrderStudentCount() > 1){

      payCard.setContent(b ? "已开启多人支持，请重新设置" : "已关闭多人支持，请重新设置");
      //}
    });
    elPay.setOnHeaderTouchListener((view1, motionEvent) -> {
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        if (AppUtils.getCurApp(getContext()) != 0) {//非教练APP
          if (gymWrapper.isPro()) {
            if (hasOrder) {
              showAlert(R.string.alert_batch_has_ordered);
              return true;
            } else {
              return false;
            }
          } else {
            UpgradeInfoDialogFragment.newInstance(QRActivity.getIdentifyKey("course_batch_pay"))
                .show(getFragmentManager(), "");
            return true;
          }
        } else {// 教练App
          UseStaffAppFragmentFragment.newInstance().show(getChildFragmentManager(), "");
          return true;
        }
      } else {
        return false;
      }
    });
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    elPay.setExpanded(AppUtils.getCurApp(getActivity()) != 0);
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
    PhotoUtils.small(img, course.getPhoto());
    text1.setText(course.getName());
    text3.setText(getString(R.string.course_d_lenght, course.getLength() / 60));
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
    if (staff == null) return;
    if (coach == null) return;
    this.trainer = staff;
    coach.setContent(staff.getUsername());
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
    space.setContent(ListUtils.ListObj2StrCN(spaces));
  }

  /**
   * 设置可约人数
   */
  public void setOrderSutdentCount(@IntRange(from = 1) int x) {
    if (orderSutdentCount != null) orderSutdentCount.setContent(Integer.toString(x));
  }

  public void setMutlSupport(boolean support) {
    elMultiSupport.setExpanded(support);
    numHasChange = false;
  }

  public boolean mutilSupportble() {
    return elMultiSupport != null && elMultiSupport.isExpanded();
  }

  public boolean needPay() {
    return elPay == null || elPay.isExpanded();
  }

  public void openPay(boolean open) {
    if (elPay != null) elPay.setExpanded(open);
  }

  public void openPayOnline(boolean open) {
    if (open) {
      payOnline.setContent("已开启");
    } else {
      payOnline.setContent("未设置");
    }
  }

  public void setRules(List<Rule> rules, ArrayList<CardTplBatchShip> ships) {
    List<Rule> rules1 = new ArrayList<>();
    if (rules != null) {
      for (Rule rule : rules) {
        if (rule.channel.equalsIgnoreCase("ONLINE")) {
          payOnlineRule = rule;
        } else {
          rules1.add(rule);
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

  /**
   * 更改课程
   */
 public void onCourseLayoutClicked() {
    routeTo("course", "/choose/",
        CourseChooseParams.builder().src(mSource).mIsPrivate(isPrivate).build());
  }

  /**
   * 更改教练
   */
 public void onCoachClicked() {
    if (AppUtils.getCurApp(getContext()) == 0) return;
    routeTo("staff", "/trainer/choose/",
        new TrainerChooseParams().selectedId(trainer != null ? trainer.getId() : null).build());
  }

  /**
   * 更改场地
   */
 public void onSpaceClicked() {
    routeTo("gym", "/site/choose/", new SiteSelectedParams().isPrivate(isPrivate)
        .selectIds(ListUtils.getIdList(spaces))
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
    routeTo("/batch/pay/online/",
        new cn.qingchengfit.saasbase.course.batch.views.BatchPayOnlineParams().rule(payOnlineRule)
            .maxPeople(getOrderStudentCount())
            .multiPrice(elMultiSupport.isExpanded())
            .build());
  }

  /**
   * 卡支付设置
   */
 public void onPayCardClicked() {
    boolean isAdd ;
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

  public List<String> getSupportSpace() {
    return ListUtils.getIdList(spaces);
  }

  public List<Rule> getRules() {
    List<Rule> rules = new ArrayList<>();
    if (!numHasChange) {
      if (payOnlineRule != null) rules.add(payOnlineRule);
      if (rulesPayCards.size() > 0) rules.addAll(rulesPayCards);
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

  public interface BatchTempleListener {
    public void onBatchTemple();
  }
}
