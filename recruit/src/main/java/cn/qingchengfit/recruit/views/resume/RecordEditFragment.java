package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Organization;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@FragmentWithArgs public class RecordEditFragment extends BaseFragment {
  public static final String TAG = RecordEditFragment.class.getName();
  public static final int TYPE_MEETING = 1;
  public static final int TYPE_COMFIRM = 2;
  public static final int TYPE_COMPETITION = 3;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.recordedit_score) CommonInputView recordeditScore;
  @BindView(R2.id.recordedit_dateoff) CommonInputView recordeditDateoff;
  @BindView(R2.id.recordedit_upimg) TextView recordeditUpimg;
  @BindView(R2.id.recordedit_img) ImageView recordeditImg;
  @BindView(R2.id.recordedit_comfirm_btn) Button recordeditComfirmBtn;
  @BindView(R2.id.rootview) ScrollView rootview;
  @BindView(R2.id.record_edit_name) CommonInputView recordEditName;
  @BindView(R2.id.recordedit_datestart) CommonInputView recordeditDatestart;
  @BindView(R2.id.host_img) ImageView hostImg;
  @BindView(R2.id.host_qc_identify) ImageView hostQcIdentify;
  @BindView(R2.id.host_name) TextView hostName;
  @BindView(R2.id.host_address) TextView hostAddress;
  @BindView(R2.id.comfirm_scroe_layout) RelativeLayout comfirmScroeLayout;
  @BindView(R2.id.comfirm_has_certification) TextView comfirmHasCertification;
  @BindView(R2.id.comfirm_certification_layout) RelativeLayout comfirmCertificationLayout;
  @BindView(R2.id.recordedit_certificat_name) CommonInputView recordeditCertificatName;
  @BindView(R2.id.host_layout) RelativeLayout hostLayout;
  @BindView(R2.id.recordedit_date) CommonInputView recordeditDate;
  @BindView(R2.id.comfirm_scroe_switch) Switch comfirmScroeSwitch;
  @BindView(R2.id.comfirm_certification_switch) Switch comfirmCertificationSwitch;
  @BindView(R2.id.recordedit_upimg_layout) RelativeLayout recordeditUpimgLayout;
  TimeDialogWindow pwTime;
  @Inject QcRestRepository restRepository;
  @Arg int mType;
  @Arg(required = false) Certificate certificatesEntity;
  @Arg Organization organization;
  private boolean mTitle;
  private boolean mIsHidenImg = false;
  private Gson gson = new Gson();
  private Certificate addCertificate = new Certificate();
  private MaterialDialog delDialog;
  private DialogSheet mDialogSheet;
  private Subscription spUpImg;
  private ChoosePictureFragmentNewDialog choosePictureFragmentNewDialog;

  public RecordEditFragment() {
  }

  private void showDialog() {
    if (delDialog == null) {
      delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
          .content("删除此条资质?")
          .positiveText("确定")
          .negativeText("取消")
          .autoDismiss(true)
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
          })
          .build();
    }
    delDialog.show();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecordEditFragmentBuilder.injectArguments(this);
    mTitle = certificatesEntity != null;
    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_resume_record_edit, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    if (mType == 0) mType = 1;
    switch (mType) {
      case TYPE_MEETING:
        toolbarTitle.setText(mTitle ? "编辑认证信息" : "添加大会认证");
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
        toolbarTitle.setText(mTitle ? "编辑认证信息" : "添加培训认证");
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
        toolbarTitle.setText(mTitle ? "编辑认证信息" : "添加赛事认证");
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
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showDialog();
        return true;
      }
    });

    if (organization != null) {
      hostName.setText(organization.getName());
      hostAddress.setText("联系方式:" + organization.getContact());
      Glide.with(getContext()).load(organization.getPhoto()).asBitmap().into(new CircleImgWrapper(hostImg, getContext()));
      addCertificate.setOrganization_id(organization.id);
    }

    if (certificatesEntity != null) {
      if (certificatesEntity.is_authenticated()) {
        hostQcIdentify.setVisibility(View.VISIBLE);
      } else {
        hostQcIdentify.setVisibility(View.GONE);
      }
      recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getStart())));
      recordeditDate.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(certificatesEntity.getDate_of_issue())));

      recordEditName.setContent(certificatesEntity.getName());
      recordeditScore.setContent(certificatesEntity.getGrade());
      if (certificatesEntity.getGrade() == null || TextUtils.isEmpty(certificatesEntity.getGrade()) || certificatesEntity.getGrade()
          .equals("0")) {
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
      int y = c.get(Calendar.YEAR);
      if (3000L == y) {
        recordeditDateoff.setContent("长期有效");
      } else {
        recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(d));
      }

      if (!TextUtils.isEmpty(certificatesEntity.getPhoto())) {
        recordeditImg.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(certificatesEntity.getPhoto()).into(recordeditImg);
      } else {
        recordeditImg.setVisibility(View.GONE);
      }
    } else {
      recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
      recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    }

    comfirmCertificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    return view;
  }

  @OnClick(R2.id.comfirm_certification_layout) public void onCertification() {
    comfirmCertificationSwitch.toggle();
  }

  @OnClick(R2.id.comfirm_scroe_layout) public void onScroelayout() {
    comfirmScroeSwitch.toggle();
  }

  @OnClick(R2.id.host_layout) public void onHost() {
    //        fragmentCallBack.onFragmentChange(SearchFragment.newInstance(SearchFragment.TYPE_ORGANASITON));
    Intent toSearch = new Intent(getActivity(), SearchActivity.class);
    toSearch.putExtra("type", SearchFragment.TYPE_ORGANASITON);
    startActivityForResult(toSearch, 10010);
  }

  @OnClick(R2.id.recordedit_comfirm_btn) public void onComplete() {

    if (TextUtils.isEmpty(recordEditName.getContent())) {
      Toast.makeText(getContext(), "请填写认证名称", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(recordeditDate.getContent())) {
      Toast.makeText(getContext(), "请选择发证日期", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(recordeditDateoff.getContent()) || TextUtils.isEmpty(recordeditDatestart.getContent())) {
      Toast.makeText(getContext(), "请填写生效日期和失效日期", Toast.LENGTH_SHORT).show();
      return;
    }
    if (comfirmCertificationSwitch.isChecked() && TextUtils.isEmpty(recordeditCertificatName.getContent())) {
      if (mType == 2) {
        Toast.makeText(getContext(), "请填写证书名称", Toast.LENGTH_SHORT).show();
      } else if (mType == 3) Toast.makeText(getContext(), "请填写奖项名称", Toast.LENGTH_SHORT).show();
      return;
    }


    addCertificate.setName(recordEditName.getContent());
    addCertificate.setDate_of_issue(DateUtils.formatDateToServer(recordeditDate.getContent()));

    addCertificate.setStart(DateUtils.formatDateToServer(recordeditDatestart.getContent()));
    addCertificate.setCertificate_name(recordeditCertificatName.getContent());
    if (addCertificate.getType() == 0) addCertificate.setType(mType);
    if (!comfirmCertificationSwitch.isChecked()) {
      addCertificate.setCertificate_name(null);
      addCertificate.setPhoto(null);
    }
    String endtime = recordeditDateoff.getContent().trim();
    if (endtime.equalsIgnoreCase("长期有效")) {
      endtime = "3000-1-1";
      addCertificate.setWill_expired(false);
    } else {
      endtime = DateUtils.formatDateToServer(recordeditDateoff.getContent());
      addCertificate.setWill_expired(true);
    }
    addCertificate.setEnd(endtime);
    if (DateUtils.formatDateFromYYYYMMDD(addCertificate.getStart()).getTime() > DateUtils.formatDateFromYYYYMMDD(addCertificate.getEnd())
        .getTime()) {
      Toast.makeText(getContext(), "失效日期不能小于生效日期", Toast.LENGTH_SHORT).show();
      return;
    }
    if (!comfirmCertificationSwitch.isChecked() || comfirmCertificationLayout.getVisibility() == View.GONE) {
      addCertificate.setStart(null);
      addCertificate.setEnd(null);
    }
    showLoading();
    if (mTitle) {
      RxRegiste(restRepository.createGetApi(PostApi.class)
          .updateCertificate(certificatesEntity.getId(), addCertificate)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
              onResult(qcResponse);
            }
          }, new NetWorkThrowable()));
    } else {
      RxRegiste(restRepository.createGetApi(PostApi.class)
          .addCertificate(addCertificate).onBackpressureBuffer().subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
              onResult(qcResponse);
            }
          }, new NetWorkThrowable()));
    }
  }

  public void onResult(QcResponse qcResponse) {
    hideLoading();
    if (qcResponse.status == 200) {
      getActivity().onBackPressed();
    } else {
      Toast.makeText(getContext(), "删除失败:" + qcResponse.msg, Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R2.id.recordedit_datestart) public void onClickStart() {
    if (!TextUtils.isEmpty(recordeditDatestart.getContent())) {
      pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDatestart.getContent()));
    }
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        if (!TextUtils.equals(recordeditDateoff.getContent(), "长期有效")
            && DateUtils.formatDateFromYYYYMMDD(recordeditDateoff.getContent()).getTime() < date.getTime()) {
          Toast.makeText(getContext(), "生效日期不能晚于失效日期", Toast.LENGTH_SHORT).show();
          return;
        }

        recordeditDatestart.setContent(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.setRange(1900, 2100);
    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
  }

  @OnClick(R2.id.recordedit_date) public void onClickDate() {
    if (!TextUtils.isEmpty(recordeditDate.getContent())) {
      pwTime.setTime(DateUtils.formatDateFromYYYYMMDD(recordeditDate.getContent()));
    }
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        recordeditDate.setContent(DateUtils.Date2YYYYMMDD(date));
      }
    });
    pwTime.setRange(1900, 2100);
    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
  }

  @OnClick(R2.id.recordedit_dateoff) public void onClickDateoff() {
    if (mDialogSheet == null) {
      mDialogSheet = DialogSheet.builder(getContext()).addButton("长期有效", new View.OnClickListener() {
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
          pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
              if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(recordeditDatestart.getContent()).getTime()) {
                Toast.makeText(getContext(), "失效日期不能早于生效日期", Toast.LENGTH_SHORT).show();
                return;
              }

              recordeditDateoff.setContent(DateUtils.Date2YYYYMMDD(date));
              pwTime.dismiss();
            }
          });
          pwTime.setRange(1900, 2100);
          pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
        }
      });
    }

    mDialogSheet.show();
  }

  @OnClick({ R2.id.recordedit_upimg_layout }) public void onUpdatePic() {
    if (choosePictureFragmentNewDialog == null) choosePictureFragmentNewDialog = ChoosePictureFragmentNewDialog.newInstance();
    choosePictureFragmentNewDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
      @Override public void onChoosefile(String filePath) {

      }

      @Override public void onUploadComplete(String filePaht, String url) {
        recordeditImg.setVisibility(View.VISIBLE);
        PhotoUtils.origin(recordeditImg, url);
        if (addCertificate != null) addCertificate.setPhoto(url);
      }
    });
    choosePictureFragmentNewDialog.show(getChildFragmentManager(), "");
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String filepath = "";
    if (hostName == null) return;

    if (requestCode == 10010 && resultCode > 0) {
      addCertificate.setOrganization_id(Integer.toString(data.getIntExtra("id", 0)));
      hostName.setText(data.getStringExtra("username"));
      Glide.with(getContext()).load(data.getStringExtra("pic")).asBitmap().into(new CircleImgWrapper(hostImg, getContext()));
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
