package cn.qingchengfit.saasbase.student.views.home;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saasbase.student.utils.StudentCompareByAlphabet;
import cn.qingchengfit.saasbase.student.utils.StudentCompareJoinAt;
import cn.qingchengfit.views.fragments.BaseListFragment;
import cn.qingchengfit.widgets.AlphabetLessView;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/10/30.
 */

public class StudentSortFragment extends BaseListFragment {
    public static int ORDER_TYPE_REGIST = 0;//按注册顺序排序
    public static int ORDER_TYPE_ALPHABET = 1;//按字母表顺序排序
    public List<QcStudentBean> qcStudentBeens = new ArrayList<>();
    private int orderType = 0;//默认顺序为 注册时间排序

    @Override
    public int getNoDataIconRes() {
        return R.drawable.schedules_no_data;
    }

    @Override
    public String getNoDataStr() {
        return "暂无会员数据";
    }

    public void setData(List<QcStudentBean> studentBeen) {
        if (studentBeen != null) {
            qcStudentBeens.clear();
            qcStudentBeens.addAll(studentBeen);
        }
        addItems();
    }

    public void setData(List<QcStudentBean> studentBeen, int type) {
        if (studentBeen != null) {
            qcStudentBeens.clear();
            qcStudentBeens.addAll(studentBeen);
        }
        changeOrderType(type);
    }

    public void changeOrderType(int type) {
        orderType = type;
        addItems();
    }

    @Override
    public boolean isBlockTouch() {
        return false;
    }

    private void addItems() {
        if (commonFlexAdapter != null) {
            if (orderType == 1) {
                for (QcStudentBean qcStudentBeen : qcStudentBeens) {
                    if (qcStudentBeen.head() != null && (!AlphabetLessView.Alphabet.contains(qcStudentBeen.head())
                            || TextUtils.isEmpty(qcStudentBeen.head()))) {
                        qcStudentBeen.setHead("~");
                    }
                }
                Collections.sort(qcStudentBeens, new StudentCompareByAlphabet());//按字母排序
                datas.clear();
                if (qcStudentBeens.size() > 0) {
                    String head = qcStudentBeens.get(0).head();
                    datas.add(new StickerDateItem(head.toUpperCase()));
                    for (QcStudentBean qcStudentBeen : qcStudentBeens) {
                        if (!qcStudentBeen.head().equalsIgnoreCase(head)) {
                            datas.add(new StickerDateItem(qcStudentBeen.head().toUpperCase()));
                            head = qcStudentBeen.head();
                        }
                        datas.add(instanceItem(qcStudentBeen));
                    }
                } else {
                    datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无会员"));
                }
            } else {//按注册时间排序
                datas.clear();
                Collections.sort(qcStudentBeens, new StudentCompareJoinAt());
                if (qcStudentBeens.size() > 0) {
                    for (QcStudentBean qcStudentBeen : qcStudentBeens) {
                        datas.add(instanceItem(qcStudentBeen));
                    }
                } else {
                    datas.add(commonNoDataItem);
                }
            }
            commonFlexAdapter.updateDataSet(datas);
        }
    }

    public void filter(String s) {
        commonFlexAdapter.setSearchText(s);
        commonFlexAdapter.filterItems(datas);
    }

    protected AbstractFlexibleItem instanceItem(QcStudentBean qcStudentBean) {
        return new StudentItem(qcStudentBean);
    }

    public void scrollToPosition(int position, String s) {
        if ("#".equals(s)) s = "~";
        linearLayoutManager.scrollToPositionWithOffset(getPositionForSection(s.charAt(0)), 0);
    }

    public void togglePositionSelect(int positionSelect) {
        commonFlexAdapter.toggleSelection(positionSelect);
        commonFlexAdapter.notifyItemChanged(positionSelect);
    }

    public void selectAll() {
        commonFlexAdapter.selectAll();
        commonFlexAdapter.notifyDataSetChanged();
    }

    public void clearSelection() {
        commonFlexAdapter.clearSelection();
        commonFlexAdapter.notifyDataSetChanged();
    }

    public int getItemCount() {
        return commonFlexAdapter.getItemCount();
    }

    public int getSelectedItemCount() {
        return commonFlexAdapter.getSelectedItemCount();
    }

    public List<String> getSelectedItemIds() {
        List<Integer> integers = get();
        List<String> strings = new ArrayList<>();
        for (Integer pos : integers) {
            strings.add(qcStudentBeens.get(pos).id());
        }
        return strings;
    }

    boolean isSrlEnable = true;

    public void setSwipeRefreshLayoutEnable(boolean isEnable) {
        this.isSrlEnable = isEnable;
        if (srl != null)
            srl.setEnabled(isSrlEnable);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setSwipeRefreshLayoutEnable(isSrlEnable);
    }

    public List<Integer> get() {
        return commonFlexAdapter.getSelectedPositions();
    }

    private int getPositionForSection(char section) {
        if (qcStudentBeens.size() == 0) return -1;
        for (int i = 0; i < datas.size(); i++) {
            AbstractFlexibleItem item = datas.get(i);
            if (item instanceof StickerDateItem) {
                if (((StickerDateItem) item).getDate().toUpperCase().charAt(0) == section) {
                    return i;
                }
            }
        }
        return -1;
    }


}
