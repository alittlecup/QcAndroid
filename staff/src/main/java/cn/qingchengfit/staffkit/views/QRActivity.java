package cn.qingchengfit.staffkit.views;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.body.ScanBody;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QRActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

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
    public static final String MODULE_MODIFY_CARD_PROTOCOL = "term/edit";
    public static final String MODULE_ADD_CARD_PROTOCOL = "term/add";


    //    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;

    @Inject QcRestRepository restRepository;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.done) LinearLayout done;
    @BindView(R.id.layout_next) LinearLayout layoutNext;
    @BindView(R.id.root_view) RelativeLayout rootView;
    private Subscription sp;
    private AlertDialogWrapper dialog;
    @Inject GymWrapper gymWrapper;

    /**
     * @param module Web相应模块的后缀
     */
    public static void start(Context context,String module) {
        Intent starter = new Intent(context, QRActivity.class);
        starter.putExtra(LINK_MODULE, module);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        View v = new View(this);
        v.setBackgroundResource(R.color.toolbar);
        //rootView.addView(v,0,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //    MeasureUtils.getStatusBarHeight(this)));
        toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        toolbarTitile.setText("在电脑中使用该功能");
        //        qrdecoderview.setOnQRCodeReadListener(this);
    }

    @OnClick(R.id.btn_next) public void onClickNext() {
        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
                if (aBoolean) {
                    layoutNext.setVisibility(View.GONE);
                    qrdecoderview = new QRCodeReaderView(QRActivity.this);
                    rootView.addView(qrdecoderview, 0,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    qrdecoderview.setOnQRCodeReadListener(QRActivity.this);
                    qrdecoderview.getCameraManager().startPreview();
                    toolbarTitile.setText("扫码二维码");
                } else {
                    ToastUtils.show("请打开摄像头权限");
                }
            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        if (done.getVisibility() == View.GONE && layoutNext.getVisibility() == View.GONE && qrdecoderview != null) {
            qrdecoderview.getCameraManager().startPreview();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
    }

    @Override protected void onDestroy() {
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }

    @Override public void onQRCodeRead(final String text, PointF[] points) {
        if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
        final String session = PreferenceUtils.getPrefString(this, Configs.PREFER_SESSION, "");
        String url = "";
        if (getIntent() != null && getIntent().hasExtra(LINK_MODULE)) {
            url = getBaseContext().getResources()
                .getString(R.string.qr_code_2web, Configs.Server, gymWrapper.brand_id(),
                    gymWrapper.shop_id(), getIntent().getStringExtra(LINK_MODULE));
        }else if (getIntent() != null && getIntent().hasExtra(LINK_URL)){
            url = getIntent().getStringExtra(LINK_URL);
        }
        sp = restRepository.createPostApi(Post_Api.class).qcScans(text, new ScanBody.Builder().url(url).session_id(session)
            //                .module(getIntent().getStringExtra(LINK_MODULE))
            //                .brand_id(getIntent().getStringExtra("brand_id"))
            //                .shop_id(getIntent().getStringExtra("shop_id"))
            .build())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        done.setVisibility(View.VISIBLE);
                        toolbarTitile.setText("扫码成功");
                    } else {
                        new AlertDialogWrapper.Builder(QRActivity.this).setTitle(R.string.err_sacn_qrcode)
                            .setPositiveButton(R.string.common_comfirm, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) {
                                    if (qrdecoderview != null) {
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

    @Override public void cameraNotFound() {

    }

    @Override public void QRCodeNotFoundOnCamImage() {

    }
}
