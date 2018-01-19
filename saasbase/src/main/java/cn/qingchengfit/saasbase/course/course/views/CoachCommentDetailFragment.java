package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.CourseTeacher;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.ListUtils;
import co.hkm.soltag.TagContainerLayout;

public class CoachCommentDetailFragment extends SaasBaseFragment {

    @BindView(R2.id.coach_score) TextView coachScore;
    @BindView(R2.id.course_score) TextView courseScore;
    @BindView(R2.id.server_score) TextView serverScore;
    @BindView(R2.id.comments) TagContainerLayout comments;

    private CourseTeacher mCourseTeacher;

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
            comments.setTags(ListUtils.ListObj2Str(mCourseTeacher.getImpressions()));
        }
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
    }
}
