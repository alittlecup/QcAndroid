package com.qingchengfit.fitcoach.fragment.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.UpYunClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.CheckableButton;
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
public class GuideSetCourseFragment extends BaseFragment {

    @Bind(R.id.btn_group)
    CheckableButton btnGroup;
    @Bind(R.id.btn_private)
    CheckableButton btnPrivate;
    @Bind(R.id.course_img)
    ImageView courseImg;
    @Bind(R.id.name)
    CommonInputView name;
    @Bind(R.id.time_long)
    CommonInputView timeLong;
    @Bind(R.id.order_count)
    CommonInputView orderCount;
    @Bind(R.id.next_step)
    Button nextStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_set_course, container, false);
        ButterKnife.bind(this, view);
        RxBus.getBus().post(new EventStep.Builder().step(1).build());
        RxBusAdd(EventChooseImage.class)
                .subscribe(new Action1<EventChooseImage>() {
                    @Override
                    public void call(EventChooseImage eventChooseImage) {
                        UpYunClient.rxUpLoad("course/", eventChooseImage.filePath)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        Glide.with(getContext()).load(s).asBitmap().into(new CircleImgWrapper(courseImg,getContext()));

                                    }
                                });
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

    @OnClick({R.id.layout_gym_img, R.id.next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_gym_img:
                ChoosePictureFragmentDialog.newInstance().show(getFragmentManager(),"");
                break;
            case R.id.next_step:
                getFragmentManager().beginTransaction()
                        .replace(R.id.guide_frag,new GuideAddBatchFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @OnClick({R.id.btn_group, R.id.btn_private})
    public void privateChange(View view) {
        switch (view.getId()) {
            case R.id.btn_group:

                break;
            case R.id.btn_private:
                break;
        }
    }
}
