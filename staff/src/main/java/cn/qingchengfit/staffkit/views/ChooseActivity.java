package cn.qingchengfit.staffkit.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.chat.ConversationFriendsFragment;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.batch.BatchPayCardFragment;
import cn.qingchengfit.staffkit.views.batch.BatchPayCardFragmentBuilder;
import cn.qingchengfit.staffkit.views.batch.BatchPayOnlineFragmentBuilder;
import cn.qingchengfit.staffkit.views.course.GymCourseListFragment;
import cn.qingchengfit.staffkit.views.gym.ChooseAddressFragment;
import cn.qingchengfit.staffkit.views.gym.coach.ChooseTrainerFragmentBuilder;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigCardtypeListFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.choose.MultiChooseStudentWithFilterFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.filter.ReferrerFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterSourceFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;

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

public class ChooseActivity extends BaseActivity implements FragCallBack {
    public static final int CHOOSE_COURSE_GROUP = 3000;
    public static final int CHOOSE_TRAINER = 3001;
    public static final int CHOOSE_SALES = 3002;
    public static final int CHOOSE_MULTI_STUDENTS = 11;
    public static final int BATCH_PAY_ONLINE = 31;
    public static final int BATCH_PAY_CARD = 32;
    public static final int SIGN_IN_CARDS = 51; // 用卡签到
    public static final int CONVERSATION_FRIEND = 61; // 用卡签到

    /**
     * 选择来源
     */
    public static final int CHOOSE_SOURCE = 3003;
    public static final int CHOOSE_REFERENCE = 3004;
    public static final int CHOOSE_ADDRESS = 71; // 选择地址
    public String chosenId;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.frag) FrameLayout frag;
    LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
    private Subscription spSearch;

    public static Intent newIntent(Context context, int to, String id) {
        Intent t = new Intent(context, ChooseActivity.class);
        t.putExtra("to", to);
        t.putExtra("id", id);
        return t;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        Fragment fragment = new Fragment();
        int to = getIntent().getIntExtra("to", 0);
        chosenId = getIntent().getStringExtra("id");
        if (getIntent().getData() != null && getIntent().getData().getPath() != null) {
            String path = getIntent().getData().getPath();
            if (path.contains("chat_friend")) {
                to = CONVERSATION_FRIEND;
            }
        }
        switch (to) {
            case CHOOSE_TRAINER:
                fragment = new ChooseTrainerFragmentBuilder().id(getIntent().getStringExtra("id")).build();
                break;
            case CHOOSE_SALES:

                break;
            case CHOOSE_SOURCE:
                fragment = new TopFilterSourceFragment();
                break;
            case CHOOSE_REFERENCE:
                fragment = new ReferrerFragmentBuilder(3).build();
                break;
            case BATCH_PAY_ONLINE:
                /**
                 * {@link BatchPayCardFragment}
                 */
                toolbarLayout.setVisibility(View.GONE);
                fragment =
                    new BatchPayOnlineFragmentBuilder(getIntent().getIntExtra("max", 1)).rule((Rule) getIntent().getParcelableExtra("rule"))
                        .build();
                break;
            case BATCH_PAY_CARD:
                toolbarLayout.setVisibility(View.GONE);
                List<Rule> rules = getIntent().getParcelableArrayListExtra("rules");
                List<CardTplBatchShip> ships = getIntent().getParcelableArrayListExtra("order");
                fragment =
                    new BatchPayCardFragmentBuilder(getIntent().getBooleanExtra("private", false), getIntent().getIntExtra("max", 1)).rules(
                        (ArrayList<Rule>) rules).cardTplBatchShips((ArrayList<CardTplBatchShip>) ships).build();
                break;
            case CHOOSE_MULTI_STUDENTS:
                toolbarLayout.setVisibility(View.GONE);
                fragment =
                    new MultiChooseStudentWithFilterFragmentBuilder().expandedChosen(getIntent().getBooleanExtra("open", false)).build();
                break;
            case SIGN_IN_CARDS:
                ArrayList<SignInCardCostBean.CardCost> cost = getIntent().getParcelableArrayListExtra("costs");
                fragment = new SigninConfigCardtypeListFragmentBuilder().costList(cost).build();
                break;
            case CONVERSATION_FRIEND:
                toolbarLayout.setVisibility(View.GONE);
                fragment = new ConversationFriendsFragment();
                break;
            case CHOOSE_ADDRESS:
                toolbarLayout.setVisibility(View.GONE);
                fragment = new ChooseAddressFragment();
                break;
            default:
                fragment = GymCourseListFragment.newInstance(getIntent().getIntExtra("type", Configs.TYPE_GROUP));
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragment).commit();
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

    @OnClick({ R.id.searchview_clear, R.id.searchview_cancle }) public void onSearch(View v) {
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
}
