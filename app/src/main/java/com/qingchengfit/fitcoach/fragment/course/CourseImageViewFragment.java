package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.usecase.bean.SchedulePhoto;
import cn.qingchengfit.staffkit.utils.DateUtils;
import uk.co.senab.photoview.PhotoView;

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
 * Created by Paper on 16/8/8.
 */
public class CourseImageViewFragment extends BaseFragment {

    @Bind(R.id.photoview)
    PhotoView photoview;
    @Bind(R.id.seer)
    TextView seer;
    @Bind(R.id.uploader_date)
    TextView uploaderDate;
    @Bind(R.id.time)
    TextView time;
    private SchedulePhoto mPhoto;

    public static CourseImageViewFragment newInstance(SchedulePhoto photo) {

        Bundle args = new Bundle();
        args.putParcelable("photo", photo);
        CourseImageViewFragment fragment = new CourseImageViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoto = getArguments().getParcelable("photo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_image_viewer, container, false);
        ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("查看大图",false,null,0,null);
        Glide.with(getContext()).load(mPhoto.getPhoto()).placeholder(R.drawable.img_loadingimage).into(photoview);
        seer.setText(mPhoto.is_public()?"": String.format(Locale.CHINA, "仅%s可见", mPhoto.getOwner().name));
        uploaderDate.setText(String.format(Locale.CHINA, "由%s上传", mPhoto.getCreated_by().name).concat(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(mPhoto.getCreated_at()))));
        time.setText(DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(mPhoto.getCreated_at())));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override
    public String getFragmentName() {
        return CourseImageViewFragment.class.getName();
    }

    @Override
    public void onDestroyView() {
        mCallbackActivity.setToolbar("全部课程照片",false,null,0,null);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
