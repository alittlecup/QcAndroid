package cn.qingchengfit.staffkit.views.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.StudentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/2/27.
 */

public class BindStudentDetailFragment extends BaseFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    StudentAdapter studentAdapter;
    private List<StudentBean> datas = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_student_detail, container, false);
        return view;
    }

    @Override public String getFragmentName() {
        return BindStudentDetailFragment.class.getName();
    }
}
