package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.item.ResumeBaseInfoItem;
import cn.qingchengfit.recruit.item.ResumeCertificateItem;
import cn.qingchengfit.recruit.item.ResumeEduExpItem;
import cn.qingchengfit.recruit.item.ResumeIntentImgShowItem;
import cn.qingchengfit.recruit.item.ResumeIntentItem;
import cn.qingchengfit.recruit.item.ResumeTitleItem;
import cn.qingchengfit.recruit.item.ResumeWebDescItem;
import cn.qingchengfit.recruit.item.ResumeWorkExpItem;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/6/9.
 */
public class ResumeHomeFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener{

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R2.id.tv_resume_completed) TextView tvResumeCompleted;
    @BindView(R2.id.tv_resume_open) TextView tvResumeOpen;
    @BindView(R2.id.rv) RecyclerView rv;
    CommonFlexAdapter commonFlexAdapter;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        commonFlexAdapter = new CommonFlexAdapter(new ArrayList(),this);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rv.setAdapter(commonFlexAdapter);
        onMyResume();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("我的简历");
    }

    void onMyResume(){
        commonFlexAdapter.addItem(new ResumeTitleItem(0,getContext()));
        commonFlexAdapter.addItem(new ResumeBaseInfoItem());
        commonFlexAdapter.addItem(new ResumeTitleItem(1,getContext()));
        commonFlexAdapter.addItem(new ResumeIntentItem());
        commonFlexAdapter.addItem(new ResumeTitleItem(2,getContext()));
        List<String> imgs = new ArrayList<>();
        imgs.add("https://ws4.sinaimg.cn/large/006tNc79ly1fg5esjtk4bj31kw0veq7d.jpg");
        imgs.add("https://ws4.sinaimg.cn/large/006tNc79ly1fg5esjtk4bj31kw0veq7d.jpg");

        commonFlexAdapter.addItem(new ResumeIntentImgShowItem(imgs));
        commonFlexAdapter.addItem(new ResumeTitleItem(3,getContext()));
        commonFlexAdapter.addItem(new ResumeWorkExpItem(new WorkExp(),getContext()));
        commonFlexAdapter.addItem(new ResumeTitleItem(4,getContext()));
        commonFlexAdapter.addItem(new ResumeCertificateItem());
        commonFlexAdapter.addItem(new ResumeTitleItem(5,getContext()));
        commonFlexAdapter.addItem(new ResumeEduExpItem());
        commonFlexAdapter.addItem(new ResumeTitleItem(6,getContext()));
        commonFlexAdapter.addItem(new ResumeWebDescItem("哈哈哈"));
    }

    @Override public String getFragmentName() {
        return ResumeHomeFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int i) {
        return false;
    }
}
