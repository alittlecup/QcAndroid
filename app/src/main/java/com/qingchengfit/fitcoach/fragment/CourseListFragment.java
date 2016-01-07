package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImageTwoTextAdapter;
import com.qingchengfit.fitcoach.adapter.ImageTwoTextBean;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends VpFragment {



    @Bind(R.id.course_count)
    TextView courseCount;
    @Bind(R.id.preview)
    TextView preview;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private ImageTwoTextAdapter mImageTwoTextAdapter;
    private List<ImageTwoTextBean> datas = new ArrayList<>();
    private int mType =1;//当前页的类型

    public CourseListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        ButterKnife.bind(this, view);
        mImageTwoTextAdapter = new ImageTwoTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mImageTwoTextAdapter);
        mImageTwoTextAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ImageTwoTextBean b = datas.get(pos);
                if (b.type == 1){
                    //添加课程
                }else {
                    //课程详情
                    
                }
            }
        });
        return view;
    }

    /**
     * 预约课程 跳转到web页面
     */
    @OnClick(R.id.preview)
    public void onPreview(){

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public String getTitle() {
        if (mType == 1){
            return getString(R.string.course_private);
        }else if (mType ==2){
            return getString(R.string.course_group);
        }else {
            return getString(R.string.course_group);
        }

    }
}
