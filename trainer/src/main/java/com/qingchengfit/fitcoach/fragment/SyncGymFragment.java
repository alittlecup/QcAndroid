package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.event.EventLoginChange;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.event.EventSyncDone;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.items.SyncGymItem;
import com.qingchengfit.fitcoach.items.SyncWaitingItemItem;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
 * Created by Paper on 16/11/21.
 */
public class SyncGymFragment extends BaseFragment {

    @BindView(R.id.sync_gym_hint) TextView syncGymHint;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    @Inject RepoCoachServiceImpl repoCoachService;

    private CommonFlexAdapter commonFlexAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private SyncWaitingItemItem syncItem;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_gyms, container, false);
        ButterKnife.bind(this, view);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mData.clear();
        syncItem = new SyncWaitingItemItem();
        mData.add(syncItem);
        commonFlexAdapter = new CommonFlexAdapter(mData, this);
        recyclerview.setAdapter(commonFlexAdapter);
        RxBusAdd(EventSyncDone.class).subscribe(new Action1<EventSyncDone>() {
            @Override public void call(EventSyncDone eventSyncDone) {
                Intent toMain = new Intent(getActivity(), Main2Activity.class);
                startActivity(toMain);
                getActivity().finish();
            }
        });

        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid).subscribeOn(Schedulers.io())

            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(qcCoachServiceResponse -> {
                if (qcCoachServiceResponse.status == 200) {
                    syncGymHint.setText(getString(R.string.hint_sync_gyms, qcCoachServiceResponse.data.services.size()));
                    repoCoachService.createServices(qcCoachServiceResponse.data.services);
                    RxBus.getBus().post(new EventLoginChange());
                    mData.clear();
                    for (int i = 0; i < qcCoachServiceResponse.data.services.size(); i++) {
                        mData.add(new SyncGymItem(qcCoachServiceResponse.data.services.get(i)));
                    }
                    mData.add(syncItem);
                    commonFlexAdapter.notifyDataSetChanged();
                    recyclerview.scrollToPosition(mData.size());
                    RxRegiste(rx.Observable.just("")
                        .delay(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override public void call(String s) {
                                syncItem.isDone = true;
                                commonFlexAdapter.notifyItemChanged(mData.size() - 1);
                            }
                        }));
                } else {
                    ToastUtils.showDefaultStyle("数据同步失败");
                }
            }, throwable -> {
                ToastUtils.showDefaultStyle("数据同步失败");
            }));

        return view;
    }

    @Override public String getFragmentName() {
        return SyncGymFragment.class.getName();
    }
}
