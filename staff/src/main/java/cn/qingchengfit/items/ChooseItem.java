//package cn.qingchengfit.items;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.staffkit.R;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import eu.davidea.viewholders.FlexibleViewHolder;
//import java.util.List;
//
//public class ChooseItem extends AbstractFlexibleItem<ChooseItem.ChooseVH> {
//
//    public String textStr;
//
//    public ChooseItem(String text) {
//        this.textStr = text;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_choose;
//    }
//
//    @Override public ChooseVH createViewHolder(View view, FlexibleAdapter adapter) {
//        return new ChooseVH(view, adapter);
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseVH holder, int position, List payloads) {
//        holder.text.setText(textStr);
//        //        adapter.animateView(holder.itemView, position, adapter.isSelected(position));
//        if (adapter.isSelected(position)) {
//            holder.chooseImg.setVisibility(View.VISIBLE);
//        } else {
//            holder.chooseImg.setVisibility(View.GONE);
//        }
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    public class ChooseVH extends FlexibleViewHolder {
//        @BindView(R.id.text) TextView text;
//        @BindView(R.id.choose_img) ImageView chooseImg;
//
//        public ChooseVH(View view, FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//        }
//    }
//}