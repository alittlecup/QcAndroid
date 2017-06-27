package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class ChooseWardrobeActivity extends BaseActivity implements ChooseWardrobePresenter.MVPView, FragCallBack {
    public static final String LOCKER = "locker";

    @BindView(R.id.search_et) EditText searchEt;
    @Inject ChooseWardrobePresenter presenter;
    @BindView(R.id.clear) ImageView clear;
    @BindView(R.id.chosen_info) TextView chosenInfo;
    @BindView(R.id.chosen_layout) LinearLayout chosenLayout;
    private Locker mChosenLocker;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooose_wardrobe);
        ButterKnife.bind(this);
        presenter.attachView(this);

        //获取已选择的locker
        mChosenLocker = getIntent().getParcelableExtra("locker");

        if (mChosenLocker != null) {
            chosenLayout.setVisibility(View.VISIBLE);
            chosenInfo.setText(getString(R.string.user_phone, mChosenLocker.name, mChosenLocker.region.name));
        } else {
            chosenLayout.setVisibility(View.GONE);
        }

        RxTextView.textChanges(searchEt)
            .debounce(150, TimeUnit.MILLISECONDS)
            .switchMap(new Func1<CharSequence, Observable<CharSequence>>() {
                @Override public Observable<CharSequence> call(CharSequence charSequence) {
                    return Observable.just(charSequence);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<CharSequence>() {
                @Override public void call(CharSequence charSequence) {
                    if (TextUtils.isEmpty(charSequence)) {
                        presenter.queryAllLocker(App.staffId);
                        clear.setVisibility(View.GONE);
                    } else {
                        presenter.searchLocker(App.staffId, charSequence.toString());
                        clear.setVisibility(View.VISIBLE);
                    }
                }
            });
        AppUtils.showKeyboard(this, searchEt);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.unattachView();
        AppUtils.hideKeyboard(this);
    }

    @OnClick({ R.id.back, R.id.clear, R.id.del_btn }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AppUtils.hideKeyboard(this);
                if (mChosenLocker == null) {
                    setResult(Activity.RESULT_OK);
                } else {
                    setResult(Activity.RESULT_CANCELED);
                }
                this.finish();
                break;
            case R.id.clear:
                searchEt.setText("");
                break;
            case R.id.del_btn:
                mChosenLocker = null;
                chosenInfo.setText("");
                chosenLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onList(List<Locker> lockers, List<LockerRegion> regions) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, ChooseMainFragment.newInstance(lockers, regions)).commit();
    }

    @Override public void onSearch(List<Locker> lockers, List<LockerRegion> regions) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, SearchResultFragment.newInstance(lockers)).commit();
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
