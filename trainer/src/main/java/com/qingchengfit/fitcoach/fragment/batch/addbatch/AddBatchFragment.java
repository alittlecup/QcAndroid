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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.TimeRepeat;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.ArrangeBatchBody;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.RxbusBatchLooperConfictEvent;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.batch.BatchActivity;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.items.AddBatchCircleItem;
import com.qingchengfit.fitcoach.items.BatchCircleItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

import static com.qingchengfit.fitcoach.R.id.enddate;
import static com.qingchengfit.fitcoach.R.id.startdate;

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
public class AddBatchFragment extends BaseFragment implements AddBatchView, FlexibleAdapter.OnItemClickListener {

    public static final int RESULT_ACCOUNT = 5;
    private static final int RESULT_COURSE = 6;

    @BindView(R.id.course_img) ImageView img;
    @BindView(R.id.img_private) ImageView imgFoot;
    @BindView(R.id.course_name) TextView text1;
    //@BindView(R.id.texticon)
    //ImageView texticon;
    @BindView(R.id.course_time_long)
    //TextView text2;
        //@BindView(R.id.text3)
        TextView text3;
    //@BindView(R.id.righticon)
    //ImageView righticon;
    //@BindView(R.id.course_layout)
    //RelativeLayout courseLayout;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.space) CommonInputView space;
    @BindView(R.id.account_type) CommonInputView accountType;
    @BindView(R.id.startdate) CommonInputView starttime;
    @BindView(R.id.enddate) CommonInputView endtime;
    @BindView(R.id.batch_date) RecyclerView recyclerview;

    @Inject AddBatchPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @Inject CoachService mCoachService;
    private ArrangeBatchBody body = new ArrangeBatchBody();
    private QcSchedulesResponse.Teacher mTeacher;
    private CourseDetail mCourse;
    private int mType;
    private TimeDialogWindow pwTime;
    private Observable<CmBean> RxObCmBean;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> mTimeRep = new HashMap<>();
    private MaterialDialog failDialog;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (!body.is_free && (body.rules == null || body.rules.size() == 0)) {
                ToastUtils.show("请设置结算方式");
                return true;
            }
            if (body.max_users == 0) {
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
            //if (TextUtils.isEmpty(body.teacher_id)) {
            //    ToastUtils.show("请选择教练");
            //    return true;
            //}

            if (TextUtils.isEmpty(starttime.getContent()) || TextUtils.isEmpty(endtime.getContent())) {
                ToastUtils.show("请选择排期时间");
                return true;
            }
            if (!DateUtils.AlessB(starttime.getContent(), endtime.getContent())) {
                ToastUtils.show("开始时间不能大于结束时间");
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

    public static AddBatchFragment newInstance(CourseDetail course) {

        Bundle args = new Bundle();
        args.putParcelable("course", course);
        AddBatchFragment fragment = new AddBatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getParcelable("course");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof BatchActivity) {
            ((BatchActivity) getActivity()).getComponent().inject(this);
        } else if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);
        }
        delegatePresenter(presenter, this);

        presenter.attachView(this);
        //mCallbackActivity.setToolbar("添加排期", false, null, R.menu.menu_compelete, listener);
        toolbar.inflateMenu(R.menu.menu_complete);
        toolbar.setOnMenuItemClickListener(listener);
        toolbarTitle.setText("添加排期");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        if (mCourse != null) {
            mType = mCourse.is_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP;
            body.course_id = mCourse.id;
            coach.setLabel("教练");
            Glide.with(getContext()).load(PhotoUtils.getSmall(mCourse.photo)).placeholder(R.drawable.img_default_course).into(img);
            text1.setText(mCourse.name);
            text3.setText(String.format(Locale.CHINA, "时长%d分钟", mCourse.getLength() / 60));

            presenter.getBatchTemplete(mType, null, body.course_id);//拉取模板
        } else if (mTeacher != null) {
            mType = Configs.TYPE_PRIVATE;
            coach.setLabel("课程");
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(mTeacher.avatar))
                .asBitmap()
                .placeholder(R.drawable.ic_default_head_nogender)
                .into(new CircleImgWrapper(img, getContext()));
            text1.setText(mTeacher.username);
            //body.teacher_id = mTeacher.id;
        }
        if (App.gUser != null) {
            //body.teacher_id = App.coachid + "";
            coach.setContent(App.gUser.username);
        }

        Calendar c = Calendar.getInstance();
        starttime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
        c.add(Calendar.MONTH, 2);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE, -1);
        endtime.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));

        mData.add(0, new AddBatchCircleItem(getString(R.string.add_course_circle)));
        mAdapter = new CommonFlexAdapter(mData, this);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mAdapter);

        RxBusAdd(CmBean.class).subscribe(new Action1<CmBean>() {
            @Override public void call(CmBean cmBean) {
                List<CmBean> cmBeens = new ArrayList<CmBean>();
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i) instanceof BatchCircleItem) {
                        cmBeens.add(((BatchCircleItem) mData.get(i)).cmBean);
                    }
                }
                if (CmBean.checkCmBean(cmBeens, cmBean)) {
                    mData.add(mData.size() - 1, new BatchCircleItem(cmBean, mCourse.is_private));
                    cmBeens.add(cmBean);
                    body.time_repeats = CmBean.geTimeRepFromBean(cmBeens);
                    mAdapter.notifyDataSetChanged();
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                } else {
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                }
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(CmBean.class.getName(), RxObCmBean);
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onFailed() {
        hideLoading();
    }

    //通过检查
    @Override public void checkOk() {
        showLoading();
        presenter.arrangeBatch(body);
    }

    @Override public void checkFailed(String s) {
        if (failDialog == null) {
            failDialog = new MaterialDialog.Builder(getContext()).positiveText(R.string.comfirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();
        }
        if (failDialog.isShowing()) failDialog.dismiss();
        failDialog.setContent(s);
        failDialog.show();
    }

    @Override public void onTemplete(ArrayList<Rule> rules, ArrayList<TimeRepeat> time_repeats, int maxuer) {
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
                TimeRepeat time_repeat = time_repeats.get(i);
                String key = time_repeat.getStart() + "-" + time_repeat.getEnd();
                if (mTimeRep.get(key) != null) {
                    mTimeRep.get(key).add(time_repeat.getWeekday());
                } else {
                    ArrayList<Integer> weeks = new ArrayList<>();
                    weeks.add(time_repeat.getWeekday());
                    mTimeRep.put(key, weeks);
                }
            }
            mData.clear();
            for (int i = 0; i < CmBean.getBeansFromTimeRep(mTimeRep).size(); i++) {
                mData.add(new BatchCircleItem(CmBean.getBeansFromTimeRep(mTimeRep).get(i), mCourse.is_private));
            }
            mData.add(new AddBatchCircleItem(getString(R.string.add_course_circle)));
            mAdapter.notifyDataSetChanged();
            accountType.setContent("已设置");
            ToastUtils.showS("已自动填充排期");
        }
    }

    @OnClick({ R.id.coach, R.id.space, R.id.account_type, R.id.layout_course }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coach:
                break;
            case R.id.space:
                Intent toChooseSpace = new Intent(getActivity(), FragActivity.class);
                toChooseSpace.putExtra("type", 11);
                toChooseSpace.putExtra("course_type", mType);
                toChooseSpace.putExtra("service", mCoachService);
                startActivityForResult(toChooseSpace, 3);
                break;
            case R.id.account_type:
                Intent toAccount = new Intent(getActivity(), FragActivity.class);
                toAccount.putExtra("type", 12);
                toAccount.putExtra("count", body.max_users == 0 ? mCourse.getCapacity() : body.max_users);
                toAccount.putExtra("isfree", body.is_free);
                toAccount.putExtra("service", mCoachService);
                startActivityForResult(toAccount, RESULT_ACCOUNT);

                break;
            case R.id.layout_course:
                Intent toChooseCourse = new Intent(getActivity(), CourseActivity.class);
                toChooseCourse.putExtra("to", CourseActivity.TO_CHOOSE);
                toChooseCourse.putExtra("type", mType);
                toChooseCourse.putExtra("service", mCoachService);
                startActivityForResult(toChooseCourse, RESULT_COURSE);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {//选择教练
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                String imgUrl = IntentUtils.getIntentString(data, 2);
                coach.setContent(name);
                //body.teacher_id = id;

                if (TextUtils.isEmpty(starttime.getContent()) && TextUtils.isEmpty(endtime.getContent())) {
                }
            } else if (requestCode == 2) {//选择课程
                //Course course = data.getParcelableExtra("course");
                //coach.setContent(course.getName());
                //body.course_id = course.getId();
                //if (TextUtils.isEmpty(starttime.getContent()) && TextUtils.isEmpty(endtime.getContent()))
                //    presenter.getBatchTemplete(mType, body.teacher_id, body.course_id);

            } else if (requestCode == 3) {//选择场地
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
                    body.spaces = ids;
                }
            } else if (requestCode == RESULT_ACCOUNT) {
                int count = data.getIntExtra("count", 1);
                body.max_users = count;
                accountType.setContent(getString(R.string.has_set));
            } else if (requestCode == RESULT_COURSE) {
                CourseDetail course = data.getParcelableExtra("course");
                if (course != null) mCourse = course;
                body.course_id = mCourse.id;
                Glide.with(getContext()).load(PhotoUtils.getSmall(mCourse.photo)).placeholder(R.drawable.img_default_course).into(img);
                text1.setText(mCourse.name);
                text3.setText(String.format(Locale.CHINA, "时长%d分钟", mCourse.getLength() / 60));
                presenter.getBatchTemplete(mType, null, body.course_id);//拉取模板
            }
        }
    }

    /**
     * 选择开始时间
     */
    @OnClick(startdate) public void onStartTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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
    @OnClick(enddate) public void onEndTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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
        pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof BatchCircleItem) {

        } else if (mAdapter.getItem(position) instanceof AddBatchCircleItem) {
            Intent to = new Intent(getActivity(), ChooseActivity.class);
            to.putExtra("to", ChooseActivity.TO_CHOSSE_CIRCLE);
            //to.putExtra("cmbean", ChooseActivity.TO_CHOSSE_CIRCLE);
            if (!mCourse.is_private) to.putExtra("len", ((Integer) mCourse.getLength()).longValue());
            startActivity(to);
        }
        return true;
    }
}
