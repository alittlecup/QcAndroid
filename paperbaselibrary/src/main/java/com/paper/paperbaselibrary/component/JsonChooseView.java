package com.paper.paperbaselibrary.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.paper.paperbaselibrary.R;
import com.paper.paperbaselibrary.bean.JsonChooseBean;

import java.util.ArrayList;
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
 * Created by Paper on 15/8/18 2015.
 */
public class JsonChooseView extends FrameLayout {

    private String data;

    public JsonChooseView(Context context) {
        super(context);
    }

    public JsonChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JsonChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int layerCount = parseData();
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.item_jsonchoose, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChooseAdapter adapter = new ChooseAdapter(getData(0, null));
        recyclerView.setAdapter(adapter);


    }

    private int parseData() {
        return 2;
    }

    /**
     * @param level 数据层级
     * @param key   数据上级
     * @return
     */

    private List<JsonChooseBean> getData(int level, String key) {

        return new ArrayList<JsonChooseBean>();
    }


    class ChooseViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemNext;

        public ChooseViewHolder(View itemView) {
            super(itemView);
            itemView = (TextView) itemView.findViewById(R.id.item_title);
            itemNext = (ImageView) itemView.findViewById(R.id.item_next);
        }
    }

    class ChooseAdapter extends RecyclerView.Adapter<ChooseViewHolder> {

        List<JsonChooseBean> datas;

        public ChooseAdapter(List<JsonChooseBean> d) {
            datas = d;
        }

        @Override
        public ChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChooseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jsonchoose, null));
        }

        @Override
        public void onBindViewHolder(ChooseViewHolder holder, int position) {
            holder.itemTitle.setText(datas.get(position).title);
            if (datas.get(position).hasNext)
                holder.itemNext.setVisibility(VISIBLE);
            else holder.itemNext.setVisibility(GONE);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }


}
