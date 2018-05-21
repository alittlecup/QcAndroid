package cn.qingchengfit.staffkit.views.gym.staff;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.responese.StaffPosition;
import cn.qingchengfit.model.responese.StaffShip;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.staff.event.EventAddStaffDone;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/5/12 2016.
 */
public class StaffDetailFragment extends BaseFragment implements StaffDetailView {

	ImageView headerImg;
	RelativeLayout headerLayout;
	CommonInputView username;
	RadioButton genderMale;
	RadioButton genderFemale;
	RadioGroup courseTypeRg;
	RelativeLayout genderLayout;
	CommonInputView position;
	RelativeLayout btnDel;
	Button btnAdd;
	TextView goToWeb;
	View denyLayout;
    @Inject StaffDetailPresenter presenter;
    @Inject GymFunctionFactory gymFunctionFactory;
    ManagerBody body = new ManagerBody();
	PhoneEditText phoneNum;
	Toolbar toolbar;
	TextView toolbarTitile;
    @Inject SerPermisAction serPermisAction;
    private boolean mIsAdd = true;
    private StaffShip mStaff;
    private List<String> mPositionStrList = new ArrayList<>();
    private List<StaffPosition> mPositions;

    public static StaffDetailFragment newInstance(StaffShip staffShip) {

        Bundle args = new Bundle();
        args.putParcelable("Staff", staffShip);
        StaffDetailFragment fragment = new StaffDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStaff = getArguments().getParcelable("Staff");
            if (mStaff != null) mIsAdd = false;
        }
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (mIsAdd) {
            toolbarTitile.setText("新增工作人员");
        } else {
            toolbarTitile.setText("工作人员详情");
            boolean editP = serPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_CHANGE);
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    if (TextUtils.isEmpty(body.getUsername())) {
                        ToastUtils.show("请填写姓名");
                        return true;
                    }
                    if (TextUtils.isEmpty(body.getPosition_id())) {
                        ToastUtils.show("请选择职位");
                        return true;
                    }
                    if (phoneNum.checkPhoneNum()) {
                        body.setArea_code(phoneNum.getDistrictInt());
                        showLoading();
                        presenter.onFixStaff(body);
                    }
                    return true;
                }
            });
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_detail, container, false);
      headerImg = (ImageView) view.findViewById(R.id.header_img);
      headerLayout = (RelativeLayout) view.findViewById(R.id.header_layout);
      username = (CommonInputView) view.findViewById(R.id.username);
      genderMale = (RadioButton) view.findViewById(R.id.gender_male);
      genderFemale = (RadioButton) view.findViewById(R.id.gender_female);
      courseTypeRg = (RadioGroup) view.findViewById(R.id.course_type_rg);
      genderLayout = (RelativeLayout) view.findViewById(R.id.gender_layout);
      position = (CommonInputView) view.findViewById(R.id.position);
      btnDel = (RelativeLayout) view.findViewById(R.id.btn_del);
      btnAdd = (Button) view.findViewById(R.id.btn_add);
      goToWeb = (TextView) view.findViewById(R.id.go_to_web);
      denyLayout = (View) view.findViewById(R.id.deny_layout);
      phoneNum = (PhoneEditText) view.findViewById(R.id.phone_num);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.deny_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onDeny();
        }
      });
      view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          StaffDetailFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          StaffDetailFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onHeader();
        }
      });
      view.findViewById(R.id.position).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          StaffDetailFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.go_to_web).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          StaffDetailFragment.this.onClickGoWeb();
        }
      });

      delegatePresenter(presenter, this);
        initToolbar(toolbar);
        if (mIsAdd) {
            mCallbackActivity.setToolbar("新增工作人员", false, null, 0, null);
            btnDel.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            //            goToWeb.setVisibility(View.VISIBLE);
        } else {
            boolean editP = serPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_CHANGE);
            if (!editP) {
                denyLayout.setVisibility(View.VISIBLE);
            }

            btnAdd.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                .load(PhotoUtils.getSmall(mStaff.user.getAvatar()))
                .asBitmap()
                .into(new CircleImgWrapper(headerImg, getContext()));
            username.setContent(mStaff.user.getUsername());
            try {
                courseTypeRg.check(mStaff.user.getGender() == 0 ? R.id.gender_male : R.id.gender_female);
            } catch (Exception e) {

            }

            phoneNum.setPhoneNum(mStaff.user.getPhone());
            phoneNum.setdistrictInt(mStaff.user.getArea_code());
            body.setId(mStaff.user.getId());
            body.setUsername(mStaff.user.getUsername());
            body.setPhone(mStaff.user.getPhone());
            body.setPosition_id(mStaff.position.id);
        }
        presenter.queryPostions();
        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                body.setPhone(s.toString());
            }
        });
        username.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                body.setUsername(s.toString());
            }
        });
        if (mStaff == null || TextUtils.isEmpty(mStaff.user.getAvatar())) {
            Glide.with(StaffDetailFragment.this)
                .load(Configs.HEADER_COACH_MALE)
                .asBitmap()
                .into(new CircleImgWrapper(headerImg, getActivity()));
        }
        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_male) {
                    body.setGender(0);
                } else {
                    body.setGender(1);
                }
                if (mStaff == null || TextUtils.isEmpty(mStaff.user.getAvatar())) {
                    Glide.with(StaffDetailFragment.this)
                        .load(checkedId == R.id.gender_male ? Configs.HEADER_COACH_MALE : Configs.HEADER_COACH_FEMALE)
                        .asBitmap()
                        .into(new CircleImgWrapper(headerImg, getActivity()));
                }
            }
        });
        SpannableString ss = new SpannableString(getString(R.string.hint_longin_web));
        ss.setSpan(new ForegroundColorSpan(CompatUtils.getColor(getContext(), R.color.text_orange)), 4, 7,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色

        goToWeb.setText(ss, TextView.BufferType.SPANNABLE);
        showLoading();

        return view;
    }

 public void onDeny() {
        showAlert(R.string.alert_permission_forbid);
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_del:
                if (!serPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_DELETE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }

                DialogUtils.instanceDelDialog(getActivity(), "确定删除此工作人员?", new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showLoading();
                        presenter.onDelStaff(mStaff.id);
                    }
                }).show();

                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(body.getPhone()) || body.getPhone().length() != 11) {
                    ToastUtils.show("请填写正确的手机号码");
                    return;
                }

                if (TextUtils.isEmpty(body.getUsername())) {
                    ToastUtils.show("请填写姓名");
                    return;
                }
                if (TextUtils.isEmpty(body.getPosition_id())) {
                    ToastUtils.show("请选择职位");
                    return;
                }
                body.setArea_code(phoneNum.getDistrictInt());
                showLoading();
                presenter.onAddStaff(body);
                break;
        }
    }

 public void onHeader() {
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    showLoading();

                    RxRegiste(UpYunClient.rxUpLoad("header/", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            hideLoading();
                            Glide.with(getActivity())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .into(new CircleImgWrapper(headerImg, getActivity()));
                            body.setAvatar(s);
                        }
                    }));
                } else {

                }
            }
        });
        dialog.show(getFragmentManager(), "");
    }

 public void onClick() {
        if (mPositionStrList.size() > 0) {
            final DialogList dialogList = new DialogList(getContext());
            dialogList.list(mPositionStrList, new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialogList.dismiss();
                    body.setPosition_id(mPositions.get(position).id);
                    StaffDetailFragment.this.position.setContent(mPositions.get(position).name);
                }
            });
            dialogList.show();
        } else {
            presenter.queryPostions();
            ToastUtils.show("获取不到职位列表,请重试");
        }
    }

    @Override public void onFixSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onAddSuccess() {
        hideLoading();
        RxBus.getBus().post(new EventAddStaffDone());
        getActivity().onBackPressed();
    }

    @Override public void onDelSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onFailed(String s) {
        hideLoading();
    }

    @Override public void onPositions(List<StaffPosition> positions) {
        hideLoading();
        mPositions = positions;
        mPositionStrList.clear();
        for (int i = 0; i < positions.size(); i++) {
            mPositionStrList.add(positions.get(i).name);
        }
        if (mPositions.size() > 0) {
            if (mStaff != null && mStaff.position != null) {
                for (StaffPosition p : mPositions) {
                    if (p.id.equals(mStaff.position.id)) {
                        position.setContent(p.name);
                        break;
                    }
                }
            } else {
                position.setContent(mPositions.get(0).name);
                body.setPosition_id(mPositions.get(0).id);
            }
        }
    }

 public void onClickGoWeb() {
        gymFunctionFactory.goQrScan(this, QRActivity.PERMISSION_STAFF, null, null);


    }
}
