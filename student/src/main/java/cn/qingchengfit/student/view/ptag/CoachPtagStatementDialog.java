package cn.qingchengfit.student.view.ptag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.event.PtagStatementEvent;
import cn.qingchengfit.views.fragments.BaseDialogFragment;

public class CoachPtagStatementDialog extends BaseDialogFragment {

  public static CoachPtagStatementDialog newInstance(String title, String content) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("content", content);
    CoachPtagStatementDialog fragment = new CoachPtagStatementDialog();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.st_dialog_ptag_statement, container, false);
    Button btnDownload = view.findViewById(R.id.btn_first);
    Button btnStartRecord = view.findViewById(R.id.btn_second);
    TextView tvTitle = view.findViewById(R.id.tv_ptag_statement_title);
    TextView tvContent = view.findViewById(R.id.tv_ptag_statement_content);
    ImageView imgClose = view.findViewById(R.id.img_clost_ptag_statement);
    imgClose.setOnClickListener(v -> {
      dismiss();
    });
    if (getArguments() != null){
      tvTitle.setText(getArguments().getString("title"));
      tvContent.setText(getArguments().getString("content"));
    }
    btnDownload.setOnClickListener(v -> {
      RxBus.getBus().post(new PtagStatementEvent(PtagStatementEvent.DOWNLOAD_PDF));
    });

    btnStartRecord.setOnClickListener(v -> {
      dismiss();
    });

    return view;
  }
}
