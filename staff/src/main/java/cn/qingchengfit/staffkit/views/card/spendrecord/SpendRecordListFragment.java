package cn.qingchengfit.staffkit.views.card.spendrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.StatementBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.StudentClassRecordAdapter;
import cn.qingchengfit.staffkit.views.custom.EndlessRecyclerOnScrollListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ToastUtils;
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
public class SpendRecordListFragment extends BaseFragment implements SpendRecordListView {

    @BindView(R.id.add_sum) TextView addSum;
    @BindView(R.id.minus_sum) TextView minusSum;
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recycleview;
    @Inject SpendRecordListPresenter presenter;
    private StudentClassRecordAdapter adater;
    private List<StatementBean> datas = new ArrayList<>();
    private int year, month;

    public static SpendRecordListFragment newInstance(int year, int month) {

        Bundle args = new Bundle();

        args.putInt("year", year);
        args.putInt("month", month);
        SpendRecordListFragment fragment = new SpendRecordListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            year = getArguments().getInt("year");
            month = getArguments().getInt("month");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_spend_record_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        final LinearLayoutManager manger = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(manger);
        adater = new StudentClassRecordAdapter(datas);
        recycleview.setAdapter(adater);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData(year, month, true);
            }
        });
        recycleview.addScrollListener(new EndlessRecyclerOnScrollListener(manger) {

            @Override public void onLoadMore() {
                presenter.queryData(year, month, false);
            }
        });
        presenter.queryData(year, month, true);
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
