//package cn.qingchengfit.recruit.views.resume;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.items.RectAddItem;
//import cn.qingchengfit.recruit.R;
//import cn.qingchengfit.recruit.R2;
//import cn.qingchengfit.recruit.item.Image120DelItem;
//import cn.qingchengfit.views.fragments.BaseFragment;
//import cn.qingchengfit.widgets.CommonFlexAdapter;
//import com.hannesdorfmann.fragmentargs.FragmentArgs;
//import com.hannesdorfmann.fragmentargs.annotation.Arg;
//import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import java.util.ArrayList;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2017/6/15.
// */
//@FragmentWithArgs
//public class ResumeEditPhtopsFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {
//    @Arg ArrayList<String> phots;
//
//    CommonFlexAdapter commonFlexAdapter;
//    @BindView(R2.id.toolbar) Toolbar toolbar;
//    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
//    @BindView(R2.id.rv) RecyclerView rv;
//
//    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FragmentArgs.inject(this);
//    }
//
//    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_resume_cm_list, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
//        rv.setLayoutManager(new GridLayoutManager(getContext(),3));
//        rv.setAdapter(commonFlexAdapter);
//        commonFlexAdapter.addItem(new RectAddItem());
//        return view;
//    }
//
//    @Override protected void onFinishAnimation() {
//        super.onFinishAnimation();
//        commonFlexAdapter.clear();
//        if (commonFlexAdapter != null && rv != null){
//            for (int i = 0; i < phots.size(); i++) {
//                commonFlexAdapter.addItem(new Image120DelItem(phots.get(i)));
//            }
//            commonFlexAdapter.addItem(new RectAddItem());
//        }
//
//
//    }
//
//    @Override public String getFragmentName() {
//        return ResumeEditPhtopsFragment.class.getName();
//    }
//
//    @Override public boolean onItemClick(int i) {
//        if (commonFlexAdapter.getItem(i) instanceof Image120DelItem){
//
//        }else if (commonFlexAdapter.getItem(i) instanceof RectAddItem){
//        }
//        return true;
//    }
//
//    @Override public void onDestroyView() {
//        super.onDestroyView();
//    }
//}
