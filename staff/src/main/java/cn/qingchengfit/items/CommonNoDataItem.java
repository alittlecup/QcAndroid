//package cn.qingchengfit.items;
//
//import android.support.annotation.DrawableRes;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.staffkit.R;
//import cn.qingchengfit.utils.MeasureUtils;
//import cn.qingchengfit.utils.StringUtils;
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
// * Created by Paper on 16/8/9.
// */
//public class CommonNoDataItem extends AbstractFlexibleItem<CommonNoDataItem.CommonNodataVH> {
//
//    @DrawableRes public int imgRes;
//    public String hintStr;
//    public String titleStr;
//    public String btnStr;
//
//    private OnEmptyBtnClickListener onEmptyBtnClickListener;
//
//    public CommonNoDataItem(int imgRes, String hintStr, String titleStr) {
//        this.imgRes = imgRes;
//        this.hintStr = hintStr;
//        this.titleStr = titleStr;
//    }
//
//    public CommonNoDataItem(int imgUrl, String hintStr) {
//        this.imgRes = imgUrl;
//        this.hintStr = hintStr;
//    }
//
//    public CommonNoDataItem(int imgRes, String hintStr, String titleStr, String btnStr, OnEmptyBtnClickListener onEmptyBtnClickListener) {
//        this.imgRes = imgRes;
//        this.hintStr = hintStr;
//        this.titleStr = titleStr;
//        this.btnStr = btnStr;
//        this.onEmptyBtnClickListener = onEmptyBtnClickListener;
//    }
//
//    public void setOnEmptyBtnClickListener(OnEmptyBtnClickListener onEmptyBtnClickListener) {
//        this.onEmptyBtnClickListener = onEmptyBtnClickListener;
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_common_no_data;
//    }
//
//    @Override public CommonNodataVH createViewHolder(View view, FlexibleAdapter adapter) {
//        final CommonNodataVH vh = new CommonNodataVH(view, adapter);
//        if (!StringUtils.isEmpty(btnStr)) {
//            vh.btnEmptyPage.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View view) {
//                    if (onEmptyBtnClickListener != null) {
//                        onEmptyBtnClickListener.onEmptyClickListener(vh.btnEmptyPage);
//                    }
//                }
//            });
//        }
//        return vh;
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, CommonNodataVH holder, int position, List payloads) {
//        //        Glide.with(App.context).load(imgRes).into(holder.img);
//        holder.itemView.setMinimumHeight(
//            MeasureUtils.getScreenHeight(holder.itemView.getResources()) - MeasureUtils.dpToPx(120f, holder.itemView.getResources()));
//        if (imgRes != 0) holder.img.setImageResource(imgRes);
//        holder.hint.setText(hintStr);
//        if (StringUtils.isEmpty(titleStr)) {
//            holder.tvTitle.setVisibility(View.GONE);
//        } else {
//            holder.tvTitle.setVisibility(View.VISIBLE);
//            holder.tvTitle.setText(titleStr);
//        }
//
//        if (!StringUtils.isEmpty(btnStr)) {
//            holder.btnEmptyPage.setVisibility(View.VISIBLE);
//            holder.btnEmptyPage.setText(btnStr);
//        }
//    }
//
//    public interface OnEmptyBtnClickListener {
//        void onEmptyClickListener(View v);
//    }
//
//    public static class CommonNodataVH extends FlexibleViewHolder {
//        @BindView(R.id.img) ImageView img;
//        @BindView(R.id.hint) TextView hint;
//        @BindView(R.id.tv_title) TextView tvTitle;
//        @BindView(R.id.btn_empty_page) TextView btnEmptyPage;
//
//        public CommonNodataVH(View view, FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//            btnEmptyPage.setVisibility(View.GONE);
//        }
//    }
//}
