package cn.qingchengfit.staffkit.views.custom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.utils.IntentUtils;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by Paper on 16/3/15 2016.
 */
public class BottomSheetListDialogFragment extends BaseBottomSheetDialogFragment {

    @BindView(R.id.recycleview) RecyclerView recycleview;
    private List<String> d;

    public static void start(Fragment fragment, int requestCode, ArrayList<String> datas) {
        BottomSheetListDialogFragment f = newInstance(datas);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static void start(Fragment fragment, int requestCode, String[] datas) {
        BottomSheetListDialogFragment f = newInstance(new ArrayList<String>(Arrays.asList(datas)));
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static BottomSheetListDialogFragment newInstance(ArrayList<String> datas) {
        Bundle args = new Bundle();
        args.putStringArrayList("datas", datas);
        BottomSheetListDialogFragment fragment = new BottomSheetListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            d = getArguments().getStringArrayList("datas");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        BSAdapter adatper = new BSAdapter();
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new FlexibleItemDecoration(getContext())
          .withDivider(R.drawable.divider_horizon_left_44dp)
          .withOffset(1).withBottomEdge(true));
        recycleview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (getTargetFragment() == null) {

                } else {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        IntentUtils.instanceStringIntent(pos + ""));
                    BottomSheetListDialogFragment.this.dismiss();
                }
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    class BSVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_text) TextView text;

        public BSVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_text, parent, false);
            BSVH b = new BSVH(view);
            b.itemView.setOnClickListener(this);
            return b;
        }

        @Override public void onBindViewHolder(BSVH holder, int position) {
            holder.itemView.setTag(position);
            holder.text.setText(d.get(position));
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
