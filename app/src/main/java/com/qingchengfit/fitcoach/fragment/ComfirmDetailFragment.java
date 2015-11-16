package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComfirmDetailFragment extends Fragment {
    public static final String TAG = ComfirmDetailFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recorddetail_title)
    TextView recorddetailTitle;
    @Bind(R.id.recorddetail_host)
    TextView recorddetailHost;
    @Bind(R.id.recorddetail_time)
    TextView recorddetailTime;
    @Bind(R.id.recorddetail_score)
    TextView recorddetailScore;
    @Bind(R.id.recorddetail_comment)
    TextView recorddetailComment;
    @Bind(R.id.comfirm_img)
    ImageView comfirmImg;
    @Bind(R.id.comfirm_createtime)
    TextView comfirmCreatetime;
    @Bind(R.id.record_comfirm_img)
    ImageView recordComfirmImg;
    private int id;

    public ComfirmDetailFragment() {
    }

    public static ComfirmDetailFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        ComfirmDetailFragment fragment = new ComfirmDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comfirm_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("认证详情");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        QcCloudClient.getApi().getApi.qcGetCertificateDetail(id).subscribe(qcCertificateDetailResponse ->
                {

                    if (recorddetailTitle != null) {
                        getActivity().runOnUiThread(() -> {
                            recorddetailTitle.setText(qcCertificateDetailResponse.getData().getCertificate().getName());
                            recorddetailHost.setText(qcCertificateDetailResponse.getData().getCertificate().getOrganization().getName());
                            recorddetailScore.setText(qcCertificateDetailResponse.getData().getCertificate().getGrade());
//                            recorddetailComment.setText(qcCertificateDetailResponse.getData().getCertificate().get);
//                            StringBuffer sb = new StringBuffer();
//                            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getCreated_at())));
//                            sb.append("-");
//                            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getDate_of_issue())));

//                            recorddetailTime.setText(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getDate_of_issue())));
                            StringBuffer sb = new StringBuffer();
                            sb.append("有效期:  ");
                            sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getStart())));
                            sb.append("-");
                            Date d = DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getEnd());
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            c.setTime(d);
                            if (c.get(Calendar.YEAR) == 3000)
                                recorddetailTime.setText("长期有效");
                            else {
                                sb.append(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getEnd())));
                                recorddetailTime.setText(sb.toString());
                            }
                            comfirmCreatetime.setText(DateUtils.getDateDay(DateUtils.formatDateFromServer(qcCertificateDetailResponse.getData().getCertificate().getDate_of_issue())));
                            if (qcCertificateDetailResponse.getData().getCertificate().is_authenticated()) {
                                Glide.with(getActivity()).load(R.drawable.img_record_comfirmed).into(recordComfirmImg);
                            } else
                                Glide.with(getActivity()).load(R.drawable.img_record_uncomfirmed).into(recordComfirmImg);
                            Glide.with(getActivity()).load(qcCertificateDetailResponse.getData().getCertificate().getPhoto()).asBitmap().into(new ScaleWidthWrapper(comfirmImg));
                        });
                    }
                }, throwable -> {
                }, () -> {
                }
        );
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
