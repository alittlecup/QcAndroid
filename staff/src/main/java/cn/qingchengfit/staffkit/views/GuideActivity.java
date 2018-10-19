package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.SaveEvent;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.staffkit.views.gym.SetGymFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.LinkedList;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static cn.qingchengfit.staffkit.views.gym.GymActivity.GYM_TO;

/**
 * Created by Paper on 16/6/12.
 * <p>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public class GuideActivity extends BaseActivity implements FragCallBack {
    public boolean isAdd = false;
	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView down;
	LinearLayout titileLayout;
	ImageView searchviewClear;
	Button searchviewCancle;
	RelativeLayout toolbarLayout;
	EditText searchviewEt;
	LinearLayout searchview;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository mRestRepository;
    private Observable<SaveEvent> saveObservabl;
    private Subscription spSearch;
    private Subscription spQueryBrand;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      down = (ImageView) findViewById(R.id.down);
      titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
      searchviewClear = (ImageView) findViewById(R.id.searchview_clear);
      searchviewCancle = (Button) findViewById(R.id.searchview_cancle);
      toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
      searchviewEt = (EditText) findViewById(R.id.searchview_et);
      searchview = (LinearLayout) findViewById(R.id.searchview);

        Window window = getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.primary));
      findViewById(R.id.searchview_clear).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onSearch(v);
        }
      });
      findViewById(R.id.searchview_cancle).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onSearch(v);
        }
      });

      //        ((App) getApplication()).getAppCompoent().inject(this);
        mRestRepository = new RestRepository();
        if (getIntent().getParcelableExtra(Configs.EXTRA_BRAND) == null) {

          spQueryBrand = mRestRepository.getGet_api()
                .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                    @Override public void call(final QcDataResponse<BrandsResponse> qcResponseBrands) {
                        if (ResponseConstant.checkSuccess(qcResponseBrands)) {
                            if (qcResponseBrands.data.brands.size() > 0) {

                                Intent choose = new Intent(GuideActivity.this, GuideChooseBrandAcitivity.class);
                                startActivityForResult(choose, 3);
                            } else {
                                Intent toBrands2 = new Intent(GuideActivity.this, AddBrandActivity.class);
                                toBrands2.putExtra(GYM_TO, GymActivity.GYM_GUIDE);
                                startActivityForResult(toBrands2, 2);
                            }
                        } else {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                });
        } else {

          getSupportFragmentManager().beginTransaction().replace(R.id.frag, new SetGymFragment()).commit();
        }
        isAdd = getIntent().getBooleanExtra("isAdd", false);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup ) {
            ((ViewGroup) toolbar.getParent()).setPadding(0,
              MeasureUtils.getStatusBarHeight(this), 0, 0);
        }
        toolbarTitile.setText("完善资料");

        saveObservabl = RxBus.getBus().register(SaveEvent.class);
        saveObservabl.subscribe(new Action1<SaveEvent>() {
            @Override public void call(SaveEvent saveEvent) {
                SystemInitBody body = (SystemInitBody) App.caches.get("init");
                if (body != null) {
                    String bodyStr = new Gson().toJson(body);
                    Timber.e(bodyStr);
                    PreferenceUtils.setPrefString(App.context, "init", bodyStr);
                }
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                this.gymWrapper.getBrand().setId(brand.getId());
                this.gymWrapper.getBrand().setName(brand.getName());
                this.gymWrapper.getBrand().setPhoto(brand.getPhoto());
                SystemInitBody body;
                if (TextUtils.isEmpty(PreferenceUtils.getPrefString(this, "init", ""))) {
                    body = new SystemInitBody();
                } else {
                    body = new Gson().fromJson(PreferenceUtils.getPrefString(this, "init", ""), SystemInitBody.class);
                }
                App.caches.put("init", body);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new SetGymFragment()).commit();
            } else if (requestCode == 2) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);

                this.gymWrapper.getBrand().setId(brand.getId());
                this.gymWrapper.getBrand().setName(brand.getName());
                this.gymWrapper.getBrand().setPhoto(brand.getPhoto());
                SystemInitBody body;
                if (TextUtils.isEmpty(PreferenceUtils.getPrefString(this, "init", ""))) {
                    body = new SystemInitBody();
                } else {
                    body = new Gson().fromJson(PreferenceUtils.getPrefString(this, "init", ""), SystemInitBody.class);
                }
                App.caches.put("init", body);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new SetGymFragment()).commit();
            } else if (requestCode == 3) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                this.gymWrapper.getBrand().setId(brand.getId());
                this.gymWrapper.getBrand().setName(brand.getName());
                this.gymWrapper.getBrand().setPhoto(brand.getPhoto());
                SystemInitBody body;
                if (TextUtils.isEmpty(PreferenceUtils.getPrefString(this, "init", ""))) {
                    body = new SystemInitBody();
                } else {
                    body = new Gson().fromJson(PreferenceUtils.getPrefString(this, "init", ""), SystemInitBody.class);
                }
                App.caches.put("init", body);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new SetGymFragment()).commit();
            }
        } else {
            if (requestCode == 2) {

                finish();
            } else if (requestCode == 3) {
                if (resultCode == 999) {
                    Intent toBrands2 = new Intent(GuideActivity.this, AddBrandActivity.class);
                    toBrands2.putExtra(GYM_TO, GymActivity.GYM_GUIDE);
                    startActivityForResult(toBrands2, 2);
                } else {
                    GuideActivity.this.finish();
                }
            }
        }
    }

    @Override protected void onDestroy() {
        RxBus.getBus().unregister(SaveEvent.class.getName(), saveObservabl);
        //        RxBus.getBus().post(new RxCloseAppEvent());
        super.onDestroy();
        if (spQueryBrand != null && spQueryBrand.isUnsubscribed()) {
            spQueryBrand.unsubscribe();
        }
    }

    @Override protected boolean isFitSystemBar() {
        return false;
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        hideSearch();
        toolbarLayout.setVisibility(View.VISIBLE);
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        toolbarList.add(bean);
        setBar(bean);
    }

    @Override public void cleanToolbar() {
        toolbarList.clear();
    }

 public void onSearch(View v) {
        switch (v.getId()) {
            case R.id.searchview_clear:
                searchviewEt.setText("");
                break;
            case R.id.searchview_cancle:
                hideSearch();
                break;
        }
    }

    public void hideSearch() {
        searchviewEt.setText("");
        searchview.setVisibility(View.GONE);
        if (spSearch != null) spSearch.unsubscribe();
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
        searchviewEt.setHint(hint);
        searchview.setVisibility(View.VISIBLE);
        spSearch = RxTextView.textChanges(searchviewEt).subscribe(action1);
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    public void setBar(ToolbarBean bar) {
        //        toolbarTitile.setText(bar.title);
        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
        if (bar.onClickListener != null) {
            toolbarTitile.setOnClickListener(bar.onClickListener);
        } else {
            toolbarTitile.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (bar.menu != 0) {
            toolbar.inflateMenu(bar.menu);
            toolbar.setOnMenuItemClickListener(bar.listener);
        }
    }
}
