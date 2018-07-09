package cn.qingchengfit.student.view.sendmsg;

import android.icu.util.BuddhistCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.widget.FlexableListFragment;
import cn.qingchengfit.student.BuildConfig;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.ShortMsg;
import cn.qingchengfit.student.item.ShortMsgItem;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/14.
 *
 *
 * {@link }
 */
@Leaf(module = "student", path = "/student/sendmsg/") public class SendMsgHomeFragment
    extends SaasCommonFragment implements ShortMsgPresentersPresenter.MVPView {

  TabLayout tabview;
  ViewPager viewpager;
  EditText etSearch;

  FlexableListFragment allFragment;
  FlexableListFragment sendedFragment;
  FlexableListFragment scriptFragment;

  List<AbstractFlexibleItem> mDatasAll = new ArrayList<>();
  List<AbstractFlexibleItem> mDatasScript = new ArrayList<>();
  List<AbstractFlexibleItem> mDatasSended = new ArrayList<>();

  @Inject ShortMsgPresentersPresenter presenter;
  Toolbar toolbar;
  TextView toolbarTitile;
  SendMsgHomeAdapter adapter;
  ImageView searchviewClear;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StudentParamsInjector.inject(this);
  }


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_send_msg_home, container, false);
    tabview = (TabLayout) view.findViewById(R.id.tabview);
    viewpager = (ViewPager) view.findViewById(R.id.viewpager);
    etSearch = (EditText) view.findViewById(R.id.et_search);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    searchviewClear = (ImageView) view.findViewById(R.id.search_clear);
    view.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        addMsg();
      }
    });

    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    if (allFragment == null) {
      allFragment = new FlexableListFragment();
      allFragment.customNoStr = "点击右下方按钮新建短信";
      allFragment.customNoStrTitle = "暂无短信记录";
      allFragment.customNoImage = R.drawable.img_empty_msg;
    }
    if (sendedFragment == null) {
      sendedFragment = new FlexableListFragment();
      sendedFragment.customNoStr = "点击右下方按钮新建短信";
      sendedFragment.customNoStrTitle = "暂无短信记录";
      sendedFragment.customNoImage = R.drawable.img_empty_msg;
    }
    if (scriptFragment == null) {
      scriptFragment = new FlexableListFragment();
      scriptFragment.customNoStr = "点击右下方按钮新建短信";
      scriptFragment.customNoStrTitle = "暂无短信记录";
      scriptFragment.customNoImage = R.drawable.img_empty_msg;
    }

    etSearch.setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(getContext(), R.drawable.vd_search_grey_14dp), null, null, null);
    etSearch.setHint(getString(R.string.search_sms_hint));
    RxTextView.afterTextChangeEvents(etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
          @Override public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
            sendedFragment.setFilterString(etSearch.getText().toString().trim());
            allFragment.setFilterString(etSearch.getText().toString().trim());
            scriptFragment.setFilterString(etSearch.getText().toString().trim());
            presenter.queryShortMsgList(null, etSearch.getText().toString().trim());
            searchviewClear.setVisibility(
                etSearch.getText().toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
          }
        });
    RxView.clicks(searchviewClear).subscribe(aVoid -> etSearch.setText(""));
    allFragment.setItemClickListener(position -> {
      if (mDatasAll.isEmpty()) return false;
      if (mDatasAll.get(position) instanceof ShortMsgItem) {
        ShortMsgItem item = (ShortMsgItem) mDatasAll.get(position);
        routeToDetail(new ShortMsgDetailParams().shortMsg(item.getShortMsg()).build());

      }
      return false;
    });
    sendedFragment.setItemClickListener(position -> {
      if (mDatasSended.isEmpty()) return false;
      if (mDatasSended.get(position) instanceof ShortMsgItem) {
        ShortMsgItem item = (ShortMsgItem) mDatasSended.get(position);
        routeToDetail(new ShortMsgDetailParams().shortMsg(item.getShortMsg()).build());

      }
      return false;
    });
    scriptFragment.setItemClickListener(position -> {
      if (mDatasScript.isEmpty()) return false;
      if (mDatasScript.get(position) instanceof ShortMsgItem) {
        ShortMsgItem item = (ShortMsgItem) mDatasScript.get(position);
        routeToDetail(new ShortMsgDetailParams().shortMsg(item.getShortMsg()).build());
      }
      return false;
    });
    viewpager.setOffscreenPageLimit(2);
    if (adapter == null) adapter = new SendMsgHomeAdapter(getChildFragmentManager());
    viewpager.setAdapter(adapter);
    tabview.setupWithViewPager(viewpager);
    tabview.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(tabview.getViewTreeObserver(), this);
            presenter.queryShortMsgList(null, null);
          }
        });

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("群发短信");
  }

  @Override protected void onVisible() {
    super.onVisible();
  }

  @Override public String getFragmentName() {
    return SendMsgHomeFragment.class.getName();
  }

  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {

  }

  @Override public void onShortMsgList(List<ShortMsg> list) {
    if (list != null) {
      mDatasAll.clear();
      mDatasScript.clear();
      mDatasSended.clear();
      for (int i = 0; i < list.size(); i++) {
        ShortMsg s = list.get(i);
        if (s.status == 1) {
          mDatasSended.add(new ShortMsgItem(s));
        } else {
          mDatasScript.add(new ShortMsgItem(s));
        }
        mDatasAll.add(new ShortMsgItem(s));
      }
      allFragment.setData(mDatasAll);
      sendedFragment.setData(mDatasSended);
      scriptFragment.setData(mDatasScript);
    }
  }

  @Override public void onShortMsgDetail(ShortMsg detail) {

  }

  @Override public void onPostSuccess() {

  }

  @Override public void onPutSuccess() {

  }

  @Override public void onDelSuccess() {

  }

  public void addMsg() {
    routeTo("/student/msgsend",null);
  }

  private void routeToDetail(Bundle bundle) {
    routeTo("/student/msgdetail",bundle);
  }

  class SendMsgHomeAdapter extends FragmentStatePagerAdapter {

    public SendMsgHomeAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return allFragment;
        case 1:
          return sendedFragment;
        default:
          return scriptFragment;
      }
    }

    @Override public int getCount() {
      return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "全部";
        case 1:
          return "已发送";
        default:
          return "草稿箱";
      }
    }
  }
  @Override protected void routeTo(String uri, Bundle bd, boolean b) {
    if (BuildConfig.RUN_AS_APP) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (this.getActivity() instanceof BaseActivity) {
        this.routeTo(Uri.parse(BuildConfig.PROJECT_NAME
            + "://"
            + ((BaseActivity) this.getActivity()).getModuleName()
            + uri), bd, b);
      }
    } else {
      super.routeTo(uri, bd, b);
    }
  }
}
