package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import java.util.Locale;

public class CourseBaseInfoShowFragment extends BaseFragment {

    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.img_foot) ImageView imgFoot;
    @BindView(R2.id.text1) TextView text1;
    @BindView(R2.id.texticon) ImageView texticon;
    @BindView(R2.id.text2) TextView text2;
    @BindView(R2.id.text3) TextView text3;
    @BindView(R2.id.righticon) ImageView righticon;
    @BindView(R2.id.course_layout) RelativeLayout courseLayout;
    @BindView(R2.id.min_course_num) TextView minCourseNum;
    @BindView(R2.id.min_course_num_layout) LinearLayout minCourseNumLayout;
    @BindView(R2.id.default_course_plan) TextView defaultCoursePlan;
    @BindView(R2.id.default_course_plan_layout) LinearLayout defaultCoursePlanLayout;
    @BindView(R2.id.suit_gyms) TextView suitGyms;
    @BindView(R2.id.suit_gyms_layout) LinearLayout suitGymsLayout;
    @BindView(R2.id.single_order_count) TextView singleOrderCount;
    @BindView(R2.id.img_layout) FrameLayout imgLayout;
    @BindView(R2.id.layout_single_order) LinearLayout layoutSingleOrder;

    private CourseType mCourse;

    public static CourseBaseInfoShowFragment newInstance(CourseType course) {

        Bundle args = new Bundle();
        args.putParcelable("course", course);
        CourseBaseInfoShowFragment fragment = new CourseBaseInfoShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = (CourseType) getArguments().getParcelable("course");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_base_info_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        setView();
        return view;
    }

    private void setView() {
        if (mCourse != null) {
            Glide.with(getContext()).load(mCourse.getPhoto()).placeholder(R.drawable.img_loadingimage).into(img);
            imgFoot.setVisibility(View.GONE);
            text1.setText(mCourse.getName());
            text2.setVisibility(View.GONE);
            text3.setText(String.format(Locale.CHINA, "时长%d分钟", mCourse.getLength() / 60)
                .concat(", ")
                .concat(String.format(Locale.CHINA, "累计%d节课", mCourse.getSchedule_count())));
            singleOrderCount.setText(mCourse.getCapacity() + "");

            //默认课程计划
            if (mCourse.is_private()) {
                defaultCoursePlanLayout.setVisibility(View.GONE);
                minCourseNumLayout.setVisibility(View.GONE);
                layoutSingleOrder.setVisibility(View.GONE);
            } else {
                defaultCoursePlanLayout.setVisibility(View.VISIBLE);
                if (mCourse.getPlan() != null) defaultCoursePlan.setText(mCourse.getPlan().getName());
                minCourseNumLayout.setVisibility(View.VISIBLE);
                //最小上课人数
                minCourseNum.setText("" + mCourse.getMin_users());//切忌传int
            }

            //适用场馆
            if (getParentFragment() instanceof EditCourseFragment) {
                suitGymsLayout.setVisibility(View.GONE);
            } else {
                suitGymsLayout.setVisibility(View.VISIBLE);
                suitGyms.setText(mCourse.getShopStr());
            }
        }
    }

    @Override public String getFragmentName() {
        return CourseBaseInfoShowFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}