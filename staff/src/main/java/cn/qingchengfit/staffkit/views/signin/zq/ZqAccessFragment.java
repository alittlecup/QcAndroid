package cn.qingchengfit.staffkit.views.signin.zq;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.staffkit.views.signin.zq.event.EventAddZq;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;
import cn.qingchengfit.staffkit.views.signin.zq.model.BottomModel;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import cn.qingchengfit.staffkit.views.signin.zq.presenter.ZqAccessPresenter;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions.RxPermissions;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/14.
 */

public class ZqAccessFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, ZqAccessPresenter.MVPView,
    BottomListFragment.ComfirmChooseListener {

  public static final String IMG_URL =
      "http://zoneke-img.b0.upaiyun.com/0ffa48dbbbb23b74c4d936a51dd65d60.jpg";

  public static final String INTRO_URL =
      "https://mp.weixin.qq.com/s/Rz4jRg2NyJG5AEDxC1M41A";

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tv_zq_introduce) TextView tvZqIntroduce;
  @BindView(R.id.tv_zq_buy) TextView tvZqBuy;
  @BindView(R.id.recycler_zq_access) RecyclerView recyclerZqAccess;

  @Inject ZqAccessPresenter presenter;
  @BindView(R.id.img_access) ImageView imgAccess;

  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private BottomListFragment bottomListFragment;
  private List<BottomModel> bottomList = new ArrayList<>();
  private BottomModel bottomModel;
  private Guard guard;
  private int position;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_zq_access, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    bottomListFragment = BottomListFragment.newInstance("");
    setToolbar();
    initData();
    initBus();
    return view;
  }

  private void initData() {
    presenter.getAccess();
    Glide.with(getContext()).load(IMG_URL).asBitmap().into(imgAccess);
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerZqAccess.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerZqAccess.addItemDecoration(new FlexibleItemDecoration(getContext()).withTopEdge(true)
        .withBottomEdge(true)
        .withDefaultDivider());
    recyclerZqAccess.setAdapter(adapter);
  }

  private void initBus(){
    RxBusAdd(EventAddZq.class).onBackpressureLatest().subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe(new Action1<EventAddZq>() {
      @Override public void call(EventAddZq eventAddZq) {
        presenter.getAccess();
      }
    });
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText("智奇门禁");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        ContainerActivity.router(GymFunctionFactory.ADD_ZQ_ACCESS, getContext());
        return false;
      }
    });
  }

  @OnClick(R.id.tv_zq_introduce)
  public void onIntro(){
    WebActivity.startWeb(INTRO_URL, getContext());
  }

  @OnClick(R.id.tv_zq_buy)
  public void onBuy(){
    DialogUtils.instanceDelDialog(getContext(), getResources().getString(R.string.dialog_buy_access_zq),
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            new RxPermissions(getActivity()).request(Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Boolean>() {
                  @Override public void call(Boolean aBoolean) {
                    if (aBoolean) {
                      Uri uri = Uri.parse(new StringBuilder().append("tel:")
                          .append(getResources().getString(R.string.bug_access_phone))
                          .toString());
                      Intent dialntent = new Intent(Intent.ACTION_DIAL, uri);
                      dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(dialntent);
                    } else {
                      ToastUtils.show("请在应用设置中开启拨打电话权限");
                    }
                  }
                });
          }
        }).show();
  }

  //bottomList 用于区分不同情况下的bottom弹出菜单
  private void initBottomList(int status) {
    List<AbstractFlexibleItem> bottomItems = new ArrayList<>();
    if (presenter.getBottomList(getContext(), status).size() > 0) {
      bottomList.clear();
      bottomList.addAll(presenter.getBottomList(getContext(), status));
    }
    for (BottomModel content : bottomList) {
      bottomItems.add(new SimpleTextItemItem(content.name, Gravity.CENTER));
    }
    BottomListFragment bottomListFragment = BottomListFragment.newInstance("");
    bottomListFragment.setListener(this);
    bottomListFragment.loadData(bottomItems);
    bottomListFragment.show(getFragmentManager(), null);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    this.position = position;
    if (adapter.getItem(position) instanceof ItemZqAccess) {
      guard = ((ItemZqAccess) adapter.getItem(position)).getData();
      initBottomList(((ItemZqAccess) adapter.getItem(position)).getData().status);
    }
    return false;
  }

  @Override public void onGetAccess(List<Guard> guardList) {
    if (guardList != null && guardList.size() > 0) {
      itemList.clear();
    }
    for (Guard guard : guardList) {
      itemList.add(new ItemZqAccess(guard));
    }
    adapter.notifyDataSetChanged();
  }

  @Override public void changeStatusOk() {
    presenter.getAccess();
  }

  @Override public void onDeleteOk() {
    itemList.remove(position);
    adapter.notifyItemRemoved(position);
  }

  @Override public void onAddOk() {
  }

  @Override public void onEditOk() {
    presenter.getAccess();
  }

  //按位置操作 0 & 1 不确定（根据当前状态）  2编辑门禁  3删除门禁
  private void changeAccessStatus(int position) {
    BottomModel model = bottomList.get(position);
    switch (position) {
      case 0:
      case 1:
        presenter.changeZqStatus(guard.id, model.status);
        break;
      case 2:
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("access", createBody());
        b.putString("guard", guard.id);
        intent.putExtra("data", b);
        intent.putExtra("router", GymFunctionFactory.EDIT_ZQ_ACCESS);
        startActivity(intent);
        break;
      case 3:
        onDeleteAccess();
        break;
      case 4:
        break;
    }
  }

  private void onDeleteAccess(){
    DialogUtils.instanceDelDialog(getContext(),
        getResources().getString(R.string.dialog_delete_access, guard.name), new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (which == DialogAction.POSITIVE) {
              presenter.deleteZqAccess(guard.id);
            }
          }
        }).show();
  }

  private AccessBody createBody() {
    AccessBody body = new AccessBody();
    body.name = guard.name;
    body.device_id = guard.device_id;
    body.behavior = guard.behavior;
    body.start = guard.start;
    body.end = guard.end;
    return body;
  }

  @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
    if (bottomList.size() > 0) {
      changeAccessStatus(selectedPos.get(0));
    }
  }
}
