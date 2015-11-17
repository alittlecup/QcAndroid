package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.MeetingBean;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MeetingFragment extends Fragment {


    List<MeetingBean> mMeetingDatas = new ArrayList<>();
    private RecyclerView mRecyclerviewRecyclerView;
    private SwipeRefreshLayout mRefreshSwipeRefreshLayout;

    public MeetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting, container, false);
        mRecyclerviewRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRefreshSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mRefreshSwipeRefreshLayout.isRefreshing())
                    freshData();
            }
        });
        mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRefreshSwipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRefreshSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mRecyclerviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MeetingAdapter meetingAdapter = new MeetingAdapter(mMeetingDatas);
        mRecyclerviewRecyclerView.setAdapter(meetingAdapter);

        return view;
    }

    private void freshData() {

    }

    /**
     * recycle adapter
     */
    public static class MeetingHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mTimeTextView;
        TextView mAddressTextView;
        ImageView mImgImageView;

        public MeetingHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.meeting_title);
            mTimeTextView = (TextView) itemView.findViewById(R.id.meeting_time);
            mAddressTextView = (TextView) itemView.findViewById(R.id.meeting_address);
            mImgImageView = (ImageView) itemView.findViewById(R.id.meeting_img);
        }
    }

    public class MeetingAdapter extends RecyclerView.Adapter<MeetingHolder> implements View.OnClickListener {
        List<MeetingBean> datas;
        private OnRecycleItemClickListener listener;

        public MeetingAdapter(List<MeetingBean> d) {
            this.datas = d;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public MeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MeetingHolder holder = new MeetingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false));

            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeetingHolder holder, int position) {
            holder.itemView.setTag(position);
            MeetingBean meetingBean = datas.get(position);
            holder.mTimeTextView.setText(meetingBean.time);
            holder.mAddressTextView.setText(meetingBean.address);
            holder.mTitleTextView.setText(meetingBean.title);
            Glide.with(getContext()).load(meetingBean.img).into(holder.mImgImageView);

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }


}
