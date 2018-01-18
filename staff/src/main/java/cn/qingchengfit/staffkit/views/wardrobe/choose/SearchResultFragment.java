package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.statement.ContainerActivity;
import cn.qingchengfit.staffkit.views.wardrobe.back.WardrobeReturnDialog;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeDetailFragment;
import cn.qingchengfit.staffkit.views.wardrobe.item.LockerHeaderItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.WardrobeChooseItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.WardrobeItem;
import cn.qingchengfit.staffkit.views.wardrobe.main.WardrobeMainFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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
public class SearchResultFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.rv) RecycleViewWithNoImg rv;
    CommonFlexAdapter adatper;
    private List<AbstractFlexibleItem> mItems = new ArrayList<>();

    public static SearchResultFragment newInstance(List<Locker> l) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("l", (ArrayList<Locker>) l);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serach_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        ArrayList<Locker> lockers = getArguments().getParcelableArrayList("l");

        if (adatper != null) RxBus.getBus().post(new EventFresh());
        adatper = new CommonFlexAdapter(mItems, this);
        SmoothScrollGridLayoutManager manger = new SmoothScrollGridLayoutManager(getContext(), 2);
        rv.stopLoading();
        manger.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                switch (adatper.getItemViewType(position)) {
                    case R.layout.item_wardrobe:
                        return 1;
                    default:
                        return 2;
                }
            }
        });
        rv.setLayoutManager(manger);
        rv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                rv.stopLoading();
            }
        });

        if (lockers != null && lockers.size() > 0) {
            HashMap<Long, List<Locker>> datas = new HashMap<>();
            for (int i = 0; i < lockers.size(); i++) {
                Locker locker = lockers.get(i);
                if (datas.keySet().contains(locker.region.id)) {
                    datas.get(locker.region.id).add(locker);
                } else {
                    List<Locker> d = new ArrayList<>();
                    d.add(locker);
                    datas.put(locker.region.id, d);
                }
            }
            for (long lid : datas.keySet()) {
                List<Locker> ls = datas.get(lid);
                mItems.add(new LockerHeaderItem(ls.get(0).region));
                for (int j = 0; j < ls.size(); j++) {
                    if (getActivity() instanceof ChooseWardrobeActivity) {
                        mItems.add(new WardrobeChooseItem(ls.get(j)));
                    } else if (getActivity() instanceof ContainerActivity) mItems.add(new WardrobeItem(ls.get(j)));
                }
            }
        } else {
            mItems.add(new CommonNoDataItem(R.drawable.no_search_result, "未找到相关结果"));
        }
        rv.setAdapter(adatper);
        adatper.updateDataSet(mItems);
        return view;
    }

    public void updateItems(List<Locker> lockers){
        if (lockers.size() >= 0){
            mItems.clear();
        }
        if (lockers != null && lockers.size() > 0) {
            HashMap<Long, List<Locker>> datas = new HashMap<>();
            for (int i = 0; i < lockers.size(); i++) {
                Locker locker = lockers.get(i);
                if (datas.keySet().contains(locker.region.id)) {
                    datas.get(locker.region.id).add(locker);
                } else {
                    List<Locker> d = new ArrayList<>();
                    d.add(locker);
                    datas.put(locker.region.id, d);
                }
            }
            for (long lid : datas.keySet()) {
                List<Locker> ls = datas.get(lid);
                mItems.add(new LockerHeaderItem(ls.get(0).region));
                for (int j = 0; j < ls.size(); j++) {
                    if (getActivity() instanceof ChooseWardrobeActivity) {
                        mItems.add(new WardrobeChooseItem(ls.get(j)));
                    } else if (getActivity() instanceof ContainerActivity) mItems.add(new WardrobeItem(ls.get(j)));
                }
            }
        } else {
            mItems.add(new CommonNoDataItem(R.drawable.no_search_result, "未找到相关结果"));
        }
        adatper.updateDataSet(mItems);
    }

    @Override public String getFragmentName() {
        return SearchResultFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (adatper.getItem(position) instanceof WardrobeChooseItem

            ) {
            Locker locker = ((WardrobeChooseItem) adatper.getItem(position)).getLocker();
            if (locker != null && !locker.is_used) {
                AppUtils.hideKeyboard(getActivity());
                Intent result = new Intent();
                result.putExtra(ChooseWardrobeActivity.LOCKER, locker);
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
            }
        } else if (adatper.getItem(position) instanceof WardrobeItem) {
            AppUtils.hideKeyboard(getActivity());
            Locker locker = ((WardrobeItem) adatper.getItem(position)).getLocker();
            if (locker.is_used) {
                WardrobeReturnDialog dialog = WardrobeReturnDialog.newInstance(locker);
                dialog.show(getFragmentManager(), "");
            } else {
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), WardrobeDetailFragment.newInstance(locker))
                    .addToBackStack(WardrobeMainFragment.class.getName())
                    .commit();
            }
        }
        return true;
    }
}
