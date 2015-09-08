package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;
import com.qingchengfit.fitcoach.component.RecycleDivider;

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
 * Created by Paper on 15/9/7 2015.
 */
public class WorkExepSettingFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private WorkExepAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(getActivity().getString(R.string.workexper_title));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.inflateMenu(R.menu.add);
//        toolbar.setOnMenuItemClickListener(item -> {
//            Fragment fragment = RecordEditFragment.newInstance("添加认证", null);
//            getFragmentManager().beginTransaction().replace(R.id.settting_fraglayout, fragment).commit();
//            return true;

//        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerview.addItemDecoration(new RecycleDivider(getActivity()));

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new WorkExepAdapter(new ArrayList<>());
        adapter.setListener((v, pos) -> {
            //TODO 添加数据内容
            Fragment fragment = new WorkExpeEditFragment();
            getFragmentManager().beginTransaction().replace(R.id.settting_fraglayout, fragment).addToBackStack("").commit();
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

    public static class WorkExepVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_workexpe_name)
        TextView itemWorkexpeName;
        @Bind(R.id.item_workexpe_time)
        TextView itemWorkexpeTime;
        @Bind(R.id.item_workexpe)
        LinearLayout itemWorkexpe;

        public WorkExepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WorkExepAdapter extends RecyclerView.Adapter<WorkExepVH> implements View.OnClickListener {


        private List<BaseInfoBean> datas;
        private OnRecycleItemClickListener listener;

        public WorkExepAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public WorkExepVH onCreateViewHolder(ViewGroup parent, int viewType) {
            WorkExepVH holder = new WorkExepVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workexpe, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(WorkExepVH holder, int position) {
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
