package cn.qingchengfit.saasbase.student.views.transfer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.FunnelTwoView;

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
 * // 转化率 图表
 * //Created by yangming on 16/12/5.
 */
public class StudentTransferTopView extends BaseFragment  {

    @BindView(R2.id.funnelview2)
    FunnelTwoView funnelview;

    @BindView(R2.id.tv_lable)
    TextView tvLable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_student_transfer_top, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }




    @Override
    public String getFragmentName() {
        return this.getClass().getName();
    }

    public void onFollowUpConver(StudentTransferBean conver,String title) {
        if (conver != null && conver.create_count != null && conver.following_count != null && conver.member_count != null) {
            ArrayList<Float> counts = new ArrayList<>();
            counts.add(conver.create_count);
            counts.add(conver.following_count);
            counts.add(conver.member_count);
            funnelview.setData(counts);
            funnelview.animateY();
        }
        tvLable.setText(title);
    }

}
