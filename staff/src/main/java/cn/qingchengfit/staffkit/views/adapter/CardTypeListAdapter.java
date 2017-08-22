package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.ClubCardView;
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
 * Created by Paper on 16/2/14 2016.
 */
public class CardTypeListAdapter extends RecyclerView.Adapter<CardTypeListAdapter.CardTypeListVH> {

    private List<CardTpl> datas = new ArrayList<>();

    public CardTypeListAdapter(List<CardTpl> datas) {
        this.datas = datas;
    }

    @Override public CardTypeListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CardTypeListVH vh = new CardTypeListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardtype, parent, false));
        return vh;
    }

    @Override public void onBindViewHolder(CardTypeListVH holder, int position) {
        CardTpl cardType = datas.get(position);
        holder.clubCardView.setLable(cardType.name);
        holder.clubCardView.setType(cardType.type);
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    public class CardTypeListVH extends RecyclerView.ViewHolder {
        @BindView(R.id.cardtype) public ClubCardView clubCardView;

        public CardTypeListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
