package cn.qingchengfit.saascommon.qrcode.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.network.SaasCommonApi;
import cn.qingchengfit.saascommon.qrcode.model.QrEvent;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;

import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.qrcode.model.ScanBody;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QRActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

  /**
   * 第一个是module_key，第二个是identifyKey，APP端使用identifyKey即可
   *
   * 课程预约 orders_day                  order_record
   * 团课  teamarrange_calendar           team_course_arrange_calendar
   * 私教  priarrange_calendar  private_course_arrange_calendar
   * 会员  manage_members   member_setting
   * 会员卡 manage_costs   card
   * 代约团课 orders_day    team_course_order
   * 代约私教 orders_day    private_course_order
   * 签到处理 checkin_manage checkin_manage（目前web端没有这个模块）
   * 入场签到 checkin_setting checkin_setting
   * 商店  commodity_list commodity_list
   * 提测数据模板 measuresetting    measure_setting
   * 导出记录 exportsetting     export_record
   * 工作人员 manage_staff    staff_setting
   * 教练  coachsetting    coach_setting
   * 会员积分 score_rank    score_rank
   * 活动  activity_setting    activity_setting
   * 接入口碑网 koubei    koubei
   * 注册送会员卡 giftcard    gift_card
   * 场馆公告 notice    notice_list
   * 赛事训练营 training training(目前web端没有这个模块)
   * 在线支付 pay_bills   pay_bills
   * 课程报表 cost_report  cost_report
   * 销售报表  sales_report  sales_report
   * 评分报表 comments_report  comment_report
   * 会员卡报表 card_report card_report
   * 签到报表 checkin_report  check_in_report
   * 场馆信息 studio_list   studio_setting
   * 营业时间 open_time open_time(目前web端没有这个模块)
   * 场地  space_setting  space_setting
   * 短信  messagechannels   msg_channels
   * 更衣柜  locker_setting locker_setting
   * 会员端配置 shop_home_setting studio_home
   */

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
  public static final String MODULE_WORKSPACE_COMMODITY_LIST = "/commodity/list";

  /**
   * 健身房服务
   */
  public static final String MODULE_SERVICE_GROUP = "/teamarrange/calendar2";
  public static final String MODULE_SERVICE_PRIVATE = "/priarrange/calendar2";
  public static final String MODULE_SERVICE_FREE = "/checkin/setting";
  public static final String MODULE_SERVICE_SHOP = "/commodity/manage";
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
  public static final String MODULE_SHOP_TURNOVERS = "/shop-turnovers";
  public static final String MODULE_OPERATE_SCORE = "/score/rank";
  public static final String MODULE_OPERATE_ACTIVITY = "/activity/setting";
  public static final String MODULE_OPERATE_AD = "/shop/home/setting";
  public static final String MODULE_OPERTAT_KOUBEI = "/koubei";
  public static final String MODULE_OPERATE_REGIST = "/giftcard";
  public static final String MODULE_OPERATE_ANOUNCE = "/notice";
  public static final String MODULE_OPERATE_COMPETITION = "/competition";
  public static final String MODULE_OPERATE_COUPON = "/coupons/list";
  public static final String MODULE_OPERATE_RED_EVELOP_TPL = "/red-envelope-tpls/list";
  public static final String MODULE_OPERATE_PRIVATE_SHARE = "/private/collage";
  public static final String MODULE_OPERATE_GROUP_SHARE = "/group/collage";
  public static final String MODULE_OPERATE_MORE = "/spread/more";
  public static final String MODULE_MARKET_DIANPING = "/meituan/dianping";

  public static final String MODULE_TMALL_JOIN = "/tmall/join";
  public static final String MODULE_PARNTER_MANAGER = "/partners/manager";

  /**
   * 财务
   */
  public static final String MODULE_FINACE_ONLINE = "/pay/bills";
  public static final String MODULE_FINANCE_COURSE = "/cost/report";
  public static final String MODULE_FINANCE_SALE = "/sales/report";
  public static final String MODULE_FINANCE_MARK = "/comments/report";
  public static final String MODULE_FINANCE_CARD = "/card/report";
  public static final String MODULE_FINANCE_SIGN_IN = "/checkin/report";
  public static final String MODULE_FINANCE_ORDER = "/class/report";
  public static final String MODULE_FINANCE_VISUAL_REPORT = "/visual/reports";

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
   * 数据实验室
   */
  public static final String MODULE_DATA_INCOME = "/data-lab/sales-revenue-analysis";// 营销收入
  public static final String MODULE_DATA_MEMBER = "/data-lab/members-analysis";// 会员数据
  public static final String MODULE_DATA_GROUP = "/data-lab/team-course-analysis";// 团课数据
  public static final String MODULE_DATA_PRIVATE = "/data-lab/private-course-analysis";// 私教数据
  public static final String MODULE_DATA_BIG = "/data-lab/macro-analysis";// 宏观数据
  public static final String MODULE_DATA_WHITEPAPER = "/datalab/whitepaper";// 白皮书
  /**
   * 智能健身房
   */
  public static final String MODULE_SMARTGYM_SMART = "/smart_gym";// 白皮书

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

  //智奇门禁
  public static final String ZQ_ACCESS = "/zq/access";
  public static final String ADD_ZQ_ACCESS = "/add/zq/access";
  public static final String EDIT_ZQ_ACCESS = "/edit/zq/access";
  public static final String SIGN_IN_CODE = "/sign/code";
  public static final String SIGN_IN_CARD = "/sign/card";

  public static final String GYM_PARTNER_ALI = "enter-ali";
  public static final String GYM_PARTNER_KOUBEI = "koubei";

  QRCodeReaderView qrdecoderview;

  Toolbar toolbar;
  TextView toolbarTitile;
  LinearLayout done;
  LinearLayout layoutNext;
  LinearLayout root;
  RelativeLayout rootView;
  private Subscription sp;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository restRepository;

  /**
   * @param module Web相应模块的后缀
   */
  public static void start(Context context, String module) {
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

  public static String getIdentifyKey(String module) {
    switch (module.toLowerCase()) {
      case MODULE_NONE:
        return "";
      case MODULE_WORKSPACE_ORDER_LIST:
        return "order_record";
      case MODULE_WORKSPACE_GROUP:
        return "team_course_order";
      case MODULE_WORKSPACE_PRIVATE:
        return "private_course_order";
      case MODULE_WORKSPACE_ORDER_SIGNIN:
        return "checkin_manage";
      case MODULE_FINACE_ONLINE:
        return "pay_bills";
      case MODULE_FINANCE_CARD:
        return "card_report";
      case MODULE_FINANCE_COURSE:
        return "cost_report";
      case MODULE_FINANCE_MARK:
        return "comment_report";
      case MODULE_FINANCE_SALE:
        return "sales_report";
      case MODULE_FINANCE_SIGN_IN:
        return "check_in_report";

      case MODULE_OPERATE_ACTIVITY:
        return "activity_setting";
      case MODULE_OPERATE_AD:
        return "ad";
      case MODULE_OPERATE_ANOUNCE:
        return "notice_list";
      case MODULE_OPERATE_REGIST:
        return "gift_card";
      case MODULE_OPERTAT_KOUBEI:
        return "koubei";
      case MODULE_OPERATE_SCORE:
        return "score_rank";
      case MODULE_OPERATE_COMPETITION:
        return "training";

      case MODULE_MANAGE_COACH:
        return "coach_setting";
      case MODULE_MANAGE_STAFF:
        return "staff_setting";

      case MODULE_SERVICE_FREE:
        return "checkin_setting";
      case MODULE_SERVICE_GROUP:
        return "team_course_arrange_calendar";
      case MODULE_SERVICE_PRIVATE:
        return "private_course_arrange_calendar";
      case MODULE_SERVICE_SHOP:
        return "commodity_manage";
      case MODULE_WORKSPACE_COMMODITY_LIST:
        return "commodity_list";

      case MODULE_WARDROBE:
        return "locker_setting";
      case MODULE_GYM_INFO:
        return "studio_setting";
      case MODULE_GYM_TIME:
        return "open_time";
      case MODULE_GYM_SITE:
        return "space_setting";
      case MODULE_HOME:
        return "studio_home";
      case MODULE_MSG:
        return "msg_channels";
      case MODULE_WECHAT:
        return "gym_wechat";

      case MODULE_STUDENT:
        return "member_setting";
      case MODULE_STUDENT_CARDS:
        return "card";
      case MODULE_STUDENT_BODY_TEST:
        return "measure_setting";
      case REPORT_EXPORT:
        return "export_record";
      default:
        return "";
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_saas_qr);
    toolbar = findViewById(R.id.toolbar);
    toolbarTitile = findViewById(R.id.toolbar_title);
    done = findViewById(R.id.done);
    layoutNext = findViewById(R.id.layout_next);
    root = findViewById(R.id.root);
    rootView = findViewById(R.id.root_view);
    findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickNext();
      }
    });

    View v = new View(this);
    v.setBackgroundResource(R.color.toolbar);
    root.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        MeasureUtils.getStatusBarHeight(this)));
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        finish();
      }
    });
    toolbarTitile.setText("在电脑中使用该功能");
    //qrdecoderview.setOnQRCodeReadListener(this);
    //根据版本修改提示的网址
    TextView tvContent = findViewById(R.id.tv_content);
    tvContent.setText(
        String.format(getResources().getString(R.string.qr_point_text), Configs.QR_POINT_URL));
    TextView pointUrl = findViewById(R.id.tv_point_url);
    pointUrl.setText(Configs.QR_POINT_URL);
  }

  public void onClickNext() {
    new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          layoutNext.setVisibility(View.GONE);
          qrdecoderview = new QRCodeReaderView(QRActivity.this);
          rootView.addView(qrdecoderview, 0,
              new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT));
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
    if (done.getVisibility() == View.GONE
        && layoutNext.getVisibility() == View.GONE
        && qrdecoderview != null) {
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
      url = getResources().getString(R.string.qr_code_2web, restRepository.getHost(),
          gymWrapper.brand_id(), gymWrapper.shop_id(), getIntent().getStringExtra(LINK_MODULE));
    } else if (getIntent() != null && getIntent().hasExtra(LINK_URL)) {
      url = getIntent().getStringExtra(LINK_URL);
    }
    sp = restRepository.createRxJava1Api(SaasCommonApi.class)
        .qcScans(text, new ScanBody.Builder().url(url).session_id(session).build())
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
              RxBus.getBus().post(new QrEvent());
            } else {
              DialogUtils.instanceDelDialog(QRActivity.this, getString(R.string.err_sacn_qrcode),
                  (dialog, which) -> {
                    dialog.dismiss();
                    if (qrdecoderview != null) {
                      qrdecoderview.getCameraManager().startPreview();
                      toolbarTitile.setText("扫码二维码");
                    }
                  });
            }
          }
        });
  }

  @Override public void cameraNotFound() {

  }

  @Override public void QRCodeNotFoundOnCamImage() {

  }
}
