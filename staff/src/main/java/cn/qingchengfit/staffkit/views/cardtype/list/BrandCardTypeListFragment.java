package cn.qingchengfit.staffkit.views.cardtype.list;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.RxCardTypeEvent;
import cn.qingchengfit.staffkit.views.cardtype.detail.CardtypeDetailFragment;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

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
 * Created by Paper on 16/3/14 2016.
 */
public class BrandCardTypeListFragment extends BaseFragment implements CardTypeListView {

    public static final int CARDTYPE_ALL = 0;
    public static final int CARDTYPE_MUTI = 1;
    public static final int CARDTYPE_SINGLE = 2;

    @BindView(R.id.cardtype_list) RecycleViewWithNoImg cardtypeList;
    @BindView(R.id.card_count) TextView cardCount;
    @BindView(R.id.card_type) TextView cardType;
    @BindView(R.id.card_type_down) ImageView cardTypeDown;
    @BindView(R.id.card_disable) TextView cardDisable;
    private List<CardTpl> datas = new ArrayList<>();
    private int type = 0;
    private TypeListAdatper adatper;

    private boolean isStore = false;
    private Observable<RxCardTypeEvent> mObCt;
    private int mSupportType;

    //0为所有 1为储值卡 2次卡 3期限卡
    public static BrandCardTypeListFragment newInstance(int supoortMore) {
        Bundle args = new Bundle();
        args.putInt("supportMore", supoortMore);
        BrandCardTypeListFragment fragment = new BrandCardTypeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSupportType = getArguments().getInt("supportMore");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brandcardtypelist, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        if (isStore) cardtypeList.stopLoading();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && !isStore) {
            isStore = true;
        }
    }

    private void initView() {
        cardtypeList.setLayoutManager(new LinearLayoutManager(getContext()));
        adatper = new TypeListAdatper();
        cardtypeList.setAdapter(adatper);
        cardtypeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (getParentFragment() instanceof BrandCardListFragment) {
                    ((BrandCardListFragment) getParentFragment()).refresh();
                }
            }
        });
        if (getParentFragment() instanceof BrandCardListFragment) {
            setCardType(((BrandCardListFragment) getParentFragment()).getmCardtype());
        }
        mObCt = RxBusAdd(RxCardTypeEvent.class);
        mObCt.subscribe(new Subscriber<RxCardTypeEvent>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(RxCardTypeEvent rxCardTypeEvent) {
                cardtypeList.stopLoading();
                datas.clear();
                for (CardTpl card_tpl : rxCardTypeEvent.datas) {
                    if (mSupportType == CARDTYPE_ALL) {
                        datas.add(card_tpl);
                    } else if (mSupportType == CARDTYPE_MUTI) {
                        if (card_tpl.getShopIds() != null && card_tpl.getShopIds().size() > 1) datas.add(card_tpl);
                    } else if (mSupportType == CARDTYPE_SINGLE) {
                        if (card_tpl.getShopIds() != null && card_tpl.getShopIds().size() == 1) datas.add(card_tpl);
                    }
                }

                cardCount.setText(datas.size() + "");
                setCardType(rxCardTypeEvent.cardtype);
                adatper.notifyDataSetChanged();
            }
        });

        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .replace(mCallbackActivity.getFragId(), CardtypeDetailFragment.newInstance(datas.get(pos)))
                    .addToBackStack(null)
                    .commit();
            }
        });
    }

    @Override public void onGetData(List<CardTpl> card_tpls) {
        datas.clear();
        datas.addAll(card_tpls);
        adatper.notifyDataSetChanged();
        cardtypeList.setNoData(datas.size() == 0);
        cardtypeList.setFresh(false);
    }

    @Override public void onDestroyView() {
        //        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return BrandCardTypeListFragment.class.getName();
    }

    @OnClick({ R.id.card_type, R.id.card_type_down }) public void onClick(View view) {
        BottomSheetListDialogFragment.start(this, 1, getResources().getStringArray(R.array.cardtype_filter));

        //        switch (view.getId()) {
        //            case R.id.card_type:
        //                break;
        //            case R.id.card_type_down:
        //                break;
        //        }
    }

    @OnClick({ R.id.card_disable, R.id.card_disable_status }) public void onChangeCardDisable() {
        BottomSheetListDialogFragment.start(this, 2, getResources().getStringArray(R.array.cardtype_disable));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                setCardType(pos);
                if (getParentFragment() instanceof BrandCardListFragment) {
                    ((BrandCardListFragment) getParentFragment()).setmCardtype(pos);
                }
            } else if (requestCode == 2) {
                int pos = Integer.parseInt(IntentUtils.getIntentString(data));
                setCardDisable(pos);
                if (getParentFragment() instanceof BrandCardListFragment) {
                    ((BrandCardListFragment) getParentFragment()).setCardDisable(pos);
                }
            }
        }
    }

    public void setCardType(int pos) {
        cardType.setText(getResources().getStringArray(R.array.cardtype_filter)[pos]);
        if (pos > 0) {
            cardType.setTextColor(CompatUtils.getColor(getContext(), R.color.colorPrimary));
            cardTypeDown.setImageResource(R.drawable.ic_triangle_down_green);
        } else {
            cardType.setTextColor(CompatUtils.getColor(getContext(), R.color.text_grey));
            cardTypeDown.setImageResource(R.drawable.ic_triangle_down_grey);
        }
    }

    public void setCardDisable(int pos) {
        cardDisable.setText(getResources().getStringArray(R.array.cardtype_disable)[pos]);
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getParentFragment() instanceof BrandCardListFragment) {
            boolean enable = ((BrandCardListFragment) getParentFragment()).getCardDisable() == 0;
            cardDisable.setText(getResources().getStringArray(R.array.cardtype_disable)[enable ? 0 : 1]);
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    class TypeListAdatper extends RecyclerView.Adapter<TypeListVH> implements View.OnClickListener {

        private OnRecycleItemClickListener listener;

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public TypeListVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            TypeListVH typeListVH = new TypeListVH(view);
            typeListVH.itemView.setOnClickListener(this);
            return typeListVH;
        }

        @Override public void onBindViewHolder(TypeListVH holder, int position) {
            holder.itemView.setTag(position);
            CardTpl card_tpl = datas.get(position);
            holder.cardid.setText("ID: " + card_tpl.getId());
            holder.cardname.setText(card_tpl.getName());
            holder.intro.setText(card_tpl.getDescription());
            holder.limit.setText(card_tpl.getLimit());
            if (holder.cardView instanceof CardView) {
                ((CardView) holder.cardView).setCardBackgroundColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
            } else {
                GradientDrawable shapeDrawable = (GradientDrawable) holder.cardView.getBackground();
                shapeDrawable.setColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
            }
            holder.supoortgyms.setText("适用场馆: " + card_tpl.getShopNames());
            if (card_tpl.getType() == Configs.CATEGORY_VALUE) {
                holder.type.setText("储值类型");
            } else if (card_tpl.getType() == Configs.CATEGORY_TIMES) {
                holder.type.setText("次卡类型");
            } else {
                holder.type.setText("期限类型");
            }
            holder.cardDisable.setVisibility(card_tpl.is_enable ? View.GONE : View.VISIBLE);
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }

    class TypeListVH extends RecyclerView.ViewHolder {
        @BindView(R.id.cardname) TextView cardname;
        @BindView(R.id.cardid) TextView cardid;
        @BindView(R.id.limit) TextView limit;
        @BindView(R.id.intro) TextView intro;
        @BindView(R.id.cardview) View cardView;
        @BindView(R.id.type) TextView type;
        @BindView(R.id.card_bg) LinearLayout cardBg;
        @BindView(R.id.support_gyms) TextView supoortgyms;
        @BindView(R.id.disable_corner) ImageView cardDisable;

        public TypeListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
