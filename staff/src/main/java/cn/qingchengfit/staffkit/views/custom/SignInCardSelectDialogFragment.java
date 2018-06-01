package cn.qingchengfit.staffkit.views.custom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.responese.SigninValidCard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/8/30.
 */
public class SignInCardSelectDialogFragment extends BaseBottomSheetDialogFragment {

	RecyclerView recycleview;
    private List<SigninValidCard.DataBean.CardsBean> d;
    private String selectedCardId = "";

    public static void start(Fragment fragment, int requestCode, ArrayList<SigninValidCard.DataBean.CardsBean> datas, String selectedId) {
        SignInCardSelectDialogFragment f = newInstance(datas, selectedId);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static SignInCardSelectDialogFragment newInstance(ArrayList<SigninValidCard.DataBean.CardsBean> datas, String selectedId) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", datas);
        args.putString("selectedId", selectedId);
        SignInCardSelectDialogFragment fragment = new SignInCardSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            d = getArguments().getParcelableArrayList("datas");
            selectedCardId = getArguments().getString("selectedId");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_signin_card_select, container, false);
      recycleview = (RecyclerView) view.findViewById(R.id.recycleview);


      BSAdapter adatper = new BSAdapter();
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (getTargetFragment() == null) {

                } else {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        IntentUtils.instanceStringIntent(String.valueOf(pos)));
                    SignInCardSelectDialogFragment.this.dismiss();
                }
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    class BSVH extends RecyclerView.ViewHolder {
	TextView realcard_name;
	TextView realcard_balance;
	ImageView iv_card_selected;

        public BSVH(View view) {
            super(view);
            realcard_name = (TextView) view.findViewById(R.id.realcard_name);
            realcard_balance = (TextView) view.findViewById(R.id.realcard_balance);
            iv_card_selected = (ImageView) view.findViewById(R.id.iv_card_selected);

        }
    }

    class BSAdapter extends RecyclerView.Adapter<BSVH> implements View.OnClickListener {
        private OnRecycleItemClickListener listener;

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public BSVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signin_card_select, parent, false);
            BSVH b = new BSVH(view);
            b.itemView.setOnClickListener(this);
            return b;
        }

        @Override public void onBindViewHolder(BSVH holder, int position) {
            holder.itemView.setTag(position);
            SigninValidCard.DataBean.CardsBean card = d.get(position);
            String balance;
            if (card.getType() == Configs.CATEGORY_VALUE) {
                balance = TextUtils.concat("余额:", String.valueOf(card.getBalance()), "元").toString();
            } else if (card.getType() == Configs.CATEGORY_DATE) {
                balance = TextUtils.concat("有效期至:", card.getEnd().split("T")[0], "").toString();
            } else {
                balance = TextUtils.concat("余额:", String.valueOf(card.getBalance()), "次").toString();
            }
            holder.realcard_balance.setText(balance);
            holder.realcard_name.setText(card.getName() + "(" + card.getId() + ")");
            if (selectedCardId.equals(String.valueOf(card.getId()))) {
                holder.iv_card_selected.setVisibility(View.VISIBLE);
            } else {
                holder.iv_card_selected.setVisibility(View.GONE);
            }
        }

        @Override public int getItemCount() {
            return d.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }
}
