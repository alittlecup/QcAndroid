package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.AddCertificate;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.BitmapUtils;
import cn.qingchengfit.widgets.utils.ChoosePicUtils;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.FileUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecordEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordEditFragment extends BaseSettingFragment {
    public static final String TAG = RecordEditFragment.class.getName();
    public static final int TYPE_MEETING = 1;
    public static final int TYPE_COMFIRM = 2;
    public static final int TYPE_COMPETITION = 3;
    private static final String TITLE = "param1";
    private static final String CONTENT = "param2";
    private static final String TYPE = "param_type";

    private static String FILE_PATH = Configs.CameraPic;
    //    @BindView(R.id.recordedit_host)
//    CommonInputView recordeditHost;
//    @BindView(R.id.recordedit_type_meeting)
//    RadioButton recordeditTypeMeeting;
//    @BindView(R.id.recordedit_type_comfirm)
//    RadioButton recordeditTypeComfirm;
//    @BindView(R.id.recordedit_type_competition)
//    RadioButton recordeditTypeCompetition;
//    @BindView(R.id.recordedit_type)
//    RadioGroup recordeditType;
//    @BindView(R.id.recordedit_date)
//    CommonInputView recordeditDate;
    @BindView(R.id.recordedit_score)
    CommonInputView recordeditScore;
    @BindView(R.id.recordedit_dateoff)
    CommonInputView recordeditDateoff;
    @BindView(R.id.recordedit_upimg)
    TextView recordeditUpimg;
    @BindView(R.id.recordedit_img)
    ImageView recordeditImg;
    @BindView(R.id.recordedit_comfirm_btn)
    Button recordeditComfirmBtn;
    TimeDialogWindow pwTime;
    @BindView(R.id.rootview)
    ScrollView rootview;
    @BindView(R.id.record_edit_name)
    CommonInputView recordEditName;
    @BindView(R.id.recordedit_datestart)
    CommonInputView recordeditDatestart;
    @BindView(R.id.host_img)
    ImageView hostImg;
    @BindView(R.id.host_qc_identify)
    ImageView hostQcIdentify;
    @BindView(R.id.host_name)
    TextView hostName;
    @BindView(R.id.host_address)
    TextView hostAddress;
    @BindView(R.id.comfirm_scroe_layout)
    RelativeLayout comfirmScroeLayout;
    @BindView(R.id.comfirm_has_certification)
    TextView comfirmHasCertification;
    @BindView(R.id.comfirm_certification_layout)
    RelativeLayout comfirmCertificationLayout;
    @BindView(R.id.recordedit_certificat_name)
    CommonInputView recordeditCertificatName;
    @BindView(R.id.host_layout)
    RelativeLayout hostLayout;
    @BindView(R.id.recordedit_date)
    CommonInputView recordeditDate;
    @BindView(R.id.comfirm_scroe_switch)
    Switch comfirmScroeSwitch;
    @BindView(R.id.comfirm_certification_switch)
    Switch comfirmCertificationSwitch;
    @BindView(R.id.recordedit_upimg_layout)
    RelativeLayout recordeditUpimgLayout;
//    @BindView(R.id.divider_margin)
//    View divder;
//    @BindView(R.id.divider)
//    View divder2;


    private boolean mTitle;
    private boolean mIsHidenImg = false;
    private String mContent;
    private int mType;
    private Gson gson = new Gson();
    private QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity;
    private AddCertificate addCertificate;
    private MaterialDialog delDialog;
    private DialogSheet mDialogSheet;
    private Unbinder unbinder;

    public RecordEditFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 是否为编辑
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordEditFragment.
     */
    public static RecordEditFragment newInstance(boolean param1, String param2, int type) {
        RecordEditFragment fragment = new RecordEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(TITLE, param1);
        args.putString(CONTENT, param2);
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("删除此条资质?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            fragmentCallBack.ShowLoading("请稍后");
                            QcCloudClient.getApi().postApi.qcDelCertificate(certificatesEntity.getId()).subscribeOn(Schedulers.newThread()).subscribe(qcResponse -> onResult(qcResponse), throwable -> {
                            }, () -> {
                            });
                            dialog.dismiss();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getBoolean(TITLE);
            mContent = getArguments().getString(CONTENT);
            mType = getArguments().getInt(TYPE);
        }
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record_edit, container, false);
        unbinder=ButterKnife.bind(this, view);
        if (mType == 0)
            mType = 1;
        switch (mType) {
            case TYPE_MEETING:
                fragmentCallBack.onToolbarMenu(0, 0, mTitle ? "编辑认证信息" : "添加大会认证");
                recordEditName.setLabel("大会名称");
                recordeditDate.setLabel("大会日期");
                recordeditUpimg.setText("上传参会凭证");
                comfirmCertificationLayout.setVisibility(View.GONE);
                recordeditDatestart.setVisibility(View.GONE);
                recordeditDateoff.setVisibility(View.GONE);
                recordeditCertificatName.setVisibility(View.GONE);
                recordeditUpimgLayout.setVisibility(View.VISIBLE);
                break;

            case TYPE_COMFIRM:
                fragmentCallBack.onToolbarMenu(0, 0, mTitle ? "编辑认证信息" : "添加培训认证");
                recordEditName.setLabel("培训名称");
                recordeditDate.setLabel("培训日期");
                comfirmHasCertification.setText("有无证书");
                recordeditCertificatName.setLabel("证书名称");
                recordeditDatestart.setLabel("证书生效日期");
                recordeditDateoff.setLabel("证书失效日期");
                recordeditUpimg.setText("上传证书/图片");

                mIsHidenImg = true;
                break;
            case TYPE_COMPETITION:
                fragmentCallBack.onToolbarMenu(0, 0, mTitle ? "编辑认证信息" : "添加赛事认证");
                recordEditName.setLabel("赛事名称");
                recordeditDate.setLabel("赛事日期");
                comfirmHasCertification.setText("有无奖项");
                recordeditCertificatName.setLabel("奖项名称");
                recordeditDatestart.setLabel("奖项生效日期");
                recordeditDateoff.setLabel("奖项失效日期");
                recordeditUpimg.setText("上传奖项/图片");

                mIsHidenImg = true;
                break;

        }

//        fragmentCallBack.onToolbarMenu(mTitle ? R.menu.menu_delete : 0, 0, mTitle ? "编辑认证信息" : "添加认证");
        fragmentCallBack.onToolbarClickListener(item1 -> {
            if (certificatesEntity != null) {
                showDialog();
            }
            return true;
        });
        if (addCertificate == null)
            addCertificate = new AddCertificate(App.coachid);
        if (mContent != null) {
            certificatesEntity = gson.fromJson(mContent, QcCertificatesReponse.DataEntity.CertificatesEntity.class);
//            recordeditHost.setContent(certificatesEntity.getOrganization().getName());
            hostName.setText(certificatesEntity.getOrganization().getName());
            hostAddress.setText("联系方式:" + certificatesEntity.getOrganization().getContact());
            if (certificatesEntity.getIs_authenticated())
                hostQcIdentify.setVisibility(View.VISIBLE);
            else hostQcIdentify.setVisibility(View.GONE);
            Glide.with(App.AppContex).load(certificatesEntity.getOrganization().getPhoto()).asBitmap().into(new CircleImgWrapper(hostImg, App.AppContex));
            addCertificate.setOrganization_id(certificatesEntity.getOrganization().getId() + "");
            recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
            recordeditDate.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

            recordEditName.setContent(certificatesEntity.getName());
            recordeditScore.setContent(certificatesEntity.getGrade());
            if (certificatesEntity.getGrade() == null || TextUtils.isEmpty(certificatesEntity.getGrade()) || certificatesEntity.getGrade().equals("0")) {
                comfirmScroeSwitch.setChecked(false);


            } else {
                comfirmScroeSwitch.setChecked(true);
                recordeditScore.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(certificatesEntity.getCertificate_name())) {
                comfirmCertificationSwitch.setChecked(false);
            } else {
                comfirmCertificationSwitch.setChecked(true);
                recordeditCertificatName.setContent(certificatesEntity.getCertificate_name());
                recordeditDatestart.setVisibility(View.VISIBLE);
                recordeditDateoff.setVisibility(View.VISIBLE);
                recordeditCertificatName.setVisibility(View.VISIBLE);
                if (mIsHidenImg)
                    recordeditUpimgLayout.setVisibility(View.VISIBLE);
            }
            Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000) {
                recordeditDateoff.setContent("长期有效");
            } else {
                recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(d));
            }
//            switch (certificatesEntity.getType()) {
//                case TYPE_MEETING:
//
//                    break;
//                case TYPE_COMFIRM:
//                    recordeditType.check(R.id.recordedit_type_comfirm);
//                    break;
//                case TYPE_COMPETITION:
//                    recordeditType.check(R.id.recordedit_type_competition);
//                    break;
//            }
            if (!TextUtils.isEmpty(certificatesEntity.getPhoto())) {
                recordeditImg.setVisibility(View.VISIBLE);
                Glide.with(App.AppContex).load(certificatesEntity.getPhoto()).into(recordeditImg);
            } else recordeditImg.setVisibility(View.GONE);
        } else {
            recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        }
//        recordeditType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.recordedit_type_meeting:
//                        addCertificate.setType(TYPE_MEETING);
//                        break;
//                    case R.id.recordedit_type_comfirm:
//                        addCertificate.setType(TYPE_COMFIRM);
//                        break;
//                    case R.id.recordedit_type_competition:
//                        addCertificate.setType(TYPE_COMPETITION);
//                        break;
//                }
//            }
//        });


        comfirmCertificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recordeditDatestart.setVisibility(View.VISIBLE);
                    recordeditDateoff.setVisibility(View.VISIBLE);
                    recordeditCertificatName.setVisibility(View.VISIBLE);
                    if (mIsHidenImg)
                        recordeditUpimgLayout.setVisibility(View.VISIBLE);
                } else {
                    recordeditDatestart.setVisibility(View.GONE);
                    recordeditDateoff.setVisibility(View.GONE);
                    recordeditCertificatName.setVisibility(View.GONE);
                    if (mIsHidenImg)
                        recordeditUpimgLayout.setVisibility(View.GONE);
                }
            }
        });
        comfirmScroeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recordeditScore.setVisibility(View.VISIBLE);
                } else {
                    recordeditScore.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @OnClick(R.id.comfirm_certification_layout)
    public void onCertification() {
        comfirmCertificationSwitch.toggle();
    }

    @OnClick(R.id.comfirm_scroe_layout)
    public void onScroelayout() {
        comfirmScroeSwitch.toggle();
    }


    @OnClick(R.id.host_layout)
    public void onHost() {
//        fragmentCallBack.onFragmentChange(SearchFragment.newInstance(SearchFragment.TYPE_ORGANASITON));
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
        startActivityForResult(toSearch, 10010);
    }

    @OnClick(R.id.recordedit_comfirm_btn)
    public void onComplete() {

        if (TextUtils.isEmpty(recordEditName.getContent())) {
            Toast.makeText(App.AppContex, "请填写认证名称", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(recordeditHost.getContent())) {
//            Toast.makeText(App.AppContex, "请填写主办机构", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (TextUtils.isEmpty(recordeditDate.getContent())) {
            Toast.makeText(App.AppContex, "请选择发证日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recordeditDateoff.getContent()) || TextUtils.isEmpty(recordeditDatestart.getContent())) {
            Toast.makeText(App.AppContex, "请填写生效日期和失效日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comfirmScroeSwitch.isChecked() && TextUtils.isEmpty(recordeditScore.getContent())) {
            Toast.makeText(App.AppContex, "请填写学分", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comfirmCertificationSwitch.isChecked() && TextUtils.isEmpty(recordeditCertificatName.getContent())) {
            if (mType == 2)
                Toast.makeText(App.AppContex, "请填写证书名称", Toast.LENGTH_SHORT).show();
            else if (mType == 3)
                Toast.makeText(App.AppContex, "请填写奖项名称", Toast.LENGTH_SHORT).show();
            return;
        }


        addCertificate.setGrade(recordeditScore.getContent());
        addCertificate.setName(recordEditName.getContent());
        addCertificate.setDate_of_issue(DateUtils.formatDateToServer(recordeditDate.getContent()));
        addCertificate.setStart(DateUtils.formatDateToServer(recordeditDatestart.getContent()));
        addCertificate.setCertificate_name(recordeditCertificatName.getContent());
        if (addCertificate.getType() == 0)
            addCertificate.setType(mType);
        if (!comfirmCertificationSwitch.isChecked()) {
            addCertificate.setCertificate_name("");
            addCertificate.setPhoto("");
        }
        if (!comfirmScroeSwitch.isChecked())
            addCertificate.setGrade("");

        String endtime = recordeditDateoff.getContent().trim();
        if (endtime.equalsIgnoreCase("长期有效")) {
            endtime = "3000-1-1";
            addCertificate.setWill_expired(false);
        } else {
            endtime = DateUtils.formatDateToServer(recordeditDateoff.getContent());
            addCertificate.setWill_expired(true);
        }
        addCertificate.setEnd(endtime);
        if (DateUtils.formatDateFromYYYYMMDD(addCertificate.getStart()).getTime() > DateUtils.formatDateFromYYYYMMDD(addCertificate.getEnd()).getTime()) {
            Toast.makeText(App.AppContex, "失效日期不能小于生效日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!comfirmCertificationSwitch.isChecked() || comfirmCertificationLayout.getVisibility() == View.GONE) {
            addCertificate.setStart("");
            addCertificate.setEnd("");
        }

        fragmentCallBack.ShowLoading("请稍后");
        if (mTitle)
            QcCloudClient.getApi().postApi.qcEditCertificate(certificatesEntity.getId(), addCertificate)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onResult, throwable -> {
            }, () -> {
            });
        else
            QcCloudClient.getApi().postApi.qcAddCertificate(addCertificate).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onResult, throwable -> {
            }, () -> {
            });
    }

    public void onResult(QcResponse qcResponse) {
        getActivity().runOnUiThread(() -> {
            fragmentCallBack.hideLoading();
            if (qcResponse.status == ResponseResult.SUCCESS) {
                fragmentCallBack.fixCount();
                getActivity().onBackPressed();
            } else {
                Toast.makeText(App.AppContex, "删除失败:" + qcResponse.msg, Toast.LENGTH_SHORT).show();
            }

        });

    }

    @OnClick(R.id.recordedit_datestart)
    public void onClickStart() {
        if (!TextUtils.isEmpty(recordeditDatestart.getContent())) {
            pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDatestart.getContent()));
        }
        pwTime.setOnTimeSelectListener(date -> {
            if (!TextUtils.equals(recordeditDateoff.getContent(), "长期有效") &&
                    DateUtils.formatDateFromYYYYMMDD(recordeditDateoff.getContent()).getTime()
                            < date.getTime()
                    ) {
                Toast.makeText(App.AppContex, "生效日期不能晚于失效日期", Toast.LENGTH_SHORT).show();
                return;
            }

            recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(date));
            pwTime.dismiss();
        });
        pwTime.setRange(1900, 2100);
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }


    @OnClick(R.id.recordedit_date)
    public void onClickDate() {
        if (!TextUtils.isEmpty(recordeditDate.getContent())) {
            pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDate.getContent()));
        }
        pwTime.setOnTimeSelectListener(date -> {
            recordeditDate.setContent(DateUtils.Date2YYYYMMDD(date));
        });
        pwTime.setRange(1900, 2100);
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    @OnClick(R.id.recordedit_dateoff)
    public void onClickDateoff() {
        if (mDialogSheet == null) {
            mDialogSheet = DialogSheet.builder(getContext())
                    .addButton("长期有效", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogSheet.dismiss();
                            recordeditDateoff.setContent("长期有效");

                        }
                    })
                    .addButton("选择日期", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogSheet.dismiss();
                            if (!TextUtils.isEmpty(recordeditDateoff.getContent())) {
                                pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDateoff.getContent()));
                            }
                            pwTime.setOnTimeSelectListener(date -> {
                                if (date.getTime()
                                        < DateUtils.formatDateFromYYYYMMDD(recordeditDatestart.getContent()).getTime()
                                        ) {
                                    Toast.makeText(App.AppContex, "失效日期不能早于生效日期", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(date));
                                pwTime.dismiss();
                            });
                            pwTime.setRange(1900, 2100);
                            pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());


                        }
                    });
        }

        mDialogSheet.show();


//        pwTime.setOnTimeSelectListener(date -> {
//            recordeditDateoff.setContent(DateUtils.getDateDay(date));
//            addCertificate.setEnd(DateUtils.formatDateToServer(DateUtils.getDateDay(date)));
//        });
//        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }


    @OnClick({R.id.recordedit_upimg_layout})
    public void onUpdatePic() {
        PicChooseDialog dialog = new PicChooseDialog(getActivity());
        dialog.setListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();
                                   Intent intent = new Intent();
                                   // 指定开启系统相机的Action
                                   intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                   intent.addCategory(Intent.CATEGORY_DEFAULT);
                                   intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
                                   startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
                               }
                           },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                }

        );
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = "";
        if (hostName == null)
            return;

        if (resultCode == -1) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            else filepath = FILE_PATH;
            LogUtil.d(filepath);
            fragmentCallBack.ShowLoading("正在上传");
            Observable.just(filepath)
                    .subscribeOn(Schedulers.io())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
                        File upFile = new File(Configs.ExternalCache + filename);
                        boolean reslut = UpYunClient.upLoadImg("/certificate/", filename, upFile);
                        getActivity().runOnUiThread(() -> {
                            fragmentCallBack.hideLoading();

                            if (reslut) {

                                LogUtil.d("success");
                                Glide.with(App.AppContex).load(Uri.fromFile(upFile))
                                        .asBitmap()
                                        .into(new ScaleWidthWrapper(recordeditImg));
                                recordeditImg.setVisibility(View.VISIBLE);
                                addCertificate.setPhoto(UpYunClient.UPYUNPATH + "/certificate/" + filename + ".png");

                            } else {
                                Toast.makeText(App.AppContex, "图片上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });

        } else if (requestCode == 10010 && resultCode > 0) {
            addCertificate.setOrganization_id(Integer.toString(data.getIntExtra("id", 0)));
            hostName.setText(data.getStringExtra("username"));
            Glide.with(App.AppContex).load(data.getStringExtra("pic")).asBitmap().into(new CircleImgWrapper(hostImg, App.AppContex));
            hostAddress.setText("联系方式:" + data.getStringExtra("address"));
            if (data.getBooleanExtra("isauth", false))
                hostQcIdentify.setVisibility(View.VISIBLE);
            else hostQcIdentify.setVisibility(View.GONE);

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
