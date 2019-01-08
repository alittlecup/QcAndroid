package cn.qingchengfit.card.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.card.databinding.BottomChooseCouponDialogBinding;
import cn.qingchengfit.card.item.CouponItem;
import cn.qingchengfit.saasbase.cards.bean.Coupon;
import cn.qingchengfit.widgets.BottomChooseDialog;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

public class BottomChooseCouponDialog extends BottomSheetDialog implements FlexibleAdapter.OnItemClickListener {

  BottomChooseCouponDialogBinding mBinding;
  CommonFlexAdapter adapter;


  public BottomChooseCouponDialog(@NonNull Context context,
      List<Coupon> contents) {
    this(context, contents, SelectableAdapter.Mode.SINGLE);
  }

  public BottomChooseCouponDialog(@NonNull Context context, List<Coupon> contents, @SelectableAdapter.Mode int type) {
    super(context);
    initView(context);
    setContentView(mBinding.getRoot());
    if (contents != null && !contents.isEmpty()) {
      List<CouponItem> items = new ArrayList<>();
      for (Coupon data : contents) {
        items.add(new CouponItem(data));
      }
      adapter.updateDataSet(items);
    }
    adapter.addListener(this);
    adapter.setMode(type);
    try {
      // hack bg color of the BottomSheetDialog
      ViewGroup parent = (ViewGroup) mBinding.getRoot().getParent();
      parent.setBackgroundResource(android.R.color.transparent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initView(Context context) {
    mBinding = BottomChooseCouponDialogBinding.inflate(LayoutInflater.from(context));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(context));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.imgClose.setOnClickListener(v -> cancel());
  }

  BottomChooseDialog.onItemClickListener listener;




  public void setEnabled(int position, boolean isEnable) {
    if (adapter != null) {
      IFlexible item = adapter.getItem(position);
      if (item != null) {
        item.setEnabled(isEnable);
      }
    }
  }


  public void setOnItemClickListener(BottomChooseDialog.onItemClickListener listener) {
    this.listener = listener;
  }

  public interface onItemClickListener {
    /**
     * 返回值是因为通过选中的item 进行业务判断，来反向控制是否选中
     * @param position
     * @return true-表示可以修改选择，false-表示不可以选中新的点击
     */
    boolean onItemClick(int position);
  }

  @Override public boolean onItemClick(int position) {
    if(listener!=null){
      listener.onItemClick(position);
    }
    return false;
  }
}
