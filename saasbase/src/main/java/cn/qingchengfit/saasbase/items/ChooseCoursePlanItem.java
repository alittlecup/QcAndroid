//package cn.qingchengfit.saasbase.items;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.model.responese.CoursePlan;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.staffkit.constant.Configs;
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
// * Created by Paper on 16/8/2.
// */
//public class ChooseCoursePlanItem extends AbstractFlexibleItem<ChooseCoursePlanItem.ChooseCoursePlanVH> {
//
//    public boolean chosen;
//    public CoursePlan coursePlan;
//
//    public ChooseCoursePlanItem(boolean chosen, CoursePlan coursePlan) {
//        this.chosen = chosen;
//        this.coursePlan = coursePlan;
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_course_plan;
//    }
//
//    @Override public ChooseCoursePlanVH createViewHolder(View view, FlexibleAdapter adapter) {
//        return new ChooseCoursePlanVH(view, adapter);
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseCoursePlanVH holder, int position, List payloads) {
//        holder.name.setText(coursePlan.getName());
//        if (coursePlan.getTags() != null) {
//            String tags = "";
//            for (int i = 0; i < coursePlan.getTags().size(); i++) {
//                tags = tags.concat(coursePlan.getTags().get(i)).concat(i == coursePlan.getTags().size() - 1 ? "" : Configs.SEPARATOR);
//            }
//            holder.content.setVisibility(View.VISIBLE);
//            holder.content.setText(tags);
//        } else {
//            holder.content.setVisibility(View.GONE);
//        }
//        holder.chosen.setImageResource(chosen ? R.drawable.ic_green_right : R.drawable.ic_arrow_right);
//    }
//
//    public static class ChooseCoursePlanVH extends FlexibleViewHolder {
//        @BindView(R.id.name) TextView name;
//        @BindView(R.id.content) TextView content;
//        @BindView(R.id.chosen) ImageView chosen;
//
//        public ChooseCoursePlanVH(View view, FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//        }
//    }
//}
