package com.qingchengfit.fitcoach.fragment.batch.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.action.SerPermisAction;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.VpFragment;
import com.qingchengfit.fitcoach.fragment.batch.BatchActivity;
import com.qingchengfit.fitcoach.fragment.batch.addbatch.AddBatchFragment;
import com.qingchengfit.fitcoach.fragment.batch.details.BatchDetailFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateDetail;
import com.qingchengfit.fitcoach.items.BatchItem;
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
public class CourseBatchDetailFragment extends VpFragment implements CourseBatchDetailView, FlexibleAdapter.OnItemClickListener {
    public static final int RESULT_COURSE = 1;

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @Inject CourseBatchDetailPresenter presenter;
    private DialogSheet delCourseDialog;
    private MaterialDialog delDialog;

    private int mType;

    private Course mCourese;
    private QcResponsePrivateDetail.PrivateCoach mTeacher;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    CommonFlexAdapter mCommonFlexAdapter;

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
        if (getActivity() instanceof BatchActivity) {
            ((BatchActivity) getActivity()).getComponent().inject(this);
        }
        presenter.attachView(this);

        presenter.queryGroup(App.coachid+"", mType == Configs.TYPE_PRIVATE);
        mCommonFlexAdapter = new CommonFlexAdapter(mDatas, this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mCommonFlexAdapter);
        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
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
        if ((mType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)) || (
            mType == Configs.TYPE_PRIVATE
                && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }
        Intent toChooseCourse = new Intent(getActivity(), CourseActivity.class);
        toChooseCourse.putExtra("to",CourseActivity.TO_CHOOSE);
        toChooseCourse.putExtra("type",mType);
        startActivityForResult(toChooseCourse,RESULT_COURSE);

        //getParentFragment().getFragmentManager()
        //    .beginTransaction()
        //    .replace(R.id.frag, AddBatchFragment.newInstance(teacher, groupClass))
        //    .addToBackStack(null)
        //    .commit();
    }

    @Override public String getFragmentName() {
        return CourseBatchDetailFragment.class.getName();
    }

    @Override public void onGoup(Course course, final List<QcResponseGroupDetail.GroupBatch> batch) {
        mDatas.clear();
        boolean isOutofDate = false;
        for (int i = 0; i < batch.size(); i++) {
            if (!isOutofDate && DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(batch.get(i).to_date))){
                mDatas.add(new HintItem.Builder().text("--已过期排期--").resBg(R.color.bg_grey).build());
                isOutofDate = true;
            }
            mDatas.add(new BatchItem(batch.get(i)));
        }
        mCommonFlexAdapter.notifyDataSetChanged();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == RESULT_COURSE){

                CourseDetail course = data.getParcelableExtra("course");
                if (course != null) {
                    getParentFragment().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag, AddBatchFragment.newInstance(course))
                        .addToBackStack(null)
                        .commit();
                }
            }

        }
    }

    @Deprecated
    @Override public void onPrivate(QcResponsePrivateDetail.PrivateCoach coach, final List<QcResponsePrivateDetail.PrivateBatch> batch) {
    }

    @Override public String getTitle() {
        return getArguments().getInt("type") == Configs.TYPE_PRIVATE?"私教排期":"团课排期";
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
            getParentFragment().getFragmentManager().beginTransaction()
                .replace(R.id.frag, BatchDetailFragment.newInstance(mType,batch.id))
                .addToBackStack(null)
                .commit();
        }
        return true;
    }
}
