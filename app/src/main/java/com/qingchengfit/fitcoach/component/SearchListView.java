package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

import java.util.ArrayList;
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
 * Created by Paper on 15/9/9 2015.
 */
public class SearchListView extends RelativeLayout {

    public String keyword;
    @Bind(R.id.searchresult_btn)
    Button searchresultBtn;
    @Bind(R.id.searchresult_none)
    LinearLayout searchresultNone;
    @Bind(R.id.searchresult_rv)
    RecyclerView searchresultRv;
    SearchResultAdapter adapter;
    private List<String> strings;

    public SearchListView(Context context) {
        super(context);
        init(context);
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setKeyword(String keyword, List<String> result) {
        this.keyword = keyword;
        this.strings = result;
        if (result.size() > 0) {
            adapter.notifyDataSetChanged();
            searchresultRv.setVisibility(VISIBLE);
            searchresultNone.setVisibility(GONE);
        } else {
            searchresultRv.setVisibility(GONE);
            searchresultNone.setVisibility(VISIBLE);
        }
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_search_result, this, false);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        searchresultRv.setLayoutManager(new LinearLayoutManager(getContext()));
        strings = new ArrayList<>();
        strings.add("中国建设协会");
        strings.add("China fit 健身");
        strings.add("说好的健身呢");
        strings.add("必须要包含健身");
        adapter = new SearchResultAdapter(new ArrayList<>());
        searchresultRv.setAdapter(adapter);

    }

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        adapter.setListener(listener);
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class SearchResultVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_text)
        TextView itemText;

        public SearchResultVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultVH> implements OnClickListener {


        private List<String> datas;
        private OnRecycleItemClickListener listener;

        public SearchResultAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public SearchResultVH onCreateViewHolder(ViewGroup parent, int viewType) {
            SearchResultVH holder = new SearchResultVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(SearchResultVH holder, int position) {
            holder.itemView.setTag(position);
            String s = datas.get(position);
            if (TextUtils.isEmpty(keyword) && s.contains(s)) {
                SpannableString ss = new SpannableString(s);
                int pos = s.indexOf(keyword);
                ss.setSpan(new ForegroundColorSpan(Color.CYAN), pos, pos + keyword.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.itemText.setText(ss);
            } else holder.itemText.setText(s);
        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }


}
