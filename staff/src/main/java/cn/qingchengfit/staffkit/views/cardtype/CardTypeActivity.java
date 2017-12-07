package cn.qingchengfit.staffkit.views.cardtype;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.cardtype.list.BrandCardListFragment;
import cn.qingchengfit.staffkit.views.cardtype.list.CardListFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.LinkedList;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/4 2016.
 */
public class CardTypeActivity extends BaseActivity implements FragCallBack {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.student_frag) FrameLayout studentFrag;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Observable<OnBackEvent> sb;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_toolbar);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if (gymWrapper.inBrand()) {
            getSupportFragmentManager().beginTransaction().replace(getFragId(), new BrandCardListFragment()).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().replace(getFragId(), new CardListFragment()).commitAllowingStateLoss();
        }
        initBus();
    }

    //private void initDI() {
    //    component = DaggerCardTypeComponent.builder().appComponent(((App) getApplication()).getAppCompoent())
    //            .gymMoudle(new GymMoudle(
    //                    (CoachService) getIntent().getParcelableExtra(Configs.EXTRA_GYM_SERVICE),
    //                    (Brand) getIntent().getParcelableExtra(Configs.EXTRA_BRAND),
    //                    (GymStatus)getIntent().getParcelableExtra(Configs.EXTRA_GYM_STATUS)
    //
    //            ))
    //            .build();
    //    component.inject(this);
    //}

    @Override protected void onResumeFragments() {
        super.onResumeFragments();

    }

    private void initBus(){
        sb = RxBus.getBus().register(OnBackEvent.class);
        sb.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<OnBackEvent>() {
                @Override public void call(OnBackEvent onBackEvent) {
                    onStateNotSaved();
                    getSupportFragmentManager().popBackStack(null, 1);
                    if(!gymWrapper.inBrand()) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(getFragId(), new CardListFragment())
                            .commitAllowingStateLoss();
                    }else{
                        getSupportFragmentManager().beginTransaction()
                            .replace(getFragId(), new BrandCardListFragment())
                            .commitAllowingStateLoss();
                    }
                }
            });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(OnBackEvent.class.getName(), sb);
    }

    @Override public int getFragId() {
        return R.id.student_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        toolbarList.clear();
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    public void setBar(ToolbarBean bar) {
        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            titileLayout.setOnClickListener(bar.onClickListener);
        } else {
            titileLayout.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }

    @Override public void onChangeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(getFragId(), fragment).addToBackStack(fragment.getFragmentName());
        transaction.commit();
    }

    @Override public void onBackPressed() {
        if (toolbarList.size() > 1) {
            toolbarList.removeLast();
            setBar(toolbarList.getLast());
        }

        super.onBackPressed();
    }
}
