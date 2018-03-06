package cn.qingchengfit.saasbase.cards.views.offday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.OffDay;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 16/3/24 2016.
 */
@Leaf(module = "card", path = "/offday/list")
public class OffDayListFragment
    extends SaasBaseFragment implements OffDayListView {

  @BindView(R2.id.rv_offday_list) RecyclerView recyclerview;

  @Inject OffDayListPresenter presenter;
  @Inject IPermissionModel permissionModel;
  @Need Card card;

  OffDayListAdapter adapter;
  List<OffDay> datas = new ArrayList<>();
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_offday_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    toolbarTitle.setText("请假记录");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (!permissionModel.check(PermissionServerUtils.MANAGE_COSTS_CAN_DELETE)) {
          showAlert(R.string.alert_permission_forbid);
          return false;
        }
        routeTo(AppUtils.getRouterUri(getContext(), "card/add/offday"),
            new AddOffDayParams().cardId(card.getId()).build());
        return true;
      }
    });
    adapter = new OffDayListAdapter(datas);
    recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerview.setAdapter(adapter);

    adapter.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, final int pos) {
        if (!permissionModel.check(PermissionServerUtils.MANAGE_COSTS_CAN_DELETE)) {
          showAlert(R.string.alert_permission_forbid);
          return;
        }
        if (v.getId() == R.id.cancel_offday) {
          if (!datas.get(pos).cancel) {
            //删除请假
            DialogUtils.instanceDelDialog(getContext(), "是否删除请假?",
                new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    presenter.cancleOffDay(datas.get(pos).id);
                  }
                }).show();
          }
        } else {
          routeTo(AppUtils.getRouterUri(getContext(), "card/offday/ahead"),
              new AheadOffDayParams().card(card).offDayId(datas.get(pos).id).build());
        }
      }
    });

    presenter.queryData(card.getId());
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return false;
      }
    });
    return view;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }

  @Override public void onOffDayList(List<OffDay> offDays) {
    datas.clear();
    datas.addAll(offDays);
    adapter.notifyDataSetChanged();
  }

  @Override public void onSucceess() {
    presenter.queryData(card.getId());
  }

  @Override public void onFailed(String s) {
    ToastUtils.show(s);
  }

  @Override public String getFragmentName() {
    return OffDayListFragment.class.getName();
  }
}
