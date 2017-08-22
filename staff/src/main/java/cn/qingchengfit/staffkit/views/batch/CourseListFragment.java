package cn.qingchengfit.staffkit.views.batch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.GroupCourse;
import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageThreeTextAdapter;
import cn.qingchengfit.staffkit.views.batch.addbatch.AddBatchFragment;
import cn.qingchengfit.staffkit.views.batch.list.CourseBatchDetailFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.WebActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends BaseFragment implements TitleFragment, CourseListView {

    @BindView(R.id.course_count) TextView courseCount;
    @BindView(R.id.preview) TextView preview;
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;

    @BindView(R.id.add_batch_btn) FloatingActionButton addBatchBtn;
    @Inject CoureseListPresenter presenter;
    @BindView(R.id.btn_how_to_use) TextView btnHowToUse;
    @BindView(R.id.btn_no_data) Button btnNoData;
    private ImageThreeTextAdapter mImageTwoTextAdapter;
    private List<ImageThreeTextBean> datas = new ArrayList<>();
    private int mCourseType = 1;//当前页的类型
    private int mGymType = 1;//个人健身房 0是同步健身房
    //    private int course_count;
    private String toUrl;
    private boolean isLoaded = false;
    private LinearLayoutManager llmanager;

    public CourseListFragment() {

    }

    /**
     * @param CourseType 1是私教 2是团课
     */

    public static CourseListFragment newInstance(int CourseType) {
        Bundle args = new Bundle();
        args.putInt("coursetype", CourseType);
        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseType = getArguments().getInt("coursetype");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR)) || (
            mCourseType == Configs.TYPE_PRIVATE
                && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR))) {
            View v = inflater.inflate(R.layout.item_common_no_data, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            img.setImageResource(R.drawable.ic_no_permission);
            TextView hint = (TextView) v.findViewById(R.id.hint);
            hint.setText(R.string.sorry_for_no_permission);
            return v;
        }

        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        llmanager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(llmanager);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!TextUtils.isEmpty(toUrl)) {
                    Intent toWeb = new Intent(getContext(), WebActivity.class);
                    toWeb.putExtra("url", toUrl);
                    startActivity(toWeb);
                }
            }
        });
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (mCourseType == Configs.TYPE_PRIVATE) {
                    presenter.getPrivate();
                } else {
                    presenter.getGroup();
                }
            }
        });

        if (mCourseType == Configs.TYPE_PRIVATE) {
            presenter.getPrivate();
        } else {
            presenter.getGroup();
        }
        if (isLoaded) recyclerview.stopLoading();
        btnHowToUse.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_grey), null,
            null, null);
        recyclerview.addScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    btnHowToUse.setVisibility(View.VISIBLE);
                } else {
                    btnHowToUse.setVisibility(View.GONE);
                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        btnHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                WebActivity.startWeb(
                    mCourseType == Configs.TYPE_PRIVATE ? Router.WEB_HOW_TO_USE_BATCH_PRIVATE : Router.WEB_HOW_TO_USE_BATCH_GROUP,
                    getContext());
            }
        });
        btnHowToUse.setText(mCourseType == Configs.TYPE_PRIVATE ? "如何添加私教排期？" : "如何添加团课排期？");
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoaded) isLoaded = true;
    }

    @OnClick({ R.id.add_batch_btn, R.id.btn_no_data }) public void onAddbatch() {
        if ((mCourseType == Configs.TYPE_GROUP && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE))
            || (mCourseType == Configs.TYPE_PRIVATE && !SerPermisAction.checkAtLeastOne(
            PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE))) {
            showAlert(R.string.alert_permission_forbid);
            return;
        }

        if (mCourseType == Configs.TYPE_PRIVATE) {

            Intent toChooseTrainer = new Intent(getActivity(), ChooseActivity.class);
            toChooseTrainer.putExtra("to", ChooseActivity.CHOOSE_TRAINER);
            startActivityForResult(toChooseTrainer, 1);
        } else {
            Intent toChooseGourp = new Intent(getActivity(), ChooseActivity.class);
            toChooseGourp.putExtra("type", Configs.TYPE_GROUP);
            startActivityForResult(toChooseGourp, 2);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Staff teacher = data.getParcelableExtra("trainer");
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(mCallbackActivity.getFragId(), AddBatchFragment.newInstance(teacher, null))
                    .addToBackStack(GymCoursesFragment.TAG)
                    .commit();
            } else if (requestCode == 2) {

                CourseTypeSample course = data.getParcelableExtra("course");
                GroupCourse groupClass = new GroupCourse();
                groupClass.id = course.getId();
                groupClass.photo = course.getPhoto();
                groupClass.length = course.getLength();
                groupClass.name = course.getName();
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(mCallbackActivity.getFragId(), AddBatchFragment.newInstance(null, groupClass))
                    .addToBackStack(GymCoursesFragment.TAG)
                    .commit();
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getTitle() {
        if (getArguments().getInt("coursetype") == Configs.TYPE_PRIVATE) {
            return "私教排期";
        } else {
            return "团课排期";
        }
    }

    @Override public void onGroup(final List<GroupCourse> groupClasses) {
        datas.clear();
        for (int i = 0; i < groupClasses.size(); i++) {
            GroupCourse g = groupClasses.get(i);
            ImageThreeTextBean b;
            if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(g.to_date))) {
                b = new ImageThreeTextBean(g.photo, g.name, "", getString(R.string.course_no_batch));
            } else {
                b = new ImageThreeTextBean(g.photo, g.name, "", g.from_date + "至" + g.to_date + "," + g.count + "节课程");
            }
            b.showRight = true;
            datas.add(b);
        }
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(llmanager);
        recyclerview.setAdapter(mImageTwoTextAdapter);
        recyclerview.setNoData(datas.size() == 0);
        if (datas.size() == 0) {
            btnNoData.setText("添加团课排期");
            btnNoData.setVisibility(View.VISIBLE);
            addBatchBtn.setVisibility(View.GONE);
        } else {
            btnNoData.setVisibility(View.GONE);
            addBatchBtn.setVisibility(View.VISIBLE);
        }
        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                //团课详情
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(mCallbackActivity.getFragId(),
                        CourseBatchDetailFragment.newInstance(Configs.TYPE_GROUP, groupClasses.get(pos).id))
                    .addToBackStack("")
                    .commit();
            }
        });
        btnHowToUse.setVisibility(datas.size() >= 5 ? View.GONE : View.VISIBLE);
    }

    @Override public void onPrivate(final List<QcResponsePrivateCourse.PrivateClass> privateClasses) {
        datas.clear();
        for (int i = 0; i < privateClasses.size(); i++) {
            QcResponsePrivateCourse.PrivateClass g = privateClasses.get(i);
            ImageThreeTextBean b;
            if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(g.to_date))) {
                b = new ImageThreeTextBean(g.avatar, g.username, "", getString(R.string.course_no_batch));
            } else {
                b = new ImageThreeTextBean(g.avatar, g.username, "", g.from_date + "至" + g.to_date + "," + g.courses_count + "种课程");
            }
            b.showRight = true;
            datas.add(b);
        }
        mImageTwoTextAdapter = new ImageThreeTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        recyclerview.setNoData(datas.size() == 0);
        if (datas.size() == 0) {
            btnNoData.setText("添加私教排期");
            btnNoData.setVisibility(View.VISIBLE);
            addBatchBtn.setVisibility(View.GONE);
        } else {
            btnNoData.setVisibility(View.GONE);
            addBatchBtn.setVisibility(View.VISIBLE);
        }
        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                //                私教课详情
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(mCallbackActivity.getFragId(),
                        CourseBatchDetailFragment.newInstance(Configs.TYPE_PRIVATE, privateClasses.get(pos).id))
                    .addToBackStack("")
                    .commit();
            }
        });
        btnHowToUse.setVisibility(datas.size() >= 5 ? View.GONE : View.VISIBLE);
    }

    @Override public void onCoursesInfo(int course_count, String url) {
        if (mCourseType == Configs.TYPE_PRIVATE) {
            courseCount.setText(String.format(getString(R.string.private_teacher_num), course_count));
            preview.setText("会员私教页预览");
        } else {
            courseCount.setText(course_count + "节团课");
            preview.setText("会员团课页预览");
        }

        toUrl = url;
    }

    @Override public String getFragmentName() {
        if (isAdded()) {
            if (mCourseType == Configs.TYPE_PRIVATE) {
                return "私教排期";
            } else if (mCourseType == Configs.TYPE_GROUP) {
                return getString(R.string.course_type_group) + "排期";
            } else {
                return getString(R.string.course_type_group) + "排期";
            }
        } else {
            return "";
        }
    }
}
