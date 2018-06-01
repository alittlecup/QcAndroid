package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.staffkit.rxbus.event.LoadingEvent;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.io.File;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/2/22 2016.
 */
public class ReportFragment extends BaseDialogFragment implements CommonPView {

    @Inject ReportPresenter presenter;
	EditText settingAdviceMail;
	EditText settingAdviceContent;
	ImageView adviceRightArrow;
	ImageView adviceUpdateImg;
	RelativeLayout adviceUpdate;
	Button settingAdviceBtn;
	Toolbar toolbar;
	TextView toolbarTitile;

    private String uploadImageUrl = "";

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ReportFragment newInstance() {

        Bundle args = new Bundle();

        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
      settingAdviceMail = (EditText) view.findViewById(R.id.setting_advice_mail);
      settingAdviceContent = (EditText) view.findViewById(R.id.setting_advice_content);
      adviceRightArrow = (ImageView) view.findViewById(R.id.advice_right_arrow);
      adviceUpdateImg = (ImageView) view.findViewById(R.id.advice_update_img);
      adviceUpdate = (RelativeLayout) view.findViewById(R.id.advice_update);
      settingAdviceBtn = (Button) view.findViewById(R.id.setting_advice_btn);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.setting_advice_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAdvice();
        }
      });
      view.findViewById(R.id.advice_update).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          upImage();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText("意见反馈");
        presenter.attachView(this);
        return view;
    }

 public void onAdvice() {
        if (TextUtils.isEmpty(settingAdviceMail.getText()) || TextUtils.isEmpty(settingAdviceContent.getText())) {
            ToastUtils.show(getString(R.string.err_advice));
            return;
        }
        presenter.postReport(
            new FeedBackBody(settingAdviceMail.getText().toString().trim(), settingAdviceContent.getText().toString().trim(),
                uploadImageUrl));
    }

 public void upImage() {
        RxBus.getBus().post(new LoadingEvent(true));
        ChoosePictureFragmentDialog dialog = new ChoosePictureFragmentDialog();
        dialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, final String filePath) {
                if (isSuccess) {
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override public void call(Subscriber<? super String> subscriber) {
                            String upImg = UpYunClient.upLoadImg("report/", new File(filePath));
                            subscriber.onNext(upImg);
                        }
                    })
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                        @Override public void onCompleted() {

                        }

                        @Override public void onError(Throwable e) {
                            RxBus.getBus().post(new LoadingEvent(false));
                        }

                        @Override public void onNext(String upImg) {
                            if (TextUtils.isEmpty(upImg)) {
                                ToastUtils.showDefaultStyle("图片上传失败");
                            } else {
                                Glide.with(getContext()).load(PhotoUtils.getSmall(upImg)).into(adviceUpdateImg);
                                uploadImageUrl = upImg;
                            }
                            RxBus.getBus().post(new LoadingEvent(false));
                        }
                    });
                } else {
                    ToastUtils.showDefaultStyle("图片上传失败");
                }
            }
        });
        dialog.show(getFragmentManager(), "");
    }

    @Override public void onDestroyView() {
        if (presenter != null) presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onSuccess() {
        dismiss();
    }

    @Override public void onFailed(String s) {

    }
}
