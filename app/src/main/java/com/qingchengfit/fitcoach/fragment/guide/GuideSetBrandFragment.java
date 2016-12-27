package com.qingchengfit.fitcoach.fragment.guide;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseBrandActivity;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.CreatBrandBody;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
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
public class GuideSetBrandFragment extends BaseFragment {

    @BindView(R.id.brand_img)
    ImageView brandImg;
    @BindView(R.id.brand_name)
    CommonInputView brandName;
    @BindView(R.id.next_step)
    Button nextStep;
    String imgUrl = "";
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_brand, container, false);
        unbinder=ButterKnife.bind(this, view);
        RxBusAdd(EventChooseImage.class)
                .subscribe(new Action1<EventChooseImage>() {
                    @Override
                    public void call(EventChooseImage eventChooseImage) {
                        showLoading();
                        UpYunClient.rxUpLoad("brand/", eventChooseImage.filePath)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        hideLoading();
                                        Glide.with(getContext()).load(
                                            PhotoUtils.getSmall(s)).asBitmap().into(new CircleImgWrapper(brandImg, getContext()));
                                        imgUrl = s;
                                    }
                                });
                    }
                });
        if (App.gUser != null && App.gUser.id != null) {
            RxRegiste(QcCloudClient.getApi().getApi.qcGetBrands(App.gUser.id + "")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseBrands>() {
                    @Override public void call(QcResponseBrands qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            if (qcResponse.data != null && qcResponse.data.brands.size() > 0) {
                                Intent toChooseBrand = new Intent(getActivity(), ChooseBrandActivity.class);
                                startActivity(toChooseBrand);
                                getActivity().finish();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                    }
                }));
        }


        return view;
    }

    @OnClick(R.id.layout_brand_logo)
    public void onClickBrandLogo() {
        ChoosePictureFragmentDialog.newInstance().show(getFragmentManager(), "");

        RxPermissions.getInstance(getContext())
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                        } else {
                            ToastUtils.showDefaultStyle(getString(R.string.permission_request_camara));
                        }
                    }
                });
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_step:
                if (brandName.isEmpty()) {
                    ToastUtils.showDefaultStyle(getString(R.string.alert_write_brand_name));
                    return;
                }
                showLoading();
                QcCloudClient.getApi().postApi.qcCreatBrand(new CreatBrandBody.Builder()
                        .name(brandName.getContent())
                        .photo(imgUrl)
                        .build())
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(qcResponsCreatBrand -> {
                            hideLoading();
                            if (qcResponsCreatBrand.status == 200) {

                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                                        .replace(R.id.guide_frag, new GuideSetGymFragmentBuilder(imgUrl, brandName.getContent(), qcResponsCreatBrand.data.brand.getId()).build())
                                        .addToBackStack(null)
                                        .commit();
                                if (getParentFragment() instanceof GuideFragment) {
                                    ((GuideFragment) getParentFragment()).initBean.brand_id = qcResponsCreatBrand.data.brand.getId();
                                    RxBus.getBus().post(new CoachInitBean());
                                }

                            } else ToastUtils.showDefaultStyle(qcResponsCreatBrand.msg);
                        }, throwable -> {
                            hideLoading();
                        });


                break;
        }
    }
}
