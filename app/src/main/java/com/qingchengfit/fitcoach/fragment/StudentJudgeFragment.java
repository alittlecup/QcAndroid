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

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentJudgeFragment extends BaseFragment {

    public static final String TAGS = "tags";
    public static final String EVALUATE = "EvaluateEntitys";
    QcMyhomeResponse.DataEntity.CoachEntity.TagsEntitys mTags;
    QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity mEntityls;
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
                                                   QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity entityls) {

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
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;

        if (mTags != null && mEntityls != null) {
            studentJudgeCoachScore.setText(mEntityls.getCoach_score() + "");
            studentJudgeCoachStar.setRating((float) mEntityls.getCoach_score());
            studentJudgeCourseScore.setText(mEntityls.getCourse_score() + "");
            studentJudgeCourseStar.setRating((float) mEntityls.getCourse_score());
            tagGroup.setTags(
                    mTags.toArray()
            );

            String count = Integer.toString(mEntityls.getTotal_count());
            SpannableString s = new SpannableString("评论基于\n" + count + "条评论");
            s.setSpan(new ForegroundColorSpan(Color.YELLOW), 3, 3 + count.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            studentJudgeText.setText(s);
        } else {
            QcCloudClient.getApi().getApi.qcGetEvaluate(App.coachid).subscribe(qcEvaluateResponse -> {
                getActivity().runOnUiThread(() -> {
                    mTags = qcEvaluateResponse.getData().getHomeTags();
                    mEntityls = qcEvaluateResponse.getData().getHomeEvaluate();


                    if (mTags != null && mEntityls != null) {
                        studentJudgeCoachScore.setText(mEntityls.getCoach_score() + "");
                        studentJudgeCoachStar.setRating((float) mEntityls.getCoach_score());
                        studentJudgeCourseScore.setText(mEntityls.getCourse_score() + "");
                        studentJudgeCourseStar.setRating((float) mEntityls.getCourse_score());
                        tagGroup.setTags(
                                mTags.toArray()
                        );

                        String count = Integer.toString(mEntityls.getTotal_count());
                        SpannableString s = new SpannableString("评论基于\n" + count + "条评论");
                        s.setSpan(new ForegroundColorSpan(Color.CYAN), 4, 4 + count.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        studentJudgeText.setText(s);
                    }
                });


            });
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
