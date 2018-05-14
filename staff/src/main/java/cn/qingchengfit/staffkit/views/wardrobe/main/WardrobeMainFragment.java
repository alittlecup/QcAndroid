package cn.qingchengfit.staffkit.views.wardrobe.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.AllLockers;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.model.responese.LockerRegions;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventContinueHire;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.wardrobe.add.WardrobeAddFragment;
import cn.qingchengfit.staffkit.views.wardrobe.back.WardrobeReturnFragment;
import cn.qingchengfit.staffkit.views.wardrobe.choose.SearchResultFragment;
import cn.qingchengfit.staffkit.views.wardrobe.choose.SimpleChooseItemItem;
import cn.qingchengfit.staffkit.views.wardrobe.district.DistrictListFragment;
import cn.qingchengfit.staffkit.views.wardrobe.hire.WardrobeContinueHireFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AnimatedButton;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
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
 * Created by Paper on 16/8/25.
 */
public class WardrobeMainFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.strip) TabLayout strip;
  @BindView(R.id.viewpager) ViewPager viewpager;
  @BindView(R.id.all_region_rv) RecyclerView allRegionRv;
  @BindView(R.id.all_region) LinearLayout allRegion;
  @BindView(R.id.btn_show_all) AnimatedButton btnShowAll;
  @Inject RestRepository restRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  HashMap<Long, List<Locker>> mLockers = new HashMap<>();
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitile;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.layout_manage_district) FrameLayout layoutManageDistrict;
  private FragmentAdapter fragmentAdapter;
  private ArrayList<Fragment> chooseWardrobeListFragments = new ArrayList<>();
  private List<LockerRegion> regions = new ArrayList<>();
  private CommonFlexAdapter mAdapter;
  private List<AbstractFlexibleItem> mData = new ArrayList<>();
  private Bundle mSaveState = new Bundle();
  private SearchResultFragment searchResultFragment;
  private Toolbar.OnMenuItemClickListener menuItemClickListener =
      new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (item.getItemId() == R.id.action_add) {
            if (!serPermisAction.check(PermissionServerUtils.LOCKER_SETTING_CAN_WRITE)) {
              showAlert(R.string.alert_permission_forbid);
              return true;
            }
            mSaveState.putInt("page", viewpager.getCurrentItem());
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), new WardrobeAddFragment())
                .addToBackStack(getFragmentName())
                .commit();
          } else if (item.getItemId() == R.id.action_search) {
            showSearch(toolbarLayout);
            //RxBus.getBus().post(new EventOpenSearch("搜索更衣柜"));
          }
          return true;
        }
      };

  public static WardrobeMainFragment newInstance(List<Locker> lockers) {

    Bundle args = new Bundle();
    args.putParcelableArrayList("l", (ArrayList<Locker>) lockers);
    WardrobeMainFragment fragment = new WardrobeMainFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //        mLockers = getArguments().getParcelableArrayList("l");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_wardrobe_main, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    RxRegiste(restRepository.getGet_api()
        .qcGetAllRegion(App.staffId, gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<LockerRegions>>() {
          @Override public void call(QcDataResponse<LockerRegions> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              regions.clear();
              regions.addAll(qcResponse.data.locker_regions);
              RxRegiste(restRepository.getGet_api()
                  .qcGetAllLockers(App.staffId, gymWrapper.getParams())
                  .observeOn(AndroidSchedulers.mainThread())
                  .onBackpressureBuffer()
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Action1<QcDataResponse<AllLockers>>() {
                    @Override public void call(QcDataResponse<AllLockers> qcResponseAllLockers) {
                      if (ResponseConstant.checkSuccess(qcResponseAllLockers)) {
                        handleLockers(qcResponseAllLockers.data.lockers);
                      } else {

                      }
                    }
                  }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                  }));
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
          }
        }));

    RxBusAdd(EventContinueHire.class).subscribe(new Action1<EventContinueHire>() {
      @Override public void call(EventContinueHire eventContinueHire) {
        if (eventContinueHire.type == EventContinueHire.CONTINUE) {
          getFragmentManager().beginTransaction()
              .replace(mCallbackActivity.getFragId(),
                  WardrobeContinueHireFragment.newInstance(eventContinueHire.locker))
              .addToBackStack(getFragmentName())
              .commit();
        } else if (eventContinueHire.type == EventContinueHire.BACK) {
          getFragmentManager().beginTransaction()
              .replace(mCallbackActivity.getFragId(),
                  WardrobeReturnFragment.newInstance(eventContinueHire.locker))
              .addToBackStack(getFragmentName())
              .commit();
        }
      }
    });
    RxBusAdd(EventFresh.class).subscribe(new Action1<EventFresh>() {
      @Override public void call(EventFresh eventFresh) {
        RxRegiste(restRepository.getGet_api()
            .qcGetAllLockers(App.staffId, gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<AllLockers>>() {
              @Override public void call(QcDataResponse<AllLockers> qcResponseAllLockers) {
                if (ResponseConstant.checkSuccess(qcResponseAllLockers)) {
                  mSaveState.putInt("page", viewpager.getCurrentItem());
                  handleLockers(qcResponseAllLockers.data.lockers);
                } else {

                }
              }
            }, new Action1<Throwable>() {
              @Override public void call(Throwable throwable) {

              }
            }));
      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("更衣柜");
    initSearch(toolbarLayout, "输入更衣柜号或会员手机号进行搜索");
    toolbar.inflateMenu(R.menu.menu_student);
    toolbar.setOnMenuItemClickListener(menuItemClickListener);
  }

  @Override public void onPause() {
    super.onPause();
    mSaveState.putInt("page", viewpager.getCurrentItem());
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  public void handleLockers(List<Locker> lockers) {
    mLockers.clear();
    if (fragmentAdapter == null) {
      if (lockers != null) {
        chooseWardrobeListFragments.clear();
        for (int i = 0; i < regions.size(); i++) {
          List<Locker> ls = new ArrayList<>();
          for (int j = 0; j < lockers.size(); j++) {
            Locker locker = lockers.get(j);
            if (locker.region != null && locker.region.id.longValue() == regions.get(i).id) {
              ls.add(locker);
            }
          }
          mLockers.put(regions.get(i).id, ls);
          chooseWardrobeListFragments.add(WardrobeListFragment.newInstance(ls, regions.get(i)));
        }

        fragmentAdapter =
            new FragmentAdapter(getChildFragmentManager(), chooseWardrobeListFragments);
        viewpager.setAdapter(fragmentAdapter);

        strip.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(strip));

        mData.clear();
        for (int j = 0; j < regions.size(); j++) {
          mData.add(new SimpleChooseItemItem(regions.get(j)));
        }
        mAdapter = new CommonFlexAdapter(mData, this);
        allRegionRv.setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), 2));
        allRegionRv.setAdapter(mAdapter);
      }
    } else {

      allRegionRv.setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), 2));
      allRegionRv.setAdapter(mAdapter);
      viewpager.setAdapter(fragmentAdapter);
      strip.setupWithViewPager(viewpager);
      viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(strip));
      freshData(lockers);
    }
  }

  private void freshData(List<Locker> lockers) {
    mLockers.clear();
    for (int i = 0; i < regions.size(); i++) {
      List<Locker> ls = new ArrayList<>();

      for (int j = 0; j < lockers.size(); j++) {
        Locker locker = lockers.get(j);
        if (locker.region != null && locker.region.id.longValue() == regions.get(i).id) {
          ls.add(locker);
        }
        mLockers.put(regions.get(i).id, ls);
      }
      Fragment f = fragmentAdapter.findByTag(regions.get(i).id);
      if (f != null && f instanceof WardrobeListFragment) {
        ((WardrobeListFragment) f).fresh();
      } else {
        chooseWardrobeListFragments.add(WardrobeListFragment.newInstance(ls, regions.get(i)));
      }
    }
    fragmentAdapter.notifyDataSetChanged();
    if (viewpager != null) {
      int x = mSaveState.getInt("page", 0);
      if (x < viewpager.getAdapter().getCount()) viewpager.setCurrentItem(x);
    }
  }

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (viewpager != null) {
      int x = mSaveState.getInt("page", 0);
      viewpager.setCurrentItem(x);
    }
  }

  @Override public void onTextSearch(String text) {
    super.onTextSearch(text);
    searchLocker(text);
  }

  public void searchLocker(String key) {
    if (TextUtils.isEmpty(key)){
      if (!searchResultFragment.isHidden() && searchResultFragment != null)
        getChildFragmentManager().beginTransaction().hide(searchResultFragment).commitAllowingStateLoss();
      return;
    }
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("q", key);

    RxRegiste(restRepository.getGet_api()
        .qcGetAllLockers(loginStatus.staff_id(), params).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<AllLockers>>() {
          @Override public void call(QcDataResponse<AllLockers> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              onSearch(qcResponse.data.lockers);
            } else {
              ToastUtils.show(qcResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            ToastUtils.show(throwable.getMessage());
          }
        }));
  }

  public void onSearch(List<Locker> regions) {
    if (searchResultFragment == null){
      searchResultFragment = SearchResultFragment.newInstance(regions);
      getChildFragmentManager().beginTransaction()
        .add(R.id.frag_search_result, searchResultFragment)
        .commitAllowingStateLoss();
    }else{
      searchResultFragment.updateItems(regions);
      if (searchResultFragment.isHidden()){
        getChildFragmentManager().beginTransaction().show(searchResultFragment).commitAllowingStateLoss();
      }
    }
  }

  @Override public String getFragmentName() {
    return WardrobeMainFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.btn_show_all) public void onClick() {
    btnShowAll.toggle();
    if (allRegion.getVisibility() == View.VISIBLE) {
      allRegion.setVisibility(View.GONE);
    } else {
      allRegion.setVisibility(View.VISIBLE);
    }
  }

  @OnClick(R.id.layout_manage_district) public void onManageDistrict() {
    getFragmentManager().beginTransaction()
        .replace(mCallbackActivity.getFragId(), DistrictListFragment.newInstance())
        .addToBackStack(getFragmentName())
        .commit();
  }

  @Override public boolean onItemClick(int position) {
    if (mAdapter.getItem(position) instanceof SimpleChooseItemItem) {
      onClick();
      //            int p = stripPos.get(((SimpleChooseItemItem) mAdapter.getItem(position)).getLockerRegion().id);
      viewpager.setCurrentItem(position, true);
    }
    return true;
  }
}
