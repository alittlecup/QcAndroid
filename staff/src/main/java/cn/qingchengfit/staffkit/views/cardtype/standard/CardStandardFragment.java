package cn.qingchengfit.staffkit.views.cardtype.standard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 16/3/17 2016.
 */
public class CardStandardFragment extends BaseFragment {

    List<CardStandard> datas = new ArrayList<>();
    @BindView(R.id.recycleview) RecyclerView recycleview;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_standard, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        CardStandardAdapter adapter = new CardStandardAdapter();
        recycleview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                //点击规格
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return CardStandardFragment.class.getName();
    }

    class CardStandardVH extends RecyclerView.ViewHolder {

        @BindView(R.id.card_content) TextView cardContent;
        @BindView(R.id.income) TextView income;
        @BindView(R.id.validity) TextView validity;
        @BindView(R.id.recharge) TextView recharge;

        public CardStandardVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FootVh extends RecyclerView.ViewHolder {
        @BindView(R.id.add) TextView add;

        public FootVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CardStandardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        OnRecycleItemClickListener listener;

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                CardStandardVH vh =
                    new CardStandardVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_standard, parent, false));
                vh.itemView.setOnClickListener(this);
                return vh;
            } else {
                FootVh vh = new FootVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_cardstandard, parent, false));
                return vh;
            }
        }

        @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position < getItemCount()) {
                if (holder instanceof CardStandardVH) {
                    holder.itemView.setTag(position);
                    CardStandard cardStandard = datas.get(position);
                    ((CardStandardVH) holder).cardContent.setText(cardStandard.getContent());
                    ((CardStandardVH) holder).income.setText(cardStandard.getReal_income());
                    ((CardStandardVH) holder).recharge.setText(cardStandard.getSupport_type());
                    ((CardStandardVH) holder).validity.setText(cardStandard.getValid_date());
                }
            } else {
                if (holder instanceof FootVh) {
                    ((FootVh) holder).add.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            //添加规格
                            //                            getFragmentManager().beginTransaction()
                            //                                    .replace(mCallbackActivity.getFragId(), AddCardStandardFragment.newInstance(Configs.CATEGORY_VALUE,))
                            //                                    .commit();
                        }
                    });
                    //                    ((FootVh) holder).completed.setOnClickListener(new View.OnClickListener() {
                    //                        @Override
                    //                        public void onClick(View v) {
                    //                            //
                    //                        }
                    //                    });
                }
            }
        }

        @Override public int getItemCount() {
            return datas.size() + 1;
        }

        @Override public int getItemViewType(int position) {
            if (position == datas.size()) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }
}
