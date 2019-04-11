package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.EventLatestNoti;
import cn.qingchengfit.bean.EventNotiFresh;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.responese.NotificationMsg;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saasbase.constant.ConstantNotification;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.CompatUtils;
import com.qingchengfit.fitcoach.Utils.GlideCircleTransform;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {
  public static final String TAG = NotificationFragment.class.getName();
  public static final String[] TYPES = { "COACH_1", "COACH_2", "COACH_3" };

  RecyclerView recyclerview;
  NotifiAdapter adapter;
  SwipeRefreshLayout refresh;
  SwipeRefreshLayout refreshNodata;
  List<NotificationMsg> list = new ArrayList<>();
  Toolbar toolbar;
  TextView toolbarTitile;
  FrameLayout toolbarLayout;
  private int totalPage = 1;
  private int curpage = 1;
  private int unReadCount = 0;
  private LinearLayoutManager linearLayoutManager;

  @Inject RepoCoachServiceImpl repoCoachService;

  private String type;

  public NotificationFragment() {
  }

  public static NotificationFragment newInstance(String type) {
    Bundle args = new Bundle();
    NotificationFragment fragment = new NotificationFragment();
    args.putString("t", type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getString("t");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notification, container, false);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    refreshNodata = (SwipeRefreshLayout) view.findViewById(R.id.refresh_nodata);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);

    initToolbar(toolbar);
    linearLayoutManager = new LinearLayoutManager(getContext());
    recyclerview.setLayoutManager(linearLayoutManager);
    recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    adapter = new NotifiAdapter(list);
    adapter.setListener((v, pos) -> {
      NotificationMsg notificationMsg = adapter.datas.get(pos);
      TrainerRepository.getStaticTrainerAllApi()
          .qcClearOneNotification(App.coachid, notificationMsg.getId() + "")
          .compose(RxHelper.schedulersTransformer())
          .doOnTerminate(this::hideLoading)
          .subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
              }
            }
          }, new NetWorkThrowable());
      notificationMsg.setIs_read(true);
      unReadCount--;
      if (unReadCount < 1) {
        RxBus.getBus().post(new EventNotiFresh());
      }
      if (!TextUtils.isEmpty(notificationMsg.getUrl())) {
        if (notificationMsg.getUrl().startsWith("http")) {
          Intent toWeb = new Intent(getContext(), WebActivity.class);
          toWeb.putExtra("url", notificationMsg.getUrl());
          startActivity(toWeb);
        } else {
          try {
            Uri uri = Uri.parse(notificationMsg.getUrl());
            Intent tosb = new Intent(Intent.ACTION_VIEW, uri);
            tosb.setPackage(getContext().getPackageName());
            if (uri.getQueryParameterNames() != null) {
              for (String key : uri.getQueryParameterNames()) {
                tosb.putExtra(key, uri.getQueryParameter(key));
              }
            }
            tosb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(tosb);
          } catch (Exception e) {
            LogUtil.e(e.getMessage());
          }
        }
      } else if (notificationMsg.type == 24) {//教练申请场馆被加入，要求点击跳转到场馆页面
        showLoading();
        RxRegiste(TrainerRepository.getStaticTrainerAllApi()
            .qcGetCoachService(App.coachid)
            .compose(RxHelper.schedulersTransformer())
            .doOnTerminate(this::hideLoading)
            .subscribe(qcCoachServiceResponse -> {
              if (ResponseConstant.checkSuccess(qcCoachServiceResponse)) {
                repoCoachService.createServices(qcCoachServiceResponse.data.services);
                PreferenceUtils.setPrefString(getContext(), "coachservice_id_str", notificationMsg.getShop_id());
                Intent toMain = new Intent(getActivity(), Main2Activity.class);
                toMain.putExtra(Main2Activity.ACTION, 10);
                startActivity(toMain);
                getActivity().finish();
              } else {
                ToastUtils.show("网络连接异常，请重新打开 App");
              }
            }, throwable -> {
            }));
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
    refresh.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(ConstantNotification.getNotiStr(type));
    toolbar.inflateMenu(R.menu.menu_clear_noti);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxRegiste(TrainerRepository.getStaticTrainerAllApi()
            .qcClearTypeNoti(
                new ClearNotiBody(ConstantNotification.getCategloreStr(getContext(), type)))
            .compose(RxHelper.schedulersTransformer())
            .doOnTerminate(() -> hideLoading())
            .subscribe(new Action1<QcResponse>() {
              @Override public void call(QcResponse qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                  freshData();
                }
              }
            }, new NetWorkThrowable()));
        return true;
      }
    });
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
    HashMap<String, Object> params = new HashMap<>();
    params.put("page", curpage + "");
    params.put("type__in", ConstantNotification.getCategloreStr(getContext(), type));
    TrainerRepository.getStaticTrainerAllApi()
        .qcGetNotification(params)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(qcNotificationResponse -> {
          list.clear();
          totalPage = qcNotificationResponse.getData().pages;
          list.addAll(qcNotificationResponse.getData().notifications);
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
                    qcNotificationResponse.getData().notifications.get(fistUnread).getCreated_at())
                    .getTime(), getArguments().getInt("t")));
          }
        }, new NetWorkThrowable());
  }

  @Override public void onResume() {
    super.onResume();

    adapter.notifyDataSetChanged();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public interface OnRecycleItemClickListener {
    void onItemClick(View v, int pos);
  }

  public static class NotifiVH extends RecyclerView.ViewHolder {

    ImageView itemNotiUnread;
    ImageView itemNotiIcon;
    TextView itemNotiTitle;
    TextView itemNotiTime;
    TextView itemNotiSender;
    TextView itemDesc;
    ImageView iconRight;

    public NotifiVH(View view) {
      super(view);
      itemNotiUnread = (ImageView) view.findViewById(R.id.item_noti_unread);
      itemNotiIcon = (ImageView) view.findViewById(R.id.item_noti_icon);
      itemNotiTitle = (TextView) view.findViewById(R.id.item_noti_title);
      itemNotiTime = (TextView) view.findViewById(R.id.item_noti_time);
      itemNotiSender = (TextView) view.findViewById(R.id.item_noti_sender);
      itemDesc = (TextView) view.findViewById(R.id.item_noti_desc);
      iconRight = (ImageView) view.findViewById(R.id.icon_right);
    }
  }

  class NotifiAdapter extends RecyclerView.Adapter<NotifiVH> implements View.OnClickListener {

    private List<NotificationMsg> datas;
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
      NotifiVH holder = new NotifiVH(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_notifacation, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    }

    @Override public void onBindViewHolder(NotifiVH holder, int position) {
      holder.itemView.setTag(position);
      NotificationMsg entity = datas.get(position);
      if (TextUtils.isEmpty(entity.getUrl()) && entity.type != 24) {
        holder.iconRight.setVisibility(View.GONE);
      } else {
        holder.iconRight.setVisibility(View.VISIBLE);
      }
      holder.itemNotiTitle.setText(entity.getTitle());
      holder.itemNotiTime.setText(entity.getCreated_at().replace("T", " "));
      holder.itemNotiSender.setText(entity.getSender());
      Glide.with(App.AppContex)
          .load(entity.getPhoto())
          .transform(new GlideCircleTransform(getContext()))
          .into(holder.itemNotiIcon);
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
