package com.qingchengfit.fitcoach.fragment.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.activity.BrandManageActivity;
import com.qingchengfit.fitcoach.activity.ChooseBrandActivity;
import com.qingchengfit.fitcoach.activity.GuideActivity;
import com.qingchengfit.fitcoach.activity.PopFromBottomActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.event.EventChooseGym;
import com.qingchengfit.fitcoach.event.EventClickManageBrand;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
import com.qingchengfit.fitcoach.items.AddCardStyleItem;
import com.qingchengfit.fitcoach.items.BrandShopsItem;
import com.qingchengfit.fitcoach.items.ChosenGymItem;
import com.qingchengfit.fitcoach.items.GymItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 16/11/16.
 */
@FragmentWithArgs public class ChooseGymFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @Arg CoachService mCoachService;

    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    @Inject GymWrapper gymWrapper;
    List<Brand> brands = new ArrayList<>();
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    private CommonFlexAdapter mAdapter;
    private Unbinder unbinder;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_gym, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof PopFromBottomActivity) {
            getActivity().setTitle("选择健身房");
        }

        mDatas.clear();
        //mDatas.add(new AddBatchCircleItem("+ 添加健身房"));
        mAdapter = new CommonFlexAdapter(mDatas, this);
        mAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setAdapter(mAdapter);
        /**
         * 点击健身房
         */
        RxBusAdd(EventChooseGym.class).subscribe(eventChooseGym -> {
            gymWrapper.setCoachService(eventChooseGym.getCoachService());
            getActivity().onBackPressed();
        });
        /**
         * 管理品牌
         */
        RxBusAdd(EventClickManageBrand.class).subscribe(eventClickManageBrand -> {
            Intent toBrand = new Intent(getContext(), BrandManageActivity.class);
            toBrand.putExtra("brand", eventClickManageBrand.getBrand());
            startActivity(toBrand);
        });
        refresh();
        return view;
    }

    public void refresh() {
        showLoading();
        RxRegiste(QcCloudClient.getApi().getApi.qcGetTrainerBrands(App.coachid + "")
            .flatMap(new Func1<QcResponseBrands, Observable<QcCoachServiceResponse>>() {
                @Override public Observable<QcCoachServiceResponse> call(QcResponseBrands qcResponseBrands) {
                    hideLoading();
                    brands.clear();
                    brands.addAll(qcResponseBrands.data.brands);
                    return QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                }
            })
            .map(qcCoachServiceResponse -> {
                List<List<CoachService>> ls = new ArrayList<List<CoachService>>();
                for (int i = 0; i < brands.size(); i++) {
                    List<CoachService> coachServices = new ArrayList<CoachService>();
                    if (qcCoachServiceResponse.data.services != null) {
                        for (int j = 0; j < qcCoachServiceResponse.data.services.size(); j++) {
                            if (brands.get(i).getId().equalsIgnoreCase(qcCoachServiceResponse.data.services.get(j).brand_id)) {
                                coachServices.add(qcCoachServiceResponse.data.services.get(j));
                            }
                        }
                    }
                    ls.add(coachServices);
                }
                return ls;
            }).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<List<CoachService>>>() {
                @Override public void call(List<List<CoachService>> lists) {

                    mDatas.clear();
                    for (int i = 0; i < brands.size(); i++) {
                        Brand b = brands.get(i);
                        List<ChosenGymItem> ds = new ArrayList<ChosenGymItem>();
                        if (lists.get(i).size() > 0) {
                            for (int j = 0; j < lists.get(i).size(); j++) {
                                ds.add(new ChosenGymItem(lists.get(i).get(j)));
                            }
                        }
                        mDatas.add(new BrandShopsItem(b, ds));
                    }
                    mDatas.add(new AddCardStyleItem("新建健身房"));
                    mAdapter.notifyDataSetChanged();
                }
            }, throwable -> hideLoading()));
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof GymItem) {
            gymWrapper.setCoachService(((GymItem) mAdapter.getItem(position)).coachService);
            getActivity().onBackPressed();
        } else if (mAdapter.getItem(position) instanceof AddCardStyleItem) {
            Intent goBrands = new Intent(getActivity(), ChooseBrandActivity.class);
            startActivityForResult(goBrands, 1);
        }
        return true;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                if (brand != null) {
                    Intent guide = new Intent(getActivity(), GuideActivity.class);
                    guide.putExtra("brand", brand);
                    startActivity(guide);
                }
            }
        }
    }
}
