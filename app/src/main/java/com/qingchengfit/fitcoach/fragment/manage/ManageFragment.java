package com.qingchengfit.fitcoach.fragment.manage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.FunctionBean;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.items.DailyWorkItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

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
 * Created by Paper on 16/11/10.
 */
public class ManageFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener{

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    private CommonFlexAdapter mAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manange, container, false);
        ButterKnife.bind(this, view);
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_weight).text(getString(R.string.course_batch)).build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_category_course).text(getString(R.string.course_types)).build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_student).text(getString(R.string.student)).build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_img_statement_signin).text(getString(R.string.course_statement)).build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_sale_statement).text(getString(R.string.sale_statement)).build()));
        mData.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_template_coursepaln).text(getString(R.string.course_plan)).build()));
        mAdapter = new CommonFlexAdapter(mData,this);
        SmoothScrollGridLayoutManager manager1 = new SmoothScrollGridLayoutManager(getContext(),3);
        manager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerview.setLayoutManager(manager1);
        recyclerview.setAdapter(mAdapter);

        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onItemClick(int position) {

        return false;
    }
}
