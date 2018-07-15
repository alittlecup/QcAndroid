package cn.qingchengfit.items;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.R;
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
 * Created by Paper on 16/8/9.
 */
public class CommonNoDataItem extends AbstractFlexibleItem<CommonNoDataItem.CommonNodataVH> {

    @DrawableRes
    public int imgRes;
    public Drawable drawable;
    public String hintStr;
    public String titleStr;
    public String btnStr;

    private OnEmptyBtnClickListener onEmptyBtnClickListener;

    public CommonNoDataItem(int imgRes, String hintStr, String titleStr) {
        this.imgRes = imgRes;
        this.hintStr = hintStr;
        this.titleStr = titleStr;
    }

    public CommonNoDataItem(Drawable imgRes, String hintStr, String titleStr) {
        this.drawable = imgRes;
        this.hintStr = hintStr;
        this.titleStr = titleStr;
    }


    public CommonNoDataItem(int imgUrl, String hintStr) {
        this.imgRes = imgUrl;
        this.hintStr = hintStr;
    }

    public CommonNoDataItem(int imgRes, String hintStr, String titleStr, String btnStr,
                            OnEmptyBtnClickListener onEmptyBtnClickListener) {
        this.imgRes = imgRes;
        this.hintStr = hintStr;
        this.titleStr = titleStr;
        this.btnStr = btnStr;
        this.onEmptyBtnClickListener = onEmptyBtnClickListener;
    }

    public void setOnEmptyBtnClickListener(OnEmptyBtnClickListener onEmptyBtnClickListener) {
        this.onEmptyBtnClickListener = onEmptyBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CommonNoDataItem && ((CommonNoDataItem) o).imgRes == imgRes;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_common_no_data;
    }

    @Override
    public CommonNodataVH createViewHolder(View view, FlexibleAdapter adapter) {
        final CommonNodataVH vh = new CommonNodataVH(view, adapter);
        if (!CmStringUtils.isEmpty(btnStr)) {
            vh.btnEmptyPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onEmptyBtnClickListener != null) {
                        onEmptyBtnClickListener.onEmptyClickListener(vh.btnEmptyPage);
                    }
                }
            });
        }
        return vh;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, CommonNodataVH holder, int position,
                               List payloads) {
        holder.itemView.setMinimumHeight(
                MeasureUtils.getScreenHeight(holder.itemView.getResources()) - MeasureUtils.dpToPx(120f,
                        holder.itemView.getResources()));
        if (imgRes != 0) {
            holder.img.setVisibility(View.VISIBLE);
            holder.space.setVisibility(View.GONE);
            if (drawable != null) {
                holder.img.setImageDrawable(drawable);
            } else {
                holder.img.setImageResource(imgRes);
            }
        } else {
            holder.img.setVisibility(View.GONE);
            holder.space.setVisibility(View.VISIBLE);
        }
        holder.hint.setText(hintStr);
        if (TextUtils.isEmpty(titleStr)) {
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(titleStr);
        }

        if (!CmStringUtils.isEmpty(btnStr)) {
            holder.btnEmptyPage.setVisibility(View.VISIBLE);
            holder.btnEmptyPage.setText(btnStr);
        }
    }

    public interface OnEmptyBtnClickListener {
        void onEmptyClickListener(View v);
    }

    public static class CommonNodataVH extends FlexibleViewHolder {

        ImageView img;

        TextView hint;

        Space space;

        TextView tvTitle;

        TextView btnEmptyPage;

        public CommonNodataVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          img = (ImageView) view.findViewById(R.id.img);
          hint = (TextView) view.findViewById(R.id.hint);
          space = (Space) view.findViewById(R.id.space);
          tvTitle = (TextView) view.findViewById(R.id.tv_title);
          btnEmptyPage = (TextView) view.findViewById(R.id.btn_empty_page);
        }
    }
}
