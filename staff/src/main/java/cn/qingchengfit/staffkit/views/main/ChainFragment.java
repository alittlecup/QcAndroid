package cn.qingchengfit.staffkit.views.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventGoNotification;
import cn.qingchengfit.staffkit.rxbus.event.EventNewPush;
import cn.qingchengfit.staffkit.views.adapter.MainPagerAdapter;
import cn.qingchengfit.staffkit.views.custom.TabView;
import cn.qingchengfit.staffkit.views.notification.NotificationActivity;
import cn.qingchengfit.staffkit.views.setting.BrandManageActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.HashMap;
import java.util.Locale;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/10.
 */
public class ChainFragment extends BaseFragment {

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabview) TabView tabview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.frag_choose_brand) FrameLayout mChooseBrandLayout;

    @Inject RestRepository mRestRepository;
    @BindView(R.id.schedule_notification_count) TextView scheduleNotificationCount;
    @BindView(R.id.brand_manage) TextView brandManage;
    @BindView(R.id.layout_brands) FrameLayout layoutBrands;
    private String url;
    private String mCurBrandName;
    private Brand mCurBrand;
    private View.OnClickListener mBrandListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            //getFragmentManager().beginTransaction()
            //        .replace(R.id.frag_choose_brand, new ChooseBrandFragmentBuilder().brand(mCurBrand).build())
            //        .commit();
            ViewCompat.setPivotY(mChooseBrandLayout, 0);
            if (layoutBrands.getVisibility() == View.VISIBLE) {
                onBgClick();
            } else {
                layoutBrands.setVisibility(View.VISIBLE);
                ViewCompat.animate(layoutBrands).alpha(1).setDuration(300).start();
                ViewCompat.animate(mChooseBrandLayout).scaleY(1).setDuration(300).start();
            }
        }
    };
    private MainPagerAdapter vpAdapter;

    public static ChainFragment newInstance() {
        Bundle args = new Bundle();
        ChainFragment fragment = new ChainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChainFragment newInstance(String open_url) {

        Bundle args = new Bundle();
        args.putString("open_url", open_url);
        ChainFragment fragment = new ChainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCurBrand(Brand curBrand) {
        mCurBrand = curBrand;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chain, container, false);
        unbinder = ButterKnife.bind(this, view);
        url = getArguments().getString("open_url");
        initTab();
        RxBusAdd(EventGoNotification.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EventGoNotification>() {
            @Override public void call(EventGoNotification eventGoNotification) {
                if (mCurBrand != null) {
                    Intent to = new Intent(getActivity(), NotificationActivity.class);
                    to.putExtra(Configs.EXTRA_BRAND, mCurBrand);
                    to.putExtra(Configs.EXTRA_GYM_SERVICE, new CoachService());
                    to.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
                    startActivity(to);
                }
            }
        });
        RxBusAdd(EventNewPush.class).subscribe(new Action1<EventNewPush>() {
            @Override public void call(EventNewPush eventNewPush) {
                getMessage();
            }
        });
        RxBusAdd(Brand.class).subscribe(new Action1<Brand>() {
            @Override public void call(Brand brandItemItem) {
                onBgClick();
            }
        });
        return view;
    }

    public void setMode(int status, int pos) {
        if (vpAdapter != null) {
            //vpAdapter.setStatus(status);
            viewpager.setCurrentItem(pos);
        }
    }

    @OnClick(R.id.layout_brands) public void onBgClick() {
        if (layoutBrands.getVisibility() == View.VISIBLE) {

            ViewCompat.animate(layoutBrands).alpha(0).setDuration(300).setListener(new ViewPropertyAnimatorListener() {
                @Override public void onAnimationStart(View view) {

                }

                @Override public void onAnimationEnd(View view) {
                    if (layoutBrands.getAlpha() == 0) {
                        layoutBrands.setVisibility(View.GONE);
                    }
                }

                @Override public void onAnimationCancel(View view) {

                }
            }).start();
            ViewCompat.animate(mChooseBrandLayout).scaleY(0).setDuration(300).start();
        }
    }

    private void initTab() {
        viewpager.setOffscreenPageLimit(2);
        vpAdapter = new MainPagerAdapter(getChildFragmentManager(), getContext());
        viewpager.setAdapter(vpAdapter);
        tabview.setViewPager(viewpager);
        tabview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onBgClick();
                if (positionOffset == 0f) {
                    if (position == 0) {
                        setBar(new ToolbarBean(mCurBrandName, true, mBrandListener, 0, null));
                        brandManage.setVisibility(View.VISIBLE);
                    } else if (position == 1) {
                        setBar(new ToolbarBean(getString(R.string.home_tab_special), false, null, 0, null));
                        brandManage.setVisibility(View.GONE);
                    } else {
                        setBar(new ToolbarBean(getString(R.string.home_tab_setting), false, null, 0, null));
                        brandManage.setVisibility(View.GONE);
                    }
                }
            }

            @Override public void onPageSelected(int position) {
                // POST 事件--更新小红点
            }

            @Override public void onPageScrollStateChanged(int state) {
            }
        });
        if (!TextUtils.isEmpty(url)) {
            tabview.setCurrentItem(1);
            WebActivity.startWeb(url, getActivity());
        }
    }

    /**
     * 请求接口，获取推送的message数据
     */
    public void getMessage() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("tab", "STAFF_0");
        RxRegiste(mRestRepository.getGet_api()
            .qcGetMessages(App.staffId, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<Notification>>() {
                @Override public void call(QcResponseData<Notification> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data.unread_count > 0 && toolbar.getMenu().hasVisibleItems()) {
                            scheduleNotificationCount.setVisibility(View.VISIBLE);
                            scheduleNotificationCount.setText(qcResponse.data.unread_count + "");
                        } else {
                            scheduleNotificationCount.setVisibility(View.GONE);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    ToastUtils.show("获取通知失败");
                }
            }));
    }

    @Override public void onResume() {
        super.onResume();
        getMessage();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                PreferenceUtils.setPrefString(getContext(), Configs.CUR_BRAND_ID, brand.getId());
                setBar(new ToolbarBean(brand.getName(), true, mBrandListener, 0, null));
                mCurBrandName = brand.getName();
                mCurBrand = brand;
                RxBus.getBus().post(brand);
            }
        }
    }

    public void setHomebar(String b, boolean showright) {
        if (viewpager.getCurrentItem() > 0) return;
        mCurBrandName = b;
        setBar(new ToolbarBean(b, showright, showright ? mBrandListener : null, 0, null));
    }

    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        if (bar.showRight) {
            down.setVisibility(View.VISIBLE);
        } else {
            down.setVisibility(View.GONE);
        }
        if (bar.onClickListener != null) {
            toolbarTitile.setOnClickListener(bar.onClickListener);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        } else {
            toolbar.inflateMenu(R.menu.menu_notification);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    RxBus.getBus().post(new EventGoNotification());
                    return false;
                }
            });
        }
    }

    @Override public String getFragmentName() {
        return ChainFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.brand_manage) public void onClick() {

        if (mCurBrand != null && mCurBrand.isHas_add_permission()) {
            Intent to = new Intent(getActivity(), BrandManageActivity.class);
            to.putExtra(Configs.EXTRA_BRAND, mCurBrand);
            startActivity(to);
        } else {
            //无权限
            String un = " ";
            if (mCurBrand != null && mCurBrand.getCreated_by() != null && mCurBrand.getCreated_by().getUsername() != null) {
                un = mCurBrand.getCreated_by().getUsername();
            } else {
                un = " ";
            }
            new MaterialDialog.Builder(getContext()).content(String.format(Locale.CHINA, "仅品牌创建人%s可编辑", un))
                .positiveText(R.string.common_comfirm)
                .show();
            ;
        }
    }
}
