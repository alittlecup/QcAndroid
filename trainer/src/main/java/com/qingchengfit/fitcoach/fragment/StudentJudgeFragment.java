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
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import java.text.DecimalFormat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentJudgeFragment extends BaseFragment {

    public static final String TAGS = "tags";
    public static final String EVALUATE = "EvaluateEntitys";
    QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity mEntityls;
	TagGroup tagGroup;
	TextView studentJudgeCoachScore;
	RatingBar studentJudgeCoachStar;
	TextView studentJudgeCourseScore;
	RatingBar studentJudgeCourseStar;
	TextView studentJudgeText;
	TextView studentJudgeTagCount;
	TextView studentJudgeGoodatTv;
    private String[] mTags;


    public StudentJudgeFragment() {
        // Required empty public constructor
    }

    public static StudentJudgeFragment newInstance(String[] tags, QcMyhomeResponse.DataEntity.CoachEntity.EvaluateEntity entityls) {

        Bundle args = new Bundle();

        args.putStringArray(TAGS, tags);
        args.putParcelable(EVALUATE, entityls);
        StudentJudgeFragment fragment = new StudentJudgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTags = getArguments().getStringArray(TAGS);
            mEntityls = getArguments().getParcelable(EVALUATE);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_judge, container, false);
      tagGroup = (TagGroup) view.findViewById(R.id.tag_group);
      studentJudgeCoachScore = (TextView) view.findViewById(R.id.student_judge_coach_score);
      studentJudgeCoachStar = (RatingBar) view.findViewById(R.id.student_judge_coach_star);
      studentJudgeCourseScore = (TextView) view.findViewById(R.id.student_judge_course_score);
      studentJudgeCourseStar = (RatingBar) view.findViewById(R.id.student_judge_course_star);
      studentJudgeText = (TextView) view.findViewById(R.id.student_judge_text);
      studentJudgeTagCount = (TextView) view.findViewById(R.id.student_judge_tag_count);
      studentJudgeGoodatTv = (TextView) view.findViewById(R.id.student_judge_goodat_tv);

      isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override protected void lazyLoad() {

        if (mTags != null && mEntityls != null) {
            DecimalFormat format = new DecimalFormat("#.0");
            String coachScroe = format.format(mEntityls.getCoach_score());
            if (mEntityls.getCoach_score() == 0) coachScroe = "0.0";
            studentJudgeCoachScore.setText(coachScroe);
            studentJudgeCoachStar.setRating(Float.parseFloat(coachScroe));
            String courseScroe = format.format(mEntityls.getCourse_score());
            if (mEntityls.getCourse_score() == 0) courseScroe = "0.0";
            studentJudgeCourseScore.setText(courseScroe);
            studentJudgeCourseStar.setRating(Float.parseFloat(courseScroe));
            studentJudgeTagCount.setText("擅长 ");
            tagGroup.setTags(mTags);
            if (mTags.length == 0) {
                tagGroup.setVisibility(View.GONE);
                studentJudgeGoodatTv.setText("(根据课程计划统计) : 暂无数据");
            } else {
                studentJudgeGoodatTv.setText("(根据课程计划统计) :");
            }
            String count = Integer.toString(mEntityls.getTotal_count());
            //            String count = "1000";
            if (Integer.parseInt(count) > 0) {
                SpannableString s = new SpannableString("评分基于\n" + count + "条评价");
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 4, 4 + count.length() + 1,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                studentJudgeText.setText(s);
            } else {
                studentJudgeText.setText("暂无评价");
            }
        } else {
            if (!isPrepared || !isVisible) return;
            TrainerRepository.getStaticTrainerAllApi().qcGetEvaluate(App.coachid)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcEvaluateResponse -> {
                    getActivity().runOnUiThread(() -> {
                        mTags = qcEvaluateResponse.getData().getTagArray();
                        mEntityls = qcEvaluateResponse.getData().getHomeEvaluate();

                        if (mTags != null && mEntityls != null) {
                            studentJudgeCoachScore.setText(mEntityls.getCoach_score() + "");
                            studentJudgeCoachStar.setRating((float) mEntityls.getCoach_score());
                            studentJudgeCourseScore.setText(mEntityls.getCourse_score() + "");
                            studentJudgeCourseStar.setRating((float) mEntityls.getCourse_score());

                            tagGroup.setTags(mTags);
                            if (mTags.length == 0) {
                                tagGroup.setVisibility(View.GONE);
                                studentJudgeGoodatTv.setText("(根据课程计划统计) : 暂无数据");
                            } else {
                                studentJudgeGoodatTv.setText("(根据课程计划统计) :");
                            }
                            String count = Integer.toString(mEntityls.getTotal_count());
                            if (Integer.parseInt(count) > 0) {
                                SpannableString s = new SpannableString("评分基于\n" + count + "条评价");
                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 4, 4 + count.length() + 1,
                                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                studentJudgeText.setText(s);
                            } else {
                                studentJudgeText.setText("暂无评价");
                            }
                        }
                    });
                }, throwable -> {
                    return;
                }, () -> {
                });
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
