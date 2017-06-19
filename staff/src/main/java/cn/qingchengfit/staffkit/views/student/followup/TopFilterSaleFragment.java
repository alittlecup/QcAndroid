package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/7.
 */
public class TopFilterSaleFragment extends BaseFragment
    implements TopFilterSalePresenter.PresenterView, View.OnTouchListener, FlexibleAdapter.OnItemClickListener {

    public int page = 0;
    @BindView(R.id.rv_referrer) RecyclerView rvSales;
    @Inject TopFilterSalePresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    private CommonFlexAdapter mFlexAdapter;
    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private List<Staff> datas = new ArrayList<>();
    private int choosePos = 0;
    private boolean isShowShort;

    @Inject public TopFilterSaleFragment() {
    }

    public boolean isShowShort() {
        return isShowShort;
    }

    public void setShowShort(boolean showShort) {
        isShowShort = showShort;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referrer_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        initDI();
        if (getParentFragment() != null && getParentFragment() instanceof StudentFilterFragment) {
            isShowShort = true;
        }
        initView();
        presenter.getFilterSelers();
        view.setOnTouchListener(this);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarLayout.setVisibility(View.GONE);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setOnTouchListener(this);
    }

    private void initDI() {

        delegatePresenter(presenter, this);
    }

    private void initView() {
        mFlexAdapter = new CommonFlexAdapter(items, this);
        /*
         * 在侧滑中全是单选
         */
        if (getParentFragment() != null && getParentFragment() instanceof StudentFilterFragment) {
            mFlexAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        } else {
            mFlexAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        }

        mFlexAdapter.setAnimationOnScrolling(true)
            .setAnimationInitialDelay(50L)
            .setAnimationInterpolator(new DecelerateInterpolator())
            .setAnimationDelay(100L)
            .setAnimationStartPosition(0);

        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                return 1;
            }
        });

        rvSales.setItemViewCacheSize(4);
        rvSales.setLayoutManager(gridLayoutManager);
        rvSales.addItemDecoration(new SpaceItemDecoration(20, getActivity()));
        rvSales.setNestedScrollingEnabled(false);
        rvSales.setAdapter(mFlexAdapter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override public void onSalers(List<Staff> salers) {
        if (salers == null) salers = new ArrayList<>();
        Staff staff;
        datas.clear();
        items.clear();

        datas.addAll(salers);

        staff = new Staff();
        staff.id = "0";
        staff.username = "未分配销售";
        datas.add(0, staff);
        staff = new Staff();
        staff.id = "-1";
        staff.username = "全部";
        datas.add(0, staff);

        items.clear();
        if (isShowShort) {
            for (int i = 0; i < 8; i++) {
                if (datas.size() > i) items.add(new ChooseSalerItem(datas.get(i)));
            }
        } else {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.size() > i) items.add(new ChooseSalerItem(datas.get(i)));
            }
        }

        mFlexAdapter.addSelection(0);
        mFlexAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {

    }

    public Staff getChoosenSaler() {
        if (mFlexAdapter.getSelectedItemCount() > 0) {
            if (((ChooseSalerItem) mFlexAdapter.getItem(mFlexAdapter.getSelectedPositions().get(0))).getSaler().id.equals("-1")) {
                return null;
            } else {
                return ((ChooseSalerItem) mFlexAdapter.getItem(mFlexAdapter.getSelectedPositions().get(0))).getSaler();
            }
        } else {
            return null;
        }
    }

    public void selectSaler(String salerid) {
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).id.equals(salerid)) onItemClick(i);
            }
        }
    }

    /**
     * show all click
     */
    public void showAllClick() {
        isShowShort = !isShowShort;
        items.clear();
        if (!isShowShort) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.size() > i) items.add(new ChooseSalerItem(datas.get(i)));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (datas.size() > i) items.add(new ChooseSalerItem(datas.get(i)));
            }
        }
        mFlexAdapter.notifyDataSetChanged();
    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override public boolean onItemClick(int i) {
        int tmp = mFlexAdapter.getSelectedItemCount() > 0 ? mFlexAdapter.getSelectedPositions().get(0) : -1;
        if (mFlexAdapter.isSelected(i)) {
            mFlexAdapter.clearSelection();
        } else {
            mFlexAdapter.toggleSelection(i);
        }
        mFlexAdapter.notifyItemChanged(i);
        mFlexAdapter.notifyItemChanged(tmp);
        if (mFlexAdapter.getItem(i) instanceof ChooseSalerItem) {
            if (mFlexAdapter.isSelected(i)) {
                if (getParentFragment() != null && getParentFragment() instanceof StudentFilterFragment) {

                } else {
                    StudentFilter filter = new StudentFilter();
                    filter.sale = ((ChooseSalerItem) mFlexAdapter.getItem(i)).getSaler();
                    if (filter.sale.id.equals("-1")) {
                        filter.sale = null;
                    }
                    FollowUpFilterEvent e = new FollowUpFilterEvent.Builder().withEventType(FollowUpFilterEvent.EVENT_SALE_ITEM_CLICK)
                        .withPosition(i)
                        .withFilter(filter)
                        .build();
                    e.page = page;
                    RxBus.getBus().post(e);
                }
            }
        }
        return true;
    }
}
