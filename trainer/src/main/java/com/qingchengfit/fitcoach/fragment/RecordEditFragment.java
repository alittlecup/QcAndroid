package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import cn.qingchengfit.events.EventChooseImage;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddCertificate;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
  CommonInputView recordeditScore;
  CommonInputView recordeditDateoff;
  TextView recordeditUpimg;
  ImageView recordeditImg;
  Button recordeditComfirmBtn;
  TimeDialogWindow pwTime;
  ScrollView rootview;
  CommonInputView recordEditName;
  CommonInputView recordeditDatestart;
  ImageView hostImg;
  ImageView hostQcIdentify;
  TextView hostName;
  TextView hostAddress;
  RelativeLayout comfirmScroeLayout;
  TextView comfirmHasCertification;
  RelativeLayout comfirmCertificationLayout;
  CommonInputView recordeditCertificatName;
  RelativeLayout hostLayout;
  CommonInputView recordeditDate;
  Switch comfirmScroeSwitch;
  Switch comfirmCertificationSwitch;
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

  private Subscription spUpImg;

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
    DialogUtils.showConfirm(getContext(), "", "删除此条资质?", (dialog, action) -> {
      dialog.dismiss();
      if (action == DialogAction.POSITIVE) {
        deleteCer();
      }
    });
  }

  private void deleteCer() {
    fragmentCallBack.ShowLoading("请稍后");
    QcCloudClient.getApi().postApi.qcDelCertificate(certificatesEntity.getId())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(qcResponse -> onResult(qcResponse), throwable -> {
        }, () -> {
        });
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mTitle = getArguments().getBoolean(TITLE);
      mContent = getArguments().getString(CONTENT);
      mType = getArguments().getInt(TYPE);
    }
    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_resume_record_edit, container, false);
    recordeditScore = (CommonInputView) view.findViewById(R.id.recordedit_score);
    recordeditDateoff = (CommonInputView) view.findViewById(R.id.recordedit_dateoff);
    recordeditUpimg = (TextView) view.findViewById(R.id.recordedit_upimg);
    recordeditImg = (ImageView) view.findViewById(R.id.recordedit_img);
    recordeditComfirmBtn = (Button) view.findViewById(R.id.recordedit_comfirm_btn);
    rootview = (ScrollView) view.findViewById(R.id.rootview);
    recordEditName = (CommonInputView) view.findViewById(R.id.record_edit_name);
    recordeditDatestart = (CommonInputView) view.findViewById(R.id.recordedit_datestart);
    hostImg = (ImageView) view.findViewById(R.id.host_img);
    hostQcIdentify = (ImageView) view.findViewById(R.id.host_qc_identify);
    hostName = (TextView) view.findViewById(R.id.host_name);
    hostAddress = (TextView) view.findViewById(R.id.host_address);
    comfirmScroeLayout = (RelativeLayout) view.findViewById(R.id.comfirm_scroe_layout);
    comfirmHasCertification = (TextView) view.findViewById(R.id.comfirm_has_certification);
    comfirmCertificationLayout =
        (RelativeLayout) view.findViewById(R.id.comfirm_certification_layout);
    recordeditCertificatName = (CommonInputView) view.findViewById(R.id.recordedit_certificat_name);
    hostLayout = (RelativeLayout) view.findViewById(R.id.host_layout);
    recordeditDate = (CommonInputView) view.findViewById(R.id.recordedit_date);
    comfirmScroeSwitch = (Switch) view.findViewById(R.id.comfirm_scroe_switch);
    comfirmCertificationSwitch = (Switch) view.findViewById(R.id.comfirm_certification_switch);
    recordeditUpimgLayout = (RelativeLayout) view.findViewById(R.id.recordedit_upimg_layout);
    view.findViewById(R.id.comfirm_certification_layout)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onCertification();
          }
        });
    view.findViewById(R.id.comfirm_scroe_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onScroelayout();
      }
    });
    view.findViewById(R.id.host_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onHost();
      }
    });
    view.findViewById(R.id.recordedit_comfirm_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onComplete();
      }
    });
    view.findViewById(R.id.recordedit_datestart).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickStart();
      }
    });
    view.findViewById(R.id.recordedit_date).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickDate();
      }
    });
    view.findViewById(R.id.recordedit_dateoff).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickDate();
      }
    });
    view.findViewById(R.id.recordedit_upimg_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onUpdatePic();
      }
    });

    if (mType == 0) mType = 1;
    switch (mType) {
      case TYPE_MEETING:
        fragmentCallBack.onToolbarMenu(0, 0, mTitle ? "编辑认证信息" : "添加大会认证");
        recordEditName.setLabel("大会名称");
        //recordeditDate.setLabel("大会日期");
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
        //recordeditDate.setLabel("培训日期");
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
        //recordeditDate.setLabel("赛事日期");
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
    if (addCertificate == null) addCertificate = new AddCertificate(App.coachid);
    if (mContent != null) {
      certificatesEntity =
          gson.fromJson(mContent, QcCertificatesReponse.DataEntity.CertificatesEntity.class);
      //            recordeditHost.setContent(certificatesEntity.getOrganization().getName());
      hostName.setText(certificatesEntity.getOrganization().getName());
      hostAddress.setText("联系方式:" + certificatesEntity.getOrganization().getContact());
      if (certificatesEntity.getIs_authenticated()) {
        hostQcIdentify.setVisibility(View.VISIBLE);
      } else {
        hostQcIdentify.setVisibility(View.GONE);
      }
      Glide.with(App.AppContex)
          .load(certificatesEntity.getOrganization().getPhoto())
          .asBitmap()
          .into(new CircleImgWrapper(hostImg, App.AppContex));
      addCertificate.setOrganization_id(certificatesEntity.getOrganization().getId() + "");
      recordeditDatestart.setContent(
          DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
      recordeditDate.setContent(DateUtils.Date2YYYYMMDD(
          DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

      recordEditName.setContent(certificatesEntity.getName());
      recordeditScore.setContent(certificatesEntity.getGrade());
      if (certificatesEntity.getGrade() == null
          || TextUtils.isEmpty(certificatesEntity.getGrade())
          || certificatesEntity.getGrade().equals("0")) {
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
        if (mIsHidenImg) recordeditUpimgLayout.setVisibility(View.VISIBLE);
      }
      Date d = DateUtils.formatDateFromServer(certificatesEntity.getEnd());
      Calendar c = Calendar.getInstance(Locale.getDefault());
      c.setTime(d);
      if (c.get(Calendar.YEAR) == 3000) {
        recordeditDateoff.setContent("长期有效");
      } else {
        recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(d));
      }

      if (!TextUtils.isEmpty(certificatesEntity.getPhoto())) {
        recordeditImg.setVisibility(View.VISIBLE);
        Glide.with(App.AppContex).load(certificatesEntity.getPhoto()).into(recordeditImg);
      } else {
        recordeditImg.setVisibility(View.GONE);
      }
    } else {
      recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
      recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    }

    comfirmCertificationSwitch.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
              recordeditDatestart.setVisibility(View.VISIBLE);
              recordeditDateoff.setVisibility(View.VISIBLE);
              recordeditCertificatName.setVisibility(View.VISIBLE);
              if (mIsHidenImg) recordeditUpimgLayout.setVisibility(View.VISIBLE);
            } else {
              recordeditDatestart.setVisibility(View.GONE);
              recordeditDateoff.setVisibility(View.GONE);
              recordeditCertificatName.setVisibility(View.GONE);
              if (mIsHidenImg) recordeditUpimgLayout.setVisibility(View.GONE);
            }
          }
        });
    comfirmScroeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          recordeditScore.setVisibility(View.VISIBLE);
        } else {
          recordeditScore.setVisibility(View.GONE);
        }
      }
    });
    RxBusAdd(EventChooseImage.class).subscribe(eventChooseImage -> {
      showLoading();
      RxRegiste(UpYunClient.rxUpLoad("record/", eventChooseImage.filePath)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<String>() {
            @Override public void call(String s) {
              hideLoading();
              if (TextUtils.isEmpty(s)) {
                Toast.makeText(App.AppContex, "图片上传失败", Toast.LENGTH_SHORT).show();
              } else {
                Glide.with(App.AppContex)
                    .load(s)
                    .asBitmap()
                    .into(new ScaleWidthWrapper(recordeditImg));
                recordeditImg.setVisibility(View.VISIBLE);
                addCertificate.setPhoto(s);
              }
            }
          }, throwable -> hideLoading()));
    });

    return view;
  }

  public void onCertification() {
    comfirmCertificationSwitch.toggle();
  }

  public void onScroelayout() {
    comfirmScroeSwitch.toggle();
  }

  public void onHost() {
    //        fragmentCallBack.onFragmentChange(SearchFragment.newInstance(SearchFragment.TYPE_ORGANASITON));
    Intent toSearch = new Intent(getActivity(), SearchActivity.class);
    toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
    startActivityForResult(toSearch, 10010);
  }

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
    if (TextUtils.isEmpty(recordeditDateoff.getContent()) || TextUtils.isEmpty(
        recordeditDatestart.getContent())) {
      Toast.makeText(App.AppContex, "请填写生效日期和失效日期", Toast.LENGTH_SHORT).show();
      return;
    }
    if (comfirmScroeSwitch.isChecked() && TextUtils.isEmpty(recordeditScore.getContent())) {
      Toast.makeText(App.AppContex, "请填写学分", Toast.LENGTH_SHORT).show();
      return;
    }
    if (comfirmCertificationSwitch.isChecked() && TextUtils.isEmpty(
        recordeditCertificatName.getContent())) {
      if (mType == 2) {
        Toast.makeText(App.AppContex, "请填写证书名称", Toast.LENGTH_SHORT).show();
      } else if (mType == 3) Toast.makeText(App.AppContex, "请填写奖项名称", Toast.LENGTH_SHORT).show();
      return;
    }

    addCertificate.setGrade(recordeditScore.getContent());
    addCertificate.setName(recordEditName.getContent());
    addCertificate.setDate_of_issue(DateUtils.formatDateToServer(recordeditDate.getContent()));
    addCertificate.setStart(DateUtils.formatDateToServer(recordeditDatestart.getContent()));
    addCertificate.setCertificate_name(recordeditCertificatName.getContent());
    if (addCertificate.getType() == 0) addCertificate.setType(mType);
    if (!comfirmCertificationSwitch.isChecked()) {
      addCertificate.setCertificate_name("");
      addCertificate.setPhoto("");
    }
    if (!comfirmScroeSwitch.isChecked()) addCertificate.setGrade("");

    String endtime = recordeditDateoff.getContent().trim();
    if (endtime.equalsIgnoreCase("长期有效")) {
      endtime = "3000-1-1";
      addCertificate.setWill_expired(false);
    } else {
      endtime = DateUtils.formatDateToServer(recordeditDateoff.getContent());
      addCertificate.setWill_expired(true);
    }
    addCertificate.setEnd(endtime);
    if (DateUtils.formatDateFromYYYYMMDD(addCertificate.getStart()).getTime()
        > DateUtils.formatDateFromYYYYMMDD(addCertificate.getEnd()).getTime()) {
      Toast.makeText(App.AppContex, "失效日期不能小于生效日期", Toast.LENGTH_SHORT).show();
      return;
    }
    if (!comfirmCertificationSwitch.isChecked()
        || comfirmCertificationLayout.getVisibility() == View.GONE) {
      addCertificate.setStart("");
      addCertificate.setEnd("");
    }

    fragmentCallBack.ShowLoading("请稍后");
    if (mTitle) {
      QcCloudClient.getApi().postApi.qcEditCertificate(certificatesEntity.getId(), addCertificate)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onResult, throwable -> {
          }, () -> {
          });
    } else {
      QcCloudClient.getApi().postApi.qcAddCertificate(addCertificate)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onResult, throwable -> {
          }, () -> {
          });
    }
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

  public void onClickStart() {
    if (!TextUtils.isEmpty(recordeditDatestart.getContent())) {
      pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDatestart.getContent()));
    }
    pwTime.setOnTimeSelectListener(date -> {
      if (!TextUtils.equals(recordeditDateoff.getContent(), "长期有效")
          && DateUtils.formatDateFromYYYYMMDD(recordeditDateoff.getContent()).getTime()
          < date.getTime()) {
        Toast.makeText(App.AppContex, "生效日期不能晚于失效日期", Toast.LENGTH_SHORT).show();
        return;
      }

      recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(date));
      pwTime.dismiss();
    });
    pwTime.setRange(1900, 2100);
    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
  }

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

  public void onClickDateoff() {
    if (mDialogSheet == null) {
      mDialogSheet =
          DialogSheet.builder(getContext()).addButton("长期有效", new View.OnClickListener() {
            @Override public void onClick(View v) {
              mDialogSheet.dismiss();
              recordeditDateoff.setContent("长期有效");
            }
          }).addButton("选择日期", new View.OnClickListener() {
            @Override public void onClick(View v) {
              mDialogSheet.dismiss();
              if (!TextUtils.isEmpty(recordeditDateoff.getContent())) {
                pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDateoff.getContent()));
              }
              pwTime.setOnTimeSelectListener(date -> {
                if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(
                    recordeditDatestart.getContent()).getTime()) {
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
  }

  public void onUpdatePic() {
    ChoosePictureFragmentDialog.newInstance(false).show(getFragmentManager(), "");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String filepath = "";
    if (hostName == null) return;

    if (requestCode == 10010 && resultCode > 0) {
      addCertificate.setOrganization_id(Integer.toString(data.getIntExtra("id", 0)));
      hostName.setText(data.getStringExtra("username"));
      Glide.with(App.AppContex)
          .load(data.getStringExtra("pic"))
          .asBitmap()
          .into(new CircleImgWrapper(hostImg, App.AppContex));
      hostAddress.setText("联系方式:" + data.getStringExtra("address"));
      if (data.getBooleanExtra("isauth", false)) {
        hostQcIdentify.setVisibility(View.VISIBLE);
      } else {
        hostQcIdentify.setVisibility(View.GONE);
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    if (spUpImg != null && !spUpImg.isUnsubscribed()) spUpImg.unsubscribe();
  }
}
