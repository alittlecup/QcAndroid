package com.qingchengfit.fitcoach.fragment.batch.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.ArrangeBatchBody;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.CurentPermissions;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.CourseManageFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
public class BatchDetailFragment extends BaseFragment implements BatchDetailView {

    public static final int RESULT_COURSE = 11;
    public static final int RESULT_ACCOUNT = 12;

    @BindView(R.id.img) ImageView img;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    @BindView(R.id.account_type) CommonInputView accountType;
    @Inject BatchDetailPresenter presenter;
    @BindView(R.id.coach) CommonInputView coach;
    @BindView(R.id.space) CommonInputView space;
    @BindView(R.id.item_date) TextView itemDate;
    @BindView(R.id.permission_view) View permissionView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.layout_batch_loop) LinearLayout layoutBatchLoop;
    ArrangeBatchBody body = new ArrangeBatchBody();
    @Inject CoachService coachService;
    private int mType;
    private String mId;
    private ArrayList<Rule> mCurRules;
    private int mMaxuser;
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            body.batch_id = mId;
            showLoading();
            presenter.checkBatch(App.coachid + "", mType, body);
            return false;
        }
    };

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
        if (getActivity() instanceof CourseActivity) ((CourseActivity) getActivity()).getComponent().inject(this);
        presenter.attachView(this);
        toolbarTitle.setText(mType == Configs.TYPE_PRIVATE ? "私教排期" : "团课排期");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        presenter.queryData(App.coachid + "", mId);
        //addLayout.setVisibility(View.GONE);
        righticon.setVisibility(View.VISIBLE);
        accountType.setContent("已设置");
        layoutBatchLoop.setOnClickListener(this::onBatchLoop);
        if ((mType == Configs.TYPE_GROUP && !CurentPermissions.newInstance()
            .queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)) || (mType == Configs.TYPE_PRIVATE && !CurentPermissions
            .newInstance()
            .queryPermission(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
            permissionView.setVisibility(View.VISIBLE);
        } else {
            permissionView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override public String getFragmentName() {
        return BatchDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onCoach(QcSchedulesResponse.Teacher teacher) {
        coach.setLabel("教练");
        coach.setContent(teacher.username);
    }

    @OnClick(R.id.layout_course) public void onLayoutCourse() {
        Intent toChooseCourse = new Intent(getActivity(), CourseActivity.class);
        toChooseCourse.putExtra("to", CourseActivity.TO_CHOOSE);
        toChooseCourse.putExtra("type", mType);
        toChooseCourse.putExtra("service", coachService);
        startActivityForResult(toChooseCourse, RESULT_COURSE);
    }

    @Override public void onCourse(Course course) {
        Glide.with(getContext()).load(PhotoUtils.getSmall(course.getPhoto())).placeholder(R.drawable.ic_default_header).into(img);
        text1.setText(course.getName());
        text3.setText("时长" + (course.getLength() / 60) + "分钟");
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

    @Override public void onRule(List<Rule> rules, int max_user) {
        mCurRules = (ArrayList<Rule>) rules;
        mMaxuser = max_user;
        body.rules = (ArrayList<Rule>) rules;
        body.max_users = max_user;
    }

    @Override public void onTimeRepeat(String timestart, String timeend) {
        itemDate.setText(timestart + "\n" + timeend);
        body.from_date = timestart;
        body.to_date = timeend;
    }

    public void onBatchLoop(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.frag, CourseManageFragment.newInstance(coachService.getModel(), coachService.getId() + "", mId, mType))
            .addToBackStack(null)
            .commit();
    }

    @Override public void checkOk() {
        presenter.updateBatch(mId, body);
    }

    @Override public void onSuccess() {
        hideLoading();
        ToastUtils.show("修改成功");
        getActivity().onBackPressed();
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public void onDelOk() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @OnClick({ R.id.coach, R.id.space, R.id.account_type }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coach:
                //StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
                break;
            case R.id.space:
                Intent toChooseSpace = new Intent(getActivity(), FragActivity.class);
                toChooseSpace.putExtra("type", 11);
                toChooseSpace.putExtra("course_type", mType);
                toChooseSpace.putExtra("service", coachService);
                startActivityForResult(toChooseSpace, 5);
                break;
            case R.id.account_type:
                Intent toAccount = new Intent(getActivity(), FragActivity.class);
                toAccount.putExtra("type", 12);
                toAccount.putExtra("count", body.max_users == 0 ? 8 : body.max_users);
                toAccount.putExtra("isfree", body.is_free);
                toAccount.putExtra("service", coachService);
                startActivityForResult(toAccount, RESULT_ACCOUNT);
                break;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 5) { //选择场地
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
            } else if (requestCode == RESULT_COURSE) {
                CourseDetail course = data.getParcelableExtra("course");
                onCourse(course);
            } else if (requestCode == RESULT_ACCOUNT) {
                int count = data.getIntExtra("count", 1);
                body.max_users = count;
                accountType.setContent(getString(R.string.has_set));
            }
        }
    }

    @OnClick(R.id.permission_view) public void onClick() {
        showAlert(R.string.alert_permission_forbid);
    }

    @OnClick(R.id.del_batch) public void delBatch() {
        if ((mType == Configs.TYPE_GROUP && !CurentPermissions.newInstance()
            .queryPermission(com.qingchengfit.fitcoach.bean.base.PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_DELETE)) || (mType
            == Configs.TYPE_PRIVATE && !CurentPermissions.newInstance()
            .queryPermission(com.qingchengfit.fitcoach.bean.base.PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_DELETE))) {
            showAlert(R.string.sorry_no_permission);
            return;
        }
        new MaterialDialog.Builder(getContext()).content("是否删除该排期")
            .autoDismiss(true)
            .canceledOnTouchOutside(true)
            .positiveText(R.string.comfirm)
            .negativeText(R.string.pickerview_cancel)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showLoading();
                    presenter.delbatch(App.coachid + "", mId);
                }
            })
            .show();
        //}
    }
}
