package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.views.VpFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.BaseInfoAdapter;
import cn.qingchengfit.bean.BaseInfoBean;
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
public class StudentBaseInfoFragment extends VpFragment {
    private RecyclerView mRecyclerView;
    private BaseInfoAdapter mAdapter;
    private List<BaseInfoBean> datas = new ArrayList<>();


    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_phone, "电话", "123123123"));
        //        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_city, "城市", "北京"));
        //        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_wechat, "微信", "北京朝阳"));
        mAdapter = new BaseInfoAdapter(datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void setDatas(List<BaseInfoBean> d) {
        datas.clear();
        datas.addAll(d);
        mAdapter.notifyDataSetChanged();
    }

    @Override public String getTitle() {
        return "基本信息";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
