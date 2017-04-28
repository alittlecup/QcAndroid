package com.qingchengfit.fitcoach.activity;

import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.PreferenceUtils;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.ScanBody;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QRActivity extends BaseAcitivity implements QRCodeReaderView.OnQRCodeReadListener {

    public static final String LINK_URL = "com.qingcheng.qr.linkurl";
    public static final String LINK_MODULE = "com.qingcheng.qr.module";
    public static final String MODULE_SETTING = "studio/setting";
//    public static final String MODULE_SETTING = "studio/setting";
    public static final String MODULE_MSG = "messagesetting";
    public static final String MODULE_PERMISSION = "permissionsetting";
    public static final String MODULE_MSG_SETTING = "messagesetting";
    public static final String MODULE_BODYTEST = "measureSetting";
    public static final String MODULE_COURSE_PLAN = "planssetting";
    public static final String MODULE_BILL = "pay/bills";
    public static final String MODULE_PAY_ONLINE = "pay/setting";
    public static final String MODULE_ACTIVITY = "activity/setting";
    public static final String MODULE_ALI = "koubei";
    public static final String MODULE_AD = "mobile/advertisement/setting";
    public static final String MODULE_NOTICE = "notice";
    public static final String MODULE_GIFT = "giftcard";
    public static final String MODULE_COMMODITY = "commodity/list";

    //    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitile;
    @BindView(R.id.done)
    LinearLayout done;
    @BindView(R.id.layout_next)
    LinearLayout layoutNext;
    @BindView(R.id.root_view)
    RelativeLayout rootView;
    private Subscription sp;
    private AlertDialogWrapper dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarTitile.setText("在电脑中使用该功能");
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        layoutNext.setVisibility(View.GONE);
        qrdecoderview = new QRCodeReaderView(this);
        rootView.addView(qrdecoderview,0,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        qrdecoderview.setOnQRCodeReadListener(this);
        qrdecoderview.getCameraManager().startPreview();
        toolbarTitile.setText("扫码二维码");
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (done.getVisibility() == View.GONE && layoutNext.getVisibility() == View.GONE && qrdecoderview != null) {
            qrdecoderview.getCameraManager().startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(qrdecoderview != null)
            qrdecoderview.getCameraManager().stopPreview();
    }

    @Override
    protected void onDestroy() {
        if (sp != null)
            sp.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        if(qrdecoderview != null)
            qrdecoderview.getCameraManager().stopPreview();
        final String session = PreferenceUtils.getPrefString(this, "session_id", "");
        sp = QcCloudClient.getApi().postApi.qcScans(text, new ScanBody.Builder()
                .url(getIntent().getStringExtra(LINK_URL))
                .session_id(session)
//                .module(getIntent().getStringExtra(LINK_MODULE))
//                .brand_id(getIntent().getStringExtra("brand_id"))
//                .shop_id(getIntent().getStringExtra("shop_id"))
                .build())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            done.setVisibility(View.VISIBLE);
                            toolbarTitile.setText("扫码成功");
                        } else {
                            new AlertDialogWrapper.Builder(QRActivity.this)
                                    .setTitle(R.string.err_sacn_qrcode)
                                    .setPositiveButton(R.string.comfirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(qrdecoderview != null){
                                                qrdecoderview.getCameraManager().startPreview();
                                                toolbarTitile.setText("扫码二维码");
                                            }
                                        }
                                    })
                                    .show();

                        }
                    }
                });

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }
}
