package cn.qingchengfit.saasbase.student.views.allot;

import cn.qingchengfit.saasbase.student.items.AllotCoachItem;
import cn.qingchengfit.saasbase.student.items.AllotStaffItem;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by huangbaole on 2017/10/27.
 */

public class AllotCoachListFragment extends AllotListFragment {
    @Override
    public AbstractFlexibleItem generateItem(AllotDataResponse item) {
        return new AllotCoachItem(item);
    }
}
