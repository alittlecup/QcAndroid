package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.CompatUtils;
import com.qingchengfit.fitcoach.Utils.GlideCircleTransform;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.EventLatestNoti;
import com.qingchengfit.fitcoach.bean.EventNotiFresh;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseSettingFragment {
    public static final String TAG = NotificationFragment.class.getName();
    public static final String[] TYPES = { "COACH_1", "COACH_2", "COACH_3" };

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    NotifiAdapter adapter;
    //    List<QcNotificationResponse.DataEntity.MsgsEntity> list;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.refresh_nodata) SwipeRefreshLayout refreshNodata;
    List<QcNotificationResponse.DataEntity.MsgsEntity> list = new ArrayList<>();
    //    @BindView(R.id.pulltorefresh)
    //    PtrFrameLayout pulltorefresh;
    private int totalPage = 1;
    private int curpage = 1;
    private int unReadCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private Unbinder unbinder;

    public static NotificationFragment newInstance(int type) {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        args.putInt("t", type);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new NotifiAdapter(list);
        adapter.setListener((v, pos) -> {
            QcCloudClient.getApi().postApi.qcClearOneNotification(App.coachid, adapter.datas.get(pos).getId() + "")
                .subscribeOn(Schedulers.io())
                .subscribe();
            adapter.datas.get(pos).setIs_read(true);
            unReadCount--;
            if (unReadCount < 1) {
                RxBus.getBus().post(new EventNotiFresh());
            }
            if (!TextUtils.isEmpty(adapter.datas.get(pos).getUrl())) {
                Intent toWeb = new Intent(getContext(), WebActivity.class);
                toWeb.putExtra("url", adapter.datas.get(pos).getUrl());

                startActivity(toWeb);
            }
            adapter.notifyItemChanged(pos);
        });
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean isScrollDown = false;

            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int last = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int total = linearLayoutManager.getItemCount();
                    if (last == (total - 1) && isScrollDown) {
                        loadingMore();
                    }
                }
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0 || dy > 0) {
                    isScrollDown = true;
                } else {
                    isScrollDown = false;
                }
            }
        });
        refresh.setColorSchemeResources(R.color.primary);
        refreshNodata.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {

                unReadCount = 0;
                curpage = 1;
                onRefesh();
            }
        });
        refreshNodata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                list.clear();
                unReadCount = 0;
                curpage = 1;
                onRefesh();
            }
        });
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(), this);
                refresh.setRefreshing(true);
                onRefesh();
            }
        });
        list.clear();
        //        onRefesh();
        return view;
    }

    private void loadingMore() {
        if (curpage < totalPage) {
            curpage++;
            onRefesh();
        } else {
            ToastUtils.showDefaultStyle("无更多通知");
        }
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void freshData() {
        list.clear();
        unReadCount = 0;
        curpage = 1;
        onRefesh();
    }

    public synchronized void onRefesh() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", curpage + "");
        params.put("tab", TYPES[getArguments().getInt("t")]);
        QcCloudClient.getApi().getApi.qcGetMessages(App.coachid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<QcNotificationResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    if (refresh != null) {
                        refresh.setRefreshing(false);
                        refreshNodata.setRefreshing(false);
                        ToastUtils.show(R.drawable.ic_share_fail, "网络错误");
                    }
                }

                @Override public void onNext(QcNotificationResponse qcNotificationResponse) {
                    if (refresh != null) {
                        list.clear();
                        totalPage = qcNotificationResponse.getData().getPages();
                        list.addAll(qcNotificationResponse.getData().getNotifications());
                        int fistUnread = -1;
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (!list.get(i).is_read()) {
                                    if (fistUnread < 0) fistUnread = i;
                                    unReadCount++;
                                }
                            }
                            refresh.setVisibility(View.VISIBLE);
                            refreshNodata.setVisibility(View.GONE);

                            adapter.notifyDataSetChanged();
                            //                                recyclerview.setAdapter(adapter);
                        } else {
                            refresh.setVisibility(View.GONE);
                            refreshNodata.setVisibility(View.VISIBLE);
                        }
                        refresh.setRefreshing(false);
                        refreshNodata.setRefreshing(false);

                        RxBus.getBus().post(new EventNotiFresh());
                        if (fistUnread > 0) {
                            RxBus.getBus()
                                .post(new EventLatestNoti(DateUtils.formatDateFromServer(
                                    qcNotificationResponse.getData().getNotifications().get(fistUnread).getCreated_at()).getTime(),
                                    getArguments().getInt("t")));
                        }
                    }
                }
            });
    }

    @Override public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class NotifiVH extends RecyclerView.ViewHolder {

        @BindView(R.id.item_noti_unread) ImageView itemNotiUnread;
        @BindView(R.id.item_noti_icon) ImageView itemNotiIcon;
        @BindView(R.id.item_noti_title) TextView itemNotiTitle;
        @BindView(R.id.item_noti_time) TextView itemNotiTime;
        @BindView(R.id.item_noti_sender) TextView itemNotiSender;
        @BindView(R.id.item_noti_desc) TextView itemDesc;
        @BindView(R.id.icon_right) ImageView iconRight;

        public NotifiVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NotifiAdapter extends RecyclerView.Adapter<NotifiVH> implements View.OnClickListener {

        private List<QcNotificationResponse.DataEntity.MsgsEntity> datas;
        private OnRecycleItemClickListener listener;

        public NotifiAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public NotifiVH onCreateViewHolder(ViewGroup parent, int viewType) {
            NotifiVH holder = new NotifiVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifacation, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(NotifiVH holder, int position) {
            holder.itemView.setTag(position);
            QcNotificationResponse.DataEntity.MsgsEntity entity = datas.get(position);
            if (TextUtils.isEmpty(entity.getUrl())) {
                holder.iconRight.setVisibility(View.GONE);
            } else {
                holder.iconRight.setVisibility(View.VISIBLE);
            }
            holder.itemNotiTitle.setText(entity.getTitle());
            holder.itemNotiTime.setText(entity.getCreated_at().replace("T", " "));
            holder.itemNotiSender.setText(entity.getSender());
            Glide.with(App.AppContex).load(entity.getPhoto()).transform(new GlideCircleTransform(getContext())).into(holder.itemNotiIcon);
            holder.itemDesc.setText(entity.getDescription());
            if (!entity.is_read()) {
                holder.itemNotiUnread.setVisibility(View.VISIBLE);
            } else {
                holder.itemNotiUnread.setVisibility(View.INVISIBLE);
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
