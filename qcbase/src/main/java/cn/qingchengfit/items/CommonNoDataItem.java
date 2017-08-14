package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
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

    @DrawableRes public int imgRes;
    public String hintStr;
    public String titleStr;
  public String btnStr;

  private OnEmptyBtnClickListener onEmptyBtnClickListener;

    public CommonNoDataItem(int imgRes, String hintStr, String titleStr) {
        this.imgRes = imgRes;
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

    @Override public boolean equals(Object o) {
        return o instanceof CommonNoDataItem && ((CommonNoDataItem) o).imgRes == imgRes;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_common_no_data;
    }

    @Override public CommonNodataVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
      final CommonNodataVH vh =
          new CommonNodataVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
      if (!CmStringUtils.isEmpty(btnStr)) {
        vh.btnEmptyPage.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            if (onEmptyBtnClickListener != null) {
              onEmptyBtnClickListener.onEmptyClickListener(vh.btnEmptyPage);
            }
          }
        });
      }
      return vh;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CommonNodataVH holder, int position, List payloads) {
        holder.itemView.setMinimumHeight(
            MeasureUtils.getScreenHeight(holder.itemView.getResources()) - MeasureUtils.dpToPx(120f, holder.itemView.getResources()));
        if (imgRes != 0){
          holder.img.setVisibility(View.VISIBLE);
          holder.space.setVisibility(View.GONE);
          holder.img.setImageResource(imgRes);
        }else {
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
        @BindView(R2.id.img) ImageView img;
        @BindView(R2.id.hint) TextView hint;
        @BindView(R2.id.space) Space space;
      @BindView(R2.id.tv_title) TextView tvTitle;
      @BindView(R2.id.btn_empty_page) TextView btnEmptyPage;

        public CommonNodataVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
