package cn.qingchengfit.staffkit.views.gym.couse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/11 2016.
 */

@Deprecated public class CourseDetailFragment extends BaseFragment implements CourseDetailView {

    @Inject CourseDetailPresenter presenter;
    @BindView(R.id.gym_addcourse_img) ImageView gymAddcourseImg;
    @BindView(R.id.gym_addcourse_img_layout) RelativeLayout gymAddcourseImgLayout;
    @BindView(R.id.course_type_private) RadioButton courseTypePrivate;
    @BindView(R.id.course_type_group) RadioButton courseTypeGroup;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    @BindView(R.id.course_type_layout) RelativeLayout courseTypeLayout;
    @BindView(R.id.course_name) CommonInputView courseName;
    @BindView(R.id.course_time) CommonInputView courseTime;
    @BindView(R.id.course_capacity) CommonInputView courseCapacity;
    @BindView(R.id.gym_course_detail_layout) LinearLayout gymCourseDetailLayout;
    @BindView(R.id.btn_del) RelativeLayout btnDel;

    private boolean isAdd = false;
    private CourseTypeSample mCourse;

    public static CourseDetailFragment newInstance() {
        CourseDetailFragment fragment = new CourseDetailFragment();
        return fragment;
    }

    public static CourseDetailFragment newInstance(CourseTypeSample student) {

        Bundle args = new Bundle();
        args.putParcelable("data", student);
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getParcelable("data");
        } else {

        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        //        ((GymComponent) mCallbackActivity.getComponent()).inject(this);

        presenter.attachView(this);
        mCallbackActivity.setToolbar("课程详情", false, null, R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(mCourse.getName())) {
                    ToastUtils.show("请填写正确名称");
                    return true;
                }
                if ((Object) mCourse.getLength() != null && mCourse.getLength() != 0) {
                    ToastUtils.show("请填写课程时长");
                    return true;
                }
                presenter.fixCourse(mCourse);
                return true;
            }
        });

        courseTypeLayout.setVisibility(View.GONE);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return EditStudentInfoFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onFixSuccess() {
        ToastUtils.showS("修改成功!");
        getActivity().onBackPressed();
    }

    @Override public void onDelSuccess() {
        getActivity().onBackPressed();
    }

    @Override public void onFailed() {

    }

    @OnClick({ R.id.gym_addcourse_img_layout, R.id.btn_del }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gym_addcourse_img_layout:
                ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance();
                dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                    @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                        if (isSuccess) {
                            CourseDetailFragment.this.RxRegiste(UpYunClient.rxUpLoad("course/", filePath).subscribe(new Action1<String>() {
                                @Override public void call(String s) {
                                    Glide.with(getActivity()).load(PhotoUtils.getSmall(s)).into(gymAddcourseImg);
                                }
                            }));
                        } else {
                            ToastUtils.show("请重新选择图片");
                        }
                    }
                });
                dialog.show(getFragmentManager(), "");
                break;
            case R.id.btn_del:
                //                presenter.
                break;
        }
    }
}
