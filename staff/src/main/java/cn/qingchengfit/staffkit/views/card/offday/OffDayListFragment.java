package cn.qingchengfit.staffkit.views.card.offday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.OffDay;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class OffDayListFragment extends BaseFragment implements OffDayListView {

    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;

    @Inject OffDayListPresenter presenter;

    OffDayListAdapter adapter;
    List<OffDay> datas = new ArrayList<>();

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycleview_with_nodata, container, false);
        unbinder = ButterKnife.bind(this, view);

        mCallbackActivity.setToolbar("请假记录", false, null, R.menu.menu_add, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), new AddOffDayFragment())
                    .addToBackStack(null)
                    .commit();
                return true;
            }
        });

        presenter.attachView(this);
        adapter = new OffDayListAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData();
            }
        });
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, final int pos) {
                if (v.getId() == R.id.cancel_offday) {
                    if (!datas.get(pos).cancel) {
                        //删除请假
                        DialogUtils.instanceDelDialog(getContext(), "是否删除请假?", new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                presenter.cancleOffDay(datas.get(pos).id);
                            }
                        }).show();
                    }
                } else {
                    getFragmentManager().beginTransaction()
                        .
                            setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out)
                        .add(mCallbackActivity.getFragId(), new AheadOffDayFragmentBuilder(datas.get(pos).id).build())
                        .commitAllowingStateLoss();
                }
            }
        });

        presenter.queryData();
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
        recyclerview.setNoData(datas.size() == 0);
    }

    @Override public void onSucceess() {
        presenter.queryData();
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public String getFragmentName() {
        return OffDayListFragment.class.getName();
    }
}
