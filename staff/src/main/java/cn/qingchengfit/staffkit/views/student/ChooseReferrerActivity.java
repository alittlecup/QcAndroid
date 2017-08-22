package cn.qingchengfit.staffkit.views.student;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.commpont.StaffWrapperComponent;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.staffkit.views.student.filter.ReferrerFragment;
import cn.qingchengfit.staffkit.views.student.filter.ReferrerFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFilterEvent;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
public class ChooseReferrerActivity extends BaseActivity implements FragCallBack, ChooseReferrerPresenter.PresenterView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;

    //@BindView(R.id.tv_add_new_origin) TextView tvAddNewOrigin;
    @BindView(R.id.frag) FrameLayout frag;
    @BindView(R.id.et_search) EditText etSearch;
    Observable observable;
    ReferrerFragment referrerFragment;
    @Inject ChooseReferrerPresenter presenter;
    private StaffWrapperComponent component;
    private StudentReferrerBean referrerBean;
    private StaffWrapper staffWrapper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_choose_referrer);
        ButterKnife.bind(this);
        initView();
        initToolBar();
        initBus();
    }

    private void initView() {
        referrerFragment = new ReferrerFragmentBuilder(3).build();
        staffWrapper = new StaffWrapper();
        getSupportFragmentManager().beginTransaction().replace(getFragId(), referrerFragment).commit();
        etSearch.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_search_24dp_grey), null, null, null);
        etSearch.requestFocus();
        AppUtils.showKeyboard(this, etSearch);
        //        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        //            @Override public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        //                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        //
        //                    // 先隐藏键盘
        //                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
        //                        ChooseReferrerActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //
        //                    if (!TextUtils.isEmpty(etSearch.getText().toString())) {
        //                        //跳转activity
        //                        referrerFragment.searchReferrer(etSearch.getText().toString());
        //                    }
        //
        //                    return true;
        //                }
        //                return false;
        //            }
        //        });

        RxTextView.afterTextChangeEvents(etSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                    String keyword = textViewAfterTextChangeEvent.editable().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        //跳转activity
                        referrerFragment.searchReferrer(keyword);
                    }
                }
            });
    }

    public void initToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbarTitile.setText("查找推荐人");
    }

    private void initBus() {
        observable = RxBus.getBus().register(FollowUpFilterEvent.class);
      observable.observeOn(Schedulers.io())
          .onBackpressureBuffer()
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<FollowUpFilterEvent>() {
            @Override public void call(FollowUpFilterEvent followUpFilterEvent) {
                switch (followUpFilterEvent.eventType) {
                    case FollowUpFilterEvent.EVENT_REFERRER_ITEM_CLICK:
                        // 获取选中的推荐人
                        //if (!referrerFragment.getSelectedList().isEmpty()) {
                        //    referrerBean = referrerFragment.getSelectedList().get(0);
                        //}
                        setResult(RESULT_OK, IntentUtils.instancePacecle(referrerBean));
                        finish();
                        break;
                }
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(FollowUpFilterEvent.class.getName(), observable);
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean toolbarBean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(toolbarBean);
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {
    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {
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
            toolbar.setOnMenuItemClickListener(bar.listener == null ? null : bar.listener);
        }
    }

    //搜索栏清除按钮
    @OnClick(R.id.searchview_clear) public void onClear() {
    }

    //取消搜索
    @OnClick(R.id.searchview_cancle) public void onClickCancel() {
    }

    //@OnClick(R.id.tv_add_new_origin) public void onClick() {
    //    // TODO
    //}

    @Override public void onReferrers(List<StudentReferrerBean> salers) {

    }

    @Override public void onShowError(String e) {

    }
}
