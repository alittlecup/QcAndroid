package cn.qingchengfit.staffkit.views.batch.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.BatchWeekLoopItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.batch.looplist.CourseManageFragment;
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
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 16/4/30 2016.
 */
public class BatchDetailFragment extends BaseFragment implements BatchDetailView, FlexibleAdapter.OnItemClickListener {

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
    @BindView(R.id.permission_view) View permissionView;
    @BindView(R.id.order_sutdent_count) CommonInputView orderSutdentCount;
    @BindView(R.id.tag_pro) ImageView tagPro;
    @BindView(R.id.sw_need_pay) SwitchCompat swNeedPay;
    @BindView(R.id.layout_need_pay) LinearLayout layoutNeedPay;
    @BindView(R.id.can_not_close) View canNotClose;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.pay_online) CommonInputView payOnline;
    @BindView(R.id.pay_card) CommonInputView payCard;
    @BindView(R.id.rv_batch_loop) RecyclerView rvBatchLoop;
    @BindView(R.id.tv_batch_date) TextView tvBatchDate;
    @BindView(R.id.img_layout) FrameLayout imgLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.label_check_all) TextView labelCheckAll;
    @BindView(R.id.del_batch) Button delBatch;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject BatchDetailPresenter presenter;
    ArrangeBatchBody body = new ArrangeBatchBody();
    private int mType;
    private String mId;
    private Rule ruleOnline;
    private List<Rule> rulesPayCards = new ArrayList<>();
    private ArrayList<CardTplBatchShip> mCardTplBatchShips;
    private boolean mHasOrder = false;
    private DialogList stucount;
    private CommonFlexAdapter batchLoopAdapter;
    private List<AbstractFlexibleItem> mbatchLoopDatas = new ArrayList<>();

    //修改时间
    private TimeDialogWindow timeWindow;
    private TimePeriodChooser timeDialogWindow;
    private boolean isChangeCardPay;
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            body.batch_id = mId;
            if (body.max_users != Integer.valueOf(orderSutdentCount.getContent()) && !isChangeCardPay&& rulesPayCards!= null && rulesPayCards.size() >0) {
                showAlert("会员卡结算未填写完整");
                return true;
            }
            body.max_users = Integer.valueOf(orderSutdentCount.getContent());

            if (body.time_repeats == null) body.time_repeats = new ArrayList<>();
            body.time_repeats.clear();
            for (int i = 0; i < mbatchLoopDatas.size(); i++) {
                if (mbatchLoopDatas.get(i) instanceof BatchWeekLoopItem) {
                    body.time_repeats.add(((BatchWeekLoopItem) mbatchLoopDatas.get(i)).getTime_repeat());
                }
            }
            if (swNeedPay.isChecked()) {
                body.is_free = false;
                if (body.rules == null) {
                    body.rules = new ArrayList<>();
                } else {
                    body.rules.clear();
                }
                if (ruleOnline != null) {
                    ruleOnline.to_number = body.max_users+1;
                    body.rules.add(ruleOnline);}
                body.rules.addAll(rulesPayCards);
                if (body.rules.size() == 0) {
                    cn.qingchengfit.utils.ToastUtils.show("请至少选择一种支付方式");
                    return true;
                }
            } else {
                body.is_free = true;
            }
            showLoading();
            presenter.checkBatch(mType, body);
            return false;
        }
    };
    private MaterialDialog exitDialg;
    private boolean proGym;

    public static BatchDetailFragment newInstance(int type, String id) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        BatchDetailFragment fragment = new BatchDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            mId = getArguments().getString("id");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        proGym = gymWrapper.isPro();
        tagPro.setVisibility(proGym ? View.GONE : View.VISIBLE);
        canNotClose.setVisibility(proGym ? View.GONE : View.VISIBLE);
        rvBatchLoop.setNestedScrollingEnabled(false);
        rvBatchLoop.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rvBatchLoop.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider_line_vertial));
        batchLoopAdapter = new CommonFlexAdapter(mbatchLoopDatas, this);
        rvBatchLoop.setAdapter(batchLoopAdapter);
        presenter.queryData(mId);

        imgFoot.setVisibility(View.GONE);
        righticon.setVisibility(View.VISIBLE);
        labelCheckAll.setText(mType == Configs.TYPE_PRIVATE ? "查看所有排期" : "查看所有课程");

        RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
            @Override public void call(EventFresh eventFresh) {
                if (mType == Configs.TYPE_PRIVATE) {
                    mCallbackActivity.setToolbar("私教排期", false, null, R.menu.menu_save, menuItemClickListener);
                } else {
                    mCallbackActivity.setToolbar("团课排期", false, null, R.menu.menu_save, menuItemClickListener);
                }
            }
        });

        if ((mType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)) || (
            mType == Configs.TYPE_PRIVATE
                && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
            permissionView.setVisibility(View.VISIBLE);
        } else {
            permissionView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        //toolbarTitile.setText(mType == Configs.TYPE_PRIVATE ? "私教排期" : "团课排期");
        toolbarTitile.setText("详情");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    @Override public boolean onFragmentBackPress() {
        if (exitDialg == null) {
            exitDialg = DialogUtils.instanceDelDialog(getContext(), "确定放弃本次编辑？", new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            });
        }
        if (!exitDialg.isShowing()) exitDialg.show();
        return true;
    }

    @Override public String getFragmentName() {
        return BatchDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 教练信息
     */
    @Override public void onCoach(Staff teacher) {
        if (Configs.TYPE_PRIVATE == mType) {
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(teacher.avatar))
                .asBitmap()
                .placeholder(R.drawable.ic_default_head_nogender)
                .into(new CircleImgWrapper(img, getContext()));
            imgLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            text1.setText(teacher.username);
        } else {
            coach.setLabel("教练");
            coach.setContent(teacher.username);
        }
        body.teacher_id = teacher.id;
    }

    @Override public void onCourse(CourseTypeSample course) {
        if (Configs.TYPE_GROUP == mType) {
            Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.ic_default_header).into(img);
            text1.setText(course.getName());
            text3.setText("时长" + course.getLength() / 60 + "分钟");
        } else {
            coach.setLabel("课程");
            coach.setContent(course.getName());
        }
        body.course_id = course.getId();
    }

    @Override public void onSpace(List<Space> spaces) {
        String sp = "";
        List<String> spaceStrs = new ArrayList<>();
        for (int i = 0; i < spaces.size(); i++) {
            spaceStrs.add(spaces.get(i).getId());
            if (i < spaces.size() - 1) {
                sp = TextUtils.concat(sp, spaces.get(0).getName(), ",").toString();
            } else {
                sp = TextUtils.concat(sp, spaces.get(0).getName()).toString();
            }
        }
        space.setContent(sp);
        body.spaces = spaceStrs;
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
        orderSutdentCount.setContent(max_user + "");
        body.rules = (ArrayList<Rule>) rules;
        body.max_users = max_user;
        orderSutdentCount.setContent(max_user + "");
        body.is_free = isFree;
        swNeedPay.setChecked(!isFree);
        canNotClose.setVisibility(hasOrder ? View.VISIBLE : View.GONE);
        mCardTplBatchShips = (ArrayList<CardTplBatchShip>) cardTplBatchShips;

        payCard.setHint(getString(R.string.batch_can_pay_card_count, mCardTplBatchShips.size()));
        payOnline.setHint(ruleOnline == null ? "未开启" : "已开启");
        mHasOrder = hasOrder;
        canNotClose.setVisibility(hasOrder || !proGym ? View.VISIBLE : View.GONE);
    }

    @Override public void onTimeRepeat(String timestart, String timeend, List<Time_repeat> time_repeats) {
        tvBatchDate.setText(
            getString(mType == Configs.TYPE_PRIVATE ? R.string.batch_date_loop_private : R.string.batch_date_loop_group, timestart,
                timeend));
        body.from_date = timestart;
        body.to_date = timeend;
        if (time_repeats != null) {
            mbatchLoopDatas.clear();
            for (int i = 0; i < time_repeats.size(); i++) {
                Time_repeat time_repeat = time_repeats.get(i);
                mbatchLoopDatas.add(new BatchWeekLoopItem(time_repeat, mType == Configs.TYPE_PRIVATE));
            }
            batchLoopAdapter.notifyDataSetChanged();
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
     * 查看所有排期
     */
    @OnClick(R.id.layout_all_batch) public void onBatchLoop() {
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), CourseManageFragment.newInstance(mId, mType))
            .addToBackStack(null)
            .commit();
    }

    @Override public void checkOk() {
        presenter.updateBatch(mId, body);
    }

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.show("修改成功");
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override public void onFailed() {
        hideLoading();
        ToastUtils.show("修改失败");
    }

    @Override public void onCheckFaild(String s) {
        hideLoading();
        showAlert(s);
    }

    @Override public void onDelOk() {
        hideLoading();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @OnClick({ R.id.course_layout, R.id.coach, R.id.space }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_layout:
                if (mType == Configs.TYPE_PRIVATE) {
                    Intent toChooseTrainer = new Intent(getActivity(), ChooseActivity.class);
                    toChooseTrainer.putExtra("to", ChooseActivity.CHOOSE_TRAINER);
                    toChooseTrainer.putExtra("id", body.teacher_id);
                    startActivityForResult(toChooseTrainer, 1);
                } else {
                    ChooseGroupCourseFragment.start(this, 2, "", mType);
                }
                break;
            case R.id.coach:
                if (mType == Configs.TYPE_GROUP) {
                    ChooseCoachFragment.start(this, 3, "", Configs.INIT_TYPE_ADD);
                } else {
                    ChooseGroupCourseFragment.start(this, 4, "", mType);
                }

                break;
            case R.id.space:
                MutiChooseSiteFragment.start(this, 5, body.shop_id, mType);
                break;
        }
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
                        payCard.setHint("已修改可约人数，请重新设置");
                        stucount.dismiss();
                    }
                }).title("选择人数");
        stucount.show();
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
                body.teacher_id = id;
            } else if (requestCode == 3) { //团课 教练
                String name = IntentUtils.getIntentString(data, 0);
                String id = IntentUtils.getIntentString(data, 1);
                String imgUrl = IntentUtils.getIntentString(data, 2);
                coach.setContent(name);
                body.teacher_id = id;
            } else if (requestCode == 4) { //私教课程
                CourseTypeSample course = data.getParcelableExtra("course");
                coach.setContent(course.getName());
                body.course_id = course.getId();
            } else if (requestCode == 2) { //团课教程
                CourseTypeSample course = data.getParcelableExtra("course");
                Glide.with(getActivity())
                    .load(PhotoUtils.getSmall(course.getPhoto()))
                    .asBitmap()
                    .into(new CircleImgWrapper(img, getActivity()));
                text1.setText(course.getName());
                body.course_id = course.getId();
            } else if (requestCode == 5) { //选择场地
                List<String> ids = data.getStringArrayListExtra("ids");
                String names = data.getStringExtra("string");
                space.setContent(names);
                body.spaces = ids;
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

    @OnClick(R.id.permission_view) public void onClick() {
        showAlert(R.string.alert_permission_forbid);
    }

    @OnClick(R.id.del_batch) public void delBatch() {
        if ((mType == Configs.TYPE_GROUP && !SerPermisAction.check(gymWrapper.shop_id(),
            PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_DELETE)) || (mType == Configs.TYPE_PRIVATE && !SerPermisAction.check(
            gymWrapper.shop_id(), PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_DELETE))) {
            showAlert(R.string.alert_permission_forbid);
        } else {
            new MaterialDialog.Builder(getContext()).content("是否删除该排期")
                .autoDismiss(true)
                .canceledOnTouchOutside(true)
                .positiveText(R.string.common_comfirm)
                .negativeText(R.string.pickerview_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showLoading();
                        presenter.delbatch(App.staffId, mId);
                    }
                })
                .show();
        }
    }

    @Override public boolean onItemClick(final int pos) {

        if (mType == Configs.TYPE_GROUP) {
            if (timeWindow == null) {
              timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
            }

            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    if (mbatchLoopDatas.get(pos) instanceof BatchWeekLoopItem) {
                        ((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().setStart(DateUtils.getTimeHHMM(date));
                        batchLoopAdapter.notifyItemChanged(pos);
                    }
                }
            });
            Date d = new Date();
            if (mbatchLoopDatas.get(pos) instanceof BatchWeekLoopItem) {
                d = DateUtils.getDateFromHHmm(((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().getStart());
            }
            timeWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
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
                    if (mbatchLoopDatas.get(pos) instanceof BatchWeekLoopItem) {
                        ((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().setStart(DateUtils.getTimeHHMM(start));
                        ((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().setEnd(DateUtils.getTimeHHMM(end));
                        batchLoopAdapter.notifyItemChanged(pos);
                    }
                }
            });
            Date s = new Date();
            Date e = new Date();
            if (mbatchLoopDatas.get(pos) instanceof BatchWeekLoopItem) {
                s = DateUtils.getDateFromHHmm(((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().getStart());
                e = DateUtils.getDateFromHHmm(((BatchWeekLoopItem) mbatchLoopDatas.get(pos)).getTime_repeat().getEnd());
            }
            timeDialogWindow.setTime(s, e);
            timeDialogWindow.showAtLocation();
        }
        return true;
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
        toPayOnline.putExtra("private", mType == Configs.TYPE_PRIVATE);
        toPayOnline.putExtra("order", mCardTplBatchShips);
        try {
            toPayOnline.putExtra("max", Integer.parseInt(orderSutdentCount.getContent()));
        } catch (Exception e) {
            toPayOnline.putExtra("max", 8);
        }
        startActivityForResult(toPayOnline, ChooseActivity.BATCH_PAY_CARD);
    }

    @OnClick(R.id.can_not_close) public void canNotClose() {
        if (proGym) {
            showAlert(R.string.alert_batch_has_ordered);
        } else {
            new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
        }
    }
}
