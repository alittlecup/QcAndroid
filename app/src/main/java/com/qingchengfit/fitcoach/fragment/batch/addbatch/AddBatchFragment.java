package com.qingchengfit.fitcoach.fragment.batch.addbatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.model.bean.CmBean;
import cn.qingchengfit.staffkit.model.bean.Course;
import cn.qingchengfit.staffkit.model.bean.Rule;
import cn.qingchengfit.staffkit.model.bean.Time_repeat;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.DoneAccountEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxbusBatchLooperConfictEvent;
import cn.qingchengfit.staffkit.usecase.body.ArrangeBatchBody;
import cn.qingchengfit.staffkit.usecase.response.QcResponseGroupCourse;
import cn.qingchengfit.staffkit.usecase.response.QcSchedulesResponse;
import cn.qingchengfit.staffkit.utils.DateUtils;
import cn.qingchengfit.staffkit.utils.DialogUtils;
import cn.qingchengfit.staffkit.utils.IntentUtils;
import cn.qingchengfit.staffkit.utils.MeasureUtils;
import cn.qingchengfit.staffkit.utils.PhotoUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
import cn.qingchengfit.staffkit.views.adapter.CmAdapter;
import cn.qingchengfit.staffkit.views.custom.CircleImgWrapper;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;
import cn.qingchengfit.staffkit.views.gym.ChooseGroupCourseFragment;
import cn.qingchengfit.staffkit.views.gym.SetAccountTypeAddFragment;
import cn.qingchengfit.staffkit.views.gym.cycle.AddCycleFragment;
import cn.qingchengfit.staffkit.views.gym.site.MutiChooseSiteFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
public class AddBatchFragment extends BaseFragment implements AddBatchView {


    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.img_foot)
    ImageView imgFoot;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.texticon)
    ImageView texticon;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.righticon)
    ImageView righticon;
    @Bind(R.id.course_layout)
    RelativeLayout courseLayout;
    @Bind(R.id.coach)
    CommonInputView coach;
    @Bind(R.id.space)
    CommonInputView space;
    @Bind(R.id.account_type)
    CommonInputView accountType;
    @Bind(R.id.starttime)
    CommonInputView starttime;
    @Bind(R.id.endtime)
    CommonInputView endtime;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.add)
    TextView add;


    @Inject
    AddBatchPresenter presenter;

    private String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private ArrangeBatchBody body = new ArrangeBatchBody();
    private QcSchedulesResponse.Teacher mTeacher;
    private QcResponseGroupCourse.GroupClass mCourse;
    private int mType;
    private TimeDialogWindow pwTime;
    private Observable<DoneAccountEvent> RxObDone;
    private Observable<CmBean> RxObCmBean;
    private CmAdapter adapter;
    List<CmBean> datas = new ArrayList<>();

    private HashMap<String, ArrayList<Integer>> mTimeRep = new HashMap<>();


    public static AddBatchFragment newInstance(QcSchedulesResponse.Teacher teacher, QcResponseGroupCourse.GroupClass course) {

        Bundle args = new Bundle();
        args.putParcelable("teacher", teacher);
        args.putParcelable("course", course);
        AddBatchFragment fragment = new AddBatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeacher = getArguments().getParcelable("teacher");
            mCourse = getArguments().getParcelable("course");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_batch, container, false);
        ButterKnife.bind(this, view);
        ((GymComponent) mCallbackActivity.getComponent()).inject(this);
        presenter.attachView(this);
        mCallbackActivity.setToolbar("添加排期", false, null, R.menu.menu_compelete, listener);

        if (mCourse != null) {
            mType = Configs.TYPE_GROUP;
            body.course_id = mCourse.id;
            coach.setLabel("教练");
            Glide.with(getContext()).load(PhotoUtils.getSmall(mCourse.photo)).placeholder(R.drawable.img_default_course).into(img);
            text1.setText(mCourse.name);
            text3.setText(String.format(Locale.CHINA, "时长%d分钟", (int) (Float.parseFloat(mCourse.length) / 60)));
        } else if (mTeacher != null) {
            mType = Configs.TYPE_PRIVATE;
            coach.setLabel("课程");
            Glide.with(getContext()).load(PhotoUtils.getSmall(mTeacher.avatar)).asBitmap().placeholder(R.drawable.ic_default_head_nogender).into(new CircleImgWrapper(img, getContext()));
            text1.setText(mTeacher.username);
            body.teacher_id = mTeacher.id;
        }


        adapter = new CmAdapter(datas,mType);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL,MeasureUtils.dpToPx(8f,getResources())));
        recyclerview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                datas.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });


        RxObDone = RxBus.getBus().register(DoneAccountEvent.class);
        RxObDone.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<DoneAccountEvent>() {
            @Override
            public void call(DoneAccountEvent doneAccountEvent) {
                accountType.setContent(getString(R.string.common_have_setting));
                mCallbackActivity.setToolbar("添加排期", false, null, R.menu.menu_compelete, listener);
                body.rules = doneAccountEvent.rules;
                body.max_users = doneAccountEvent.max_user;
            }
        });
        RxObCmBean = RxBus.getBus().register(CmBean.class);
        RxObCmBean.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CmBean>() {
                    @Override
                    public void call(CmBean cmBean) {
                        //check
                        if (CmBean.CheckCmBean(datas, cmBean)) {
                            mCallbackActivity.setToolbar("添加排期", false, null, R.menu.menu_compelete, listener);
                            //添加CmBean
                            datas.add(cmBean);
                            body.time_repeats = CmBean.geTimeRepFromBean(datas);
                            adapter.notifyDataSetChanged();
                            RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                        } else {
                            RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                        }
                    }
                });
        return view;
    }
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (body.rules == null || body.rules.size() == 0) {
                ToastUtils.show("请设置结算方式");
                return true;
            }
            if (body.spaces == null || body.spaces.size() == 0) {
                ToastUtils.show("请选择场地");
                return true;
            }
            if (TextUtils.isEmpty(body.course_id)) {
                ToastUtils.show("请选择课程");
                return true;
            }
            if (TextUtils.isEmpty(body.teacher_id)) {
                ToastUtils.show("请选择教练");
                return true;
            }

            if (TextUtils.isEmpty(starttime.getContent()) || TextUtils.isEmpty(endtime.getContent())) {
                ToastUtils.show("请选择排期时间");
                return true;
            }
            body.from_date = starttime.getContent();
            body.to_date = endtime.getContent();
            if (body.time_repeats == null || body.time_repeats.size() == 0) {
                ToastUtils.show("请添加课程周期");
                return true;
            }

            presenter.checkBatch(mType, body);

            return true;
        }
    };

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onDestroyView() {
        RxBus.getBus().unregister(CmBean.class.getName(), RxObCmBean);
        RxBus.getBus().unregister(DoneAccountEvent.class.getName(), RxObDone);
        presenter.unattachView();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSuccess() {
        hideLoading();
        getActivity().onBackPressed();
//        mCallbackActivity.cleanToolbar();
//        getFragmentManager().popBackStack(GymCoursesFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        getFragmentManager().beginTransaction()
//                .replace(mCallbackActivity.getFragId(), CourseBatchDetailFragment.newInstance(mType, mType == Configs.TYPE_PRIVATE ? mTeacher.id : mCourse.id))
//                .commit();
    }

    @Override
    public void onFailed() {
        hideLoading();
    }


    //通过检查
    @Override
    public void checkOk() {
        showLoading();
        presenter.arrangeBatch(body);
    }

    @Override
    public void checkFailed(String s) {
        DialogUtils.instanceDelDialog(getContext(), s, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onTemplete(ArrayList<Rule> rules, ArrayList<Time_repeat> time_repeats, int maxuer) {
        //TODO 可以选择不自动填充
        if (rules.size() > 0) {
            body.rules = rules;
            body.max_users = maxuer;
            body.time_repeats = time_repeats;
            /**
             * time repeats -> CmBean
             */
            mTimeRep.clear();
            for (int i = 0; i < time_repeats.size(); i++) {
                Time_repeat time_repeat = time_repeats.get(i);
                String key = time_repeat.getStart() + "-" + time_repeat.getEnd();
                if (mTimeRep.get(key) != null) {
                    mTimeRep.get(key).add(time_repeat.getWeekday());
                } else {
                    ArrayList<Integer> weeks = new ArrayList<>();
                    weeks.add(time_repeat.getWeekday());
                    mTimeRep.put(key, weeks);
                }
            }
            datas.clear();
            datas.addAll(CmBean.getBeansFromTimeRep(mTimeRep));
            adapter.notifyDataSetChanged();
            accountType.setContent("已设置");
            ToastUtils.showS("已自动填充排期");
        }
    }

    @OnClick({R.id.coach, R.id.space, R.id.account_type, R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coach:
                if (mType == Configs.TYPE_GROUP) {
                    ChooseCoachFragment.start(this, 1, null, Configs.INIT_TYPE_ADD);//选择教练
                } else {
                    ChooseGroupCourseFragment.start(this, 2, body.course_id, mType);//选择课程
                }
                break;
            case R.id.space:
                MutiChooseSiteFragment.start(this, 3, "", mType);
                break;
            case R.id.account_type:
                getFragmentManager().beginTransaction()
                        .add(R.id.frag, SetAccountTypeAddFragment.newInstance(mType, body.rules, body.max_users))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.add:

                AddCycleFragment fragment = null;
                if (mType == Configs.TYPE_GROUP)
                    fragment = AddCycleFragment.newInstance(Long.parseLong(mCourse.length));
                else fragment = new AddCycleFragment();
                getFragmentManager().beginTransaction()
                        .add(mCallbackActivity.getFragId(), fragment)
                        .addToBackStack(null)
                        .commit();

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {//选择教练
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                String imgUrl = IntentUtils.getIntentString(data, 2);
                coach.setContent(name);
                body.teacher_id = id;

                if (TextUtils.isEmpty(starttime.getContent()) && TextUtils.isEmpty(endtime.getContent()))
                    presenter.getBatchTemplete(mType, body.teacher_id, body.course_id);//拉取模板
            } else if (requestCode == 2) {//选择课程
                Course course = data.getParcelableExtra("course");
                coach.setContent(course.getName());
                body.course_id = course.getId();
                if (TextUtils.isEmpty(starttime.getContent()) && TextUtils.isEmpty(endtime.getContent()))
                    presenter.getBatchTemplete(mType, body.teacher_id, body.course_id);

            } else if (requestCode == 3) {//选择场地
                List<String> ids = data.getStringArrayListExtra("ids");
                String names = data.getStringExtra("string");

                space.setContent(names);
                body.spaces = ids;

            }
        }
    }

    /**
     * 选择开始时间
     */
    @OnClick(R.id.starttime)
    public void onStartTime() {
        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR)-10, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                starttime.setContent(DateUtils.Date2YYYYMMDD(date));
                if (endtime.isEmpty())
                    endtime.setContent(DateUtils.getEndDayOfMonthNew(date));
                pwTime.dismiss();
            }
        });
        pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 选择结束时间
     */
    @OnClick(R.id.endtime)
    public void onEndTime() {
        if (pwTime == null)
            pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
        pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR)-10, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
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
        pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());

    }

}
