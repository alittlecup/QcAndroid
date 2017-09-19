package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 2017/3/14.
 */

public class FlexableListFragment extends BaseFragment {
    @DrawableRes public int customNoImage;
    public String customNoStr;
    public String customNoStrTitle;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private CommonFlexAdapter mFlexAdapter;
    private FlexibleAdapter.OnItemClickListener mItemClickListener;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recycleview = new RecyclerView(getContext());
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        if (mFlexAdapter == null) mFlexAdapter = new CommonFlexAdapter(mData, mItemClickListener);
        if (mData.size() == 0) mData.add(new CommonNoDataItem(customNoImage, customNoStr, customNoStrTitle));

        recycleview.setAdapter(mFlexAdapter);
        return recycleview;
    }

    @Override protected void onVisible() {
        super.onVisible();
        freshView();
    }

    public void sort(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            mFlexAdapter.setSearchText(null);
        } else {
            List<AbstractFlexibleItem> copy = new ArrayList<>();
            copy.addAll(mData);
            if (mFlexAdapter != null) {
                mFlexAdapter.setSearchText(keyword.trim());
                mFlexAdapter.filterItems(copy);
            }
        }
    }

    public void freshView() {
        if (getContext() != null && mFlexAdapter != null) {
            mFlexAdapter.notifyDataSetChanged();
        }
    }

    public String getFragmentName() {
        return FlexableListFragment.class.getName();
    }

    public void setData(@NonNull List<AbstractFlexibleItem> ds) {
        mData.clear();
        mData.addAll(ds);

        if (mData.size() == 0) {
            mData.add(new CommonNoDataItem(customNoImage, customNoStr, customNoStrTitle));
        }
        if (mFlexAdapter != null && recycleview != null) mFlexAdapter.notifyDataSetChanged();
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

    public void setFilterString(String filter) {
        if (mFlexAdapter != null) mFlexAdapter.setTag("filter", filter);
    }
}
