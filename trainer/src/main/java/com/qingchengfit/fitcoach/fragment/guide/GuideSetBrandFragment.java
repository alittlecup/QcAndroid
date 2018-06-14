package com.qingchengfit.fitcoach.fragment.guide;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.CoachInitBean;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.saasbase.network.body.CreatBrandBody;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
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

	ImageView brandImg;
	CommonInputView brandName;
	Button nextStep;
    String imgUrl = "";


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_brand, container, false);
      brandImg = (ImageView) view.findViewById(R.id.brand_img);
      brandName = (CommonInputView) view.findViewById(R.id.brand_name);
      nextStep = (Button) view.findViewById(R.id.next_step);
      view.findViewById(R.id.layout_brand_logo).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickBrandLogo();
        }
      });
      view.findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          GuideSetBrandFragment.this.onClick(v);
        }
      });

      RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                UpYunClient.rxUpLoad("brand/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            hideLoading();
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(s))
                                .asBitmap()
                                .into(new CircleImgWrapper(brandImg, getContext()));
                            imgUrl = s;
                        }
                    },new HttpThrowable());
            }
        });
        //if (App.gUser != null && App.gUser.id != null) {
        //    RxRegiste(QcCloudClient.getApi().getApi.qcGetBrands(App.gUser.id + "")
      //        .onBackpressureBuffer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Action1<QcResponseBrands>() {
        //            @Override public void call(QcResponseBrands qcResponse) {
        //                if (ResponseConstant.checkSuccess(qcResponse)) {
        //                    if (qcResponse.data != null && qcResponse.data.brands.size() > 0) {
        //                        Intent toChooseBrand = new Intent(getActivity(), ChooseBrandActivity.class);
        //                        startActivity(toChooseBrand);
        //                        getActivity().finish();
        //                    }
        //                }
        //            }
        //        }, new Action1<Throwable>() {
        //            @Override public void call(Throwable throwable) {
        //            }
        //        }));
        //}

        return view;
    }

 public void onClickBrandLogo() {
        ChoosePictureFragmentDialog.newInstance().show(getFragmentManager(), "");

        new RxPermissions(getActivity())
            .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean aBoolean) {
                    if (aBoolean) {
                    } else {
                        ToastUtils.showDefaultStyle(getString(R.string.permission_request_camara));
                    }
                }
            });
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_step:
                if (brandName.isEmpty()) {
                    ToastUtils.showDefaultStyle(getString(R.string.alert_write_brand_name));
                    return;
                }
                showLoading();
                QcCloudClient.getApi().postApi.qcCreatBrand(new CreatBrandBody.Builder().name(brandName.getContent()).photo(imgUrl).build())
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(qcResponsCreatBrand -> {
                        hideLoading();
                        if (qcResponsCreatBrand.status == 200) {

                            getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                                .replace(R.id.guide_frag, new GuideSetGymFragmentBuilder(imgUrl, brandName.getContent(),
                                    qcResponsCreatBrand.data.brand.getId()).build())
                                .addToBackStack(null)
                                .commit();
                            if (getParentFragment() instanceof GuideFragment) {
                                ((GuideFragment) getParentFragment()).initBean.brand_id = qcResponsCreatBrand.data.brand.getId();
                                RxBus.getBus().post(new CoachInitBean());
                            }
                        } else {
                            ToastUtils.showDefaultStyle(qcResponsCreatBrand.msg);
                        }
                    }, throwable -> {
                        hideLoading();
                    });

                break;
        }
    }
}
