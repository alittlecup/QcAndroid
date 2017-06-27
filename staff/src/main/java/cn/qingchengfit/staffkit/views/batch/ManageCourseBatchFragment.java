package cn.qingchengfit.staffkit.views.batch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.common.Batch;
import cn.qingchengfit.model.responese.CmBean;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.DoneAccountEvent;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.rxbus.event.FreshGymListEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxCompleteGuideEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxbusBatchLooperConfictEvent;
import cn.qingchengfit.staffkit.rxbus.event.SaveEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.CmAdapter;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.TimePeriodChooser;
import cn.qingchengfit.staffkit.views.gym.BatchPresenter;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.staffkit.views.gym.SetAccountTypeFragment;
import cn.qingchengfit.staffkit.views.gym.addcourse.AddGuideCourseFragment;
import cn.qingchengfit.staffkit.views.gym.cycle.AddCycleFragmentBuilder;
import cn.qingchengfit.staffkit.views.gym.site.ChooseSiteFragment;
import cn.qingchengfit.staffkit.views.login.SplashActivity;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static cn.qingchengfit.staffkit.views.gym.GymActivity.GYM_TO;

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
 * Created by Paper on 16/1/28 2016.
 */
public class ManageCourseBatchFragment extends BaseFragment implements ManageCourseView {
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.img_foot) ImageView imgFoot;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.place) CommonInputView place;
    @BindView(R.id.account_type) CommonInputView accountType;
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.add) TextView add;
    @BindView(R.id.btn) Button btn;

    @Inject BatchPresenter batchPresenter;
    @BindView(R.id.guide_step_2) ImageView guideStep2;
    List<CmBean> datas = new ArrayList<>();
    private TimeDialogWindow pwTime;
    private View view;
    private CmAdapter adapter;
    private int mType;
    private CourseTypeSample mCourse;
    private Staff mTeacher;

    private DialogList dialogList;
    private String[] weeks = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private Observable<DoneAccountEvent> RxObDone;
    private SystemInitBody body;
    private Observable<CmBean> RxObCmBean;
    private ArrangeBatchBody arrangeBatchBody;

    public static ManageCourseBatchFragment newInstance(int type, CourseTypeSample course, Staff teacher) {
        Bundle args = new Bundle();
        ManageCourseBatchFragment fragment = new ManageCourseBatchFragment();
        args.putParcelable("course", course);
        args.putParcelable("teacher", teacher);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type", AddGuideCourseFragment.COURSE_GUIDE);
            mCourse = getArguments().getParcelable("course");
            mTeacher = getArguments().getParcelable("teacher");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_course_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDI();

        adapter = new CmAdapter(datas, mCourse.is_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                datas.remove(pos);
                adapter.notifyDataSetChanged();
                checkBtn();
            }
        });
        starttime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        endtime.setContent(DateUtils.getEndDayOfMonthNew(new Date()));
        RxObDone = RxBus.getBus().register(DoneAccountEvent.class);
        RxObDone.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<DoneAccountEvent>() {
            @Override public void call(DoneAccountEvent doneAccountEvent) {
                accountType.setContent(getString(R.string.common_have_setting));
                checkBtn();
            }
        });
        RxObCmBean = RxBus.getBus().register(CmBean.class);
        RxObCmBean.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CmBean>() {
            @Override public void call(CmBean cmBean) {
                //添加CmBean
                try {
                    if (cmBean.week == null || cmBean.week.size() == 0 || cmBean.dateStart == null || cmBean.dateEnd == null) {
                        return;
                    }

                    if (CmBean.CheckCmBean(datas, cmBean)) {
                        //添加CmBean
                        datas.add(cmBean);
                        adapter.notifyDataSetChanged();
                        RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                        checkBtn();
                    } else {
                        RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                    }
                } catch (Exception e) {

                }
            }
        });

        mCallbackActivity.setToolbar(getString(R.string.titile_manage_course), false, null, 0, null);
        initInfo();
        righticon.setVisibility(View.VISIBLE);
        batchPresenter.attachView(this);
        return view;
    }

    private void initDI() {
    }

    private void initInfo() {

        if (mType == AddGuideCourseFragment.COURSE_GUIDE) {
            body = (SystemInitBody) App.caches.get("init");
            if (body != null && body.spaces.size() == 0) {
                body.spaces.add(new Space("默认场地", "50", true, true));

                place.setContent("默认场地");
            }

            if (body != null && body.courses != null && body.courses.size() > 0) {
                CourseTypeSample course = body.courses.get(0);
                Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.ic_default_header).into(img);
                text1.setText(course.getName());
                text3.setText(String.format("时长%d分钟", course.getLength() / 60));
                if (mType == Configs.TYPE_PRIVATE) {
                    imgFoot.setImageResource(R.drawable.ic_course_type_private);
                } else {
                    imgFoot.setImageResource(R.drawable.ic_course_type_group);
                }
            }
            if (body.batches != null && body.batches.size() > 0) {
                if (body.batches.get(0).getRules() != null && body.batches.get(0).getRules().size() > 0) {
                    accountType.setContent(getString(R.string.common_have_setting));
                }
                if (!TextUtils.isEmpty(body.batches.get(0).getSpace_name())) {
                    place.setContent(body.batches.get(0).getSpace_name());
                }
                if (body.teachers != null && body.teachers.size() > 0) {
                    for (Staff teacher : body.teachers) {
                        if (teacher.getPhone().equalsIgnoreCase(body.batches.get(0).getTeacher_phone())) {
                            coach.setContent(teacher.getUsername());
                        }
                    }
                }
                if (body.batches.get(0).getTime_repeats() != null && body.batches.get(0).getTime_repeats().size() > 0) {
                    datas.clear();
                    HashMap<String, CmBean> b = new HashMap<>();
                    for (Time_repeat time_repeat : body.batches.get(0).getTime_repeats()) {
                        String p = time_repeat.getStart() + time_repeat.getEnd();
                        if (b.get(p) == null) {
                            b.put(p, new CmBean());
                        }
                        b.get(p).week.add(time_repeat.getWeekday());
                        b.get(p).dateStart = DateUtils.getDateFromHHmm(time_repeat.getStart());
                        if (time_repeat.getEnd() != null) b.get(p).dateEnd = DateUtils.getDateFromHHmm(time_repeat.getEnd());
                    }
                    Iterator iter = b.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry e = (Map.Entry) iter.next();
                        datas.add((CmBean) e.getValue());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    try {

                        Calendar c = Calendar.getInstance();
                        starttime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
                        c.add(Calendar.MONTH, 2);
                        c.set(Calendar.DAY_OF_MONTH, 1);
                        c.add(Calendar.DATE, -1);
                        endtime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));

                        CourseTypeSample course = body.courses.get(0);

                        if (course.is_private()) {
                            ArrayList<Integer> defautxxx1 = new ArrayList<>();
                            defautxxx1.add(1);
                            defautxxx1.add(2);
                            defautxxx1.add(3);
                            defautxxx1.add(4);
                            defautxxx1.add(5);
                            defautxxx1.add(6);
                            defautxxx1.add(7);
                            CmBean cmBean = new CmBean(defautxxx1, DateUtils.getDateFromHHmm("8:00"), DateUtils.getDateFromHHmm("21:00"));
                            datas.add(cmBean);
                        } else {
                            ArrayList<Integer> defautxxx1 = new ArrayList<>();
                            defautxxx1.add(1);
                            defautxxx1.add(3);
                            defautxxx1.add(5);
                            defautxxx1.add(7);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(DateUtils.getDateFromHHmm("8:00"));
                            calendar.add(Calendar.SECOND, course.getLength());
                            CmBean cmBean = new CmBean(defautxxx1, DateUtils.getDateFromHHmm("8:00"), calendar.getTime());
                            datas.add(cmBean);
                            ArrayList<Integer> defautxxx2 = new ArrayList<>();
                            defautxxx2.add(2);
                            defautxxx2.add(4);
                            defautxxx2.add(6);

                            calendar.setTime(DateUtils.getDateFromHHmm("14:00"));
                            calendar.add(Calendar.SECOND, course.getLength());
                            CmBean cmBean2 = new CmBean(defautxxx2, DateUtils.getDateFromHHmm("14:00"), calendar.getTime());
                            datas.add(cmBean2);
                        }
                    } catch (Exception e) {

                    }
                }
            } else {

                body.batches = new ArrayList<>();
                Batch batch = new Batch();
                batch.setSpace_name("默认场地");
                body.batches.add(batch);
                try {

                    Calendar c = Calendar.getInstance();
                    starttime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
                    c.add(Calendar.MONTH, 2);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.add(Calendar.DATE, -1);
                    endtime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));

                    CourseTypeSample course = body.courses.get(0);

                    if (course.is_private()) {
                        ArrayList<Integer> defautxxx1 = new ArrayList<>();
                        defautxxx1.add(1);
                        defautxxx1.add(2);
                        defautxxx1.add(3);
                        defautxxx1.add(4);
                        defautxxx1.add(5);
                        defautxxx1.add(6);
                        defautxxx1.add(7);
                        CmBean cmBean = new CmBean(defautxxx1, DateUtils.getDateFromHHmm("8:00"), DateUtils.getDateFromHHmm("21:00"));
                        datas.add(cmBean);
                    } else {
                        ArrayList<Integer> defautxxx1 = new ArrayList<>();
                        defautxxx1.add(1);
                        defautxxx1.add(3);
                        defautxxx1.add(5);
                        defautxxx1.add(7);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DateUtils.getDateFromHHmm("8:00"));
                        calendar.add(Calendar.SECOND, course.getLength());
                        CmBean cmBean = new CmBean(defautxxx1, DateUtils.getDateFromHHmm("8:00"), calendar.getTime());
                        datas.add(cmBean);
                        ArrayList<Integer> defautxxx2 = new ArrayList<>();
                        defautxxx2.add(2);
                        defautxxx2.add(4);
                        defautxxx2.add(6);

                        calendar.setTime(DateUtils.getDateFromHHmm("14:00"));
                        calendar.add(Calendar.SECOND, course.getLength());
                        CmBean cmBean2 = new CmBean(defautxxx2, DateUtils.getDateFromHHmm("14:00"), calendar.getTime());
                        datas.add(cmBean2);
                    }
                } catch (Exception e) {

                }

                adapter.notifyDataSetChanged();
            }
        } else {
            arrangeBatchBody = new ArrangeBatchBody();
            righticon.setVisibility(View.GONE);
            guideStep2.setVisibility(View.GONE);
            if (mCourse != null) {
                Glide.with(getContext()).load(PhotoUtils.getSmall(mCourse.photo)).placeholder(R.drawable.img_default_course).into(img);
                text1.setText(mCourse.name);
                text3.setText(String.format(getString(R.string.course_time_length), Integer.toString(mCourse.length / 60)));
                if (mCourse.is_private) {
                    imgFoot.setImageResource(R.drawable.ic_course_type_private);
                } else {
                    imgFoot.setImageResource(R.drawable.ic_course_type_group);
                }
                arrangeBatchBody.course_id = mCourse.id + "";
            }
            if (mTeacher != null) {
                coach.setContent(mTeacher.username);
                arrangeBatchBody.teacher_id = mTeacher.id;
            }
        }

        checkBtn();
    }

    @OnClick(R.id.course_layout) public void onCourseClick() {
        if (mType == AddGuideCourseFragment.COURSE_GUIDE) {
            //            getFragmentManager().beginTransaction()
            //                    .replace(R.id.frag, AddGuideCourseFragment.newInstance())
            //                    .commit();
        }
    }

    @OnClick(R.id.coach) public void onCoach() {
        if (mType == AddGuideCourseFragment.COURSE_GUIDE) {
            ChooseCoachFragment.start(this, 2, body.batches.get(0).getTeacher_phone(), 0);
        } else {
            ChooseCoachFragment.start(this, 2, "", 0);
        }
    }

    @OnClick(R.id.place) public void onPlace() {
        ChooseSiteFragment.start(this, 3, body.batches.get(0).getSpace_name(), 0,
            mCourse.is_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP);
    }

    @OnClick(R.id.account_type) public void onAccountType() {
        getFragmentManager().beginTransaction()
            .add(R.id.frag, SetAccountTypeFragment.newInstance(mCourse.is_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP))
            .addToBackStack(null)
            .commit();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IntentUtils.RESULT_OK) {

            switch (requestCode) {
                case 2:
                    coach.setContent(IntentUtils.getIntentString(data));
                    if (mType == AddGuideCourseFragment.COURSE_GUIDE) {
                        body.batches.get(0).setTeacher_phone(IntentUtils.getIntentString2(data));
                    }
                    break;
                case 3:
                    place.setContent(IntentUtils.getIntentString(data));
                    if (mType == AddGuideCourseFragment.COURSE_GUIDE) body.batches.get(0).setSpace_name(IntentUtils.getIntentString(data));
                    break;
                default:
                    break;
            }

            checkBtn();
        }
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(DoneAccountEvent.class.getName(), RxObDone);
        RxBus.getBus().unregister(CmBean.class.getName(), RxObCmBean);
        batchPresenter.unattachView();
        super.onDestroyView();
    }

    @OnClick(R.id.btn) public void onBtn() {
        if (mType == Configs.INIT_TYPE_GUIDE) {
            Batch batch = body.batches.get(0);
            batch.setCourse_name(text1.getText().toString());
            batch.setFrom_date(starttime.getContent());
            batch.setTo_date(endtime.getContent());
            batch.setTime_repeats(new ArrayList<Time_repeat>());

            for (CmBean bean : datas) {

                for (int i = 0; i < bean.week.size(); i++) {
                    Time_repeat time_repeat = new Time_repeat();
                    time_repeat.setWeekday(bean.week.get(i));
                    time_repeat.setStart(DateUtils.getTimeHHMM(bean.dateStart));
                    if (bean.dateEnd != null) time_repeat.setEnd(DateUtils.getTimeHHMM(bean.dateEnd));

                    batch.getTime_repeats().add(time_repeat);
                }
            }
            RxBus.getBus().post(new SaveEvent());
            showLoading();
            batchPresenter.initGymShop();
        } else {
            showLoading();
            batchPresenter.arrangeBatch(arrangeBatchBody);
        }
    }

    @Override public void onSucceed(CoachService coachService) {
        hideLoading();
        /**
         * 清除init数据
         */
        PreferenceUtils.setPrefString(getContext(), "init", "");
        App.caches.clear();
        RxBus.getBus().post(new RxCompleteGuideEvent());//通知单店模式
        RxBus.getBus().post(new FreshGymListEvent());//通知健身房列表刷新
        if (getActivity() instanceof GuideActivity) {
            if (((GuideActivity) getActivity()).isAdd) {
                Intent toGymDetail = new Intent(getActivity(), GymActivity.class);
                toGymDetail.putExtra(Configs.EXTRA_BRAND, new Brand(coachService.getBrand_id()));
                toGymDetail.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                toGymDetail.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
                toGymDetail.putExtra(GYM_TO, GymActivity.GYM_DETAIL);
                startActivity(toGymDetail);
                getActivity().finish();
            } else {
                Intent toGymDetail = new Intent(getActivity(), SplashActivity.class);
                toGymDetail.putExtra(GYM_TO, GymActivity.GYM_DETAIL);
                toGymDetail.putExtra(GymActivity.GYM_ID, coachService.getId());
                toGymDetail.putExtra(GymActivity.GYM_MODEL, coachService.getModel());
                toGymDetail.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                toGymDetail.putExtra(Configs.EXTRA_GYM_STATUS, true);
                toGymDetail.putExtra(Configs.EXTRA_BRAND, new Brand(coachService.getBrand_id()));
                toGymDetail.putExtra(MainActivity.IS_SIGNLE, true);
                startActivity(toGymDetail);
                getActivity().finish();
            }
            RxBus.getBus().post(new EventFreshCoachService());
        }
    }

    @Override public void onFailed() {
        hideLoading();
    }

    @OnClick(R.id.add) public void onAdd() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(),
                new AddCycleFragmentBuilder().courselength(mCourse.is_private ? 0L : (long) mCourse.length).build())
            .addToBackStack(null)
            .commit();
    }

    private void checkBtn() {
        RxBus.getBus().post(new SaveEvent());
        btn.setEnabled(body.batches != null
            && body.batches.size() > 0
            && !TextUtils.isEmpty(body.batches.get(0).getSpace_name())
            && !TextUtils.isEmpty(body.batches.get(0).getTeacher_phone())
            && body.card_tpls != null
            && body.card_tpls.size() > 0
            && datas.size() > 0);
    }

    private boolean checkBatch() {
        for (CmBean bean : datas) {
            //            if (bean.week >= 0 && bean.dateStart != null)
            //                return true;
        }
        return false;
    }

    /**
     * 选择开始时间
     */
    @OnClick(R.id.starttime) public void onStartTime() {
      if (pwTime == null) {
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
      }
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
            Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                starttime.setContent(DateUtils.Date2YYYYMMDD(date));

                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 选择结束时间
     */
    @OnClick(R.id.endtime) public void onEndTime() {
      if (pwTime == null) {
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
      }
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
            Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()) {
                    Toast.makeText(getContext(), R.string.alert_endtime_greater_starttime, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.getTime() - DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime() > 92 * DateUtils.DAY_TIME) {
                    Toast.makeText(getContext(), R.string.alert_batch_greater_three_month, Toast.LENGTH_SHORT).show();
                    return;
                }
                endtime.setContent(DateUtils.Date2YYYYMMDD(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 选择周几
     */
    public void chooseWeek(final int pos) {
        //        if (dialogList == null) {
        dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        //        }
        dialogList.list(weeks, new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                datas.get(pos).week = position;
                adapter.notifyItemChanged(pos);
                dialogList.dismiss();
                checkBtn();
            }
        });
        dialogList.show();
    }

    /**
     * 时间选择器
     */
    public void chooseTime(final int pos) {
        if (mType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
              timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }
            if (datas.get(pos).dateStart != null) {
                timeWindow.setTime(datas.get(pos).dateStart);
            } else {
                timeWindow.setTime(new Date(DateUtils.getDayMidnight(new Date())));
            }
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    datas.get(pos).dateStart = date;
                    adapter.notifyItemChanged(pos);
                    checkBtn();
                }
            });
            timeWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
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

                    datas.get(pos).dateStart = start;
                    datas.get(pos).dateEnd = end;
                    adapter.notifyItemChanged(pos);
                    checkBtn();
                }
            });
            Date dstart = datas.get(pos).dateStart;
            Date dend = datas.get(pos).dateEnd;

            if (dstart != null && dend != null) {
                timeDialogWindow.setTime(dstart, dend);
            } else {
                timeDialogWindow.setTime(new Date(DateUtils.getDayMidnight(new Date())), new Date(DateUtils.getDayMidnight(new Date())));
            }
            timeDialogWindow.showAtLocation();
        }
    }

    @Override public String getFragmentName() {
        return ManageCourseBatchFragment.class.getName();
    }
}
