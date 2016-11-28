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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.ChooseBrandActivity;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventAddress;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.bean.base.Shop;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.UpYunClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.tbruyelle.rxpermissions.RxPermissions;
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
@FragmentWithArgs
public class GuideSetGymFragment extends BaseFragment {
    @Arg
    public String brandid;
    @Arg
    public String brandImgUrl;
    @Arg
    public String brandNameStr;

    @BindView(R.id.brand_img)
    ImageView brandImg;
    @BindView(R.id.brand_name)
    TextView brandName;
    @BindView(R.id.gym_img)
    ImageView gymImg;
    @BindView(R.id.gym_name)
    CommonInputView gymName;
    @BindView(R.id.next_step)
    Button nextStep;
    @BindView(R.id.gym_address)
    CommonInputView gymAddress;

    private double lat;
    private double lng;
    private int city_code;
    private String imgUrl;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_setgym, container, false);
        unbinder = ButterKnife.bind(GuideSetGymFragment.this, view);
        Glide.with(getContext()).load(brandImgUrl).asBitmap().into(new CircleImgWrapper(brandImg, getContext()));
        brandName.setText(brandNameStr);
        RxBus.getBus().post(new EventStep.Builder().step(0).build());
        RxBusAdd(EventAddress.class)
                .subscribe(new Action1<EventAddress>() {
                    @Override
                    public void call(EventAddress eventAddress) {
                        gymAddress.setContent(eventAddress.address);
                        // TODO: 16/11/14 设置城市和latlng
                        city_code = eventAddress.city_code;
                        lat = eventAddress.lat;
                        lng = eventAddress.log;

                    }
                });
        RxBusAdd(EventChooseImage.class)
                .subscribe(new Action1<EventChooseImage>() {
                    @Override
                    public void call(EventChooseImage eventChooseImage) {
                        UpYunClient.rxUpLoad("gym/", eventChooseImage.filePath)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        Glide.with(getContext()).load(s).into(gymImg);
                                        imgUrl = s;
                                    }
                                });
                    }
                });

        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_brand, R.id.layout_gym_img, R.id.gym_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_brand:
                Intent toChooseBrand = new Intent(getActivity(), ChooseBrandActivity.class);
                startActivityForResult(toChooseBrand, 1);
                break;
            case R.id.layout_gym_img:
                ChoosePictureFragmentDialog.newInstance().show(getFragmentManager(), "");
                break;
            case R.id.gym_address:
                RxPermissions.getInstance(getContext())
                    .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean){
                                Intent toAddress = new Intent(getActivity(), ChooseActivity.class);
                                startActivity(toAddress);
                            }
                            else ToastUtils.showDefaultStyle("请开启定位权限");
                        }
                    });

                break;
//            case R.id.next_step:
//
//                break;
        }
    }
    @OnClick(R.id.next_step)
    public void onNextStep(){
        if (gymAddress.isEmpty()){
            ToastUtils.showDefaultStyle(getString(R.string.err_write_address));
            return;
        }
        if (gymName.isEmpty() || lat == 0 || lng == 0 ||city_code == 0){
            ToastUtils.showDefaultStyle(getString(R.string.err_write_gym_name));
            return;
        }
        if (getParentFragment() instanceof GuideFragment){
            ((GuideFragment) getParentFragment()).initBean.shop = new Shop.Builder()
                    .address(gymAddress.getContent())
                    .gd_district_id(city_code+"")
                    .name(gymName.getContent())
                    .gd_lat(lat)
                    .gd_lng(lng)
                    .photo(imgUrl)
                    .build();
            RxBus.getBus().post(new CoachInitBean());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
                    .replace(R.id.guide_frag,new GuideSetCourseFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                Brand brand = (Brand) IntentUtils.getParcelable(data);
                brandImgUrl = brand.getPhoto();
                brandNameStr = brand.getName();
                Glide.with(getContext()).load(brand.getPhoto()).asBitmap().into(new CircleImgWrapper(brandImg, getContext()));
                brandName.setText(brand.getName());
                if (getParentFragment() instanceof GuideFragment) {
                    ((GuideFragment) getParentFragment()).initBean.brand_id = brand.getId();
                    RxBus.getBus().post(new CoachInitBean());
                }

            }
        }
    }
}
