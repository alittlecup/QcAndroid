package cn.qingchengfit.staffkit.views.setting.brand;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.body.BrandBody;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.BrandResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
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
 * Created by Paper on 16/7/14.
 */
public class BrandEditFragment extends BaseFragment {

    @BindView(R.id.header_img) ImageView headerImg;
    @BindView(R.id.brand_name) CommonInputView brandName;
    @BindView(R.id.change_creator) CommonInputView changeCreator;
    @BindView(R.id.del) TextView del;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @Inject RestRepository restRepository;

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

        initBus();
        initToolbar(toolbar);
        if (getArguments() != null) {
            brand = getArguments().getParcelable("brand");

            if (brand != null) {
                postBrand.name = brand.getName();
                postBrand.photo = brand.getPhoto();
                //toolbarTitle.setText("修改品牌信息");
                mCallbackActivity.setBar(new ToolbarBean("修改品牌信息",false,null,R.menu.menu_save,new Toolbar.OnMenuItemClickListener() {
                    @Override public boolean onMenuItemClick(MenuItem item) {
                        postBrand.name = brandName.getContent();
                        if (TextUtils.isEmpty(postBrand.name)) {
                            ToastUtils.show("请填写品牌名称");
                            return true;
                        }
                        showLoading();

                        RxRegiste(restRepository.getPost_api()
                          .qcEditBrand(brand.getId(), postBrand)
                          .observeOn(AndroidSchedulers.mainThread())
                          .onBackpressureBuffer()
                          .subscribeOn(Schedulers.io())
                          .subscribe(new Action1<QcResponse>() {
                              @Override public void call(QcResponse qcResponse) {
                                  hideLoading();
                                  if (ResponseConstant.checkSuccess(qcResponse)) {
                                      ToastUtils.show("修改成功");
                                      getActivity().getSupportFragmentManager()
                                        .popBackStackImmediate(BrandManageFragment.TAG,
                                          FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                  } else {
                                      ToastUtils.show("修改失败");
                                  }
                              }
                          }));

                        return true;
                    }
                }));

                brandName.setContent(brand.getName());
                brandId.setHint(brand.cname);
                String creator = "";
                if (brand.getCreated_by() != null) {
                    creator = brand.getCreated_by().getUsername();
                }
                changeCreator.setContent(creator);
                brandCreateTime.setHint(DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(brand.created_at)));
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(brand.getPhoto()))
                    .asBitmap()
                    .placeholder(R.drawable.ic_default_header)
                    .into(new CircleImgWrapper(headerImg, getContext()));
            }

            RxRegiste(restRepository.getGet_api()
                .qcGetBrand(App.staffId, brand.getId())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<BrandResponse>>() {
                    @Override public void call(QcDataResponse<BrandResponse> qcResponseBrands) {
                        if (ResponseConstant.checkSuccess(qcResponseBrands)) {
                            brandName.setContent(qcResponseBrands.data.brand.getName());
                            Glide.with(getContext())
                                .load(PhotoUtils.getSmall(brand.getPhoto()))
                                .asBitmap()
                                .into(new CircleImgWrapper(headerImg, getContext()));
                        }
                    }
                }));
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

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        ((ViewGroup)toolbar.getParent()).setVisibility(View.GONE);
    }

    private void initBus(){
        RxBusAdd(ChangeOwenerEvent.class)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ChangeOwenerEvent>() {
                @Override public void call(ChangeOwenerEvent changeOwenerEvent) {
                    if (changeOwenerEvent != null) {
                        changeCreator.setContent(changeOwenerEvent.getBody().username);
                        brand.created_by.setUsername(changeOwenerEvent.getBody().username);
                        brand.created_by.setGender(changeOwenerEvent.getBody().gender);
                        brand.created_by.setPhone(changeOwenerEvent.getBody().phone);
                    }
                }
            });
    }

    @Override public String getFragmentName() {
        return BrandManageFragment.TAG;
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
                    .replace(mCallbackActivity.getFragId(), BrandCreatorEditFragment.newInstance(brand))
                    .addToBackStack(getFragmentName())
                    .commit();
                break;
            case R.id.del:
                new MaterialDialog.Builder(getContext()).content("是否删除该品牌？")
                    .autoDismiss(true)
                    .canceledOnTouchOutside(true)
                    .negativeText(R.string.pickerview_cancel)
                    .positiveText(R.string.common_comfirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showLoading();
                            RxRegiste(restRepository.getPost_api()
                                .qcDelbrand(brand.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .onBackpressureBuffer()
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<QcResponse>() {
                                    @Override public void call(QcResponse qcResponse) {
                                        if (ResponseConstant.checkSuccess(qcResponse)) {
                                            hideLoading();
                                            ToastUtils.show("删除品牌成功！");
                                            PreferenceUtils.setPrefString(getContext(), Configs.CUR_BRAND_ID, "");
                                            BusinessUtils.reOpenApp(getActivity());
                                        } else {
                                            hideLoading();
                                        }
                                    }
                                }, new NetWorkThrowable()));
                        }
                    })
                    .show();
                break;
        }
    }
}
