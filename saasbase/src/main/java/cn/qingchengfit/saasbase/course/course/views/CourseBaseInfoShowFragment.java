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


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import java.util.Locale;

public class CourseBaseInfoShowFragment extends BaseFragment {

	ImageView img;
	ImageView imgFoot;
	TextView text1;
	ImageView texticon;
	TextView text2;
	TextView text3;
	ImageView righticon;
	RelativeLayout courseLayout;
	TextView minCourseNum;
	LinearLayout minCourseNumLayout;
	TextView defaultCoursePlan;
	LinearLayout defaultCoursePlanLayout;
	TextView suitGyms;
	LinearLayout suitGymsLayout;
	TextView singleOrderCount;
	FrameLayout imgLayout;
	LinearLayout layoutSingleOrder;

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
      img = (ImageView) view.findViewById(R.id.img);
      imgFoot = (ImageView) view.findViewById(R.id.img_foot);
      text1 = (TextView) view.findViewById(R.id.text1);
      texticon = (ImageView) view.findViewById(R.id.texticon);
      text2 = (TextView) view.findViewById(R.id.text2);
      text3 = (TextView) view.findViewById(R.id.text3);
      righticon = (ImageView) view.findViewById(R.id.righticon);
      courseLayout = (RelativeLayout) view.findViewById(R.id.course_layout);
      minCourseNum = (TextView) view.findViewById(R.id.min_course_num);
      minCourseNumLayout = (LinearLayout) view.findViewById(R.id.min_course_num_layout);
      defaultCoursePlan = (TextView) view.findViewById(R.id.default_course_plan);
      defaultCoursePlanLayout = (LinearLayout) view.findViewById(R.id.default_course_plan_layout);
      suitGyms = (TextView) view.findViewById(R.id.suit_gyms);
      suitGymsLayout = (LinearLayout) view.findViewById(R.id.suit_gyms_layout);
      singleOrderCount = (TextView) view.findViewById(R.id.single_order_count);
      imgLayout = (FrameLayout) view.findViewById(R.id.img_layout);
      layoutSingleOrder = (LinearLayout) view.findViewById(R.id.layout_single_order);

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