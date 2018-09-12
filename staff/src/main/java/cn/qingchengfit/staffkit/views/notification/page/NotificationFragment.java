package cn.qingchengfit.staffkit.views.notification.page;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.utils.DialogUtils;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.NotificationMsg;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.views.CardDetailParams;
import cn.qingchengfit.saasbase.constant.ConstantNotification;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventClearAllNoti;
import cn.qingchengfit.staffkit.rxbus.event.EventLatestNoti;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static cn.qingchengfit.utils.ToastUtils.show;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/9/21.
 */
public class NotificationFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, NotificationPresenter.MVPView,
    FlexibleAdapter.EndlessScrollListener {
  public static final String[] TYPES = { "STAFF_1", "STAFF_2" };
  public static final List<Long> MSG_TYPES = new ArrayList<>(Arrays.asList(1l, 2l));

  RecycleViewWithNoImg rv;
  @Inject NotificationPresenter presenter;
  @Inject RestRepository mRestRepository;
  @Inject SerPermisAction serPermisAction;
  @Inject GymBaseInfoAction gymBaseInfoAction;
  @Inject GymWrapper gymWrapper;
  Toolbar toolbar;
  TextView toolbarTitile;
  FrameLayout toolbarLayout;
  private List<AbstractFlexibleItem> mData = new ArrayList<>();
  private CommonFlexAdapter mAdatper;
  private int unReadCount = 0;
  private String type;

  public static NotificationFragment newInstance(String type) {

    Bundle args = new Bundle();
    args.putString("t", type);
    NotificationFragment fragment = new NotificationFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getString("t", "");
  }

  public int getUnReadCount() {
    return unReadCount;
  }

  public void setUnReadCount(int unReadCount) {
    this.unReadCount = unReadCount;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
    rv = (RecycleViewWithNoImg) view.findViewById(R.id.rv);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    mAdatper = new CommonFlexAdapter(mData, this);
    mAdatper.setEndlessScrollListener(this, new ProgressItem(getContext()));
    rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    rv.setItemAnimator(new FadeInUpItemAnimator());
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.setAdapter(mAdatper);
    rv.setOnRefreshListener(() -> refresh());
    refresh();
    RxBusAdd(EventClearAllNoti.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventClearAllNoti>() {
          @Override public void call(EventClearAllNoti eventClearAllNoti) {
            mAdatper.setEndlessScrollListener(NotificationFragment.this,
                new ProgressItem(getContext()));
            refresh();
          }
        });

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(ConstantNotification.getNotiStr(type));
    toolbar.inflateMenu(R.menu.menu_all_read);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        presenter.clearNoti(ConstantNotification.getCategloreStr(getContext(), type));
        return true;
      }
    });
  }

  public void refresh() {
    presenter.refresh(ConstantNotification.getCategloreStr(getContext(), type));
  }

  @Override public String getFragmentName() {
    return NotificationFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if (mAdatper.getItem(position) instanceof NotificationItem) {
      final NotificationMsg msg = ((NotificationItem) mAdatper.getItem(position)).getMsg();
      curMsg = msg;
      presenter.clearOneNoti(App.staffId, msg.getId() + "", position);
      if (StringUtils.isEmpty(msg.getUrl()) && msg.type < 11) {

      } else {
        String s = msg.getUrl();

        try {
          Intent toActivity = null;
          if (!TextUtils.isEmpty(s)) {

          } else {

          }
          if (!StringUtils.isEmpty(msg.getBrand_id())
              && !StringUtils.isEmpty(msg.getShop_id())
              && msg.type < 20) {
            final CoachService coachService1 =
                gymBaseInfoAction.getGymByShopIdNow(msg.getBrand_id(), msg.getShop_id());
            if (coachService1 != null) {
              HashMap<String, Object> p = new HashMap<>();
              p.put("id", coachService1.getId());
              p.put("model", coachService1.getModel());
              mRestRepository.getGet_api()
                  .qcPermission(App.staffId, p)
                  .onBackpressureBuffer()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<QcResponsePermission>() {
                    @Override public void call(QcResponsePermission qcResponse) {
                      if (ResponseConstant.checkSuccess(qcResponse)) {
                        serPermisAction.writePermiss(qcResponse.data.permissions);
                        Intent toActivity = null;

                        if (!StringUtils.isEmpty(msg.getUrl()) && msg.type != 17) {
                          toActivity =
                              new Intent(getContext().getPackageName(), Uri.parse(msg.getUrl()));
                          toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else if (msg.type == 11 || msg.type == 12 || msg.type == 16) {
                          gymWrapper.setCoachService(coachService1);
                          QcRouteUtil.setRouteOptions(
                              new RouteOptions("student").setActionName("/student/home")).call();
                          return;
                          //toActivity = new Intent(getActivity(), cn.qingchengfit.saasbase.mvvm_student.StudentActivity.class);
                        } else if (msg.type == 13) {
                          if (msg.card_id == 0) return;
                          //toActivity = new Intent(getActivity(), CardDetailActivity.class);
                          Card realCard = new Card();
                          realCard.setCard_no("#70A4A9");
                          realCard.setId(msg.card_id + "");
                          routeTo("card", "/detail/",
                              CardDetailParams.builder().cardid(msg.card_id + "").build());
                          //toActivity.putExtra(Configs.EXTRA_REAL_CARD, realCard);
                        } else if (msg.type == 17) {
                          showLoading();
                          presenter.checkoutSellerStudentPermission(msg.getUser().getId());
                          return;
                        } else if (msg.type == 19) {
                          gymWrapper.setCoachService(coachService1);
                          QcRouteUtil.setRouteOptions(
                              new RouteOptions("student").setActionName("/student/birthday"))
                              .call();
                          return;
                        }

                        toActivity.putExtra(Configs.EXTRA_GYM_SERVICE, coachService1);
                        toActivity.putExtra(Configs.EXTRA_GYM_STATUS,
                            new GymStatus.Builder().isGuide(false).isSingle(false).build());
                        toActivity.putExtra(Configs.EXTRA_BRAND,
                            new Brand.Builder().id(coachService1.brand_id()).build());
                        Single.just(toActivity)
                            .subscribeOn(Schedulers.computation())
                            .delay(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s1 -> {
                              if (getContext() != null) startActivity(s1);
                            }, throwable -> {

                            });
                      } else {
                        Timber.e(qcResponse.getMsg());
                      }
                    }
                  }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                      Timber.e(throwable.getMessage());
                    }
                  });
            } else {
              Toast.makeText(getContext(), "没有场馆信息", Toast.LENGTH_SHORT).show();
            }
          } else {
            if (s.toLowerCase().startsWith("http")) {
              WebActivity.startWeb(s, getContext());
            } else {

              Uri uri = Uri.parse(s);
              Intent tosb = new Intent(Intent.ACTION_VIEW, uri);
              tosb.setPackage(getContext().getPackageName());
              if (uri.getQueryParameterNames() != null) {
                for (String key : uri.getQueryParameterNames()) {
                  tosb.putExtra(key, uri.getQueryParameter(key));
                }
              }
              tosb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(tosb);
            }
          }
        } catch (Exception e) {
          Timber.e("notification: " + e.getMessage());
          WebActivity.startWeb(s, getActivity());
        }
      }
    }
    return true;
  }

  private NotificationMsg curMsg = null;

  @Override public void onRefresh(List<NotificationMsg> data, int unReadCount) {
    rv.stopLoading();
    mData.clear();
    this.unReadCount = 0;
    int mfirstUnread = -1;
    if (data == null || data.size() == 0) {
      mData.add(new CommonNoDataItem(R.drawable.no_notification, "暂无通知"));
    } else {
      this.unReadCount = unReadCount;
      List<Long> readMsg = new ArrayList<>();
      for (int i = 0; i < data.size(); i++) {
        NotificationMsg msg = data.get(i);
        if (!msg.is_read() && MSG_TYPES.contains(msg.getType())) {
          readMsg.add(msg.getId());
        }
        mData.add(new NotificationItem(data.get(i)));
        if (!data.get(i).is_read()) {
          if (mfirstUnread < 0) mfirstUnread = i;
        }
      }
    }
    //        mAdatper.clear();
    mAdatper.updateDataSet(mData, true);
    //        mAdatper.notifyDataSetChanged();

    try {
      if (mfirstUnread > 0) {
        RxBus.getBus()
            .post(new EventLatestNoti(
                DateUtils.formatDateFromServer(data.get(mfirstUnread).getCreated_at()).getTime(),
                Integer.parseInt(type)));
      } else {
        RxBus.getBus().post(new EventLatestNoti(0l, Integer.parseInt(type)));
      }
    } catch (Exception e) {

    }
  }

  @Override public void onLoadmore(List<NotificationMsg> data, int unReadCount) {
    mAdatper.removeItem(mAdatper.getItemCount() - 1);
    if (data == null) return;
    List<Long> readMsg = new ArrayList<>();
    List<AbstractFlexibleItem> newItem = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      NotificationMsg msg = data.get(i);
      if (!msg.is_read() && MSG_TYPES.contains(msg.getType())) {
        readMsg.add(msg.getId());
      }
      newItem.add(new NotificationItem(data.get(i)));
    }

    this.unReadCount = unReadCount;
    mAdatper.onLoadMoreComplete(newItem);
  }

  @Override public void onClearPos(int pos) {
    unReadCount--;
    if (unReadCount < 1) RxBus.getBus().post(new EventLatestNoti(0l, getArguments().getInt("t")));
    if (mAdatper.getItem(pos) instanceof NotificationItem) {
      ((NotificationItem) mAdatper.getItem(pos)).getMsg().setIs_read(true);
      mAdatper.notifyItemChanged(pos);
    }
  }

  @Override public void checkUserPermission(boolean hasPermission) {
    hideLoading();
    if (hasPermission) {
      if (curMsg == null) return;
      final CoachService coachService1 =
          gymBaseInfoAction.getGymByShopIdNow(curMsg.getBrand_id(), curMsg.getShop_id());
      Intent toActivity = new Intent(getActivity(), StudentsDetailActivity.class);
      toActivity.putExtra("status_to_tab", 1);
      toActivity.putExtra("qcstudent", new QcStudentBean(curMsg.getUser()));
      toActivity.putExtra(Configs.EXTRA_GYM_SERVICE, coachService1);
      toActivity.putExtra(Configs.EXTRA_GYM_STATUS,
          new GymStatus.Builder().isGuide(false).isSingle(false).build());
      toActivity.putExtra(Configs.EXTRA_BRAND,
          new Brand.Builder().id(coachService1.brand_id()).build());
      Single.just(toActivity)
          .subscribeOn(Schedulers.computation())
          .delay(2000, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(s1 -> {
            if (getContext() != null) startActivity(s1);
          }, throwable -> {

          });
    } else {
      DialogUtils.showAlert(getContext(), "您没有查看该会员详情的权限", null);
    }
  }

  @Override public void onClearOk() {
    refresh();
  }

  @Override public void onShowError(String e) {
    hideLoading();
    rv.stopLoading();
    show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }

  @Override public void noMoreLoad(int i) {

  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.loadMore(App.staffId, ConstantNotification.getCategloreStr(getContext(), type));
  }
}
