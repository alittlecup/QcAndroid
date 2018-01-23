package cn.qingchengfit.staffkit.views.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.custom.CitiesChooser;
import cn.qingchengfit.staffkit.views.custom.CitiesData;
import cn.qingchengfit.staffkit.views.custom.CityBean;
import cn.qingchengfit.staffkit.views.custom.DistrictBean;
import cn.qingchengfit.staffkit.views.custom.ProvinceBean;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.io.File;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/2/23 2016.
 */
public class FixSelfInfoFragment extends BaseDialogFragment implements CommonPView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.header_layout) RelativeLayout headerLayout;
    @BindView(R.id.username) CommonInputView username;
    @BindView(R.id.gender_male) RadioButton genderMale;
    @BindView(R.id.gender_female) RadioButton genderFemale;
    @BindView(R.id.course_type_rg) RadioGroup courseTypeRg;
    @BindView(R.id.gender_layout) RelativeLayout genderLayout;
    @BindView(R.id.phone) CommonInputView phone;
    @BindView(R.id.city) CommonInputView city;
    @BindView(R.id.comfirm) Button comfirm;
    @Inject FixSelfInofPresneter presneter;

    private String uploadImageUrl;
    private CitiesChooser citiesChooser;
    private Staff self;
    private boolean back = false;

    public static void start(Fragment fragment, int requestCode, Staff self) {
        BaseDialogFragment f = newInstance(self);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static FixSelfInfoFragment newInstance(Staff self) {
        Bundle args = new Bundle();
        args.putParcelable("self", self);
        FixSelfInfoFragment fragment = new FixSelfInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FixSelfInfoFragment newInstance(Staff self, boolean back) {
        Bundle args = new Bundle();
        args.putParcelable("self", self);
        args.putBoolean("back", back);
        FixSelfInfoFragment fragment = new FixSelfInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        citiesChooser = new CitiesChooser(getContext());
        if (getArguments() != null) {
            self = getArguments().getParcelable("self");
            back = getArguments().getBoolean("back");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fix_self_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presneter,this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (back) {
                    getActivity().finish();
                } else {
                    dismiss();
                }
            }
        });
        toolbarTitile.setText(getString(R.string.title_fix_self_info));
        initToolbarPadding(toolbar);
        Glide.with(getContext())
            .load(PhotoUtils.getMiddle(self.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(headerImg, getContext()));
        courseTypeRg.check((self.getGender() == 0) ? R.id.gender_male : R.id.gender_female);
        username.setContent(self.getUsername());
        courseTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_male) {
                    self.setGender(0);
                } else {
                    self.setGender(1);
                }
            }
        });
        if (self != null && self.getGd_district() != null && self.getGd_district().getId() > 0) {
            city.setContent(
                self.getGd_district().getProvince().getName() + self.getGd_district().getCity().getName() + self.getGd_district()
                    .getName());
            city.setTag(self.getGd_district().getId());
        } else {
            city.setContent("");
            city.setTag(0);
        }
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                if (TextUtils.isEmpty(eventChooseImage.filePath)) {
                  LogUtil.e("filePaht == null");
                }
                showLoading();
                Glide.with(getContext())
                    .load(new File(eventChooseImage.filePath))
                    .asBitmap()
                    .placeholder(R.drawable.img_loadingimage)
                    .into(new CircleImgWrapper(headerImg, getContext()));
                RxRegiste(UpYunClient.rxUpLoad("/avatar/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            hideLoading();
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .placeholder(R.drawable.img_loadingimage)
                                .into(new CircleImgWrapper(headerImg, getContext()));
                            uploadImageUrl = s;
                            self.setAvatar(s);
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            Glide.with(getContext())
                                .load(R.drawable.img_loadingimage)
                                .asBitmap()
                                .into(new CircleImgWrapper(headerImg, getContext()));
                            ToastUtils.show("图片上传失败，请重试");
                            hideLoading();
                        }
                    }));
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                Glide.with(getContext()).load(R.drawable.img_loadingimage).into(headerImg);
                ToastUtils.show("图片上传失败，请重试");
                hideLoading();
            }
        });

        return view;
    }

    @OnClick(R.id.header_layout) public void onHeaderClick() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.city) public void onCity() {
        citiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
            @Override public void onCityChoosen(String provice, String city, String district, int id) {
                String content;
                if (city.startsWith(provice)) {
                    content = provice + district;
                } else {
                    content = provice + city + district;
                }
                FixSelfInfoFragment.this.city.setContent(content);
                FixSelfInfoFragment.this.city.setTag(id);
            }
        });
        citiesChooser.show(getView());
    }

    @OnClick(R.id.comfirm) public void onOk() {
        if (TextUtils.isEmpty(username.getContent())) {
            ToastUtils.show("请填写用户名");
            return;
        }
        self.setUsername(username.getContent());
        if ((int) FixSelfInfoFragment.this.city.getTag() != 0) {
            self.setGd_district_id(String.valueOf((int) FixSelfInfoFragment.this.city.getTag()));
        } else {
            self.setGd_district_id(null);
        }
        self.setGd_district(null);
        presneter.commitSelfInfo(self);
    }

    @Override public void onDestroyView() {
        presneter.unattachView();
        super.onDestroyView();
    }

    @Override public void onSuccess() {
        ToastUtils.showS("修改成功");
        if (back) {
            getActivity().finish();
        } else {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
            dismiss();
        }
    }

    @Override public void onFailed(String s) {

    }

    public String getDistrict(int id) {
        if (id <= 0) {
            return "";
        }
        final CitiesData citiesData = (CitiesData) App.caches.get("cityData");
        if (citiesData != null) {
            for (int i = 0; i < citiesData.provinces.size(); i++) {
                ProvinceBean provinceBean = citiesData.provinces.get(i);
                for (int j = 0; j < provinceBean.cities.size(); j++) {
                    CityBean cityBean = provinceBean.cities.get(j);
                    for (int k = 0; k < cityBean.districts.size(); k++) {
                        DistrictBean districtBean = cityBean.districts.get(k);
                        if (districtBean.id == id) {
                            return new StringBuilder().append(provinceBean.name).append(cityBean.name).append(districtBean.name).toString();
                        }
                    }
                }
            }
        }
        return "";
    }
}
