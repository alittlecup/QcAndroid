package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.AddCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseFrament extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gym_addcourse_img)
    ImageView gymAddcourseImg;
    @Bind(R.id.gym_addcourse_img_layout)
    RelativeLayout gymAddcourseImgLayout;
    @Bind(R.id.course_type_private)
    RadioButton courseTypePrivate;
    @Bind(R.id.course_type_group)
    RadioButton courseTypeGroup;
    @Bind(R.id.course_type_rg)
    RadioGroup courseTypeRg;
    @Bind(R.id.course_name)
    CommonInputView courseName;
    @Bind(R.id.course_time)
    CommonInputView courseTime;
    @Bind(R.id.gym_course_detail_layout)
    LinearLayout gymCourseDetailLayout;
    @Bind(R.id.add_gym_course_btn)
    Button addGymCourseBtn;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    public static final int TYPE_DEL = 3;
    private int mType;
    private String mModel;
    private int mId;
    private int mCourseId;

    private Subscription addSp;//新增课程
    private Subscription upPic;//新增课程
    private String upImg;
    private String upName;
    private int upTime;
    private Boolean upIsPrivate = true;
    private MaterialDialog delDialog;

    public AddCourseFrament() {
    }

    public static AddCourseFrament newInstance(int type,String model,int id) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putString("model", model);

        AddCourseFrament fragment = new AddCourseFrament();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddCourseFrament newInstance(int type,String model,int id,int course_id,String name,String pic,int time,boolean IsPrivate) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putInt("time", time);
        args.putString("model", model);
        args.putString("name", name);
        args.putString("pic", pic);
        args.putBoolean("isP", IsPrivate);
        args.putInt("course_id",course_id);
        AddCourseFrament fragment = new AddCourseFrament();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mType = getArguments().getInt("type");
            mId = getArguments().getInt("id");
            mModel = getArguments().getString("model");
            upImg = getArguments().getString("pic");
            upIsPrivate = getArguments().getBoolean("isP");
            upName = getArguments().getString("name");
            upTime = getArguments().getInt("time", -1);
            mCourseId = getArguments().getInt("course_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course_frament, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (mType == TYPE_ADD){
            toolbar.setTitle("新增课程");
        }else if(mType == TYPE_EDIT){
            toolbar.setTitle("编辑课程");
            toolbar.inflateMenu(R.menu.menu_delete);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //删除
                    delCourse();
                    return true;
                }
            });
            Glide.with(App.AppContex).load(upImg).into(gymAddcourseImg);
            courseName.setContent(upName);
            courseTime.setContent(upTime+"");
            if (upIsPrivate){
                courseTypeRg.check(R.id.course_type_private);
            }else courseTypeRg.check(R.id.course_type_group);
        }

        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.course_type_private) {
                    upIsPrivate = true;
                } else upIsPrivate = false;
            }
        });
        return view;
    }

    @OnClick(R.id.add_gym_course_btn)
    public void onAddCourse(){
        if (TextUtils.isEmpty(courseName.getContent())){
            ToastUtils.showDefaultStyle("请填写课程名称");
            return;
        }
        if (TextUtils.isEmpty(courseTime.getContent())){
            ToastUtils.showDefaultStyle("请填写课程时长");
            return;
        }
        addGymCourseBtn.setEnabled(false);
        if (mType == TYPE_ADD) {
            addSp = QcCloudClient.getApi().postApi.qcAddCourse(App.coachid, new AddCourse(mId, mModel, upName, upImg, upTime, upIsPrivate))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<QcResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            addGymCourseBtn.setEnabled(true);
                        }

                        @Override
                        public void onNext(QcResponse qcResponse) {
                            addGymCourseBtn.setEnabled(true);
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                //新增成功
                                getActivity().onBackPressed();
                                ToastUtils.show("新增成功");
                            }
                        }
                    });
        }else if (mType == TYPE_EDIT){
            addSp = QcCloudClient.getApi().postApi.qcEditCourse(App.coachid, new AddCourse(mId, mModel, upName, upImg, upTime, upIsPrivate))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<QcResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            addGymCourseBtn.setEnabled(true);
                        }

                        @Override
                        public void onNext(QcResponse qcResponse) {
                            addGymCourseBtn.setEnabled(true);
                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                //新增成功
                                getActivity().onBackPressed();
                                ToastUtils.show("修改成功");
                            }
                        }
                    });
        }
    }



    /**
     * 新增图片
     */
    @OnClick(R.id.gym_addcourse_img_layout)
    public void addPhoto(){
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.show(getFragmentManager(), "");
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override
            public void onChoosePicResult(boolean isSuccess, String filePath) {
                dialog.dismiss();
                if (isSuccess) {
                    upPic = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            upImg = UpYunClient.upLoadImg("/course/", new File(filePath));
                            subscriber.onNext(upImg);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(String upImg) {
                                    if (TextUtils.isEmpty(upImg)) {
                                        ToastUtils.showDefaultStyle("图片上传失败");
                                    } else {
                                        Glide.with(App.AppContex).load(new File(filePath)).into(gymAddcourseImg);
                                    }
                                }
                            });


                } else {
                    LogUtil.e("选择图片失败");
                }
            }
        });
    }

    public HashMap<String,String> getParams(){
        HashMap<String,String> params =new HashMap<>();
        params.put("model",mModel);
        params.put("id",mId+"");
        params.put("name",upName);
        params.put("photo",upImg);
        params.put("length", upTime + "");
        params.put("is_private", upIsPrivate + "");

        return params;
    }

    @Override
    public void onDestroyView() {
        if (addSp != null )
            addSp.unsubscribe();
        if (upPic != null)
            upPic.unsubscribe();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 删除课程
     */
    private void delCourse() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("是否删除课程?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            HashMap<String,String> params = new HashMap<String, String>();
                            params.put("id",mId+"");
                            params.put("course_id",mCourseId+"");
                            params.put("model",mModel);

                            QcCloudClient.getApi().postApi.qcDelCourse(App.coachid, params)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<QcResponse>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(QcResponse qcResponse) {
                                            if (qcResponse.status == ResponseResult.SUCCESS) {
                                                ToastUtils.show("删除成功");
                                                getActivity().onBackPressed();
                                            } else {

                                            }
                                        }
                                    });


                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }



}
