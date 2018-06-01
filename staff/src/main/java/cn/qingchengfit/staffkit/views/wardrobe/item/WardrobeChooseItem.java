package cn.qingchengfit.staffkit.views.wardrobe.item;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
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
 * Created by Paper on 16/8/29.
 */
public class WardrobeChooseItem extends AbstractFlexibleItem<WardrobeChooseItem.WardrobeVH> {

    Locker locker;

    public WardrobeChooseItem(Locker locker) {
        this.locker = locker;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_wardrobe;
    }

    @Override public WardrobeVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new WardrobeVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, WardrobeVH holder, int position, List payloads) {
        holder.wardrobeId.setText(locker.name);
        if (locker.is_used) {
            holder.itemRoot.setBackgroundResource(R.drawable.bg_wardrobe_choose);
            holder.name.setText("已占用");
        } else {
            holder.itemRoot.setBackgroundResource(R.drawable.bg_wardrobe_normal);
            holder.name.setText("");
        }
        holder.phone.setVisibility(View.GONE);
        holder.period.setVisibility(View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class WardrobeVH extends FlexibleViewHolder {

	TextView name;
	TextView phone;
	TextView period;
	TextView longTimeTag;
	RelativeLayout itemRoot;
	TextView wardrobeId;

        public WardrobeVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          name = (TextView) view.findViewById(R.id.name);
          phone = (TextView) view.findViewById(R.id.phone);
          period = (TextView) view.findViewById(R.id.period);
          longTimeTag = (TextView) view.findViewById(R.id.long_time_tag);
          itemRoot = (RelativeLayout) view.findViewById(R.id.item_root);
          wardrobeId = (TextView) view.findViewById(R.id.wardrobe_id);
        }
    }
}
