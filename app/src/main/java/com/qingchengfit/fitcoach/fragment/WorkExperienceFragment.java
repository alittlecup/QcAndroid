package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkExperienceFragment extends Fragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private RecordComfirmAdapter adapter;

    public WorkExperienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        ButterKnife.bind(this, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecordComfirmAdapter(new ArrayList<>());
        recyclerview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class WorkExperienceVH extends RecyclerView.ViewHolder {
//        @Bind(R.id.item_time)
//        TextView itemTime;
//        @Bind(R.id.item_studio_name)
//        TextView itemStudioName;
//        @Bind(R.id.item_studio_comfirm)
//        TextView itemStudioComfirm;


        public WorkExperienceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RecordComfirmAdapter extends RecyclerView.Adapter<WorkExperienceVH> {


        private List<BaseInfoBean> datas;

        public RecordComfirmAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public WorkExperienceVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WorkExperienceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexperience, null));
        }

        @Override
        public void onBindViewHolder(WorkExperienceVH holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 3;
        }
    }


}