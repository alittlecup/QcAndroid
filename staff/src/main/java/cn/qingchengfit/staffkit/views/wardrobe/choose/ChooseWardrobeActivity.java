package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class ChooseWardrobeActivity extends BaseActivity
    implements ChooseWardrobePresenter.MVPView, FragCallBack {
  public static final String LOCKER = "locker";

	EditText searchEt;
  @Inject ChooseWardrobePresenter presenter;
	ImageView clear;
	TextView chosenInfo;
	LinearLayout chosenLayout;
	LinearLayout layoutWardrobeToolbar;
  private Locker mChosenLocker;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chooose_wardrobe);
    searchEt = (EditText) findViewById(R.id.search_et);
    clear = (ImageView) findViewById(R.id.clear);
    chosenInfo = (TextView) findViewById(R.id.chosen_info);
    chosenLayout = (LinearLayout) findViewById(R.id.chosen_layout);
    layoutWardrobeToolbar = (LinearLayout) findViewById(R.id.layout_wardrobe_toolbar);
    findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChooseWardrobeActivity.this.onClick(v);
      }
    });
    findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChooseWardrobeActivity.this.onClick(v);
      }
    });
    findViewById(R.id.del_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ChooseWardrobeActivity.this.onClick(v);
      }
    });

    presenter.attachView(this);
    presenter.onNewSps();
    //获取已选择的locker
    mChosenLocker = getIntent().getParcelableExtra("locker");

    if (mChosenLocker != null) {
      chosenLayout.setVisibility(View.VISIBLE);
      chosenInfo.setText(
          getString(R.string.user_phone, mChosenLocker.name, mChosenLocker.region.name));
    } else {
      chosenLayout.setVisibility(View.GONE);
    }

    if (!CompatUtils.less21()) {
      layoutWardrobeToolbar.setPadding(0, MeasureUtils.getStatusBarHeight(getBaseContext()) + 10, 0,
          MeasureUtils.dpToPx(8f, getBaseContext().getResources()));
    }

    RxTextView.textChanges(searchEt)
        .debounce(450, TimeUnit.MILLISECONDS)
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

 public void onClick(View view) {
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

  @Override public void showSelectSheet(String title, List<String> strs,
      AdapterView.OnItemClickListener listener) {

  }

  @Override public void popBack() {

  }

  @Override public void popBack(int count) {

  }

  @Override public void onList(List<Locker> lockers, List<LockerRegion> regions) {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.frag, ChooseMainFragment.newInstance(lockers, regions))
        .commit();
  }

  @Override public void onSearch(List<Locker> lockers, List<LockerRegion> regions) {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.frag, SearchResultFragment.newInstance(lockers))
        .commit();
  }

  @Override public int getFragId() {
    return 0;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {

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
