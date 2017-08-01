package cn.qingchengfit.staffkit.views.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFilterEvent;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterSourceFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;
import se.emilsjolander.intentbuilder.IntentBuilder;

/**
 * //Edited by paper comment @ 2017/3/6
 * //Created by yangming on 16/12/5.
 */
@IntentBuilder public class ChooseOriginActivity extends BaseActivity implements FragCallBack, ChooseOriginPresenter.PresenterView {

    public static final int RESULT_ADD_ORIGIN = 11;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;

    @BindView(R.id.frag) FrameLayout frag;
    Observable observable;
    TopFilterSourceFragment sourceFragment;
    @Inject ChooseOriginPresenter presenter;
    private StudentSourceBean sourceBean;
    private String addSourceName;
    private StaffWrapper staffWrapper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChooseOriginActivityIntentBuilder.inject(getIntent(), this);
        setContentView(R.layout.activity_student_add_choose_origin);
        ButterKnife.bind(this);
        sourceFragment = new TopFilterSourceFragment();
        initView();
        initToolBar();
        initBus();
    }

    private void initView() {
        staffWrapper = new StaffWrapper();
        presenter.attachView(this);
        sourceFragment.chooseType = 3;
        getSupportFragmentManager().beginTransaction().replace(getFragId(), sourceFragment).commit();
    }

    public void initToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbarTitile.setText("来源");
    }

    private void initBus() {
        observable = RxBus.getBus().register(FollowUpFilterEvent.class);
        observable.subscribe(new Action1<FollowUpFilterEvent>() {
            @Override public void call(FollowUpFilterEvent followUpFilterEvent) {
                switch (followUpFilterEvent.eventType) {
                    case FollowUpFilterEvent.EVENT_SOURCE_ITEM_CLICK:
                        // 获取选中的来源
                        if (sourceFragment.getSelectedList() != null) {
                            sourceBean = sourceFragment.getSelectedList();
                        }
                        setResult(RESULT_OK, IntentUtils.instancePacecle(sourceBean));
                        finish();
                        break;
                }
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(FollowUpFilterEvent.class.getName(), observable);
        presenter.unattachView();
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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_ADD_ORIGIN) {
                // 添加
                if (!TextUtils.isEmpty(IntentUtils.getIntentString(data))) {
                    addSourceName = IntentUtils.getIntentString(data);
                    presenter.addOrigin(IntentUtils.getIntentString(data));
                }
            }
        }
    }

    @Override public void onSuccess() {
        //sourceFragment.freshData();
        StudentSourceBean sourceBean = new StudentSourceBean();
        sourceBean.name = addSourceName;
        setResult(RESULT_OK, IntentUtils.instancePacecle(sourceBean));
        finish();
    }

    @Override public void onShowError(String e) {

    }
}
