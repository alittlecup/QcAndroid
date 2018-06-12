package com.qingchengfit.fitcoach.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import cn.qingchengfit.bean.SearchItemBean;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.recruit.views.organization.AddGymBean;
import cn.qingchengfit.recruit.views.organization.QcSearchOrganResponse;
import cn.qingchengfit.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.activity.ExpChooseBrandActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends android.support.v4.app.Fragment {
    public static final String TAG = SearchFragment.class.getName();
    public static final int TYPE_GYM = 0;
    public static final int TYPE_ORGANASITON = 1;
    public static final int RESULT_BRAND = 2;

    public String keyword;
	EditText searchviewEt;
	ImageView searchviewClear;
	Button searchresultBtn;
	LinearLayout searchresultNone;
	RecyclerView searchresultRv;
    SearchResultAdapter adapter;
	TextView searchHottable;
	Toolbar toolbar;
	TextView searchHint;
	TextView searchresultHint;
    private int type;
    private List<SearchItemBean> strings;
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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new InternalSearchHandler(getContext());
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_search_recruit, container, false);
      searchviewEt = (EditText) view.findViewById(R.id.searchview_et);
      searchviewClear = (ImageView) view.findViewById(R.id.searchview_clear);
      searchresultBtn = (Button) view.findViewById(R.id.searchresult_btn);
      searchresultNone = (LinearLayout) view.findViewById(R.id.searchresult_none);
      searchresultRv = (RecyclerView) view.findViewById(R.id.searchresult_rv);
      searchHottable = (TextView) view.findViewById(R.id.search_hottable);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      searchHint = (TextView) view.findViewById(R.id.search_hint);
      searchresultHint = (TextView) view.findViewById(R.id.searchresult_hint);

      view.findViewById(R.id.searchview_clear).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClear();
        }
      });
      view.findViewById(R.id.searchresult_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAdd();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (type == TYPE_ORGANASITON) {
            toolbar.setTitle("添加资质认证");
            searchviewEt.setHint("搜索机构(至少输入三个字)");
            searchHint.setText("请先选择主办机构");
            searchresultBtn.setText("添加主办机构");
            searchresultHint.setText("没有匹配的机构\n您可以添加该机构");
        } else if (type == TYPE_GYM) {
            toolbar.setTitle("添加工作经历");
            searchHint.setText("请先选择健身房");
            searchviewEt.setHint("搜索健身房(至少输入三个字)");
            searchresultBtn.setText("添加健身房");
            searchresultHint.setText("没有匹配的健身房\n您可以添加该健身房");
        }

        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchviewClear == null) return;
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

            @Override public void afterTextChanged(Editable s) {

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
        LogUtil.e("initRv");
        if (type == TYPE_GYM) {
            searchHottable.setText("热门健身房");
            QcCloudClient.getApi().getApi.qcHotGym(params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(qcSerachGymRepsonse -> {
                if (searchHottable != null) {
                    strings.clear();
                    if (qcSerachGymRepsonse.getData().getGym().size() > 0) {
                        searchHottable.setText("热门健身房");
                        searchresultRv.setVisibility(View.VISIBLE);
                        for (AddGymBean addGymBean : qcSerachGymRepsonse.getData().getGym()) {
                            StringBuffer ss = new StringBuffer();
                            if (addGymBean.district != null && addGymBean.district.city != null && !TextUtils.isEmpty(
                                addGymBean.district.city.name)) {
                                ss.append(addGymBean.district.city.name).append("  ");
                                }
                            ss.append(addGymBean.brand_name);
                            strings.add(new SearchItemBean(addGymBean.name, addGymBean.photo, ss.toString(), addGymBean.is_authenticated));
                        }
                        strings.add(new SearchItemBean());
                        adapter.setListener(((v, pos) -> {
                            if (pos != adapter.getItemCount() - 1) {
                                searchListener.onSearchResult(100, Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id),
                                    qcSerachGymRepsonse.getData().getGym().get(pos).name,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).brand_name,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).photo,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).is_authenticated);
                            }
                        }));
                        adapter.notifyDataSetChanged();
                    }
                    //                                    else searchresultRv.setVisibility(View.GONE);
                }
                ;
            },new HttpThrowable(), () -> {
            });
        } else if (type == TYPE_ORGANASITON) {
            searchHottable.setText("热门机构");
            QcCloudClient.getApi().getApi.qcHotOrganization(params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcSearchOrganResponse -> {
                    if (searchHottable != null) {
                        strings.clear();
                        if (qcSearchOrganResponse.getData().getOrganizations().size() > 0) {
                            searchHottable.setText("热门机构");
                            searchresultRv.setVisibility(View.VISIBLE);
                            for (QcSearchOrganResponse.DataEntity.OrganizationsEntity addGymBean : qcSearchOrganResponse.getData()
                                .getOrganizations()) {
                                strings.add(
                                    new SearchItemBean(addGymBean.getName(), addGymBean.getPhoto(), "联系方式:" + addGymBean.getContact(),
                                        addGymBean.is_authenticated()));
                            }
                            strings.add(new SearchItemBean());
                            adapter.setListener(((v, pos) -> {
                                if (pos != adapter.getItemCount() - 1) {

                                    searchListener.onSearchResult(100, qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(),
                                        qcSearchOrganResponse.getData().getOrganizations().get(pos).getName(),
                                        qcSearchOrganResponse.getData().getOrganizations().get(pos).getContact(),
                                        qcSearchOrganResponse.getData().getOrganizations().get(pos).getPhoto(),
                                        qcSearchOrganResponse.getData().getOrganizations().get(pos).is_authenticated());
                                }
                            }));
                            adapter.notifyDataSetChanged();
                        }
                    }
                    ;
                },new HttpThrowable(), () -> {
                });
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_BRAND) {
                Brand brand = (Brand) IntentUtils.getParcelable(data);
                getFragmentManager().beginTransaction()
                    .add(R.id.search_fraglayout, new AddGymFragmentBuilder(brand.getPhoto(), brand.getName(), brand.getId()).build())
                    .addToBackStack(null)
                    .commit();
            }
        }
    }

 public void onClear() {
        searchviewEt.setText("");
    }

 public void onAdd() {
        if (type == TYPE_GYM) {
            Intent toChooseBrand = new Intent(getActivity(), ExpChooseBrandActivity.class);

            startActivityForResult(toChooseBrand, RESULT_BRAND);
        } else if (type == TYPE_ORGANASITON) {
            getFragmentManager().beginTransaction().add(R.id.search_fraglayout, new AddOganasitionFragment()).addToBackStack(null).commit();
        }
    }

    //    @OnClick(R.id.searchview_cancle)
    //    public void onCancle() {
    //        searchListener.onSearchResult(-100, 0, "");
    //    }

    /**
     * 搜索健身房
     */
    private void searchResult(String keyword) {
        if (TextUtils.isEmpty(keyword)) return;
        Map<String, String> params = new HashMap<>();
        params.put("q", keyword);
        if (type == TYPE_GYM) {

          QcCloudClient.getApi().getApi.qcSearchGym(params)
              .onBackpressureBuffer()
              .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread()).subscribe(qcSerachGymRepsonse -> {
                if (searchHottable != null) {
                    strings.clear();
                    if (qcSerachGymRepsonse.getData().getGym().size() > 0) {
                        searchHottable.setText("搜索结果");
                        searchresultRv.setVisibility(View.VISIBLE);
                        for (AddGymBean addGymBean : qcSerachGymRepsonse.getData().getGym()) {
                            StringBuffer city = new StringBuffer();
                            if (addGymBean.district != null && addGymBean.district.city != null && !TextUtils.isEmpty(
                                addGymBean.district.city.name)) {
                                city.append(addGymBean.district.city.name).append("    ");
                            }
                            city.append(addGymBean.brand_name);
                            LogUtil.e("city:"
                                + addGymBean.district.toString()
                                + "   brand:"
                                + addGymBean.brand_name
                                + "  au:"
                                + addGymBean.is_authenticated);
                            //                                            city += addGymBean.brand_name;
                            strings.add(
                                new SearchItemBean(addGymBean.name, addGymBean.photo, city.toString(), addGymBean.is_authenticated));
                        }
                        strings.add(new SearchItemBean("添加健身房"));
                        adapter.setListener(((v, pos) -> {
                            if (pos == adapter.getItemCount() - 1) {
                                onAdd();
                            } else {
                                searchListener.onSearchResult(100, Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id),
                                    qcSerachGymRepsonse.getData().getGym().get(pos).name,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).brand_name,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).photo,
                                    qcSerachGymRepsonse.getData().getGym().get(pos).is_authenticated);
                            }
                        }));
                        adapter.notifyDataSetChanged();
                    } else {
                        searchresultRv.setVisibility(View.GONE);
                    }
                }
                ;
            }, new HttpThrowable(), () -> {
            });
        } else if (type == TYPE_ORGANASITON) {
          QcCloudClient.getApi().getApi.qcSearchOrganization(params)
              .onBackpressureBuffer()
              .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread()).subscribe(qcSearchOrganResponse -> {
                if (searchHottable != null) {
                    strings.clear();
                    if (qcSearchOrganResponse.getData().getOrganizations().size() > 0) {
                        searchHottable.setText("搜索结果");
                        searchresultRv.setVisibility(View.VISIBLE);
                        for (QcSearchOrganResponse.DataEntity.OrganizationsEntity addGymBean : qcSearchOrganResponse.getData()
                            .getOrganizations()) {
                            strings.add(new SearchItemBean(addGymBean.getName(), addGymBean.getPhoto(), "联系方式:" + addGymBean.getContact(),
                                addGymBean.is_authenticated()));
                        }
                        strings.add(new SearchItemBean("添加主办机构"));
                        adapter.setListener(((v, pos) -> {

                            if (pos == adapter.getItemCount() - 1) {
                                onAdd();
                            } else {
                                searchListener.onSearchResult(100, qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(),
                                    qcSearchOrganResponse.getData().getOrganizations().get(pos).getName(),
                                    qcSearchOrganResponse.getData().getOrganizations().get(pos).getContact(),
                                    qcSearchOrganResponse.getData().getOrganizations().get(pos).getPhoto(),
                                    qcSearchOrganResponse.getData().getOrganizations().get(pos).is_authenticated());
                            }
                            //                                                searchListener.onSearchResult(100, qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(), qcSearchOrganResponse.getData().getOrganizations().get(pos).getName());
                        }));
                        adapter.notifyDataSetChanged();
                    } else {
                        searchresultRv.setVisibility(View.GONE);
                    }
                }
            }, new HttpThrowable(), () -> {
            });
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        adapter.setListener(listener);
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchInterface) {
            searchListener = (SearchInterface) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        searchListener = null;
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View v, int pos);
    }

    public static class SearchResultVH extends RecyclerView.ViewHolder {
	TextView itemText;
	ImageView imageView;
	ImageView qcIdentify;
	TextView itemAddress;

        public SearchResultVH(View view) {
            super(view);
          itemText = (TextView) view.findViewById(R.id.item_text);
          imageView = (ImageView) view.findViewById(R.id.item_img);
          qcIdentify = (ImageView) view.findViewById(R.id.item_qc_identify);
          itemAddress = (TextView) view.findViewById(R.id.item_address);

        }
    }

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultVH> implements View.OnClickListener {

        private List<SearchItemBean> datas;
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

        @Override public SearchResultVH onCreateViewHolder(ViewGroup parent, int viewType) {
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

        @Override public void onBindViewHolder(SearchResultVH holder, int position) {
            holder.itemView.setTag(position);
            SearchItemBean s = datas.get(position);
            if (TextUtils.isEmpty(s.getName())) {
                holder.itemText.setVisibility(View.GONE);
                return;
            } else {
                holder.itemText.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(keyword) && s.getName().contains(keyword)) {
                SpannableString ss = new SpannableString(s.getName());
                int pos = s.getName().indexOf(keyword);
                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)), pos, pos + keyword.length(),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.itemText.setText(ss);
            } else {
                holder.itemText.setText(s.getName());
            }

            if (!TextUtils.isEmpty(s.getImg())) {
                Glide.with(App.AppContex).load(s.getImg()).asBitmap().into(new CircleImgWrapper(holder.imageView, App.AppContex));
            }
            //            if (s.isAuthor())
            //                holder.qcIdentify.setVisibility(View.VISIBLE);
            //            else holder.qcIdentify.setVisibility(View.GONE);

            LogUtil.e("city:" + s.getCity() + "  " + s.isAuthor());
            if (!TextUtils.isEmpty(s.getCity())) {

                holder.itemAddress.setVisibility(View.VISIBLE);
                holder.itemAddress.setText(s.getCity());
            } else {
                holder.itemAddress.setVisibility(View.GONE);
            }
        }

        @Override public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            LogUtil.e("onclick");
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class InternalHandler extends Handler {
        WeakReference<Context> context;

        InternalHandler(Context c) {
            context = new WeakReference<Context>(c);
        }

        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.getData().getString("keyword");
            if (s != null && keyword.length() > 0 && s.equals(keyword)) searchResult(keyword);
        }
    }

    public class InternalSearchHandler extends Handler {
        WeakReference<Context> context;

        InternalSearchHandler(Context c) {
            context = new WeakReference<Context>(c);
        }

        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.getData().getString("keyword");
            if (s.equals(keyword) && keyword.length() > 2) searchResult(keyword);
        }
    }
}
