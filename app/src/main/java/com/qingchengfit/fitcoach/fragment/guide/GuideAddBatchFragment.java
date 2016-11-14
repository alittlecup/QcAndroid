package com.qingchengfit.fitcoach.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.items.AddBatchCircleItem;
import com.qingchengfit.fitcoach.items.BatchCircleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.functions.Action1;


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
 * Created by Paper on 16/11/14.
 */
public class GuideAddBatchFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @Bind(R.id.course_img)
    ImageView courseImg;
    @Bind(R.id.course_name)
    TextView courseName;
    @Bind(R.id.startdate)
    CommonInputView startdate;
    @Bind(R.id.enddate)
    CommonInputView enddate;
    @Bind(R.id.batch_date)
    RecyclerView batchDate;
    @Bind(R.id.completed)
    Button completed;
    private TimeDialogWindow pwTime;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_add_batch, container, false);
        ButterKnife.bind(this, view);
        mData.add(new AddBatchCircleItem("+课程周期"));
        mAdapter = new CommonFlexAdapter(mData, this);
        batchDate.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        batchDate.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        batchDate.setAdapter(mAdapter);
        RxBusAdd(CmBean.class)
                .subscribe(new Action1<CmBean>() {
                    @Override
                    public void call(CmBean cmBean) {
                        mData.add(0, new BatchCircleItem(cmBean));
                        mAdapter.notifyDataSetChanged();
                    }
                });

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

    @OnClick({R.id.startdate, R.id.enddate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startdate:
                if (pwTime == null)
                    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        startdate.setContent(DateUtils.Date2YYYYMMDD(date));

                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.enddate:
                if (pwTime == null)
                    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        enddate.setContent(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());

                break;
        }
    }

    @Override
    public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof BatchCircleItem) {

        } else if (mAdapter.getItem(position) instanceof AddBatchCircleItem) {
            Intent to = new Intent(getActivity(), ChooseActivity.class);
            to.putExtra("to", ChooseActivity.TO_CHOSSE_CIRCLE);
            startActivity(to);
        }
        return false;
    }

    @OnClick(R.id.completed)
    public void onClick() {


        RxBus.getBus().post(new EventStep.Builder().step(2).build());
    }
}
