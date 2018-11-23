package cn.qingchengfit.saasbase.cards.views.offday;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.model.common.PayEvent;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.item.PayWardrobeItem;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
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
 * Created by Paper on 2017/5/5.
 */

public class PayMethodOfflineDialog extends BaseDialogFragment
    implements FlexibleAdapter.OnItemClickListener {
  RecyclerView rv;
  private CommonFlexAdapter mAdapter;
  private List<AbstractFlexibleItem> mDatas = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_wardrobe_pay, container, false);
    rv = (RecyclerView) view.findViewById(R.id.rv);

    rv.setHasFixedSize(true);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    mAdapter = new CommonFlexAdapter(mDatas, this);
    mDatas.clear();
    mDatas.add(new SimpleTextItemItem("线下支付"));
    mDatas.add(new PayWardrobeItem(2, null));
    mDatas.add(new PayWardrobeItem(4, null));
    mDatas.add(new PayWardrobeItem(5, null));
    mDatas.add(new PayWardrobeItem(6, null));
    rv.setAdapter(mAdapter);
    mAdapter.updateDataSet(mDatas);
    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);

  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    Window window = dialog.getWindow();
    window.getDecorView().setPadding(0, 0, 0, 0);

    WindowManager.LayoutParams wlp = window.getAttributes();
    wlp.gravity = Gravity.BOTTOM;
    wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
    wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(wlp);
    window.setWindowAnimations(R.style.ButtomDialogStyle);
    dialog.setCanceledOnTouchOutside(true);
    return dialog;
  }

  @Override public boolean onItemClick(int i) {
    if (mAdapter.getItem(i) instanceof PayWardrobeItem) {
      RxBus.getBus().post(new PayEvent(((PayWardrobeItem) mAdapter.getItem(i)).getPay_mode()));
      dismiss();
    }
    return false;
  }
}
