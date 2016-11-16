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
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.bean.RxbusBatchLooperConfictEvent;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.items.AddBatchCircleItem;
import com.qingchengfit.fitcoach.items.BatchCircleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


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

    @BindView(R.id.course_img)
    ImageView courseImg;
    @BindView(R.id.course_name)
    TextView courseName;
    @BindView(R.id.startdate)
    CommonInputView startdate;
    @BindView(R.id.enddate)
    CommonInputView enddate;
    @BindView(R.id.batch_date)
    RecyclerView batchDate;
    @BindView(R.id.completed)
    Button completed;
    @BindView(R.id.img_private)
    ImageView imgPrivate;
    @BindView(R.id.course_time_long)
    TextView courseTimeLong;
    private TimeDialogWindow pwTime;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_add_batch, container, false);
        unbinder=ButterKnife.bind(this, view);
        if (getParentFragment() instanceof GuideFragment) {
            CoachInitBean bean = ((GuideFragment) getParentFragment()).initBean;
            if (bean.courses == null || bean.courses.size() == 0) {
                getActivity().onBackPressed();
                return view;
            } else {
                Glide.with(getContext()).load(bean.courses.get(0).photo).into(courseImg);
                imgPrivate.setImageResource(bean.courses.get(0).is_private ? R.drawable.ic_course_type_private : R.drawable.ic_course_type_group);
                courseName.setText(bean.courses.get(0).name);
                courseTimeLong.setText(getString(R.string.time_long_d_min, bean.courses.get(0).length / 60));
            }
//            if (bean.batches != null && bean.batches.size() > 0) {
//                InitBatch batch = bean.batches.get(0);
//                startdate.setContent(batch.from_date);
//                enddate.setContent(batch.to_date);
//                CmBean.geTimeRepFromBean()
//                for (int i = 0; i < batch.time_repeats.size(); i++) {
//                    TimeRepeat timeRepeat = batch.time_repeats.get(i);
//                    mData.add(new BatchCircleItem(new CmBean(timeRepeat.weekday)));
//                }
//
//
//            }

        }


        mData.add(0, new AddBatchCircleItem(getString(R.string.add_course_circle)));
        mAdapter = new CommonFlexAdapter(mData, this);
        batchDate.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        batchDate.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        batchDate.setAdapter(mAdapter);
        RxBusAdd(CmBean.class)
                .subscribe(new Action1<CmBean>() {
                    @Override
                    public void call(CmBean cmBean) {
                        List<CmBean> cmBeens = new ArrayList<CmBean>();
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i) instanceof BatchCircleItem) {
                                cmBeens.add(((BatchCircleItem) mData.get(i)).cmBean);
                            }
                        }
                        if (CmBean.checkCmBean(cmBeens, cmBean)) {
                            mData.add(new BatchCircleItem(cmBean));
                            mAdapter.notifyDataSetChanged();
                            RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                        } else {
                            RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                        }


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
        unbinder.unbind();
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

        if (getParentFragment() instanceof GuideFragment) {
            RxBus.getBus().post(new EventStep.Builder().step(2).build());

            showLoading();
            QcCloudClient.getApi().postApi
                    .qcInit(((GuideFragment) getParentFragment()).getInitBean())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponse>() {
                        @Override
                        public void call(QcResponse qcResponse) {
                            hideLoading();
                            if (qcResponse.status == 200) {
                                PreferenceUtils.setPrefString(getContext(), "initSystem", "");
                                Intent toMain = new Intent(getActivity(), Main2Activity.class);
                                startActivity(toMain);
                                getActivity().finish();
                            } else ToastUtils.showDefaultStyle(qcResponse.msg);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            hideLoading();
                            ToastUtils.showDefaultStyle("初始化系统失败!");
                        }
                    });
        }


    }
}
