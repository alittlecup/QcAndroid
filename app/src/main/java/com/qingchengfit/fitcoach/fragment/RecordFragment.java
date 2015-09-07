package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
public class RecordFragment extends Fragment {
    public static final String TAG = RecordFragment.class.getName();
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private RecordComfirmAdapter adapter;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(getActivity().getString(R.string.record_title));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(item -> {
            Fragment fragment = RecordEditFragment.newInstance("添加认证", null);
            getFragmentManager().beginTransaction().add(R.id.settting_fraglayout, fragment).show(fragment).commit();
            return true;

        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecordComfirmAdapter(new ArrayList<>());
        adapter.setListener((v, pos) -> {
            //TODO 添加数据内容
            Fragment fragment = RecordEditFragment.newInstance("添加认证", null);
            getFragmentManager().beginTransaction().add(R.id.settting_fraglayout, fragment).show(fragment).commit();
        });
        recyclerview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


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
