package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/12 2016.
 */
public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ImageGridVh> implements View.OnClickListener {

    private List<BodyTestBody.Photo> datas;
    private OnRecycleItemClickListener listener;
    private boolean isEditable = false;

    public ImageGridAdapter(List<BodyTestBody.Photo> datas) {
        this.datas = datas;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
        this.notifyDataSetChanged();
    }

    public List<BodyTestBody.Photo> getDatas() {
        return datas;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public ImageGridVh onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageGridVh vh = new ImageGridVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_img, parent, false));
        vh.itemView.setOnClickListener(this);
        vh.delete.setOnClickListener(this);
        return vh;
    }

    public void refresh(List<BodyTestBody.Photo> photos) {
        this.datas.clear();
        this.datas.addAll(photos);
        notifyDataSetChanged();
    }

    @Override public void onBindViewHolder(ImageGridVh holder, int position) {
        holder.itemView.setTag(position);
        holder.delete.setTag(position);
        if (isEditable && position == datas.size()) {
            holder.delete.setVisibility(View.GONE);
            holder.img.setScaleType(ImageView.ScaleType.CENTER);
            //            Glide.with(App.context).load(R.drawable.ic_add_image).into(holder.img);
            holder.img.setImageResource(R.drawable.ic_add_image);
            holder.img.setBackgroundResource(R.drawable.bg_rect);
        } else {
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(datas.get(position).photo))
                .placeholder(R.drawable.img_loadingimage)
                .into(holder.img);
            if (isEditable) {
                holder.delete.setVisibility(View.VISIBLE);
            } else {
                holder.delete.setVisibility(View.GONE);
            }
        }
    }

    @Override public int getItemCount() {
        return datas.size() + (isEditable ? 1 : 0);
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class ImageGridVh extends RecyclerView.ViewHolder {

	ImageView img;
	ImageView delete;
	ProgressBar progress;

        public ImageGridVh(View itemView) {
            super(itemView);
          img = (ImageView) itemView.findViewById(R.id.img);
          delete = (ImageView) itemView.findViewById(R.id.delete);
          progress = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
