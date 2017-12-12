package cn.qingchengfit.saasbase.student.views.home;

import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.base.Trainer;
import cn.qingchengfit.saasbase.student.views.SimpleStudentListFragment;
import cn.qingchengfit.widgets.QcToggleButton;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * StudentListFragment底部的展示会员列表界面。
 * Created by huangbaole on 2017/10/25.
 */

public class StudentRecyclerViewFragment extends StudentSortFragment   {
    public ObservableBoolean recentCheck = new ObservableBoolean(true);
    public ObservableBoolean headCheck = new ObservableBoolean(false);
    public ObservableBoolean filterCheck = new ObservableBoolean(false);


    public void onRecentButtonClick(View v) {
        if (!recentCheck.get()) {
            changeOrderType(ORDER_TYPE_REGIST);
            recentCheck.set(true);
            headCheck.set(false);
        }
    }

    public void onAlphabetButtonClick(View v) {
        if (!headCheck.get()) {
            changeOrderType(ORDER_TYPE_ALPHABET);
            headCheck.set(true);
            recentCheck.set(false);
        }

    }

    @Override
    protected AbstractFlexibleItem instanceItem(QcStudentBean qcStudentBean) {
        if(callback!=null){
            return callback.generateItem(qcStudentBean);
        }
        return super.instanceItem(qcStudentBean);
    }

    public void onFilterButtonClick(View v) {
        if (!filterCheck.get()) {
            filterCheck.set(true);
        }
        if (null != filterClickListener) {
            filterClickListener.onFilterButtonClick(filterCheck.get());
        }
    }


    private onFilterClickListener filterClickListener;
    private GenerateItemCallback callback;

    public void setCallback(GenerateItemCallback callback) {
        this.callback = callback;
    }

    public void setOnFilterClickListener(onFilterClickListener filterClickListener) {
        this.filterClickListener = filterClickListener;
    }






    public interface onFilterClickListener {
        void onFilterButtonClick(boolean isChecked);
    }
    public  interface GenerateItemCallback{
        AbstractFlexibleItem generateItem(QcStudentBean qcStudentBean);
    }

}
