package cn.qingchengfit.saasbase.student.views.allot;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentAllotListBinding;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.items.AllotStaffItem;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/10/27.
 */
@Leaf(module = "student",path = "/sales/list")
public class AllotSellerFragment extends SaasBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.OnItemClickListener{
    FragmentAllotListBinding binding;

    AllotListFragment listFragment;
    @Inject
    SaasbaseRouterCenter saasbaseRouterCenter;
    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_allot_list, container, false);
        initFragment();
        ToolbarModel model=new ToolbarModel("销售列表");
        initToolbar(binding.includeToolbar.toolbar);
        binding.setToolbarModel(model);
        loadData();
        return binding.getRoot();
    }
    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }
    private void loadData() {

        RxRegiste(
                studentModel.qcGetAllotSalesPreView("7505", gymWrapper.getParams())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(this::onLoadFinish)
                        .subscribe(allotDataResponseWrapQcDataResponse -> {
                            if (allotDataResponseWrapQcDataResponse.status == 200) {
                                onDataList(allotDataResponseWrapQcDataResponse.getData().sellers);
                            } else {
                                onShowError(allotDataResponseWrapQcDataResponse.getMsg());
                            }
                        }, throwable -> onShowError(throwable.getMessage()))
        );
    }

    private void initFragment() {
        listFragment = new AllotSalesListFragment();
        stuff(R.id.frag_allot_list_container, listFragment);
        listFragment.initListener(this);
    }

    @Override
    public void onRefresh() {
        loadData();
    }



    private void onDataList(List<AllotDataResponse> datas) {
        listFragment.setAllotDatas(datas);
    }

    public void onLoadFinish() {
        listFragment.stopRefresh();
    }

    @Override
    public boolean onItemClick(int i) {
        if(listFragment.getDatas().get(i) instanceof AllotStaffItem){
            Uri uri=Uri.parse("qcstaff://student/sale/detail");
            routeTo(uri,new SaleDetailParams().staff(((AllotStaffItem) listFragment.getDatas().get(i)).data.getSeller()).build());
        }
        return false;
    }
}
