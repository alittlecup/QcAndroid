package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/11/20 2015.
 */
public class BaseInfoViewHolder extends RecyclerView.ViewHolder {
    public ImageView img;
    public TextView title;
    public TextView content;

    public BaseInfoViewHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.baseinfo_item_icon);
        title = (TextView) itemView.findViewById(R.id.baseinfo_item_label);
        content = (TextView) itemView.findViewById(R.id.baseinfo_item_content);
    }
}
