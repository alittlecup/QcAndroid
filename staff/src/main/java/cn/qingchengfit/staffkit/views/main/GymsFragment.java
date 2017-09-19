package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventFreshGyms;
import cn.qingchengfit.items.CoachServiceItem;
import cn.qingchengfit.items.ListAddItem;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.GuideActivity;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.google.gson.Gson;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/16 2016.
 */
public class GymsFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, GymListPresenter.GymListView {

    @BindView(R.id.recycleview) RecyclerView recycleview;

    @Inject GymListPresenter gymListPresenter;
    @Inject GymWrapper gymWrapper;

    private List<AbstractFlexibleItem> adapterDatas = new ArrayList<>();
    private CommonFlexAdapter mAdapter;
  private Observable<EventFreshGyms> mFreshOb;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gyms, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDI();
        initView();
      mFreshOb = RxBus.getBus().register(EventFreshGyms.class);
      mFreshOb.subscribe(new Action1<EventFreshGyms>() {
        @Override public void call(EventFreshGyms eventFreshGyms) {
                refresh();
            }
        });
        return view;
    }

    private void initDI() {
        delegatePresenter(gymListPresenter, this);
    }

    public void refresh() {
        gymListPresenter.loadData();
    }

    private void initView() {

        mAdapter = new CommonFlexAdapter(adapterDatas, this);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.setHasFixedSize(true);
        recycleview.setNestedScrollingEnabled(false);
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(mAdapter);
        /**
         * 订阅品牌
         */
        gymListPresenter.subscribeGymsByBrandId();
    }

    @Override public void onResume() {
        super.onResume();
        gymWrapper.setCoachService(null);
    }

    @Override public void onDestroyView() {
      RxBus.getBus().unregister(EventFreshGyms.class.getName(), mFreshOb);
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return GymsFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        if (adapterDatas.get(position) instanceof CoachServiceItem) {
            gymWrapper.setCoachService(((CoachServiceItem) adapterDatas.get(position)).getCoachService());
            Intent toGymdetail = new Intent(getActivity(), GymActivity.class);
            toGymdetail.putExtra(GymActivity.GYM_TO, GymActivity.GYM_DETAIL);
            startActivity(toGymdetail);
        } else if (adapterDatas.get(position) instanceof ListAddItem) {
            if (gymWrapper.getBrand().has_add_permission) {
                SystemInitBody body;
                if (TextUtils.isEmpty(PreferenceUtils.getPrefString(getContext(), "init", ""))) {
                    body = new SystemInitBody();
                } else {
                    body = new Gson().fromJson(PreferenceUtils.getPrefString(getContext(), "init", ""), SystemInitBody.class);
                }
                App.caches.put("init", body);
                Intent toGuide = new Intent(getActivity(), GuideActivity.class);
                toGuide.putExtra(Configs.EXTRA_BRAND, gymWrapper.getBrand());
                toGuide.putExtra("isAdd", true);
                startActivity(toGuide);
            } else {
                ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand),
                    gymWrapper.getBrand().getCreated_by() == null || gymWrapper.getBrand().getCreated_by().getUsername() == null ? ""
                        : gymWrapper.getBrand().getCreated_by().getUsername()));
            }
        }
        return true;
    }

    @Override public void onServiceList(List<CoachService> list) {
        adapterDatas.clear();
        for (int i = 0; i < list.size(); i++) {
            adapterDatas.add(new CoachServiceItem(list.get(i), true));
        }
        if (adapterDatas.size() == 0) {
            adapterDatas.add(new SimpleTextItemItem("该品牌下暂无场馆", Gravity.CENTER));
            adapterDatas.add(new ListAddItem("添加场馆"));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
