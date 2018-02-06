//package cn.qingchengfit.staffkit.views.gym.staff;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.qingchengfit.events.EventChooseImage;
//import cn.qingchengfit.model.base.Staff;
//import cn.qingchengfit.model.body.ManagerBody;
//import cn.qingchengfit.model.responese.StaffPosition;
//import cn.qingchengfit.staffkit.App;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.views.custom.PhoneEditText;
//import cn.qingchengfit.utils.ToastUtils;
//import cn.qingchengfit.utils.UpYunClient;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
//import cn.qingchengfit.widgets.CommonInputView;
//import com.bumptech.glide.Glide;
//import com.hannesdorfmann.fragmentargs.FragmentArgs;
//import com.hannesdorfmann.fragmentargs.annotation.Arg;
//import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
//import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
//import com.tencent.qcloud.timchat.widget.PhotoUtils;
//import java.util.List;
//import javax.inject.Inject;
//import rx.functions.Action1;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2016/12/27.
// */
//@FragmentWithArgs public class SuFragment extends BaseFragment implements StaffDetailView {
//    @Arg Staff mStaff;
//
//    @BindView(R.id.hint) TextView hint;
//    @BindView(R.id.header_img) ImageView headerImg;
//    @BindView(R.id.header_layout) RelativeLayout headerLayout;
//    @BindView(R.id.username) CommonInputView username;
//    @BindView(R.id.gender_male) RadioButton genderMale;
//    @BindView(R.id.gender_female) RadioButton genderFemale;
//    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
//    @BindView(R.id.gender_layout) RelativeLayout genderLayout;
//    @BindView(R.id.phone_num) PhoneEditText phoneNum;
//    @BindView(R.id.change_su_btn) TextView changeSuBtn;
//    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
//    @Inject SuPresenter mSuPresenter;
//    //上传的照片
//    private String uploadImg;
//
//    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FragmentArgs.inject(this);
//    }
//
//    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_su_staff, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        delegatePresenter(mSuPresenter, this);
//        initToolbar(toolbar);
//        username.setContent(mStaff.getUsername());
//        phoneNum.setPhoneNum(mStaff.getPhone());
//        Glide.with(getContext())
//            .load(PhotoUtils.getSmall(mStaff.getAvatar()))
//            .asBitmap()
//            .into(new CircleImgWrapper(headerImg, getContext()));
//        courseTypeRg.check(mStaff.getGender() == 0 ? R.id.gender_male : R.id.gender_female);
//        hint.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_grey), null, null,
//            null);
//        hint.setCompoundDrawablePadding(16);
//        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
//            @Override public void call(EventChooseImage eventChooseImage) {
//                showLoading();
//                RxRegiste(UpYunClient.rxUpLoad("course/", eventChooseImage.filePath).subscribe(new Action1<String>() {
//                    @Override public void call(String s) {
//                        hideLoading();
//                        Glide.with(getContext())
//                            .load(PhotoUtils.getSmall(s))
//                            .asBitmap()
//                            .into(new CircleImgWrapper(headerImg, getContext()));
//                        uploadImg = s;
//                    }
//                }));
//            }
//        });
//        return view;
//    }
//
//    @Override public void initToolbar(@NonNull Toolbar toolbar) {
//        super.initToolbar(toolbar);
//        toolbarTitile.setText("超级管理员");
//        toolbar.inflateMenu(R.menu.menu_save);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override public boolean onMenuItemClick(MenuItem item) {
//                ManagerBody.Builder builder = new ManagerBody.Builder();
//                if (uploadImg != null) {
//                    builder.avatar(uploadImg);
//                } else {
//                    builder.avatar(mStaff.getAvatar());
//                }
//                if (TextUtils.isEmpty(username.getContent())) {
//                    ToastUtils.show("姓名不能为空");
//                    return true;
//                } else {
//                    builder.username(username.getContent());
//                }
//
//                if (phoneNum.checkPhoneNum()) {
//                    builder.phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt());
//                } else {
//                    return true;
//                }
//
//                builder.gender(courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? 0 : 1);
//                showLoading();
//                mSuPresenter.onFixStaff(builder.build(), App.staffId, mStaff.id);
//                return true;
//            }
//        });
//    }
//
//    @Override public String getFragmentName() {
//        return SuFragment.class.getName();
//    }
//
//    @OnClick({ R.id.header_layout, R.id.change_su_btn }) public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.header_layout:
//                ChoosePictureFragmentDialog.newInstance(true, 11).show(getFragmentManager(), "");
//                break;
//            case R.id.change_su_btn:
//                getFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
//                    .replace(mCallbackActivity.getFragId(), new SuIdendifyFragmentBuilder(mStaff).build())
//                    .addToBackStack(getFragmentName())
//                    .commit();
//                break;
//        }
//    }
//
//    @Override public void onFixSuccess() {
//        hideLoading();
//        ToastUtils.show("修改成功");
//    }
//
//    @Override public void onAddSuccess() {
//
//    }
//
//    @Override public void onDelSuccess() {
//
//    }
//
//    @Override public void onFailed(String s) {
//        hideLoading();
//        ToastUtils.show(s);
//    }
//
//    @Override public void onPositions(List<StaffPosition> positions) {
//
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//    }
//}
