package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/28 2015.
 */

public class ImageThreeTextAdapter extends RecyclerView.Adapter<ImageThreeTextAdapter.ImageTwoTextVH> implements View.OnClickListener {

    private List<ImageThreeTextBean> datas;
    private OnRecycleItemClickListener listener;

    public ImageThreeTextAdapter(List<ImageThreeTextBean> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public List<ImageThreeTextBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ImageThreeTextBean> datas) {
        this.datas = datas;
    }

    @Override public ImageTwoTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageTwoTextVH vh = null;
        if (viewType == 0) {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
        } else if (viewType == 1) {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_add, parent, false));
        } else {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
        }
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    public void freshData(List<ImageThreeTextBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override public void onBindViewHolder(ImageTwoTextVH holder, int position) {
        holder.itemView.setTag(position);
        ImageThreeTextBean bean = datas.get(position);
        holder.text1.setText(bean.text1);

        if (bean.type != 0) {
            holder.text2.setVisibility(View.GONE);
            holder.text3.setVisibility(View.GONE);
            holder.texticon.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
        } else {

            if (TextUtils.isEmpty(bean.text2)) {
                holder.text2.setVisibility(View.GONE);
            } else {
                holder.text2.setVisibility(View.VISIBLE);
                holder.text2.setText(bean.text2);
            }
            if (TextUtils.isEmpty(bean.text3)) {
                holder.text3.setVisibility(View.GONE);
            } else {
                holder.text3.setVisibility(View.VISIBLE);
                holder.text3.setText(bean.text3);
            }

            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(bean.imgUrl))
                .placeholder(R.drawable.img_default_course)
                .into(holder.img);
            if (bean.showIcon) {
                holder.texticon.setVisibility(View.VISIBLE);
            } else {
                holder.texticon.setVisibility(View.GONE);
            }
            if (bean.showRight) {
                holder.righticon.setVisibility(View.VISIBLE);
                if (bean.rightIcon != 0) {
                    holder.righticon.setImageResource(bean.rightIcon);
                    holder.text1.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                }
            } else {
                holder.righticon.setVisibility(View.GONE);
            }
        }
    }

    @Override public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    @Override public int getItemCount() {
        if (datas != null) {
            return datas.size();
        } else {
            return 0;
        }
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public static class ImageTwoTextVH extends RecyclerView.ViewHolder {

        @BindView(R.id.img) ImageView img;
        @BindView(R.id.text1) TextView text1;
        @BindView(R.id.texticon) ImageView texticon;
        @BindView(R.id.text2) TextView text2;
        @BindView(R.id.text3) TextView text3;
        @BindView(R.id.righticon) ImageView righticon;

        public ImageTwoTextVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
