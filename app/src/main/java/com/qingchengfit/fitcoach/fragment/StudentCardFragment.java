package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.StudentCardAdapter;
import com.qingchengfit.fitcoach.bean.StudentCardBean;

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
public class StudentCardFragment extends VpFragment {
    private StudentCardAdapter mAdapter;
    private List<StudentCardBean> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mData.add(new StudentCardBean("中美引力处置卡", "余额1500块", "工作室", "张三,李四,王五,毛毛,琪琪,和她妈妈到山上去", "2013-10-10至2015-12-12"));
//        mData.add(new StudentCardBean("中美引力处置卡", "余额1500块", "工作室", "张三,李四,王五,毛毛,琪琪,和她妈妈到山上去", "2013-10-10至2015-12-12"));
//        mData.add(new StudentCardBean("中美引力处置卡", "余额1500块", "工作室", "张三,李四,王五,毛毛,琪琪,和她妈妈到山上去", "2013-10-10至2015-12-12"));
//        mData.add(new StudentCardBean("中美引力处置卡", "余额1500块", "工作室", "张三,李四,王五,毛毛,琪琪,和她妈妈到山上去", "2013-10-10至2015-12-12"));
        mAdapter = new StudentCardAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void setmData(List<StudentCardBean> mData) {
        this.mData.clear();
        this.mData.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getTitle() {
        return "会员卡";
    }
}
