package cn.qingchengfit.recruit.views.organization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {
    public static final String TAG = SearchFragment.class.getName();
    public static final int TYPE_GYM = 0;
    public static final int TYPE_ORGANASITON = 1;
    public static final int RESULT_BRAND = 2;
    public static final int RESULT_GYM = 3;

    public String keyword;
    SearchResultAdapter adapter;

    @BindView(R2.id.searchview_et) EditText searchviewEt;
    @BindView(R2.id.searchview_clear) ImageView searchviewClear;
    @BindView(R2.id.searchresult_btn) Button searchresultBtn;
    @BindView(R2.id.searchresult_none) LinearLayout searchresultNone;
    @BindView(R2.id.searchresult_rv) RecyclerView searchresultRv;
    @BindView(R2.id.search_hottable) TextView searchHottable;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.search_hint) TextView searchHint;
    @BindView(R2.id.searchresult_hint) TextView searchresultHint;
    @Inject QcRestRepository restRepository;
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
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        searchviewEt.setCompoundDrawables(ContextCompat.getDrawable(getContext(), R.drawable.vd_search_grey_14dp), null, null, null);
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
        if (type == TYPE_GYM) {
            searchHottable.setText("热门健身房");
            RxRegiste(restRepository.createGetApi(GetApi.class)
                .qcHotGym(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcSerachGymRepsonse>() {
                    @Override public void call(final QcSerachGymRepsonse qcSerachGymRepsonse) {
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
                                    strings.add(
                                        new SearchItemBean(addGymBean.name, addGymBean.photo, ss.toString(), addGymBean.is_authenticated));
                                }
                                strings.add(new SearchItemBean());
                                adapter.setListener(new OnRecycleItemClickListener() {
                                    @Override public void onItemClick(View v, int pos) {
                                        if (pos != adapter.getItemCount() - 1) {
                                            searchListener.onSearchResult(100,
                                                Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id),
                                                qcSerachGymRepsonse.getData().getGym().get(pos).name,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).brand_name,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).photo,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).is_authenticated);
                                        }
                    }
                                });
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                }));
        } else if (type == TYPE_ORGANASITON) {
            searchHottable.setText("热门机构");
            RxRegiste(restRepository.createGetApi(GetApi.class)
                .qcHotOrganization(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcSearchOrganResponse>() {
                    @Override public void call(final QcSearchOrganResponse qcSearchOrganResponse) {
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
                                adapter.setListener(new OnRecycleItemClickListener() {
                                    @Override public void onItemClick(View v, int pos) {
                                        if (pos != adapter.getItemCount() - 1) {

                                            searchListener.onSearchResult(100,
                                                qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(),
                                                qcSearchOrganResponse.getData().getOrganizations().get(pos).getName(),
                                                qcSearchOrganResponse.getData().getOrganizations().get(pos).getContact(),
                                                qcSearchOrganResponse.getData().getOrganizations().get(pos).getPhoto(),
                                                qcSearchOrganResponse.getData().getOrganizations().get(pos).is_authenticated());
                                        }
                    }
                                });
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

            }
                }));
    }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_BRAND) {
                Brand brand = (Brand) data.getParcelableExtra("fragment_result");
                if (brand != null) {
                    try {
                        Intent guide = new Intent(getContext().getPackageName() + ".guide");
                        guide.putExtra("brand", brand);
                        guide.putExtra("workexp", true);
                        startActivityForResult(guide, RESULT_GYM);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }
            } else if (requestCode == RESULT_GYM) {
                CoachService coachService = data.getParcelableExtra("gym");
                searchListener.onSearchResult(100, Integer.parseInt(coachService.gym_id), coachService.name, coachService.brand_name,
                    coachService.photo, false);
            }
    }
    }

    @OnClick(R2.id.searchview_clear) public void onClear() {
        searchviewEt.setText("");
    }

    @OnClick(R2.id.searchresult_btn) public void onAdd() {
        if (type == TYPE_GYM) {
            try {
                Intent toChooseBrand = new Intent(getContext().getPackageName() + ".chooseBrand");
                startActivityForResult(toChooseBrand, RESULT_BRAND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == TYPE_ORGANASITON) {
            getFragmentManager().beginTransaction().add(R.id.search_fraglayout, new AddOganasitionFragment()).addToBackStack(null).commit();
        }
    }

    /**
     * 搜索健身房
     */
    private void searchResult(String keyword) {
        if (TextUtils.isEmpty(keyword)) return;
        Map<String, String> params = new HashMap<>();
        params.put("q", keyword);
        if (type == TYPE_GYM) {

            RxRegiste(restRepository.createGetApi(GetApi.class)
                .qcSearchGym(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcSerachGymRepsonse>() {
                    @Override public void call(final QcSerachGymRepsonse qcSerachGymRepsonse) {
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
                                    strings.add(new SearchItemBean(addGymBean.name, addGymBean.photo, city.toString(),
                                        addGymBean.is_authenticated));
                                }
                                strings.add(new SearchItemBean("添加健身房"));
                                adapter.setListener(new OnRecycleItemClickListener() {
                                    @Override public void onItemClick(View v, int pos) {
                                        if (pos == adapter.getItemCount() - 1) {
                                            onAdd();
                                        } else {
                                            searchListener.onSearchResult(100,
                                                Integer.parseInt(qcSerachGymRepsonse.getData().getGym().get(pos).id),
                                                qcSerachGymRepsonse.getData().getGym().get(pos).name,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).brand_name,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).photo,
                                                qcSerachGymRepsonse.getData().getGym().get(pos).is_authenticated);
                                        }
                                    }
                                });

                                adapter.notifyDataSetChanged();
                            } else {
                                searchresultRv.setVisibility(View.GONE);
                            }
                        }
                    }
                }));
        } else if (type == TYPE_ORGANASITON) {
            RxRegiste(restRepository.createGetApi(GetApi.class)
                .qcSearchOrganization(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcSearchOrganResponse>() {
                               @Override public void call(final QcSearchOrganResponse qcSearchOrganResponse) {
                                   if (searchHottable != null) {
                                       strings.clear();
                                       if (qcSearchOrganResponse.getData().getOrganizations().size() > 0) {
                                           searchHottable.setText("搜索结果");
                                           searchresultRv.setVisibility(View.VISIBLE);
                                           for (QcSearchOrganResponse.DataEntity.OrganizationsEntity addGymBean : qcSearchOrganResponse.getData()
                                               .getOrganizations()) {
                                               strings.add(
                                                   new SearchItemBean(addGymBean.getName(), addGymBean.getPhoto(), "联系方式:" + addGymBean.getContact(),
                                                       addGymBean.is_authenticated()));
                                           }
                                           strings.add(new SearchItemBean("添加主办机构"));
                                           adapter.setListener(new OnRecycleItemClickListener() {
                                               @Override public void onItemClick(View v, int pos) {
                                                   if (pos == adapter.getItemCount() - 1) {
                                                       onAdd();
                                                   } else {
                                                       searchListener.onSearchResult(100,
                                                           qcSearchOrganResponse.getData().getOrganizations().get(pos).getId(),
                                                           qcSearchOrganResponse.getData().getOrganizations().get(pos).getName(),
                                                           qcSearchOrganResponse.getData().getOrganizations().get(pos).getContact(),
                                                           qcSearchOrganResponse.getData().getOrganizations().get(pos).getPhoto(),
                                                           qcSearchOrganResponse.getData().getOrganizations().get(pos).is_authenticated());
                                                   }
                               }
                                           });

                                           adapter.notifyDataSetChanged();
                                       } else {
                                           searchresultRv.setVisibility(View.GONE);
                           }
                                   }
                               }
                           }

                ));
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
        @BindView(R2.id.item_text) TextView itemText;
        @BindView(R2.id.item_img) ImageView imageView;
        @BindView(R2.id.item_qc_identify) ImageView qcIdentify;
        @BindView(R2.id.item_address) TextView itemAddress;

        public SearchResultVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                ss.setSpan(new ForegroundColorSpan(AppUtils.getPrimaryColor(getContext())), pos, pos + keyword.length(),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.itemText.setText(ss);
            } else {
                holder.itemText.setText(s.getName());
            }

            if (!TextUtils.isEmpty(s.getImg())) {
                Glide.with(getContext()).load(s.getImg()).asBitmap().into(new CircleImgWrapper(holder.imageView, getContext()));
            }
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
