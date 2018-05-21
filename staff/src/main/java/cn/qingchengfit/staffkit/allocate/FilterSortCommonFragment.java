package cn.qingchengfit.staffkit.allocate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.allocate.coach.AllocateStudentArgsBundler;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparator;
import cn.qingchengfit.staffkit.allocate.coach.comparator.ItemComparatorJoinAt;
import cn.qingchengfit.staffkit.allocate.coach.event.ChangeUIEvent;
import cn.qingchengfit.staffkit.allocate.coach.event.SortEvent;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcToggleButton;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by fb on 2017/5/3.
 */

@FragmentWithArgs public class FilterSortCommonFragment extends BaseFragment {

    public static final int SORT_TYPE_ALPHA = 684;//字母排序
    public static final int SORT_TYPE_REGISTER = 685;//最新注册时间排序
    public int sortType = SORT_TYPE_REGISTER;
    @Arg(bundler = AllocateStudentArgsBundler.class) List<CommonAllocateDetailItem> datas = new ArrayList<>();
	QcToggleButton tvSortRegister;
	QcToggleButton tvSortAlpha;
	QcToggleButton tvSortFilter;
    private ArrayMap<String, Integer> alphabetSort = new ArrayMap<>();

    @Override public String getFragmentName() {
        return FilterSortCommonFragment.class.getName();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fliter, container, false);
      tvSortRegister = (QcToggleButton) view.findViewById(R.id.tv_sort_register);
      tvSortAlpha = (QcToggleButton) view.findViewById(R.id.tv_sort_alpha);
      tvSortFilter = (QcToggleButton) view.findViewById(R.id.tv_sort_filter);

      sortData(datas);
        initBus();
        return view;
    }

    private void initView() {
        tvSortAlpha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) sortDataByAlpah(datas);
            }
        });
        tvSortRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) sortDataByRegister(datas);
            }
        });
        tvSortFilter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (getActivity() instanceof AllocateCoachActivity) {
                    ((AllocateCoachActivity) getActivity()).drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
    }

    private void initBus() {
        RxBus.getBus().register(ChangeUIEvent.class).subscribe(new Action1<ChangeUIEvent>() {
            @Override public void call(ChangeUIEvent changeUIEvent) {
                if (tvSortFilter != null) tvSortFilter.setChecked(changeUIEvent.isChange);
            }
        });
    }

    public void refreshView() {
        tvSortRegister.setChecked(sortType == SORT_TYPE_REGISTER);
        tvSortAlpha.setChecked(sortType == SORT_TYPE_ALPHA);
    }

    public void sortData(List<CommonAllocateDetailItem> datas) {
        alphabetSort.clear();
        switch (sortType) {
            case SORT_TYPE_ALPHA:
                sortDataByAlpah(datas);
                break;
            case SORT_TYPE_REGISTER:
                sortDataByRegister(datas);
                break;
        }
    }

    public void sortDataByAlpah(List<CommonAllocateDetailItem> datas) {
        sortType = SORT_TYPE_ALPHA;
        this.datas = datas;
        Collections.sort(datas, new ItemComparator());
        String tag = "";
        for (int i = 0; i < datas.size(); i++) {
            QcStudentBean bean = datas.get(i).getData();
            if (!bean.head.equalsIgnoreCase(tag)) {
                tag = bean.head;
                alphabetSort.put(tag, i);
            }
        }
        refreshView();
        RxBus.getBus().post(new SortEvent(SortEvent.SORT_BY_ALPHABET, datas));
    }

    public void sortDataByRegister(List<CommonAllocateDetailItem> datas) {
        sortType = SORT_TYPE_REGISTER;
        Collections.sort(datas, new ItemComparatorJoinAt());
        refreshView();
        RxBus.getBus().post(new SortEvent(SortEvent.SORT_BY_REGEISTER, datas));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
