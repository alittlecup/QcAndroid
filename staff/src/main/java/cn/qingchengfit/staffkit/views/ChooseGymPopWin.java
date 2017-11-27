package cn.qingchengfit.staffkit.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.widget.PopupWindow;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.MeasureUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
 * Created by Paper on 2017/3/8.
 */

public class ChooseGymPopWin extends PopupWindow implements FlexibleAdapter.OnItemClickListener {

    public FlexibleAdapter.OnItemClickListener listener;
    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    CommonFlexAdapter adapter;

    public ChooseGymPopWin(Context context) {
        super(context);
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setBackgroundResource(R.drawable.bubble_down);
        recyclerView.setPadding(0, 0, 10, 0);
        recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(context));

        recyclerView.addItemDecoration(new FlexibleItemDecoration(context)
          .withDivider(R.drawable.divider_horizon_left_44dp)
          .withOffset(1).withBottomEdge(true));        //recyclerView.getLayoutParams().height = MeasureUtils.dpToPx(197f,context.getResources());
        //recyclerView.getLayoutParams().width = MeasureUtils.dpToPx(210f,context.getResources());
        adapter = new CommonFlexAdapter(mDatas, this);
        adapter.setMode(SelectableAdapter.Mode.SINGLE);
        //setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bubble_down));
        recyclerView.setAdapter(adapter);
        setContentView(recyclerView);
        setHeight(MeasureUtils.dpToPx(197f, context.getResources()));
        setWidth(MeasureUtils.dpToPx(210f, context.getResources()));
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(5);
        }
    }

    public void setDatas(List<Shop> shops) {
        if (shops != null) {
            mDatas.clear();
            mDatas.add(new ChooseShopItemItem(Configs.IMAGE_ALL, "全部场馆"));
            for (int i = 0; i < shops.size(); i++) {
                mDatas.add(new ChooseShopItemItem(shops.get(i).photo, shops.get(i).name));
            }
            adapter.notifyDataSetChanged();
        }
    }

    public FlexibleAdapter.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(FlexibleAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override public boolean onItemClick(int position) {
        if (!adapter.isSelected(position)) adapter.toggleSelection(position);
        if (listener != null) {
            listener.onItemClick(position);
        }
        dismiss();
        return true;
    }
}
