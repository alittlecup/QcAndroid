package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
 * Created by Paper on 16/1/12 2016.
 */
public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ImageGridVh>
    implements View.OnClickListener{

    private List<String> datas;
    private OnRecycleItemClickListener listener;


    public ImageGridAdapter(List<String> datas) {
        this.datas = datas;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ImageGridVh onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageGridVh vh = new ImageGridVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_img, parent, false));
        vh.itemView.setOnClickListener(this);
        vh.delete.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageGridVh holder, int position) {
        holder.itemView.setTag(position);

        Glide.with(App.AppContex).load(datas.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null)
            listener.onItemClick(v,(int)v.getTag());
    }


    public class ImageGridVh extends RecyclerView.ViewHolder {

        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.delete)
        ImageView delete;
        @Bind(R.id.progress)
        ProgressBar progress;

        public ImageGridVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
