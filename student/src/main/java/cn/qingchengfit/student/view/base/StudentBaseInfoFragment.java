package cn.qingchengfit.student.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.FragmentStudentBaseInfoBinding;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/11/20 2015.
 */
@Leaf(module = "student", path = "/student/detail/baseInfo")
public class StudentBaseInfoFragment extends BaseFragment {

    @Need
    StudentBaseInfoBean bean;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StudentParamsInjector.inject(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        //mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ////        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_phone, "电话", "123123123"));
        ////        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_city, "城市", "北京"));
        ////        datas.add(new BaseInfoBean(R.drawable.ic_baseinfo_wechat, "微信", "北京朝阳"));
        //mAdapter = new BaseInfoAdapter(datas);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setAdapter(mAdapter);

        FragmentStudentBaseInfoBinding
            binding = FragmentStudentBaseInfoBinding.inflate(inflater, container, false);
        binding.setToolbarModel(new ToolbarModel("基本信息"));
        initToolbar(binding.includeToolbar.toolbar);
        //binding.toolbar.setText("基本信息");
        //binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        if (bean != null){
            PhotoUtils.smallCircle(binding.imageStudentBaseInfoHead, bean.getHead(),
                cn.qingchengfit.saascommon.R.drawable.ic_default_staff_man_head,
                cn.qingchengfit.saascommon.R.drawable.ic_default_staff_man_head);
            binding.inputStudentBaseInfoPhone.setContent(bean.getPhone());
            binding.inputStudentBaseInfoBirthday.setContent(bean.getBirthday());
            binding.inputStudentBaseInfoAddress.setContent(bean.getAddress());
            binding.inputStudentBaseInfoRegister.setContent(bean.getRegister());
        }

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
