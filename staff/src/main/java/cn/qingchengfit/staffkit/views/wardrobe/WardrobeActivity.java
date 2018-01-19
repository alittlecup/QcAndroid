package cn.qingchengfit.staffkit.views.wardrobe;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventBackPress;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.rxbus.event.EventOpenSearch;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class WardrobeActivity extends BaseActivity implements FragCallBack {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription spSearch;
    private boolean hasSecondView = false;
    private Subscription sp;
    private Observable<EventOpenSearch> mOb;
    private String keyword;
    private Observable<EventFresh> mFreshOb;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });
        mOb = RxBus.getBus().register(EventOpenSearch.class);
        mOb.subscribe(new Action1<EventOpenSearch>() {
            @Override public void call(EventOpenSearch eventOpenSearch) {
                openSeachView(eventOpenSearch.hint, new Action1<CharSequence>() {
                    @Override public void call(CharSequence charSequence) {
                        keyword = charSequence.toString();
                        if (TextUtils.isEmpty(charSequence)) {
                            //  获取所有
                            //                            onList();
                        } else {
                            //  搜索

                            //searchLocker(App.staffId, charSequence.toString());
                        }
                    }
                });
            }
        });
        mFreshOb = RxBus.getBus().register(EventFresh.class);
        mFreshOb.subscribe(new Action1<EventFresh>() {
            @Override public void call(EventFresh eventFresh) {
                if (!StringUtils.isEmpty(keyword)) {
                    searchview.setVisibility(View.VISIBLE);
                    //searchLocker(App.staffId, keyword);
                }
            }
        });

        onList();
    }

    public void onList() {
        getSupportFragmentManager().beginTransaction().replace(getFragId(), new WardrobeMainFragment()).commit();
    }

    //public void onSearch(List<Locker> regions) {
    //    getSupportFragmentManager().beginTransaction()
    //        .replace(R.id.frag, SearchResultFragment.newInstance(regions))
    //        .commitAllowingStateLoss();
    //}

    //public void searchLocker(String staffid, String key) {
    //    HashMap<String, Object> params = gymWrapper.getParams();
    //    params.put("q", key);
    //
    //    sp = restRepository.getGet_api()
    //        .qcGetAllLockers(staffid, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .subscribe(new Action1<QcDataResponse<AllLockers>>() {
    //            @Override public void call(QcDataResponse<AllLockers> qcResponse) {
    //                if (ResponseConstant.checkSuccess(qcResponse)) {
    //                    onSearch(qcResponse.data.lockers);
    //                } else {
    //                    ToastUtils.show(qcResponse.getMsg());
    //                }
    //            }
    //        }, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //                ToastUtils.show(throwable.getMessage());
    //            }
    //        });
    //}

    @Override protected void onDestroy() {
        RxBus.getBus().unregister(EventOpenSearch.class.getName(), mOb);
        RxBus.getBus().unregister(EventFresh.class.getName(), mFreshOb);
        super.onDestroy();
        if (sp != null) sp.unsubscribe();

        if (spSearch != null) spSearch.unsubscribe();
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        searchview.setVisibility(View.GONE);
        toolbarTitile.setText(title);
        down.setVisibility(showRight ? View.VISIBLE : View.GONE);
        if (title != null) {
            titileLayout.setOnClickListener(titleClick);
        } else {
            titileLayout.setOnClickListener(null);
        }
        mToolbar.getMenu().clear();
        if (menu != 0) {
            mToolbar.inflateMenu(menu);
            mToolbar.setOnMenuItemClickListener(listener);
        }
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
        searchviewEt.setHint(hint);
        searchview.setVisibility(View.VISIBLE);
        spSearch = RxTextView.textChanges(searchviewEt)
            .debounce(150, TimeUnit.MILLISECONDS)
            .switchMap(new Func1<CharSequence, Observable<CharSequence>>() {
                @Override public Observable<CharSequence> call(CharSequence charSequence) {
                    return Observable.just(charSequence);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1);
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }

    @Override public void onBackPressed() {

        if (hasSecondView) {
            RxBus.getBus().post(new EventBackPress());
        } else {
            super.onBackPressed();
        }
    }

    public boolean isHasSecondView() {
        return hasSecondView;
    }

    public void setHasSecondView(boolean hasSecondView) {
        this.hasSecondView = hasSecondView;
    }

    @OnClick(R.id.searchview_cancle) public void onClick() {
        if (searchviewEt.getText().toString().length() > 0) {
            searchviewEt.setText("");
        }
        AppUtils.hideKeyboard(this);
        searchview.setVisibility(View.GONE);
        onList();
    }
}
