package cn.qingchengfit.saasbase.student.views.followup;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.views.fragments.FilterFragment;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FilterListStringFragment extends FilterFragment {
    private String[] datas;

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
