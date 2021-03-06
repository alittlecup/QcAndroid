package cn.qingchengfit.staffkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.model.responese.FilterCardBean;
import cn.qingchengfit.staffkit.R;
import java.util.List;

/**
 * Created by fb on 2017/3/3.
 */

public class CardFilterLeftAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<FilterCardBean> dataList;
    private int lastPosition = -1;
    private OnLeftClickListener onLeftClickListener;

    public CardFilterLeftAdapter(Context context) {
        this.context = context;
    }

    public void setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
        this.onLeftClickListener = onLeftClickListener;
    }

    public void setDataList(List<FilterCardBean> dataList) {
        this.dataList = dataList;
    }

    @Override public int getCount() {
        return dataList.size();
    }

    @Override public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_card_filter, null);
            viewHolder.layoutFilterText = (RelativeLayout) view.findViewById(R.id.ll_filter_condition);
            viewHolder.layoutFilterText.setOnClickListener(this);
            viewHolder.imgConfirm = (ImageView) view.findViewById(R.id.image_filter_confirm);
            viewHolder.tvfilterName = (TextView) view.findViewById(R.id.tv_card_filter_item);
            viewHolder.dividerBottom = view.findViewById(R.id.list_divider_bottom);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.layoutFilterText.setBackgroundColor(context.getResources().getColor(R.color.bg_grey));
        viewHolder.dividerBottom.setVisibility(View.GONE);
        viewHolder.tvfilterName.setTextColor(context.getResources().getColor(R.color.qc_text_black));
        viewHolder.tvfilterName.setText(dataList.get(i).name);
        if (dataList.get(i).isChoosen) {
            viewHolder.tvfilterName.setTextColor(context.getResources().getColor(R.color.qc_allotsale_green));
            viewHolder.layoutFilterText.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        viewHolder.layoutFilterText.setTag(i);
        return view;
    }

    @Override public void onClick(View view) {
        int position = (int) view.getTag();
        onLeftClickListener.onSelected(position);
    }

    public interface OnLeftClickListener {
        void onSelected(int position);
    }

    class ViewHolder {
        TextView tvfilterName;
        ImageView imgConfirm;
        View dividerBottom;
        RelativeLayout layoutFilterText;
    }
}
