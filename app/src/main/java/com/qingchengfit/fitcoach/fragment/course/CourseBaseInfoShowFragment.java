package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import java.util.Locale;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/27.
 */
public class CourseBaseInfoShowFragment extends BaseFragment {

    @BindView(R.id.img) ImageView img;
    //    @BindView(R.id.img_foot)
    //    ImageView imgFoot;
    @BindView(R.id.text1) TextView text1;
    @BindView(R.id.texticon) ImageView texticon;
    @BindView(R.id.text2) TextView text2;
    @BindView(R.id.text3) TextView text3;
    @BindView(R.id.righticon) ImageView righticon;
    @BindView(R.id.course_layout) RelativeLayout courseLayout;
    @BindView(R.id.min_course_num) TextView minCourseNum;
    @BindView(R.id.min_course_num_layout) LinearLayout minCourseNumLayout;
    @BindView(R.id.default_course_plan) TextView defaultCoursePlan;
    @BindView(R.id.default_course_plan_layout) LinearLayout defaultCoursePlanLayout;
    @BindView(R.id.suit_gyms) TextView suitGyms;
    @BindView(R.id.suit_gyms_layout) LinearLayout suitGymsLayout;
    @BindView(R.id.single_order_count) TextView singleOrderCount;
    @BindView(R.id.layout_single) LinearLayout layoutSingle;

    private CourseDetail mCourse;
    private Unbinder unbinder;

    public static CourseBaseInfoShowFragment newInstance(CourseDetail course) {

        Bundle args = new Bundle();
        args.putParcelable("course", course);
        CourseBaseInfoShowFragment fragment = new CourseBaseInfoShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = (CourseDetail) getArguments().getParcelable("course");
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
            //            imgFoot.setVisibility(View.GONE);
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
                layoutSingle.setVisibility(View.GONE);
            } else {
                defaultCoursePlanLayout.setVisibility(View.VISIBLE);
                if (mCourse.getPlan() != null) defaultCoursePlan.setText(mCourse.getPlan().getName());
                minCourseNumLayout.setVisibility(View.VISIBLE);
                //最小上课人数
                minCourseNum.setText("" + mCourse.getMin_users());//切忌传int
                layoutSingle.setVisibility(View.VISIBLE);
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
        unbinder.unbind();
    }
}
