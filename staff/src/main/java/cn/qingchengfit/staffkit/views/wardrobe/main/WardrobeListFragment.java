package cn.qingchengfit.staffkit.views.wardrobe.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.wardrobe.back.WardrobeReturnDialog;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeDetailFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.WardrobeItem;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

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
 * Created by Paper on 16/8/30.
 */
public class WardrobeListFragment extends BaseFragment implements TitleFragment, FlexibleAdapter.OnItemClickListener {
    public static final int RESULT_STATUS = 1;

    @BindView(R.id.free_count) TextView freeCount;
    @BindView(R.id.rv) RecyclerView rv;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    CommonFlexAdapter mAdatper;
    @BindView(R.id.filter) TextView filter;
    private int count = 0;
    private List<Locker> mAllLocker = new ArrayList<>();

    public static WardrobeListFragment newInstance(List<Locker> l) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("l", (ArrayList<Locker>) l);
        WardrobeListFragment fragment = new WardrobeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WardrobeListFragment newInstance(List<Locker> l, LockerRegion lockerRegion) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("l", (ArrayList<Locker>) l);
        args.putParcelable("lr", lockerRegion);
        WardrobeListFragment fragment = new WardrobeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdatper = new CommonFlexAdapter(mData, this);
        //        }
        rv.setHasFixedSize(true);
        SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (mAdatper.getItem(position).getLayoutRes() == R.layout.item_common_no_data) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdatper);
        //        freshData(lockers);
        filter.setVisibility(View.VISIBLE);
        filter.setText(getResources().getStringArray(R.array.wardrobe_status)[0]);
        fresh();
        return view;
    }

    public void freshData(List<Locker> lockers) {
        mAllLocker = lockers;
        mData.clear();
        count = 0;
        if (lockers != null) {
            for (int i = 0; i < lockers.size(); i++) {
                mData.add(new WardrobeItem(lockers.get(i)));
                //                if (!lockers.get(i).is_used)
                count++;
            }
        }
        if (mData.size() == 0) mData.add(new CommonNoDataItem(R.drawable.no_wardrobe, "暂无更衣柜"));
        if (freeCount != null) freeCount.setText(count + "");
        if (mAdatper != null) mAdatper.updateDataSet(mData);
    }

    public void fresh() {
        try {
            if (getParentFragment() instanceof WardrobeMainFragment) {
                LockerRegion lr = getArguments().getParcelable("lr");
                List<Locker> ls = ((WardrobeMainFragment) getParentFragment()).mLockers.get(lr.id);
                freshData(ls);
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

    private void filterData(int status) {
        if (mAllLocker != null) {
            mData.clear();
            for (int i = 0; i < mAllLocker.size(); i++) {
                Locker l = mAllLocker.get(i);
                if (status == 0 || (status == 1 && !l.is_long_term_borrow && l.is_used) || (status == 2
                    && l.is_long_term_borrow
                    && l.is_used) || (status == 3 && !l.is_used)) {
                    mData.add(new WardrobeItem(l));
                }
            }
        }
        mAdatper.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return WardrobeListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.filter) public void onFilter() {
        BottomSheetListDialogFragment.start(this, RESULT_STATUS, getResources().getStringArray(R.array.wardrobe_status));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_STATUS) {
                try {
                    int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                    filter.setText(getResources().getStringArray(R.array.wardrobe_status)[pos]);
                    filterData(pos);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override public String getTitle() {
        try {
            LockerRegion lr = getArguments().getParcelable("lr");
            if (lr != null) {
                return lr.name;
            }

            ArrayList<Locker> lockers = getArguments().getParcelableArrayList("l");
            if (lockers != null && lockers.size() > 0) {
                return lockers.get(0).region.name;
            } else {
                return "无";
            }
        } catch (Exception e) {
            Timber.e(e, "wardrobe");
            return "无";
        }
    }

    public Long getRegionId() {
        try {
            LockerRegion lr = getArguments().getParcelable("lr");
            if (lr != null) {
                return lr.id;
            }
            ArrayList<Locker> lockers = getArguments().getParcelableArrayList("l");
            if (lockers != null && lockers.size() > 0) {
                return lockers.get(0).region.id;
            } else {
                return 0L;
            }
        } catch (Exception e) {
            Timber.e(e, "wardrobe");
            return 0L;
        }
    }

    @Override public boolean onItemClick(int position) {
        if (mAdatper.getItem(position) instanceof WardrobeItem) {
            Locker locker = ((WardrobeItem) mAdatper.getItem(position)).getLocker();
            if (locker.is_used) {
                WardrobeReturnDialog dialog = WardrobeReturnDialog.newInstance(locker);
                dialog.show(getFragmentManager(), "");
            } else {
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .replace(mCallbackActivity.getFragId(), WardrobeDetailFragment.newInstance(locker))
                    .addToBackStack(WardrobeMainFragment.class.getName())
                    .commit();
            }
        }
        return true;
    }
}
