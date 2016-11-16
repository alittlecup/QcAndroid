package com.qingchengfit.fitcoach.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.BitmapUtils;
import cn.qingchengfit.widgets.utils.ChoosePicUtils;
import cn.qingchengfit.widgets.utils.FileUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceFragment extends BaseSettingFragment {


    @BindView(R.id.setting_advice_mail)
    EditText settingAdviceMail;
    @BindView(R.id.setting_advice_content)
    EditText settingAdviceContent;
    @BindView(R.id.advice_update)
    RelativeLayout adviceUpdate;
    @BindView(R.id.advice_update_img)
    ImageView adviceUpdateImg;
    private FeedBackBean feedBackBean;
    private DialogSheet dialogSheet;
    private String filepath;
    private Unbinder unbinder;

    public AdviceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "意见反馈");
        feedBackBean = new FeedBackBean();

        return view;
    }

    @OnClick(R.id.setting_advice_btn)
    public void onAdvice() {
        String email = settingAdviceMail.getText().toString();
        String content = settingAdviceContent.getText().toString();
        feedBackBean.setEmail(email);
        feedBackBean.setContent(content);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(content)) {
            fragmentCallBack.ShowLoading("请稍后");
            QcCloudClient.getApi().postApi.qcFeedBack(feedBackBean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<QcEvaluateResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            fragmentCallBack.hideLoading();

                            Toast.makeText(getContext(), "提交失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(QcEvaluateResponse qcEvaluateResponse) {
                            fragmentCallBack.hideLoading();
                            if (qcEvaluateResponse.status == ResponseResult.SUCCESS) {
                                Toast.makeText(getContext(), "感谢您的反馈,我们会继续努力", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(getContext(), "服务器错误,请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @OnClick(R.id.advice_update)
    public void onAddImg() {
        if (adviceUpdateImg.getVisibility() == View.VISIBLE) {
            if (dialogSheet == null)
                dialogSheet = DialogSheet.builder(getContext())
                        .addButton("重新上传", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSheet.hide();
                                uploadImg();
                            }
                        })
                        .addButton("删除照片", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSheet.hide();
                                feedBackBean.setPhoto("");
                                adviceUpdateImg.setVisibility(View.GONE);

                            }
                        });
            dialogSheet.show();
        } else {
            uploadImg();
        }
    }

    public void uploadImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
        } else {
            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (settingAdviceMail == null)
//            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY)
                filepath = FileUtils.getPath(getActivity(), data.getData());
            LogUtil.d(filepath);
            fragmentCallBack.ShowLoading("正在上传");
            Observable.just(filepath)
                    .subscribeOn(Schedulers.io())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
                        File upFile = new File(Configs.ExternalCache + filename);

                        boolean reslut = UpYunClient.upLoadImg("/advice/", filename, upFile);
                        getActivity().runOnUiThread(() -> {
                            fragmentCallBack.hideLoading();
                            if (reslut) {
                                LogUtil.d("success");
                                String pppurl = UpYunClient.UPYUNPATH + "advice/" + filename + ".png";

                                Glide.with(App.AppContex).load(PhotoUtils.getSmall(pppurl)).into(adviceUpdateImg);
                                adviceUpdateImg.setVisibility(View.VISIBLE);


                                feedBackBean.setPhoto(pppurl);

                            } else {
                                //upload failed TODO
                                LogUtil.d("update img false");
                            }
                        });
                    });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
