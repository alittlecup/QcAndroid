package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.WebActivityWithShare;
import com.qingchengfit.fitcoach.bean.MeetingBean;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcMeetingResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qingchengfit.utils.DateUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class MeetingFragment extends BaseFragment {


    List<MeetingBean> mMeetingDatas = new ArrayList<>();
    private RecyclerView mRecyclerviewRecyclerView;
    private SwipeRefreshLayout mRefreshSwipeRefreshLayout;
    private Toolbar mToolbar;
    private MeetingAdapter meetingAdapter;

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
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        mToolbar.setTitle("会议培训");

        mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRefreshSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mRefreshSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mRecyclerviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        freshData();
        meetingAdapter = new MeetingAdapter(mMeetingDatas);
        mRecyclerviewRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerviewRecyclerView.setAdapter(meetingAdapter);

        meetingAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent toWeb = new Intent(getActivity(), WebActivityWithShare.class);
                toWeb.putExtra("url", mMeetingDatas.get(pos).url);
                startActivity(toWeb);
            }
        });
        return view;
    }

    private void freshData() {
        HashMap<String,String> params = new HashMap<>();
        params.put("oem",getString(R.string.oem_tag));
        QcCloudClient.getApi().getApi.qcGetMeetingList(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcMeetingResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(QcMeetingResponse qcMeetingResponse) {
                        mMeetingDatas.clear();
                        for (QcMeetingResponse.Meeting meeting : qcMeetingResponse.data.meetings) {
                            MeetingBean bean = new MeetingBean();
                            bean.title = meeting.name;
                            bean.address = meeting.city + "  " + meeting.address;
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(meeting.open_start)))
                                    .append("至").append(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(meeting.open_end)));
                            bean.time = stringBuffer.toString();
                            bean.img = meeting.image;
                            bean.url = meeting.link;
                            mMeetingDatas.add(bean);
                        }
                        meetingAdapter.notifyDataSetChanged();
                        mRefreshSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void lazyLoad() {

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
