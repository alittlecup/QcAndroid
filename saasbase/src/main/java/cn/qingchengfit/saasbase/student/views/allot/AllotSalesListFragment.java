package cn.qingchengfit.saasbase.student.views.allot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.saasbase.student.items.AllotStaffItem;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by huangbaole on 2017/10/27.
 */
public class AllotSalesListFragment extends AllotListFragment {


    @Override
    public AbstractFlexibleItem generateItem(AllotDataResponse item) {
        return new AllotStaffItem(item);
    }

}
