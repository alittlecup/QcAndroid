package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.widgets.AnimatedButton;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ChooseMainFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.strip) TabLayout strip;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.all_region) LinearLayout allRegion;
    @BindView(R.id.all_region_rv) RecyclerView allRegionRv;
    @BindView(R.id.btn_show_all) AnimatedButton btnShowAll;
    @BindView(R.id.layout_manage_district) FrameLayout layoutManageDistrict;
    ArrayList<Locker> lockers = new ArrayList<>();
    HashMap<Long, List<Locker>> mLockers = new HashMap<>();
    private FragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> chooseWardrobeListFragments = new ArrayList<>();
    private List<LockerRegion> regions = new ArrayList<>();
    private HashMap<Long, Integer> stripPos = new HashMap<>();
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();

    public static ChooseMainFragment newInstance(List<Locker> l, List<LockerRegion> lr) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("l", (ArrayList<Locker>) l);
        args.putParcelableArrayList("lr", (ArrayList<LockerRegion>) lr);
        ChooseMainFragment fragment = new ChooseMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardrobe_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        layoutManageDistrict.setVisibility(View.GONE);
        lockers = getArguments().getParcelableArrayList("l");
        regions = getArguments().getParcelableArrayList("lr");

        if (lockers != null && lockers.size() > 0) {

            for (int i = 0; i < regions.size(); i++) {
                List<Locker> ls = new ArrayList<>();
                for (int j = 0; j < lockers.size(); j++) {
                    Locker locker = lockers.get(j);
                    if (locker.region != null && locker.region.id.longValue() == regions.get(i).id) {
                        ls.add(locker);
                    }
                }
                mLockers.put(regions.get(i).id, ls);
                stripPos.put(regions.get(i).id, i);
                chooseWardrobeListFragments.add(ChooseWardrobeListFragment.newInstance(ls, regions.get(i)));
            }

            fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), chooseWardrobeListFragments);
            viewpager.setAdapter(fragmentAdapter);
            strip.setupWithViewPager(viewpager);
            viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(strip));

            for (int j = 0; j < regions.size(); j++) {
                mData.add(new SimpleChooseItemItem(regions.get(j)));
            }
            mAdapter = new CommonFlexAdapter(mData, this);
            allRegionRv.setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), 2));
            allRegionRv.setAdapter(mAdapter);
        }
        return view;
    }

    @OnClick(R.id.btn_show_all) public void onShowAll() {
        btnShowAll.toggle();
        if (allRegion.getVisibility() == View.VISIBLE) {
            allRegion.setVisibility(View.GONE);
        } else {
            allRegion.setVisibility(View.VISIBLE);
        }
    }

    @Override public String getFragmentName() {
        return ChooseMainFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {

        if (mAdapter.getItem(position) instanceof SimpleChooseItemItem) {
            onShowAll();
            int p = stripPos.get(((SimpleChooseItemItem) mAdapter.getItem(position)).getLockerRegion().id);
            viewpager.setCurrentItem(p, true);
            AppUtils.hideKeyboard(getActivity());
        }

        return true;
    }
}
