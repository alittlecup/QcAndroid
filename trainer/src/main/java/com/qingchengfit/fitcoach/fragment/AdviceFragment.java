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




import cn.qingchengfit.utils.ChoosePicUtils;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.DialogSheet;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdviceFragment extends BaseSettingFragment {

	EditText settingAdviceMail;
	EditText settingAdviceContent;
	RelativeLayout adviceUpdate;
	ImageView adviceUpdateImg;
    private FeedBackBean feedBackBean;
    private DialogSheet dialogSheet;
    private String filepath;

    private Subscription spUpImg;

    public AdviceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);
      settingAdviceMail = (EditText) view.findViewById(R.id.setting_advice_mail);
      settingAdviceContent = (EditText) view.findViewById(R.id.setting_advice_content);
      adviceUpdate = (RelativeLayout) view.findViewById(R.id.advice_update);
      adviceUpdateImg = (ImageView) view.findViewById(R.id.advice_update_img);
      view.findViewById(R.id.setting_advice_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAdvice();
        }
      });
      view.findViewById(R.id.advice_update).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAddImg();
        }
      });

      fragmentCallBack.onToolbarMenu(0, 0, "意见反馈");
        feedBackBean = new FeedBackBean();

        return view;
    }

 public void onAdvice() {
        String email = settingAdviceMail.getText().toString();
        String content = settingAdviceContent.getText().toString();
        feedBackBean.setEmail(email);
        feedBackBean.setContent(content);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(content)) {
            fragmentCallBack.ShowLoading("请稍后");
            QcCloudClient.getApi().postApi.qcFeedBack(feedBackBean)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcEvaluateResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        fragmentCallBack.hideLoading();

                        Toast.makeText(getContext(), "提交失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(QcEvaluateResponse qcEvaluateResponse) {
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

 public void onAddImg() {
        if (adviceUpdateImg.getVisibility() == View.VISIBLE) {
            if (dialogSheet == null) {
                dialogSheet = DialogSheet.builder(getContext()).addButton("重新上传", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialogSheet.hide();
                        uploadImg();
                    }
                }).addButton("删除照片", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialogSheet.hide();
                        feedBackBean.setPhoto("");
                        adviceUpdateImg.setVisibility(View.GONE);
                    }
                });
            }
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

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        if (settingAdviceMail == null)
        //            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY) filepath = FileUtils.getPath(getActivity(), data.getData());
            LogUtil.d(filepath);
            fragmentCallBack.ShowLoading("正在上传");

            spUpImg = UpYunClient.rxUpLoad("advice/", filepath)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        fragmentCallBack.hideLoading();
                        Glide.with(App.AppContex).load(PhotoUtils.getSmall(s)).into(adviceUpdateImg);
                        adviceUpdateImg.setVisibility(View.VISIBLE);
                        feedBackBean.setPhoto(s);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                });
            //Observable.just(filepath)
          //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
            //        .subscribe(s -> {
            //            String filename = UUID.randomUUID().toString();
            //            BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
            //            File upFile = new File(Configs.ExternalCache + filename);
            //
            //            boolean reslut = UpYunClient.upLoadImg("/advice/", filename, upFile);
            //            getActivity().runOnUiThread(() -> {
            //
            //                if (reslut) {
            //                    LogUtil.d("success");
            //                    String pppurl = UpYunClient.UPYUNPATH + "advice/" + filename + ".png";
            //
            //
            //
            //                } else {
            //                    //upload failed TODO
            //                    LogUtil.d("update img false");
            //                }
            //            });
            //        });

        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        if (spUpImg != null && spUpImg.isUnsubscribed()) {
            spUpImg.unsubscribe();
        }
    }
}
