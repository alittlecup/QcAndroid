package cn.qingchengfit.gym.pages.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import cn.qingchengfit.gym.GymBaseFragment;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.bean.BrandWithGyms;
import cn.qingchengfit.gym.bean.GymSearchResponse;
import cn.qingchengfit.gym.bean.GymWithSuperUser;
import cn.qingchengfit.gym.databinding.GyGymSearchPageBinding;
import cn.qingchengfit.gym.item.GymSearchItem;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Leaf(module = "gym", path = "/gym/search") public class GymSearchPage
    extends GymBaseFragment<GyGymSearchPageBinding, GymSearchViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  private CommonFlexAdapter adapter;
  private Comparator<BrandWithGyms> gymsComparator = new Comparator<BrandWithGyms>() {
    @Override public int compare(BrandWithGyms o1, BrandWithGyms o2) {
      List<GymWithSuperUser> gyms = o1.gyms;
      List<GymWithSuperUser> gyms1 = o2.gyms;
      return gyms1.size() - gyms.size();
    }
  };

  @Override protected void subscribeUI() {
    mViewModel.response.observe(this, this::upDateItems);
  }

  private void upDateItems(GymSearchResponse response) {
    hideLoading();
    List<AbstractFlexibleItem> items = new ArrayList<>();
    if (response != null) {
      List<BrandWithGyms> brands = response.brands;
      List<BrandWithGyms> others = response.others;
      if (brands != null && !brands.isEmpty()) {
        Collections.sort(brands, gymsComparator);
        for (BrandWithGyms brand : brands) {
          items.add(new GymSearchItem(brand));
        }
      }
      items.add(new SimpleTextItemItem("以下为场馆中包含\"" + mBinding.editSearch.getText() + "\"的结果"));
      if (others != null && !others.isEmpty()) {
        Collections.sort(others, gymsComparator);
        for (BrandWithGyms brand : others) {
          items.add(new GymSearchItem(brand));
        }
      }
    }
    if (items.size() == 1) {
      items.clear();
      items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "未搜索到结果"));
    }
    adapter.updateDataSet(items);
  }

  @Override
  public GyGymSearchPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymSearchPageBinding.inflate(inflater, container, false);
    initRecyclerView();
    initListener();
    int i = AppUtils.StatusBarLightMode(getActivity());
    if (i == 0) {
      ViewGroup.LayoutParams layoutParams = mBinding.status.getLayoutParams();
      layoutParams.height = MeasureUtils.getStatusBarHeight(getContext());
      mBinding.status.setLayoutParams(layoutParams);
    } else {
      if (!CompatUtils.less21() && isfitSystemPadding()) {
        mBinding.root.setPadding(0, MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
      } else {
        ViewGroup.LayoutParams layoutParams = mBinding.status.getLayoutParams();
        layoutParams.height = MeasureUtils.getStatusBarHeight(getContext());
        mBinding.status.setLayoutParams(layoutParams);
      }
    }
    return mBinding;
  }

  private void initListener() {
    RxTextView.afterTextChangeEvents(mBinding.editSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .map((event -> event.editable().toString()))
        .subscribe((msg -> {
          if (TextUtils.isEmpty(msg)) {
            mBinding.tvCreateGym.setVisibility(View.GONE);
            mBinding.divider.setVisibility(View.GONE);
            mBinding.imgClear.setVisibility(View.GONE);
          } else {
            mBinding.tvCreateGym.setVisibility(View.VISIBLE);
            mBinding.divider.setVisibility(View.VISIBLE);
            mBinding.tvCreateGym.setText("+ 创建\"" + msg + "\"场馆");
            mBinding.imgClear.setVisibility(View.VISIBLE);
          }
        }));
    mBinding.tvCreateGym.setOnClickListener(v -> routeTo("/gym/create", null));
    mBinding.imgArrowLeft.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    mBinding.editSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    mBinding.editSearch.setSingleLine(true);
    //mBinding.editSearch.setOnEditorActionListener(new View.OnKeyListener() {
    //  @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
    //    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
    //      String s = mBinding.editSearch.getText().toString();
    //      if (!TextUtils.isEmpty(s)) {
    //        showLoading();
    //        mViewModel.searchGym(s);
    //      }
    //    }
    //    return false;
    //  }
    //});
    RxTextView.editorActions(mBinding.editSearch)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new Action1<Integer>() {
          @Override public void call(Integer integer) {
            if (integer == EditorInfo.IME_ACTION_SEARCH) {
              String s = mBinding.editSearch.getText().toString();
              if (!TextUtils.isEmpty(s)) {
                showLoading();
                mViewModel.searchGym(s);
              }
            }
          }
        });

    mBinding.imgClear.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mBinding.editSearch.setText("");
        mBinding.imgClear.setVisibility(View.GONE);
      }
    });
    mBinding.tvCancel.setOnClickListener(v -> {
      getActivity().onBackPressed();
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(new CommonNoDataItem(R.drawable.ic_search_black, "搜索全部健身房", ""));
    adapter.updateDataSet(items);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof GymSearchItem) {
      BrandWithGyms data = ((GymSearchItem) item).getData();
      routeTo("/gym/simple/list", new BundleBuilder().withParcelable("brand", data).build());
    }
    return false;
  }
}
