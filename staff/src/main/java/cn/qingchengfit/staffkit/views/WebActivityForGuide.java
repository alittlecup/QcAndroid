package cn.qingchengfit.staffkit.views;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ShopDetail;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventPoplularize;
import cn.qingchengfit.staffkit.views.custom.CustomSwipeRefreshLayout;
import cn.qingchengfit.staffkit.views.gym.gym_web.CompletedConnectFragment;
import cn.qingchengfit.staffkit.views.gym.gym_web.ConnectWechatFragmentBuilder;
import cn.qingchengfit.staffkit.views.gym.gym_web.GymPoplularize;
import cn.qingchengfit.staffkit.views.gym.gym_web.HomePageQrCodeFragment;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/12 2015.
 */

public class WebActivityForGuide extends BaseActivity implements FragCallBack {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject HomePageQrCodeFragment homePageQrCodeFragment;
    @Inject CompletedConnectFragment completedConnectFragment;
    private TextView mToobarActionTextView;
    private Toolbar mToolbar;
    private WebView mWebviewWebView;
    //    private LinearLayout mWebviewRootLinearLayout;
    private CookieManager cookieManager;
    private LinearLayout mNoNetwork;
    private Button mRefresh;
    private List<Integer> mlastPosition = new ArrayList<>(); //记录深度
    private List<String> mTitleStack = new ArrayList<>(); //记录标题
    private MaterialDialog loadingDialog;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallbackNew;
    private List<String> hostArray = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    private String sessionid;
    private IWXAPI msgApi;
    private String test =
        "{\'appId\': \'wx81e378c8fd03319d\',\'nonceStr\': \'IvGxLujqa73veSM\',\'package\': \'Sign=WXPay\',\'partnerId \': \'1316532101\',\'paySign\': \'205F1707DD8379C0DA1782F7F9BEA2F8\',\'prepayId\': \'wx20160226124520229c7c8edf0039065235\',\'timeStamp\': \'1456462707\'}";
    private Subscription paySp;
    private ChoosePictureFragmentDialog choosePictureFragmentDialog;
    private TextView mTitle;
    private String mUrl;
    private String mCopyUrl;
    private Observable<EventPoplularize> mObPop;
    private Subscription mSpShopConfig;
    private boolean mWXSuccess;
    private String mWxName;
    private String mWxQr;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_for_guide);
        ButterKnife.bind(this);
        mCopyUrl = getIntent().getStringExtra("copyurl");
        mUrl = getIntent().getStringExtra("url");
        //((App)getApplication()).getAppCompoent().inject(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.web_for_guide_frag, WebFragment.newInstance(mUrl)).commit();
        mObPop = RxBus.getBus().register(EventPoplularize.class);
        mObPop.debounce(300, TimeUnit.MILLISECONDS).subscribe(new Action1<EventPoplularize>() {
            @Override public void call(EventPoplularize eventPoplularize) {
                switch (eventPoplularize.clickId) {
                    case R.id.btn_to_wechat_public:
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.other_frag, new ConnectWechatFragmentBuilder(mWxName, mWxQr).build())
                            .addToBackStack("wechat")
                            .commit();
                        break;
                    case R.id.btn_home_qr:
                        homePageQrCodeFragment.mUrl = mUrl;
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.other_frag, homePageQrCodeFragment)
                            .addToBackStack("")
                            .commit();
                        break;
                    case R.id.btn_more_popularize:
                        WebActivity.startWeb("http://cloud.qingchengfit.cn/mobile/urls/eeb0e361a378428fa1a862c949495e0d/",
                            WebActivityForGuide.this);
                        break;
                }
            }
        });
        mSpShopConfig = new RestRepository().getGet_api()
            .qcGetShopDetail(loginStatus.staff_id(), gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<ShopDetail>>() {
                @Override public void call(QcResponseData<ShopDetail> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mWXSuccess = qcResponse.getData().shop.weixin_success;
                        mWxName = qcResponse.getData().shop.weixin;
                        mWxQr = qcResponse.getData().shop.weixin_image;
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public void doneConnectWechat() {
        completedConnectFragment.mCopyUrl = mCopyUrl;
        getSupportFragmentManager().beginTransaction().replace(R.id.other_frag, completedConnectFragment).addToBackStack("").commit();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(EventPoplularize.class.getName(), mObPop);
        mSpShopConfig.unsubscribe();
    }

    @OnClick({ R.id.btn_help, R.id.btn_config_homepage, R.id.btn_poplularize }) public void onTabClick(View view) {
        switch (view.getId()) {
            /**
             * 帮助
             */
            case R.id.btn_help:
                DialogUtils.showAlert(this, getString(R.string.alert_title_what_is_home_page),
                    getString(R.string.alert_content_what_is_home_page));
                break;
            /**
             * 配置主页
             */

            case R.id.btn_config_homepage:
                if (!serPermisAction.check(PermissionServerUtils.SHOP_HOME_SETTING)) {
                    DialogUtils.showAlert(this, R.string.sorry_for_no_permission);
                    return;
                }

                new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent toScan = new Intent(WebActivityForGuide.this, QRActivity.class);
                            toScan.putExtra(QRActivity.LINK_URL, Configs.Server
                                + "app2web/?id="
                                + gymWrapper.id()
                                + "&model="
                                + gymWrapper.model()
                                + "&module="
                                + "");// TODO: 2017/1/21 模块名
                            startActivity(toScan);
                        } else {
                            ToastUtils.show(getString(R.string.please_open_camera));
                        }
                    }
                });
                break;
            /**
             * 推广
             */
            case R.id.btn_poplularize:
                GymPoplularize.newInstance(gymWrapper.name(), "", gymWrapper.photo(), mCopyUrl, mWXSuccess)
                    .show(getSupportFragmentManager(), "");
                break;
        }
    }

    @Override public int getFragId() {
        return 0;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }
}
