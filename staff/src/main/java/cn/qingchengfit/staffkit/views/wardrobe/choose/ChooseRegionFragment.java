package cn.qingchengfit.staffkit.views.wardrobe.choose;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.ListAddItem;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.model.responese.LockerRegions;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.wardrobe.add.DistrictAddFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/9/1.
 */
public class ChooseRegionFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

	RecyclerView rv;

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
	Toolbar toolbar;
	TextView toolbarTitile;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    public static ChooseRegionFragment newInstance() {
        Bundle args = new Bundle();
        ChooseRegionFragment fragment = new ChooseRegionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_list, container, false);
      rv = (RecyclerView) view.findViewById(R.id.rv);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);

      initToolbar(toolbar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        RxRegiste(restRepository.getGet_api()
            .qcGetAllRegion(App.staffId, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<LockerRegions>>() {
                @Override public void call(QcDataResponse<LockerRegions> qcResponseLocerRegion) {
                    if (ResponseConstant.checkSuccess(qcResponseLocerRegion)) {
                        onDistricList(qcResponseLocerRegion.data.locker_regions);
                    } else {
                        hideLoading();
                        ToastUtils.show(qcResponseLocerRegion.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    hideLoading();
                }
            }));
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("选择区域");
    }

    public void onDistricList(List<LockerRegion> regions) {
        mDatas.clear();

        if (regions == null) {

        } else {
            for (int i = 0; i < regions.size(); i++) {
                mDatas.add(new SimpleChooseItemItem(regions.get(i)));
            }
        }
        mDatas.add(new ListAddItem("添加新区域"));
        mAdapter = new CommonFlexAdapter(mDatas, this);
        rv.setAdapter(mAdapter);
    }

    @Override public String getFragmentName() {
        return ChooseRegionFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof SimpleChooseItemItem) {//选择区域
            getActivity().onBackPressed();
            RxBus.getBus().post(((SimpleChooseItemItem) mAdapter.getItem(position)).getLockerRegion());
        } else if (mAdapter.getItem(position) instanceof ListAddItem) {//增加区域
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), new DistrictAddFragment())
                .addToBackStack(getFragmentName())
                .commit();
        }
        return true;
    }
}
