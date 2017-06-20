package com.qingchengfit.fitcoach.fragment.guide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.event.EventLoginChange;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.bean.QcResponseSystenInit;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.tbruyelle.rxpermissions.RxPermissions;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/11/10.
 */
@FragmentWithArgs public class GuideSetGymFragment extends BaseFragment {
    @Arg public String brandid;
    @Arg public String brandImgUrl;
    @Arg public String brandNameStr;
    public String imgUrl;
    @BindView(R.id.brand_img) protected ImageView brandImg;
    @BindView(R.id.brand_name) protected TextView brandName;
    @BindView(R.id.gym_img) protected ImageView gymImg;
    @BindView(R.id.gym_name) protected CommonInputView gymName;
    @BindView(R.id.next_step) protected Button nextStep;
    @BindView(R.id.gym_address) protected CommonInputView gymAddress;
    @BindView(R.id.hint) protected TextView hint;
    @BindView(R.id.layout_brand) protected LinearLayout layoutBrand;

    protected double lat;
    protected double lng;
    protected int city_code;
    @Inject RepoCoachServiceImpl repoCoachService;
    private Unbinder unbinder;
    private String addressStr;
    private String gymNameStr;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_setgym, container, false);
        unbinder = ButterKnife.bind(GuideSetGymFragment.this, view);


        Glide.with(getContext())
            .load(PhotoUtils.getSmall(brandImgUrl))
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(brandImg, getContext()));
        brandName.setText(brandNameStr);
        String initStr = PreferenceUtils.getPrefString(getContext(), "initSystem", "");
        CoachInitBean initBean;
        if (initStr == null || initStr.isEmpty()) {
            initBean = new CoachInitBean();
        } else {
            initBean = new Gson().fromJson(initStr, CoachInitBean.class);
        }

        if (initBean.shop != null) {
            gymName.setContent(initBean.shop.name);
            gymNameStr = initBean.shop.name;
            gymAddress.setContent(initBean.shop.address);
            addressStr = initBean.shop.address;
            Glide.with(getContext())
                .load(PhotoUtils.getSmall(initBean.shop.photo))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(gymImg, getContext()));
        }

        RxBus.getBus().post(new EventStep.Builder().step(0).build());
        RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
            @Override public void call(EventAddress eventAddress) {
                gymAddress.setContent(eventAddress.address);
                addressStr = eventAddress.address;
                city_code = eventAddress.city_code;
                lat = eventAddress.lat;
                lng = eventAddress.log;
            }
        });

        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                UpYunClient.rxUpLoad("gym/", eventChooseImage.filePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            hideLoading();
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .into(new CircleImgWrapper(gymImg, getContext()));
                            imgUrl = s;
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                        }
                    });
            }
        });
        initData();
        return view;
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        gymName.setContent(gymNameStr);
        gymAddress.setContent(addressStr);
    }

    public void initData() {

    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.layout_brand, R.id.layout_gym_img, R.id.gym_address }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_brand:
                //Intent toChooseBrand = new Intent(getActivity(), ChooseBrandActivity.class);
                //startActivityForResult(toChooseBrand, 1);
                break;
            case R.id.layout_gym_img:
                ChoosePictureFragmentDialog.newInstance(true).show(getFragmentManager(), "");
                break;
            case R.id.gym_address:
                new RxPermissions(getActivity())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                Intent toAddress = new Intent(getActivity(), ChooseActivity.class);
                                startActivity(toAddress);
                            } else {
                                ToastUtils.showDefaultStyle("请开启定位权限");
                            }
                        }
                    });

                break;
            //            case R.id.next_step:
            //
            //                break;
        }
    }

    @OnClick(R.id.next_step) public void onNextStep() {
        if (gymAddress.isEmpty()) {
            ToastUtils.showDefaultStyle(getString(R.string.err_write_address));
            return;
        }
        if (gymName.isEmpty()) {
            ToastUtils.showDefaultStyle(getString(R.string.err_write_gym_name));
            return;
        }
        if (getParentFragment() instanceof GuideFragment) {
            ((GuideFragment) getParentFragment()).initBean.shop = new Shop.Builder().address(gymAddress.getContent())
                .gd_district_id(city_code + "")
                .name(gymName.getContent())
                .gd_lat(lat)
                .gd_lng(lng)
                .photo(imgUrl)
                .build();
            gymNameStr = gymName.getContent();
            RxBus.getBus().post(new CoachInitBean());
            showLoading();
            RxRegiste(QcCloudClient.getApi().postApi.qcInit(((GuideFragment) getParentFragment()).getInitBean())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseSystenInit>() {
                    @Override public void call(QcResponseSystenInit qcResponse) {
                        hideLoading();
                        if (qcResponse.status == 200) {
                            repoCoachService.createService((CoachService) qcResponse.data);
                            PreferenceUtils.setPrefString(getContext(), "initSystem", "");
                            if (getActivity().getIntent().getBooleanExtra("workexp", false)) {
                                Intent r = new Intent();
                                r.putExtra("gym", qcResponse.data);
                                getActivity().setResult(Activity.RESULT_OK, r);
                            } else {
                                PreferenceUtils.setPrefString(getContext(), "coachservice_id", qcResponse.data.getId());
                                RxBus.getBus().post(qcResponse.data);
                                RxBus.getBus().post(new EventLoginChange());
                                Intent toMain = new Intent(getActivity(), Main2Activity.class);
                                toMain.putExtra(Main2Activity.ACTION, Main2Activity.INIT);
                                toMain.putExtra("service", qcResponse.data);
                                startActivity(toMain);
                            }
                            getActivity().finish();
                        } else {
                            ToastUtils.showDefaultStyle(qcResponse.msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        hideLoading();
                        ToastUtils.showDefaultStyle("创建场馆失败!");
                    }
                }));
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                Brand brand = (Brand) IntentUtils.getParcelable(data);
                brandImgUrl = brand.getPhoto();
                brandNameStr = brand.getName();
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(brand.getPhoto()))
                    .asBitmap()
                    .into(new CircleImgWrapper(brandImg, getContext()));
                brandName.setText(brand.getName());
                if (getParentFragment() instanceof GuideFragment) {
                    ((GuideFragment) getParentFragment()).initBean.brand_id = brand.getId();
                    RxBus.getBus().post(new CoachInitBean());
                }
            }
        }
    }
}
