package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.CreatBrandBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddBrandActivity extends BaseAcitivity {

    @BindView(R.id.btn) Button btn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.brand_photo) ImageView brandPhoto;
    @BindView(R.id.content) CommonInputView content;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;
    @BindView(R.id.photo_layout) RelativeLayout photoLayout;
    private Subscription sp;
    private String uploadImg;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);
        ButterKnife.bind(this);
        // TODO: 16/11/10 引导流程时直接返回
        //        if (getIntent()!= null && getIntent().getIntExtra(GymActivity.GYM_TO,-1) == GymActivity.GYM_GUIDE)
        //            onSucceed();
        toolbarTitle.setText("添加健身房品牌");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        content.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn.setEnabled(true);
                } else {
                    btn.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.btn) public void onComfirm() {
        QcCloudClient.getApi().postApi.qcCreatBrand(new CreatBrandBody.Builder()
            .name(content.getContent())
            .photo(uploadImg)
            .build())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponsCreatBrand -> {
                hideLoading();
                if (qcResponsCreatBrand.status == 200) {
                   AddBrandActivity.this.finish();
                } else ToastUtils.showDefaultStyle(qcResponsCreatBrand.msg);
            }, throwable -> {
                hideLoading();
            });


    }

    @OnClick(R.id.photo_layout) public void addPhoto() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    showLoading();
                    sp = UpYunClient.rxUpLoad("brand/", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {

                            hideLoading();
                            uploadImg = s;
                            Glide.with(AddBrandActivity.this).load(PhotoUtils.getSmall(s)).asBitmap().into(new CircleImgWrapper(brandPhoto,AddBrandActivity.this));
                        }
                    });
                }
            }
        });
        choosePictureFragmentDialog.show(getSupportFragmentManager(), "");
    }

    public void onSucceed() {
        //        restRepository.getGet_api().qcGetBrands(App.staffId)
        //                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Action1<QcResponseBrands>() {
        //                    @Override
        //                    public void call(QcResponseBrands qcResponseBrands) {
        //                        if (ResponseConstant.checkSuccess(qcResponseBrands)){
        //                            if (qcResponseBrands.data.brands.size() > 0){
        //                                setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(0)));
        //                                finish();
        //                            }else {
        //                            }
        //                        }
        //                    }
        //                });

    }

    public void onFailed(String s) {
        //        ToastUtils.show(s);
    }

    @Override protected void onDestroy() {
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }
}
