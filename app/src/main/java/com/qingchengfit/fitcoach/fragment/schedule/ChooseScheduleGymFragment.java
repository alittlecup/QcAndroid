package com.qingchengfit.fitcoach.fragment.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseBrandActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.fragment.AddGymFragmentBuilder;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.items.AddBatchCircleItem;
import com.qingchengfit.fitcoach.items.GymItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
public class ChooseScheduleGymFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    protected List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    protected CommonFlexAdapter mAdapter;



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_gym, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitle.setText("选择健身房");

        mDatas.clear();
        //mDatas.add(new AddBatchCircleItem("+ 添加健身房"));
        mAdapter = new CommonFlexAdapter(mDatas, this);
        mAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(
            new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mAdapter);
        refresh();
        return view;
    }

    public void refresh() {
        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcCoachServiceResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcCoachServiceResponse qcCoachServiceResponse) {
                    if (qcCoachServiceResponse.status == 200) {
                        mDatas.clear();
                        List<CoachService> services = qcCoachServiceResponse.data.services;
                        mDatas.add(new GymItem(new CoachService.Builder().name("全部场馆").id(0).build()));
                        if (services != null) {
                            for (int i = 0; i < services.size(); i++) {
                                mDatas.add(new GymItem(services.get(i)));
                            }
                        }
                        //mDatas.add(new AddBatchCircleItem("+ 添加健身房"));
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showDefaultStyle(qcCoachServiceResponse.msg);
                    }
                }
            }));
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof GymItem) {
            RxBus.getBus().post(new EventScheduleService(((GymItem) mAdapter.getItem(position)).coachService));
            getActivity().onBackPressed();
        } else if (mAdapter.getItem(position) instanceof AddBatchCircleItem) {
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
                    getFragmentManager().beginTransaction()
                        .replace(R.id.activity_choose_address,
                            new AddGymFragmentBuilder(brand.getPhoto(), brand.getName(),
                                brand.getId()).build())
                        .addToBackStack(null)
                        .commit();
                }
            }
        }
    }
}
