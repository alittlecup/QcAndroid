package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.presenters.ModuleConfigsPresenter;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.rxbus.event.SignInLogEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInQrCodeEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInStudentItemClickEvent;
import cn.qingchengfit.staffkit.views.GenCodeActivity;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigListFragment;
import cn.qingchengfit.staffkit.views.signin.in.SignInFragment;
import cn.qingchengfit.staffkit.views.signin.out.SignOutFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/8/30.
 */
public class SignInHomeFragment extends BaseFragment implements SignInConfigPresenter.SignInConfigView, ModuleConfigsPresenter.MVPView {

    @BindView(R.id.tab) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewpager;

    //tab 的 adapter
    SignInViewpagerAdapter adapter;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepositoryV2 restRepository;

    @Inject SignInConfigPresenter presenter;
    @Inject ModuleConfigsPresenter moduleConfigsPresenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    public SignInHomeFragment() {
    }

    public static SignInHomeFragment newInstance() {
        SignInHomeFragment fragment = new SignInHomeFragment();
        return fragment;
    }

    public static SignInHomeFragment newInstance(int pos) {
        Bundle arg = new Bundle();
        SignInHomeFragment fragment = new SignInHomeFragment();
        arg.putInt("p", pos);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        delegatePresenter(moduleConfigsPresenter, this);
        showLoading();
        presenter.getCardCostList();
        moduleConfigsPresenter.getModuleConfigs();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(gymWrapper.name());
        toolbar.inflateMenu(R.menu.menu_config);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.CHECKIN_SETTING)) {
                    showAlert(R.string.alert_permission_forbid);
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.student_frag, new SigninConfigListFragment()).commit();
                }
                return true;
            }
        });
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInHomeFragment.class.getName();
    }

    private void initView() {

        viewpager.setOffscreenPageLimit(1);
        adapter = new SignInViewpagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        try {
            viewpager.setCurrentItem(getArguments().getInt("p"));
        } catch (Exception e) {

        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {
                AppUtils.hideKeyboard(getActivity());
            }
        });
    }

    private void initRxBus() {
        Observable<SignInStudentItemClickEvent> observable = RxBusAdd(SignInStudentItemClickEvent.class);
        observable.observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(new Action1<SignInStudentItemClickEvent>() {
                @Override public void call(SignInStudentItemClickEvent signInStudentItemClickEvent) {
                    if (signInStudentItemClickEvent.isSignIn()) {//手动签到
                        Intent it = new Intent(getActivity(), SignInManualActivity.class);
                        it.putExtra("student", signInStudentItemClickEvent.getStudentBean());
                        it.putExtra("isSignIn", true);
                        startActivity(it);
                    } else {//手动签出
                        Intent it = new Intent(getActivity(), SignInManualActivity.class);
                        it.putExtra("student", signInStudentItemClickEvent.getStudentBean());
                        it.putExtra("isSignIn", false);
                        startActivity(it);
                    }
                }
            });

        Observable<SignInQrCodeEvent> observableQrCode = RxBusAdd(SignInQrCodeEvent.class);
        observableQrCode.observeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io()).subscribe(new Action1<SignInQrCodeEvent>() {
            @Override public void call(final SignInQrCodeEvent signInQrCodeEvent) {
                if (signInQrCodeEvent.isSignIn()) {//手动签到
                    /**
                     * 去扫码
                     */
                    Intent toQr = new Intent(getActivity(), GenCodeActivity.class);
                    toQr.putExtra(GenCodeActivity.GEN_CODE_URL, SignInActivity.checkin_url);
                    toQr.putExtra(GenCodeActivity.GEN_CODE_TITLE, "签到二维码");
                    toQr.putExtra(GenCodeActivity.GEN_CODE_BG, R.drawable.bg_qr_code_signin);
                    getActivity().startActivity(toQr);
                    getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_hold);
                } else {//手动签出
                    /**
                     * 签出
                     */
                    Intent toQr = new Intent(getActivity(), GenCodeActivity.class);
                    toQr.putExtra(GenCodeActivity.GEN_CODE_URL, SignInActivity.checkout_url);
                    toQr.putExtra(GenCodeActivity.GEN_CODE_TITLE, "签出二维码");
                    getActivity().startActivity(toQr);
                    getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_hold);
                }
            }
        });

        Observable<SignInLogEvent> signInLogEventObservable = RxBusAdd(SignInLogEvent.class);
        signInLogEventObservable.observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(new Action1<SignInLogEvent>() {
                @Override public void call(SignInLogEvent signInLogEvent) {
                    getFragmentManager().beginTransaction()
                        .replace(mCallbackActivity.getFragId(), new SignInLogFragment())
                        .addToBackStack(null)
                        .commit();
                }
            });
    }

    @Override public void onGetSignInConfig(List<SignInConfig.Config> signInConfig) {

    }

    @Override public void onGetSignOutConfig(List<SignInConfig.Config> signInConfig) {

    }

    @Override public void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs) {
        hideLoading();
        // 选中的扣费设置在list前面,看第一个对象的状态来判断
        if (signInConfigs != null && signInConfigs.size() > 0) {
            if (signInConfigs.get(0).isSelected()) {
                PreferenceUtils.setPrefBoolean(getContext(), "showNotice" + App.staffId, true);
            } else {
                PreferenceUtils.setPrefBoolean(getContext(), "showNotice" + App.staffId, false);
            }
        }
    }

    @Override public void onCheckInConfigComplete() {

    }

    @Override public void onCheckOutConfigComplete() {

    }

    @Override public void onCostConfigSuccess() {

    }

    @Override public void onFail() {
        hideLoading();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onModuleStatus(ScoreStatus.ModuleBean moduleBean) {
        if (!moduleBean.isCheckin()) {
            getFragmentManager().beginTransaction().replace(R.id.student_frag, new SignInCloseFragment()).commitAllowingStateLoss();
        } else {
            HashMap<String, Object> params = gymWrapper.getParams();
            Observable observable = ((Get_Api) restRepository.getApi(Get_Api.class)).qcGetSignInCostConfig(App.staffId, params);
            RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInCardCostBean.Data>() {
                @Override protected void _onNext(SignInCardCostBean.Data signInCardCostBean) {
                    boolean setted = false;
                    if (signInCardCostBean.card_costs != null) {
                        for (int i = 0; i < signInCardCostBean.card_costs.size(); i++) {
                            if (signInCardCostBean.card_costs.get(i).isSelected()) {
                                setted = true;
                                break;
                            }
                        }
                    }
                    if (setted) {
                        initView();
                        initRxBus();
                    } else {
                        getFragmentManager().beginTransaction()
                            .replace(R.id.student_frag, new SignInCloseFragment())
                            .commitAllowingStateLoss();
                    }
                }

                @Override protected void _onError(String message) {
                    Timber.e(message);
                }
            }));
        }
    }

    @Override public void onStatusSuccess() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * tablayout -- adapter
     */
    class SignInViewpagerAdapter extends FragmentStatePagerAdapter {

        public SignInViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

        @Override public Fragment getItem(int position) {
            if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.CHECKIN_HELP_CAN_WRITE)) {
                return SignInPermisionFragment.newInstance();
            } else {
                if (position == 0) {
                    return SignInFragment.newInstance();
                } else {
                    return SignOutFragment.newInstance();
                }
            }
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override public int getCount() {
            return 2;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.sign_type_in);
                case 1:
                    return getString(R.string.sign_type_out);
                default:
                    return getString(R.string.sign_type_in);
            }
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_tab_item, null);
            TextView tv_sign_tab_title = (TextView) view.findViewById(R.id.tv_sign_tab_title);

            switch (position) {
                case 0:
                    tv_sign_tab_title.setText(getContext().getString(R.string.sign_type_in));
                    Drawable drawableIn = getResources().getDrawable(R.drawable.ic_sign_in_tab);
                    drawableIn.setBounds(0, 0, drawableIn.getMinimumWidth(), drawableIn.getMinimumHeight());
                    tv_sign_tab_title.setCompoundDrawables(drawableIn, null, null, null);
                    break;
                case 1:
                    tv_sign_tab_title.setText(getContext().getString(R.string.sign_type_out));
                    Drawable drawableOut = getResources().getDrawable(R.drawable.ic_sign_out_tab);
                    drawableOut.setBounds(0, 0, drawableOut.getMinimumWidth(), drawableOut.getMinimumHeight());
                    tv_sign_tab_title.setCompoundDrawables(drawableOut, null, null, null);
                    break;
            }
            return view;
        }
    }
}
