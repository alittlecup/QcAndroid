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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.responese.StaffPosition;
import cn.qingchengfit.model.responese.StaffShip;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
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

    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.header_layout) RelativeLayout headerLayout;
    @BindView(R.id.username) CommonInputView username;
    @BindView(R.id.gender_male) RadioButton genderMale;
    @BindView(R.id.gender_female) RadioButton genderFemale;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    @BindView(R.id.gender_layout) RelativeLayout genderLayout;
    @BindView(R.id.position) CommonInputView position;
    @BindView(R.id.btn_del) RelativeLayout btnDel;
    @BindView(R.id.btn_add) Button btnAdd;
    @BindView(R.id.go_to_web) TextView goToWeb;
    @BindView(R.id.deny_layout) View denyLayout;
    @Inject StaffDetailPresenter presenter;
    ManagerBody body = new ManagerBody();
    @BindView(R.id.phone_num) PhoneEditText phoneNum;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private boolean mIsAdd = true;
    private StaffShip mStaff;
    private List<String> mPositionStrList = new ArrayList<>();
    private List<StaffPosition> mPostions;

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
            boolean editP = SerPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_CHANGE);
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
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        if (mIsAdd) {
            mCallbackActivity.setToolbar("新增工作人员", false, null, 0, null);
            btnDel.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            //            goToWeb.setVisibility(View.VISIBLE);
        } else {
            boolean editP = SerPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_CHANGE);
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

    @OnClick(R.id.deny_layout) public void onDeny() {
        showAlert(R.string.alert_permission_forbid);
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.btn_del, R.id.btn_add }) public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_del:
                if (!SerPermisAction.checkAll(PermissionServerUtils.MANAGE_STAFF_CAN_DELETE)) {
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

    @OnClick(R.id.header_layout) public void onHeader() {
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

    @OnClick(R.id.position) public void onClick() {
        if (mPositionStrList.size() > 0) {
            final DialogList dialogList = new DialogList(getContext());
            dialogList.list(mPositionStrList, new AdapterView.OnItemClickListener() {
                @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialogList.dismiss();
                    body.setPosition_id(mPostions.get(position).id);
                    StaffDetailFragment.this.position.setContent(mPostions.get(position).name);
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
        mPostions = positions;
        mPositionStrList.clear();
        for (int i = 0; i < positions.size(); i++) {
            mPositionStrList.add(positions.get(i).name);
        }
        if (mPostions.size() > 0) {
            if (mStaff != null && mStaff.position != null) {
                for (StaffPosition p : mPostions) {
                    if (p.id.equals(mStaff.position.id)) {
                        position.setContent(p.name);
                        break;
                    }
                }
            } else {
                position.setContent(mPostions.get(0).name);
                body.setPosition_id(mPostions.get(0).id);
            }
        }
    }

    @OnClick(R.id.go_to_web) public void onClickGoWeb() {
        GymFunctionFactory.goQrScan(this, GymFunctionFactory.PERMISSION_STAFF, null, null);
        //        new MaterialDialog.Builder(getContext()).autoDismiss(true)
        //                .content("网页端地址\n" +
        //                        "http://cloud.qingchengfit.cn/backend/settings/")
        //                .positiveText(R.string.common_comfirm)
        //                .negativeText(R.string.copy_link)
        //                .autoDismiss(true)
        //                .onNegative(new MaterialDialog.SingleButtonCallback() {
        //                    @Override
        //                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //                        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        //                        cmb.setText("http://cloud.qingchengfit.cn/backend/settings/");
        //                        ToastUtils.showS("已复制");
        //                    }
        //                })
        //                .show();
    }
}
