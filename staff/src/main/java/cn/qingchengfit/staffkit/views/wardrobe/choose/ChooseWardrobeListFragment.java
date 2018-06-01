package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.wardrobe.item.WardrobeChooseItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
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
 * Created by Paper on 16/8/30.
 */
public class ChooseWardrobeListFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, TitleFragment {

	TextView freeCount;
	RecyclerView rv;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    CommonFlexAdapter mAdatper;
	TextView countTitle;

    public static ChooseWardrobeListFragment newInstance(List<Locker> l, LockerRegion lr) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("l", (ArrayList<Locker>) l);
        args.putParcelable("lr", lr);
        ChooseWardrobeListFragment fragment = new ChooseWardrobeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_list, container, false);
      freeCount = (TextView) view.findViewById(R.id.free_count);
      rv = (RecyclerView) view.findViewById(R.id.rv);
      countTitle = (TextView) view.findViewById(R.id.count_title);

      mAdatper = new CommonFlexAdapter(mData, this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), 2));
        List<Locker> lockers = getArguments().getParcelableArrayList("l");

        mData.clear();
        int count = 0;
        if (lockers != null) {
            for (int i = 0; i < lockers.size(); i++) {
                mData.add(new WardrobeChooseItem(lockers.get(i)));
                if (!lockers.get(i).is_used) count++;
            }
        }
        countTitle.setText(getString(R.string.free_wardrobe));
        freeCount.setText("" + count);
        rv.setAdapter(mAdatper);
        mAdatper.updateDataSet(mData);
        return view;
    }

    @Override public String getFragmentName() {
        return ChooseWardrobeListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdatper.getItem(position) instanceof WardrobeChooseItem) {
            Locker locker = ((WardrobeChooseItem) mAdatper.getItem(position)).getLocker();
            if (locker != null && !locker.is_used) {
                Intent result = new Intent();
                result.putExtra(ChooseWardrobeActivity.LOCKER, locker);
                AppUtils.hideKeyboard(getActivity());
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
            }
        }
        return true;
    }

    @Override public String getTitle() {
        LockerRegion region = getArguments().getParcelable("lr");
        try {
            return region.name;
        } catch (Exception e) {
            return "无";
        }
    }
}
