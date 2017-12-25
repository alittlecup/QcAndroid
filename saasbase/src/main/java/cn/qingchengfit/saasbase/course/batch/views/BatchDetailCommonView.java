package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.cards.views.BatchPayCardParams;
import cn.qingchengfit.saasbase.coach.event.EventStaffWrap;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseParams;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.events.EventPayOnline;
import cn.qingchengfit.saasbase.gymconfig.event.EventSiteSelected;
import cn.qingchengfit.saasbase.gymconfig.views.SiteSelectedParams;
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

  @BindView(R2.id.img) ImageView img;
  @BindView(R2.id.img_layout) FrameLayout imgLayout;
  @BindView(R2.id.img_foot) ImageView imgFoot;
  @BindView(R2.id.text1) TextView text1;
  @BindView(R2.id.text3) TextView text3;
  @BindView(R2.id.righticon) ImageView righticon;
  @BindView(R2.id.course_layout) RelativeLayout courseLayout;
  @BindView(R2.id.coach) CommonInputView coach;
  @BindView(R2.id.space) CommonInputView space;
  @BindView(R2.id.order_sutdent_count) CommonInputView orderSutdentCount;
  @BindView(R2.id.pay_online) CommonInputView payOnline;
  @BindView(R2.id.pay_card) CommonInputView payCard;
  @BindView(R2.id.el_pay) ExpandedLayout elPay;
  @BindView(R2.id.el_multi_support) ExpandedLayout elMultiSupport;

  private Course course;
  private Staff trainer;
  private ArrayList<Rule> rulesPayCards = new ArrayList<>();
  private Rule payOnlineRule;
  private List<Space> spaces = new ArrayList<>();
  private ArrayList<CardTplBatchShip> cardtplships;
  private boolean numHasChange = false;//人数已经修改

  public static BatchDetailCommonView newInstance(Course course, Staff trainer) {
    Bundle args = new Bundle();
    args.putParcelable("course", course);
    args.putParcelable("trainer", trainer);
    BatchDetailCommonView fragment = new BatchDetailCommonView();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      course = getArguments().getParcelable("course");
      trainer = getArguments().getParcelable("trainer");
      if (course == null) {
        course = new Course();
        course.is_private = true;
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
      .register(Course.class)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Course>() {
        @Override public void onNext(Course course) {
          setCourse(course);
          BatchDetailCommonView.this.course = course;
        }
      });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(course.is_private ? R.layout.fragment_batch_detail_common_private
      : R.layout.fragment_batch_detail_common_group, container, false);
    unbinder = ButterKnife.bind(this, view);
    setCourse(course);
    setTrainer(trainer);
    RxBusAdd(EventBatchPayCard.class).onBackpressureDrop()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventBatchPayCard>() {
        @Override public void onNext(EventBatchPayCard eventBatchPayCard) {
          setCardRule(eventBatchPayCard.getRules(), null);
          numHasChange = false;
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
    elMultiSupport.setOnCheckedChangeListener((compoundButton, b) -> {
      if (b && getOrderStudentCount() > 1){
        numHasChange = true;
        payCard.setContent("已修改多人支持，请重新设置");
      }
    });
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    elPay.setExpanded(true);
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

  public void setMutlSupport(boolean support){
    elMultiSupport.setExpanded(support);
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
    setCardRule(rules1, ships);
  }

  private void setCardRule(List<Rule> rules, ArrayList<CardTplBatchShip> ships) {
    rulesPayCards.clear();
    if (rules != null && rules.size() > 0) {
      rulesPayCards.addAll(rules);
      List<String> cardids = new ArrayList<>();
      for (Rule rule : rules) {
        if (!cardids.contains(rule.card_tpl_id)){
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
  @OnClick(R2.id.course_layout) public void onCourseLayoutClicked() {
    routeTo("course", "/choose/", null);
  }

  /**
   * 更改教练
   */
  @OnClick(R2.id.coach) public void onCoachClicked() {
    if (AppUtils.getCurApp(getContext()) == 0) return;
    routeTo("staff", "/trainer/choose/",
      new TrainerChooseParams().selectedId(trainer != null ? trainer.getId() : null).build());
  }

  /**
   * 更改场地
   */
  @OnClick(R2.id.space) public void onSpaceClicked() {
    routeTo("gym", "/site/choose/", new SiteSelectedParams().isPrivate(course.is_private)
      .selectIds(ListUtils.getIdList(spaces))
      .build());
  }

  /**
   * 更改上课人数
   */
  @OnClick(R2.id.order_sutdent_count) public void onOrderSutdentCountClicked() {
    new DialogList(getContext()).list(
      course.is_private ? CmStringUtils.getNums(1, 10) : CmStringUtils.getNums(1, 300),
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
  @OnClick(R2.id.pay_online) public void onPayOnlineClicked() {
    routeTo("/batch/pay/online/",
      new cn.qingchengfit.saasbase.course.batch.views.BatchPayOnlineParams().rule(payOnlineRule)
        .maxPeople(elMultiSupport.isExpanded() ? getOrderStudentCount() : 1)
        .multiPrice(elMultiSupport.isExpanded())
        .build());
  }

  /**
   * 卡支付设置
   */
  @OnClick(R2.id.pay_card) public void onPayCardClicked() {
    routeTo("card", "/card/batch/choose/", new BatchPayCardParams().rules(rulesPayCards)
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
}
