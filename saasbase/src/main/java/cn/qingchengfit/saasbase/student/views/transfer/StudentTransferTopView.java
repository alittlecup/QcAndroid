package cn.qingchengfit.saasbase.student.views.transfer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.FunnelTwoView;
import java.util.ArrayList;

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


    FunnelTwoView funnelview;


    TextView tvLable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_student_transfer_top, container, false);
      funnelview = (FunnelTwoView) view.findViewById(R.id.funnelview2);
      tvLable = (TextView) view.findViewById(R.id.tv_lable);

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
