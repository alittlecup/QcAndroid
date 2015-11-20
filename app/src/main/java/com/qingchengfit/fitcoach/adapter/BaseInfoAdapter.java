package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;

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
 * Created by Paper on 15/11/20 2015.
 */
public class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoViewHolder> {

    private List<BaseInfoBean> datas;

    public BaseInfoAdapter(List<BaseInfoBean> datas) {
        this.datas = datas;
    }

    @Override
    public BaseInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseInfoViewHolder holder = new BaseInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baseinfo, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseInfoViewHolder holder, int position) {
        if (datas != null && datas.size() > position) {
            BaseInfoBean baseInfoBean = datas.get(position);
            holder.title.setText(baseInfoBean.label);
            holder.content.setText(baseInfoBean.content);
            Glide.with(App.AppContex).load(baseInfoBean.icon).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
