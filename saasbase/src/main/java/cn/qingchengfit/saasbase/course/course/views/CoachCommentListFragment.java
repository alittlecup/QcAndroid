package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.course.course.adapter.CoachCommentAdapter;
import cn.qingchengfit.saasbase.course.course.bean.CourseTeacher;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
@Leaf(module = "course",path = "/coach/comment/")
public class CoachCommentListFragment extends SaasBaseFragment {

    @BindView(R2.id.viewpager) ViewPager viewpager;
    @BindView(R2.id.strip) PagerSlidingTabImageStrip strip;

    @BindView(R2.id.shop_img) ImageView shopImg;
    @BindView(R2.id.shop_name) TextView shopName;
    @BindView(R2.id.coach_score) TextView coachScore;
    @BindView(R2.id.course_score) TextView courseScore;
    @BindView(R2.id.server_score) TextView serverScore;
    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.hint) TextView hint;
    @BindView(R2.id.no_data_layout) LinearLayout noDataLayout;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ICourseModel courseModel;

    private CoachCommentAdapter adapter;
    List<CourseTeacher> coaches = new ArrayList<>();
    @Need public String course_id;
    @Need public String shop_id;



    @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_comment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        RxRegiste(courseModel
            .qcGetCourseTeacher(course_id,shop_id)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponseCourseTeacher -> {
                if (qcResponseCourseTeacher.data != null
                    && qcResponseCourseTeacher.data.teachers != null
                    && qcResponseCourseTeacher.data.teachers.size() > 0) {
                    coaches.clear();
                    coaches.addAll(qcResponseCourseTeacher.data.teachers);
                    noDataLayout.setVisibility(View.GONE);
                    adapter = new CoachCommentAdapter(getFragmentManager(), coaches);
                    viewpager.setAdapter(adapter);
                    strip.setViewPager(viewpager);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.no_teacher);
                    hint.setText(R.string.no_teacher_in_course);
                }
                if (qcResponseCourseTeacher.data != null && qcResponseCourseTeacher.data.shop != null) {
                    toolbarTitile.setText(qcResponseCourseTeacher.data.courseDetail.getName());
                    Glide.with(getContext())
                        .load(qcResponseCourseTeacher.data.shop.photo)
                        .placeholder(R.drawable.ic_default_header)
                        .into(shopImg);
                    shopName.setText(qcResponseCourseTeacher.data.shop.name);
                    courseScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getCourse_score()));
                    coachScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getTeacher_score()));
                    serverScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getService_score()));
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("教练评价");
    }

    @Override public String getFragmentName() {
        return CoachCommentListFragment.class.getName();
    }

}
