package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.presenter.StaffDetailView;
import cn.qingchengfit.saasbase.staff.presenter.SuPresenter;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2016/12/27.
 */
@Leaf(module = "staff", path = "/su/")
public class SuFragment extends SaasBaseFragment implements StaffDetailView {

	TextView hint;
	ImageView headerImg;
	RelativeLayout headerLayout;
	CommonInputView username;
	RadioButton genderMale;
	RadioButton genderFemale;
	RadioGroup courseTypeRg;
	RelativeLayout genderLayout;
	PhoneEditText phoneNum;
	TextView changeSuBtn;
	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout toolbarLayout;
    @Inject SuPresenter mSuPresenter;
    //上传的照片
    private String uploadImg;
    @Need StaffShip mStaff;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_su_staff_saas, container, false);
      hint = (TextView) view.findViewById(R.id.hint);
      headerImg = (ImageView) view.findViewById(R.id.header_img);
      headerLayout = (RelativeLayout) view.findViewById(R.id.header_layout);
      username = (CommonInputView) view.findViewById(R.id.username);
      genderMale = (RadioButton) view.findViewById(R.id.gender_male);
      genderFemale = (RadioButton) view.findViewById(R.id.gender_female);
      courseTypeRg = (RadioGroup) view.findViewById(R.id.course_type_rg);
      genderLayout = (RelativeLayout) view.findViewById(R.id.gender_layout);
      phoneNum = (PhoneEditText) view.findViewById(R.id.phone_num);
      changeSuBtn = (TextView) view.findViewById(R.id.change_su_btn);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
      view.findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onHeader();
        }
      });
      view.findViewById(R.id.change_su_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          SuFragment.this.onClick(v);
        }
      });

      delegatePresenter(mSuPresenter, this);
        initToolbar(toolbar);
        username.setContent(mStaff.getUsername());
        phoneNum.setPhoneNum(mStaff.getPhone());
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(mStaff.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(headerImg, getContext()));
        courseTypeRg.check(mStaff.getGender() == 0 ? R.id.gender_male : R.id.gender_female);
        hint.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_grey), null, null,
            null);
        hint.setCompoundDrawablePadding(16);
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                RxRegiste(UpYunClient.rxUpLoad("course/", eventChooseImage.filePath).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        hideLoading();
                        Glide.with(getContext())
                            .load(PhotoUtils.getSmall(s))
                            .asBitmap()
                            .into(new CircleImgWrapper(headerImg, getContext()));
                        uploadImg = s;
                    }
                }));
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("超级管理员");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                ManagerBody.Builder builder = new ManagerBody.Builder();
                if (uploadImg != null) {
                    builder.avatar(uploadImg);
                } else {
                    builder.avatar(mStaff.getAvatar());
                }
                if (TextUtils.isEmpty(username.getContent())) {
                    ToastUtils.show("姓名不能为空");
                    return true;
                } else {
                    builder.username(username.getContent());
                }

                if (phoneNum.checkPhoneNum()) {
                    builder.phone(phoneNum.getPhoneNum()).area_code(phoneNum.getDistrictInt());
                } else {
                    return true;
                }

                builder.gender(courseTypeRg.getCheckedRadioButtonId() == R.id.gender_male ? 0 : 1);
                showLoading();
                mSuPresenter.onFixStaff(builder.build(), mStaff.id);
                return true;
            }
        });
    }

    @Override public String getFragmentName() {
        return SuFragment.class.getName();
    }

 public void onHeader(){
        ChoosePictureFragmentDialog.newInstance(true, 11).show(getFragmentManager(), "");
    }

 public void onClick(View view) {
        routeTo("/su/change/",SuIdendifyParams.builder().mStaff(mStaff).build());
    }

    @Override public void onFixSuccess() {
        hideLoading();
        ToastUtils.show("修改成功");
        getActivity().onBackPressed();
    }

    @Override public void onAddSuccess() {

    }

    @Override public void onDelSuccess() {

    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }

    @Override public void onPositions(List<StaffPosition> positions) {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
