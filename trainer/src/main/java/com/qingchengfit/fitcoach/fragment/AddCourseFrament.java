package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.component.LoadingDialog;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.AddCoourseResponse;
import com.qingchengfit.fitcoach.http.bean.AddCourse;
import com.qingchengfit.fitcoach.http.bean.QcOneCourseResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.HashMap;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseFrament extends Fragment {

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    public static final int TYPE_DEL = 3;
	Toolbar toolbar;
	ImageView gymAddcourseImg;
	RelativeLayout gymAddcourseImgLayout;
	RadioButton courseTypePrivate;
	RadioButton courseTypeGroup;
	RadioGroup courseTypeRg;
	CommonInputView courseName;
	CommonInputView courseTime;
	LinearLayout gymCourseDetailLayout;
	Button addGymCourseBtn;
	RelativeLayout courseTypeLayout;
	CommonInputView courseCapacity;
    private int mType;
    private String mModel;
    private int mId;
    private String mCourseId;
    private String mUpCapacity;

    private Subscription addSp;//新增课程
    private Subscription upPic;//新增课程
    private String upImg;
    private String upName;
    private int upTime;
    private Boolean upIsPrivate = true;
    private MaterialDialog delDialog;
    private LoadingDialog loadingDialog;


    public AddCourseFrament() {
    }

    /**
     * @param type 1是新增 2是编辑
     */
    public static AddCourseFrament newInstance(int type, String model, int id, boolean isPrivate) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putString("model", model);
        args.putBoolean("isP", isPrivate);
        AddCourseFrament fragment = new AddCourseFrament();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddCourseFrament newInstance(int type, String model, int id, String course_id) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putString("model", model);
        args.putString("course_id", course_id);
        AddCourseFrament fragment = new AddCourseFrament();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            mId = getArguments().getInt("id");
            mModel = getArguments().getString("model");
            mCourseId = getArguments().getString("course_id");
            upIsPrivate = getArguments().getBoolean("isP");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course_frament, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        gymAddcourseImg = (ImageView) view.findViewById(R.id.gym_addcourse_img);
        gymAddcourseImgLayout = (RelativeLayout) view.findViewById(R.id.gym_addcourse_img_layout);
        courseTypePrivate = (RadioButton) view.findViewById(R.id.course_type_private);
        courseTypeGroup = (RadioButton) view.findViewById(R.id.course_type_group);
        courseTypeRg = (RadioGroup) view.findViewById(R.id.course_type_rg);
        courseName = (CommonInputView) view.findViewById(R.id.course_name);
        courseTime = (CommonInputView) view.findViewById(R.id.course_time);
        gymCourseDetailLayout = (LinearLayout) view.findViewById(R.id.gym_course_detail_layout);
        addGymCourseBtn = (Button) view.findViewById(R.id.add_gym_course_btn);
        courseTypeLayout = (RelativeLayout) view.findViewById(R.id.course_type_layout);
        courseCapacity = (CommonInputView) view.findViewById(R.id.course_capacity);
        view.findViewById(R.id.add_gym_course_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAddCourse();
            }
        });
        view.findViewById(R.id.gym_addcourse_img_layout)
            .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    addPhoto();
                }
            });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (mType == TYPE_ADD) {
            toolbar.setTitle("新增课程");
            courseTypeLayout.setVisibility(View.GONE);
            if (upIsPrivate) {
                courseCapacity.setVisibility(View.GONE);
            } else {
                courseCapacity.setVisibility(View.VISIBLE);
            }
        } else if (mType == TYPE_EDIT) {
            toolbar.setTitle("编辑课程");
            //            toolbar.inflateMenu(R.menu.menu_delete);
            //            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item) {
            //                    删除
            //                    delCourse();
            //                    return true;
            //                }
            //            });
            HashMap<String, String> params = new HashMap<>();
            params.put("model", mModel);
            params.put("id", mId + "");
            TrainerRepository.getStaticTrainerAllApi().qcGetOneCourse(App.coachid, mCourseId, params)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcOneCourseResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcOneCourseResponse qcOneCourseResponse) {
                        if (qcOneCourseResponse.status == ResponseResult.SUCCESS) {
                            Glide.with(App.AppContex)
                                .load(PhotoUtils.getSmall(qcOneCourseResponse.data.course.photo))
                                .into(gymAddcourseImg);
                            courseName.setContent(qcOneCourseResponse.data.course.name);
                            courseTime.setContent(qcOneCourseResponse.data.course.length / 60 + "");
                            upIsPrivate = qcOneCourseResponse.data.course.is_private;
                            courseCapacity.setContent(qcOneCourseResponse.data.course.capacity);

                            if (upIsPrivate) {
                                courseCapacity.setVisibility(View.GONE);
                            } else {
                                courseCapacity.setVisibility(View.VISIBLE);
                            }
                            //                                if (upIsPrivate) {
                            //                                    courseTypeRg.check(R.id.course_type_private);
                            //                                } else courseTypeRg.check(R.id.course_type_group);
                        }
                    }
                });
            //            Glide.with(App.AppContex).load(upImg).into(gymAddcourseImg);
            //            courseName.setContent(upName);
            //            courseTime.setContent(upTime + "");
            //            courseTypeLayout.setVisibility(View.VISIBLE);
            //            if (upIsPrivate) {
            //                courseTypeRg.check(R.id.course_type_private);
            //            } else courseTypeRg.check(R.id.course_type_group);
        }

        //        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        //            @Override
        //            public void onCheckedChanged(RadioGroup group, int checkedId) {
        //                if (checkedId == R.id.course_type_private) {
        //                    upIsPrivate = true;
        //                } else upIsPrivate = false;
        //            }
        //        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

 public void onAddCourse() {
        if (TextUtils.isEmpty(courseName.getContent())) {
            ToastUtils.showDefaultStyle("请填写课程名称");
            return;
        }
        if (TextUtils.isEmpty(courseTime.getContent())) {
            ToastUtils.showDefaultStyle("请填写课程时长");
            return;
        }
        if (!upIsPrivate && TextUtils.isEmpty(courseCapacity.getContent())) {
            ToastUtils.showDefaultStyle("请填写可容纳人数");
            return;
        }
        mUpCapacity = courseCapacity.getContent();
        upTime = Integer.parseInt(courseTime.getContent()) * 60;
        upName = courseName.getContent().trim();
        addGymCourseBtn.setEnabled(false);
        if (mType == TYPE_ADD) {
            addSp = TrainerRepository.getStaticTrainerAllApi().qcAddCourse(App.coachid,
                new AddCourse(mId, mModel, upName, upImg, upTime, upIsPrivate, upIsPrivate ? null : mUpCapacity))
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddCoourseResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        addGymCourseBtn.setEnabled(true);
                    }

                    @Override public void onNext(AddCoourseResponse qcResponse) {
                        addGymCourseBtn.setEnabled(true);
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            //新增成功
                            getActivity().onBackPressed();
                            ImageThreeTextBean bean = new ImageThreeTextBean(qcResponse.data.course.photo, qcResponse.data.course.name,
                                "时长:" + qcResponse.data.course.length / 60 + "分钟", "累计0节,服务0人次");
                            bean.tags.put(ImageThreeTextBean.TAG_COURSE, qcResponse.data.course.id);
                            bean.tags.put(ImageThreeTextBean.TAG_COURSETYPE,
                                upIsPrivate ? Configs.TYPE_PRIVATE + "" : Configs.TYPE_GROUP + "");
                            bean.tags.put(ImageThreeTextBean.TAG_LENGTH, qcResponse.data.course.length + "");
                            bean.tags.put("isNewAdd", "1");
                            RxBus.getBus().post(bean);
                            RxBus.getBus().post(RxBus.BUS_REFRESH);
                            ToastUtils.show("新增成功");
                        }
                    }
                });
        } else if (mType == TYPE_EDIT) {
            AddCourse addCourse = new AddCourse(mId, mModel, upName, upImg, upTime, upIsPrivate, upIsPrivate ? null : mUpCapacity);
            addCourse.course_id = mCourseId;
            addSp = TrainerRepository.getStaticTrainerAllApi().qcEditCourse(App.coachid, addCourse)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        addGymCourseBtn.setEnabled(true);
                    }

                    @Override public void onNext(QcResponse qcResponse) {
                        addGymCourseBtn.setEnabled(true);
                        if (qcResponse.status == ResponseResult.SUCCESS) {

                            //新增成功
                            getActivity().onBackPressed();
                            RxBus.getBus().post(RxBus.BUS_REFRESH);
                            ToastUtils.show("修改成功");
                        }
                    }
                });
        }
    }

    /**
     * 新增图片
     */
 public void addPhoto() {
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.show(getFragmentManager(), "");
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                dialog.dismiss();
                if (isSuccess) {
                    ShowLoading("正在上传,请稍后...");
                    upPic = UpYunClient.rxUpLoad("course/", filePath)

                        //upPic = Observable.create(new Observable.OnSubscribe<String>() {
                        //    @Override
                        //    public void call(Subscriber<? super String> subscriber) {
                        //        upImg = UpYunClient.upLoadImg("course/", new File(filePath));
                        //        subscriber.onNext(upImg);
                        //    }
                        //})
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {
                                hideLoading();
                            }

                            @Override public void onNext(String upImg) {
                                if (TextUtils.isEmpty(upImg)) {
                                    ToastUtils.showDefaultStyle("图片上传失败");
                                } else {
                                    Glide.with(App.AppContex).load(PhotoUtils.getSmall(upImg)).into(gymAddcourseImg);
                                }
                                hideLoading();
                            }
                        });
                } else {
                    LogUtil.e("选择图片失败");
                }
            }
        });
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mId + "");
        params.put("name", upName);
        params.put("photo", upImg);
        params.put("length", upTime + "");
        params.put("is_private", upIsPrivate + "");

        return params;
    }

    @Override public void onDestroyView() {
        if (addSp != null) addSp.unsubscribe();
        if (upPic != null) upPic.unsubscribe();
        super.onDestroyView();

    }

    /**
     * 删除课程
     */
    //private void delCourse() {
    //    if (delDialog == null) {
    //        delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
    //            .content("是否删除课程?")
    //            .positiveText("确定")
    //            .negativeText("取消")
    //            .callback(new MaterialDialog.ButtonCallback() {
    //                @Override public void onPositive(MaterialDialog dialog) {
    //                    super.onPositive(dialog);
    //                    dialog.dismiss();
    //                    HashMap<String, String> params = new HashMap<String, String>();
    //                    params.put("id", mId + "");
    //                    params.put("course_id", mCourseId + "");
    //                    params.put("model", mModel);
    //
    //                    //                            TrainerRepository.getStaticTrainerAllApi().qcDelCourse(App.coachid, params)
    //                    //                                    .observeOn(AndroidSchedulers.mainThread())
    //                  //                                    .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                    //                                    .subscribe(new Subscriber<QcResponse>() {
    //                    //                                        @Override
    //                    //                                        public void onCompleted() {
    //                    //
    //                    //                                        }
    //                    //
    //                    //                                        @Override
    //                    //                                        public void onError(Throwable e) {
    //                    //
    //                    //                                        }
    //                    //
    //                    //                                        @Override
    //                    //                                        public void onNext(QcResponse qcResponse) {
    //                    //                                            if (qcResponse.status == ResponseResult.SUCCESS) {
    //                    //                                                ToastUtils.show("删除成功");
    //                    //                                                getActivity().onBackPressed();
    //                    //                                                getActivity().onBackPressed();
    //                    //                                                RxBus.getBus().post(RxBus.BUS_REFRESH);
    //                    //                                            } else {
    //                    //
    //                    //                                            }
    //                    //                                        }
    //                    //                                    });
    //                    //
    //                    //
    //                }
    //
    //                @Override public void onNegative(MaterialDialog dialog) {
    //                    super.onNegative(dialog);
    //                    dialog.dismiss();
    //                }
    //            })
    //            .cancelable(false)
    //            .build();
    //    }
    //    delDialog.show();
    //}

    public void ShowLoading(String content) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext(),content);
        }
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }
}
