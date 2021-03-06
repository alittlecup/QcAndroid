package com.qingchengfit.fitcoach;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/2/22.
 */
public class FlexableListFragment extends BaseFragment {

    @DrawableRes public int customNoImage;
    public String customNoStr;
	RecyclerView recycleview;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private CommonFlexAdapter mFlexAdapter;
    private FlexibleAdapter.OnItemClickListener mItemClickListener;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flexable_list, container, false);
      recycleview = (RecyclerView) view.findViewById(R.id.recycleview);

      recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mFlexAdapter = new CommonFlexAdapter(mData, mItemClickListener);
        recycleview.setAdapter(mFlexAdapter);
        return view;
    }

    @Override protected void onVisible() {
        super.onVisible();
        freshView();
    }

    public void freshView() {
        if (getContext() != null && mFlexAdapter != null) {
            mFlexAdapter.updateDataSet(mData);
        }
    }

    @Override public boolean isBlockTouch() {
        return false;
    }

    public String getFragmentName() {
        return FlexableListFragment.class.getName();
    }

    public void setData(@NonNull List<AbstractFlexibleItem> ds) {
        mData.clear();
        mData.addAll(ds);

        if (mData.size() == 0) {
            mData.add(new CommonNoDataItem(customNoImage, customNoStr));
        }
    }

    public FlexibleAdapter.OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    /***
     * 必须要在setData之前调用
     * @param itemClickListener
     */
    public void setItemClickListener(FlexibleAdapter.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
