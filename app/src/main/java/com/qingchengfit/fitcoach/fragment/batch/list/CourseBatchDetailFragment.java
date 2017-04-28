package com.qingchengfit.fitcoach.fragment.batch.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.action.SerPermisAction;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.CurentPermissions;
import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.VpFragment;
import com.qingchengfit.fitcoach.fragment.batch.BatchActivity;
import com.qingchengfit.fitcoach.fragment.batch.addbatch.AddBatchFragment;
import com.qingchengfit.fitcoach.fragment.batch.details.BatchDetailFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.fragment.course.CourseListFragment;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateDetail;
import com.qingchengfit.fitcoach.items.BatchItem;
import com.qingchengfit.fitcoach.items.CommonNoDataItem;
import com.qingchengfit.fitcoach.items.HideBatchItem;
import com.qingchengfit.fitcoach.items.HintItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * Created by Paper on 16/3/29 2016.
 */
public class CourseBatchDetailFragment extends VpFragment
    implements CourseBatchDetailView, FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {
    public static final int RESULT_COURSE = 1;

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @Inject CourseBatchDetailPresenter presenter;
    @BindView(R.id.preview) TextView preview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private DialogSheet delCourseDialog;
    private MaterialDialog delDialog;

    private int mType;

    private Course mCourese;
    private QcResponsePrivateDetail.PrivateCoach mTeacher;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    List<AbstractFlexibleItem> mOutdateDatas = new ArrayList<>();
    private Bundle saveState = new Bundle();
    CommonFlexAdapter mCommonFlexAdapter;
    @Inject CoachService mCoachService;
    /**
     * 记录是否展示已过期排期
     */
    private boolean isShow;
    private DialogList dialogList;

    public static CourseBatchDetailFragment newInstance(int type) {
        Bundle args = new Bundle();
        CourseBatchDetailFragment fragment = new CourseBatchDetailFragment();
        args.putInt("type", type);
        //args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            //mId = getArguments().getString("id");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setText(mType == Configs.TYPE_GROUP?"团课排期":"私教排期");
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(item -> {
            if (dialogList == null) {
                dialogList = new DialogList(getContext());
                ArrayList<String> flows = new ArrayList<>();
                flows.add("课程种类");
                flows.add("课件");
                dialogList.list(flows, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialogList.dismiss();
                        if (position == 0){
                            getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_right_out)
                                .replace(R.id.frag, CourseListFragment.newInstance(mType == Configs.TYPE_PRIVATE))
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                        }else if (position ==1 ){
                            Intent toPlan = new Intent(getActivity(), FragActivity.class);
                            toPlan.putExtra("type", 8);
                            toPlan.putExtra("service", mCoachService);
                            startActivity(toPlan);
                        }
                    }
                });
            }
            dialogList.show();
            return true;});
        if (getActivity() instanceof BatchActivity) {
            ((BatchActivity) getActivity()).getComponent().inject(this);
        } else if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);
        }
        delegatePresenter(presenter, this);

        if ((mType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR)) || (mType
            == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR))) {
            View v = inflater.inflate(R.layout.item_common_no_data, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            img.setImageResource(R.drawable.ic_no_permission);
            TextView hint = (TextView) v.findViewById(R.id.hint);
            hint.setText(R.string.sorry_for_no_permission);
            return v;
        }

        presenter.attachView(this);
        preview.setText(
            mType == Configs.TYPE_PRIVATE ? getString(R.string.private_course_preview) : getString(R.string.group_course_preview));

        presenter.queryGroup(App.coachid + "", mType == Configs.TYPE_PRIVATE);
        mCommonFlexAdapter = new CommonFlexAdapter(mDatas, this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mCommonFlexAdapter);
        return view;
    }

    @Override public void onDestroyView() {

        super.onDestroyView();
    }

    private void showDelCourse() {
        if (delCourseDialog == null) {
            delCourseDialog = new DialogSheet(getContext());
            delCourseDialog.addButton("编辑", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //编辑课程
                    delCourseDialog.dismiss();
                }
            });
            delCourseDialog.addButton("删除", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //删除课程
                    delCourseDialog.dismiss();
                    delCourse();
                }
            });
        }
        delCourseDialog.show();
    }

    /**
     * 删除课程
     */
    private void delCourse() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("是否删除课程?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                        presenter.delCourse();
                    }
                })
                .cancelable(false)
                .build();
        }
        delDialog.show();
    }

    /**
     * 添加课程排期
     */
    @OnClick(R.id.add_batch_btn) public void onAddCouseManage() {
        if ((mType == Configs.TYPE_GROUP && !CurentPermissions.newInstance()
            .queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)) || (mType == Configs.TYPE_PRIVATE
            && !CurentPermissions.newInstance().queryPermission((PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE)))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        Intent toChooseCourse = new Intent(getActivity(), CourseActivity.class);
        toChooseCourse.putExtra("to", CourseActivity.TO_CHOOSE);
        toChooseCourse.putExtra("type", mType);
        toChooseCourse.putExtra("service", mCoachService);
        startActivityForResult(toChooseCourse, RESULT_COURSE);
    }

    @Override public String getFragmentName() {
        return CourseBatchDetailFragment.class.getName();
    }

    @Override public void onGoup(Course course, final List<QcResponseGroupDetail.GroupBatch> batch) {
        mDatas.clear();
        mOutdateDatas.clear();
        boolean isOutofDate = false;
        int pos = -1;
        for (int i = 0; i < batch.size(); i++) {
            if (!isOutofDate) {
                if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(batch.get(i).to_date))) {
                    if (mDatas.size() == 0) {
                        mDatas.add(new HintItem.Builder().text(mType == Configs.TYPE_PRIVATE ? getString(R.string.hint_no_private_course)
                            : getString(R.string.hint_no_group_course)).resBg(R.color.white).build());
                    }
                    mDatas.add(new HideBatchItem());
                    isOutofDate = true;
                    pos = mDatas.size() - 1;
                    mOutdateDatas.add(new BatchItem(batch.get(i)));
                } else {
                    mDatas.add(new BatchItem(batch.get(i)));
                }
            } else {
                mOutdateDatas.add(new BatchItem(batch.get(i)));
            }
        }
        if (mDatas.size() == 0) {
            //if (mOutdateDatas == null || mOutdateDatas.size() == 0) {
            //    mDatas.add(new HintItem.Builder().text(
            //        mType == Configs.TYPE_PRIVATE ? getString(R.string.hint_no_private_course) : getString(R.string.hint_no_group_course))
            //        .resBg(R.color.white)
            //        .build());
            //} else {
            mDatas.add(new CommonNoDataItem(R.drawable.no_batch,
                mType == Configs.TYPE_PRIVATE ? getString(R.string.hint_no_private_course) : getString(R.string.hint_no_group_course)));
            //}
        }

        mCommonFlexAdapter.notifyDataSetChanged();
        if (pos >= 0 && isShow) {
            mCommonFlexAdapter.toggleSelection(pos);
            mDatas.addAll(mOutdateDatas);
        }
        mCommonFlexAdapter.notifyDataSetChanged();
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_COURSE) {

                CourseDetail course = data.getParcelableExtra("course");
                if (course != null) {
                    getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag, AddBatchFragment.newInstance(course))
                        .addToBackStack(null)
                        .commit();
                }
            }
        }
    }

    @Deprecated @Override
    public void onPrivate(QcResponsePrivateDetail.PrivateCoach coach, final List<QcResponsePrivateDetail.PrivateBatch> batch) {
    }

    @Override public String getTitle() {
        return getArguments().getInt("type") == Configs.TYPE_PRIVATE ? "私教排期" : "团课排期";
    }

    @Override public void onShowError(String s) {
        LogUtil.e(s);
    }

    @Override public void onShowError(@StringRes int i) {
        onShowError(getString(i));
    }

    @Override public boolean onItemClick(int position) {
        if (mCommonFlexAdapter.getItem(position) instanceof BatchItem) {
            QcResponseGroupDetail.GroupBatch batch = ((BatchItem) mCommonFlexAdapter.getItem(position)).getBatch();
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.frag, BatchDetailFragment.newInstance(mType, batch.id))
                .addToBackStack(null)
                .commit();
        } else if (mCommonFlexAdapter.getItem(position) instanceof HideBatchItem) {
            mCommonFlexAdapter.toggleSelection(position);
            if (mCommonFlexAdapter.isSelected(position)) {
                isShow = true;
                mDatas.addAll(mOutdateDatas);
            } else {
                isShow = false;
                mDatas.removeAll(mOutdateDatas);
            }
            mCommonFlexAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @OnClick(R.id.preview) public void onClick() {
        WebActivity.startWeb((mType == Configs.TYPE_PRIVATE ? Configs.PRIVATE_PRIVEIW : Configs.GROUP_PRIVEIW)
            + "?id="
            + mCoachService.getId()
            + "&model="
            + mCoachService.getModel(), getContext());
    }

    @Override public void noMoreLoad(int i) {

    }

    @Override public void onLoadMore(int i, int i1) {

    }
}
