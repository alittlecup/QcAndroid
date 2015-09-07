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
import com.qingchengfit.fitcoach.bean.BaseInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordComfirmFragment extends Fragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private RecordComfirmAdapter adapter;

    public RecordComfirmFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_comfirm, container, false);
        ButterKnife.bind(this, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecordComfirmAdapter(new ArrayList<>());
        adapter.setListener((v, pos) -> {
            ComfirmDetailFragment fragment = new ComfirmDetailFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.myhome_fraglayout, fragment)
                    .show(fragment).addToBackStack("").commit();
        });
        recyclerview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public interface OnRecycleItemClickListener {
        public void onItemClick(View v, int pos);
    }

    public static class RecordComfirmVH extends RecyclerView.ViewHolder {
        @Bind(R.id.recordcomfirm_title)
        TextView recordcomfirmTitle;
        @Bind(R.id.recordcomfirm_subtitle)
        TextView recordcomfirmSubtitle;
        @Bind(R.id.recordcomfirm_time)
        TextView recordcomfirmTime;

        public RecordComfirmVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RecordComfirmAdapter extends RecyclerView.Adapter<RecordComfirmVH> implements View.OnClickListener {

        private List<BaseInfoBean> datas;
        private OnRecycleItemClickListener listener;

        public RecordComfirmAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public RecordComfirmVH onCreateViewHolder(ViewGroup parent, int viewType) {
            RecordComfirmVH holder = new RecordComfirmVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordcomfirm, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecordComfirmVH holder, int position) {
            holder.itemView.setTag(position);
        }


        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

}
