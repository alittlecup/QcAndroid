//package cn.qingchengfit.staffkit.views.gym.addcourse;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.RxBus;
//import cn.qingchengfit.model.body.CourseBody;
//import cn.qingchengfit.model.responese.CourseTypeSample;
//import cn.qingchengfit.staffkit.App;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.constant.Configs;
//import cn.qingchengfit.staffkit.rxbus.event.SaveEvent;
//import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
//import cn.qingchengfit.staffkit.views.batch.ManageCourseBatchFragment;
//import cn.qingchengfit.utils.PhotoUtils;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.utils.UpYunClient;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
//import cn.qingchengfit.widgets.CommonInputView;
//import com.bumptech.glide.Glide;
//import java.util.ArrayList;
//import javax.inject.Inject;
//import rx.functions.Action1;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/1/28 2016.
// */
//public class AddGuideCourseFragment extends BaseFragment implements AddGuideCourseView {
//
//    public static final int COURSE_GUIDE = 0;
//    public static final int COURSE_ADD = 1;
//    public static final int COURSE_EDIT = 2;
//    @BindView(R.id.gym_addcourse_img) ImageView gymAddcourseImg;
//    @BindView(R.id.gym_addcourse_img_layout) RelativeLayout gymAddcourseImgLayout;
//    //    @BindView(R.id.course_type_private)
//    //    RadioButton courseTypePrivate;
//    //    @BindView(R.id.course_type_group)
//    //    RadioButton courseTypeGroup;
//    //    @BindView(R.id.course_type_rg)
//    //    RadioGroup courseTypeRg;
//    @BindView(R.id.course_type_layout) RelativeLayout courseTypeLayout;
//    @BindView(R.id.course_name) CommonInputView courseName;
//    @BindView(R.id.course_time) CommonInputView courseTime;
//    //    @BindView(R.id.course_capacity)
//    //    CommonInputView courseCapacity;
//    @BindView(R.id.add_gym_course_btn) Button addGymCourseBtn;
//    @Inject AddGuideCoursePresenter presenter;
//    CourseBody post = new CourseBody();
//    @BindView(R.id.guide_step_1) ImageView guideStep1;
//    @BindView(R.id.gym_course_detail_layout) LinearLayout gymCourseDetailLayout;
//    @BindView(R.id.guide_title) TextView guideTitle;
//    private String uploadImg = "";
//    private int mTyoe = COURSE_GUIDE;
//    private CourseTypeSample mCourse;
//    private int mCourseType = Configs.TYPE_PRIVATE;
//    private boolean isPrivate = false;
//
//    private TextWatcher textChange = new TextWatcher() {
//        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override public void afterTextChanged(Editable s) {
//            addGymCourseBtn.setEnabled(!TextUtils.isEmpty(courseName.getContent()) && !TextUtils.isEmpty(courseTime.getContent())
//                //                            && !TextUtils.isEmpty(courseCapacity.getContent())
//
//            );
//        }
//    };
//
//    public static AddGuideCourseFragment newInstance(int type, int coursetype, CourseTypeSample course) {
//        Bundle args = new Bundle();
//        args.putParcelable("course", course);
//        args.putInt("type", type);
//        args.putInt("coursetype", coursetype);
//        AddGuideCourseFragment fragment = new AddGuideCourseFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public static AddGuideCourseFragment newInstance(boolean isPrivate) {
//        Bundle args = new Bundle();
//        args.putBoolean("isPrivate", isPrivate);
//        AddGuideCourseFragment fragment = new AddGuideCourseFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mTyoe = getArguments().getInt("type", COURSE_GUIDE);
//            mCourse = getArguments().getParcelable("course");
//            mCourseType = getArguments().getInt("coursetype");
//            isPrivate = getArguments().getBoolean("isPrivate", false);
//        }
//    }
//
//    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_manage_course, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        delegatePresenter(presenter, this);
//        mCallbackActivity.setToolbar("安排课程", false, null, 0, null);
//        courseName.addTextWatcher(textChange);
//        courseTime.addTextWatcher(textChange);
//        courseTypeLayout.setVisibility(View.GONE);
//        initData();
//        return view;
//    }
//
//    private void initData() {
//        if (mTyoe == COURSE_GUIDE) {
//            SystemInitBody body = (SystemInitBody) App.caches.get("init");
//            if (body != null && body.courses != null && body.courses.size() > 0) {
//                final CourseTypeSample cou = body.courses.get(0);
//                courseName.setContent(cou.getName());
//                courseTime.setContent(cou.getLength() / 60 + "");
//                uploadImg = cou.getPhoto();
//                Glide.with(getContext())
//                    .load(PhotoUtils.getMiddle(cou.getPhoto()))
//                    .placeholder(R.drawable.img_default_course)
//                    .into(gymAddcourseImg);
//                //                if (Integer.parseInt(cou.getIs_private()) == Configs.TYPE_PRIVATE)
//
//            }
//            guideTitle.setVisibility(View.VISIBLE);
//            if (isPrivate) {
//                guideTitle.setText("-- 添加私教种类 -- ");
//            } else {
//                guideTitle.setText("-- 添加团课种类 -- ");
//            }
//            addGymCourseBtn.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    SystemInitBody body = (SystemInitBody) App.caches.get("init");
//                    body.courses = new ArrayList<>();
//                    CourseTypeSample course = new CourseTypeSample();
//                    //                    course.setIs_private("" + (courseTypeRg.getCheckedRadioButtonId() == R.id.course_type_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP));
//                    course.setIs_private(isPrivate);
//                    course.setLength(Integer.parseInt(courseTime.getContent()) * 60);
//                    course.setName(courseName.getContent());
//                    course.setPhoto(uploadImg);
//                    body.courses.add(course);
//                    RxBus.getBus().post(new SaveEvent());
//
//                    CourseTypeSample course1 = new CourseTypeSample();
//                    course1.is_private = isPrivate;
//                    course1.name = courseName.getContent();
//                    course1.length = Integer.parseInt(courseTime.getContent()) * 60;
//                    course1.photo = uploadImg;
//                    getFragmentManager().beginTransaction()
//                        .replace(R.id.frag, ManageCourseBatchFragment.newInstance(0, course1, null))
//                        .commit();
//                }
//            });
//        } else {
//            guideStep1.setVisibility(View.GONE);
//            if (mCourse != null) {
//                Glide.with(getContext()).load(PhotoUtils.getMiddle(mCourse.photo)).into(gymAddcourseImg);
//                courseName.setContent(mCourse.name);
//                courseTime.setContent(mCourse.length / 60 + "");
//                post.id = mCourse.id + "";
//                post.photo = mCourse.photo;
//                uploadImg = mCourse.photo;
//                post.length = mCourse.length;
//                post.is_private = mCourse.is_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP;
//                post.name = mCourse.name;
//            }
//            if (mTyoe == COURSE_EDIT) {
//
//                mCallbackActivity.setToolbar("编辑课程", false, null, R.menu.menu_del, new Toolbar.OnMenuItemClickListener() {
//                    @Override public boolean onMenuItemClick(MenuItem item) {
//                        //删除课程
//                        //                        new MaterialDialog.Builder(getContext())
//                        //                                .content("确认删除课程?")
//                        //                                .positiveText("确定")
//                        //                                .negativeText("取消")
//                        //                                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        //                                    @Override
//                        //                                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        //                                        presenter.delCourse(post.id);//删除课程
//                        //                                    }
//                        //                                })
//                        //                                .show();
//                        //
//                        return true;
//                    }
//                });
//            } else if (mTyoe == COURSE_ADD) {
//                mCallbackActivity.setToolbar("新增课程", false, null, 0, null);
//                //                if (mCourseType == Configs.TYPE_GROUP)
//                //                    courseTypeRg.check(R.id.course_type_group);
//                //                else
//                //                    courseTypeRg.check(R.id.course_type_private);
//
//            }
//            addGymCourseBtn.setText(getString(R.string.common_comfirm));
//            addGymCourseBtn.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    if (TextUtils.isEmpty(courseName.getContent())) {
//                        ToastUtils.show("请填写课程名称");
//                        return;
//                    }
//                    if (TextUtils.isEmpty(courseTime.getContent())) {
//                        ToastUtils.show("请填写课程时长");
//                        return;
//                    }
//                    //                    post.is_private = courseTypeRg.getCheckedRadioButtonId() == R.id.course_type_private ? Configs.TYPE_PRIVATE : Configs.TYPE_GROUP;
//                    post.length = Integer.parseInt(courseTime.getContent()) * 60;
//                    post.name = courseName.getContent();
//                    post.photo = uploadImg;
//                    addGymCourseBtn.setEnabled(false);
//                    if (TextUtils.isEmpty(post.id))//判断是否是编辑
//                    {
//                        presenter.addCourse(post);
//                    } else {
//                        presenter.editCourse(post.id, post);
//                    }
//                }
//            });
//        }
//    }
//
//    @OnClick(R.id.gym_addcourse_img_layout) public void onChooseImage() {
//        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
//        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
//            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
//                if (isSuccess) {
//                    showLoading();
//                    AddGuideCourseFragment.this.RxRegiste(UpYunClient.rxUpLoad("course/", filePath).subscribe(new Action1<String>() {
//                        @Override public void call(String s) {
//                            hideLoading();
//                            Glide.with(getContext()).load(PhotoUtils.getSmall(s)).into(gymAddcourseImg);
//                            post.photo = s;
//                            uploadImg = s;
//                        }
//                    }));
//                } else {
//                    ToastUtils.show("请重新选择图片");
//                }
//            }
//        });
//        choosePictureFragmentDialog.show(getFragmentManager(), "");
//    }
//
//    //    @OnClick(R.id.course_type_layout)
//    //    public void onChangeCourseType() {
//    //        if (courseTypeRg.getCheckedRadioButtonId() == R.id.course_type_private) {
//    //            courseTypeRg.check(R.id.course_type_group);
//    //        } else courseTypeRg.check(R.id.course_type_private);
//    //    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override public void onSucceed() {
//        getActivity().onBackPressed();
//    }
//
//    @Override public void onFailed(String s) {
//        addGymCourseBtn.setEnabled(true);
//        ToastUtils.show(s);
//    }
//
//    @Override public String getFragmentName() {
//        return AddGuideCourseFragment.class.getName();
//    }
//}
