package cn.qingchengfit.staffkit.views.student.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.common.AttendanceRecord;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.ChooseGymPopWin;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * 会员详情 -- 上课记录
 * <p>
 * Created by Paper on 16/3/19 2016.
 */
public class ClassRecordFragment extends BaseFragment implements ClassRecordView, TitleFragment, FlexibleAdapter.OnItemClickListener {

    public String curShopid = "";
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
    @BindView(R.id.tv_gym) TextView tvGym;
    @Inject ClassRecordPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private CommonFlexAdapter commonFlexAdapter;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private List<Shop> shops = new ArrayList<>();
    private ChooseGymPopWin chooseGymPopWin;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_records, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        tvGym.setText(gymWrapper.name());
        tvGym.setCompoundDrawablesWithIntrinsicBounds(null, null,
            ContextCompat.getDrawable(getContext(), R.drawable.vector_arrow_down_green), null);
        SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getContext(), 4);
        recycleview.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (datas.size() > position && datas.get(position) instanceof AttendanceAnalysItem) return 1;
                return 4;
            }
        });
        commonFlexAdapter = new CommonFlexAdapter(datas, this);
        recycleview.setAdapter(commonFlexAdapter);
        //adater = new StudentClassRecordAdapter(datas);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData(curShopid);
            }
        });
        presenter.queryData(curShopid);
        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onData(List<AttendanceRecord> attendanceRecords, ClassRecords.Stat stat, List<Shop> ss) {
        recycleview.stopLoading();
        shops.clear();
        shops.addAll(ss);
        datas.clear();
        datas.add(new AttendanceAnalysItem(stat.days + "", "出勤", CompatUtils.getColor(getContext(), R.color.orange),
            getString(R.string.unit_day)));
        datas.add(new AttendanceAnalysItem(stat.checkin + "", "签到", Color.parseColor("#8cb5ba"), getString(R.string.unit_time)));
        datas.add(new AttendanceAnalysItem(stat.group + "", "团课", CompatUtils.getColor(getContext(), R.color.blue),
            getString(R.string.unit_calss)));
        datas.add(new AttendanceAnalysItem(stat.private_count + "", "私教", CompatUtils.getColor(getContext(), R.color.purple),
            getString(R.string.unit_calss)));
        int month = -1;
        List<AttendanceRecord> monthData = new ArrayList<>();
        /**
         * 通过判断月份 来分组
         * 在发现第二组时 统一添加 标题 和第一组的数据
         * g p c用来统计团课私教 签到 当月数量
         */
        int g = 0, p = 0, c = 0;
        for (int i = 0; i < attendanceRecords.size(); i++) {
            AttendanceRecord ar = attendanceRecords.get(i);
            if (DateUtils.getMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(ar.start)) != month) {
                month = DateUtils.getMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(ar.start));
                if (i != 0) {
                    if (monthData.size() > 0) {
                        datas.add(
                            new AttendanceRecordHeadItem(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(0).start), c, g,
                                p));
                    }
                    int day = 0;
                    for (int j = 0; j < monthData.size(); j++) {
                        if (DateUtils.getDayOfMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(j).start)) != day) {
                            day = DateUtils.getDayOfMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(j).start));
                            datas.add(new AttendanceRecordItem(monthData.get(j), true));
                        } else {
                            datas.add(new AttendanceRecordItem(monthData.get(j), false));
                        }
                    }
                    monthData = new ArrayList<>();
                    g = p = c = 0;
                }
            }
            if (ar.type == 1) g++;
            if (ar.type == 2) p++;
            if (ar.type == 3) c++;
            monthData.add(ar);
        }
        if (monthData.size() > 0) {
            datas.add(new AttendanceRecordHeadItem(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(0).start), c, g, p));
        }
        int day = 0;
        for (int j = 0; j < monthData.size(); j++) {
            if (DateUtils.getDayOfMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(j).start)) != day) {
                day = DateUtils.getDayOfMonth(cn.qingchengfit.utils.DateUtils.formatDateFromServer(monthData.get(j).start));
                datas.add(new AttendanceRecordItem(monthData.get(j), true));
            } else {
                datas.add(new AttendanceRecordItem(monthData.get(j), false));
            }
        }
        monthData = new ArrayList<>();
        g = p = c = 0;

        commonFlexAdapter.notifyDataSetChanged();
        recycleview.setNoData(datas.size() == 0);
    }

    @Override public String getTitle() {
        return "上课记录";
    }

    @Override public String getFragmentName() {
        return ClassRecordFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        if (datas.get(position) instanceof AttendanceRecordItem) {
            WebActivity.startWeb(((AttendanceRecordItem) datas.get(position)).getUrl(), getActivity());
        }
        return false;
    }

    @OnClick(R.id.tv_gym) public void onClickGym() {
        if (chooseGymPopWin == null) {
            chooseGymPopWin = new ChooseGymPopWin(getContext());
            chooseGymPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override public void onDismiss() {
                    tvGym.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(getContext(), R.drawable.vector_arrow_down_green), null);
                }
            });
            chooseGymPopWin.setListener(new FlexibleAdapter.OnItemClickListener() {
                @Override public boolean onItemClick(int position) {
                    if (position == 0) {
                        tvGym.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(getContext(), R.drawable.vector_arrow_down_green), null);
                        tvGym.setText(R.string.all_gyms);
                        curShopid = "";
                    } else if (shops.size() > (position - 1)) {
                        tvGym.setText(shops.get(position - 1).name);
                        curShopid = shops.get(position - 1).id;
                    }
                    presenter.queryData(curShopid);
                    return true;
                }
            });
        }
        chooseGymPopWin.setDatas(shops);
        chooseGymPopWin.showAsDropDown(tvGym);
        tvGym.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.vector_arrow_up_green),
            null);
    }
}
