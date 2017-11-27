package cn.qingchengfit.staffkit.views.student.choose;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.AlphabetHeaderItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ChooseStudentItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.QcStudentComparator;
import cn.qingchengfit.utils.QcStudentCompareJoinAt;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AlphabetLessView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import java.util.ArrayList;
import java.util.Collections;
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
 * Created by Paper on 2017/3/15.
 */
public class ChooseStudentListFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnUpdateListener, IFilterable {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.layout_alphabet) AlphabetLessView layoutAlphabet;
    List<QcStudentBean> originData;
    private CommonFlexAdapter adapter;
    private List<AbstractFlexibleItem> items = new ArrayList<>();
    /**
     * 0，是字母排序
     * 1，是最新注册
     */
    private int sortType = 0;
    private SelectChangeListener listener;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list_chosen, container, false);
        unbinder = ButterKnife.bind(this, view);
        final LinearLayoutManager sm = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(sm);
        recyclerview.addItemDecoration(new FlexibleItemDecoration(getContext())
          .withDivider(R.drawable.divider_horizon_left_44dp,R.layout.item_choose_student)
          .withOffset(1).withBottomEdge(true));
        recyclerview.setNestedScrollingEnabled(false);
        adapter = new CommonFlexAdapter(items, this);
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.showAllHeaders().setNotifyChangeOfUnfilteredItems(false).setNotifyChangeOfUnfilteredItems(true);
        adapter.setMode(SelectableAdapter.Mode.MULTI);
        recyclerview.setAdapter(adapter);
        layoutAlphabet.setOnAlphabetChange(new AlphabetLessView.OnAlphabetChange() {
            @Override public void onChange(int i, String s, int rvpos) {
                try {
                    sm.scrollToPosition(items.indexOf(adapter.getHeaderItems().get(i)));
                } catch (Exception e) {

                }
            }
        });
        return view;
    }

    public void localFilter(String s) {
        if (TextUtils.isEmpty(s)) {
            adapter.setSearchText(null);
            sort(sortType);
        } else {
            adapter.setSearchText(s);
            adapter.filterItems(items, 100);
            adapter.hideAllHeaders();
        }
    }

    /**
     * 0，是字母排序
     * 1，是最新注册
     */
    public void sort(int t) {
        if (adapter != null) adapter.clearSelection();
        sortType = t;
        items.clear();
        layoutAlphabet.clearElement();
        if (originData != null && originData.size() > 0) {
            if (sortType == 0) {
                layoutAlphabet.setVisibility(View.VISIBLE);
                for (int i = 0; i < originData.size(); i++) {
                    if (!"abcdefghijklmnopqrstuvwxyz".contains(originData.get(i).getHead()) || StringUtils.isEmpty(
                        originData.get(i).getHead())) {
                        originData.get(i).setHead("~");
                    }
                }
                Collections.sort(originData, new QcStudentComparator());
                String header = originData.get(0).head();
                AlphabetHeaderItem itemA = new AlphabetHeaderItem(TextUtils.equals(header, "~") ? "#" : header.toUpperCase());
                layoutAlphabet.addElement(header, 0);
                for (int i = 0; i < originData.size(); i++) {
                    QcStudentBean a = originData.get(i);
                    if (!TextUtils.equals(a.head(), header)) {
                        header = a.head();
                        itemA = new AlphabetHeaderItem(TextUtils.equals(header, "~") ? "#" : header.toUpperCase());
                        layoutAlphabet.addElement(header, i);
                    }
                    items.add(new ChooseStudentItem(a, itemA));
                }
                layoutAlphabet.init();
            } else {//sortType == 1
                layoutAlphabet.setVisibility(View.GONE);
                Collections.sort(originData, new QcStudentCompareJoinAt());
                for (int i = 0; i < originData.size(); i++) {
                    items.add(new ChooseStudentItem(originData.get(i), null));
                }
            }
        } else {
            //空状态
        }
        adapter.updateDataSet(items);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i) instanceof ChooseStudentItem) {
                if (DirtySender.studentList.contains(((ChooseStudentItem) adapter.getItem(i)).getUser())) adapter.addSelection(i);
            }
        }
        if (sortType == 0) adapter.showAllHeaders();
        adapter.notifyDataSetChanged();
    }

    public void setDatas(List<QcStudentBean> students, String filterStr) {
        if (students != null) {
            originData = students;
            localFilter(null);
            localFilter(filterStr);
        }
    }

    public void selectAll() {
        adapter.selectAll(R.layout.item_choose_student);
        DirtySender.studentList.clear();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ChooseStudentItem) {
                DirtySender.studentList.add(((ChooseStudentItem) items.get(i)).getUser());
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void clear() {
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
        DirtySender.studentList.clear();
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    //public List<QcStudentBean> getSelectedStudents() {
    //    List<QcStudentBean> ret = new ArrayList<>();
    //    List<Integer> se = adapter.getSelectedPositions();
    //    for (int i = 0; i < se.size(); i++) {
    //        AbstractFlexibleItem item = items.get(se.get(i));
    //        if (item instanceof ChooseStudentItem) {
    //            ret.add(((ChooseStudentItem) item).getUser());
    //        }
    //    }
    //    return ret;
    //}

    public void selectStudent(List<QcStudentBean> students) {
        if (students != null) {
            adapter.clearSelection();
            for (int i = 0; i < students.size(); i++) {
                adapter.toggleSelection(items.indexOf(students.get(i)));
            }
            adapter.notifyDataSetChanged();
        }
    }

    public int getSelectedCount() {
        return adapter.getSelectedItemCount();
    }

    @Override public String getFragmentName() {
        return ChooseStudentListFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        if (items.get(position) instanceof ChooseStudentItem) {
            adapter.toggleSelection(position);
            adapter.notifyItemChanged(position);
            if (adapter.isSelected(position)) {
                DirtySender.studentList.add(((ChooseStudentItem) adapter.getItem(position)).getUser());
            } else {
                DirtySender.studentList.remove(((ChooseStudentItem) adapter.getItem(position)).getUser());
            }

            if (listener != null) listener.onSelectChange(adapter.getSelectedItemCount());
        }

        return false;
    }

    public SelectChangeListener getListener() {
        return listener;
    }

    public void setListener(SelectChangeListener listener) {
        this.listener = listener;
    }

    @Override public boolean filter(String constraint) {
        return false;
    }

    @Override public void onUpdateEmptyView(int size) {
        if (size > 0 && size <= items.size()) {
            adapter.clearSelection();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i) instanceof ChooseStudentItem && DirtySender.studentList.contains(
                    ((ChooseStudentItem) adapter.getItem(i)).getUser())) {
                    adapter.addSelection(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public interface SelectChangeListener {
        void onSelectChange(int count);
    }
}
