package cn.qingchengfit.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.GymUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
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
 * Created by Paper on 16/8/27.
 */
public class CoachServiceItem extends AbstractFlexibleItem<CoachServiceItem.CoachServiceVH> {

    private CoachService coachService;
    private boolean isShowRight = false;

    public CoachServiceItem(CoachService coachService) {
        this.coachService = coachService;
    }

    public CoachServiceItem(CoachService coachService, boolean isShowRight) {
        this.coachService = coachService;
        this.isShowRight = isShowRight;
    }

    public CoachService getCoachService() {
        return coachService;
    }

    public void setCoachService(CoachService coachService) {
        this.coachService = coachService;
    }

    @Override public boolean equals(Object o) {
        if (o instanceof CoachServiceItem){
            return ((CoachServiceItem) o).getCoachService().getId().equalsIgnoreCase(coachService.getId());
        }else return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_gym;
    }

    @Override public CoachServiceVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CoachServiceVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CoachServiceVH holder, int position, List payloads) {

        holder.itemGymName.setText(coachService.getName());
        holder.itemGymPhonenum.setText("联系方式：" + coachService.getPhone());
        holder.itemBrand.setText("我的职位：" + coachService.getPosition());
        holder.qcIdentify.setImageResource(GymUtils.getSystemEndDay(coachService) >= 0 ? R.drawable.ic_pro_green : R.drawable.ic_pro_free);
        Glide.with(adapter.getRecyclerView().getContext())
            .load(PhotoUtils.getSmall(coachService.getPhoto()))
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(holder.itemGymHeader, adapter.getRecyclerView().getContext()));
        if (isShowRight) {
            holder.itemRight.setVisibility(View.VISIBLE);
            holder.itemRight.setImageResource(R.drawable.ic_arrow_right);
        } else {
            holder.itemRight.setVisibility(View.GONE);
        }
    }

    public class CoachServiceVH extends FlexibleViewHolder {
	ImageView itemGymHeader;
	TextView itemGymName;
	TextView itemIsPersonal;
	ImageView qcIdentify;
	TextView itemGymPhonenum;
	ImageView itemRight;
	TextView itemBrand;
	View view;

        public CoachServiceVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemGymHeader = (ImageView) view.findViewById(R.id.item_gym_header);
          itemGymName = (TextView) view.findViewById(R.id.item_gym_name);
          itemIsPersonal = (TextView) view.findViewById(R.id.item_is_personal);
          qcIdentify = (ImageView) view.findViewById(R.id.qc_identify);
          itemGymPhonenum = (TextView) view.findViewById(R.id.item_gym_phonenum);
          itemRight = (ImageView) view.findViewById(R.id.item_right);
          itemBrand = (TextView) view.findViewById(R.id.item_gym_brand);
          view = (View) view.findViewById(R.id.hiden);
        }
    }
}
