package cn.qingchengfit.staffkit.views.batch.single;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.common.BatchOpenRule;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.custom.TimePeriodChooser;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;
import cn.qingchengfit.staffkit.views.gym.ChooseGroupCourseFragment;
import cn.qingchengfit.staffkit.views.gym.site.MutiChooseSiteFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/1/22.
 */
@FragmentWithArgs public class SingleBatchFragment extends BaseFragment implements SingleBatchPresenter.MVPView {

    @Arg public boolean mIsPrivate;
    @Arg public String mSingleBatchid;
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.img_layout) FrameLayout imgLayout;
    @BindView(R.id.img_foot) ImageView imgFoot;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    @BindView(R.id.course_layout) RelativeLayout courseLayout;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.space) CommonInputView space;
    //@BindView(R.id.account_type) CommonInputView accountType;
    @BindView(R.id.course_date) CommonInputView courseDate;
    @BindView(R.id.course_time) CommonInputView courseTime;
    @Inject SingleBatchPresenter mSingleBatchPresenter;
    @BindView(R.id.root_scroll) ScrollView rootScroll;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.sw_need_pay) SwitchCompat swNeedPay;
    @BindView(R.id.layout_need_pay) LinearLayout layoutNeedPay;
    @BindView(R.id.can_not_close) View canNotClose;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.pay_online) CommonInputView payOnline;
    @BindView(R.id.pay_card) CommonInputView payCard;
    @BindView(R.id.order_sutdent_count) CommonInputView orderSutdentCount;
    @BindView(R.id.btn_del) TextView btnDel;
    @BindView(R.id.tag_pro) ImageView tagPro;
    @BindView(R.id.civ_to_open_time) CommonInputView civOpenTime;
    @BindArray(R.array.order_open_time) String[] arrayOpenTime;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    /**
     * 选择准确的时间
     */
    TimeDialogWindow chooseOpenTimeDialog;
    private SingleBatchBody mBody = new SingleBatchBody();
    private boolean mHasOrder;
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private TimeDialogWindow pwTime;
    private Date mStart;//开始时间
    private Date mEnd; //结束时间
    private Rule ruleOnline;
    private List<Rule> rulesPayCards = new ArrayList<>();
    private ArrayList<CardTplBatchShip> mCardTplBatchShips;
    private boolean isChangeCardPay;
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (courseDate.isEmpty()) {
                ToastUtils.show("课程时间不能为空");
                return true;
            }
            if (mBody.max_users != Integer.valueOf(orderSutdentCount.getContent()) && !isChangeCardPay&& rulesPayCards!= null && rulesPayCards.size() >0) {
                showAlert("会员卡结算未填写完整");
                return true;
            }
            if (mIsPrivate) {
                mBody.start = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent().split("-")[0] + ":00";
                mBody.end = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent().split("-")[1] + ":00";
            } else {
                mBody.start = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent() + ":00";
            }
            mBody.max_users = Integer.valueOf(orderSutdentCount.getContent());
            if (mSingleBatchPresenter.getBatchOpenRule() == null){
                ToastUtils.show("请设置何时开放约课");
                return true;
            }
            mBody.open_rule = mSingleBatchPresenter.getBatchOpenRule();
            if (swNeedPay.isChecked()) {
                mBody.is_free = false;
                if (mBody.rule == null) {
                    mBody.rule = new ArrayList<>();
                } else {
                    mBody.rule.clear();
                }
                if (ruleOnline != null) {
                    ruleOnline.to_number = mBody.max_users+1;
                    mBody.rule.add(ruleOnline);
                }
                mBody.rule.addAll(rulesPayCards);
                if (mBody.rule.size() == 0) {
                    cn.qingchengfit.utils.ToastUtils.show("请至少选择一种支付方式");
                    return true;
                }
            } else {
                mBody.is_free = true;
            }
            showLoading();
            mSingleBatchPresenter.saveSingleBatch(App.staffId, mSingleBatchid, mIsPrivate, mBody);
            return true;
        }
    };
    private DialogList stucount;
    private MaterialDialog exitDialg;
    private boolean proGym;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);

        delegatePresenter(mSingleBatchPresenter, this);
        showLoading();
        mSingleBatchPresenter.querySingleBatchId(App.staffId, mIsPrivate, mSingleBatchid);
        //accountType.setContent("已设置");

        proGym = gymWrapper.isPro();
        tagPro.setVisibility(proGym ? View.GONE : View.VISIBLE);
        canNotClose.setVisibility(proGym ? View.GONE : View.VISIBLE);
        btnDel.setText(mIsPrivate ? "删除该排期" : "删除该课程");
        courseTime.setLabel(mIsPrivate ? "可约时间段" : "课程时间");
        //RxBusAdd(DoneAccountEvent.class).subscribe(new Action1<DoneAccountEvent>() {
        //    @Override public void call(DoneAccountEvent doneAccountEvent) {
        //        mBody.rule = doneAccountEvent.rules;
        //        rulesPayCards = mBody.rule;
        //        mBody.is_free = doneAccountEvent.isFree;
        //    }
        //});
        RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
            @Override public void call(EventFresh eventFresh) {
                if (mIsPrivate) {
                    mCallbackActivity.setToolbar("私教排期", false, null, R.menu.menu_save, menuItemClickListener);
                } else {
                    mCallbackActivity.setToolbar("团课排期", false, null, R.menu.menu_save, menuItemClickListener);
                }
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(mIsPrivate ? "课程排期" : "课程");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    @Override public boolean onFragmentBackPress() {
        if (exitDialg == null) {
            exitDialg = DialogUtils.instanceDelDialog(getContext(), "确定放弃本次编辑？", new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    getFragmentManager().popBackStackImmediate();
                }
            });
        }
        if (!exitDialg.isShowing()) exitDialg.show();
        return true;
    }

    @Override public String getFragmentName() {
        return SingleBatchFragment.class.getName();
    }

    @OnClick({ R.id.course_date, R.id.course_time, R.id.btn_del }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_date:
                break;
            case R.id.course_time:
                if (!mIsPrivate) {
                    if (timeWindow == null) {
                      timeWindow =
                          new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
                    }

                    timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                        @Override public void onTimeSelect(Date date) {

                            courseTime.setContent(DateUtils.getTimeHHMM(date));
                        }
                    });
                    timeWindow.showAtLocation(rootScroll, Gravity.BOTTOM, 0, 0, mStart);
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
                            if (start.getTime() <= new Date().getTime()) {
                                ToastUtils.showDefaultStyle("开始时间不能小于当前时间");
                                return;
                            }
                            courseTime.setContent(DateUtils.getTimeHHMM(start) + "-" + DateUtils.getTimeHHMM(end));
                        }
                    });
                    timeDialogWindow.setTime(mStart, mEnd);
                    timeDialogWindow.showAtLocation();
                }

                break;
            case R.id.btn_del:
                mSingleBatchPresenter.delSingleBatchId(App.staffId, mIsPrivate, mSingleBatchid);
                break;
        }
    }

    @OnClick({ R.id.course_layout, R.id.coach, R.id.space }) public void onClickChange(View view) {
        switch (view.getId()) {
            case R.id.course_layout:

                if (mIsPrivate) {
                    ChooseCoachFragment.start(this, 1, "", Configs.INIT_TYPE_ADD);
                } else {
                    ChooseGroupCourseFragment.start(this, 2, "", mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
                }
                break;
            case R.id.coach:
                if (!mIsPrivate) {
                    ChooseCoachFragment.start(this, 3, "", Configs.INIT_TYPE_ADD);
                } else {
                    ChooseGroupCourseFragment.start(this, 4, "", mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
                }

                break;
            case R.id.space:
                MutiChooseSiteFragment.start(this, 5, mBody.shop_id, mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {       //私教 教练
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                String imgUrl = IntentUtils.getIntentString(data, 2);
                Glide.with(getActivity()).load(PhotoUtils.getSmall(imgUrl)).asBitmap().into(new CircleImgWrapper(img, getActivity()));
                text1.setText(name);
                mBody.teacher_id = id;
            } else if (requestCode == 3) { //团课 教练
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                String imgUrl = IntentUtils.getIntentString(data, 2);
                coach.setContent(name);
                mBody.teacher_id = id;
            } else if (requestCode == 4) { //私教课程
                CourseTypeSample course = data.getParcelableExtra("course");
                coach.setContent(course.getName());
                mBody.course_id = course.getId();
            } else if (requestCode == 2) { //团课教程
                CourseTypeSample course = data.getParcelableExtra("course");
                Glide.with(getActivity())
                    .load(PhotoUtils.getSmall(course.getPhoto()))
                    .asBitmap()
                    .into(new CircleImgWrapper(img, getActivity()));
                text1.setText(course.getName());
                mBody.course_id = course.getId();
            } else if (requestCode == 5) { //选择场地
                List<String> ids = data.getStringArrayListExtra("ids");
                String names = data.getStringExtra("string");
                space.setContent(names);
                mBody.spaces = ids;
            } else if (requestCode == ChooseActivity.BATCH_PAY_ONLINE) {
                try {
                    ruleOnline = (Rule) IntentUtils.getParcelable(data);
                    payOnline.setHint("已开启");
                } catch (Exception e) {
                    ruleOnline = null;
                    payOnline.setHint("未开启");
                }
            } else if (requestCode == ChooseActivity.BATCH_PAY_CARD) {
                isChangeCardPay = true;
                ArrayList<Rule> rules = data.getParcelableArrayListExtra("rules");
                int maxCount = data.getIntExtra("count", 0);
                rulesPayCards.clear();
                rulesPayCards.addAll(rules);
                if (maxCount > 0) {
                    payCard.setHint(getString(R.string.batch_can_pay_card_count, maxCount));
                } else {
                    payCard.setHint(getString(R.string.common_un_setting));
                }
            }
        }
    }

    /**
     * 开启是否支付
     *
     * @param isChecked 需要支付
     */
    @OnCheckedChanged(R.id.sw_need_pay) public void swNeedPay(boolean isChecked) {
        payCard.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        payOnline.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        divider.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onCoach(Staff teacher) {
        hideLoading();
        if (mIsPrivate) {
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(teacher.avatar))
                .asBitmap()
                .placeholder(R.drawable.ic_default_head_nogender)
                .into(new CircleImgWrapper(img, getContext()));
            text1.setText(teacher.username);
        } else {

            coach.setLabel("教练");
            coach.setContent(teacher.username);
        }
        mBody.teacher_id = teacher.id;
    }

    @Override public void onCourse(CourseTypeSample course) {

        if (!mIsPrivate) {
            Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.ic_default_header).into(img);
            text1.setText(course.getName());
            text3.setText("时长" + course.getLength() / 60 + "分钟");
        } else {
            coach.setLabel("课程");
            coach.setContent(course.getName());
        }
        mBody.course_id = course.getId();
    }

    @Override public void onSpace(Space space, List<Space> spaces) {
        if (mIsPrivate) {
            String sp = "";
            List<String> spaceStrs = new ArrayList<>();

            for (int i = 0; i < ((List) spaces).size(); i++) {
                Object e = ((List) spaces).get(i);
                if (e instanceof Space) {
                    spaceStrs.add(((Space) e).getId());
                    if (i < ((List) spaces).size() - 1) {
                        sp = TextUtils.concat(sp, ((Space) e).getName(), ",").toString();
                    } else {
                        sp = TextUtils.concat(sp, ((Space) e).getName()).toString();
                    }
                }
            }
            this.space.setContent(sp);
            mBody.spaces = spaceStrs;
        } else {
            this.space.setContent(space.getName());
            mBody.spaces = new ArrayList<>();
            mBody.spaces.add(space.getId());
        }
    }

    @Override
    public void onRule(List<Rule> rules, int max_user, boolean isFree, List<CardTplBatchShip> cardTplBatchShips, boolean hasOrder) {
        if (rules != null) {
            rulesPayCards.clear();
            for (int i = 0; i < rules.size(); i++) {
                if (rules.get(i).channel.equals(Configs.CHANNEL_CARD)) {
                    rulesPayCards.add(rules.get(i));
                } else {
                    ruleOnline = rules.get(i);
                }
            }
        }
        mBody.max_users = max_user;
        mBody.rule = (ArrayList<Rule>) rules;
        mBody.is_free = isFree;
        mCardTplBatchShips = (ArrayList<CardTplBatchShip>) cardTplBatchShips;
        mHasOrder = hasOrder;
        canNotClose.setVisibility(hasOrder ? View.VISIBLE : View.GONE);
        swNeedPay.setChecked(!isFree);
        payCard.setHint(getString(R.string.batch_can_pay_card_count, mCardTplBatchShips.size()));
        payOnline.setHint(ruleOnline == null ? "未开启" : "已开启");
        orderSutdentCount.setContent(max_user+"");
        mHasOrder = hasOrder;
        canNotClose.setVisibility(hasOrder || !proGym ? View.VISIBLE : View.GONE);
    }

    @Override public void onDate(Date start, Date end) {
        mStart = start;
        mEnd = end;
        courseDate.setContent(
            DateUtils.Date2YYYYMMDD(start) + "  " + getResources().getStringArray(R.array.weeks)[DateUtils.getDayOfWeek(start)]);
        courseTime.setContent(DateUtils.getTimeHHMM(start) + (mIsPrivate ? ("-" + DateUtils.getTimeHHMM(end)) : ""));
    }

    @Override public void checkOk() {

    }

    @Override public void onOpenRule(BatchOpenRule rule) {
        if (rule != null ){
            if (rule.type <3 && !TextUtils.isEmpty(rule.open_datetime))
                civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(rule.open_datetime)));
            else if (rule.type == 3 && rule.advance_hours != null){
                if (mStart != null)
                    civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(DateUtils.addHour(mStart,-rule.advance_hours)));
            }
        }
    }

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.show("修改成功");
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override public void onFailed() {
        hideLoading();
    }

    @Override public void onDelOk() {
        hideLoading();
        getFragmentManager().popBackStackImmediate();
    }

    /**
     * 点击在线支付
     */
    @OnClick(R.id.pay_online) public void clickPayOnline() {
        Intent toPayOnline =
            IntentUtils.getChooseIntent(getContext(), gymWrapper.getCoachService(), gymWrapper.getBrand(), ChooseActivity.BATCH_PAY_ONLINE);
        toPayOnline.putExtra("rule", ruleOnline);
        try {
            toPayOnline.putExtra("max", Integer.parseInt(orderSutdentCount.getContent()));
        } catch (Exception e) {

        }
        startActivityForResult(toPayOnline, ChooseActivity.BATCH_PAY_ONLINE);
    }

    /**
     * 点击配置卡支付
     */
    @OnClick(R.id.pay_card) public void clickPayCard() {
        Intent toPayOnline =
            IntentUtils.getChooseIntent(getContext(), gymWrapper.getCoachService(), gymWrapper.getBrand(), ChooseActivity.BATCH_PAY_CARD);
        toPayOnline.putExtra("rules", (ArrayList) rulesPayCards);
        toPayOnline.putExtra("private", mIsPrivate);
        toPayOnline.putExtra("order", mCardTplBatchShips);
        try {
            toPayOnline.putExtra("max", Integer.parseInt(orderSutdentCount.getContent()));
        } catch (Exception e) {
            toPayOnline.putExtra("max", 8);
        }
        startActivityForResult(toPayOnline, ChooseActivity.BATCH_PAY_CARD);
    }

    /**
     * 选择数量
     */
    @OnClick(R.id.order_sutdent_count) public void clickStuCount() {
        if (stucount == null) {
            stucount = new DialogList(getContext()).list(mIsPrivate ? StringUtils.getNums(1, 10) : StringUtils.getNums(1, 300),
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if ((position + 1) != mBody.max_users) {
                            isChangeCardPay = false;
                        }
                        orderSutdentCount.setContent(Integer.toString(position + 1));
                        payCard.setHint("已修改可约人数，请重新设置");
                        stucount.dismiss();
                    }
                }).title("选择人数");
        }
        stucount.show();
    }

    @OnClick(R.id.civ_to_open_time)
    public void clickOpenTime(){
        chooseOpenTime();
    }

    public void chooseOpenTime(){
        if (chooseOpenTimeDialog == null){
            chooseOpenTimeDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.ALL);
            chooseOpenTimeDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    civOpenTime.setContent(DateUtils.Date2YYYYMMDDHHmm(date));
                    mSingleBatchPresenter.setOpenRuleType(2);
                    mSingleBatchPresenter.setOpenRuleTime(DateUtils.formatToServer(date),null);
                }
            });
        }
        chooseOpenTimeDialog.setRange(DateUtils.getYear(new Date())-1,DateUtils.getYear(new Date())+1);
        Date d = new Date();
        if (!TextUtils.isEmpty(civOpenTime.getContent())){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm", Locale.CHINA);
                d = formatter.parse(civOpenTime.getContent());
            }catch (Exception e){

            }

        }
        chooseOpenTimeDialog.showAtLocation(getView(),Gravity.BOTTOM, 0, 0, d);
    }

    @OnClick(R.id.can_not_close) public void canNotClose() {
        if (proGym) {
            showAlert(R.string.alert_batch_has_ordered);
        } else {
            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
        }
    }
}
