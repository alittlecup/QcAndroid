package cn.qingchengfit.staffkit.views.gym.coach;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.widgets.PhoneEditText;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/11 2016.
 */
public class CoachDetailFragment extends BaseFragment implements CoachDetailView {

	ImageView headerImg;
	CommonInputView civName;
	RadioButton genderMale;
	RadioButton genderFemale;
	RadioGroup courseTypeRg;
	RelativeLayout genderLayout;

    @Inject EditCoachInfoPresenter presenter;
    @Inject SerPermisAction serPermisAction;
	TextView downTrainerApp;
	RelativeLayout headerLayout;
	RelativeLayout btnDel;
	View denyLayout;
	PhoneEditText phoneNum;
	Toolbar toolbar;
	TextView toolbarTitile;

    private boolean isAdd = false;
    private Staff mCoach;

    public static CoachDetailFragment newInstance() {
        CoachDetailFragment fragment = new CoachDetailFragment();
        return fragment;
    }

    public static CoachDetailFragment newInstance(Staff student) {

        Bundle args = new Bundle();
        args.putParcelable("data", student);
        CoachDetailFragment fragment = new CoachDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoach = getArguments().getParcelable("data");
        } else {

        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_coach_info, container, false);
      headerImg = (ImageView) view.findViewById(R.id.header_img);
      civName = (CommonInputView) view.findViewById(R.id.civ_name);
      genderMale = (RadioButton) view.findViewById(R.id.gender_male);
      genderFemale = (RadioButton) view.findViewById(R.id.gender_female);
      courseTypeRg = (RadioGroup) view.findViewById(R.id.course_type_rg);
      genderLayout = (RelativeLayout) view.findViewById(R.id.gender_layout);
      downTrainerApp = (TextView) view.findViewById(R.id.down_trainer_app);
      headerLayout = (RelativeLayout) view.findViewById(R.id.header_layout);
      btnDel = (RelativeLayout) view.findViewById(R.id.btn_del);
      denyLayout = (View) view.findViewById(R.id.deny_layout);
      phoneNum = (PhoneEditText) view.findViewById(R.id.phone_num);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.down_trainer_app).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          CoachDetailFragment.this.onClick();
        }
      });
      view.findViewById(R.id.deny_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onDeny();
        }
      });
      view.findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          CoachDetailFragment.this.onClick();
        }
      });
      view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          CoachDetailFragment.this.onClick();
        }
      });

      delegatePresenter(presenter, this);
        initToolbar(toolbar);
        boolean eP = serPermisAction.checkAll(PermissionServerUtils.COACHSETTING_CAN_CHANGE);

        denyLayout.setVisibility(eP ? View.GONE : View.VISIBLE);
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(mCoach.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(headerImg, getContext()));
        civName.setContent(mCoach.getUsername());
        courseTypeRg.check(mCoach.gender == 0 ? R.id.gender_male : R.id.gender_female);
        phoneNum.setPhoneNum(mCoach.phone);
        phoneNum.setdistrictInt(mCoach.area_code);
        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                mCoach.phone = s.toString();
            }
        });
        civName.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                mCoach.username = s.toString().trim();
            }
        });
        if (TextUtils.isEmpty(mCoach.avatar)) {
            Glide.with(CoachDetailFragment.this)
                .load(Configs.HEADER_COACH_MALE)
                .asBitmap()
                .into(new CircleImgWrapper(headerImg, getActivity()));
        }
        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_male) {
                    mCoach.gender = 0;
                } else {
                    mCoach.gender = 1;
                }
                if (TextUtils.isEmpty(mCoach.avatar)) {
                    Glide.with(CoachDetailFragment.this)
                        .load(checkedId == R.id.gender_male ? Configs.HEADER_COACH_MALE : Configs.HEADER_COACH_FEMALE)
                        .asBitmap()
                        .into(new CircleImgWrapper(headerImg, getActivity()));
                }
            }
        });

        SpannableString ss = new SpannableString(getString(R.string.down_trainer_app_hint));
        Drawable d;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          d = getResources().getDrawable(R.drawable.ic_laucher_trainer, null);
        } else {
          d = getResources().getDrawable(R.drawable.ic_laucher_trainer);
        }
        if (d != null) d.setBounds(0, 0, MeasureUtils.dpToPx(15f, getResources()), MeasureUtils.dpToPx(15f, getResources()));
        ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_orange)), 20, 29,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色

        downTrainerApp.setText(ss, TextView.BufferType.SPANNABLE);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        boolean eP = serPermisAction.checkAll(PermissionServerUtils.COACHSETTING_CAN_CHANGE);
        toolbarTitile.setText("教练详情");
        if (eP) {
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (TextUtils.isEmpty(mCoach.username)) {
                        ToastUtils.show("请填写正确姓名");
                        return true;
                    }
                    //                mCoach.area_code = phoneNum.getDistrictInt();
                    if (phoneNum.checkPhoneNum()) {
                        mCoach.area_code = phoneNum.getDistrictInt();
                        presenter.editCoach(mCoach);
                    }
                    return true;
                }
            });
        }
    }

 public void onClick() {
        WebActivity.startWebForResult(Configs.DOWNLOAD_TRAINER, getContext(), 444);
    }

    @Override public String getFragmentName() {
        return EditStudentInfoFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onDeny() {
        showAlert(R.string.alert_permission_forbid);
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_layout:
                ChoosePictureFragmentDialog f = ChoosePictureFragmentDialog.newInstance();
                f.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                    @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                        if (isSuccess) {
                            showLoading();
                            CoachDetailFragment.this.RxRegiste(UpYunClient.rxUpLoad("coach/", filePath).subscribe(new Action1<String>() {
                                @Override public void call(String s) {
                                    hideLoading();
                                    Glide.with(getContext()).load(s).asBitmap().into(new CircleImgWrapper(headerImg, getContext()));
                                    mCoach.avatar = s;
                                }
                            }, new Action1<Throwable>() {
                                @Override public void call(Throwable throwable) {
                                    ToastUtils.show("上传图片失败");
                                    hideLoading();
                                }
                            }));
                        }
                    }
                });
                f.show(getFragmentManager(), "");
                break;
            case R.id.btn_del:

                if (!serPermisAction.checkAll(PermissionServerUtils.COACHSETTING_CAN_DELETE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }

                DialogUtils.instanceDelDialog(getContext(), getString(R.string.alert_del_coach), new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.delCoach(mCoach.id);
                    }
                }).show();
                break;
        }
    }

    @Override public void onFixSuccess() {
        ToastUtils.showS("修改成功!");
        getActivity().onBackPressed();
    }

    @Override public void onDelSuccess() {
        getActivity().onBackPressed();
    }

    @Override public void onFailed() {

    }
}
