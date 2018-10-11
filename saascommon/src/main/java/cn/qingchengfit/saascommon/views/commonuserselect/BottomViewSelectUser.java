package cn.qingchengfit.saascommon.views.commonuserselect;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.databinding.CmViewBottomUserSelectBinding;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

public class BottomViewSelectUser extends BottomSheetDialogFragment
  implements FlexibleAdapter.OnItemClickListener {

  CmViewBottomUserSelectBinding binding;
  CommonFlexAdapter adapter = new CommonFlexAdapter(new ArrayList(), this);
  private Observable<EventClickViewPosition> obBus;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //if (getParentFragment() instanceof CommonUserSelectView) {
    //  ((CommonUserSelectView) getParentFragment()).selectItems.observe(this, commonUserItems -> {
    //    adapter.updateDataSet(commonUserItems);
    //  });
    //}
    obBus = RxBus.getBus().register(EventClickViewPosition.class);
  }

  void clearSelect() {
    if (getParentFragment() instanceof CommonUserSelectView) {
      ((CommonUserSelectView) getParentFragment()).selectedAll.setValue(false);
    }
    dismiss();
  }

  void delSelect(List<CommonUserItem> items) {
    if (getParentFragment() instanceof CommonUserSelectView) {
      ((CommonUserSelectView) getParentFragment()).selectItems.setValue(items);
      ((CommonUserSelectView) getParentFragment()).setSelectedItem(items);
    }
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    binding = CmViewBottomUserSelectBinding.inflate(inflater);
    binding.tvClearAll.setOnClickListener(v -> clearSelect());
    initRv();

    obBus.subscribe(new BusSubscribe<EventClickViewPosition>() {
      @Override public void onNext(EventClickViewPosition eventClickViewPosition) {
        if (eventClickViewPosition.getId() == R.id.item_delete) {
          adapter.removeItem(eventClickViewPosition.getPosition());
          adapter.notifyDataSetChanged();
          List<CommonUserItem> items = new ArrayList<>();
          items.addAll(adapter.getCurrentItems());
          delSelect(items);
          binding.tvStudCount.setText(String.format(getResources().getString(R.string.bottom_view_select_count),adapter.getItemCount()));

        }
      }
    });
    return binding.getRoot();
  }

  private void initRv() {
    binding.recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    binding.recycleview.addItemDecoration(
      new FlexibleItemDecoration(getContext()).withOffset(2).withBottomEdge(true));
    binding.recycleview.setAdapter(adapter);
    adapter.setTag("del", true);
    if (getParentFragment() instanceof CommonUserSelectView) {
      List<CommonUserItem> value =
          ((CommonUserSelectView) getParentFragment()).selectItems.getValue();
      adapter.updateDataSet(value);
      binding.tvStudCount.setText(String.format(getResources().getString(R.string.bottom_view_select_count),value.size()));
    }
  }

  @Override public boolean onItemClick(int position) {
    return true;
  }

  @Override public void onDestroy() {
    RxBus.getBus().unregister(EventClickViewPosition.class.getName(), obBus);
    super.onDestroy();
  }
}
