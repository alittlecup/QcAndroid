package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.views.activity.WebActivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.StudentClassRecordAdapter;
import cn.qingchengfit.bean.StatementBean;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 15/11/20 2015.
 */
public class StudentClassRecordFragment extends VpFragment {

	LinearLayout layoutNoData;
    private RecyclerView mRecyclerView;
    private StudentClassRecordAdapter mAdapter;
    private List<StatementBean> datas = new ArrayList<>();


    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
      layoutNoData = (LinearLayout) view.findViewById(R.id.layout_no_data);

      mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mAdapter = new StudentClassRecordAdapter(datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent toWenb = new Intent(getContext(), WebActivity.class);
                toWenb.putExtra("url", datas.get(pos).url);
                startActivity(toWenb);
            }
        });
        return view;
    }

    public void setDatas(List<StatementBean> d) {
        if (d == null || d.size() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            layoutNoData.setVisibility(View.GONE);
            datas.clear();
            datas.addAll(d);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override public String getTitle() {
        return "上课记录";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
