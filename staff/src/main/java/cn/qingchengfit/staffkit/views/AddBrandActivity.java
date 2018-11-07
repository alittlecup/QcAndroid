package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.gym.AddBrandView;
import cn.qingchengfit.staffkit.views.gym.CreateBrandPresenter;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by peggy on 16/5/26.
 */

public class AddBrandActivity extends BaseActivity implements AddBrandView {

	Button btn;
	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView brandPhoto;
    @Inject CreateBrandPresenter presenter;
	CommonInputView content;
    @Inject StaffRespository restRepository;
	ImageView guideStep1;
    private Subscription sp;
    private String uploadImg;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gym_add_brand);
      btn = (Button) findViewById(R.id.btn);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      brandPhoto = (ImageView) findViewById(R.id.brand_photo);
      content = (CommonInputView) findViewById(R.id.content);
      guideStep1 = (ImageView) findViewById(R.id.guide_step_1);
      findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });
      findViewById(R.id.photo_layout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          addPhoto();
        }
      });

      presenter.attachView(this);
        presenter.onNewSps();
        if (getIntent() != null && getIntent().getIntExtra(GymActivity.GYM_TO, -1) == GymActivity.GYM_GUIDE) {
            guideStep1.setVisibility(View.VISIBLE);
        }
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        toolbarTitile.setText(getString(R.string.title_brand));

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

 public void onComfirm() {
        if (!TextUtils.isEmpty(content.getContent().trim())) {
            showLoading();
            presenter.createBrand(content.getContent().trim(), uploadImg);
        }
    }

 public void addPhoto() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    showLoading();
                    sp = UpYunClient.rxUpLoad("brand/", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {

                            hideLoading();
                            uploadImg = s;
                            Glide.with(AddBrandActivity.this).load(PhotoUtils.getSmall(s)).into(brandPhoto);
                        }
                    });
                }
            }
        });
        choosePictureFragmentDialog.show(getSupportFragmentManager(), "");
    }

    @Override public void onSucceed(final CreatBrand creatBrand) {
        SensorsUtils.track("QcSaasCreateBrand")
          .commit(this);


        restRepository.getStaffAllApi()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                @Override public void call(QcDataResponse<BrandsResponse> qcResponseBrands) {
                    hideLoading();
                    if (ResponseConstant.checkSuccess(qcResponseBrands)) {
                        if (qcResponseBrands.data.brands.size() > 0) {
                            for (int i = 0; i < qcResponseBrands.data.brands.size(); i++) {
                                if (qcResponseBrands.data.brands.get(i).getId().equalsIgnoreCase(creatBrand.brand.getId())) {
                                    setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(i)));
                                    finish();
                                    return;
                                }
                            }

                            setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(0)));
                            finish();
                        } else {
                            ToastUtils.show("您没有品牌");
                        }
                    } else {
                        ToastUtils.show(qcResponseBrands.getMsg());
                    }
                }
            });
    }

    @Override public void onFailed(String s) {
        hideLoading();
        ToastUtils.show(s);
    }

    @Override protected void onDestroy() {
        //        if (getIntent().getIntExtra(GymActivity.GYM_TO,-1) == GymActivity.GYM_GUIDE){
        //            RxBus.getBus().post(new RxCloseAppEvent());
        //        }
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }
}
