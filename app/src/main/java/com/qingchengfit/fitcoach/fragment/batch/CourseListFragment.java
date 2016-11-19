package com.qingchengfit.fitcoach.fragment.batch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.inject.commpont.GymComponent;
import cn.qingchengfit.staffkit.model.bean.Course;
import cn.qingchengfit.staffkit.model.bean.ImageThreeTextBean;
import cn.qingchengfit.staffkit.model.dataaction.SerPermisAction;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.response.QcResponseGroupCourse;
import cn.qingchengfit.staffkit.usecase.response.QcResponsePrivateCourse;
import cn.qingchengfit.staffkit.usecase.response.QcSchedulesResponse;
import cn.qingchengfit.staffkit.utils.DateUtils;
import cn.qingchengfit.staffkit.utils.IntentUtils;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.adapter.ImageThreeTextAdapter;
import cn.qingchengfit.staffkit.views.batch.addbatch.AddBatchFragment;
import cn.qingchengfit.staffkit.views.batch.list.CourseBatchDetailFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.ChooseCoachFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends BaseFragment implements TitleFragment, CourseListView {


    @Bind(R.id.course_count)
    TextView courseCount;
    @Bind(R.id.preview)
    TextView preview;
    @Bind(R.id.recyclerview)
    RecycleViewWithNoImg recyclerview;
    @Bind(R.id.no_data)
    LinearLayout noData;
    @Bind(R.id.add_batch_btn)
    Button addBatchBtn;

    private ImageThreeTextAdapter mImageTwoTextAdapter;
    private List<ImageThreeTextBean> datas = new ArrayList<>();
    private int mCourseType = 1;//当前页的类型
    private int mGymType = 1;//个人健身房 0是同步健身房
    //    private int course_count;
    private String toUrl;
    @Inject
    CoureseListPresenter presenter;
    @Inject
    CoachService coachService;

    private boolean isLoaded = false;

    /**
     * @param CourseType 1是私教 2是团课
     * @return
     */

    public static CourseListFragment newInstance(int CourseType) {
        Bundle args = new Bundle();
        args.putInt("coursetype", CourseType);
        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseType = getArguments().getInt("coursetype");
        }


    }

    public CourseListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR))
                || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR))) {
            View v = inflater.inflate(R.layout.item_common_no_data, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            img.setImageResource(R.drawable.ic_no_permission);
            TextView hint = (TextView) v.findViewById(R.id.hint);
            hint.setText(R.string.sorry_for_no_permission);
            return v;
        }

        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        ButterKnife.bind(this, view);
        ((GymComponent) mCallbackActivity.getComponent()).inject(this);
        presenter.attachView(this);
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(toUrl)) {
                    Intent toWeb = new Intent(getContext(), WebActivity.class);
                    toWeb.putExtra("url", toUrl);
                    startActivity(toWeb);
                }
            }
        });
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCourseType == Configs.TYPE_PRIVATE) {
                    addBatchBtn.setText(getString(R.string.add_private_batch));
                    presenter.getPrivate();

                } else {
                    addBatchBtn.setText(getString(R.string.add_group_batch));
                    presenter.getGroup();
                }
            }
        });


        if (mCourseType == Configs.TYPE_PRIVATE) {
            addBatchBtn.setText(getString(R.string.add_private_batch));
            presenter.getPrivate();

        } else {
            addBatchBtn.setText(getString(R.string.add_group_batch));
            presenter.getGroup();
        }
        if (isLoaded)
            recyclerview.stopLoading();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoaded)
            isLoaded = true;
    }

    @OnClick(R.id.add_batch_btn)
    public void onAddbatch() {
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE))
                || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        if (mCourseType == Configs.TYPE_PRIVATE) {
            ChooseCoachFragment.start(this, 1, "", Configs.INIT_TYPE_CHOOSE);
        } else {
            Intent toChooseGourp = new Intent(getActivity(), ChooseActivity.class);
            toChooseGourp.putExtra(Configs.EXTRA_GYM_SERVICE,coachService);
            toChooseGourp.putExtra("type", Configs.TYPE_GROUP);
            startActivityForResult(toChooseGourp, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                QcSchedulesResponse.Teacher teacher = new QcSchedulesResponse.Teacher();
                teacher.username = IntentUtils.getIntentString(data, 0);
                teacher.id = IntentUtils.getIntentString(data, 1);
                teacher.avatar = IntentUtils.getIntentString(data, 2);
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), AddBatchFragment.newInstance(teacher, null))
                        .addToBackStack(GymCoursesFragment.TAG)
                        .commit();


            } else if (requestCode == 2) {

                Course course = data.getParcelableExtra("course");
                QcResponseGroupCourse.GroupClass groupClass = new QcResponseGroupCourse.GroupClass();
                groupClass.id = course.getId();
                groupClass.photo = course.getPhoto();
                groupClass.length = course.getLength();
                groupClass.name = course.getName();
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), AddBatchFragment.newInstance(null, groupClass))
                        .addToBackStack(GymCoursesFragment.TAG)
                        .commit();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public String getTitle() {
        if (getArguments().getInt("coursetype") == Configs.TYPE_PRIVATE)
            return "私教排期";
        else
            return "团课排期";

    }

    @Override
    public void onGroup(final List<QcResponseGroupCourse.GroupClass> groupClasses) {
        datas.clear();
        for (int i = 0; i < groupClasses.size(); i++) {
            QcResponseGroupCourse.GroupClass g = groupClasses.get(i);
            ImageThreeTextBean b;
            if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(g.to_date))) {
                b = new ImageThreeTextBean(g.photo, g.name, "", getString(R.string.course_no_batch));
            } else
                b = new ImageThreeTextBean(g.photo, g.name, "", g.from_date + "至" + g.to_date + "," + g.count + "节课程");
            b.showRight = true;
            datas.add(b);
        }
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
//        if (datas.size() == 0)
//            noData.setVisibility(View.VISIBLE);
//        else noData.setVisibility(View.GONE);
        recyclerview.setNoData(datas.size() == 0);
        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
//                        || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
//                    showAlert(R.string.alert_permission_forbid);
//                    return;
//                }
                //团课详情
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), CourseBatchDetailFragment.newInstance(Configs.TYPE_GROUP, groupClasses.get(pos).id))
                        .addToBackStack("")
                        .commit();
            }
        });
    }

    @Override
    public void onPrivate(final List<QcResponsePrivateCourse.PrivateClass> privateClasses) {
        datas.clear();
        for (int i = 0; i < privateClasses.size(); i++) {
            QcResponsePrivateCourse.PrivateClass g = privateClasses.get(i);
            ImageThreeTextBean b;
            if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(g.to_date))) {
                b = new ImageThreeTextBean(g.avatar, g.username, "", getString(R.string.course_no_batch));
            } else
                b = new ImageThreeTextBean(g.avatar, g.username, "", g.from_date + "至" + g.to_date + "," + g.courses_count + "种课程");
            b.showRight = true;
            datas.add(b);
        }
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        recyclerview.setNoData(datas.size() == 0);


        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
//                        || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
//                    showAlert(R.string.alert_permission_forbid);
//                    return;
//                }
//                私教课详情
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), CourseBatchDetailFragment.newInstance(Configs.TYPE_PRIVATE, privateClasses.get(pos).id))
                        .addToBackStack("")
                        .commit();
            }
        });


    }

    @Override
    public void onCoursesInfo(int course_count, String url) {
        if (mCourseType == Configs.TYPE_PRIVATE) {
            courseCount.setText(String.format(getString(R.string.private_teacher_num), course_count));
            preview.setText("会员私教页预览");
        } else {
            courseCount.setText(course_count + "节团课");
            preview.setText("会员团课页预览");
        }

        toUrl = url;
    }

    @Override
    public String getFragmentName() {
        if (isAdded()) {
            if (mCourseType == Configs.TYPE_PRIVATE) {
                return "私教排期";
            } else if (mCourseType == Configs.TYPE_GROUP) {
                return getString(R.string.course_type_group) + "排期";
            } else {
                return getString(R.string.course_type_group) + "排期";
            }
        } else return "";
    }
}
