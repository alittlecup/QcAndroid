package cn.qingchengfit.staffkit.views.allotsales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.AllotSalePreView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.MyBindingViewHolder;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class SalesListFragment extends BaseFragment implements SalesListPresenter.PresenterView {

  @Inject SalesListPresenter presenter;
  @Inject StaffWrapper staffWrapper;

  AllotSalesAdapter adapter;
  List<AllotSalePreView> datas = new ArrayList<>();
  @BindView(R.id.rv_sales) RecycleViewWithNoImg rvSales;
  Unbinder unbinder;
  private LinearLayoutManager mLinearLayoutManager;

  public static SalesListFragment newInstance() {

    Bundle args = new Bundle();

    SalesListFragment fragment = new SalesListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view =
        inflater.inflate(R.layout.fragment_allotsale_salelist, container, false);
    unbinder = ButterKnife.bind(this, view);
    if (getActivity() instanceof AllotSalesActivity) {
      ((AllotSalesActivity) getActivity()).removeFilterFragment();
    }
    initDI();
    staffWrapper.setStaff(null);
    mCallbackActivity.setToolbar("销售列表", false, null, 0, null);
    initView();
    presenter.getSalesPreviewList();

    return view;
  }

  private void initView() {
    mLinearLayoutManager = new LinearLayoutManager(getContext());
    rvSales.setLayoutManager(mLinearLayoutManager);
    rvSales.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    rvSales.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        rvSales.setFresh(false);
      }
    });
    datas.clear();
    adapter = new AllotSalesAdapter(datas);
    rvSales.setAdapter(adapter);
  }

  private void initDI() {
    delegatePresenter(presenter, this);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public void onSalesPreview(List<AllotSalePreView> list) {
    rvSales.stopLoading();
    datas.clear();
    datas.addAll(list);
    adapter.notifyDataSetChanged();
  }

  @Override public void onResume() {
    if (getActivity() instanceof AllotSalesActivity) {
      ((AllotSalesActivity) getActivity()).removeFilterFragment();
    }
    super.onResume();
  }

  @Override public void onShowError(String e) {
    ToastUtils.show(e);
  }

  public class AllotSalesAdapter extends RecyclerView.Adapter<MyBindingViewHolder>
      implements View.OnClickListener {

    List<AllotSalePreView> adapterDatas;

    public AllotSalesAdapter(List<AllotSalePreView> adapterDatas) {
      this.adapterDatas = adapterDatas;
    }

    @Override public MyBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      MyBindingViewHolder holder = new MyBindingViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_allotsale_sale, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    }

    @Override public void onBindViewHolder(MyBindingViewHolder holder, int position) {
      holder.itemView.setTag(position);
      AllotSalePreView salePreView = adapterDatas.get(position);
      Staff seller = null;
      if (salePreView != null) {
        seller = salePreView.getSeller();
      }
      if (seller == null) {
        ToastUtils.showS("出错了， 请重试");
        return;
      }
      holder.tvSaleName.setText(TextUtils.isEmpty(seller.username) ? getResources().getString(
          R.string.qc_allotsale_sale_detail_notitle) : seller.username);
      if (TextUtils.isEmpty(seller.username)) {
        holder.itemSalesHeader.setImageDrawable(
            getResources().getDrawable(R.drawable.ic_nosales_normal));
      }
      holder.tvSaleStucount.setText(
          getString(R.string.qc_allotsale_stucount, salePreView.getCount()));

      String headerUrl = seller.getAvatar();
      if (!TextUtils.isEmpty(seller.username) && headerUrl != null && headerUrl.equals("")) {
        if (seller.gender == 0) {
          holder.itemSalesHeader.setImageDrawable(
              getResources().getDrawable(R.drawable.default_manage_male));
        } else {
          holder.itemSalesHeader.setImageDrawable(
              getResources().getDrawable(R.drawable.default_manager_female));
        }
      } else if (!TextUtils.isEmpty(seller.username)) {
        Glide.with(getActivity())
            .load(PhotoUtils.getSmall(headerUrl))
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemSalesHeader, getContext()));
      }
      //            ((ItemAllotsaleSaleBinding) holder.getBinding()).tvSaleStudents.setText(salePreView.getUsers().toString().replace("[", "").replace("]", ""));

    }

    @Override public int getItemCount() {
      return adapterDatas.size();
    }

    @Override public void onClick(View v) {
      if (getActivity() != null && getActivity() instanceof AllotSalesActivity) {
        ((AllotSalesActivity) getActivity()).staffWrapper.setStaff(
            adapterDatas.get((int) v.getTag()).getSeller());
      }
      // 跳转到分配销售
      staffWrapper.setStaff(adapterDatas.get((int) v.getTag()).getSeller());
      getFragmentManager().beginTransaction()
          .replace(mCallbackActivity.getFragId(), new SaleDetailFragment())
          .addToBackStack("saleList")
          .commit();
    }
  }
}
