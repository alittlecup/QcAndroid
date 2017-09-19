package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/6.
 */
public class TopFilterFragment extends BaseFragment {

    public boolean isTodyOnly;
    public int studentStatus;

    @BindView(R.id.tv_student_follow_up_filter_sale) TextView tvStudentFollowUpFilterSale;
    @BindView(R.id.tv_student_follow_up_filter_register) TextView tvStudentFollowUpFilterRegister;
    //@BindView(R.id.tv_student_follow_up_filter_source) TextView tvStudentFollowUpFilterSource;
    //@BindView(R.id.tv_student_follow_up_filter_referrer) TextView tvStudentFollowUpFilterReferrer;
    //@BindView(R.id.tv_student_follow_up_filter_lastest_time) TextView
    //        tvStudentFollowUpFilterLastestTime;
    @BindView(R.id.rl_student_follow_up_filter_sale) RelativeLayout rlStudentFollowUpFilterSale;
    @BindView(R.id.rl_student_follow_up_filter_register) RelativeLayout rlStudentFollowUpFilterRegister;
    //@BindView(R.id.rl_student_follow_up_filter_source) RelativeLayout rlStudentFollowUpFilterSource;
    //@BindView(R.id.rl_student_follow_up_filter_referrer) RelativeLayout
    //        rlStudentFollowUpFilterReferrer;
    //@BindView(R.id.rl_student_follow_up_filter_lastest_time) RelativeLayout
    //        rlStudentFollowUpFilterLastestTime;

    private View.OnClickListener itemOnClick;

    @Inject public TopFilterFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up_top_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        switch (studentStatus) {
            case 0:
                rlStudentFollowUpFilterSale.setVisibility(View.VISIBLE);
                rlStudentFollowUpFilterRegister.setVisibility(View.VISIBLE);
                //rlStudentFollowUpFilterSource.setVisibility(View.VISIBLE);
                //rlStudentFollowUpFilterReferrer.setVisibility(View.VISIBLE);
                //rlStudentFollowUpFilterLastestTime.setVisibility(View.GONE);
                break;
            case 1:
                rlStudentFollowUpFilterSale.setVisibility(View.VISIBLE);
                rlStudentFollowUpFilterRegister.setVisibility(View.GONE);
                //rlStudentFollowUpFilterSource.setVisibility(View.GONE);
                //rlStudentFollowUpFilterReferrer.setVisibility(View.GONE);
                //rlStudentFollowUpFilterLastestTime.setVisibility(View.VISIBLE);
                break;
            case 2:
                rlStudentFollowUpFilterSale.setVisibility(View.VISIBLE);
                rlStudentFollowUpFilterRegister.setVisibility(View.GONE);
                //rlStudentFollowUpFilterSource.setVisibility(View.GONE);
                //rlStudentFollowUpFilterReferrer.setVisibility(View.GONE);
                //rlStudentFollowUpFilterLastestTime.setVisibility(View.GONE);
                break;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    //@OnClick({
    //        R.id.rl_student_follow_up_filter_sale, R.id.rl_student_follow_up_filter_register,
    //        R.id.rl_student_follow_up_filter_source, R.id.rl_student_follow_up_filter_referrer,
    //        R.id.rl_student_follow_up_filter_lastest_time
    //}) public void onClick(View view) {
    //    if (itemOnClick != null) itemOnClick.onClick(view);
    //    // 更换箭头图标 // TODO
    //    switch (view.getId()) {
    //        case R.id.rl_student_follow_up_filter_sale:
    //            ToastUtils.show("sale");
    //            break;
    //        case R.id.rl_student_follow_up_filter_register:
    //            ToastUtils.show("register");
    //            break;
    //        case R.id.rl_student_follow_up_filter_source:
    //            ToastUtils.show("source");
    //            break;
    //        case R.id.rl_student_follow_up_filter_referrer:
    //            ToastUtils.show("referrer");
    //            break;
    //        case R.id.rl_student_follow_up_filter_lastest_time:
    //            ToastUtils.show("lastest_time");
    //            break;
    //    }
    //}

    public void setOnItemClick(View.OnClickListener listener) {
        itemOnClick = listener;
    }
}
