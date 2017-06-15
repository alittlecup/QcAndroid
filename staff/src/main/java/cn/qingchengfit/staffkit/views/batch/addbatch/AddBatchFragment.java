package cn.qingchengfit.staffkit.views.batch.addbatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CmBean;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.GroupCourse;
import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.RxbusBatchLooperConfictEvent;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.adapter.CmAdapter;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.ChooseGroupCourseFragment;
import cn.qingchengfit.staffkit.views.gym.cycle.AddCycleFragment;
import cn.qingchengfit.staffkit.views.gym.cycle.AddCycleFragmentBuilder;
import cn.qingchengfit.staffkit.views.gym.site.MutiChooseSiteFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
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

    @BindView(R.id.img) ImageView img;
    @BindView(R.id.img_foot) ImageView imgFoot;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    @BindView(R.id.course_layout) RelativeLayout courseLayout;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.space) CommonInputView space;
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.add) TextView add;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pay_online) CommonInputView payOnline;
    @BindView(R.id.pay_card) CommonInputView payCard;
    @BindView(R.id.sw_need_pay) SwitchCompat swNeedPay;
    @BindView(R.id.tv_batch_loop_hint) TextView tvBatchLoopHint;
    @BindView(R.id.order_sutdent_count) CommonInputView orderSutdentCount;
    @BindView(R.id.img_layout) FrameLayout imgLayout;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.tag_pro) ImageView tagPro;
    @BindView(R.id.can_not_close) View canNotClose;
    @BindView(R.id.layout_need_pay) LinearLayout layoutNeedPay;
    @BindView(R.id.tv_clear_auto_batch) TextView tvClearAutoBatch;

    @Inject AddBatchPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    boolean hasSetAccount = false;
    List<CmBean> datas = new ArrayList<>();

    private ArrangeBatchBody body = new ArrangeBatchBody();
    private Staff mTeacher;
    private GroupCourse mCourse;
    private int mType;
    private TimeDialogWindow pwTime;

    private Observable<CmBean> RxObCmBean;
    private CmAdapter adapter;
    private HashMap<String, ArrayList<Integer>> mTimeRep = new HashMap<>();
    private Rule rulePayOnline;
    private List<Rule> cardRules = new ArrayList<>();

    private boolean isChangeCardPay;
    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (swNeedPay.isChecked() && (rulePayOnline == null && cardRules.size() == 0)) {
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
            if (body.max_users != Integer.valueOf(orderSutdentCount.getContent()) && !isChangeCardPay) {
                showAlert("会员卡结算未填写完整");
                return true;
            }
            body.max_users = Integer.valueOf(orderSutdentCount.getContent());
            body.is_free = !swNeedPay.isChecked();
            if (!body.is_free) {
                if (body.rules == null) body.rules = new ArrayList<>();
                body.rules.clear();
                body.rules.addAll(cardRules);
                if (rulePayOnline != null) body.rules.add(rulePayOnline);
            }
            try {
                body.max_users = Integer.parseInt(orderSutdentCount.getContent());
            } catch (Exception e) {
                body.max_users = 8;
            }
            body.from_date = starttime.getContent();
            body.to_date = endtime.getContent();
            body.time_repeats = CmBean.geTimeRepFromBean(datas);
            if (body.time_repeats == null || body.time_repeats.size() == 0) {
                ToastUtils.show("请添加课程周期");
                return true;
            }

            presenter.checkBatch(mType, body);

            return true;
        }
    };
    /**
     * 最大可约数量
     */
    private DialogList stucount;
    private MaterialDialog exitDialg;
    private boolean proGym;

    public static AddBatchFragment newInstance(Staff teacher, GroupCourse course) {

        Bundle args = new Bundle();
        args.putParcelable("teacher", teacher);
        args.putParcelable("course", course);
        AddBatchFragment fragment = new AddBatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeacher = getArguments().getParcelable("teacher");
            mCourse = getArguments().getParcelable("course");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        proGym = gymWrapper.isPro();
        tagPro.setVisibility(proGym ? View.GONE : View.VISIBLE);
        canNotClose.setVisibility(proGym ? View.GONE : View.VISIBLE);
        if (mCourse != null) {
            mType = Configs.TYPE_GROUP;
            body.course_id = String.valueOf(mCourse.id);
            coach.setLabel("教练");
            imgLayout.setBackgroundResource(R.drawable.md_transparent);
            Glide.with(getContext()).load(PhotoUtils.getSmall(mCourse.photo)).placeholder(R.drawable.img_default_course).into(img);

            text1.setText(mCourse.name);
            text3.setText(String.format(Locale.CHINA, "时长%d分钟", mCourse.length / 60));
        } else if (mTeacher != null) {
            mType = Configs.TYPE_PRIVATE;
            coach.setLabel("课程");
            imgLayout.setBackgroundResource(R.drawable.bg_rect_black);
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(mTeacher.avatar))
                .asBitmap()
                .placeholder(mTeacher.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                .into(new CircleImgWrapper(img, getContext()));
            imgLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            text1.setText(mTeacher.username);
            body.teacher_id = mTeacher.id;
        }
        initToolbar(toolbar);
        if (!gymWrapper.isPro()) {
            body.is_free = true;
        }

        adapter = new CmAdapter(datas, mType);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(
            new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, MeasureUtils.dpToPx(8f, getResources())));
        recyclerview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (v.getId() == R.id.delete) {
                    datas.remove(pos);
                    //body.time_repeats = CmBean.geTimeRepFromBean(datas);
                    adapter.notifyDataSetChanged();
                } else {
                    CmBean cm = datas.get(pos);
                    cm.position = pos;
                    getFragmentManager().beginTransaction()
                        .add(mCallbackActivity.getFragId(), new AddCycleFragmentBuilder().cmBean(cm)
                            .courselength(mType == Configs.TYPE_GROUP ? (long) mCourse.length : 0L)
                            .build())
                        .addToBackStack(null)
                        .commit();
                }
            }
        });

        RxObCmBean = RxBus.getBus().register(CmBean.class);
        RxObCmBean.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CmBean>() {
            @Override public void call(CmBean cmBean) {

                //check
                if (cmBean.dateEnd == null && cmBean.dateStart == null && (cmBean.week == null || cmBean.week.size() == 0)) {
                    return;
                }
                if (cmBean.position >= 0 && datas.size() > cmBean.position) {
                    CmBean cb = datas.get(cmBean.position);
                    cb.dateStart = cmBean.dateStart;
                    cb.dateEnd = cmBean.dateEnd;
                    cb.week.clear();
                    cb.week.addAll(cmBean.week);
                    adapter.notifyItemChanged(cmBean.position);
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                    return;
                }

                if (CmBean.CheckCmBean(datas, cmBean)) {
                    //添加CmBean
                    datas.add(cmBean);
                    //body.time_repeats = CmBean.geTimeRepFromBean(datas);
                    adapter.notifyDataSetChanged();
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                    delegatePresenter(presenter, AddBatchFragment.this);
                } else {
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        starttime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        calendar.add(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        endtime.setContent(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        swNeedPay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(swNeedPay.getViewTreeObserver(), this);
                swNeedPay.setChecked(gymWrapper.isPro());
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(mType == Configs.TYPE_PRIVATE ? "新增私教排期" : "新增团课排课");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override public boolean onFragmentBackPress() {
        if (exitDialg == null) {
            exitDialg = DialogUtils.instanceDelDialog(getContext(), mType == Configs.TYPE_PRIVATE ? "确定放弃本次排期？" : "确定放弃本次排课",
                new MaterialDialog.SingleButtonCallback() {
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
        return null;
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(CmBean.class.getName(), RxObCmBean);
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onSuccess() {
        hideLoading();
        getFragmentManager().popBackStackImmediate();
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
        DialogUtils.instanceDelDialog(getContext(), s, new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override public void onTemplete(boolean isFree, ArrayList<Rule> rules, ArrayList<Time_repeat> time_repeats, int maxuer) {
        if (gymWrapper.isPro()) {
            // 可以选择不自动填充

        } else {
            if (body.is_free) {
            } else {
                body.is_free = true;
                return;
            }
        }
        if (rules.size() > 0) {
            body.is_free = isFree;
            body.max_users = maxuer;
            orderSutdentCount.setContent(maxuer + "");
            if (rules != null) {
                cardRules.clear();
                rulePayOnline = null;
                for (int i = 0; i < rules.size(); i++) {
                    if (rules.get(i).channel.equals(Configs.CHANNEL_CARD)) {
                        cardRules.add(rules.get(i));
                    } else {
                        rulePayOnline = rules.get(i);
                    }
                }
                payCard.setHint(getString(R.string.common_un_setting));
                if (rulePayOnline != null) {
                    payOnline.setHint("已开启");
                }
            }
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
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).autoAdd = true;
            }

            adapter.notifyDataSetChanged();
            ToastUtils.showS("已自动填充排期");
            tvBatchLoopHint.setText("课程周期 (已根据历史信息自动填充)");
            tvClearAutoBatch.setVisibility(View.VISIBLE);
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

    /**
     * @param view 教练  场地   新增循环
     */
    @OnClick({ R.id.coach, R.id.space, R.id.add }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coach:
                if (mType == Configs.TYPE_GROUP) {
                    Intent toChooseTrainer = new Intent(getActivity(), ChooseActivity.class);
                    toChooseTrainer.putExtra("to", ChooseActivity.CHOOSE_TRAINER);
                    toChooseTrainer.putExtra("id", body.teacher_id);
                    startActivityForResult(toChooseTrainer, 1);
                } else {
                    ChooseGroupCourseFragment.start(this, 2, body.course_id, mType);//选择课程
                }
                break;
            case R.id.space:
                MutiChooseSiteFragment.start(this, 3, "", mType);
                break;
            case R.id.add:
                AddCycleFragment fragment = null;
                if (mType == Configs.TYPE_GROUP) {
                    fragment = new AddCycleFragmentBuilder().courselength((long) mCourse.length).build();
                } else {
                    fragment = new AddCycleFragmentBuilder().build();
                }
                getFragmentManager().beginTransaction().add(mCallbackActivity.getFragId(), fragment).addToBackStack(null).commit();

                break;
        }
    }

    /**
     * 选择开始时间
     */
    @OnClick(R.id.starttime) public void onStartTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
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
    @OnClick(R.id.endtime) public void onEndTime() {
        if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), Type.YEAR_MONTH_DAY);
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

    /**
     * 选择数量
     */
    @OnClick(R.id.order_sutdent_count) public void clickStuCount() {
        stucount =
            new DialogList(getContext()).list(mType == Configs.TYPE_PRIVATE ? StringUtils.getNums(1, 10) : StringUtils.getNums(1, 300),
                new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        orderSutdentCount.setContent(Integer.toString(position + 1));
                        if ((position + 1) != body.max_users) {
                            isChangeCardPay = false;
                        }
                        if (mType == Configs.TYPE_PRIVATE) payCard.setHint("已修改可约人数，请重新设置");
                        stucount.dismiss();
                    }
                }).title("选择人数");
        stucount.show();
    }

    /**
     * 点击在线支付
     */
    @OnClick(R.id.pay_online) public void clickPayOnline() {
        Intent toPayOnline = IntentUtils.getChooseIntent(getContext(), null, null, ChooseActivity.BATCH_PAY_ONLINE);
        toPayOnline.putExtra("rule", rulePayOnline);
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
        toPayOnline.putExtra("rules", (ArrayList) cardRules);
        toPayOnline.putExtra("private", mType == Configs.TYPE_PRIVATE);
        try {
            toPayOnline.putExtra("max", Integer.parseInt(orderSutdentCount.getContent()));
        } catch (Exception e) {
            toPayOnline.putExtra("max", 8);
        }
        startActivityForResult(toPayOnline, ChooseActivity.BATCH_PAY_CARD);
    }

    @OnClick(R.id.tv_clear_auto_batch) public void clearBatch() {
        List<CmBean> tmp = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (!datas.get(i).autoAdd) tmp.add(datas.get(i));
        }
        datas.clear();
        datas.addAll(tmp);
        adapter.notifyDataSetChanged();
        tvBatchLoopHint.setText("课程周期");
        tvClearAutoBatch.setVisibility(View.GONE);
    }

    /**
     * 返回值
     */
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {//选择教练
                Staff coachbean = data.getParcelableExtra("trainer");
                coach.setContent(coachbean.getUsername());
                body.teacher_id = coachbean.getId();
                presenter.getBatchTemplete(mType, body.teacher_id, body.course_id);//拉取模板
            } else if (requestCode == 2) {//选择课程
                CourseTypeSample course = data.getParcelableExtra("course");
                coach.setContent(course.getName());
                body.course_id = course.getId();
                presenter.getBatchTemplete(mType, body.teacher_id, body.course_id);
            } else if (requestCode == 3) {//选择场地
                List<String> ids = data.getStringArrayListExtra("ids");
                String names = data.getStringExtra("string");
                space.setContent(names);
                body.spaces = ids;
            } else if (requestCode == ChooseActivity.BATCH_PAY_ONLINE) {//在线支付
                try {
                    rulePayOnline = (Rule) IntentUtils.getParcelable(data);
                    payOnline.setHint("已开启");
                } catch (Exception e) {
                    rulePayOnline = null;
                    payOnline.setHint("未开启");
                }
            } else if (requestCode == ChooseActivity.BATCH_PAY_CARD) {//会员卡支付
                isChangeCardPay = true;
                ArrayList<Rule> rules = data.getParcelableArrayListExtra("rules");
                int maxCount = data.getIntExtra("count", 0);
                cardRules.clear();
                cardRules.addAll(rules);
                if (maxCount > 0) {
                    payCard.setHint(getString(R.string.batch_can_pay_card_count, maxCount));
                } else {
                    payCard.setHint(getString(R.string.common_un_setting));
                }
            }
        }
    }

    @OnClick(R.id.can_not_close) public void canNotClose() {
        if (proGym) {
            showAlert(R.string.alert_batch_has_ordered);
        } else {
            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
        }
    }
}
