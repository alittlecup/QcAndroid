package cn.qingchengfit.staffkit.views.wardrobe.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
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
public class WardrobeItem extends AbstractFlexibleItem<WardrobeItem.WardrobeVH> {

    Locker locker;

    public WardrobeItem(Locker locker) {
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
            holder.wardrobeId.setTextColor(CompatUtils.getColor(holder.itemView.getContext(), R.color.text_black));
        } else {
            holder.itemRoot.setBackgroundResource(R.drawable.bg_wardrobe_normal);
            holder.wardrobeId.setTextColor(CompatUtils.getColor(holder.itemView.getContext(), R.color.colorPrimary));
        }
        if (locker != null && locker.user != null) {
            holder.name.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);
            holder.name.setText(locker.user.getUsername());
            holder.phone.setText(locker.user.getPhone());
            if (locker.is_long_term_borrow) {
                holder.period.setVisibility(View.VISIBLE);
                holder.period.setText(DateUtils.Date2MMDD(DateUtils.formatDateFromServer(locker.start)) + "至" + DateUtils.Date2MMDD(
                    DateUtils.formatDateFromServer(locker.end)));
            } else {
                holder.period.setVisibility(View.GONE);
            }
        } else {
            holder.name.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
            holder.period.setVisibility(View.GONE);
        }
        holder.longTimeTag.setVisibility(locker.is_long_term_borrow ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class WardrobeVH extends FlexibleViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.phone) TextView phone;
        @BindView(R.id.period) TextView period;
        @BindView(R.id.long_time_tag) TextView longTimeTag;
        @BindView(R.id.item_root) RelativeLayout itemRoot;
        @BindView(R.id.wardrobe_id) TextView wardrobeId;

        public WardrobeVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
