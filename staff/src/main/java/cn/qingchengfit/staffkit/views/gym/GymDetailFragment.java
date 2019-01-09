package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.items.ButtonItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.model.responese.HomeDetailGym;
import cn.qingchengfit.model.responese.HomeStatement;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.turnovers.TurnoverBarChartFragment;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.events.EventChartTitle;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.qrcode.views.QRScanActivity;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.saascommon.views.CommonDialog;
import cn.qingchengfit.saascommon.views.CommonSimpleTextActivity;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragment;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragmentBuilder;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.Prefer;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.dianping.pages.DianPingEmptyFragment;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.rxbus.event.GoToGuideEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxCompleteGuideEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.GymDetailShowGuideDialogFragment;
import cn.qingchengfit.staffkit.views.MainFirstFragment;
import cn.qingchengfit.staffkit.views.PopFromBottomActivity;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.gym.items.GymFuntionItem;
import cn.qingchengfit.staffkit.views.gym.upgrate.GymExpireFragment;
import cn.qingchengfit.staffkit.views.setting.BrandManageActivity;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.followup.IncreaseStudentPageParams;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.wxpreview.old.GymPoplularize;
import cn.qingchengfit.wxpreview.old.WebActivityForGuide;
import cn.qingchengfit.wxpreview.old.newa.MiniProgramUtil;
import cn.qingchengfit.wxpreview.old.newa.WxPreviewEmptyActivity;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8bx
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/1 2016.
 */
public class GymDetailFragment extends BaseFragment
    implements GymDetailView, AdapterView.OnItemClickListener, FlexibleAdapter.OnItemClickListener {

  public static final int RESULT_STAFF_MANAGE = 12;

  RecyclerView recycleview;
  ImageView shopImg;
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView down;
  LinearLayout gymLayout;
  TextView toolbarLeft;
  AppBarLayout layoutCollapsed;
  TextView scheduleNotificationCount;
  ViewPager vpCharts;
  CircleIndicator indicator;
  TextView gymName;
  TextView gymSu;
  Button mRechargeBtn;
  ImageView tagPro;
  LinearLayout layoutCharge;
  CompatTextView tvPrice;
  LinearLayout llScan;

  @Inject GymDetailPresenter gymDetailPresenter;
  @Inject StaffRespository restRepository;
  @Inject GymWrapper gymWrapper;
  //@Inject GymMoreFragment gymMoreFragment;
  @Inject SerPermisAction serPermisAction;
  @Inject IPermissionModel permissionModel;
  @Inject GymFunctionFactory gymFunctionFactory;
  private String mCopyUrl;
  private Observable<RxCompleteGuideEvent> mObGuideComplete;
  private GymMoreAdapter adapter;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();

  private DialogList dialogList;
  private QuitGymFragment quitDialog;
  private GymDetailChartAdapter mChartAdapter;
  private LinearLayout llTitleBelow, llTitleAbove, llCheckout, llTitle;
  /**
   * 即将到期提醒
   */
  private GymExpireFragment mGymExpireDialog;
  private String mPreViewUrl;
  private boolean firstMonthClose;
  private static float offSetMaxSize;
  List<Fragment> fragments = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    offSetMaxSize = DensityUtil.dip2px(getContext(), 110);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gym_detail, container, false);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
    shopImg = (ImageView) view.findViewById(R.id.shop_img);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    down = (ImageView) view.findViewById(R.id.down);
    gymLayout = (LinearLayout) view.findViewById(R.id.gym_layout);
    toolbarLeft = (TextView) view.findViewById(R.id.toolbar_left);
    layoutCollapsed = (AppBarLayout) view.findViewById(R.id.layout_collapsed);
    llTitleBelow = view.findViewById(R.id.ll_title_below);
    llTitleAbove = view.findViewById(R.id.ll_title_above);
    llCheckout = view.findViewById(R.id.ll_checkout);
    llTitle = view.findViewById(R.id.ll_title);
    scheduleNotificationCount = (TextView) view.findViewById(R.id.schedule_notification_count);
    vpCharts = (ViewPager) view.findViewById(R.id.vp_charts);
    indicator = (CircleIndicator) view.findViewById(R.id.indicator);
    gymName = (TextView) view.findViewById(R.id.gym_name);
    gymSu = (TextView) view.findViewById(R.id.gym_su);
    mRechargeBtn = (Button) view.findViewById(R.id.recharge);
    tagPro = (ImageView) view.findViewById(R.id.tag_pro);
    layoutCharge = (LinearLayout) view.findViewById(R.id.layout_to_charge);
    tvPrice = (CompatTextView) view.findViewById(R.id.tv_price);
    llScan = view.findViewById(R.id.ll_scan);
    view.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onTitleClick();
      }
    });
    view.findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onTitleClick();
      }
    });
    view.findViewById(R.id.toolbar_left).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onLeft();
      }
    });
    view.findViewById(R.id.btn_preview).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        guideToStudentPreview();
      }
    });
    view.findViewById(R.id.btn_wx_pm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (MiniProgramUtil.getMiniProgream(getContext(), gymWrapper.getGymId()) != null) {
          RouteUtil.routeTo(getContext(), "wxmini", "/show/mini", null);
        } else {
          RouteUtil.routeTo(getContext(), "wxmini", "/mini/page", null);
        }
      }
    });
    view.findViewById(R.id.btn_poplularize).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onExportClick();
      }
    });

    delegatePresenter(gymDetailPresenter, this);
    initToolbar(toolbar);
    initView();
    registeSensors();
    initListener(view);
    view.setOnTouchListener((v, event) -> true);
    view.findViewById(R.id.btn_close).setOnClickListener(view1 -> {
      if (showTime > 0) {
        SensorsUtils.track("QcSaasSpecialPriceBannerClose")
            .addProperty("qc_stay_time", System.currentTimeMillis() / 1000 - showTime)
            .commit(getContext());
      }
      layoutCharge.setVisibility(View.GONE);
      firstMonthClose = true;
    });
    RxBusAdd(EventChartTitle.class).subscribe(eventChartTitle -> {
      switch (eventChartTitle.getChartType()) {
        case 1:
          if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
              PermissionServerUtils.COST_REPORT)) {
            return;
          }
          Intent toStatement = new Intent(getActivity(), ContainerActivity.class);
          toStatement.putExtra(Configs.ROUTER, QRActivity.MODULE_FINANCE_COURSE);
          startActivity(toStatement);

          break;
        case 2:
          if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
              PermissionServerUtils.CHECKIN_REPORT)) {
            return;
          }
          Intent toSignIn = new Intent(getActivity(), ContainerActivity.class);
          toSignIn.putExtra(Configs.ROUTER, QRActivity.MODULE_FINANCE_SIGN_IN);
          startActivity(toSignIn);
          break;
        case 3:

          if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS)) {
            showAlert(R.string.sorry_for_no_permission);
            return;
          }
          routeTo("student", "student/increase",
              new IncreaseStudentPageParams().curType(IncreaseType.INCREASE_MEMBER).build());

          break;
        default:
          if (!serPermisAction.check(PermissionServerUtils.SALES_REPORT)) {
            showAlert(R.string.sorry_for_no_permission);
            return;
          }
          Intent toSale = new Intent(getActivity(), ContainerActivity.class);
          toSale.putExtra(Configs.ROUTER, QRActivity.MODULE_FINANCE_SALE);
          startActivity(toSale);

          break;
      }
    });
    RxBusAdd(EventFreshCoachService.class).subscribe(new Action1<EventFreshCoachService>() {
      @Override public void call(EventFreshCoachService eventFreshCoachService) {
        gymDetailPresenter.getGymWelcome();
        adapter.setTag("isPro", true);
      }
    });
    RxBusAdd(GoToGuideEvent.class).subscribe(goToGuideEvent -> onHowtoUse());
    RxBusAdd(EventLoginChange.class).delay(600, TimeUnit.MILLISECONDS)
        .subscribe(eventLoginChange -> setGymInfo());
    if (isSingleBrand()) {
      view.findViewById(R.id.include_bottom).setVisibility(View.GONE);
    }

    return view;
  }

  private void initListener(View root) {
    layoutCollapsed.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
      int offset = Math.abs(verticalOffset);
      updateToolbarUI(offset / offSetMaxSize);
    });
    root.findViewById(R.id.img_cash).setOnClickListener(v -> routeToCheckoutMoney());
    root.findViewById(R.id.img_charge_card).setOnClickListener(v -> routeToChargeCard());
    root.findViewById(R.id.img_new_card).setOnClickListener(v -> routeToBuyCard());
    root.findViewById(R.id.img_cash_scan).setOnClickListener(v -> routeToShowQrCode());

    root.findViewById(R.id.fl_cash).setOnClickListener(v -> routeToCheckoutMoney());
    root.findViewById(R.id.fl_charge_card).setOnClickListener(v -> routeToChargeCard());
    root.findViewById(R.id.fl_new_card).setOnClickListener(v -> routeToBuyCard());
    root.findViewById(R.id.fl_scan).setOnClickListener(v -> routeToShowQrCode());
  }

  private void routeToCheckoutMoney() {
    QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/money"))
        .call();
  }

  private void routeToChargeCard() {
    Bundle builder = new BundleBuilder().withBoolean("addAble", false)
        .withInt("chooseType", 1)
        .withBoolean("isFromCheckout", true)
        .build();
    routeTo("student", "/search/student/", builder);
  }

  private void routeToBuyCard() {
    Bundle builder = new BundleBuilder().withBoolean("isFromCheckout", true).build();
    routeTo("card", "/cardtpl/nonew", builder);
  }

  private void routeToShowQrCode() {
    QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/qrcode"))
        .call();
  }

  private void updateToolbarUI(float present) {
    if (present <= 0) {
      llTitleAbove.setVisibility(View.GONE);
      llTitleBelow.setVisibility(View.VISIBLE);
      llTitle.setVisibility(View.VISIBLE);
    } else if (present <= 1) {
      llTitleBelow.setVisibility(View.GONE);
      llTitle.setVisibility(View.GONE);
      llTitleAbove.setVisibility(View.VISIBLE);
      llTitleAbove.setAlpha(present);
      llCheckout.setAlpha(Math.abs(1 - present));
    }
  }

  private boolean isSingleBrand() {
    return getParentFragment() instanceof MainFirstFragment;
  }

  /**
   * 刷新健身房的权限和数据
   */
  @Override public void onResume() {
    super.onResume();
    gymDetailPresenter.updatePermission();
  }

  public String weixin_image = "";
  public boolean weixin_success = false;
  public String weixin = "";

  private void onExportClick() {
    GymPoplularize gymPoplularize =
        GymPoplularize.newInstance(gymWrapper.name(), "", gymWrapper.photo(), mCopyUrl,
            weixin_success);
    gymPoplularize.setOnListItemClickListener(new GymPoplularize.GymPoplularizeListener() {
      @Override public void onBtnToWechatPublicClicked(GymPoplularize dialog) {
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("wxQr", weixin_image);
        bundle.putString("wxName", weixin);
        Intent intent = new Intent(getContext(), WxPreviewEmptyActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("to", 1);
        startActivity(intent);
      }

      @Override public void onBtnHomeQrClicked(GymPoplularize dialog) {
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("mUrl", mCopyUrl);
        Intent intent = new Intent(getContext(), WxPreviewEmptyActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("to", 2);
        startActivity(intent);
      }

      @Override public void onBtnMorePopularizeClicked(GymPoplularize dialog) {
        dialog.dismiss();
        WebActivity.startWeb(
            "http://cloud.qingchengfit.cn/mobile/urls/eeb0e361a378428fa1a862c949495e0d/",
            getContext());
      }

      @Override public void onBtnMiniProgramClicked(GymPoplularize dialog) {
        if (MiniProgramUtil.getMiniProgream(getContext(), gymWrapper.getGymId()) != null) {
          RouteUtil.routeTo(getContext(), "wxmini", "/show/mini", null);
        } else {
          RouteUtil.routeTo(getContext(), "wxmini", "/mini/page", null);
        }
      }
    });
    gymPoplularize.show(getChildFragmentManager(), "");
  }

  /**
   * 点击标题弹出场馆信息
   */
  public void onTitleClick() {
    gymLayout.setPivotY(0);
    if (gymLayout.getVisibility() == View.VISIBLE) {
      ViewCompat.animate(gymLayout)
          .scaleY(0)
          .setDuration(200L)
          .setListener(new ViewPropertyAnimatorListener() {
            @Override public void onAnimationStart(View view) {
            }

            @Override public void onAnimationEnd(View view) {
              if (view.getScaleY() == 0) {
                if (gymLayout != null) gymLayout.setVisibility(View.GONE);
              }
            }

            @Override public void onAnimationCancel(View view) {
            }
          })
          .start();
    } else {
      gymLayout.setScaleY(0);
      gymLayout.setVisibility(View.VISIBLE);
      ViewCompat.animate(gymLayout).scaleY(1).setDuration(200L).start();
    }
    ViewCompat.animate(down).rotationBy(180).setDuration(200).start();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    if (getActivity() instanceof MainActivity) {
      toolbarLeft.setVisibility(View.VISIBLE);
    } else {
      toolbarLeft.setVisibility(View.GONE);
      super.initToolbar(toolbar);
    }
    toolbar.getMenu().clear();
    llScan.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(getContext(), QRScanActivity.class);
        intent.putExtra("title", "扫描二维码");
        intent.putExtra("point_text", "将取景框对准二维码，即可自动扫描");
        startActivityForResult(intent, 1001);
      }
    });
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(getContext()),
          0, 0);
    }
  }

  private void initView() {
    datas.clear();
    datas.add(new GymFuntionItem(
        new GymFuntion.Builder().img(R.drawable.ic_function_more).text(R.string.more).build()));
    adapter = new GymMoreAdapter(datas, this);
    adapter.setTag("isPro", gymWrapper.isPro());

    SmoothScrollGridLayoutManager layoutManager =
        new SmoothScrollGridLayoutManager(getContext(), 4);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        switch (adapter.getItemViewType(position)) {
          case R.layout.item_gym_function:
            return 1;
          default:
            return 4;
        }
      }
    });
    recycleview.setLayoutManager(layoutManager);
    recycleview.setHasFixedSize(true);
    recycleview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.item_button, 20)
            .withTopEdge(true));
    recycleview.setAdapter(adapter);
    notifyMyFunctions();
    recycleview.getViewTreeObserver().
        addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override public void onGlobalLayout() {
                                      if (recycleview != null) {
                                        CompatUtils.removeGlobalLayout(recycleview.getViewTreeObserver(), this);
                                        //判断是否调至会员端预览
                                        if (!PreferenceUtils.getPrefBoolean(getContext(), "goStudentPre", false)) {
                                          getChildFragmentManager().beginTransaction()
                                              .replace(R.id.top_frag, new GymDetailShowGuideDialogFragment())
                                              .addToBackStack(null)
                                              .commit();
                                          //rootScroll.fullScroll(View.FOCUS_DOWN);
                                          layoutCollapsed.setExpanded(false);
                                          PreferenceUtils.setPrefBoolean(getContext(), "goStudentPre", true);
                                        } else {
                                          //rootScroll.fullScroll(View.FOCUS_UP);
                                          layoutCollapsed.setExpanded(true);
                                        }
                                      }
                                    }
                                  }

        );

    onGymInfo(gymWrapper.getCoachService());
    mChartAdapter = new GymDetailChartAdapter(getChildFragmentManager(), null);

    vpCharts.setOffscreenPageLimit(4);
    vpCharts.setAdapter(mChartAdapter);
    indicator.setViewPager(vpCharts);
  }

  void notifyMyFunctions() {
    int addcount = ((4 - (datas.size()) % 4) % 4);
    for (int i = 0; i < addcount; i++) {
      datas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(QRActivity.MODULE_NONE)));
    }
    if (isSingleBrand()) {
      datas.add(ButtonItem.newBuilder().txt("会员端界面").build());
    }
    datas.add(SimpleTextItemItem.newBuilder()
        .bg(R.color.transparent)
        .gravity(Gravity.CENTER)
        .text("如何使用")
        .build());
    adapter.updateDataSet(datas);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1001) {
        if (null != data) {
          String content = data.getStringExtra("content");
          if (!TextUtils.isEmpty(content)) {
            if (content.startsWith("DIANPING-QINGCHENG-SHOPMAPPING")) {
              DianPingEmptyFragment.addDianPingEmptyFragment(getFragmentManager(), content);
            } else if (content.startsWith("http")) {
              WebActivity.startWeb(content, getContext());
            } else {
              Intent intent = new Intent(getActivity(), CommonSimpleTextActivity.class);
              intent.putExtra("content", content);
              startActivity(intent);
            }
          }
        }
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDetach() {
    super.onDetach();
    gymWrapper.setCoachService(null);
  }

  @Override public void setGymInfo() {
  }

  @Override public void renew() {

  }

  @Override public void initFunction() {

  }

  @Override public void studentPreview(final String url, final String copy) {
    mCopyUrl = copy;
    mPreViewUrl = url;
  }

  private void guideToStudentPreview() {
    Intent toWebForGuide = new Intent(getActivity(), WebActivityForGuide.class);
    toWebForGuide.putExtra("url", mPreViewUrl);
    toWebForGuide.putExtra("copyurl", mCopyUrl);
    startActivity(toWebForGuide);
  }

  @Override public void guideToWechat(final String url) {

  }

  @Override public void setBanner(List<Banner> strings) {
  }

  @Override public void setInfo(HomeStatement stats) {
    hideLoading();
    if (stats != null) {
      fragments = getFragments(stats);
      mChartAdapter.setHomeStatment(stats);
      indicator.setViewPager(vpCharts);
      mChartAdapter.notifyDataSetChanged();
    }
  }

  long showTime = 0L;

  @Override
  public void setRecharge(final GymDetail.Recharge recharge, boolean hasFirst, String price) {
    hideLoading();
    showTime = 0L;
    layoutCharge.setVisibility((hasFirst && !firstMonthClose) ? View.VISIBLE : View.GONE);
    if (hasFirst && !firstMonthClose) {
      showTime = System.currentTimeMillis() / 1000;
      SensorsUtils.track("QcSaasSpecialPriceBannerShow").commit(getContext());
    }
    tvPrice.setText(getString(R.string.underline_pro_update_now, price));
    mRechargeBtn.setOnClickListener(v -> {
      SensorsUtils.track("QcSaasEnterRechargePageBtnClick")
          .addProperty("qc_saas_shop_status", gymWrapper.isPro() ? "pro" : "free")
          .addProperty("qc_saas_shop_expire_date", gymWrapper.system_end())
          .commit(getContext());
      toCharge();
    });
    layoutCharge.setOnClickListener(view -> {
      if (showTime > 0) {
        SensorsUtils.track("QcSaasEnterRechargePageBtnClick")
            .addProperty("qc_stay_time", System.currentTimeMillis() / 1000 - showTime)
            .commit(getContext());
      }
      toCharge();
    });
  }

  private void showCRMDialog() {
    boolean isFirst = PreferenceUtils.getPrefBoolean(getContext(), "crm_dialog", true);
    if (isFirst) {
      CommonDialog dialog = new CommonDialog(getContext());
      dialog.setImageView(R.drawable.crm_export_ad);
      dialog.setClickListener(new CommonDialog.DialogClickListener() {
        @Override public void onCloseClickListener(Dialog dialog) {
          dialog.dismiss();
        }

        @Override public void onItemClickListener(Dialog dialog) {
          dialog.dismiss();
          if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS)) {
            QcRouteUtil.setRouteOptions(new RouteOptions("student").setActionName("/student/home"))
                .call();
          } else {
            DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
          }
        }
      });
      dialog.showDialog();
      PreferenceUtils.setPrefBoolean(getContext(), "crm_dialog", false);
    }
  }

  private void toCharge() {
    Intent toRenewal = new Intent(getActivity(), PopFromBottomActivity.class);
    toRenewal.putExtra(BaseActivity.ROUTER, Router.BOTTOM_RENEWAL);
    startActivity(toRenewal);
  }

  @Override public void showOutDataTime() {

  }

  @Override public void hideOutDateTime() {

  }

  @Override public void onGymInfo(final CoachService coachService) {
    hideLoading();
    //swipeToRefresh.setRefreshing(false);
    showCRMDialog();
    if (coachService == null) return;
    gymWrapper.setCoachService(coachService);
    CoachService gym = coachService;
    if (GymUtils.getSystemEndDay(gymWrapper.getCoachService()) >= 0
        && GymUtils.getSystemEndDay(gymWrapper.getCoachService()) <= 7) {
      if (!cn.qingchengfit.utils.PreferenceUtils.getPrefString(getContext(), Prefer.SYSTEM_END_HINT,
          "").equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))) {
        if (mGymExpireDialog == null) mGymExpireDialog = new GymExpireFragment();
        if (!mGymExpireDialog.isVisible()) mGymExpireDialog.show(getFragmentManager(), "");
        cn.qingchengfit.utils.PreferenceUtils.setPrefString(getContext(), Prefer.SYSTEM_END_HINT,
            DateUtils.Date2YYYYMMDD(new Date()));
      }
    }
    if (coachService instanceof HomeDetailGym) {
      weixin = ((HomeDetailGym) coachService).weixin;
      weixin_image = ((HomeDetailGym) coachService).weixin_image;
      weixin_success = ((HomeDetailGym) coachService).weixin_success;
    }
    gymName.setText(gym.getName());
    Glide.with(getContext())
        .load(PhotoUtils.getSmall(gym.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(shopImg, getContext()));
    if (gymWrapper.isPro()) {
      mRechargeBtn.setBackgroundResource(R.drawable.bg_rect_prime);
      mRechargeBtn.setText("续费");
      mRechargeBtn.setTextColor(
          cn.qingchengfit.utils.CompatUtils.getColor(getContext(), R.color.colorPrimary));
      tagPro.setImageResource(R.drawable.ic_pro_green);
    } else {
      mRechargeBtn.setBackgroundResource(R.drawable.btn_prime);
      mRechargeBtn.setText("升级");
      mRechargeBtn.setTextColor(
          cn.qingchengfit.utils.CompatUtils.getColor(getContext(), R.color.white));
      tagPro.setImageResource(R.drawable.ic_pro_free);
    }

    toolbarTitile.setText(coachService.getName());
  }

  /**
   * 当前常用功能
   */
  @Override public void onModule(Object module) {
    datas.clear();
    if (module != null && module instanceof List) {
      for (Object o : (List) module) {
        if (o instanceof String) {
          if (!TextUtils.isEmpty((String) o)) {
            datas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion((String) o)));
          }
        }
      }
    }
    datas.add(new GymFuntionItem(new GymFuntion.Builder().img(R.drawable.ic_function_more)
        .moduleName("more")
        .text(R.string.more)
        .build()));

    notifyMyFunctions();
  }

  @Override public void onFailed() {
  }

  @Override public void onNotiCount(int count) {

    if (scheduleNotificationCount != null) {
      if (count > 0) {
        scheduleNotificationCount.setVisibility(View.VISIBLE);
        scheduleNotificationCount.setText(count + "");
      } else {
        scheduleNotificationCount.setVisibility(View.GONE);
      }
    }
  }

  @Override public void onManageBrand() {
    //品牌管理
    if (!gymWrapper.getBrand().has_add_permission) {
      cn.qingchengfit.utils.ToastUtils.show("您没有该场馆管理权限");
      return;
    }
    Intent toBrandManage = new Intent(getActivity(), BrandManageActivity.class);
    toBrandManage.putExtra(Configs.EXTRA_BRAND, gymWrapper.getBrand());
    startActivity(toBrandManage);
  }

  @Override public void onSpecialPoint(int count) {

  }

  @Override public void onMiniProgram(MiniProgram miniProgram) {
    MiniProgramUtil.saveMiniProgream(getContext(), gymWrapper.getGymId(), miniProgram);
  }

  @Override public void onQuitGym() {

  }

  /**
   * 单场馆新增健身房
   */
  public void onLeft() {

    //新增健身房
    SystemInitBody body;
    if (TextUtils.isEmpty(PreferenceUtils.getPrefString(getContext(), "init", ""))) {
      body = new SystemInitBody();
    } else {
      body = new Gson().fromJson(PreferenceUtils.getPrefString(getContext(), "init", ""),
          SystemInitBody.class);
    }
    App.caches.put("init", body);
    Intent toGuide = new Intent(getActivity(), GuideActivity.class);
    toGuide.putExtra("isAdd", true);
    startActivity(toGuide);
    mObGuideComplete = RxBus.getBus().register(RxCompleteGuideEvent.class);
    mObGuideComplete.subscribe(new Action1<RxCompleteGuideEvent>() {
      @Override public void call(RxCompleteGuideEvent rxCompleteGuideEvent) {

      }
    });
  }

  @Override public String getFragmentName() {
    return GymDetailFragment.class.getName();
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (dialogList != null) {
      dialogList.dismiss();
      //品牌管理
      if (getActivity() instanceof MainActivity && position == 0) {
        gymDetailPresenter.manageBrand();
      }
    }
  }

  void onHowtoUse() {
    WebActivity.startWeb(Router.WEB_HOW_TO_USE, getContext());
  }

  /**
   * 超级管理员数据
   */
  @Override public void onSuperUser(Staff su) {
    gymSu.setText("超级管理员：" + su.getUsername());
  }

  /**
   * 功能呢点击
   */
  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null) return true;
    if (item instanceof GymFuntionItem) {
      String name = ((GymFuntionItem) adapter.getItem(position)).getGymFuntion().getModuleName();
      if (!TextUtils.isEmpty(name)) {
        if (name.equalsIgnoreCase("more")) {
          if (getActivity() instanceof GymActivity) {
            ViewCompat.setTransitionName(recycleview, "funcitonView");
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(recycleview, "funcitonView")
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                    R.anim.slide_left_in, R.anim.slide_right_out)
                .add(mCallbackActivity.getFragId(), new GymMoreFragment())
                .addToBackStack(null)
                .commit();
          } else {
            Intent toMore = new Intent(getActivity(), GymActivity.class);
            toMore.putExtra(GymActivity.GYM_TO, GymActivity.GYM_MORE);
            startActivity(toMore);
          }
        } else {
          if (GymFunctionFactory.getModuleStatus(name) > 0 && !gymWrapper.isPro()) {
            UpgradeInfoDialogFragment.newInstance(QRActivity.getIdentifyKey(name))
                .show(getFragmentManager(), "");
            return true;
          }
          gymFunctionFactory.getJumpIntent(name, gymWrapper.getCoachService(),
              gymWrapper.getBrand(), new GymStatus.Builder().build(), this);
        }
      }
    } else if (item instanceof ButtonItem) {
      guideToStudentPreview();
    } else if (item instanceof SimpleTextItemItem) {
      onHowtoUse();
    }
    return true;
  }

  private List<Fragment> getFragments(HomeStatement statement) {
    List<Fragment> fragments = new ArrayList<>();
    pos.clear();
    if (statement.shop_turnovers != null
        && statement.shop_turnovers.date_counts != null
        && !statement.shop_turnovers.date_counts.isEmpty()) {
      TurnoverBarChartFragment turnoverBarChartFragment = new TurnoverBarChartFragment();
      fragments.add(turnoverBarChartFragment);
      pos.add(4);
    }
    if (statement.new_sells != null) {
      BaseStatementChartFragment fragment = new BaseStatementChartFragmentBuilder(0).build();
      fragments.add(fragment);
      pos.add(0);
    }
    if (statement.new_orders != null) {
      BaseStatementChartFragment fragment = new BaseStatementChartFragmentBuilder(1).build();
      fragments.add(fragment);
      pos.add(1);
    }
    if (statement.new_checkin != null) {
      BaseStatementChartFragment fragment = new BaseStatementChartFragmentBuilder(2).build();
      fragments.add(fragment);
      pos.add(2);
    }
    if (statement.new_users != null) {
      BaseStatementChartFragment fragment = new BaseStatementChartFragmentBuilder(3).build();
      fragments.add(fragment);
      pos.add(3);
    }
    return fragments;
  }

  private List<Integer> pos = new ArrayList<>();

  /**
   * 报表适配器
   */
  class GymDetailChartAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;

    public GymDetailChartAdapter(FragmentManager fm, HomeStatement statement) {
      super(fm);
      this.mFm = fm;
      this.statement = statement;
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Override public Fragment getItem(int position) {

      Integer integer = pos.get(position);
      Fragment fragment = fragments.get(position);
      if (statement == null) return fragment;
      switch (integer) {
        case 4:
          ((TurnoverBarChartFragment) fragment).setBarChartData(statement.shop_turnovers.getData());
          break;
        case 0:
          ((BaseStatementChartFragment) fragment).doData(statement.new_sells.getData());
          ;
          break;
        case 1:
          ((BaseStatementChartFragment) fragment).doData(statement.new_orders.getData());
          break;
        case 2:
          ((BaseStatementChartFragment) fragment).doData(statement.new_checkin.getData());
          break;
        case 3:
          ((BaseStatementChartFragment) fragment).doData(statement.new_users.getData());
          break;
      }
      return fragment;
    }

    public void setHomeStatment(HomeStatement statement) {
      this.statement = statement;
    }

    private HomeStatement statement;

    @Override public long getItemId(int position) {
      return position;
    }
  }

  /**
   * 记录神策的公共事件
   */
  void registeSensors() {
    try {
      JSONObject properties = new JSONObject();
      properties.put("qc_shop_id", gymWrapper.shop_id());
      properties.put("qc_brand_id", gymWrapper.brand_id());
      properties.put("qc_gym_id", gymWrapper.getGymId());
      SensorsDataAPI.sharedInstance(getContext()).registerSuperProperties(properties);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
