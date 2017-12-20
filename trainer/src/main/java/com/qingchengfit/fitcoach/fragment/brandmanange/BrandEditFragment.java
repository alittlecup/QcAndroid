package com.qingchengfit.fitcoach.fragment.brandmanange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.activity.BrandManageActivity;
import cn.qingchengfit.bean.BrandBody;
import cn.qingchengfit.events.EventChooseImage;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
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
 * Created by Paper on 16/7/14.
 */
public class BrandEditFragment extends BaseFragment {

    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.brand_name) CommonInputView brandName;
    @BindView(R.id.change_creator) CommonInputView changeCreator;
    @BindView(R.id.del) TextView del;

    BrandBody postBrand = new BrandBody();
    @BindView(R.id.brand_id) CommonInputView brandId;
    @BindView(R.id.brand_create) CommonInputView brandCreateTime;
    private Brand brand;

    public static BrandEditFragment newInstance(Brand bran) {

        Bundle args = new Bundle();
        args.putParcelable("brand", bran);
        BrandEditFragment fragment = new BrandEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_brand, container, false);
        unbinder = ButterKnife.bind(this, view);
        //((BrandManageComponent) mCallbackActivity.getComponent()).inject(this);
        if (getArguments() != null) {
            brand = getArguments().getParcelable("brand");

            if (brand != null) {
                postBrand.name = brand.getName();
                postBrand.photo = brand.getPhoto();
                if (getActivity() instanceof BrandManageActivity) {
                    ((BrandManageActivity) getActivity()).settoolbar("修改品牌信息", R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
                        @Override public boolean onMenuItemClick(MenuItem item) {
                            postBrand.name = brandName.getContent();
                            if (TextUtils.isEmpty(postBrand.name)) {
                                ToastUtils.show("请填写品牌名称");
                                return true;
                            }
                            showLoading();

                            RxRegiste(QcCloudClient.getApi().postApi.qcEditBrand(brand.getId(), postBrand)
                                .observeOn(AndroidSchedulers.mainThread())
                                .onBackpressureBuffer()
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<QcResponse>() {
                                    @Override public void call(QcResponse qcResponse) {
                                        hideLoading();
                                        if (ResponseConstant.checkSuccess(qcResponse)) {
                                            ToastUtils.show("修改成功");
                                            getActivity().onBackPressed();
                                        } else {
                                            ToastUtils.show("修改失败");
                                        }
                                    }
                                }));

                            return false;
                        }
                    });
                }

                brandName.setContent(brand.getName());
                brandId.setHint(brand.getCname());
                String creator = "";
                if (brand.getCreated_by() != null) {
                    creator = brand.getCreated_by().getUsername();
                }
                changeCreator.setContent(creator);
                brandCreateTime.setHint(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(brand.getCreated_at())));
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(brand.getPhoto()))
                    .asBitmap()
                    .placeholder(R.drawable.ic_default_header)
                    .into(new CircleImgWrapper(headerImg, getContext()));
            }

            //RxRegiste(restRepository.getGet_api().qcGetBrand(App.staffId, brand.getId())
          //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
            //        .observeOn(AndroidSchedulers.mainThread())
            //        .subscribe(new Action1<QcResponseData<BrandResponse>>() {
            //            @Override
            //            public void call(QcResponseData<BrandResponse> qcResponseBrands) {
            //                if (ResponseConstant.checkSuccess(qcResponseBrands)) {
            //                    brandName.setContent(qcResponseBrands.data.brand.getName());
            //                    Glide.with(getContext()).load(PhotoUtils.getSmall(brand.getPhoto())).asBitmap().into(new CircleImgWrapper(headerImg, getContext()));
            //
            //                }
            //            }
            //        })
            //);

        }

        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                showLoading();
                RxRegiste(UpYunClient.rxUpLoad("header/", eventChooseImage.filePath).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        Glide.with(getContext())
                            .load(PhotoUtils.getSmall(s))
                            .asBitmap()
                            .into(new CircleImgWrapper(headerImg, getContext()));
                        postBrand.photo = s;
                        hideLoading();
                    }
                }));
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return BrandEditFragment.class.getName();
    }

    @Override public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
    }

    @OnClick({ R.id.header_layout, R.id.change_creator, R.id.del }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_layout:
                ChoosePictureFragmentDialog f = ChoosePictureFragmentDialog.newInstance();
                f.show(getFragmentManager(), "");
                break;
            case R.id.change_creator:
                getFragmentManager().beginTransaction()
                    .replace(R.id.frag, BrandCreatorEditFragment.newInstance(brand))
                    .addToBackStack(getFragmentName())
                    .commit();
                break;
            case R.id.del:
                new MaterialDialog.Builder(getContext()).content(R.string.contact_gm)
                    .autoDismiss(true)
                    .canceledOnTouchOutside(true)
                    .negativeText(R.string.pickerview_cancel)
                    .positiveText(R.string.comfirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PhoneFuncUtils.callPhone(getContext(), getString(R.string.qingcheng_gm_phone));
                        }
                    })
                    .show();
                break;
        }
    }
}
