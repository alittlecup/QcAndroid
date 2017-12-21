package cn.qingchengfit.saasbase.course.course.views;

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
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.course.bean.CoursePlan;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

public class CourseBaseInfoEditFragment extends BaseFragment {

    @BindView(R2.id.header_img) ImageView headerImg;
    @BindView(R2.id.header_layout) RelativeLayout headerLayout;
    @BindView(R2.id.course_name) CommonInputView courseName;
    @BindView(R2.id.course_length) CommonInputView courseLength;
    @BindView(R2.id.course_min_count) CommonInputView courseMinCount;
    @BindView(R2.id.default_course_plan) CommonInputView defaultCoursePlan;
    @BindView(R2.id.single_count) CommonInputView singleCount;
    private CourseType mCourse;
    private boolean isPrivate;

    public static CourseBaseInfoEditFragment newInstance(CourseType course) {
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
            isPrivate = mCourse.is_private;
        }
        //
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_base_info_edit, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mCourse != null) {
            Glide.with(getContext()).load(mCourse.getPhoto()).placeholder(R.drawable.ic_default_header).into(headerImg);
            courseName.setContent(mCourse.getName());
            if ((Object) mCourse.getLength() != null && mCourse.getLength() != 0) {
                courseLength.setContent(Integer.toString(mCourse.getLength() / 60));
            }
            if (mCourse.getMin_users() != 0) courseMinCount.setContent(Integer.toString(mCourse.getMin_users()));
            if (mCourse.getCapacity() != 0) singleCount.setContent(Integer.toString(mCourse.getCapacity()));

            if (!mCourse.is_private()) {//团课
                defaultCoursePlan.setVisibility(View.VISIBLE);
                if (mCourse.getPlan() != null) defaultCoursePlan.setContent(mCourse.getPlan().getName());
                courseMinCount.setVisibility(View.VISIBLE);
            } else {//私教
                courseMinCount.setVisibility(View.GONE);
                defaultCoursePlan.setVisibility(View.GONE);
            }
            //                defaultCoursePlan.setContent();课程计划
        } else {
            mCourse = new CourseType();
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
        courseMinCount.setOnClickListener(view1 -> {
            new DialogList(getContext()).list(
               isPrivate? CmStringUtils.getNums(1, 10) : CmStringUtils.getNums(1, 300),
              (parent, view2, position, id) -> {
                  int before = 8;
                  try {
                      before = Integer.parseInt(courseMinCount.getContent());
                  } catch (Exception e) {
                      LogUtil.e("上课人数不为int类型");
                  }
                  if (before != (position + 1)) {
                      courseMinCount.setContent(Integer.toString(position + 1));
                  }
              }).title("选择人数").show();
        });
        singleCount.setOnClickListener(view1 -> {
            new DialogList(getContext()).list(
              isPrivate? CmStringUtils.getNums(1, 10) : CmStringUtils.getNums(1, 300),
              (parent, view2, position, id) -> {
                  int before = 8;
                  try {
                      before = Integer.parseInt(singleCount.getContent());
                  } catch (Exception e) {
                      LogUtil.e("上课人数不为int类型");
                  }
                  if (before != (position + 1)) {
                      singleCount.setContent(Integer.toString(position + 1));
                  }
              }).title("选择人数").show();
        });
        return view;
    }

    @Nullable public CourseType getCourse() {
        if (TextUtils.isEmpty(courseName.getContent())) {
            ToastUtils.show("请填写课程名称");
            return null;
        }
        mCourse.setName(courseName.getContent());
        if (TextUtils.isEmpty(courseLength.getContent())) {
            ToastUtils.show("请填写课程时长");
            return null;
        }
        mCourse.setLength(Integer.parseInt(courseLength.getContent()) * 60);

        if (courseMinCount.getVisibility() == View.VISIBLE && TextUtils.isEmpty(courseMinCount.getContent())) {
            ToastUtils.show("请填写课程最小上课人数");
            return null;
        }
        if (courseMinCount.getVisibility() == View.VISIBLE) mCourse.setMin_users(Integer.parseInt(courseMinCount.getContent()));
        if (TextUtils.isEmpty(singleCount.getContent())) {
            ToastUtils.show("请填写单节可约人数");
            return null;
        }
        mCourse.setCapacity(Integer.parseInt(singleCount.getContent()));
        return mCourse;
    }

    @Override public String getFragmentName() {
        return CourseBaseInfoEditFragment.class.getName();
    }

    @Override public void onDetach() {
        super.onDetach();
    }

    @OnClick(R2.id.default_course_plan) public void onCoursePlan() {
        if (mCourse.getPlan() != null && mCourse.getPlan().getId() != null)
            routeTo("/plan/list/",new ChooseCoursePlanParams().mChosenId(mCourse.getPlan().getId()+"").build());
    }

    /**
     * 选择头像
     */
    @OnClick(R2.id.header_layout) public void onClick() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {

            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    Glide.with(getContext()).load(new File(filePath)).placeholder(R.drawable.ic_default_header).into(headerImg);
                    showLoading();
                    RxRegiste(UpYunClient.rxUpLoad("/course/", filePath).timeout(5000, TimeUnit.MILLISECONDS).subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            Glide.with(getContext()).load(PhotoUtils.getSmall(s)).placeholder(R.drawable.ic_default_header).into(headerImg);
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