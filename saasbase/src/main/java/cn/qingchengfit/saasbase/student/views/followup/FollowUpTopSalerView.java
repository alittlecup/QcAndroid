package cn.qingchengfit.saasbase.student.views.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.student.items.ChooseSalerItem;
import cn.qingchengfit.saasbase.student.items.ChooseStaffItem;
import cn.qingchengfit.saasbase.student.items.FollowUpChooseStaffitem;
import cn.qingchengfit.saasbase.student.other.BaseGirdListFragment;
import cn.qingchengfit.saasbase.student.presenters.followup.FollowUpSalersPresenter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FollowUpTopSalerView extends BaseGirdListFragment implements
        FollowUpSalersPresenter.MVPView, FlexibleAdapter.OnItemClickListener {
    @Inject
    FollowUpSalersPresenter presenter;
    @Inject
    LoginStatus loginStatus;

    @Override
    public void onSalers(List<Staff> sellers) {
        Staff staff1 = new Staff();
        staff1.id = "0";
        staff1.username = "未分配销售";
        sellers.add(0, staff1);
        Staff staff = new Staff();
        staff.id = "-1";
        staff.username = "全部";
        sellers.add(0, staff);
        setStaffs(sellers);
        commonFlexAdapter.addSelection(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegatePresenter(presenter, this);
        presenter.getFilterSelers(loginStatus.staff_id());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        commonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
        commonFlexAdapter.addListener(this);

    }

    @Override
    public AbstractFlexibleItem generateItem(Staff item) {
        return new ChooseSalerItem(item);
    }

    @Override
    public boolean onItemClick(int i) {
        int tmp = commonFlexAdapter.getSelectedItemCount() > 0 ? commonFlexAdapter.getSelectedPositions().get(0) : -1;
        if (commonFlexAdapter.isSelected(i)) {
            commonFlexAdapter.clearSelection();
        } else {
            commonFlexAdapter.toggleSelection(i);
        }
        commonFlexAdapter.notifyItemChanged(i);
        commonFlexAdapter.notifyItemChanged(tmp);
        // REFACTOR: 2017/11/20 adapter选中效果有延迟
        if (commonFlexAdapter.getItem(i) instanceof ChooseSalerItem) {
            if (commonFlexAdapter.isSelected(i)) {
                Staff saler = ((ChooseSalerItem) datas.get(i)).getSaler();
                itemClick.call(saler.id.equals("-1") ? null : saler);
            }
        }
        return true;
    }

    private Action1<Staff> itemClick;

    public void setOnItemClick(Action1<Staff> action1) {
        this.itemClick = action1;
    }
}
