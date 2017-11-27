package cn.qingchengfit.staffkit.views.export;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.export.item.ExportRecordItem;
import cn.qingchengfit.staffkit.views.export.model.ExportRecord;
import cn.qingchengfit.staffkit.views.export.presenter.ImportExportPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/6.
 */

public class ExportRecordFragment extends BaseFragment implements ImportExportPresenter.MVPView,
    ExportRecordItem.OnButtonClickListener {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.recycler_export_record) RecyclerView recyclerExportRecord;
  @Inject ImportExportPresenter presenter;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_export_record, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    toolbarTitle.setText(getString(R.string.toolbar_export_record));
    adapter = new CommonFlexAdapter(itemList);
    recyclerExportRecord.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerExportRecord.addItemDecoration(new FlexibleItemDecoration(getContext())
        .addItemViewType(R.layout.item_export_record, 1)
        .withDivider(R.drawable.divider_card_list)
        .withBottomEdge(true));
    recyclerExportRecord.setAdapter(adapter);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    showLoadingTrans();
    presenter.qcGetExportRecord();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onExportRecord(List<ExportRecord> record) {
    hideLoadingTrans();
    if(record.size() > 0){
      itemList.clear();
    }
    for (ExportRecord exportRecord : record){
      itemList.add(new ExportRecordItem(exportRecord, this));
    }
    adapter.updateDataSet(itemList);
  }

  @Override public void onExportSuccess() {
  }

  @Override public void onSendEmailSuccess() {

  }

  @Override public void onDownload(int position) {
    if (itemList.get(position) instanceof ExportRecordItem) {
      getFragmentManager()
          .beginTransaction()
          .setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold)
          .replace(R.id.frag_import_export, ExportSendEmailFragment.newInstance(((ExportRecordItem) itemList.get(position)).getData()))
          .addToBackStack(null)
          .commit();
    }
  }
}
