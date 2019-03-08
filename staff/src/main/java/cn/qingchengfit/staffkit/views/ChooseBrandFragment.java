package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.ListAddItem;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.rxbus.event.EventBrandChange;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
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
 * Created by Paper on 2017/1/12.
 */

public class ChooseBrandFragment extends BaseDialogFragment
    implements FlexibleAdapter.OnItemClickListener {

  public RecyclerView rvBrand;
  public CommonFlexAdapter mCommonFlexAdapter;
  public List<AbstractFlexibleItem> mDatas = new ArrayList<>();

  @Inject public StaffRespository mRestRepository;
  @Inject public GymWrapper gymWrapper;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_choose_brand, container, false);
    rvBrand = (RecyclerView) view.findViewById(R.id.rv_brand);

    mCommonFlexAdapter = new CommonFlexAdapter(mDatas, this);
    mCommonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    rvBrand.setHasFixedSize(true);
    rvBrand.setNestedScrollingEnabled(false);
    rvBrand.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rvBrand.setAdapter(mCommonFlexAdapter);
    queryData();
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    return view;
  }

  @Override public void show(FragmentManager manager, String tag) {

    super.show(manager, tag);
  }

  public String getFragmentName() {
    return ChooseBrandFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    if (mCommonFlexAdapter.getItem(position) instanceof BrandItemItem) {
      gymWrapper.setBrand(((BrandItemItem) mCommonFlexAdapter.getItem(position)).getBrand());
      RxBus.getBus().post(new EventBrandChange());
      dismiss();
    } else if (mCommonFlexAdapter.getItem(position) instanceof ListAddItem) {
      startActivityForResult(new Intent(getActivity(), AddBrandActivity.class), 1);
    }
    return true;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        queryData();
      }
    }
  }

  public void queryData() {
    RxRegiste(mRestRepository.getStaffAllApi()
        .qcGetBrands(App.staffId)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
          @Override public void call(QcDataResponse<BrandsResponse> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              mDatas.clear();
              int pos = 0;
              if (qcResponse.data.brands != null) {
                for (int i = 0; i < qcResponse.data.brands.size(); i++) {
                  mDatas.add(new BrandItemItem(qcResponse.data.brands.get(i), getContext()));
                  if (qcResponse.data.brands.get(i)
                      .getId()
                      .equalsIgnoreCase(gymWrapper.brand_id())) {
                    pos = i;
                  }
                }
              }
              mDatas.add(new ListAddItem(getString(R.string.create_brand)));
              mCommonFlexAdapter.updateDataSet(mDatas);
              mCommonFlexAdapter.notifyDataSetChanged();
              mCommonFlexAdapter.toggleSelection(pos);
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
          }
        }));
  }
}
