package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.bean.CourseTeacher;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.ListUtils;
import co.hkm.soltag.TagContainerLayout;

public class CoachCommentDetailFragment extends SaasBaseFragment {

	TextView coachScore;
	TextView courseScore;
	TextView serverScore;
	TagContainerLayout comments;

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
      coachScore = (TextView) view.findViewById(R.id.coach_score);
      courseScore = (TextView) view.findViewById(R.id.course_score);
      serverScore = (TextView) view.findViewById(R.id.server_score);
      comments = (TagContainerLayout) view.findViewById(R.id.comments);

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
