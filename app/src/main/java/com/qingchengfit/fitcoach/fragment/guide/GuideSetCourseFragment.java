package com.qingchengfit.fitcoach.fragment.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.qingchengfit.widgets.CheckableButton;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.bean.EventStep;
import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.UpYunClient;
import java.util.ArrayList;
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
@FragmentWithArgs public class GuideSetCourseFragment extends BaseFragment {

    @Arg public boolean isPrivate;

    @BindView(R.id.btn_group) CheckableButton btnGroup;
    @BindView(R.id.btn_private) CheckableButton btnPrivate;
    @BindView(R.id.course_img) ImageView courseImg;
    @BindView(R.id.name) CommonInputView name;
    @BindView(R.id.time_long) CommonInputView timeLong;
    @BindView(R.id.order_count) CommonInputView orderCount;
    @BindView(R.id.next_step) Button nextStep;
    @BindView(R.id.guide_title) TextView guideTitle;

    private String imgUrl;

    private Unbinder unbinder;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_set_course, container, false);
        unbinder = ButterKnife.bind(this, view);

        guideTitle.setText(getString(isPrivate?R.string.guide_set_private_course_hint:R.string.guide_set_group_course_hint));
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                UpYunClient.rxUpLoad("course/", eventChooseImage.filePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            Glide.with(getContext()).load(s).into(courseImg);
                            imgUrl = s;
                        }
                    });
            }
        });
        btnGroup.setClick(new View.OnClickListener() {
            @Override public void onClick(View v) {
                btnPrivate.toggle();
            }
        });
        btnPrivate.setClick(new View.OnClickListener() {
            @Override public void onClick(View v) {
                btnGroup.toggle();
            }
        });
        return view;
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.layout_gym_img, R.id.next_step }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_gym_img:
                ChoosePictureFragmentDialog.newInstance().show(getFragmentManager(), "");
                break;
            case R.id.next_step:
                if (name.isEmpty()) {
                    ToastUtils.showDefaultStyle(getString(R.string.err_write_course_name));
                    return;
                }
                if (timeLong.isEmpty()) {
                    ToastUtils.showDefaultStyle(getString(R.string.err_write_course_time_long));
                    return;
                }
                /**
                 * 可约人数限制
                 */
                //                if (orderCount.isEmpty()){
                //                    ToastUtils.showDefaultStyle(getString(R.string.err_write_course_order_num));
                //                    return;
                //                }
                if (getParentFragment() instanceof GuideFragment) {
                    ((GuideFragment) getParentFragment()).initBean.courses = new ArrayList<>();
                    ((GuideFragment) getParentFragment()).initBean.courses.add(new Course.Builder().photo(imgUrl)
                        .name(name.getContent())
                        .capacity(Integer.parseInt(orderCount.getContent()))
                        .is_private(isPrivate)
                        .length((Integer.parseInt(timeLong.getContent()) * 60))
                        .build());

                    RxBus.getBus().post(new CoachInitBean());
                    getFragmentManager().beginTransaction()
                        .replace(R.id.guide_frag, new GuideAddBatchFragment())
                        .addToBackStack(null)
                        .commit();
                }

                break;
        }
    }
}
