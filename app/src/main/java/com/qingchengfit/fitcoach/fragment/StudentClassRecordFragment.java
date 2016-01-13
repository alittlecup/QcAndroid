package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.StudentClassRecordAdapter;
import com.qingchengfit.fitcoach.bean.StatementBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.add1)
    Button add1;
    @Bind(R.id.add2)
    Button add2;
    private RecyclerView mRecyclerView;
    private StudentClassRecordAdapter mAdapter;
    private List<StatementBean> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        add1.setVisibility(View.VISIBLE);
        add2.setVisibility(View.VISIBLE);
        add1.setText("代约私教");
        add2.setText("代约团教");

//        datas.clear();
//        datas.add(new StatementBean(new Date(), "http://mmbiz.qpic.cn/mmbiz/8AuWu0VumXI39Nc61ibOCh2NFDNelSBSZTsTAUXNMLxWrJFqkkks3r0MRhN3ibTb0FFpqEGsod5BblF7iaQeJelzw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1"
//                        , "Pilats mat", "90009099 xxx工作室", true, true, false)
//        );
//        datas.add(new StatementBean(new Date(), "http://mmbiz.qpic.cn/mmbiz/8AuWu0VumXI39Nc61ibOCh2NFDNelSBSZTsTAUXNMLxWrJFqkkks3r0MRhN3ibTb0FFpqEGsod5BblF7iaQeJelzw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1"
//                        , "Pilats mat", "90009099 xxx工作室", true, true, false)
//        );
//        datas.add(new StatementBean(new Date(), "http://mmbiz.qpic.cn/mmbiz/8AuWu0VumXI39Nc61ibOCh2NFDNelSBSZTsTAUXNMLxWrJFqkkks3r0MRhN3ibTb0FFpqEGsod5BblF7iaQeJelzw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1"
//                        , "Pilats mat", "90009099 xxx工作室", true, true, false)
//        );
        mAdapter = new StudentClassRecordAdapter(datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void setDatas(List<StatementBean> d) {
        datas.clear();
        datas.addAll(d);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getTitle() {
        return "上课记录";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}