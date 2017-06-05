package cn.qingchengfit.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.io.File;
import java.util.List;

public class ImageChosenItem extends AbstractFlexibleItem<ImageChosenItem.ImageChosenVH> {

    public ImageChosenItem(boolean mode, String imgUrl,Context context) {
        this.mode = mode;
        this.imgUrl = imgUrl;
        mContext = context;
    }
    private Context mContext;
    private boolean mode;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_image_chosen;
    }

    @Override public ImageChosenVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ImageChosenVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ImageChosenVH holder, int position, List payloads) {
        Glide.with(mContext).load(new File(imgUrl)).into(holder.mImg);
        holder.mCheckBox.setVisibility(mode?View.GONE:View.VISIBLE);
        holder.mCheckBox.setChecked(adapter.isSelected(position));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ImageChosenVH extends FlexibleViewHolder {
        CheckBox mCheckBox;
        ImageView mImg;

        public ImageChosenVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mCheckBox = (CheckBox)view.findViewById(R.id.checkbox);
            mImg = (ImageView)view.findViewById(R.id.img);
        }

        @Override public void toggleActivation() {
            super.toggleActivation();
            mCheckBox.setChecked(mAdapter.isSelected(getAdapterPosition()));
        }
    }
}