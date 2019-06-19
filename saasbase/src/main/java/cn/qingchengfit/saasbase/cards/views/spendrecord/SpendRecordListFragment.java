package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.StatementBean;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

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
 * Created by Paper on 16/4/29 2016.
 */
@Leaf(module = "card", path = "/consumption/list") public class SpendRecordListFragment
    extends BaseFragment implements SpendRecordListView {

  TextView addSum;
  TextView minusSum;
  RecycleViewWithNoImg recycleview;
  @Inject SpendRecordListPresenter presenter;
  private StudentClassRecordAdapter adater;
  private List<StatementBean> datas = new ArrayList<>();
  private int year, month;
  private String cardId;

  public void setShowAddIcon(boolean showAddIcon) {
    this.showAddIcon = showAddIcon;
  }

  boolean showAddIcon = true;

  public static SpendRecordListFragment newInstance(String cardId, int year, int month) {

    Bundle args = new Bundle();

    args.putInt("year", year);
    args.putInt("month", month);
    args.putString("card_id", cardId);
    SpendRecordListFragment fragment = new SpendRecordListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      year = getArguments().getInt("year");
      month = getArguments().getInt("month");
      cardId = getArguments().getString("card_id");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_spend_record_list, container, false);
    addSum = (TextView) view.findViewById(R.id.add_sum);
    minusSum = (TextView) view.findViewById(R.id.minus_sum);
    recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recyclerview);

    delegatePresenter(presenter, this);
    final LinearLayoutManager manger = new LinearLayoutManager(getContext());
    recycleview.setLayoutManager(manger);
    adater = new StudentClassRecordAdapter(datas);
    recycleview.setAdapter(adater);
    LinearLayout llcontainer = view.findViewById(R.id.ll_container);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        presenter.queryData(year, month, true, cardId);
      }
    });
    recycleview.addScrollListener(new EndlessRecyclerOnScrollListener(manger) {

      @Override public void onLoadMore() {
        presenter.queryData(year, month, false, cardId);
      }
    });
    presenter.queryData(year, month, true, cardId);
    llcontainer.setVisibility(showAddIcon ? View.VISIBLE : View.GONE);
    return view;
  }

  @Override public String getFragmentName() {
    return SpendRecordListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onRecordList(List<StatementBean> d, int page) {
    if (page == 1) datas.clear();
    datas.addAll(d);
    adater.notifyDataSetChanged();
    recycleview.setFresh(false);
    recycleview.setNoData(datas.size() == 0);
  }

  @Override public void onAccount(float account, float cost) {
    addSum.setText(String.format(Locale.CHINA, "%.2f", account));
    minusSum.setText(String.format(Locale.CHINA, "%.2f", cost));
  }

  @Override public void onFailed(String s) {
    ToastUtils.show(s);
  }
}
