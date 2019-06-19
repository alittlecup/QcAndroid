package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.model.responese.QcResponseRenewalHistory;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.gym.items.RenewalsHistoryItem;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by Paper on 2016/12/21.
 */
public class RenewalHistoryFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

    public List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    public CommonFlexAdapter mCommonFlexAdapter;
    public int mCurPage = 1;
    public int mMaxPage = 1;
	RecycleViewWithNoImg recycleview;
    @Inject StaffRespository mRestRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
	Toolbar toolbar;
	TextView toolbarTitile;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_renewal_history, container, false);
      recycleview = (RecycleViewWithNoImg) view.findViewById(R.id.recycleview);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitile.setText(R.string.gym_renewal_history);
        if (getActivity() instanceof GymActivity) {
            recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
            recycleview.setRefreshble(false);
            mCommonFlexAdapter = new CommonFlexAdapter(mDatas, this);
            mCommonFlexAdapter.setEndlessScrollListener(this, new ProgressItem(getContext()));
            recycleview.setNodataHint("暂无续费记录");
            recycleview.setNoDataImgRes(R.drawable.no_renewal_history);
            recycleview.setAdapter(mCommonFlexAdapter);
            loadData(mCurPage);
        }
        return view;
    }

    @Override public String getFragmentName() {
        return RenewalHistoryFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        return false;
    }

    public void onLoadMore() {
        if (mCurPage <= mMaxPage) {
            loadData(mCurPage);
        } else {
            if (mDatas.size() > 0 && mDatas.get(mDatas.size() - 1) instanceof ProgressItem) {
                mDatas.remove(mDatas.size() - 1);
            }
        }
    }

    public void loadData(int page) {
        HashMap<String, Object> p = gymWrapper.getParams();
        p.put("page", page + "");
        RxRegiste(mRestRepository.getStaffAllApi()
            .qcGetRenewalHistorys(App.staffId, p)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseRenewalHistory>() {
                @Override public void call(QcResponseRenewalHistory qcResponse) {
                    recycleview.stopLoading();
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.data != null && qcResponse.data.renews != null) {
                            if (mDatas.size() > 0 && mDatas.get(mDatas.size() - 1) instanceof ProgressItem) {
                                mDatas.remove(mDatas.size() - 1);
                            }
                            for (int i = 0; i < qcResponse.data.renews.size(); i++) {
                                mDatas.add(new RenewalsHistoryItem(qcResponse.data.renews.get(i), getContext()));
                            }
                            mCommonFlexAdapter.notifyDataSetChanged();
                            if (mDatas.size() == 0) {
                                recycleview.setNoData(true);
                            } else {
                                recycleview.setNoData(false);
                            }

                            mCurPage++;
                            mMaxPage = qcResponse.data.pages;
                        }
                    } else {
                        ToastUtils.show(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (recycleview != null) recycleview.stopLoading();

                    ToastUtils.show(throwable.getMessage());
                }
            }));
    }

    @Override public void noMoreLoad(int i) {

    }

    @Override public void onLoadMore(int i, int i1) {
        onLoadMore();
    }
}
