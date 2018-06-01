package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;

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
 * Created by Paper on 16/2/1 2016.
 */
public class ImageTextVerticalVH extends RecyclerView.ViewHolder {

  public TextView tv;
  public ImageView img;

  public ImageTextVerticalVH(View itemView) {
    super(itemView);
    tv = itemView.findViewById(R.id.text);
    img = itemView.findViewById(R.id.img);
  }
}