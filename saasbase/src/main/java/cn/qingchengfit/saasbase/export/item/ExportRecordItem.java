package cn.qingchengfit.saasbase.export.item;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.export.bean.ExportRecord;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ExportRecordItem extends AbstractFlexibleItem<ExportRecordItem.ExportRecordVH> {

  private static final int EXOPRT_ING = 1;
  private static final int EXOPRT_SUCCESS = 2;
  private static final int EXOPRT_FAILED = 3;

  ExportRecord exportRecord;
  OnButtonClickListener listener;

  public ExportRecordItem(ExportRecord exportRecord, OnButtonClickListener listener) {
    this.exportRecord = exportRecord;
    this.listener = listener;
  }

  public ExportRecord getData() {
    return exportRecord;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_export_record;
  }

  @Override public ExportRecordVH createViewHolder(View view, FlexibleAdapter adapter) {
    ExportRecordVH holder = new ExportRecordVH(view, adapter);
    holder.tvExportDownload.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.onDownload((int) v.getTag());
        }
      }
    });
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ExportRecordVH holder, int position,
    List payloads) {
    holder.tvExportTitle.setText(DateUtils.getFileNameFormServer(exportRecord.file_name));
    holder.tvExportInfo.setText(DateUtils.replaceTFromServer(exportRecord.created_at) + " " + exportRecord.created_by.username);
    switch (exportRecord.status) {
      case EXOPRT_ING:
        holder.tvExportState.setVisibility(View.VISIBLE);
        holder.tvExportDownload.setVisibility(View.GONE);
        holder.tvExportState.setText(holder.itemView.getResources().getString(R.string.text_export_status));
        break;
      case EXOPRT_SUCCESS:
        holder.tvExportState.setVisibility(View.GONE);
        holder.tvExportDownload.setVisibility(View.VISIBLE);
        break;
      case EXOPRT_FAILED:
        break;
    }
    holder.tvExportDownload.setTag(position);
  }

  class ExportRecordVH extends FlexibleViewHolder {

    @BindView(R2.id.tv_export_title) TextView tvExportTitle;
    @BindView(R2.id.tv_export_info) TextView tvExportInfo;
    @BindView(R2.id.tv_export_download) TextView tvExportDownload;
    @BindView(R2.id.tv_export_state) TextView tvExportState;

    public ExportRecordVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnButtonClickListener {
    void onDownload(int position);
  }
}
