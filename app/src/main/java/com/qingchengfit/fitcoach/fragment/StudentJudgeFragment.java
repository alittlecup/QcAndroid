package com.qingchengfit.fitcoach.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentJudgeFragment extends Fragment {

    public static final String TAGS = "tags";
    public static final String EVALUATE = "EvaluateEntitys";
    QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys mTags;
    QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntitys mEntityls;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;
    @Bind(R.id.student_judge_coach_score)
    TextView studentJudgeCoachScore;
    @Bind(R.id.student_judge_coach_star)
    RatingBar studentJudgeCoachStar;
    @Bind(R.id.student_judge_course_score)
    TextView studentJudgeCourseScore;
    @Bind(R.id.student_judge_course_star)
    RatingBar studentJudgeCourseStar;
    @Bind(R.id.student_judge_text)
    TextView studentJudgeText;

    public StudentJudgeFragment() {
        // Required empty public constructor
    }

    public static StudentJudgeFragment newInstance(QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys tags,
                                                   QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntitys entityls) {

        Bundle args = new Bundle();

        args.putParcelable(TAGS, tags);
        args.putParcelable(EVALUATE, entityls);
        StudentJudgeFragment fragment = new StudentJudgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTags = getArguments().getParcelable(TAGS);
            mEntityls = getArguments().getParcelable(EVALUATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_judge, container, false);
        ButterKnife.bind(this, view);
        if (mTags != null && mEntityls != null) {
            studentJudgeCoachScore.setText(mEntityls.getEvaluate().getCoach_score() + "");
            studentJudgeCoachStar.setRating((float) mEntityls.getEvaluate().getCoach_score());
            studentJudgeCourseScore.setText(mEntityls.getEvaluate().getCourse_score() + "");
            studentJudgeCourseStar.setRating((float) mEntityls.getEvaluate().getCourse_score());
            tagGroup.setTags(
                    mTags.toArray()
            );

            String count = Integer.toString(mEntityls.getEvaluate().getTotal_count());
            SpannableString s = new SpannableString("评论基于\n" + count + "条评论");
            s.setSpan(new ForegroundColorSpan(Color.YELLOW), 3, 3 + count.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            studentJudgeText.setText(s);
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
