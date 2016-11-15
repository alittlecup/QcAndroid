package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.FunctionBean;
import com.qingchengfit.fitcoach.component.ItemDecorationAlbumColumns;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.items.DailyWorkItem;
import com.qingchengfit.fitcoach.items.ManageWorkItem;

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
public class ManageFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    List<AbstractFlexibleItem> mData = new ArrayList<>();
    @Bind(R.id.recyclerview2)
    RecyclerView recyclerview2;
    private CommonFlexAdapter mAdapter;

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
        mAdapter = new CommonFlexAdapter(mData, this);
        recyclerview.addItemDecoration(new ItemDecorationAlbumColumns(1, 3));
        SmoothScrollGridLayoutManager manager1 = new SmoothScrollGridLayoutManager(getContext(), 3);
        manager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recyclerview.setLayoutManager(manager1);
        recyclerview.setAdapter(mAdapter);

        List<AbstractFlexibleItem> data2 = new ArrayList<>();
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_all_card).text(getString(R.string.student_card_manage)).subname("全面管理会员卡").build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_coach).text(getString(R.string.manage_salers)).subname("分配销售名下会员").build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_coach).text(getString(R.string.manage_staffs)).subname("不同员工不同角色").build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_authority).text(getString(R.string.manage_cards)).subname("不同角色不同权限").build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_scan).text(getString(R.string.manage_signin)).subname("不同角色不同权限").build()));
        data2.add(new ManageWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_marketing_activity).text(getString(R.string.manage_acitivity)).subname("健身房营销活动").build()));
        CommonFlexAdapter adapter2 = new CommonFlexAdapter(data2, new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                StaffAppFragmentFragment.newInstance().show(getFragmentManager(), "");
                return true;
            }
        });
        recyclerview2.addItemDecoration(new ItemDecorationAlbumColumns(1, 2));
        SmoothScrollGridLayoutManager manager2 = new SmoothScrollGridLayoutManager(getContext(), 2);
        recyclerview2.setLayoutManager(manager2);
        recyclerview2.setAdapter(adapter2);

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
        if (mAdapter.getItem(position) instanceof FunctionBean) {
            int res = ((FunctionBean) mAdapter.getItem(position)).resImg;
            switch (res) {
                case R.drawable.ic_weight://排课
                    break;
                case R.drawable.ic_category_course://课程种类

                    break;
                case R.drawable.ic_users_student:

                    break;
                case R.drawable.ic_img_statement_signin:
                    Intent toCourse = new Intent(getActivity(), FragActivity.class);
                    toCourse.putExtra("to",0);
                    startActivity(toCourse);
                    break;
                case R.drawable.ic_sale_statement:
                    Intent tosale = new Intent(getActivity(), FragActivity.class);
                    tosale.putExtra("to",1);
                    startActivity(tosale);

                    break;
                case R.drawable.ic_template_coursepaln:
                    Intent toPlan = new Intent(getActivity(), FragActivity.class);
                    toPlan.putExtra("to",1);
                    startActivity(toPlan);
                    break;
                default:
            }


        }
        return true;
    }
}
