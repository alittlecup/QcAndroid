package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.RevenUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CitiesChooser;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.ModifyCoachInfo;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.service.UpyunService;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyInfoFragment extends BaseSettingFragment implements ChoosePictureFragmentDialog.ChoosePicResult{
    public static final String TAG = ModifyInfoFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int SELECT_PIC_KITKAT = 10;
    public static int SELECT_PIC = 11;
    public static int SELECT_CAM = 12;
    private static String FILE_PATH = Configs.ExternalPath + "header.png";

    @BindView(R.id.modifyinfo_header_pic)
    ImageView modifyinfoHeaderPic;
    @BindView(R.id.comple_gender)
    RadioGroup compleGender;
    Gson gson = new Gson();
    @BindView(R.id.mofifyinfo_name)
    CommonInputView mofifyinfoName;
    @BindView(R.id.comple_gender_label)
    TextView compleGenderLabel;
    @BindView(R.id.comple_gender_male)
    RadioButton compleGenderMale;
    @BindView(R.id.comple_gender_female)
    RadioButton compleGenderFemale;
    @BindView(R.id.mofifyinfo_city)
    CommonInputView mofifyinfoCity;
    @BindView(R.id.mofifyinfo_wechat)
    CommonInputView mofifyinfoWechat;
    @BindView(R.id.mofifyinfo_weibo)
    CommonInputView mofifyinfoWeibo;
    @BindView(R.id.modifyinfo_label)
    TextView modifyinfoLabel;
    @BindView(R.id.modifyinfo_sign_et)
    EditText modifyinfoSignEt;
    @BindView(R.id.modifyinfo_right_arrow)
    ImageView modifyinfoRightArrow;
    @BindView(R.id.modifyinfo_brief)
    RelativeLayout modifyinfoBrief;
    @BindView(R.id.modifyinfo_name)
    EditText modifyinfoName;
    @BindView(R.id.modifyinfo_desc)
    EditText modifyinfoDesc;
    @BindView(R.id.modifyinfo_inputpan)
    LinearLayout modifyinfoInputpan;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private QcCoachRespone.DataEntity.CoachEntity user;
    private ModifyCoachInfo mModifyCoachInfo;

    private FragmentManager mFragmentManager;
    private Coach coach;
    private CitiesChooser citiesChooser;
    private Bundle saveState;
    private boolean isLoading = false;
    private Uri mAvatarResult;
    private Unbinder unbinder;

    public ModifyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyInfoFragment newInstance(String param1, String param2) {
        ModifyInfoFragment fragment = new ModifyInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Observable<UpyunService.UpYunResult> uppicObserver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFragmentManager = getChildFragmentManager();
        citiesChooser = new CitiesChooser(getContext());
        mModifyCoachInfo = new ModifyCoachInfo();
        isLoading = true;
        uppicObserver = RxBus.getBus().register(UpyunService.UpYunResult.class.getName());
        uppicObserver.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UpyunService.UpYunResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UpyunService.UpYunResult upYunResult) {
                mModifyCoachInfo.setAvatar(upYunResult.getUrl());
                user.setAvatar(upYunResult.getUrl());
//                Glide.with(App.AppContex)
//                        .load(upYunResult.getUrl())
//                        .asBitmap()
//                        .into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_info, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "基本信息设置");
        String coachStr = PreferenceUtils.getPrefString(getContext(), "coach", "");
        coach = gson.fromJson(coachStr, Coach.class);
        refresh.setColorSchemeResources(R.color.primary);
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                refresh.setRefreshing(true);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData();
            }
        });
        queryData();
        return view;
    }

    public void queryData() {

        QcCloudClient.getApi().getApi.qcGetCoach(Integer.parseInt(coach.id))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcCoachRespone>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(QcCoachRespone qcCoachRespone) {
                        if (modifyinfoDesc != null) {
                            user = qcCoachRespone.getData().getCoach();
                            initInfo();
                            refresh.setRefreshing(false);
                        }
                    }

                });
    }


    /**
     * 初始化个人信息
     */
    private void initInfo() {
//        if (!restoreStateFromArguments()) {
        initHead(user.getAvatar());
        if (user.getDistrict() != null && user.getDistrict().province != null) {
            mModifyCoachInfo.setDistrict_id(user.getDistrict().id);
        }
        mofifyinfoCity.setContent(user.getDistrictStr());
        mofifyinfoName.setContent(user.getUsername());
        mofifyinfoWechat.setContent(user.getWeixin());
//        mofifyinfoWeibo.setContent(user.get);
        modifyinfoSignEt.setText(user.getShort_description());
        mModifyCoachInfo.setGender(user.getGender());
        if (user.getGender() == 0) {
            compleGender.check(R.id.comple_gender_male);
        } else {
            compleGender.check(R.id.comple_gender_female);
        }
        compleGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.comple_gender_male) {
                mModifyCoachInfo.setGender(0);
            } else mModifyCoachInfo.setGender(1);
        });
//        }
    }

    public void initHead(String headurl) {
        int gender = R.drawable.img_default_female;
        if (user.getGender() == 0)
            gender = R.drawable.img_default_male;
        if (TextUtils.isEmpty(headurl)) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .asBitmap()
                    .into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
        } else {
            Glide.with(App.AppContex)
                    .load(PhotoUtils.getSmall(headurl))
                    .asBitmap()
                    .into(new CircleImgWrapper(modifyinfoHeaderPic, App.AppContex));
        }

    }

    /**
     * 修改城市
     */
    @OnClick(R.id.mofifyinfo_city)
    public void onClickCity() {
        citiesChooser.setOnCityChoosenListener((provice, city, district, id) -> {
            mofifyinfoCity.setContent(provice + city);
            mModifyCoachInfo.setDistrict_id(Integer.toString(id));
            citiesChooser.hide();
        });
        citiesChooser.show(modifyinfoBrief);

    }

    @OnClick(R.id.modifyinfo_sign_layout)
    public void onClickSign() {
        modifyinfoSignEt.requestFocus();
    }


    @OnClick(R.id.modifyinfo_header_layout)
    public void onChangeHeader() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = new ChoosePictureFragmentDialog();
        choosePictureFragmentDialog.setResult(this);
        choosePictureFragmentDialog.show(getFragmentManager(), "choose pic");
//        PicChooseDialog dialog = new PicChooseDialog(getActivity());
//        dialog.setListener(v -> {
//                    dialog.dismiss();
//                    Intent intent = new Intent();
//                    // 指定开启系统相机的Action
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
//                    startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
//                },
//                v -> {
//                    //图片选择
//                    dialog.dismiss();
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/jpeg");
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
//                    } else {
//                        startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
//                    }
//                }
//
//        );
//        dialog.show();
    }

    public void goGalley() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent, SELECT_PIC);
        }
    }

    public void goCamera() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//         根据文件地址创建文件
        File file = new File(FILE_PATH);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                //e.printStackTrace();
                RevenUtils.sendException("goCamera", TAG, e);
            }
//         把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
//         设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, SELECT_CAM);
    }

    /**
     * 修改简介
     */
    @OnClick(R.id.modifyinfo_brief)
    public void onClickBrief() {
        Fragment fragment = ModifyBrifeFragment.newInstance(user.getDescription());
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.settting_fraglayout, fragment)
                .show(fragment).addToBackStack("").commit();
//        fragmentCallBack.onFragmentChange(ModifyBrifeFragment.newInstance(user.getDescription()));
    }


    /**
     * 确认修改
     */
    @OnClick(R.id.modifyinfo_comfirm)
    public void onComfirm() {

        mModifyCoachInfo.setWeixin(mofifyinfoWechat.getContent());
        mModifyCoachInfo.setShort_description(modifyinfoSignEt.getText().toString());
        mModifyCoachInfo.setUsername(mofifyinfoName.getContent().trim());
        fragmentCallBack.ShowLoading("请稍后");

        QcCloudClient.getApi().postApi.qcModifyCoach(Integer.parseInt(coach.id), mModifyCoachInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                        fragmentCallBack.hideLoading();
                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        fragmentCallBack.hideLoading();
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            fragmentCallBack.fixCount();

//                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            ToastUtils.show("修改成功");
                            getActivity().onBackPressed();


                        } else {
                            Toast.makeText(getContext(), qcResponse.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String filepath = "";
//        if (mModifyCoachInfo == null)
//            return;
//        if (resultCode == Activity.RESULT_OK) {
//
////            if (requestCode == ChoosePicUtils.CHOOSE_CAMERA || requestCode == ChoosePicUtils.CHOOSE_GALLERY ) {
////                Intent intent = new Intent("com.android.camera.action.CROP");
////                intent.setType("image/*");
////
////                intent.setData(data.getData());
////                intent.putExtra("outputX", 300);
////                intent.putExtra("outputY", 300);
////                intent.putExtra("aspectX", 1);
////                intent.putExtra("aspectY", 1);
////                intent.putExtra("scale", false);
////                intent.putExtra("return-data", true);
////                intent.putExtra("return-data", false);
////
////                intent.putExtra("output", );
////            }
//
//
//            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY || requestCode == SELECT_PIC_KITKAT)
//                filepath = FileUtils.getPath(getActivity(), data.getData());
//            else filepath = Configs.CameraPic;
//            LogUtil.d(filepath);
//            fragmentCallBack.ShowLoading("正在上传");
//            Observable.just(filepath)
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(s -> {
//                        String filename = UUID.randomUUID().toString();
//                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
//                        File upFile = new File(Configs.ExternalCache + filename);
//
//                        boolean reslut = UpYunClient.upLoadImg("/header/" + coach.id + "/", filename, upFile);
//                        getActivity().runOnUiThread(() -> {
//                            fragmentCallBack.hideLoading();
//                            if (reslut) {
//                                LogUtil.d("success");
//                                String pppurl = UpYunClient.UPYUNPATH + "header/" + coach.id + "/" + filename + ".png";
//                                Glide.with(App.AppContex).load(pppurl)
//                                        .transform(new GlideCircleTransform(App.AppContex))
//                                        .into(modifyinfoHeaderPic);
//                                mModifyCoachInfo.setAvatar(pppurl);
//                                user.setAvatar(pppurl);
//
//                            } else {
//                                //upload failed TODO
//                                LogUtil.d("update img false");
//                            }
//                        });
//
//                    });
//
//        }
//
//
//    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        Bundle state = new Bundle();
//        state.putString("city",mofifyinfoCity.getContent());
//        state.putString("desc",modifyinfoDesc.getText().toString());
//        state.putString("username",mofifyinfoName.getContent());
//        state.putString("wechat", mofifyinfoWechat.getContent());
//        outState.putBundle(this.getClass().getName(),state);
//        super.onSaveInstanceState(outState);
//    }
//
//    private boolean restoreStateFromArguments() {
//        Bundle b = getArguments();
//        saveState = b.getBundle(this.getClass().getName());
//        if (saveState != null) {
//            restoreSave();
//            return true;
//        } else return false;
//    }
//
//    @UiThread
//    private void restoreSave() {
//        if (saveState != null) {
//
//            mofifyinfoCity.setContent(saveState.getString("city"));
//            mofifyinfoWechat.setContent(saveState.getString("wechat"));
//            mofifyinfoName.setContent(saveState.getString("username"));
//            modifyinfoDesc.setText(saveState.getString("desc"));
//            initHead(saveState.getString("avatar"));
//        }
//    }


    @Override
    public void onDestroyView() {
//        Bundle state = new Bundle();
//        state.putString("city", mofifyinfoCity.getContent());
//        state.putString("desc", modifyinfoDesc.getText().toString());
//        state.putString("username", mofifyinfoName.getContent());
//        state.putString("wechat", mofifyinfoWechat.getContent());
//        state.putString("avatar", user.getAvatar());
//        getArguments().putBundle(this.getClass().getName(), state);
        RxBus.getBus().unregister(UpyunService.UpYunResult.class.getName(),uppicObserver);
        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onChoosePicResult(boolean isSuccess, String filePath) {
        if (isSuccess) {
            fragmentCallBack.ShowLoading("正在上传");
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    String upImg = UpYunClient.upLoadImg("course/", new File(filePath));
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
                            fragmentCallBack.hideLoading();
                        }

                        @Override
                        public void onNext(String upImg) {
                            upImg = upImg+"!120x120";
                            if (TextUtils.isEmpty(upImg)) {
                                ToastUtils.showDefaultStyle("图片上传失败");
                            } else {
                                mModifyCoachInfo.setAvatar(upImg);
                                Glide.with(App.AppContex).load(PhotoUtils.getSmall(upImg)).asBitmap().into(new CircleImgWrapper(modifyinfoHeaderPic,App.AppContex));
                            }
                            fragmentCallBack.hideLoading();
                        }
                    });


        } else {
            LogUtil.e("选择图片失败");
        }


    }


}
