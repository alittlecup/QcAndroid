//package com.qingchengfit.fitcoach.fragment.batch.details;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.bumptech.glide.Glide;
//import com.qingchengfit.fitcoach.App;
//import com.qingchengfit.fitcoach.Configs;
//import com.qingchengfit.fitcoach.R;
//import com.qingchengfit.fitcoach.RxBus;
//import com.qingchengfit.fitcoach.Utils.IntentUtils;
//import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
//import com.qingchengfit.fitcoach.Utils.PhotoUtils;
//import com.qingchengfit.fitcoach.action.SerPermisAction;
//import com.qingchengfit.fitcoach.bean.base.Course;
//import com.qingchengfit.fitcoach.component.CircleImgWrapper;
//import com.qingchengfit.fitcoach.fragment.BaseFragment;
//import com.qingchengfit.fitcoach.fragment.batch.BatchActivity;
//import com.qingchengfit.fitcoach.http.bean.CoachService;
//import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import rx.functions.Action1;
//
//import static com.qingchengfit.fitcoach.R.styleable.CommonInputView;
//
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/4/30 2016.
// */
//public class BatchDetailFragment extends BaseFragment implements BatchDetailView {
//
//
//    @BindView(R.id.img)
//    ImageView img;
//    @BindView(R.id.img_foot)
//    ImageView imgFoot;
//    @BindView(R.id.text1)
//    TextView text1;
//    @BindView(R.id.texticon)
//    ImageView texticon;
//    @BindView(R.id.text2)
//    TextView text2;
//    @BindView(R.id.text3)
//    TextView text3;
//    @BindView(R.id.righticon)
//    ImageView righticon;
//    @BindView(R.id.course_layout)
//    RelativeLayout courseLayout;
//    @BindView(R.id.account_type)
//    CommonInputView accountType;
////    @BindView(R.id.add)
////    TextView add;
//
//    @Inject
//    BatchDetailPresenter presenter;
//    @BindView(R.id.coach)
//    CommonInputView coach;
//    @BindView(R.id.space)
//    CommonInputView space;
//    @BindView(R.id.item_date)
//    TextView itemDate;
//    @BindView(R.id.add_layout)
//    LinearLayout addLayout;
//    @BindView(R.id.permission_view)
//    View permissionView;
//    private int mType;
//    private String mId;
//    private ArrayList<Rule> mCurRules;
//    private int mMaxuser;
//    ArrangeBatchBody body = new ArrangeBatchBody();
//    private Observable<DoneAccountEvent> mAccountOb;
//    @Inject
//    CoachService coachService;
//
//    public static BatchDetailFragment newInstance(int type, String id) {
//
//        Bundle args = new Bundle();
//        args.putInt("type", type);
//        args.putString("id", id);
//        BatchDetailFragment fragment = new BatchDetailFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mType = getArguments().getInt("type");
//            mId = getArguments().getString("id");
//        }
//    }
//
//    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            body.batch_id = mId;
//            showLoading();
//            presenter.checkBatch(mType, body);
//            return false;
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_batch_detail, container, false);
//        unbinder = ButterKnife.Bind(this, view);
//        if (getActivity() instanceof BatchActivity)
//            ((BatchActivity) getActivity()).getComponent().inject(this);
//        presenter.attachView(this);
//        if (mType == Configs.TYPE_PRIVATE) {
//            mCallbackActivity.setToolbar("私教排期", false, null, R.menu.menu_save, menuItemClickListener);
//        } else {
//            mCallbackActivity.setToolbar("团课排期", false, null, R.menu.menu_save, menuItemClickListener);
//        }
//        presenter.queryData(mId);
//        addLayout.setVisibility(View.GONE);
//        imgFoot.setVisibility(View.GONE);
//        righticon.setVisibility(View.VISIBLE);
//        accountType.setContent("已设置");
//        mAccountOb = RxBus.getBus().register(DoneAccountEvent.class);
//        mAccountOb.subscribe(new Action1<DoneAccountEvent>() {
//            @Override
//            public void call(DoneAccountEvent doneAccountEvent) {
//                body.max_users = doneAccountEvent.max_user;
//                mMaxuser = body.max_users;
//                body.rules = doneAccountEvent.rules;
//                mCurRules = body.rules;
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
//        RxBusAdd(EventFresh.class)
//                .subscribe(new Action1<EventFresh>() {
//                    @Override
//                    public void call(EventFresh eventFresh) {
//                        if (mType == Configs.TYPE_PRIVATE) {
//                            mCallbackActivity.setToolbar("私教排期", false, null, R.menu.menu_save, menuItemClickListener);
//                        } else {
//                            mCallbackActivity.setToolbar("团课排期", false, null, R.menu.menu_save, menuItemClickListener);
//                        }
//                    }
//                });
//
//
//        if (( mType== Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
//                || ( mType== Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
//            permissionView.setVisibility(View.VISIBLE);
//        }else permissionView.setVisibility(View.GONE);
//        return view;
//    }
//
//    @Override
//    public String getFragmentName() {
//        return BatchDetailFragment.class.getName();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//    }
//
//
//    @Override
//    public void onCoach(QcSchedulesResponse.Teacher teacher) {
//        if (Configs.TYPE_PRIVATE == mType) {
//            Glide.with(getContext()).load(PhotoUtils.getSmall(teacher.avatar)).asBitmap().placeholder(R.drawable.ic_default_head_nogender).into(new CircleImgWrapper(img, getContext()));
//            text1.setText(teacher.username);
//
//        } else {
//
//            coach.setLabel("教练");
//            coach.setContent(teacher.username);
//        }
//        body.teacher_id = teacher.id;
//
//    }
//
//    @Override
//    public void onCourse(Course course) {
//        if (Configs.TYPE_GROUP == mType) {
//            Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.ic_default_header).into(img);
//            text1.setText(course.getName());
//            text3.setText("时长" + (Float.parseFloat(course.getLength()) / 60) + "分钟");
//        } else {
//            coach.setLabel("课程");
//            coach.setContent(course.getName());
//        }
//        body.course_id = course.getId();
//
//    }
//
//    @Override
//    public void onSpace(List<Space> spaces) {
//        String sp = "";
//        List<String> spaceStrs = new ArrayList<>();
//        for (int i = 0; i < spaces.size(); i++) {
//            spaceStrs.add(spaces.get(i).getId());
//            if (i < spaces.size() - 1) {
//                sp = TextUtils.concat(sp, spaces.get(0).getName(), ",").toString();
//            } else sp = TextUtils.concat(sp, spaces.get(0).getName()).toString();
//
//        }
//        space.setContent(sp);
//        body.spaces = spaceStrs;
//    }
//
//    @Override
//    public void onRule(List<Rule> rules, int max_user) {
//        mCurRules = (ArrayList<Rule>) rules;
//        mMaxuser = max_user;
//        body.rules = (ArrayList<Rule>) rules;
//        body.max_users = max_user;
//    }
//
//    @Override
//    public void onTimeRepeat(String timestart, String timeend) {
//        itemDate.setText(timestart + "\n" + timeend);
//        body.from_date = timestart;
//        body.to_date = timeend;
//    }
//
//
//    @OnClick(R.id.batch_loop)
//    public void onBatchLoop() {
//        getFragmentManager().beginTransaction()
//                .add(mCallbackActivity.getFragId(), CourseManageFragment.newInstance(mId, mType))
//                .addToBackStack(null)
//                .commit();
//    }
//
//
//    @Override
//    public void checkOk() {
//        presenter.updateBatch(mId, body);
//    }
//
//    @Override
//    public void onSuccess() {
//        hideLoading();
//        ToastUtils.show("修改成功");
//        getActivity().onBackPressed();
//    }
//
//    @Override
//    public void onFailed() {
//        ToastUtils.show("修改失败");
//    }
//
//    @Override
//    public void onDelOk() {
//        hideLoading();
//        getActivity().onBackPressed();
//    }
//
//
//    @OnClick({R.id.course_layout, R.id.coach, R.id.space, R.id.account_type})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.course_layout:
//                //
//                if (mType == Configs.TYPE_PRIVATE) {
//                    ChooseCoachFragment.start(this, 1, "", Configs.INIT_TYPE_ADD);
//                } else {
//                    ChooseGroupCourseFragment.start(this, 2, "", mType);
//                }
//                break;
//            case R.id.coach:
//                if (mType == Configs.TYPE_GROUP) {
//                    ChooseCoachFragment.start(this, 3, "", Configs.INIT_TYPE_ADD);
//                } else {
//                    ChooseGroupCourseFragment.start(this, 4, "", mType);
//                }
//
//                break;
//            case R.id.space:
//                MutiChooseSiteFragment.start(this, 5, body.shop_id, mType);
////                ChooseSiteFragment.start(this, 5, "", Configs.INIT_TYPE_ADD,mType);
//                break;
//            case R.id.account_type:
//                getFragmentManager().beginTransaction()
//                        .add(R.id.frag, SetAccountTypeAddFragment.newInstance(mType, mCurRules, mMaxuser))
//                        .addToBackStack(null)
//                        .commit();
//
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 1) {       //私教 教练
//                String name = IntentUtils.getIntentString(data, 0);
//                String id = IntentUtils.getIntentString(data, 1);
//                String imgUrl = IntentUtils.getIntentString(data, 2);
//                Glide.with(getActivity()).load(PhotoUtils.getSmall(imgUrl)).asBitmap().into(new CircleImgWrapper(img, getActivity()));
//                text1.setText(name);
//                body.teacher_id = id;
//            } else if (requestCode == 3) { //团课 教练
//                String name = IntentUtils.getIntentString(data, 0);
//                String id = IntentUtils.getIntentString(data, 1);
//                String imgUrl = IntentUtils.getIntentString(data, 2);
//                coach.setContent(name);
//                body.teacher_id = id;
//            } else if (requestCode == 4) { //私教课程
//                Course course = data.getParcelableExtra("course");
//                coach.setContent(course.getName());
//                body.course_id = course.getId();
//            } else if (requestCode == 2) { //团课教程
//                Course course = data.getParcelableExtra("course");
//                Glide.with(getActivity()).load(PhotoUtils.getSmall(course.getPhoto())).asBitmap().into(new CircleImgWrapper(img, getActivity()));
//                text1.setText(course.getName());
//                body.course_id = course.getId();
//            } else if (requestCode == 5) { //选择场地
//                List<String> ids = data.getStringArrayListExtra("ids");
//                String names = data.getStringExtra("string");
//                space.setContent(names);
//                body.spaces = ids;
//            }
//
//        }
//    }
//
//    @OnClick(R.id.permission_view)
//    public void onClick() {
//        showAlert(R.string.alert_permission_forbid);
//    }
//    @OnClick(R.id.del_batch)
//    public void delBatch(){
//        if (( mType== Configs.TYPE_GROUP && !SerPermisAction.check(coachService.getShop_id(),PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_DELETE))
//                || ( mType== Configs.TYPE_PRIVATE && !SerPermisAction.check(coachService.getShop_id(),PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_DELETE))) {
//            showAlert(R.string.alert_permission_forbid);
//        }else {
//            new MaterialDialog.Builder(getContext())
//                    .content("是否删除该排期")
//                    .autoDismiss(true)
//                    .canceledOnTouchOutside(true)
//                    .positiveText(R.string.common_comfirm)
//                    .negativeText(R.string.pickerview_cancel)
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            showLoading();
//                            presenter.delbatch(App.staffId,mId);
//                        }
//                    })
//                    .show();
//        }
//    }
//
//}