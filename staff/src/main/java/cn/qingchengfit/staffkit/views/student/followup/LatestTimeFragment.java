package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * // 最近7天 list
 * //Created by yangming on 16/12/2.
 */
public class LatestTimeFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, View.OnTouchListener {

    public LinearLayoutManager mLinearLayoutManager;
    public CommonFlexAdapter flexibleAdapter;
    public List<AbstractFlexibleItem> items = new ArrayList();
    public List<StudentSourceBean> datas = new ArrayList<>();
    public int selectedPos = 0;
    public int type;

    @BindView(R.id.rv_referrer) RecyclerView rvReferrer;
    private FlexibleAdapter.OnItemClickListener onItemClickListener;

    @Inject public LatestTimeFragment() {
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up_latest_time, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        view.setOnTouchListener(this);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initView() {
        items.clear();
        if (type == 1) items.add(new LatestTimeItem("今日", true));
        items.add(new LatestTimeItem(
            "最近7天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -6)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")", true));
        items.add(new LatestTimeItem(
            "最近30天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -29)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")"));
        items.add(new LatestTimeItem("自定义"));
        flexibleAdapter = new CommonFlexAdapter(items, this);
        flexibleAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvReferrer.setLayoutManager(mLinearLayoutManager);
        rvReferrer.setAdapter(flexibleAdapter);
    }

    /**
     * list item click
     */
    private void itemClick(int position) {
        if (selectedPos >= 0) {
            ((LatestTimeItem) items.get(selectedPos)).setSelect(false);
        }
        selectedPos = position;
        ((LatestTimeItem) items.get(selectedPos)).setSelect(true);
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override public boolean onItemClick(int position) {
        if (position < flexibleAdapter.getItemCount() - 1) {
            if (!flexibleAdapter.isSelected(position)) flexibleAdapter.toggleSelection(position);
        }
        flexibleAdapter.notifyDataSetChanged();
        if (onItemClickListener != null) onItemClickListener.onItemClick(position);
        return false;
    }

    @OnClick(R.id.view_mask) public void onClick() {
    }

    public FlexibleAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(FlexibleAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
