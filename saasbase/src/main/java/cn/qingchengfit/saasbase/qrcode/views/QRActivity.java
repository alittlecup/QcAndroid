package cn.qingchengfit.saasbase.qrcode.views;

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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.event.OnBackEvent;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.qrcode.model.ScanBody;
import cn.qingchengfit.saasbase.repository.PostApi;
import cn.qingchengfit.utils.MeasureUtils;
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
    public static final String MODULE_PAY_CASH = "/pay/bills";

    public static final String MODULE_NONE = "";
    /**
     * 工作台
     */
    public static final String MODULE_WORKSPACE_ORDER_LIST = "/orders/day";
    public static final String MODULE_WORKSPACE_GROUP = "/group/order";
    public static final String MODULE_WORKSPACE_PRIVATE = "/private/order";
    public static final String MODULE_WORKSPACE_ORDER_SIGNIN = "/checkin/help";

    /**
     * 健身房服务
     */
    public static final String MODULE_SERVICE_GROUP = "/teamarrange/calendar2";
    public static final String MODULE_SERVICE_PRIVATE = "/priarrange/calendar2";
    public static final String MODULE_SERVICE_FREE = "/checkin/setting";
    public static final String MODULE_SERVICE_SHOP = "/commodity/list";
    /**
     * 会员
     */
    public static final String MODULE_STUDENT = "/manage/members";
    public static final String MODULE_STUDENT_CARDS = "/manage/costs";
    public static final String MODULE_STUDENT_BODY_TEST = "/measure/setting";

    /**
     * 内部管理
     */
    public static final String MODULE_MANAGE_STAFF = "/manage/staff";
    public static final String MODULE_MANAGE_STAFF_ADD = "/manage/staff/add/";
    public static final String MODULE_MANAGE_COACH = "/coachsetting";
    /**
     * 运营推广
     */
    public static final String MODULE_OPERATE_SCORE = "/score/rank";
    public static final String MODULE_OPERATE_ACTIVITY = "/activity/setting";
    public static final String MODULE_OPERATE_AD = "/shop/home/setting";
    public static final String MODULE_OPERTAT_KOUBEI = "/koubei";
    public static final String MODULE_OPERATE_REGIST = "/giftcard";
    public static final String MODULE_OPERATE_ANOUNCE = "/notice";
    public static final String MODULE_OPERATE_COMPETITION = "/competition";

    /**
     * 财务
     */
    public static final String MODULE_FINACE_ONLINE = "/pay/bills";
    public static final String MODULE_FINANCE_COURSE = "/cost/report";
    public static final String MODULE_FINANCE_SALE = "/sales/report";
    public static final String MODULE_FINANCE_MARK = "/comments/report";
    public static final String MODULE_FINANCE_CARD = "/card/report";
    public static final String MODULE_FINANCE_SIGN_IN = "/checkin/report";

    /**
     * 场馆信息
     */
    public static final String MODULE_GYM_INFO = "/studio/setting?mb_module=info";//场馆管理
    public static final String MODULE_GYM_TIME = "/studio/setting?mb_module=time";
    public static final String MODULE_GYM_SITE = "/space/setting";
    public static final String MODULE_MSG = "/message/setting";
    public static final String MODULE_WARDROBE = "/locker/setting";
    public static final String MODULE_HOME = "/shop/home";
    public static final String MODULE_WECHAT = "/shop/weixin/setting";// 微信公众号

    /**
     * article 文章回复 资讯
     */
    public static final String MODULE_ARTICLE_COMMENT_REPLY = "/article/comment/reply";//
    public static final String MODULE_ARTICLE_COMMENT_LIST = "/article/comment/list";//

    public static final String PERMISSION_STAFF = "/position/setting";
    public static final String PERMISSION_TRAINER = "/coach/permission/setting";
    public static final String SIGNIN_SCREEN = "/checkin/screen/";
    public static final String PLANS_SETTING_GROUP = "/private/course/plans/setting";
    public static final String PLANS_SETTING_PRIVATE = "/checkin/screen/";

    //求职招聘消息列表
    public static final String RECRUIT_MESSAGE_LIST = "/recruit/message_list";
    //导入导出
    public static final String REPORT_EXPORT = "/report/export";

    public static final String STUDENT_IMPORT = "/manage/members/import";
    public static final String STUDENT_EXPORT = "/manage/members/export";
    public static final String CARD_IMPORT = "/cards/import";
    public static final String CARD_EXPORT = "/cards/export";

    //用户协议
    public static final String USER_PROTOCOL = "/user/protocol";
    public static final String USER_PROTOCOL_URL = "/protocol/staff/";

    //会员卡服务协议
    public static final String MODULE_ADD_CARD_PROTOCOL = "/card_tpl/add";
    public static final String CARD_TPL_ID = "card_tpl_id";
    public static final String MULTI_CARD_TPL = "card-templates/add";

    //    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;

    @Inject QcRestRepository restRepository;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @BindView(R2.id.done) LinearLayout done;
    @BindView(R2.id.layout_next) LinearLayout layoutNext;
    @BindView(R2.id.root) LinearLayout root;
    @BindView(R2.id.root_view) RelativeLayout rootView;
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

    public static void start(Context context, String module, String tpl_id) {
        Intent starter = new Intent(context, QRActivity.class);
        starter.putExtra(LINK_MODULE, module);
        starter.putExtra(CARD_TPL_ID, tpl_id);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saas_qr);
        ButterKnife.bind(this);
        View v = new View(this);
        v.setBackgroundResource(R.color.toolbar);
        root.addView(v,0,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          MeasureUtils.getStatusBarHeight(this)));
        toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        toolbarTitile.setText("在电脑中使用该功能");
        //        qrdecoderview.setOnQRCodeReadListener(this);
    }

    @OnClick(R2.id.btn_next) public void onClickNext() {
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
            url = getResources()
                .getString(R.string.qr_code_2web, Configs.Server, gymWrapper.brand_id(),
                    gymWrapper.shop_id(), getIntent().getStringExtra(LINK_MODULE));
        } else if (getIntent() != null && getIntent().hasExtra(LINK_URL)) {
            url = getIntent().getStringExtra(LINK_URL);
        }
        sp = restRepository.createPostApi(PostApi.class).qcScans(text, new ScanBody.Builder().url(url).session_id(session)
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
                    RxBus.getBus().post(new OnBackEvent());
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