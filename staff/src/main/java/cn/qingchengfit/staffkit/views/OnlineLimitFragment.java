package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.ChooseItem;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.EventMutiChoose;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/17.
 */
public class OnlineLimitFragment extends BottomSheetDialogFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;

    List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    CommonFlexAdapter mFlexAdapter;
    private Unbinder unbinder;

    public static OnlineLimitFragment newInstance(int... d) {

        Bundle args = new Bundle();
        args.putIntArray("k", d);
        OnlineLimitFragment fragment = new OnlineLimitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_list_muti, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext()));

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
        if (unbinder != null) unbinder.unbind();
    }

    @OnClick(R.id.comfirm) public void onClick() {
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
