//package cn.qingchengfit.saasbase.items;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.staffkit.R;
//import com.bumptech.glide.Glide;
//import com.tencent.qcloud.timchat.widget.PhotoUtils;
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
// * Created by Paper on 16/7/28.
// */
//public class JackManageItem extends AbstractFlexibleItem<JackManageItem.JackManageVH> {
//
//    String img;
//
//    public JackManageItem(String img) {
//        this.img = img;
//        setDraggable(true);
//    }
//
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_jacket_manage;
//    }
//
//    @Override public JackManageVH createViewHolder(View view, FlexibleAdapter adapter) {
//        return new JackManageVH(view, adapter);
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, JackManageVH holder, int position, List payloads) {
//        Glide.with(holder.getContentView().getContext()).load(PhotoUtils.getSmall(img)).into(holder.img);
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    public static class JackManageVH extends FlexibleViewHolder {
//        @BindView(R.id.delete) ImageView delete;
//        @BindView(R.id.img) ImageView img;
//        @BindView(R.id.drag_tag) ImageView dragTag;
//        @BindView(R.id.container) LinearLayout container;
//        @BindView(R.id.text) TextView textView;
//
//        public JackManageVH(View view, final FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//            if (adapter.isHandleDragEnabled()) {
//                setDragHandleView(dragTag);
//            }
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    adapter.removeItem(getAdapterPosition());
//                }
//            });
//        }
//    }
//}
