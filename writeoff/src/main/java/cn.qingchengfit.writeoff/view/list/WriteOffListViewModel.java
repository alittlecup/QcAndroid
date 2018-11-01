package cn.qingchengfit.writeoff.view.list;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.CommonItemFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.writeoff.WriteOffItem;
import cn.qingchengfit.writeoff.network.IWriteOffRepository;
import cn.qingchengfit.writeoff.vo.IWriteOffItemData;
import cn.qingchengfit.writeoff.vo.impl.TicketListWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class WriteOffListViewModel
    extends FlexibleViewModel<List<? extends IWriteOffItemData>, WriteOffItem, String> {
  @Inject IWriteOffRepository repository;
  private MutableLiveData<List<? extends IWriteOffItemData>> datas = new MutableLiveData<>();

  @Inject public WriteOffListViewModel() {
  }

  @NonNull @Override
  protected LiveData<List<? extends IWriteOffItemData>> getSource(@NonNull String s) {
    return Transformations.switchMap(repository.qcGetVerifyTickets(), input -> {
      dealResource(input, ticketListWrapper -> datas.setValue(ticketListWrapper.etickets));
      return datas;
    });
  }

  @Override
  protected boolean isSourceValid(@Nullable List<? extends IWriteOffItemData> iWriteOffItemData) {
    return iWriteOffItemData != null;
  }

  @Override
  protected List<WriteOffItem> map(@NonNull List<? extends IWriteOffItemData> iWriteOffItemData) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<IWriteOffItemData, WriteOffItem>(WriteOffItem.class))
        .from(new ArrayList<>(iWriteOffItemData));
  }
}
