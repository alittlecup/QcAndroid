package cn.qingchengfit.staffkit.views.student.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.model.base.User_Student;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.MutiChooseCoachActivity;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/6 2016.
 */
public class EditStudentInfoFragment extends BaseFragment implements EditStudentInfoView {
  public static final int RESULT_SALERS = 1;
  public static final int RESULT_ORIGIN = 11;
  public static final int RESULT_REFERRER = 12;
  private static final int RESULT_ADD_REMARKS = 13;
  private static final int RESULT_COACH = 14;
  public boolean isAdd = true;
  public User_Student user;
  TextView tvStudentSource;
  LinearLayout llStudentSource;
  TextView tvStudentReferrer;
  LinearLayout llStudentReferrer;
  CommonInputView civRemark;
  ImageView headerImg;
  CommonInputView civName;
  RadioButton genderMale;
  RadioButton genderFemale;
  RadioGroup courseTypeRg;
  RelativeLayout genderLayout;
  CommonInputView civBirthday;
  CommonInputView civAddress;
  Button btnSave;
  RelativeLayout toolbarLayout;
  RelativeLayout headerLayout;
  Toolbar toolbar;
  TextView toolbarTitile;
  CommonInputView supportShops;
  CommonInputView mCivSalers;
  TextView tvHeader;
  TextView tvAddHint;
  PhoneEditText phoneNum;
  View divider;
  View marginDivider;
  TextView gender;
  @Inject EditStudentInfoPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  CommonInputView civCoaches;
  private String shopid;

  private StudentReferrerBean referrerBean;
  private StudentSourceBean sourceBean;
  private String remarks;
  private List<Staff> coaches = new ArrayList<>();

  public static EditStudentInfoFragment newInstance(boolean isAdd, User_Student student) {
    Bundle args = new Bundle();
    args.putBoolean("add", isAdd);
    args.putParcelable("student", student);
    EditStudentInfoFragment fragment = new EditStudentInfoFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initBus();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_edit_student_info, container, false);
    tvStudentSource = (TextView) view.findViewById(R.id.tv_student_source);
    llStudentSource = (LinearLayout) view.findViewById(R.id.ll_student_source);
    tvStudentReferrer = (TextView) view.findViewById(R.id.tv_student_referrer);
    llStudentReferrer = (LinearLayout) view.findViewById(R.id.ll_student_referrer);
    civRemark = (CommonInputView) view.findViewById(R.id.civ_remark);
    headerImg = (ImageView) view.findViewById(R.id.header_img);
    civName = (CommonInputView) view.findViewById(R.id.civ_name);
    genderMale = (RadioButton) view.findViewById(R.id.gender_male);
    genderFemale = (RadioButton) view.findViewById(R.id.gender_female);
    courseTypeRg = (RadioGroup) view.findViewById(R.id.course_type_rg);
    genderLayout = (RelativeLayout) view.findViewById(R.id.gender_layout);
    civBirthday = (CommonInputView) view.findViewById(R.id.civ_birthday);
    civAddress = (CommonInputView) view.findViewById(R.id.civ_address);
    btnSave = (Button) view.findViewById(R.id.btn_save);
    toolbarLayout = (RelativeLayout) view.findViewById(R.id.toolbar_layout);
    headerLayout = (RelativeLayout) view.findViewById(R.id.header_layout);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    gender = view.findViewById(R.id.gender);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    supportShops = (CommonInputView) view.findViewById(R.id.support_shops);
    mCivSalers = (CommonInputView) view.findViewById(R.id.civ_salers);
    tvHeader = (TextView) view.findViewById(R.id.tv_header);
    tvAddHint = (TextView) view.findViewById(R.id.tv_add_hint);
    phoneNum = (PhoneEditText) view.findViewById(R.id.phone_num);
    divider = (View) view.findViewById(R.id.divider_edit_student);
    marginDivider = (View) view.findViewById(R.id.margin_divider_edit_student);
    civCoaches = (CommonInputView) view.findViewById(R.id.civ_coaches);
    view.findViewById(R.id.civ_birthday).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBirthday();
      }
    });
    view.findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        EditStudentInfoFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        EditStudentInfoFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.support_shops).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSupportShops();
      }
    });
    view.findViewById(R.id.civ_salers).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSalerClick();
      }
    });
    view.findViewById(R.id.civ_coaches).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onChooseCoached();
      }
    });
    view.findViewById(R.id.civ_remark).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onRemarkClick();
      }
    });
    view.findViewById(R.id.ll_student_source).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSourceAndReferrerClick(v);
      }
    });
    view.findViewById(R.id.ll_student_referrer).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSourceAndReferrerClick(v);
      }
    });
    phoneNum.setRequest(true);

    delegatePresenter(presenter, this);
    if (getArguments() != null) {
      isAdd = getArguments().getBoolean("add");
      user = getArguments().getParcelable("student");
    }
    initToolbar(toolbar);
    if (!isAdd) {
      civName.setContent(user.getUsername());
      civAddress.setContent(user.getAddress());
      civRemark.setContent(user.getRemarks());
      phoneNum.setPhoneNum(user.getPhone());
      phoneNum.setdistrictInt(user.getArea_code());
      civBirthday.setContent(user.getDate_of_birth());
      courseTypeRg.check(user.getGender() == 0 ? R.id.gender_male : R.id.gender_female);
      supportShops.setVisibility(View.GONE);
      civAddress.setVisibility(View.VISIBLE);
      mCivSalers.setVisibility(View.GONE);
      civCoaches.setVisibility(View.GONE);
      divider.setVisibility(View.GONE);
      marginDivider.setVisibility(View.GONE);

      llStudentReferrer.setVisibility(View.GONE);
      llStudentSource.setVisibility(View.GONE);
      tvHeader.setText(R.string.student_header);
      tvAddHint.setVisibility(View.GONE);
      if (TextUtils.isEmpty(user.getAvatar())) {
        Glide.with(getActivity())
            .load(
                user.getGender() == 0 ? Configs.HEADER_STUDENT_MALE : Configs.HEADER_STUDENT_FEMALE)
            .asBitmap()
            .into(new CircleImgWrapper(headerImg, getActivity()));
      } else {
        Glide.with(getActivity())
            .load(PhotoUtils.getSmall(user.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(headerImg, getActivity()));
      }
    } else {
      user = new User_Student();
      headerImg.setImageResource(R.drawable.ic_camara_grey);
      tvAddHint.setVisibility(View.VISIBLE);
      user.setShops(gymWrapper.shop_id());
      tvHeader.setText(getString(R.string.signin_img));
    }
    courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        user.setGender(checkedId == R.id.gender_male ? 0 : 1);
        //if (TextUtils.isEmpty(user.getAvatar())) {
        //    Glide.with(getActivity())
        //        .load(checkedId == R.id.gender_male ? Configs.HEADER_STUDENT_MALE : Configs.HEADER_STUDENT_FEMALE)
        //        .asBitmap()
        //        .into(new CircleImgWrapper(headerImg, getActivity()));
        //}
      }
    });

    gender.setText(new SpanUtils().append(gender.getText())
        .append(" *")
        .setForegroundColor(getResources().getColor(R.color.red))
        .create());

    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    SensorsUtils.trackScreen(this.getClass().getCanonicalName() + (isAdd ? "add" : "edit"));
    return view;
  }

  private void initBus() {
    RxBus.getBus()
        .register(EventTxT.class)
        .onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventTxT>() {
          @Override public void onNext(EventTxT eventTxT) {
            if (!TextUtils.isEmpty(eventTxT.txt)) {
              civRemark.setContent("已填写");
              remarks = eventTxT.txt;
            } else {
              civRemark.setContent("请填写");
            }
          }
        });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_save);
    toolbarTitile.setText(isAdd ? "新增会员" : "修改会员信息");
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        saveStudentInfo();
        return true;
      }
    });
    civCoaches.setMaxLines(1);
  }

  @Override public String getFragmentName() {
    return EditStudentInfoFragment.class.getName();
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onBirthday() {
    final TimeDialogWindow pwTime =
        new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    pwTime.setRange(1900, 2100);
    pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        civBirthday.setContent(DateUtils.Date2YYYYMMDD(date));
        user.setDate_of_birth(DateUtils.Date2YYYYMMDD(date));
        pwTime.dismiss();
      }
    });
    pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.header_layout:
        ChoosePictureFragmentDialog f = ChoosePictureFragmentDialog.newInstance();
        f.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
          @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
            if (isSuccess) {
              showLoading();
              EditStudentInfoFragment.this.RxRegiste(
                  UpYunClient.rxUpLoad("header/", filePath).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                      if (isAdd) {
                        Glide.with(getContext())
                            .load(PhotoUtils.getSmall(s))
                            .asBitmap()
                            .into(headerImg);
                        user.setCheckin_avatar(s);
                      } else {
                        Glide.with(getContext())
                            .load(PhotoUtils.getSmall(s))
                            .asBitmap()
                            .into(new CircleImgWrapper(headerImg, getContext()));
                        user.setAvatar(s);
                      }
                      hideLoading();
                    }
                  }));
            }
          }
        });
        f.show(getFragmentManager(), "");
        break;
      case R.id.btn_save:

        break;
    }
  }

  /**
   * 保存会员信息
   */
  public void saveStudentInfo() {
    if (TextUtils.isEmpty(civName.getContent())) {
      ToastUtils.show("请填写学员姓名");
      return;
    }
    List<String> idList = new ArrayList<>();
    for (Staff coachBean : coaches) {
      idList.add(coachBean.id);
    }
    if (!idList.isEmpty()) {
      user.setCoach_ids(StringUtils.List2Str(idList));
    } else {
      user.setCoach_ids(null);
    }
    user.setUsername(civName.getContent());
    user.setAddress(civAddress.getContent());
    user.setRemarks(remarks);
    user.setRecommend_by_id(referrerBean == null ? null : referrerBean.id);
    user.setOrigin(sourceBean == null ? null : sourceBean.name);

    if (phoneNum.checkPhoneNum()) {
      user.setPhone(phoneNum.getPhoneNum());
      user.setArea_code(phoneNum.getDistrictInt());
    } else {
      ToastUtils.show("请填写正确的手机号码");
      return;
    }

    if (isAdd) {
      List<String> spIds = new ArrayList<>();
      spIds.add(gymWrapper.shop_id());
      user.setShops(StringUtils.List2Str(spIds));
      presenter.saveStudent(user);
    } else {
      presenter.editStudent(user);
    }
  }

  public void onSupportShops() {
    MutiChooseGymFragment.start(EditStudentInfoFragment.this, false, null, 4);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 4) {
        ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
        //                List<Integer> ppp = IntentUtils.getIntegerList(data);
        List<String> spIds = new ArrayList<>();
        String spGyms = "";
        if (shops != null) {
          for (int i = 0; i < shops.size(); i++) {

            if (i < shops.size() - 1) {
              spGyms = TextUtils.concat(spGyms, shops.get(i).name, "、").toString();
            } else {
              spGyms = TextUtils.concat(spGyms, shops.get(i).name).toString();
            }
            spIds.add(shops.get(i).id);
          }
        }
        supportShops.setContent(spGyms);
        user.setShops(StringUtils.List2Str(spIds));
      } else if (requestCode == RESULT_SALERS) {//销售选择
        ArrayList<Staff> reslut = data.getParcelableArrayListExtra("result");
        if (reslut != null && reslut.size() > 0) {
          List<String> salers = new ArrayList<>();
          List<String> salersid = new ArrayList<>();
          for (int i = 0; i < reslut.size(); i++) {
            salers.add(reslut.get(i).username);
            salersid.add(reslut.get(i).id);
          }
          mCivSalers.setContent(StringUtils.List2Str(salers));
          user.setSeller_ids(StringUtils.List2Str(salersid));
        } else {
          mCivSalers.setContent(getString(R.string.no_saler));
        }
      } else if (requestCode == RESULT_ORIGIN) {
        sourceBean = (StudentSourceBean) IntentUtils.getParcelable(data);
        tvStudentSource.setText(sourceBean.name);
      } else if (requestCode == RESULT_REFERRER) {
        referrerBean = (StudentReferrerBean) IntentUtils.getParcelable(data);
        tvStudentReferrer.setText(referrerBean.username);
      } else if (requestCode == RESULT_ADD_REMARKS) {
        remarks = IntentUtils.getIntentString(data);
        civRemark.setContent(remarks);
      } else if (requestCode == RESULT_COACH) {
        coaches = data.getParcelableArrayListExtra("result");
        if (coaches != null) {
          civCoaches.setContent(StringUtils.coachesNames(coaches));
          user.setCoaches(coaches);
        }
      }
    }
  }

  @Override public void onSuccess() {
    ToastUtils.show(isAdd ? "添加成功" : "保存成功");
    getActivity().onBackPressed();
  }

  @Override public void onFailed(String s) {
    ToastUtils.show(s);
  }

  public void onSalerClick() {
    if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
      Intent toChooseSaler = new Intent(getActivity(), MutiChooseSalersActivity.class);
      toChooseSaler.putExtra("hasReturn", true);
      toChooseSaler.putStringArrayListExtra(MutiChooseSalersActivity.INPUT_SALERS,
          (ArrayList) StringUtils.Str2List(user.getSeller_ids()));
      startActivityForResult(toChooseSaler, RESULT_SALERS);
    }
  }

  public void onChooseCoached() {
    if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
      Intent toChooseSaler = new Intent(getActivity(), MutiChooseCoachActivity.class);
      toChooseSaler.putExtra("hasReturn", true);
      toChooseSaler.putStringArrayListExtra(MutiChooseCoachActivity.INPUT_COACHES,
          (ArrayList<String>) StringUtils.coachIds(user.getCoaches()));
      startActivityForResult(toChooseSaler, RESULT_COACH);
    }
  }

  public void onRemarkClick() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("填写备注信息")
            .hint("")
            .content(TextUtils.isEmpty(remarks) ? user.getRemarks() : remarks)
            .build());
  }

  /**
   * 来源
   * 推荐人
   */
  public void onSourceAndReferrerClick(View view) {
    switch (view.getId()) {
      case R.id.ll_student_source:
        startActivityForResult(
            ChooseActivity.newIntent(getContext(), ChooseActivity.CHOOSE_SOURCE, ""),
            RESULT_ORIGIN);
        break;
      case R.id.ll_student_referrer:
        startActivityForResult(new Intent(getActivity(), ChooseReferrerActivity.class),
            RESULT_REFERRER);
        break;
    }
  }
}
