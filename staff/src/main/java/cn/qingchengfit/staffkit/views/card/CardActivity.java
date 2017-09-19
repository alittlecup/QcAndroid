package cn.qingchengfit.staffkit.views.card;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.card.cardlist.RealCardListFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
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
 * Created by Paper on 16/3/17 2016.
 */
public class CardActivity extends BaseActivity implements FragCallBack {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview) LinearLayout searchview;
    private Subscription spSearch;

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
        getSupportFragmentManager().beginTransaction().replace(getFragId(), new RealCardListFragment()).commit();
    }

    @Override public int getFragId() {
        return R.id.student_frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        toolbarTitile.setText(title);
        down.setVisibility(showRight ? View.VISIBLE : View.GONE);
        if (title != null) {
            titileLayout.setOnClickListener(titleClick);
        } else {
            titileLayout.setOnClickListener(null);
        }
        toolbar.getMenu().clear();
        if (menu != 0) {
            toolbar.inflateMenu(menu);
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    @Override public void cleanToolbar() {

    }

    @OnClick({ R.id.searchview_clear, R.id.searchview_cancle }) public void onSearch(View v) {
        switch (v.getId()) {
            case R.id.searchview_clear:
                searchviewEt.setText("");
                break;
            case R.id.searchview_cancle:
                searchviewEt.setText("");
                searchview.setVisibility(View.GONE);
                Observable.just("").delay(3, TimeUnit.SECONDS).observeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        if (spSearch != null) {
                            spSearch.unsubscribe();
                        }
                    }
                });

                break;
        }
    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
        searchviewEt.setHint(hint);
        searchview.setVisibility(View.VISIBLE);
        spSearch =
            RxTextView.textChanges(searchviewEt).debounce(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }
}
