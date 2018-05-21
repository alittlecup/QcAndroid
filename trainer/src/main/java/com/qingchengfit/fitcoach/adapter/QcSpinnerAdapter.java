package com.qingchengfit.fitcoach.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.LoopView;
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
 * Created by Paper on 15/10/10 2015.
 */
public class QcSpinnerAdapter extends BaseAdapter {
  List<SpinData> datas;

  public QcSpinnerAdapter(List d) {
    this.datas = d;
  }

  @Override public int getCount() {
    return datas.size();
  }

  @Override public Object getItem(int position) {
    return datas.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    if (convertView == null) {
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    SpinData spinData = datas.get(position);
    viewHolder.spinnerTv.setText(spinData.text);
    if (TextUtils.isEmpty(spinData.color)) {
      viewHolder.spinnerIcon.setVisibility(View.GONE);
    } else {
      viewHolder.spinnerIcon.setVisibility(View.VISIBLE);
      viewHolder.spinnerIcon.setImageDrawable(new LoopView(spinData.color));
    }
    return convertView;
  }

  public static class SpinData {
    public String color;
    public String text;

    public SpinData(String color, String text) {
      this.color = color;
      this.text = text;
    }
  }

  /**
   * This class contains all butterknife-injected Views & Layouts from layout file 'spinner_item.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
   */
  static class ViewHolder {
    ImageView spinnerIcon;
    TextView spinnerTv;

    ViewHolder(View view) {
      spinnerIcon = view.findViewById(R.id.spinner_icon);
      spinnerTv = view.findViewById(R.id.spinner_tv);
    }
  }
}
