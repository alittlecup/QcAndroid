package com.qingchengfit.fitcoach.fragment;


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
import com.paper.paperbaselibrary.component.GlideCircleTransform;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseSettingFragment {
    public static final String TAG = NotificationFragment.class.getName();
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    NotifiAdapter adapter;
    List<QcNotificationResponse.DataEntity.MsgsEntity> list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.refresh_nodata)
    SwipeRefreshLayout refreshNodata;
//    @Bind(R.id.pulltorefresh)
//    PtrFrameLayout pulltorefresh;


    public NotificationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, R.drawable.ic_arrow_left, "全部通知");
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new NotifiAdapter(list);
//        final StoreHouseHeader header = new StoreHouseHeader(getContext());
//        header.setPadding(0, MeasureUtils.dpToPx(15f, getResources()), 0, 0);
//        header.initWithStringArray(R.array.storehouse);


//        pulltorefresh.setHeaderView(header);
//        pulltorefresh.addPtrUIHandler(new PtrUIHandler() {
//            @Override
//            public void onUIReset(PtrFrameLayout ptrFrameLayout) {
//
//            }
//
//            @Override
//            public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
//
//            }
//
//            @Override
//            public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                onRefesh();
//            }
//
//            @Override
//            public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
//
//            }
//
//            @Override
//            public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b1, PtrIndicator ptrIndicator) {
//
//            }
//        });
//        onRefesh();
        refresh.setColorSchemeResources(R.color.primary);
        refreshNodata.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefesh();
            }
        });
        refreshNodata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefesh();
            }
        });
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                refresh.setRefreshing(true);
                onRefesh();
            }
        });

        return view;
    }

    public void onRefesh() {
        QcCloudClient.getApi().getApi.qcGetMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QcNotificationResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        refresh.setRefreshing(false);
                        refreshNodata.setRefreshing(false);
                        ToastUtils.show(R.drawable.ic_share_fail, "网络错误");
                    }

                    @Override
                    public void onNext(QcNotificationResponse qcNotificationResponse) {
                        list = qcNotificationResponse.getData().getMsgs();
                        if (list != null && list.size() > 0) {
                            refresh.setVisibility(View.VISIBLE);
                            refreshNodata.setVisibility(View.GONE);
                            adapter = new NotifiAdapter(qcNotificationResponse.getData().getMsgs());
                            adapter.setListener((v, pos) -> {
//                                fragmentCallBack.onFragmentChange(WebFragment.newInstance(list.get(pos).getUrl(), true));
                                fragmentCallBack.onFragmentChange(NotiDetailFragment.newInstance(adapter.datas.get(pos).getId()));
                                fragmentCallBack.onToolbarMenu(0, 0, "通知详情");
                            });
                            recyclerview.setAdapter(adapter);
                        } else {
                            refresh.setVisibility(View.GONE);
                            refreshNodata.setVisibility(View.VISIBLE);
                        }
                        refresh.setRefreshing(false);
                        refreshNodata.setRefreshing(false);

                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentCallBack.onToolbarMenu(0, 0, "全部通知");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class NotifiVH extends RecyclerView.ViewHolder {

        @Bind(R.id.item_noti_unread)
        ImageView itemNotiUnread;
        @Bind(R.id.item_noti_icon)
        ImageView itemNotiIcon;
        @Bind(R.id.item_noti_title)
        TextView itemNotiTitle;
        @Bind(R.id.item_noti_time)
        TextView itemNotiTime;
        @Bind(R.id.item_noti_sender)
        TextView itemNotiSender;

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

        @Override
        public NotifiVH onCreateViewHolder(ViewGroup parent, int viewType) {
            NotifiVH holder = new NotifiVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifacation, parent, false));
            holder.itemView.setOnClickListener(this);


            return holder;
        }

        @Override
        public void onBindViewHolder(NotifiVH holder, int position) {
            holder.itemView.setTag(position);
            QcNotificationResponse.DataEntity.MsgsEntity entity = datas.get(position);
            holder.itemNotiTitle.setText(entity.getTitle());
            holder.itemNotiTime.setText(entity.getCreated_at().replace("T", " "));
            holder.itemNotiSender.setText(entity.getSender());
            Glide.with(App.AppContex).load(entity.getPhoto()).transform(new GlideCircleTransform(getContext())).into(holder.itemNotiIcon);
            if (TextUtils.isEmpty(entity.getCreated_at())) {
                holder.itemNotiUnread.setVisibility(View.VISIBLE);
            } else {
                holder.itemNotiUnread.setVisibility(View.INVISIBLE);
            }
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
