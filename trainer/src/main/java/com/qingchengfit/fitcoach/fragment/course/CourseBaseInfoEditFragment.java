package com.qingchengfit.fitcoach.fragment.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.CoursePlan;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class CourseBaseInfoEditFragment extends BaseFragment {

    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.header_layout) RelativeLayout headerLayout;
    @BindView(R.id.course_name) CommonInputView courseName;
    @BindView(R.id.course_length) CommonInputView courseLength;
    @BindView(R.id.course_min_count) CommonInputView courseMinCount;
    @BindView(R.id.default_course_plan) CommonInputView defaultCoursePlan;
    @BindView(R.id.single_count) CommonInputView singleCount;
    @Inject CoachService mCoachService;
    private CourseDetail mCourse;
    private Unbinder unbinder;

    public static CourseBaseInfoEditFragment newInstance(CourseDetail course) {
        Bundle args = new Bundle();
        args.putParcelable("course", course);
        CourseBaseInfoEditFragment fragment = new CourseBaseInfoEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getParcelable("course");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_base_info_edit, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity) {
            ((CourseActivity) getActivity()).getComponent().inject(this);
        }
        if (mCourse != null) {
            Glide.with(getContext()).load(mCourse.getPhoto()).placeholder(R.drawable.ic_default_header).into(headerImg);
            courseName.setContent(mCourse.getName());
            if (mCourse.getLength() != 0) courseLength.setContent(Integer.toString(mCourse.getLength() / 60));
            if (mCourse.getMin_users() != 0) courseMinCount.setContent(Integer.toString(mCourse.getMin_users()));
            if (mCourse.getCapacity() != 0) singleCount.setContent(Integer.toString(mCourse.getCapacity()));

            if (!mCourse.is_private()) {//团课
                defaultCoursePlan.setVisibility(View.VISIBLE);
                if (mCourse.getPlan() != null) defaultCoursePlan.setContent(mCourse.getPlan().getName());
                courseMinCount.setVisibility(View.VISIBLE);
                singleCount.setVisibility(View.VISIBLE);
            } else {//私教
                courseMinCount.setVisibility(View.GONE);
                defaultCoursePlan.setVisibility(View.GONE);
                singleCount.setVisibility(View.GONE);
            }
        } else {
            mCourse = new CourseDetail();
        }
        RxBusAdd(CoursePlan.class).subscribe(new Action1<CoursePlan>() {
            @Override public void call(CoursePlan coursePlan) {
                if (coursePlan.getId() > 0) {
                    defaultCoursePlan.setContent(coursePlan.getName());
                    mCourse.setPlan(coursePlan);
                } else {
                    mCourse.setPlan(null);
                    defaultCoursePlan.setContent("不使用");
                }
            }
        });
        return view;
    }

    @Nullable public CourseDetail getCourse() {
        if (TextUtils.isEmpty(courseName.getContent())) {
            ToastUtils.show("请填写课程名称");
            return null;
        }
        mCourse.setName(courseName.getContent());
        if (TextUtils.isEmpty(courseLength.getContent())) {
            ToastUtils.show("请填写课程时长");
            return null;
        }
        mCourse.setLength(Float.parseFloat(courseLength.getContent()) * 60);

        if (courseMinCount.getVisibility() == View.VISIBLE && TextUtils.isEmpty(courseMinCount.getContent())) {
            ToastUtils.show("请填写课程最小上课人数");
            return null;
        }
        if (courseMinCount.getVisibility() == View.VISIBLE) mCourse.setMin_users(Integer.parseInt(courseMinCount.getContent()));
        if (singleCount.getVisibility() == View.VISIBLE && TextUtils.isEmpty(singleCount.getContent())) {
            ToastUtils.show("请填写单节可约人数");
            return null;
        }
        if (singleCount.getVisibility() == View.VISIBLE) mCourse.setCapacity(Integer.parseInt(singleCount.getContent()));
        return mCourse;
    }

    @Override public String getFragmentName() {
        return CourseBaseInfoEditFragment.class.getName();
    }

    @Override public void onDetach() {
        super.onDetach();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.default_course_plan) public void onCoursePlan() {
        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frag, x).addToBackStack("").show(x).commit();
        Intent toPlan = new Intent(getActivity(), CourseActivity.class);
        toPlan.putExtra("service", mCoachService);
        toPlan.putExtra("id", (mCourse.getPlan() == null || mCourse.getPlan().getId() == null) ? 0L : mCourse.getPlan().getId());
        toPlan.putExtra("to", CourseActivity.TO_CHOOSE_PLAN);
        startActivity(toPlan);
    }

    /**
     * 选择头像
     */
    @OnClick(R.id.header_layout) public void onClick() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {

            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    showLoading();
                    RxRegiste(UpYunClient.rxUpLoad("course/", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            Glide.with(getContext()).load(PhotoUtils.getSmall(s)).into(headerImg);
                            mCourse.setPhoto(s);
                            hideLoading();
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            hideLoading();
                        }
                    }));
                }
            }
        });
        choosePictureFragmentDialog.show(getFragmentManager(), "");
    }
}
