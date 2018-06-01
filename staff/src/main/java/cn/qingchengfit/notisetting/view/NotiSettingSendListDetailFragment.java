package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.notisetting.bean.NotiSettingMsgDetail;
import cn.qingchengfit.notisetting.item.NotiSettingMsgDetailItem;
import cn.qingchengfit.notisetting.presenter.NotiSettingSendListDetailPresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/8/1.
 */
public class NotiSettingSendListDetailFragment extends BaseFragment
    implements NotiSettingSendListDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener {

	Toolbar toolbar;
	TextView toolbarTitle;
	QcFilterToggle qftChannel;
	QcFilterToggle qftStatus;
	QcFilterToggle qftTimes;
	TextView tvTotalTitle;
	TextView tvMsg;
	TextView tvWechat;
	TextView tvApp;
	RelativeLayout layoutTotal;
	RecyclerView rv;
	FrameLayout fragNotiList;
  int curM = 0, curY = 0, curD = 0;
  @Inject NotiSettingSendListDetailPresenter presenter;
  private NotiSettingFilterFragment filterFragment;
  private CommonFlexAdapter adapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    filterFragment = new NotiSettingFilterFragment();
    adapter = new CommonFlexAdapter(new ArrayList(), this);

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_noti_setting_msg_list_detail, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    qftChannel = (QcFilterToggle) view.findViewById(R.id.qft_channel);
    qftStatus = (QcFilterToggle) view.findViewById(R.id.qft_status);
    qftTimes = (QcFilterToggle) view.findViewById(R.id.qft_times);
    tvTotalTitle = (TextView) view.findViewById(R.id.tv_total_title);
    tvMsg = (TextView) view.findViewById(R.id.tv_msg);
    tvWechat = (TextView) view.findViewById(R.id.tv_wechat);
    tvApp = (TextView) view.findViewById(R.id.tv_app);
    layoutTotal = (RelativeLayout) view.findViewById(R.id.layout_total);
    rv = (RecyclerView) view.findViewById(R.id.rv);
    fragNotiList = (FrameLayout) view.findViewById(R.id.frag_noti_list);
    view.findViewById(R.id.qft_channel).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onQftChannelClicked();
      }
    });
    view.findViewById(R.id.qft_status).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onQftStatusClicked();
      }
    });
    view.findViewById(R.id.qft_times).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onQftTimesClicked();
      }
    });
    view.findViewById(R.id.frag_noti_list).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onFragNotiListClicked();
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    SmoothScrollLinearLayoutManager linearLayoutManager =
        new SmoothScrollLinearLayoutManager(getContext());
    if (savedInstanceState != null) {
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("pos", 0));
    }
    rv.setLayoutManager(linearLayoutManager);
    adapter.setStickyHeaders(true).setDisplayHeadersAtStartUp(true).setStickyHeaderElevation(1);
    rv.setAdapter(adapter);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("通知发送明细");
  }

  @Override protected void onFinishAnimation() {

    presenter.setParams(null);
    presenter.getList();
  }

  @Override public String getFragmentName() {
    return NotiSettingSendListDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onMsgs(List<NotiSettingMsgDetail> list, int totalcount, int curPage) {
    if (list != null) {
      if (curPage == 1) {
        adapter.clear();
        curM = curY = 0;
        if (totalcount > list.size()) {
          adapter.setEndlessScrollListener(this, new ProgressItem(getContext()));
        }
      }
      List<AbstractFlexibleItem> curItems = new ArrayList<>();
      for (int i = 0; i < list.size(); i++) {
        NotiSettingMsgDetail msg = list.get(i);
        int newY = DateUtils.getYear(DateUtils.formatDateFromServer(msg.created_at));
        int newM = DateUtils.getMonth(DateUtils.formatDateFromServer(msg.created_at));
        int newD = DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(msg.created_at));
        if (newM != curM || newY != curY || newD != curD) {
          curY = newY;
          curM = newM;
          curD = newD;
          curItems.add(new StickerDateItem(DateUtils.getYYYYMMDDfromServer(msg.created_at)));
        }
        curItems.add(generateItem(msg));
      }

      adapter.onLoadMoreComplete(curItems, 500);
    } else {
      adapter.onLoadMoreComplete(null);
    }
  }

  @Override public void noMoreLoad(int i) {

  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.getList();
  }

  @Override public boolean onItemClick(int i) {
    return false;
  }

  private NotiSettingMsgDetailItem generateItem(NotiSettingMsgDetail msg) {
    return new NotiSettingMsgDetailItem(msg);
  }

  @Override public void onSendInfo(String a, String b, String c) {
    if (!CmStringUtils.isEmpty(a))
      FlexibleUtils.highlightWords(tvMsg, "短信" + a, a);
    if (!CmStringUtils.isEmpty(b))
      FlexibleUtils.highlightWords(tvWechat, "微信" + b, b);
    if (!CmStringUtils.isEmpty(c))
      FlexibleUtils.highlightWords(tvApp, "APP推送" + c, c);
  }

 public void onQftChannelClicked() {
    toggleFilter(0);
  }

 public void onQftStatusClicked() {
    toggleFilter(1);
  }

 public void onQftTimesClicked() {
    toggleFilter(2);
  }

 public void onFragNotiListClicked() {
    hideFilter();
  }

  public void toggleFilter(int pos) {
    if (filterFragment.isVisible()) {
      if (filterFragment.showPos == pos) {
        hideFilter();
      } else {
        if (pos == 0) {
          qftStatus.setChecked(false);
          qftTimes.setChecked(false);
        } else if (pos == 1) {
          qftChannel.setChecked(false);
          qftTimes.setChecked(false);
        } else if (pos == 2) {
          qftChannel.setChecked(false);
          qftStatus.setChecked(false);
        }
        filterFragment.showPos = pos;
        filterFragment.showPos(pos);
      }
    } else {
      showFilter(pos);
    }
  }

  public void showFilter(int pos) {
    fragNotiList.setVisibility(View.VISIBLE);
    ViewCompat.animate(fragNotiList).alpha(1).start();
    filterFragment.showPos = pos;
    Fragment f = getChildFragmentManager().findFragmentByTag("filter");
    if (f == null) {
      filterFragment.setFilterResultListener(new NotiSettingFilterFragment.FilterResult() {
        @Override public void onFilterResult(HashMap<String, Object> p) {
          hideFilter();
          presenter.setParams(ListUtils.mapRemoveNull(p));
          try {
            String start = (String)p.get("start");
            String end = (String)p.get("end");
            if (start != null && end != null)
              tvTotalTitle.setText("("+start+"至"+end+")"+"消息发送数量：(成功/总数)");
          }catch (Exception e){
              
          }

          presenter.getList();
        }

        @Override public void onFilterString(String channel, String status, String time) {
          qftChannel.setText(channel == null ? "发送渠道" : channel);
          qftStatus.setText(status == null ? "状态" : status);
          qftTimes.setText(time == null ? "发送时间" : time);
          qftChannel.setTextColorRes(channel != null ? R.color.colorPrimary : R.color.text_dark);
          qftStatus.setTextColorRes(status != null ? R.color.colorPrimary : R.color.text_dark);
          qftTimes.setTextColorRes(time != null ? R.color.colorPrimary : R.color.text_dark);
        }
      });
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_noti_list, filterFragment, "filter")
          .commit();
    } else {
      getChildFragmentManager().beginTransaction().show(f).commit();
    }
  }


  public void hideFilter() {
    qftChannel.setChecked(false);
    qftStatus.setChecked(false);
    qftTimes.setChecked(false);
    ViewCompat.animate(fragNotiList).alpha(0).start();
    getChildFragmentManager().beginTransaction().hide(filterFragment).commit();
    fragNotiList.setVisibility(View.GONE);
  }


}
