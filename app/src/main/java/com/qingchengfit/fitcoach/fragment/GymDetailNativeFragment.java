package com.qingchengfit.fitcoach.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcGymDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
import com.qingchengfit.fitcoach.http.bean.ShopCourse;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymDetailNativeFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gym_name)
    TextView gymName;
    @Bind(R.id.gym_brand)
    TextView gymBrand;
    @Bind(R.id.gym_address)
    LinearLayout gymAddress;
    @Bind(R.id.gym_count)
    TextView gymCount;
    @Bind(R.id.gym_student_count)
    LinearLayout gymStudentCount;
    @Bind(R.id.opentime)
    TextView opentime;
    @Bind(R.id.course_sum)
    TextView courseSum;
    @Bind(R.id.add_course)
    TextView addCourse;
    @Bind(R.id.course_total_layout)
    RelativeLayout courseTotalLayout;
    @Bind(R.id.gym_img)
    ImageView gymImg;
    @Bind(R.id.linearlayout)
    LinearLayout linearlayout;
    @Bind(R.id.gym_title_tag)
    ImageView gymTitleTag;
    private long mId;
    private boolean mIsPrivate;
    private Subscription mHttpSc;
    private String mModel;
    private MaterialDialog alertDialog;

    public GymDetailNativeFragment() {
    }

    /**
     * @param id 健身房id
     * @return
     */
    public static GymDetailNativeFragment newInstance(long id, String model) {

        Bundle args = new Bundle();
        args.putString("model", model);
        args.putLong("id", id);
        GymDetailNativeFragment fragment = new GymDetailNativeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong("id");
            mModel = getArguments().getString("model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_detail_native, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(item -> {
            if (mModel.equals("service") && mId == 1) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.web_frag_layout, AddSelfGymFragment.newInstance(App.coachid))
                        .addToBackStack(null)
                        .commit();
            } else {
                if (alertDialog == null) {
                    alertDialog = new MaterialDialog.Builder(getContext())
                            .content("无权编辑该健身房信息")
                            .autoDismiss(true)
                            .positiveText(R.string.common_i_konw)
                            .build();
                }
                alertDialog.show();
            }
            return true;
        });
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.primary));
        drawable.setAlpha(50);

        courseTotalLayout.setBackground(drawable);
        init();
        return view;
    }

    public void init() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", Long.toString(mId));

        mHttpSc = QcCloudClient.getApi().getApi.qcGetGymDetail(App.coachid, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcGymDetailResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcGymDetailResponse qcGymDetailResponse) {
                        Glide.with(App.AppContex).load(qcGymDetailResponse.data.service.photo).asBitmap().into(new CircleImgWrapper(gymImg, App.AppContex));
                        if (qcGymDetailResponse.data.shop != null && qcGymDetailResponse.data.shop.shop!=null) {
                            List<QcPrivateGymReponse.OpenTime> openTime = qcGymDetailResponse.data.shop.shop.open_time;

                            StringBuffer ot = new StringBuffer();
                            try {
                                ot
                                        .append("周一").append(openTime.get(0).start).append("-").append(openTime.get(0).end)
                                        .append(",周二").append(openTime.get(1).start).append("-").append(openTime.get(1).end)
                                        .append("\n周三").append(openTime.get(2).start).append("-").append(openTime.get(2).end)
                                        .append(",周四").append(openTime.get(3).start).append("-").append(openTime.get(3).end)
                                        .append("\n周五").append(openTime.get(4).start).append("-").append(openTime.get(4).end)
                                        .append(",周六").append(openTime.get(5).start).append("-").append(openTime.get(5).end)
                                        .append("\n周日").append(openTime.get(6).start).append("-").append(openTime.get(6).end);
                                opentime.setText(ot.toString());
                            } catch (Exception e) {

                            }
                        }
                        toolbar.setTitle(qcGymDetailResponse.data.service.name);
                        if (qcGymDetailResponse.data.service.model.equals("service") && qcGymDetailResponse.data.service.type == 1) {
                            gymTitleTag.setVisibility(View.GONE);
                        } else if (qcGymDetailResponse.data.service.model.equals("gym")) {
                            gymTitleTag.setVisibility(View.VISIBLE);
                        }
                        gymName.setText(qcGymDetailResponse.data.service.name);
//                        gymBrand.setText(qcGymDetailResponse.data.service.);

                        //团课私教数量
                        courseSum.setText("0节团课,2门团课");
                        gymCount.setText(qcGymDetailResponse.data.shop.courses_count + "门课程, " + qcGymDetailResponse.data.shop.user_count + "名学员");
                        for (ShopCourse course : qcGymDetailResponse.data.shop.courses) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_course,null);
                            TextView text1 = (TextView)view.findViewById(R.id.text1);
                            TextView text2 = (TextView)view.findViewById(R.id.text2);
                            TextView text3 = (TextView)view.findViewById(R.id.text3);
                            ImageView img = (ImageView)view.findViewById(R.id.img);
                            ImageView righticon = (ImageView)view.findViewById(R.id.righticon);
                            LogUtil.e("course .......");
                            if (course != null){
                                LogUtil.e("course != null");
                                Glide.with(App.AppContex).load(course.image_url).into(img);
                                text1.setText(course.name);
                                text2.setText("时长"+course.length+"min");
                                text3.setText("累计"+course.course_count+"节,服务"+course.service_count+"人次");
                                if (course.is_private)
                                    righticon.setVisibility(View.VISIBLE);
                                else righticon.setVisibility(View.GONE);
                            }else {
                                LogUtil.e("course == null");
                            }
                            linearlayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(90f,getResources())));
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mHttpSc != null && !mHttpSc.isUnsubscribed())
            mHttpSc.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
