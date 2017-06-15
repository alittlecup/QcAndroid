package com.qingchengfit.fitcoach.fragment.batch.single;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.Coach;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.SingleBatchBody;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.bean.CardTplBatchShip;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
 * Created by Paper on 2017/1/22.
 */
@FragmentWithArgs public class SingleBatchFragment extends BaseFragment implements SingleBatchPresenter.MVPView {
    public static final int RESULT_COURSE = 11;
    public static final int RESULT_ACCOUNT = 12;
    @Arg public boolean mIsPrivate;
    @Arg public String mSingleBatchid;
    @BindView(R.id.img) ImageView img;
    //@BindView(R.id.img_layout) FrameLayout imgLayout;
    //@BindView(R.id.img_foot) ImageView imgFoot;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    //@BindView(R.id.course_layout) RelativeLayout courseLayout;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.space) CommonInputView space;
    @BindView(R.id.account_type) CommonInputView accountType;
    @BindView(R.id.course_date) CommonInputView courseDate;
    @BindView(R.id.course_time) CommonInputView courseTime;
    @BindView(R.id.btn_del) TextView btnDel;
    @Inject SingleBatchPresenter mSingleBatchPresenter;
    @BindView(R.id.root_scroll) ScrollView rootScroll;
    @Inject CoachService coachService;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private SingleBatchBody mBody = new SingleBatchBody();
    private ArrayList<Rule> mCurRules;
    private boolean mHasOrder;
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    //private List<CardTplBatchShip> mCardTplBatchShips;
    private TimeDialogWindow pwTime;
    private Date mStart;//开始时间
    private Date mEnd; //结束时间

    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (courseDate.isEmpty()) {
                ToastUtils.show("课程时间不能为空");
                return true;
            }
            showLoading();
            if (mIsPrivate) {
                mBody.start = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent().split("-")[0] + ":00";
                mBody.end = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent().split("-")[1] + ":00";
            } else {
                mBody.start = courseDate.getContent().split(" ")[0] + "T" + courseTime.getContent() + ":00";
            }
            mSingleBatchPresenter.saveSingleBatch(App.coachid + "", mSingleBatchid, mIsPrivate, mBody);
            return true;
        }
    };

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        //getAppComponent().inject(this);
        if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);
        }
        delegatePresenter(mSingleBatchPresenter, this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setText("课程排期");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        //mCallbackActivity.setToolbar("课程排期", false, null, R.menu.menu_save, menuItemClickListener);
        showLoading();
        mSingleBatchPresenter.querySingleBatchId(App.coachid + "", mIsPrivate, mSingleBatchid);
        accountType.setContent("已设置");
        courseTime.setLabel(mIsPrivate ? "可约时间段" : "课程时间");
        //RxBusAdd(DoneAccountEvent.class).subscribe(new Action1<DoneAccountEvent>() {
        //    @Override public void call(DoneAccountEvent doneAccountEvent) {
        //        mBody.max_users = doneAccountEvent.max_user;
        //        mBody.max_users = doneAccountEvent.max_user;
        //        mBody.rule = doneAccountEvent.rules;
        //        mCurRules = mBody.rule;
        //        mBody.is_free = doneAccountEvent.isFree;
        //    }
        //});
        //RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
        //    @Override public void call(EventFresh eventFresh) {
        //        if (mIsPrivate) {
        //            mCallbackActivity.setToolbar("私教排期", false, null, R.menu.menu_save, menuItemClickListener);
        //        } else {
        //            mCallbackActivity.setToolbar("团课排期", false, null, R.menu.menu_save, menuItemClickListener);
        //        }
        //    }
        //});
        return view;
    }

    @Override public String getFragmentName() {
        return SingleBatchFragment.class.getName();
    }

    @OnClick({ R.id.course_date, R.id.course_time, R.id.btn_del }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_date:
                if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(date -> {
                    courseDate.setContent(
                        DateUtils.Date2YYYYMMDD(date) + "  " + getResources().getStringArray(R.array.weeks)[DateUtils.getDayOfWeek(date)]);
                    pwTime.dismiss();
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, mStart == null ? new Date() : mStart);
                break;
            case R.id.course_time:
                if (!mIsPrivate) {
                    if (timeWindow == null) {
                        timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
                    }

                    timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                        @Override public void onTimeSelect(Date date) {

                            courseTime.setContent(DateUtils.getTimeHHMM(date));
                        }
                    });
                    timeWindow.showAtLocation(rootScroll, Gravity.BOTTOM, 0, 0, mStart);
                } else {
                    if (timeDialogWindow == null) {
                        timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
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
                mSingleBatchPresenter.delSingleBatchId(App.coachid + "", mIsPrivate, mSingleBatchid);
                break;
        }
    }

    @OnClick({ R.id.layout_course, R.id.coach, R.id.space, R.id.account_type }) public void onClickChange(View view) {
        switch (view.getId()) {
            case R.id.layout_course:
                Intent toChooseCourse = new Intent(getActivity(), CourseActivity.class);
                toChooseCourse.putExtra("to", CourseActivity.TO_CHOOSE);
                toChooseCourse.putExtra("type", mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
                toChooseCourse.putExtra("service", coachService);
                startActivityForResult(toChooseCourse, RESULT_COURSE);
                break;
            case R.id.coach:
                break;
            case R.id.space:
                Intent toChooseSpace = new Intent(getActivity(), FragActivity.class);
                toChooseSpace.putExtra("type", 11);
                toChooseSpace.putExtra("course_type", mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
                toChooseSpace.putExtra("service", coachService);
                startActivityForResult(toChooseSpace, 5);
                break;
            case R.id.account_type:
                //getFragmentManager().beginTransaction()
                //    .add(R.id.frag, SetAccountTypeAddFragment.newInstance(mIsPrivate ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP, mCurRules,
                //        mBody.max_users, mBody.is_free, mHasOrder, mCardTplBatchShips))
                //    .addToBackStack(null)
                //    .commit();
                Intent toAccount = new Intent(getActivity(), FragActivity.class);
                toAccount.putExtra("type", 12);
                toAccount.putExtra("count", mBody.max_users == 0 ? 8 : mBody.max_users);
                toAccount.putExtra("isfree", mBody.is_free);
                toAccount.putExtra("service", coachService);
                startActivityForResult(toAccount, RESULT_ACCOUNT);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {       //私教 教练
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
                ArrayList<Space> spaces = data.getParcelableArrayListExtra("spaces");
                if (spaces != null) {
                    String spaceStr = "";
                    List<String> ids = new ArrayList<>();
                    for (int i = 0; i < spaces.size(); i++) {
                        ids.add(spaces.get(i).id);
                        if (TextUtils.isEmpty(spaceStr)) {
                            spaceStr = spaceStr.concat(spaces.get(i).name);
                        } else {
                            spaceStr = spaceStr.concat("、").concat(spaces.get(i).name);
                        }
                    }
                    if (ids.size() > 1) {
                        space.setContent(getString(R.string.d_spaces, ids.size()));
                    } else {
                        space.setContent(spaceStr);
                    }
                    mBody.spaces = ids;
                }
            } else if (requestCode == RESULT_COURSE) {
                CourseDetail course = data.getParcelableExtra("course");
                onCourse(course);
            } else if (requestCode == RESULT_ACCOUNT) {
                int count = data.getIntExtra("count", 1);
                mBody.max_users = count;
                accountType.setContent(getString(R.string.has_set));
            }
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onCoach(Coach teacher) {
        hideLoading();
        //if (mIsPrivate) {
        //    Glide.with(getContext())
        //        .load(PhotoUtils.getSmall(teacher.header))
        //        .asBitmap()
        //        .placeholder(R.drawable.ic_default_head_nogender)
        //        .into(new CircleImgWrapper(img, getContext()));
        //    text1.setText(teacher.username);
        //} else {
        //
        //    coach.setLabel("教练");
        //    coach.setContent(teacher.username);
        //}
        coach.setLabel("教练");
        coach.setContent(teacher.name);
        mBody.teacher_id = teacher.id;
    }

    @Override public void onCourse(CourseDetail course) {
        hideLoading();
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
        mCurRules = (ArrayList<Rule>) rules;
        mBody.max_users = max_user;
        mBody.rule = (ArrayList<Rule>) rules;
        mBody.is_free = isFree;
        //mCardTplBatchShips = cardTplBatchShips;
        mHasOrder = hasOrder;
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

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.show("修改成功");
    }

    @Override public void onFailed() {
        hideLoading();
    }

    @Override public void onDelOk() {
        hideLoading();
        getActivity().onBackPressed();
    }
}
