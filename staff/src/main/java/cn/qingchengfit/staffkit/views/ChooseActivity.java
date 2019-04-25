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

import cn.qingchengfit.saasbase.chat.ConversationFriendsFragment;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.recruit.ChooseStaffFragment;
import cn.qingchengfit.saas.views.fragments.ChooseGymFragment;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.config.SigninConfigCardtypeListFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.choose.MultiChooseStudentWithFilterFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.filter.ReferrerFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.followup.TopFilterSourceFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChooseAddressFragment;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.LinkedList;
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
  public static final int CONVERSATION_FRIEND = 61; // 选择好友
  public static final int CHOOSE_STAFF = 62; // 选择工作人员
  public static final int CHOOSE_COMMON_GYM = 80; // 选择工作人员

  /**
   * 选择来源
   */
  public static final int CHOOSE_SOURCE = 3003;
  public static final int CHOOSE_REFERENCE = 3004;
  public static final int CHOOSE_ADDRESS = 71; // 选择地址
  public String chosenId;
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView down;
  LinearLayout titileLayout;
  EditText searchviewEt;
  ImageView searchviewClear;
  Button searchviewCancle;
  LinearLayout searchview;
  RelativeLayout toolbarLayout;
  FrameLayout frag;
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
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
    down = (ImageView) findViewById(R.id.down);
    titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
    searchviewEt = (EditText) findViewById(R.id.searchview_et);
    searchviewClear = (ImageView) findViewById(R.id.searchview_clear);
    searchviewCancle = (Button) findViewById(R.id.searchview_cancle);
    searchview = (LinearLayout) findViewById(R.id.searchview);
    toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
    frag = (FrameLayout) findViewById(R.id.frag);
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
      case CHOOSE_SOURCE:
        fragment = new TopFilterSourceFragment();
        break;
      case CHOOSE_REFERENCE:
        fragment = new ReferrerFragmentBuilder(3).build();
        break;
      case CHOOSE_MULTI_STUDENTS:
        toolbarLayout.setVisibility(View.GONE);
        fragment = new MultiChooseStudentWithFilterFragmentBuilder().expandedChosen(
            getIntent().getBooleanExtra("open", false)).build();
        break;
      case SIGN_IN_CARDS:
        ArrayList<SignInCardCostBean.CardCost> cost =
            getIntent().getParcelableArrayListExtra("costs");
        fragment = new SigninConfigCardtypeListFragmentBuilder().costList(cost).build();
        break;
      case CONVERSATION_FRIEND:
        toolbarLayout.setVisibility(View.GONE);
        fragment = new ConversationFriendsFragment();
        break;
      case CHOOSE_COMMON_GYM:
        toolbarLayout.setVisibility(View.GONE);
        fragment = new ChooseGymFragment();
        break;
      case CHOOSE_STAFF:
        String json = getIntent().getStringExtra("chatgym");
        ChatGym gym = new Gson().fromJson(json, ChatGym.class);
        if (gym == null) {
          this.finish();
          return;
        }
        fragment = ChooseStaffFragment.newInstance(gym);
        break;
      case CHOOSE_ADDRESS:
        toolbarLayout.setVisibility(View.GONE);
        Gym mGym = getIntent().getParcelableExtra("gym");
        if (mGym != null) {
          String cityName = "";
          String cityId = "";
          if (mGym.gd_district != null && mGym.gd_district.city != null) {
            cityName = mGym.gd_district.city.name;
            cityId = mGym.gd_district.city.id;
          }
          fragment = ChooseAddressFragment.newInstance(mGym.gd_lng == null ? 0 : mGym.gd_lng,
              mGym.gd_lat == null ? 0 : mGym.gd_lat, mGym.getAddress(), cityName, cityId);
        } else {
          fragment = ChooseAddressFragment.newInstance();
        }
        break;
      default:
        break;
    }
    getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
        .replace(R.id.frag, fragment)
        .commit();
  }

  @Override public int getFragId() {
    return R.id.frag;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
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
    initToolbar(toolbar);
  }
}
