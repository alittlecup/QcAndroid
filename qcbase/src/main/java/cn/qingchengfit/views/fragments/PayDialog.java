package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

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
 * Created by Paper on 2017/10/9.
 */

public abstract class PayDialog extends BottomSheetDialogFragment implements
    FlexibleAdapter.OnItemClickListener{

	ImageView btnClose;
	RecyclerView rv;
	TextView tvMoney;
	Button btnPay;

  CommonFlexAdapter adapter;



  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bottom_pay, container, false);
    btnClose = (ImageView) view.findViewById(R.id.btn_close);
    rv = (RecyclerView) view.findViewById(R.id.rv);
    tvMoney = (TextView) view.findViewById(R.id.tv_money);
    btnPay = (Button) view.findViewById(R.id.btn_pay);
    view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnCloseClicked();
      }
    });
    view.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnPayClicked();
      }
    });

    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new CommonFlexAdapter(getItems(),this);
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext())
        .withDivider(R.drawable.divider_grey)
        .withBottomEdge(true));
    rv.setAdapter(adapter);
    tvMoney.setText("￥"+100);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

  }

  protected abstract List<? extends AbstractFlexibleItem> getItems();

 public void onBtnCloseClicked() {
    dismiss();
  }

 public void onBtnPayClicked() {

  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    return true;
  }
}
