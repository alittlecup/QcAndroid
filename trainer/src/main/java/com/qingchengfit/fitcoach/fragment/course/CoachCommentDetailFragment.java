package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import co.hkm.soltag.TagContainerLayout;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.BusinessUtils;
import com.qingchengfit.fitcoach.bean.CourseTeacher;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;

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
 * Created by Paper on 16/7/25.
 */
public class CoachCommentDetailFragment extends BaseFragment {

    @BindView(R.id.coach_score) TextView coachScore;
    @BindView(R.id.course_score) TextView courseScore;
    @BindView(R.id.server_score) TextView serverScore;
    @BindView(R.id.comments) TagContainerLayout comments;
    @BindView(R.id.coach_img) ImageView coachImg;
    @BindView(R.id.coach_name) TextView coachName;

    private CourseTeacher mCourseTeacher;
    private Unbinder unbinder;

    public static CoachCommentDetailFragment newInstance(CourseTeacher courseTeacher) {

        Bundle args = new Bundle();
        args.putParcelable("c", courseTeacher);
        CoachCommentDetailFragment fragment = new CoachCommentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseTeacher = getArguments().getParcelable("c");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_comment, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (mCourseTeacher == null || mCourseTeacher.getImpressions() == null || mCourseTeacher.getImpressions().size() == 0) {
            comments.setVisibility(View.GONE);
        } else {
            comments.setVisibility(View.VISIBLE);
            comments.setTags(BusinessUtils.impress2Str(mCourseTeacher.getImpressions()));
        }
        coachName.setText(mCourseTeacher.getUser().name);
        Glide.with(getContext()).load(mCourseTeacher.getUser().header).asBitmap().into(new CircleImgWrapper(coachImg, getContext()));
        coachScore.setText(StringUtils.getFloatDot1(mCourseTeacher.getTeacher_score()));
        courseScore.setText(StringUtils.getFloatDot1(mCourseTeacher.getCourse_score()));
        serverScore.setText(StringUtils.getFloatDot1(mCourseTeacher.getService_score()));

        return view;
    }

    @Override public String getFragmentName() {
        return CoachCommentDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
