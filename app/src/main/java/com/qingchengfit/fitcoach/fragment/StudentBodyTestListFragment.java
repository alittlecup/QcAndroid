package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.BodyTestActivity;
import com.qingchengfit.fitcoach.adapter.SimpleAdapter;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.bean.BodyTestBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class StudentBodyTestListFragment extends VpFragment {

    @Bind(R.id.add1)
    Button add1;
    @Bind(R.id.add2)
    Button add2;
    private List<BodyTestBean> mDataList = new ArrayList<>();
    private SimpleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        add1.setVisibility(View.VISIBLE);
        add2.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SimpleAdapter(mDataList);
        mAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent toAdd = new Intent(getActivity(), BodyTestActivity.class);
                toAdd.putExtra("id",mDataList.get(pos).id);
                toAdd.putExtra("type",0);
                startActivity(toAdd);
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;

    }

    @OnClick(R.id.add1)
    public void addTest(){
        Intent toAdd = new Intent(getActivity(), BodyTestActivity.class);
        toAdd.putExtra("type",1);
        startActivity(toAdd);
    }


    public void setData(List<BodyTestBean> strings) {
        mDataList.clear();
        mDataList.addAll(strings);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public String getTitle() {
        return "体测信息";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
