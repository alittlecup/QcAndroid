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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.MutiChooseCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachBean;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.EditTextActivityIntentBuilder;
import cn.qingchengfit.staffkit.views.allotsales.choose.MutiChooseSalersActivity;
import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
import cn.qingchengfit.staffkit.views.gym.MutiChooseGymFragment;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
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
    public boolean isAdd = false;
    public User_Student user;
    @BindView(R.id.tv_student_source) TextView tvStudentSource;
    @BindView(R.id.ll_student_source) LinearLayout llStudentSource;
    @BindView(R.id.tv_student_referrer) TextView tvStudentReferrer;
    @BindView(R.id.ll_student_referrer) LinearLayout llStudentReferrer;
    @BindView(R.id.civ_remark) CommonInputView civRemark;
    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.civ_name) CommonInputView civName;
    @BindView(R.id.gender_male) RadioButton genderMale;
    @BindView(R.id.gender_female) RadioButton genderFemale;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    @BindView(R.id.gender_layout) RelativeLayout genderLayout;
    @BindView(R.id.civ_birthday) CommonInputView civBirthday;
    @BindView(R.id.civ_address) CommonInputView civAddress;
    @BindView(R.id.btn_save) Button btnSave;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.header_layout) RelativeLayout headerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.support_shops) CommonInputView supportShops;
    @BindView(R.id.civ_salers) CommonInputView mCivSalers;
    @BindView(R.id.tv_header) TextView tvHeader;
    @BindView(R.id.tv_add_hint) TextView tvAddHint;
    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @BindView(R.id.divider_edit_student) View divider;
    @BindView(R.id.margin_divider_edit_student) View marginDivider;
    @Inject EditStudentInfoPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @BindView(R.id.civ_coaches) CommonInputView civCoaches;
    private String shopid;

    private StudentReferrerBean referrerBean;
    private StudentSourceBean sourceBean;
    private String remarks;
    private List<CoachBean> coaches = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_student_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        if (!isAdd) {
            mCallbackActivity.setToolbar("修改学员", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    saveStudentInfo();
                    return false;
                }
            });
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
                    .load(user.getGender() == 0 ? Configs.HEADER_STUDENT_MALE : Configs.HEADER_STUDENT_FEMALE)
                    .asBitmap()
                    .into(new CircleImgWrapper(headerImg, getActivity()));
            } else {
                Glide.with(getActivity())
                    .load(PhotoUtils.getSmall(user.getAvatar()))
                    .asBitmap()
                    .into(new CircleImgWrapper(headerImg, getActivity()));
            }
        } else {
            mCallbackActivity.setToolbar("新增学员", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    saveStudentInfo();
                    return false;
                }
            });
            user = new User_Student();
            headerImg.setImageResource(R.drawable.ic_camara_grey);
            tvAddHint.setVisibility(View.VISIBLE);
            user.setShops(gymWrapper.shop_id());
            tvHeader.setText(getString(R.string.signin_img));

            if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
                mCivSalers.setClickable(false);
                mCivSalers.setContent(PreferenceUtils.getPrefString(App.context, Configs.PREFER_WORK_NAME, ""));
            }
        }
        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                user.setGender(checkedId == R.id.gender_male ? 0 : 1);
                if (TextUtils.isEmpty(user.getAvatar())) {
                    Glide.with(getActivity())
                        .load(checkedId == R.id.gender_male ? Configs.HEADER_STUDENT_MALE : Configs.HEADER_STUDENT_FEMALE)
                        .asBitmap()
                        .into(new CircleImgWrapper(headerImg, getActivity()));
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
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

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.civ_birthday) public void onBirthday() {
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

    @OnClick({ R.id.header_layout, R.id.btn_save }) public void onClick(View view) {
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
                                            Glide.with(getContext()).load(PhotoUtils.getSmall(s)).asBitmap().into(headerImg);
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
                //if (TextUtils.isEmpty(civName.getContent())) {
                //    ToastUtils.show("请填写学员姓名");
                //    return;
                //}
                //user.setUsername(civName.getContent());
                //user.setAddress(civAddress.getContent());
                //user.setRemarks(remarks);
                //user.setRecommend_by_id(referrerBean == null ? null : referrerBean.id);
                //user.setOrigin(sourceBean == null ? null : sourceBean.name);
                //List<String> idList = new ArrayList<>();
                //for (CoachBean coachBean : coaches) {
                //    idList.add(coachBean.id);
                //}
                //user.setCoach_ids(idList);
                //if (phoneNum.checkPhoneNum()) {
                //    user.setPhone(phoneNum.getPhoneNum());
                //    user.setArea_code(phoneNum.getDistrictInt());
                //} else {
                //    ToastUtils.show("请填写正确的电话");
                //    return;
                //}
                //saveStudentInfo();
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
        for (CoachBean coachBean : coaches) {
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

    @OnClick(R.id.support_shops) public void onSupportShops() {
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
                if (coaches != null) civCoaches.setContent(StringUtils.coachesNames(coaches));
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

    @OnClick(R.id.civ_salers) public void onClick() {
        if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
            Intent toChooseSaler = new Intent(getActivity(), MutiChooseSalersActivity.class);
            toChooseSaler.putExtra("hasReturn", true);
            toChooseSaler.putStringArrayListExtra(MutiChooseSalersActivity.INPUT_SALERS,
                (ArrayList) StringUtils.Str2List(user.getSeller_ids()));
            startActivityForResult(toChooseSaler, RESULT_SALERS);
        }
    }

    @OnClick(R.id.civ_coaches) public void onChooseCoached() {
        if (serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
            Intent toChooseSaler = new Intent(getActivity(), MutiChooseCoachActivity.class);
            toChooseSaler.putExtra("hasReturn", true);
            toChooseSaler.putStringArrayListExtra(MutiChooseCoachActivity.INPUT_COACHES,
                (ArrayList<String>) StringUtils.coachIds(user.getCoaches()));
            startActivityForResult(toChooseSaler, RESULT_COACH);
        }
    }

    @OnClick(R.id.civ_remark) public void onRemarkClick() {
        //
        Intent toAddOrigin = new EditTextActivityIntentBuilder("填写备注").build(getActivity());
        startActivityForResult(toAddOrigin, RESULT_ADD_REMARKS);
    }

    /**
     * 来源
     * 推荐人
     */
    @OnClick({ R.id.ll_student_source, R.id.ll_student_referrer }) public void onSourceAndReferrerClick(View view) {
        switch (view.getId()) {
            case R.id.ll_student_source:
                startActivityForResult(ChooseActivity.newIntent(getContext(), ChooseActivity.CHOOSE_SOURCE, ""), RESULT_ORIGIN);
                break;
            case R.id.ll_student_referrer:
                startActivityForResult(new Intent(getActivity(), ChooseReferrerActivity.class), RESULT_REFERRER);
                break;
        }
    }
}
