//package cn.qingchengfit.staffkit.views.abstractflexibleitem;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.model.base.CoachService;
//import cn.qingchengfit.staffkit.R;
//import com.bumptech.glide.Glide;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import eu.davidea.viewholders.FlexibleViewHolder;
//import java.util.List;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 16/8/15.
// */
//public class CoachServiceChooseItem extends AbstractFlexibleItem<CoachServiceChooseItem.CoachServiceVH> {
//    private CoachService coachService;
//    boolean hasPermission = true;//todo !serPermisAction.check(coachService.getShop_id(), PermissionServerUtils.MANAGE_MEMBERS_CAN_DELETE);
//    public CoachServiceChooseItem(CoachService coachService,boolean hasPermission) {
//        this.coachService = coachService;
//        this.hasPermission = hasPermission;
//        setSelectable(true);
//    }
//
//    public CoachService getCoachService() {
//        return coachService;
//    }
//
//    public void setCoachService(CoachService coachService) {
//        this.coachService = coachService;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_gym;
//    }
//
//    @Override public CoachServiceVH createViewHolder(View view, FlexibleAdapter adapter) {
//        CoachServiceVH vh = new CoachServiceVH(view, adapter);
//        return vh;
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, CoachServiceVH holder, int position, List payloads) {
//        Glide.with(adapter.getRecyclerView().getContext())
//            .load(coachService.getPhoto())
//            .placeholder(R.drawable.ic_default_header)
//            .into(holder.itemGymHeader);
//        holder.itemGymName.setText(coachService.getName() + " | " + coachService.getBrand_name());
//        holder.itemRight.setVisibility(View.VISIBLE);
//        if (adapter.isSelected(position)) {
//            holder.itemRight.setImageResource(R.drawable.ic_radio_checked);
//        } else {
//            holder.itemRight.setImageResource(R.drawable.ic_radio_unchecked);
//        }
//        holder.qcIdentify.setVisibility(View.GONE);
//        holder.itemGymPhonenum.setVisibility(View.GONE);
//        holder.itemGymBrand.setVisibility(View.GONE);
//        if (hasPermission) {
//            holder.hiden.setVisibility(View.VISIBLE);
//        } else {
//            holder.hiden.setVisibility(View.GONE);
//        }
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    public static class CoachServiceVH extends FlexibleViewHolder {
//
//        @BindView(R.id.item_gym_header) ImageView itemGymHeader;
//        @BindView(R.id.item_gym_name) TextView itemGymName;
//        @BindView(R.id.item_is_personal) TextView itemIsPersonal;
//        @BindView(R.id.qc_identify) ImageView qcIdentify;
//        @BindView(R.id.item_gym_brand) TextView itemGymBrand;
//        @BindView(R.id.item_gym_phonenum) TextView itemGymPhonenum;
//        @BindView(R.id.item_right) ImageView itemRight;
//        @BindView(R.id.hiden) View hiden;
//
//        public CoachServiceVH(View view, FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//        }
//    }
//}
