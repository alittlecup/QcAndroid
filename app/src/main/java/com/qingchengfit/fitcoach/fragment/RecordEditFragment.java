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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.BitmapUtils;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.TextpaperUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private static String FILE_PATH = Configs.ExternalPath + "tmp_certificate.png";
    @Bind(R.id.recordedit_host)
    CommonInputView recordeditHost;
    @Bind(R.id.recordedit_type_meeting)
    RadioButton recordeditTypeMeeting;
    @Bind(R.id.recordedit_type_comfirm)
    RadioButton recordeditTypeComfirm;
    @Bind(R.id.recordedit_type_competition)
    RadioButton recordeditTypeCompetition;
    @Bind(R.id.recordedit_type)
    RadioGroup recordeditType;
    @Bind(R.id.recordedit_date)
    CommonInputView recordeditDate;
    @Bind(R.id.recordedit_score)
    CommonInputView recordeditScore;
    @Bind(R.id.recordedit_dateoff)
    CommonInputView recordeditDateoff;
    @Bind(R.id.recordedit_upimg)
    TextView recordeditUpimg;
    @Bind(R.id.recordedit_img)
    ImageView recordeditImg;
    @Bind(R.id.recordedit_comfirm_btn)
    Button recordeditComfirmBtn;
    TimeDialogWindow pwTime;
    @Bind(R.id.rootview)
    ScrollView rootview;
    @Bind(R.id.record_edit_name)
    CommonInputView recordEditName;
    @Bind(R.id.recordedit_datestart)
    CommonInputView recordeditDatestart;
    private boolean mTitle;
    private String mContent;
    private Gson gson = new Gson();
    private QcCertificatesReponse.DataEntity.CertificatesEntity certificatesEntity;
    private AddCertificate addCertificate;
    private MaterialDialog delDialog;
    private DialogSheet mDialogSheet;

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
    // TODO: Rename and change types and number of parameters
    public static RecordEditFragment newInstance(boolean param1, String param2) {
        RecordEditFragment fragment = new RecordEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(TITLE, param1);
        args.putString(CONTENT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .title("是否确定删除")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            fragmentCallBack.ShowLoading("请稍后");
                            QcCloudClient.getApi().postApi.qcDelCertificate(certificatesEntity.getId()).subscribeOn(Schedulers.newThread()).subscribe(qcResponse -> onResult(qcResponse));
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
        }
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record_edit, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(mTitle ? R.menu.menu_delete : 0, 0, mTitle ? "编辑认证信息" : "添加认证");
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
            recordeditHost.setContent(certificatesEntity.getOrganization().getName());
            recordeditDatestart.setContent(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
            recordeditDate.setContent(DateUtils.getDateDay(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

            recordEditName.setContent(certificatesEntity.getName());
            recordeditScore.setContent(certificatesEntity.getGrade());
            Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000) {
                recordeditDateoff.setContent("长期有效");
            } else {
                recordeditDateoff.setContent(DateUtils.getDateDay(d));
            }
            switch (certificatesEntity.getType()) {
                case TYPE_MEETING:
                    recordeditType.check(R.id.recordedit_type_meeting);
                    break;
                case TYPE_COMFIRM:
                    recordeditType.check(R.id.recordedit_type_comfirm);
                    break;
                case TYPE_COMPETITION:
                    recordeditType.check(R.id.recordedit_type_competition);
                    break;
            }
            if (!TextUtils.isEmpty(certificatesEntity.getPhoto())) {
                recordeditImg.setVisibility(View.VISIBLE);
                Glide.with(App.AppContex).load(certificatesEntity.getPhoto()).asBitmap().into(new ScaleWidthWrapper(recordeditImg));
            } else recordeditImg.setVisibility(View.GONE);
        } else {

        }
        recordeditType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.recordedit_type_meeting:
                        addCertificate.setType(TYPE_MEETING);
                        break;
                    case R.id.recordedit_type_comfirm:
                        addCertificate.setType(TYPE_COMFIRM);
                        break;
                    case R.id.recordedit_type_competition:
                        addCertificate.setType(TYPE_COMPETITION);
                        break;
                }
            }
        });

        return view;
    }

    @OnClick(R.id.recordedit_host)
    public void onHost() {
//        fragmentCallBack.onFragmentChange(SearchFragment.newInstance(SearchFragment.TYPE_ORGANASITON));
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
        startActivityForResult(toSearch, 10010);
    }

    @OnClick(R.id.recordedit_comfirm_btn)
    public void onComplete() {
        if (TextpaperUtils.isEmpty(recordEditName.getContent(), recordeditDate.getContent(),
                recordeditDateoff.getContent(), recordeditHost.getContent()
        ))
            Toast.makeText(App.AppContex, "请填写完整信息", Toast.LENGTH_SHORT).show();

        addCertificate.setGrade(recordeditScore.getContent());
        addCertificate.setName(recordEditName.getContent());
        addCertificate.setDate_of_issue(DateUtils.formatDateToServer(recordeditDate.getContent()));
        addCertificate.setStart(DateUtils.formatDateToServer(recordeditDatestart.getContent()));

        String endtime = recordeditDateoff.getContent().trim();
        if (endtime.equalsIgnoreCase("长期有效")) {
            endtime = "3000-1-1";
        } else endtime = DateUtils.formatDateToServer(recordeditDateoff.getContent());
        addCertificate.setEnd(endtime);
        if (DateUtils.formatDateFromString(addCertificate.getStart()).getTime() > DateUtils.formatDateFromString(addCertificate.getEnd()).getTime()) {
            Toast.makeText(App.AppContex, "失效日期不能小于生效日期", Toast.LENGTH_SHORT).show();
            return;
        }


        fragmentCallBack.ShowLoading("请稍后");
        if (mTitle)
            QcCloudClient.getApi().postApi.qcEditCertificate(certificatesEntity.getId(), addCertificate)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onResult);
        else
            QcCloudClient.getApi().postApi.qcAddCertificate(addCertificate).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onResult);
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
            pwTime.setTime(DateUtils.formatDateFromStringDot(recordeditDatestart.getContent()));
        }
        pwTime.setOnTimeSelectListener(date -> {
            recordeditDatestart.setContent(DateUtils.getDateDay(date));
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }


    @OnClick(R.id.recordedit_date)
    public void onClickDate() {
        if (!TextUtils.isEmpty(recordeditDate.getContent())) {
            pwTime.setTime(DateUtils.formatDateFromStringDot(recordeditDate.getContent()));
        }
        pwTime.setOnTimeSelectListener(date -> {
            recordeditDate.setContent(DateUtils.getDateDay(date));
        });
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
                                pwTime.setTime(DateUtils.formatDateFromStringDot(recordeditDateoff.getContent()));
                            }
                            pwTime.setOnTimeSelectListener(date -> {
                                recordeditDateoff.setContent(DateUtils.getDateDay(date));
                                pwTime.dismiss();
                            });
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


    @OnClick({R.id.recordedit_upimg, R.id.recordedit_img})
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
        if (resultCode == -1) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            else filepath = FILE_PATH;
            LogUtil.d(filepath);
            fragmentCallBack.ShowLoading("正在上网");
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
            recordeditHost.setContent(data.getStringExtra("name"));
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
