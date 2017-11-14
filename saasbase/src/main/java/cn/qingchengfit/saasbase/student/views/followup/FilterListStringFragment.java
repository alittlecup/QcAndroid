package cn.qingchengfit.saasbase.student.views.followup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.views.fragments.FilterFragment;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FilterListStringFragment extends FilterFragment {
    private String[] datas;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlPopWindowCommon.addItemDecoration(
                new FlexibleItemDecoration(getContext())
                        .withDivider(cn.qingchengfit.widgets.R.drawable.divider_grey_left_margin)
                        .withBottomEdge(true));
    }

    public void setStrings(@NonNull String[] strings){
        List<FilterCommonLinearItem> items=new ArrayList<>(strings.length);
        datas=strings;
        for(String msg:strings){
            items.add(new FilterCommonLinearItem(msg));
        }
        setItemList(items);
    }
    public int getItemCount(){
        return commonFlexAdapter.getItemCount();
    }
    public IFlexible getDataAtPosition(int position){
        return commonFlexAdapter.getItem(position);
    }
}
