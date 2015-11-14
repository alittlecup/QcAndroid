package com.qingchengfit.fitcoach.fragment;


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

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentJudgeFragment extends BaseFragment {

    public static final String TAGS = "tags";
    public static final String EVALUATE = "EvaluateEntitys";
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
    @Bind(R.id.student_judge_tag_count)
    TextView studentJudgeTagCount;
    private String[] mTags;

    public StudentJudgeFragment() {
        // Required empty public constructor
    }

    public static StudentJudgeFragment newInstance(String[] tags,
                                                   QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity entityls) {

        Bundle args = new Bundle();

        args.putStringArray(TAGS, tags);
        args.putParcelable(EVALUATE, entityls);
        StudentJudgeFragment fragment = new StudentJudgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTags = getArguments().getStringArray(TAGS);
            mEntityls = getArguments().getParcelable(EVALUATE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_judge, container, false);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {


        if (mTags != null && mEntityls != null) {
            DecimalFormat format = new DecimalFormat("#.0");
            String coachScroe = format.format(mEntityls.getCoach_score());
            if (mEntityls.getCoach_score() == 0)
                coachScroe = "0.0";
            studentJudgeCoachScore.setText(coachScroe);
            studentJudgeCoachStar.setRating(Float.parseFloat(coachScroe));
            String courseScroe = format.format(mEntityls.getCourse_score());
            if (mEntityls.getCourse_score() == 0)
                courseScroe = "0.0";
            studentJudgeCourseScore.setText(courseScroe);
            studentJudgeCourseStar.setRating(Float.parseFloat(courseScroe));
            studentJudgeTagCount.setText("擅长 ");
            tagGroup.setTags(
                    mTags
            );

            String count = Integer.toString(mEntityls.getTotal_count());
//            String count = "1000";
            SpannableString s = new SpannableString("评分基于\n" + count + "条评论");
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 4, 4 + count.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            studentJudgeText.setText(s);
        } else {
            if (!isPrepared || !isVisible)
                return;
            QcCloudClient.getApi().getApi.qcGetEvaluate(App.coachid).subscribe(qcEvaluateResponse -> {
                getActivity().runOnUiThread(() -> {
                    mTags = qcEvaluateResponse.getData().getTagArray();
                    mEntityls = qcEvaluateResponse.getData().getHomeEvaluate();


                    if (mTags != null && mEntityls != null) {
                        studentJudgeCoachScore.setText(mEntityls.getCoach_score() + "");
                        studentJudgeCoachStar.setRating((float) mEntityls.getCoach_score());
                        studentJudgeCourseScore.setText(mEntityls.getCourse_score() + "");
                        studentJudgeCourseStar.setRating((float) mEntityls.getCourse_score());
                        tagGroup.setTags(
                                mTags
                        );
                        String count = Integer.toString(mEntityls.getTotal_count());
                        SpannableString s = new SpannableString("评论基于\n" + count + "条评论");
                        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 4, 4 + count.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        studentJudgeText.setText(s);
                    }
                });


            }, throwable -> {
                return;
            }, () -> {
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
