package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.paper.paperbaselibrary.component.GlideCircleTransform;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CitiesChooser;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.ModifyCoachInfo;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyInfoFragment extends BaseSettingFragment {
    public static final String TAG = ModifyInfoFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int SELECT_PIC_KITKAT = 10;
    public static int SELECT_PIC = 11;
    public static int SELECT_CAM = 12;
    private static String FILE_PATH = Configs.ExternalPath + "header.png";

    @Bind(R.id.modifyinfo_header_pic)
    ImageView modifyinfoHeaderPic;
    @Bind(R.id.comple_gender)
    RadioGroup compleGender;
    Gson gson = new Gson();
    @Bind(R.id.mofifyinfo_name)
    CommonInputView mofifyinfoName;
    @Bind(R.id.comple_gender_label)
    TextView compleGenderLabel;
    @Bind(R.id.comple_gender_male)
    RadioButton compleGenderMale;
    @Bind(R.id.comple_gender_female)
    RadioButton compleGenderFemale;
    @Bind(R.id.mofifyinfo_city)
    CommonInputView mofifyinfoCity;
    @Bind(R.id.mofifyinfo_wechat)
    CommonInputView mofifyinfoWechat;
    @Bind(R.id.mofifyinfo_weibo)
    CommonInputView mofifyinfoWeibo;
    @Bind(R.id.modifyinfo_label)
    TextView modifyinfoLabel;
    @Bind(R.id.modifyinfo_sign_et)
    EditText modifyinfoSignEt;
    @Bind(R.id.modifyinfo_right_arrow)
    ImageView modifyinfoRightArrow;
    @Bind(R.id.modifyinfo_brief)
    RelativeLayout modifyinfoBrief;
    @Bind(R.id.modifyinfo_name)
    EditText modifyinfoName;
    @Bind(R.id.modifyinfo_desc)
    EditText modifyinfoDesc;
    @Bind(R.id.modifyinfo_inputpan)
    LinearLayout modifyinfoInputpan;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private QcCoachRespone.DataEntity.CoachEntity user;
    private ModifyCoachInfo mModifyCoachInfo;

    private FragmentManager mFragmentManager;
    private Coach coach;
    private CitiesChooser citiesChooser;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_info, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "修改资料");
        String coachStr = PreferenceUtils.getPrefString(getContext(), "coach", "");
        coach = gson.fromJson(coachStr, Coach.class);
        QcCloudClient.getApi().getApi.qcGetCoach(Integer.parseInt(coach.id)).subscribe(
                qcCoachRespone -> {
                    user = qcCoachRespone.getData().getCoach();
                    getActivity().runOnUiThread(this::initInfo);
                }
        );
        return view;
    }

    /**
     * 初始化个人信息
     */
    private void initInfo() {
        initHead();
        if (user.getDistrict() != null && user.getDistrict().province != null) {
            mModifyCoachInfo.setDistrict_id(user.getDistrict().id);
        }
        mofifyinfoCity.setContent(user.getDistrictStr());
        mofifyinfoName.setContent(user.getUsername());
        mofifyinfoWechat.setContent(user.getWeixin());
//        mofifyinfoWeibo.setContent(user.get);
        modifyinfoSignEt.setText(user.getShort_description());
        mModifyCoachInfo.setGender(user.getGender());
        compleGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.comple_gender_male) {
                mModifyCoachInfo.setGender(0);
            } else mModifyCoachInfo.setGender(1);
        });
    }

    public void initHead() {
        int gender = R.drawable.img_default_female;
        if (user.getGender() == 0)
            gender = R.drawable.img_default_male;
        if (TextUtils.isEmpty(user.getAvatar())) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .transform(new GlideCircleTransform(App.AppContex))
                    .into(modifyinfoHeaderPic);
        } else {
            Glide.with(App.AppContex)
                    .load(user.getAvatar())
                    .placeholder(gender)
                    .transform(new GlideCircleTransform(App.AppContex))
                    .into(modifyinfoHeaderPic);
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
        });
        citiesChooser.show(modifyinfoBrief);

    }


    @OnClick(R.id.modifyinfo_header_pic)
    public void onChangeHeader() {

        PicChooseDialog dialog = new PicChooseDialog(getActivity());
        dialog.setListener(v -> {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    // 指定开启系统相机的Action
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
                    startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
                },
                v -> {
                    //图片选择
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                    } else {
                        startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                    }
                }

        );
        dialog.show();
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
        fragmentCallBack.onFragmentChange(ModifyBrifeFragment.newInstance(user.getDescription()));
    }


    /**
     * 确认修改
     */
    @OnClick(R.id.modifyinfo_comfirm)
    public void onComfirm() {

        mModifyCoachInfo.setWeixin(mofifyinfoWechat.getContent());
        mModifyCoachInfo.setShort_description(modifyinfoSignEt.getText().toString());
        QcCloudClient.getApi().postApi.qcModifyCoach(Integer.parseInt(coach.id), mModifyCoachInfo)
                .subscribeOn(Schedulers.newThread())
                .subscribe(qcResponse -> {
                    if (qcResponse.status == ResponseResult.SUCCESS) {

                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        });

                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), qcResponse.msg, Toast.LENGTH_SHORT).show());
                    }

                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = "";
        if (resultCode == -1) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY || requestCode == SELECT_PIC_KITKAT)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            else filepath = FILE_PATH;
            LogUtil.d(filepath);
            Observable.just(filepath)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        File upFile = new File(s);
                        boolean reslut = UpYunClient.upLoadImg("/header/", coach.id, upFile);
                        if (reslut) {
                            LogUtil.d("success");
                            getActivity().runOnUiThread(() -> Glide.with(App.AppContex).load(Uri.fromFile(upFile))
                                    .transform(new GlideCircleTransform(App.AppContex))
                                    .into(modifyinfoHeaderPic));
                            mModifyCoachInfo.setAvatar(UpYunClient.UPYUNPATH + "header/" + coach.id + ".png");


                        } else {
                            //upload failed TODO
                            LogUtil.d("update img false");
                        }
                    });

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
