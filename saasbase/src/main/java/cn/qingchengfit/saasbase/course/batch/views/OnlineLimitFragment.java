package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.ChooseItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.events.EventMutiChoose;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

public class OnlineLimitFragment extends BottomSheetDialogFragment implements FlexibleAdapter.OnItemClickListener {

	RecyclerView recyclerview;

    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    CommonFlexAdapter mFlexAdapter;


    public static OnlineLimitFragment newInstance(int... d) {

        Bundle args = new Bundle();
        args.putIntArray("k", d);
        OnlineLimitFragment fragment = new OnlineLimitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_list_muti, container, false);
      recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
      view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OnlineLimitFragment.this.onClick();
        }
      });

      setRecyclerview();
        return view;
    }

    public void setRecyclerview() {
        mDatas.clear();
        mDatas.add(new ChooseItem("新注册"));
        mDatas.add(new ChooseItem("已接洽"));
        mDatas.add(new ChooseItem("会员"));
        mFlexAdapter = new CommonFlexAdapter(mDatas, this);
        //        mFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new FlexibleItemDecoration(getContext())
          .withDefaultDivider(R.layout.item_choose)
          .withOffset(1).withBottomEdge(true));

        if (getArguments() != null && getArguments().getIntArray("k") != null && getArguments().getIntArray("k").length > 0) {
            int[] x = getArguments().getIntArray("k");
            for (int i = 0; i < x.length; i++) {
                mFlexAdapter.toggleSelection(x[i]);
            }
        } else {
            mFlexAdapter.selectAll();
        }
        recyclerview.setAdapter(mFlexAdapter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onClick() {
        String strChoose = "";
        List<Object> ret = new ArrayList<>();
        if (mFlexAdapter.isSelected(0)) {
            strChoose = strChoose.concat("新注册");
            ret.add(0);
        }
        if (mFlexAdapter.isSelected(1)) {
            strChoose = strChoose.concat("已接洽");
            ret.add(1);
        }
        if (mFlexAdapter.isSelected(2)) {
            strChoose = strChoose.concat("会员");
            ret.add(2);
        }

        RxBus.getBus().post(new EventMutiChoose(ret));
        dismiss();
    }

    @Override public boolean onItemClick(int position) {
        if (mFlexAdapter.getSelectedPositions().size() == 1 && mFlexAdapter.getSelectedPositions().get(0) == position) {
            ToastUtils.showDefaultStyle("至少选择一种限制条件");
            return true;
        } else {
            mFlexAdapter.toggleSelection(position);
        }
        mFlexAdapter.notifyItemChanged(position);

        return false;
    }
}
