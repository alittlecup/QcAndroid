package cn.qingchengfit.staffkit.views.card;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.card.detail.RealCardDetailFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.LinkedList;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 16/3/18 2016.
 */
public class CardDetailActivity extends BaseActivity implements FragCallBack {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.student_frag) FrameLayout studentFrag;
    @BindView(R.id.down) ImageView down;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    @Inject RealcardWrapper realcardWrapper;
    private TextView tvCancel;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_toolbar);
        ButterKnife.bind(this);
        tvCancel = new TextView(getApplicationContext());

        initToolbar();
        Card realCard = getIntent().getParcelableExtra(Configs.EXTRA_REAL_CARD);
        if (realCard != null) {
            realcardWrapper.setRealCard(realCard);
        }
        getSupportFragmentManager().beginTransaction().replace(getFragId(), new RealCardDetailFragment()).commit();
    }

    public void initTextToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(null);
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setText("取消");
        tvCancel.setTextSize(16);
        tvCancel.setTextColor(getApplicationContext().getResources().getColor(android.R.color.white));
        tvCancel.setGravity(Gravity.LEFT);
        if (toolbar.getChildCount() == 1) {
            toolbar.addView(tvCancel);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                view.setVisibility(View.GONE);
                initToolbar();
                onBackPressed();
            }
        });
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
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

    @Override public void onChangeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(getFragId(), fragment).addToBackStack(fragment.getFragmentName());
        transaction.commit();
    }

    @Override public void onBackPressed() {
        if (toolbarList.size() > 1) {
            toolbarList.removeLast();
            setBar(toolbarList.getLast());
        }
        super.onBackPressed();
        tvCancel.setVisibility(View.GONE);
        initToolbar();
    }

    @Override protected void onDestroy() {
        realcardWrapper.setRealCard(null);
        super.onDestroy();
    }
}
