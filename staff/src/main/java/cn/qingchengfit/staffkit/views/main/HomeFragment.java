package cn.qingchengfit.staffkit.views.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.model.responese.HomeStatement;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.events.EventChartTitle;
import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragment;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragmentBuilder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.EventBrandChange;
import cn.qingchengfit.staffkit.views.ChooseBrandFragment;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.ImageTextBean;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.custom.ItemDecorationAlbumColumns;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.gym.GridImageTextAdapter;
import cn.qingchengfit.staffkit.views.setting.BrandManageActivity;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.tbruyelle.rxpermissions.RxPermissions;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

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
 * Created by Paper on 16/2/16 2016.
 */
public class HomeFragment extends BaseFragment
    implements HomeView, FlexibleAdapter.OnItemClickListener {

  SwipeRefreshLayout refresh;
  RecyclerView recycleview1;
  ViewPager vpCharts;
  CircleIndicator indicator;
  FrameLayout fragChooseBrand;
  FrameLayout layoutBrands;
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView down;
  TextView tvLeft;
  HomeChartAdapter mHomeChartAdapter;
  GymsFragment gymsFragment;  //场馆列表
  @Inject GymWrapper gymWrapper;
  @Inject HomePresenter homePresenter;
  @Inject SerPermisAction serPermisAction;
  private List<ImageTextBean> datas1 = new ArrayList<>();
  private List<AbstractFlexibleItem> adapterDatas = new ArrayList<>();
  private HomeStatement mHomeStatement;
  private Observable<EventChartTitle> mObChartTitle;
  LinearLayout llShowGym;
  ImageView imgPhoto;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mHomeChartAdapter = new HomeChartAdapter(getChildFragmentManager());
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    recycleview1 = (RecyclerView) view.findViewById(R.id.recycleview1);
    vpCharts = (ViewPager) view.findViewById(R.id.vp_charts);
    indicator = (CircleIndicator) view.findViewById(R.id.indicator);
    fragChooseBrand = (FrameLayout) view.findViewById(R.id.frag_choose_brand);
    layoutBrands = (FrameLayout) view.findViewById(R.id.layout_brands);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    down = (ImageView) view.findViewById(R.id.down);
    tvLeft = (TextView) view.findViewById(R.id.tv_left);
    llShowGym = view.findViewById(R.id.show_gym);
    imgPhoto = view.findViewById(R.id.img_gym_photo);
    view.findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickBrandManage();
      }
    });
    view.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickTitle();
      }
    });
    view.findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickTitle();
      }
    });
    view.findViewById(R.id.layout_brands).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBgClick();
      }
    });

    initToolbar(toolbar);
    delegatePresenter(homePresenter, this);
    initView();
    isLoading = true;
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.setNavigationIcon(null);
    toolbarTitile.setText(gymWrapper.brand_name());
    down.setVisibility(View.VISIBLE);
    tvLeft.setText(R.string.brand_manange);
    tvLeft.setVisibility(View.GONE);
    llShowGym.setVisibility(View.VISIBLE);
    llShowGym.setOnClickListener(v -> {
      if (!TextUtils.isEmpty(gymWrapper.getBrand().previewUrl)) {
        WebActivity.startWeb(gymWrapper.getBrand().previewUrl, getContext());
      } else {
        ToastUtils.show("暂无预览地址");
      }
    });
    if(gymWrapper.getBrand()!=null){
      PhotoUtils.smallCircle(imgPhoto, gymWrapper.getBrand().getPhoto());
    }

  }

  public void clickBrandManage() {
    if (gymWrapper.inBrand()) {
      if (!gymWrapper.getBrand().has_add_permission) {
        cn.qingchengfit.utils.ToastUtils.show(getString(R.string.alert_no_manage_permission));
        return;
      }
      Intent toBrandManage = new Intent(getActivity(), BrandManageActivity.class);
      toBrandManage.putExtra(Configs.EXTRA_BRAND, gymWrapper.getBrand());
      startActivity(toBrandManage);
    } else {
      Intent toGuide = new Intent(getActivity(), GuideActivity.class);
      toGuide.putExtra("isAdd", true);
      startActivity(toGuide);
    }
  }

  /**
   * 点击选择品牌
   */
  public void onClickTitle() {
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_choose_brand, new ChooseBrandFragment())
        .commit();
    ViewCompat.setPivotY(fragChooseBrand, 0);
    if (layoutBrands.getVisibility() == View.VISIBLE) {
      onBgClick();
    } else {
      layoutBrands.setVisibility(View.VISIBLE);
      ViewCompat.animate(layoutBrands).alpha(1).setDuration(300).start();
      ViewCompat.animate(fragChooseBrand).scaleY(1).setDuration(300).start();
    }
  }

  /**
   * 关闭选择品牌窗口
   */
  public void onBgClick() {
    if (layoutBrands.getVisibility() == View.VISIBLE) {
      ViewCompat.animate(layoutBrands)
          .alpha(0)
          .setDuration(300)
          .setListener(new ViewPropertyAnimatorListener() {
            @Override public void onAnimationStart(View view) {
            }

            @Override public void onAnimationEnd(View view) {
              if (layoutBrands != null && layoutBrands.getAlpha() == 0) {
                layoutBrands.setVisibility(View.GONE);
              }
            }

            @Override public void onAnimationCancel(View view) {
            }
          })
          .start();
      ViewCompat.animate(fragChooseBrand).scaleY(0).setDuration(300).start();
    }
  }

  @Override public void onResume() {
    super.onResume();
    gymWrapper.setCoachService(null);
    homePresenter.updatePermission();
    /*
     * 图表点击
     */
    mObChartTitle = RxBus.getBus().register(EventChartTitle.class);
    mObChartTitle.subscribe(new Action1<EventChartTitle>() {
      @Override public void call(EventChartTitle eventChartTitle) {
        switch (eventChartTitle.getChartType()) {
          case 1:
            if (serPermisAction.checkNoOne(PermissionServerUtils.COST_REPORT)) {
              return;
            }
            Intent toStatement = new Intent(getActivity(), ContainerActivity.class);
            toStatement.putExtra("router", QRActivity.MODULE_FINANCE_COURSE);
            startActivity(toStatement);
            break;
          case 2:
            if (serPermisAction.checkNoOne(PermissionServerUtils.CHECKIN_REPORT)) {
              return;
            }
            Intent toSignIn = new Intent(getActivity(), ContainerActivity.class);
            toSignIn.putExtra("router", QRActivity.MODULE_FINANCE_SIGN_IN);
            startActivity(toSignIn);
            break;

          default:
            if (serPermisAction.checkNoOne(PermissionServerUtils.SALES_REPORT)) {
              return;
            }
            Intent toSale = new Intent(getActivity(), ContainerActivity.class);
            toSale.putExtra("router", QRActivity.MODULE_FINANCE_SALE);
            startActivity(toSale);

            break;
        }
      }
    });
  }

  @Override public void onPause() {
    super.onPause();
    RxBus.getBus().unregister(EventChartTitle.class.getName(), mObChartTitle);
  }

  private void initView() {
    //品牌切换
    RxBusAdd(EventBrandChange.class).subscribe(eventBrandChange -> {
      initToolbar(toolbar);
      onBgClick();
      homePresenter.updatePermission();
      if (gymsFragment != null) gymsFragment.refresh();
    });
    refresh.setColorSchemeColors(CompatUtils.getColor(getContext(), R.color.colorPrimary));
    refresh.setOnRefreshListener(() -> {
      homePresenter.updatePermission();
      RxBus.getBus().post(new EventFreshGyms());
    });
    initViewOfChain();
    homePresenter.queryBrands();
  }

  /**
   * 连锁运营
   */
  private void initViewOfChain() {
    datas1.clear();
    datas1.add(
        new ImageTextBean(R.drawable.ic_card_type, getString(R.string.student_general_card)));
    datas1.add(new ImageTextBean(R.drawable.ic_weight, getString(R.string.shared_course)));
    datas1.add(new ImageTextBean(R.drawable.ic_marketing_activity,
        getString(R.string.markting_acitivies)));
    GridImageTextAdapter adapter2 = new GridImageTextAdapter(datas1);
    recycleview1.setLayoutManager(new GridLayoutManager(getContext(), 3));
    recycleview1.addItemDecoration(new ItemDecorationAlbumColumns(1, 3));
    recycleview1.setHasFixedSize(true);
    recycleview1.setNestedScrollingEnabled(false);
    recycleview1.setAdapter(adapter2);
    adapter2.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
        switch (pos) {
          case 0:
            toCardType();
            break;
          case 1:
            toCourse();
            break;
          case 2:
            goQrScan(QRActivity.MODULE_ACTIVITY, PermissionServerUtils.ACTIVITY_SETTING);
            break;

          default:
            break;
        }
      }
    });
    vpCharts.setOffscreenPageLimit(2);
    vpCharts.setAdapter(mHomeChartAdapter);
    indicator.setViewPager(vpCharts);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  /**
   * 场馆列表 {@link GymsFragment}
   */
  private void initGyms() {
    gymsFragment = new GymsFragment();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.gyms_frag, gymsFragment)
        .commitAllowingStateLoss();
  }

  @Override public void setInfo(HomeStatement stats) {
    refresh.setRefreshing(false);
    if (stats != null) {
      if (stats.new_sells != null) mHomeChartAdapter.setData(0, stats.new_sells.date_counts);
      if (stats.new_checkin != null) mHomeChartAdapter.setData(2, stats.new_checkin.date_counts);
      if (stats.new_orders != null) mHomeChartAdapter.setData(1, stats.new_orders.date_counts);
    }
  }

  @Override public boolean onItemClick(int position) {
    return true;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void setBanner(List<Banner> banner) {

  }

  @Override public void setSpecialPoint(int count) {
  }

  @Override public void onBrands(List<Brand> brands) {
    if (brands == null || brands.size() == 0) {
      goGuide();
    } else {
      String mCurId = PreferenceUtils.getPrefString(getContext(), Configs.CUR_BRAND_ID, "");
      if (StringUtils.isEmpty(mCurId)) {
        for (int i = 0; i < brands.size(); i++) {
          if (brands.get(i).getGym_count() > 0) {
            gymWrapper.setBrand(brands.get(i));
            break;
          }
        }
      } else {
        for (int i = 0; i < brands.size(); i++) {
          if (brands.get(i).getId().equalsIgnoreCase(mCurId)) {
            gymWrapper.setBrand(brands.get(i));
            break;
          }
        }
      }
      PreferenceUtils.setPrefString(getContext(), Configs.CUR_BRAND_ID, gymWrapper.brand_id());
      initToolbar(toolbar);
      homePresenter.updatePermission();
      initGyms();
    }
  }

  @Override public void goGuide() {//从所有场馆那边判定是否需要去引导
  }

  @Override public void estGyms(List<CoachService> coachServiceList) {

  }

  @Override public String getFragmentName() {
    return HomeFragment.class.getName();
  }

  /**
   * 跳转会员卡种类
   */
  private void toCardType() {
    if (serPermisAction.checkNoOne(PermissionServerUtils.CARDSETTING)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    routeTo("card", "/brand/cardtpl/list/", null);
  }

  /**
   * 跳转课程种类
   */
  private void toCourse() {
    if (serPermisAction.checkNoOne(PermissionServerUtils.PRISETTING) && serPermisAction.checkNoOne(
        PermissionServerUtils.TEAMSETTING)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    routeTo("course", "/home/brand/", null);
  }

  private void goActivity(Intent it) {
    startActivity(it);
  }

  private void goQrScan(final String toUrl, String Permission) {
    if (serPermisAction.checkAtLeastOne(Permission) || Permission == null) {
      new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
          .subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
              if (aBoolean) {
                Intent toScan = new Intent(getActivity(), QRActivity.class);
                toScan.putExtra(QRActivity.LINK_URL, Configs.Server
                    + "app2web/?brand_id="
                    + gymWrapper.brand_id()
                    + "&module="
                    + toUrl);
                startActivity(toScan);
              } else {
                ToastUtils.show(getString(R.string.please_open_camera));
              }
            }
          });
    } else {
      showAlert(R.string.alert_permission_forbid);
    }
  }

  class HomeChartAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;

    public HomeChartAdapter(FragmentManager fm) {
      super(fm);
      this.mFm = fm;
    }

    @Override public int getCount() {
      return 3;
    }

    @Override public Fragment getItem(int position) {
      return new BaseStatementChartFragmentBuilder(position).build();
    }

    @Override public long getItemId(int position) {
      return position;
    }

    public String getItemName(int position) {
      return "android:switcher:" + R.id.vp_charts + ":" + position;
    }

    public void setData(int pos, List<FollowUpDataStatistic.DateCountsBean> datas) {
      Fragment f = mFm.findFragmentByTag(getItemName(pos));
      if (f != null && f instanceof BaseStatementChartFragment) {
        ((BaseStatementChartFragment) f).doData(datas);
      }
    }
  }
}
