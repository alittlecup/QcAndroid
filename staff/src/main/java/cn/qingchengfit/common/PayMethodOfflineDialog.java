package cn.qingchengfit.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.PayEvent;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.wardrobe.item.PayWardrobeItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * Created by Paper on 2017/5/5.
 */

public class PayMethodOfflineDialog extends BaseBottomSheetDialogFragment implements FlexibleAdapter.OnItemClickListener {
    @BindView(R.id.rv) RecyclerView rv;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mDatas = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_wardrobe_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        mAdapter = new CommonFlexAdapter(mDatas, this);
        mDatas.clear();
        mDatas.add(new SimpleTextItemItem("线下支付"));
        mDatas.add(new PayWardrobeItem(2, null));
        mDatas.add(new PayWardrobeItem(4, null));
        mDatas.add(new PayWardrobeItem(5, null));
        mDatas.add(new PayWardrobeItem(6, null));
        rv.setAdapter(mAdapter);
        return view;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override public boolean onItemClick(int i) {
        if (mAdapter.getItem(i) instanceof PayWardrobeItem) {
            RxBus.getBus().post(new PayEvent(((PayWardrobeItem) mAdapter.getItem(i)).getPay_mode()));
            dismiss();
        }
        return false;
    }
}