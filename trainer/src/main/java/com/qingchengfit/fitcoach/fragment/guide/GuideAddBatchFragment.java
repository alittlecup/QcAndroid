package com.qingchengfit.fitcoach.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.InitBatch;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.bean.QcResponseSystenInit;
import com.qingchengfit.fitcoach.bean.RxbusBatchLooperConfictEvent;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.items.AddBatchCircleItem;
import com.qingchengfit.fitcoach.items.BatchCircleItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    @BindView(R.id.course_img) ImageView courseImg;
    @BindView(R.id.course_name) TextView courseName;
    @BindView(R.id.startdate) CommonInputView startdate;
    @BindView(R.id.enddate) CommonInputView enddate;
    @BindView(R.id.batch_date) RecyclerView batchDate;
    @BindView(R.id.completed) Button completed;
    @BindView(R.id.img_private) ImageView imgPrivate;
    @BindView(R.id.course_time_long) TextView courseTimeLong;
    private TimeDialogWindow pwTime;
    private CommonFlexAdapter mAdapter;
    private List<AbstractFlexibleItem> mData = new ArrayList<>();
    private Unbinder unbinder;
    private boolean isPrivate = false;
    private Course course;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_add_batch, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getParentFragment() instanceof GuideFragment) {
            CoachInitBean bean = ((GuideFragment) getParentFragment()).initBean;
            if (bean.courses == null || bean.courses.size() == 0) {
                getActivity().onBackPressed();
                return view;
            } else {
                Glide.with(getContext())
                    .load(bean.courses.get(0).photo)
                    .placeholder(R.drawable.img_default_course)
                    .error(R.drawable.img_default_course)
                    .into(courseImg);
                imgPrivate.setImageResource(
                    bean.courses.get(0).is_private ? R.drawable.ic_course_type_private : R.drawable.ic_course_type_group);
                courseName.setText(bean.courses.get(0).name);
                courseTimeLong.setText(getString(R.string.time_long_d_min, bean.courses.get(0).length / 60));
                isPrivate = bean.courses.get(0).is_private;

                course = bean.courses.get(0);

                Calendar c = Calendar.getInstance();
                startdate.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
                c.add(Calendar.MONTH, 2);
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.DATE, -1);
                enddate.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
                if (!bean.courses.get(0).is_private) {
                    ArrayList<Integer> defautxxx1 = new ArrayList<>();
                    defautxxx1.add(1);
                    defautxxx1.add(3);
                    defautxxx1.add(5);
                    defautxxx1.add(7);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtils.getDateFromHHmm("9:00"));
                    calendar.add(Calendar.SECOND, course.length);
                    mData.add(new BatchCircleItem(new CmBean(defautxxx1, DateUtils.getDateFromHHmm("9:00"), calendar.getTime()),
                        bean.courses.get(0).is_private));
                    ArrayList<Integer> defautxxx2 = new ArrayList<>();
                    defautxxx2.clear();
                    defautxxx2.add(2);
                    defautxxx2.add(4);
                    defautxxx2.add(6);
                    calendar.setTime(DateUtils.getDateFromHHmm("14:00"));
                    calendar.add(Calendar.SECOND, course.length);
                    mData.add(new BatchCircleItem(new CmBean(defautxxx2, DateUtils.getDateFromHHmm("14:00"), calendar.getTime()),
                        bean.courses.get(0).is_private));
                } else {
                    ArrayList<Integer> defautxxx1 = new ArrayList<>();
                    defautxxx1.add(1);
                    defautxxx1.add(2);
                    defautxxx1.add(3);
                    defautxxx1.add(4);
                    defautxxx1.add(5);
                    defautxxx1.add(6);
                    defautxxx1.add(7);
                    mData.add(
                        new BatchCircleItem(new CmBean(defautxxx1, DateUtils.getDateFromHHmm("8:00"), DateUtils.getDateFromHHmm("21:00")),
                            bean.courses.get(0).is_private));
                }
                mData.add(mData.size(), new AddBatchCircleItem(getString(R.string.add_course_circle)));
                mAdapter = new CommonFlexAdapter(mData, this);
                batchDate.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
                batchDate.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                batchDate.setAdapter(mAdapter);
            }
        }
        RxBusAdd(CmBean.class).subscribe(new Action1<CmBean>() {
            @Override public void call(CmBean cmBean) {
                List<CmBean> cmBeens = new ArrayList<CmBean>();
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i) instanceof BatchCircleItem) {
                        cmBeens.add(((BatchCircleItem) mData.get(i)).cmBean);
                    }
                }
                if (CmBean.checkCmBean(cmBeens, cmBean)) {
                    mData.add(new BatchCircleItem(cmBean, isPrivate));
                    mAdapter.notifyDataSetChanged();
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(false));
                } else {
                    RxBus.getBus().post(new RxbusBatchLooperConfictEvent(true));
                }
            }
        });

        return view;
    }

    @OnClick(R.id.layout_course) public void onCourse() {
        getActivity().onBackPressed();
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.startdate, R.id.enddate }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startdate:
                if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        startdate.setContent(DateUtils.Date2YYYYMMDD(date));

                        pwTime.dismiss();
                        if (TextUtils.isEmpty(enddate.getContent())) {
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.MONTH, 2);
                            c.set(Calendar.DAY_OF_MONTH, 1);
                            c.add(Calendar.DATE, -1);
                            enddate.setContent(DateUtils.Date2YYYYMMDD(c.getTime()));
                        }
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.enddate:
                if (pwTime == null) pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        enddate.setContent(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }

    @Override public boolean onItemClick(int position) {
        if (mAdapter.getItem(position) instanceof BatchCircleItem) {

        } else if (mAdapter.getItem(position) instanceof AddBatchCircleItem) {
            Intent to = new Intent(getActivity(), ChooseActivity.class);
            to.putExtra("to", ChooseActivity.TO_CHOSSE_CIRCLE);
            if (course != null) to.putExtra("len", (long) course.getLength());

            startActivity(to);
        }
        return true;
    }

    @OnClick(R.id.completed) public void onClick() {
        if (startdate.getContent().isEmpty() || enddate.getContent().isEmpty()) {
            ToastUtils.showDefaultStyle(getString(R.string.err_please_input_start_end));
            return;
        }
        if (mData.size() <= 1) {
            ToastUtils.showDefaultStyle(getString(R.string.err_at_least_one_circle));
            return;
        }

        if (getParentFragment() instanceof GuideFragment) {
            RxBus.getBus().post(new EventStep.Builder().step(2).build());
            List<CmBean> initBatches = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                if (mAdapter.getItem(i) instanceof BatchCircleItem) {
                    initBatches.add(((BatchCircleItem) mAdapter.getItem(i)).cmBean);
                }
            }
            List<InitBatch> initBatches1 = new ArrayList<>();
            initBatches1.add(new InitBatch.Builder().from_date(startdate.getContent())
                .to_date(enddate.getContent())
                .course_name(courseName.getText().toString().trim())
                .time_repeats(CmBean.geTimeRepFromBean(initBatches))
                .build());
            ((GuideFragment) getParentFragment()).initBean.batches = initBatches1;
            showLoading();
            QcCloudClient.getApi().postApi.qcInit(((GuideFragment) getParentFragment()).getInitBean())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseSystenInit>() {
                    @Override public void call(QcResponseSystenInit qcResponse) {
                        hideLoading();
                        if (qcResponse.status == 200) {
                            PreferenceUtils.setPrefString(getContext(), "initSystem", "");
                            Intent toMain = new Intent(getActivity(), Main2Activity.class);
                            toMain.putExtra("to", Main2Activity.INIT);
                            toMain.putExtra("service", qcResponse.data);
                            startActivity(toMain);
                            getActivity().finish();
                        } else {
                            ToastUtils.showDefaultStyle(qcResponse.msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        hideLoading();
                        ToastUtils.showDefaultStyle("初始化系统失败!");
                    }
                });
        }
    }
}
