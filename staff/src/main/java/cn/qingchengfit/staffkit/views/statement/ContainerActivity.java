package cn.qingchengfit.staffkit.views.statement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.notisetting.view.NotiSettingHomeFragment;
import cn.qingchengfit.saasbase.article.ArticleCommentsListFragment;
import cn.qingchengfit.saasbase.article.ArticleReplyFragment;
import cn.qingchengfit.saasbase.chat.RecruitMessageListFragment;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventToolbar;
import cn.qingchengfit.staffkit.views.gym.site.SiteListFragment;
import cn.qingchengfit.staffkit.views.gym.staff.StaffDetailFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInCardFragment;
import cn.qingchengfit.staffkit.views.signin.config.SignInCodeFragment;
import cn.qingchengfit.staffkit.views.signin.zq.AddZqFragment;
import cn.qingchengfit.staffkit.views.signin.zq.EditZqFragment;
import cn.qingchengfit.staffkit.views.signin.zq.ZqAccessFragment;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;
import cn.qingchengfit.staffkit.views.statement.glance.SaleGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.SigninGlanceFragment;
import cn.qingchengfit.staffkit.views.statement.glance.StatementGlanceFragment;
import cn.qingchengfit.staffkit.views.student.score.ScoreHomeFragment;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.LinkedList;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/7 2016.
 */
public class ContainerActivity extends BaseActivity implements FragCallBack {
  Toolbar toolbar;
  TextView toolbarTitile;
  LinearLayout titleLayout;
  FrameLayout frag;
  ImageView down;
  LinkedList<ToolbarBean> toolbarList = new LinkedList<>();
  @Inject SerPermisAction serPermisAction;
  private Observable<EventToolbar> toolbarOb;

  public static void router(String module, Context context) {
    Intent toStatement = new Intent(context, ContainerActivity.class);
    toStatement.putExtra("router", module);
    context.startActivity(toStatement);
  }

  public static void router(String module, Context context, Object... params) {
    Intent toStatement = new Intent(context, ContainerActivity.class);
    toStatement.putExtra("router", module);
    toStatement.putExtra("params", params);
    context.startActivity(toStatement);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_frag);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
    titleLayout = (LinearLayout) findViewById(R.id.titile_layout);
    frag = (FrameLayout) findViewById(R.id.frag);
    down = (ImageView) findViewById(R.id.down);

    initBus();
    toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    String router = getIntent().getStringExtra("router");
    if (getIntent() != null
        && getIntent().getData() != null
        && getIntent().getData().getPath() != null) {
      router = getIntent().getData().getPath();
    }
    Fragment fragment = new Fragment();
    switch (router.toLowerCase()) {
      case QRActivity.MODULE_SERVICE_GROUP:
        //fragment = new CourseTypeBatchFragmentBuilder(false).build();
        break;
      case QRActivity.MODULE_SERVICE_PRIVATE:
        //fragment = new CourseTypeBatchFragmentBuilder(true).build();
        break;

      case QRActivity.MODULE_FINANCE_SALE:
        fragment = new SaleGlanceFragment();
        break;
      case QRActivity.MODULE_FINANCE_COURSE:
        fragment = new StatementGlanceFragment();
        break;
      case QRActivity.MODULE_FINANCE_SIGN_IN:
        fragment = new SigninGlanceFragment();
        break;
      case QRActivity.MODULE_MANAGE_COACH:
        //TODO: 2017/12/ 教练模块
        //fragment = new CoachListFragment();
        break;
      case QRActivity.MODULE_MANAGE_STAFF:
        //fragment = StaffListFragment.newInstance(App.staffId);
        //fragment = new StaffListFragment();
        break;
      case QRActivity.MODULE_MANAGE_STAFF_ADD:
        RouteUtil.routeTo(this, "staff", "/add/", null);
        finish();
        return;

      case QRActivity.MODULE_WARDROBE:
        fragment = new WardrobeMainFragment();
        break;
      case QRActivity.MODULE_GYM_SITE:
        fragment = new SiteListFragment();
        break;
      case QRActivity.MODULE_OPERATE_SCORE:
        fragment = new ScoreHomeFragment();
        break;
      case QRActivity.MODULE_WORKSPACE_ORDER_SIGNIN:
        break;
      case QRActivity.RECRUIT_MESSAGE_LIST:
        if (getIntent().getExtras().containsKey("params")) {
          fragment = new RecruitMessageListFragment();
        }
        break;
      case QRActivity.MODULE_MSG:
        fragment = new NotiSettingHomeFragment();
        break;

      case "/replies/":
      case "/replies":
      case QRActivity.MODULE_ARTICLE_COMMENT_REPLY:
        fragment = new ArticleReplyFragment();
        break;
      case "/comments/":
      case "/comments":
      case QRActivity.MODULE_ARTICLE_COMMENT_LIST:
        String articleid = IntentUtils.getIntentFromUri(getIntent(), "news_id");
        String replyId = IntentUtils.getIntentFromUri(getIntent(), "replyId");
        String replayname = IntentUtils.getIntentFromUri(getIntent(), "replyName");
        fragment = ArticleCommentsListFragment.newInstance(articleid, replyId, replayname);
        break;
      case QRActivity.ZQ_ACCESS:
        fragment = new ZqAccessFragment();
        break;
      case QRActivity.ADD_ZQ_ACCESS:
        fragment = new AddZqFragment();
        break;
      case QRActivity.EDIT_ZQ_ACCESS:
        fragment = EditZqFragment.newInstance(
            (AccessBody) getIntent().getBundleExtra("data").getParcelable("access"),
            getIntent().getExtras().getString("guard"));
        break;
      case QRActivity.SIGN_IN_CODE:
        fragment = new SignInCodeFragment();
        break;
      case QRActivity.SIGN_IN_CARD:
        fragment = new SignInCardFragment();
        break;
    }

    getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
        .replace(R.id.frag, fragment)
        .commit();
  }

  public void startFragment(Fragment fragment) {

  }

  private void initBus() {
    toolbarOb = RxBus.getBus().register(EventToolbar.class);
    toolbarOb.onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventToolbar>() {
          @Override public void call(EventToolbar eventToolbar) {
            toolbarTitile.setText(eventToolbar.getTitle());
            down.setVisibility(eventToolbar.isShowRight() ? View.VISIBLE : View.GONE);
            if (eventToolbar.getListener() != null) {
              toolbarTitile.setOnClickListener(eventToolbar.getListener());
            } else {
              toolbarTitile.setOnClickListener(null);
            }
            toolbar.getMenu().clear();
            if (eventToolbar.getMenu() != 0) {
              toolbar.inflateMenu(eventToolbar.getMenu());
              toolbar.setOnMenuItemClickListener(eventToolbar.getMenuClick());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        });
  }

  @Override protected void onDestroy() {
    RxBus.getBus().unregister(EventToolbar.class.getName(), toolbarOb);
    super.onDestroy();
  }

  @Override public int getFragId() {
    return R.id.frag;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
    ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
    toolbarList.add(bean);
    setBar(bean);
  }

  @Override public void cleanToolbar() {

  }

  @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

  }

  @Override public void onChangeFragment(BaseFragment fragment) {

  }

  public void setBar(ToolbarBean bar) {
    toolbarTitile.setText(bar.title);
    down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
    if (bar.onClickListener != null) {
      titleLayout.setOnClickListener(bar.onClickListener);
    } else {
      titleLayout.setOnClickListener(null);
    }
    toolbar.getMenu().clear();
    if (bar.menu != 0) {
      toolbar.inflateMenu(bar.menu);
      toolbar.setOnMenuItemClickListener(bar.listener);
    }
  }

  @Override public void onBackPressed() {
    if (toolbarList.size() > 1) {
      toolbarList.removeLast();
      setBar(toolbarList.getLast());
    }
    if (getIntent() != null && getIntent().getData() != null) {
      Intent ret = new Intent();
      String aciton = getIntent().getData().getHost() + getIntent().getData().getPath();
      ret.putExtra("web_action", aciton);
      setResult(Activity.RESULT_OK, ret);
    }
    super.onBackPressed();
  }
}
