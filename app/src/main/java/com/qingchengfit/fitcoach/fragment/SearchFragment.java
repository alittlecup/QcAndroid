package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddGymBean;
import com.qingchengfit.fitcoach.http.bean.QcSearchOrganResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends android.support.v4.app.Fragment {
    public static final String TAG = SearchFragment.class.getName();
    public static final int TYPE_GYM = 0;
    public static final int TYPE_ORGANASITON = 1;
    public String keyword;
    @Bind(R.id.searchview_et)
    EditText searchviewEt;
    @Bind(R.id.searchview_clear)
    ImageView searchviewClear;
    @Bind(R.id.searchview_cancle)
    Button searchviewCancle;
    //    @Bind(R.id.search_resultlist)
//    SearchListView searchResultlist;
    @Bind(R.id.searchresult_btn)
    Button searchresultBtn;
    @Bind(R.id.searchresult_none)
    LinearLayout searchresultNone;
    @Bind(R.id.searchresult_rv)
    RecyclerView searchresultRv;
    SearchResultAdapter adapter;
    @Bind(R.id.search_hottable)
    TextView searchHottable;
    private int type;
    private List<String> strings;
    private InternalSearchHandler handler;
    private SearchInterface searchListener;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int s) {

        Bundle args = new Bundle();
        args.putInt("type", s);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new InternalSearchHandler(getContext());
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        if (type == TYPE_ORGANASITON) {
            searchviewEt.setHint("搜索机构(至少输入三个字符)");
            searchresultBtn.setText("添加主办机构");
        } else if (type == TYPE_GYM) {
            searchviewEt.setHint("搜索健身房(至少输入三个字符)");
            searchresultBtn.setText("添加健身房");
        }

        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchviewClear == null)
                    return;
                if (s.length() > 0) {
                    searchviewClear.setVisibility(View.VISIBLE);
                } else {
                    //推荐
                    searchviewClear.setVisibility(View.GONE);
                    initRv();
                    searchresultRv.setVisibility(View.VISIBLE);
                }
                LogUtil.e(s.toString());
                keyword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

                LogUtil.e(s.toString());
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", s.toString());
                msg.setData(bundle);
                handler.sendMessageDelayed(msg, 1000);

            }
        });


        searchresultRv.setLayoutManager(new LinearLayoutManager(getContext()));
        strings = new ArrayList<>();

        adapter = new SearchResultAdapter(strings);
        searchresultRv.setAdapter(adapter);
        initRv();
        searchresultRv.setVisibility(View.VISIBLE);
        return view;
    }

    public void initRv() {
        Map<String, String> params = new HashMap<>();
        params.put("is_hot", "1");

        if (type == TYPE_GYM) {
            searchHottable.setText("热门健身房");
            QcCloudClient.getApi().getApi.qcHotGym(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            qcSerachGymRepsonse -> {
                                if (searchHottable != null) {
                                    strings.clear();
                                    if (qcSerachGymRepsonse.getData().getGym().size() > 0) {
                                        searchHottable.setText("热门健身房");
                                        searchresultRv.setVisibility(View.VISIBLE);
                                        for (AddGymBean addGymBean : qcSerachGymRepsonse.getData().getGym()) {
                                            strings.add(addGymBean.name);
                                        }
                                        strings.add("");
                                        adapter.setListener(((v, pos) -> {
                                            if (pos != adapter.getItemCount() - 1)
                                                searchListener.onSearchResult(100, Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id), qcSerachGymRepsonse.getData().getGym().get(pos).name);
                                        }));
                                        adapter.notifyDataSetChanged();
                                    }
//                                    else searchresultRv.setVisibility(View.GONE);
                                }
                                ;
                            }, throwable -> {
                            }, () -> {
                            });
        } else if (type == TYPE_ORGANASITON) {
            searchHottable.setText("热门机构");
            QcCloudClient.getApi().getApi.qcHotOrganization(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            qcSearchOrganResponse -> {
                                if (searchHottable != null) {
                                    strings.clear();
                                    if (qcSearchOrganResponse.getData().getOrganizations().size() > 0) {
                                        searchHottable.setText("热门机构");
                                        searchresultRv.setVisibility(View.VISIBLE);
                                        for (QcSearchOrganResponse.DataEntity.OrganizationsEntity addGymBean : qcSearchOrganResponse.getData().getOrganizations()) {
                                            strings.add(addGymBean.getName());
                                        }
                                        strings.add("");
                                        adapter.setListener(((v, pos) -> {
                                            if (pos != adapter.getItemCount() - 1)
                                                searchListener.onSearchResult(100, qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(), qcSearchOrganResponse.getData().getOrganizations().get(pos).getName());
                                        }));
                                        adapter.notifyDataSetChanged();
                                    }
//                                    else searchresultRv.setVisibility(View.GONE);
                                }
                                ;
                            }, throwable -> {
                            }, () -> {
                            }
                    );
        }


    }

    @OnClick(R.id.searchview_clear)
    public void onClear() {
        searchviewEt.setText("");
    }

    @OnClick(R.id.searchresult_btn)
    public void onAdd() {
        if (type == TYPE_GYM)
            getFragmentManager().beginTransaction().replace(R.id.search_fraglayout, new AddGymFragment()).addToBackStack(null).commit();
        else if (type == TYPE_ORGANASITON)
            getFragmentManager().beginTransaction().replace(R.id.search_fraglayout, new AddOganasitionFragment())
                    .addToBackStack(null).commit();

    }


    @OnClick(R.id.searchview_cancle)
    public void onCancle() {
        searchListener.onSearchResult(-100, 0, "");
    }

    /**
     * 搜索健身房
     *
     * @param keyword
     */
    private void searchResult(String keyword) {
        if (TextUtils.isEmpty(keyword))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("q", keyword);
        if (type == TYPE_GYM) {

            QcCloudClient.getApi().getApi.qcSearchGym(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            qcSerachGymRepsonse -> {
                                if (searchHottable != null) {
                                    strings.clear();
                                    if (qcSerachGymRepsonse.getData().getGym().size() > 0) {
                                        searchHottable.setText("搜索结果");
                                        searchresultRv.setVisibility(View.VISIBLE);
                                        for (AddGymBean addGymBean : qcSerachGymRepsonse.getData().getGym()) {
                                            strings.add(addGymBean.name);
                                        }
                                        strings.add("添加健身房");
                                        adapter.setListener(((v, pos) -> {
                                            if (pos == adapter.getItemCount() - 1) {
                                                onAdd();
                                            } else
                                                searchListener.onSearchResult(100, Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id), qcSerachGymRepsonse.getData().getGym().get(pos).name);
                                        }));
                                        adapter.notifyDataSetChanged();
                                    } else searchresultRv.setVisibility(View.GONE);
                                }
                                ;
                            }, throwable -> {
                            }, () -> {
                            });
        } else if (type == TYPE_ORGANASITON) {
            QcCloudClient.getApi().getApi.qcSearchOrganization(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            qcSearchOrganResponse ->
                            {
                                if (searchHottable != null) {
                                    strings.clear();
                                    if (qcSearchOrganResponse.getData().getOrganizations().size() > 0) {
                                        searchHottable.setText("搜索结果");
                                        searchresultRv.setVisibility(View.VISIBLE);
                                        for (QcSearchOrganResponse.DataEntity.OrganizationsEntity addGymBean : qcSearchOrganResponse.getData().getOrganizations()) {
                                            strings.add(addGymBean.getName());
                                        }
                                        strings.add("添加主办机构");
                                        adapter.setListener(((v, pos) -> {

                                            if (pos == adapter.getItemCount() - 1) {
                                                onAdd();
                                            } else
                                                searchListener.onSearchResult(100, qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(), qcSearchOrganResponse.getData().getOrganizations().get(pos).getName());
                                        }));
                                        adapter.notifyDataSetChanged();
                                    } else searchresultRv.setVisibility(View.GONE);

                                }
                            }, throwable -> {
                            }, () -> {
                            }
                    );
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        adapter.setListener(listener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchInterface) {
            searchListener = (SearchInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchListener = null;
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

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultVH> implements View.OnClickListener {


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
            SearchResultVH holder;
            if (viewType == 0) {
                holder = new SearchResultVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
                holder.itemView.setOnClickListener(this);
            } else {
                holder = new SearchResultVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_btn, parent, false));
                holder.itemView.setOnClickListener(this);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(SearchResultVH holder, int position) {
            holder.itemView.setTag(position);
            String s = datas.get(position);
            if (TextUtils.isEmpty(s))
                holder.itemText.setVisibility(View.GONE);
            else holder.itemText.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(keyword) && s.contains(keyword)) {

                SpannableString ss = new SpannableString(s);
                int pos = s.indexOf(keyword);

                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)), pos, pos + keyword.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.itemText.setText(ss);
            } else holder.itemText.setText(s);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1)
                return 1;
            else return 0;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            LogUtil.e("onclick");
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class InternalHandler extends Handler {
        WeakReference<Context> context;

        InternalHandler(Context c) {
            context = new WeakReference<Context>(c);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.getData().getString("keyword");
            if (s != null && keyword.length() > 0 && s.equals(keyword))
                searchResult(keyword);
        }
    }

    public class InternalSearchHandler extends Handler {
        WeakReference<Context> context;

        InternalSearchHandler(Context c) {
            context = new WeakReference<Context>(c);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.getData().getString("keyword");
            if (s.equals(keyword) && keyword.length() > 2)
                searchResult(keyword);
        }
    }
}
