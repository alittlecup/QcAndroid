package cn.qingchengfit.staffkit.views.cardtype;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.cardtype.list.CardTypeListPresenter;
import cn.qingchengfit.staffkit.views.cardtype.list.CardTypeListView;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 16/3/24 2016.
 */
public class ChooseCardTypeActivity extends BaseActivity implements CardTypeListView {

    @Inject CardTypeListPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;

    private List<CardTpl> datas = new ArrayList<>();
    private TypeListAdatper adatper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbarTitile.setText("选择会员卡种类");
        presenter.attachView(this);
        presenter.queryCardTypePermission(App.staffId);
        adatper = new TypeListAdatper();
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent();
                it.putExtra(Configs.EXTRA_CARD_TYPE, datas.get(pos));
                setResult(RESULT_OK, it);//返回数据
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryCardTypePermission(App.staffId);
            }
        });
    }

    @Override public void onGetData(List<CardTpl> card_tpls) {
        datas.clear();
        datas.addAll(card_tpls);
        adatper.notifyDataSetChanged();
        recycleview.setNoData(datas.size() == 0);
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        ToastUtils.show(getString(e));
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
            holder.cardid.setText(card_tpl.getId());
            holder.cardname.setText(card_tpl.getName());
            holder.intro.setText(card_tpl.getDescription());
            holder.limit.setText(card_tpl.getLimit());
            holder.cardid.setText("ID: " + card_tpl.getId());
            if (holder.cardView instanceof CardView) {
                ((CardView) holder.cardView).setCardBackgroundColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
            } else {
                GradientDrawable shapeDrawable = (GradientDrawable) holder.cardView.getBackground();
                shapeDrawable.setColor(ColorUtils.parseColor(card_tpl.getColor(), 200).getColor());
            }
            holder.supportgyms.setText("适用场馆: " + card_tpl.getShopNames());
            if (card_tpl.getType() == Configs.CATEGORY_VALUE) {
                holder.type.setText("储值类型");
            } else if (card_tpl.getType() == Configs.CATEGORY_TIMES) {
                holder.type.setText("次卡类型");
            } else {
                holder.type.setText("期限类型");
            }
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
        @BindView(R.id.type) TextView type;
        @BindView(R.id.cardview) View cardView;
        @BindView(R.id.card_bg) LinearLayout cardBg;
        @BindView(R.id.support_gyms) TextView supportgyms;

        public TypeListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
