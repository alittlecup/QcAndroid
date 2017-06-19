package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.model.responese.HomeStatement;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.Prefer;
import cn.qingchengfit.staffkit.constant.Router;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventChartTitle;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.rxbus.event.EventLoginChange;
import cn.qingchengfit.staffkit.rxbus.event.GoToGuideEvent;
import cn.qingchengfit.staffkit.rxbus.event.RxCompleteGuideEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.GymDetailShowGuideDialogFragment;
import cn.qingchengfit.staffkit.views.PopFromBottomActivity;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.WebActivityForGuide;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import cn.qingchengfit.staffkit.views.charts.BaseStatementChartFragment;
import cn.qingchengfit.staffkit.views.charts.BaseStatementChartFragmentBuilder;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.gym.items.GymFuntionItem;
import cn.qingchengfit.staffkit.views.gym.upgrate.GymExpireFragment;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.staffkit.views.login.SplashActivity;
import cn.qingchengfit.staffkit.views.main.SettingFragment;
import cn.qingchengfit.staffkit.views.setting.BrandManageActivity;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpActivity;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
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

    @BindView(R.id.student_preview) TextView studentPreview;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.shop_img) ImageView shopImg;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    //@BindView(R.id.root_scroll) NestedScrollView rootScroll;
    @BindView(R.id.gym_layout) LinearLayout gymLayout;
    @BindView(R.id.toolbar_left) TextView toolbarLeft;
    //@BindView(R.id.swipe_to_refresh) SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.layout_collapsed) AppBarLayout layoutCollapsed;
    @BindView(R.id.schedule_notification_count) TextView scheduleNotificationCount;
    @BindView(R.id.vp_charts) ViewPager vpCharts;
    @BindView(R.id.indicator) CircleIndicator indicator;
    @BindView(R.id.gym_name) TextView gymName;
    @BindView(R.id.gym_su) TextView gymSu;
    @BindView(R.id.recharge) Button mRechargeBtn;
    @BindView(R.id.btn_how_to_use) TextView btnHowToUse;
    @BindView(R.id.tag_pro) ImageView tagPro;

    @Inject GymDetailPresenter gymDetailPresenter;
    @Inject RestRepository restRepository;
    @Inject GymWrapper gymWrapper;
    @Inject GymMoreFragment gymMoreFragment;
    @Inject SerPermisAction serPermisAction;

    private String mCopyUrl;
    private Observable<RxCompleteGuideEvent> mObGuideComplete;
    private GymMoreAdapter adapter;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();

    private DialogList dialogList;
    private QuitGymFragment quitDialog;
    private GymDetailChartAdapter mChartAdapter;
    /**
     * 即将到期提醒
     */
    private GymExpireFragment mGymExpireDialog;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChartAdapter = new GymDetailChartAdapter(getChildFragmentManager());
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(gymDetailPresenter, this);
        initToolbar(toolbar);
        initView();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        RxBusAdd(EventChartTitle.class).subscribe(new Action1<EventChartTitle>() {
            @Override public void call(EventChartTitle eventChartTitle) {
                switch (eventChartTitle.getChartType()) {
                    case 1:
                        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.COST_REPORT)) {
                            return;
                        }
                        Intent toStatement = new Intent(getActivity(), ContainerActivity.class);
                        toStatement.putExtra(Configs.ROUTER, GymFunctionFactory.MODULE_FINANCE_COURSE);
                        startActivity(toStatement);

                        break;
                    case 2:
                        if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.CHECKIN_REPORT)) {
                            return;
                        }
                        Intent toSignIn = new Intent(getActivity(), ContainerActivity.class);
                        toSignIn.putExtra(Configs.ROUTER, GymFunctionFactory.MODULE_FINANCE_SIGN_IN);
                        startActivity(toSignIn);
                        break;
                    case 3:

                        if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS)) {
                            showAlert(R.string.sorry_for_no_permission);
                            return;
                        }
                        Intent toFollow = new Intent(getActivity(), FollowUpActivity.class);
                        startActivity(toFollow);

                        break;
                    default:
                        if (!serPermisAction.check(PermissionServerUtils.SALES_REPORT)) {
                            showAlert(R.string.sorry_for_no_permission);
                            return;
                        }
                        Intent toSale = new Intent(getActivity(), ContainerActivity.class);
                        toSale.putExtra(Configs.ROUTER, GymFunctionFactory.MODULE_FINANCE_SALE);
                        startActivity(toSale);

                        break;
                }
            }
        });
        RxBusAdd(EventFreshCoachService.class).subscribe(new Action1<EventFreshCoachService>() {
            @Override public void call(EventFreshCoachService eventFreshCoachService) {
                gymDetailPresenter.getGymWelcome();
                adapter.setTag("isPro", true);
            }
        });
        RxBusAdd(GoToGuideEvent.class).subscribe(new Action1<GoToGuideEvent>() {
            @Override public void call(GoToGuideEvent goToGuideEvent) {
                onHowtoUse();
            }
        });
        RxBusAdd(EventLoginChange.class).delay(600, TimeUnit.MILLISECONDS).subscribe(new Action1<EventLoginChange>() {
            @Override public void call(EventLoginChange eventLoginChange) {
                setGymInfo();
            }
        });
        return view;
    }

    /**
     * 刷新健身房的权限和数据
     */
    @Override public void onResume() {
        super.onResume();
        gymDetailPresenter.updatePermission();
    }

    @OnClick({ R.id.toolbar_title, R.id.down }) public void onTitleClick() {
        ViewCompat.setPivotY(gymLayout, 0);
        gymLayout.setPivotY(0);

        if (gymLayout.getVisibility() == View.VISIBLE) {

            ViewCompat.animate(gymLayout).scaleY(0).setDuration(500L).setListener(new ViewPropertyAnimatorListener() {
                @Override public void onAnimationStart(View view) {

                }

                @Override public void onAnimationEnd(View view) {
                    if (view.getScaleY() == 0) {
                        if (gymLayout != null) gymLayout.setVisibility(View.GONE);
                    }
                }

                @Override public void onAnimationCancel(View view) {

                }
            }).start();
            ViewCompat.setRotation(down, 0);
        } else {
            ViewCompat.setScaleY(gymLayout, 0);
            gymLayout.setVisibility(View.VISIBLE);
            ViewCompat.animate(gymLayout).scaleY(1).setDuration(500L).start();
            ViewCompat.setRotation(down, 180);
        }
        ViewCompat.animate(down).rotationBy(180).setDuration(300).start();
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarLayout.setVisibility(View.VISIBLE);
        if (getActivity() instanceof MainActivity) {
            toolbarLeft.setVisibility(View.VISIBLE);
        } else {
            toolbarLeft.setVisibility(View.GONE);
            super.initToolbar(toolbar);
        }
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), new SettingFragment())
                        .addToBackStack(null)
                        .commit();
                } else if (item.getItemId() == R.id.action_notifi) {

                } else if (item.getItemId() == R.id.action_flow) {
                    if (dialogList == null) {
                        dialogList = new DialogList(getContext());
                        List<String> actions = new ArrayList<String>();
                        if (getActivity() instanceof MainActivity) actions.add("品牌管理");
                        actions.add("离职退出该场馆");
                        actions.add("取消");
                        dialogList.list(actions, GymDetailFragment.this);
                    }
                    dialogList.show();
                }
                return true;
            }
        });
    }

    private void initView() {
        btnHowToUse.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_vector_info_grey), null,
            null, null);
        datas.clear();
        datas.add(new GymFuntionItem(new GymFuntion.Builder().img(R.drawable.ic_function_more).text(R.string.more).build()));
        adapter = new GymMoreAdapter(datas, this);
        adapter.setTag("isPro", gymWrapper.isPro());
        SmoothScrollGridLayoutManager layoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
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

                                                  vpCharts.setOffscreenPageLimit(3);
                                                  vpCharts.setAdapter(mChartAdapter);
                                                  indicator.setViewPager(vpCharts);
                                              }
                                          }
                                      }

            );

        onGymInfo(gymWrapper.getCoachService());
    }

    void notifyMyFunctions() {
        int addcount = ((4 - (datas.size()) % 4) % 4);
        for (int i = 0; i < addcount; i++) {
            datas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion(GymFunctionFactory.MODULE_NONE)));
        }
        adapter.notifyDataSetChanged();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
        if (requestCode == RESULT_STAFF_MANAGE && resultCode == 111) {
            onQuitGym();
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
        RxView.clicks(studentPreview).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override public void call(Void aVoid) {
                Intent toWebForGuide = new Intent(getActivity(), WebActivityForGuide.class);
                toWebForGuide.putExtra("url", url);
                toWebForGuide.putExtra("copyurl", mCopyUrl);
                startActivity(toWebForGuide);
            }
        });
    }

    @Override public void guideToWechat(final String url) {

    }

    @Override public void setBanner(List<Banner> strings) {
        //swipeToRefresh.setRefreshing(false);
    }

    @Override public void setInfo(HomeStatement stats) {
        hideLoading();
        if (stats != null) {
            if (stats.new_users != null) mChartAdapter.setData(3, stats.new_users.date_counts);
            if (stats.new_sells != null) mChartAdapter.setData(0, stats.new_sells.date_counts);
            if (stats.new_orders != null) mChartAdapter.setData(1, stats.new_orders.date_counts);
            if (stats.new_checkin != null) mChartAdapter.setData(2, stats.new_checkin.date_counts);
        }
    }

    @Override public void setRecharge(final GymDetail.Recharge recharge) {
        hideLoading();
        gymWrapper.setOutOfDate(recharge.trial_date < 0);
        mRechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent toRenewal = new Intent(getActivity(), PopFromBottomActivity.class);
                toRenewal.putExtra(BaseActivity.ROUTER, Router.BOTTOM_RENEWAL);
                startActivity(toRenewal);
            }
        });
    }

    @Override public void showOutDataTime() {

    }

    @Override public void hideOutDateTime() {

    }

    @Override public void onGymInfo(final CoachService coachService) {
        hideLoading();
        //swipeToRefresh.setRefreshing(false);
        if (coachService == null) return;
        gymWrapper.setCoachService(coachService);
        CoachService gym = coachService;
        if (GymUtils.getSystemEndDay(gymWrapper.getCoachService()) >= 0 && GymUtils.getSystemEndDay(gymWrapper.getCoachService()) <= 7) {
            if (!cn.qingchengfit.utils.PreferenceUtils.getPrefString(getContext(), Prefer.SYSTEM_END_HINT, "")
                .equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))) {
                if (mGymExpireDialog == null) mGymExpireDialog = new GymExpireFragment();
                if (!mGymExpireDialog.isVisible()) mGymExpireDialog.show(getFragmentManager(), "");
                cn.qingchengfit.utils.PreferenceUtils.setPrefString(getContext(), Prefer.SYSTEM_END_HINT,
                    DateUtils.Date2YYYYMMDD(new Date()));
            }
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
            mRechargeBtn.setTextColor(cn.qingchengfit.utils.CompatUtils.getColor(getContext(), R.color.colorPrimary));
            tagPro.setImageResource(R.drawable.ic_pro_green);
        } else {
            mRechargeBtn.setBackgroundResource(R.drawable.btn_prime);
            mRechargeBtn.setText("升级");
            mRechargeBtn.setTextColor(cn.qingchengfit.utils.CompatUtils.getColor(getContext(), R.color.white));
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
                    if (!TextUtils.isEmpty((String) o)) datas.add(new GymFuntionItem(GymFunctionFactory.instanceGymFuntion((String) o)));
                }
            }
        }
        datas.add(
            new GymFuntionItem(new GymFuntion.Builder().img(R.drawable.ic_function_more).moduleName("more").text(R.string.more).build()));
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
            cn.qingchengfit.utils.ToastUtils.show("您没有改场馆管理权限");
            return;
        }
        Intent toBrandManage = new Intent(getActivity(), BrandManageActivity.class);
        toBrandManage.putExtra(Configs.EXTRA_BRAND, gymWrapper.getBrand());
        startActivity(toBrandManage);
    }

    @Override public void onSpecialPoint(int count) {

    }

    /**
     * 单场馆新增健身房
     */
    @OnClick(R.id.toolbar_left) public void onLeft() {

        //新增健身房
        SystemInitBody body;
        if (TextUtils.isEmpty(PreferenceUtils.getPrefString(getContext(), "init", ""))) {
            body = new SystemInitBody();
        } else {
            body = new Gson().fromJson(PreferenceUtils.getPrefString(getContext(), "init", ""), SystemInitBody.class);
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
            } else if ((getActivity() instanceof MainActivity && position == 1) || (position == 0)) {//离职退出场馆
                if (quitDialog == null) {
                    quitDialog = new QuitGymFragment();
                    quitDialog.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            gymDetailPresenter.quitGym();
                        }
                    });
                }
                quitDialog.show(getFragmentManager(), "");
            } else {

            }
        }
    }

    @OnClick(R.id.btn_how_to_use) public void onHowtoUse() {
        WebActivity.startWeb(Router.WEB_HOW_TO_USE, getContext());
    }

    /**
     * 退出健身房
     */

    @Override public void onQuitGym() {
        if (getActivity() instanceof MainActivity) {
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
        }
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
        if (adapter.getItem(position) instanceof GymFuntionItem) {
            String name = ((GymFuntionItem) adapter.getItem(position)).getGymFuntion().getModuleName();
            if (!TextUtils.isEmpty(name)) {
                if (name.equalsIgnoreCase("more")) {
                    if (getActivity() instanceof GymActivity) {
                        ViewCompat.setTransitionName(recycleview, "funcitonView");
                        getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addSharedElement(recycleview, "funcitonView")
                            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                            .add(mCallbackActivity.getFragId(), gymMoreFragment)
                            .addToBackStack(null)
                            .commit();
                    } else {
                        Intent toMore = new Intent(getActivity(), GymActivity.class);
                        toMore.putExtra(GymActivity.GYM_TO, GymActivity.GYM_MORE);
                        startActivity(toMore);
                    }
                } else {
                    if (GymFunctionFactory.getModuleStatus(name) > 0 && !gymWrapper.isPro()) {
                        new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
                        return true;
                    }
                    GymFunctionFactory.getJumpIntent(name, gymWrapper.getCoachService(), gymWrapper.getBrand(),
                        new GymStatus.Builder().build(), this);
                }
            }
        }
        return true;
    }

    /**
     * 报表适配器
     */
    class GymDetailChartAdapter extends FragmentPagerAdapter {
        private FragmentManager mFm;

        public GymDetailChartAdapter(FragmentManager fm) {
            super(fm);
            this.mFm = fm;
        }

        @Override public int getCount() {
            return 4;
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
