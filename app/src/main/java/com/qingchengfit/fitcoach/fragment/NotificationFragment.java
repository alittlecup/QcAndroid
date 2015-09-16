package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.component.GlideCircleTransform;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
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
    @Bind(R.id.pulltorefresh)
    PtrFrameLayout pulltorefresh;


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
//        adapter = new NotifiAdapter(list);
        final StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, MeasureUtils.dpToPx(15f, getResources()), 0, 0);
        header.initWithStringArray(R.array.storehouse);
        pulltorefresh.setHeaderView(header);
        pulltorefresh.addPtrUIHandler(new PtrUIHandler() {
            @Override
            public void onUIReset(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                onRefesh();
            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b1, PtrIndicator ptrIndicator) {

            }
        });
        onRefesh();
        return view;
    }

    public void onRefesh() {
        QcCloudClient.getApi().getApi.qcGetMessages()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        qcNotificationResponse -> {
//                            list.clear();
//                            list.addAll(qcNotificationResponse.getData().getMsgs());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list = qcNotificationResponse.getData().getMsgs();
                                    adapter = new NotifiAdapter(qcNotificationResponse.getData().getMsgs());
                                    adapter.setListener((v, pos) -> {
                                        fragmentCallBack.onFragmentChange(
                                                WebFragment.newInstance(list.get(pos).getUrl()));
                                        fragmentCallBack.onToolbarMenu(0, 0, "通知详情");
                                    });

                                    recyclerview.setAdapter(adapter);
                                    pulltorefresh.refreshComplete();
                                }
                            });

                        }
                );
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
            holder.itemNotiTime.setText(entity.getCreated_at());
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
