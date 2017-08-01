package cn.qingchengfit.staffkit.views.statement.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CourseTypeform;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * Created by Paper on 16/6/29 2016.
 */
public class CourseTypeFormFragment extends BaseFragment {

    @BindView(R.id.group_course_num) TextView groupCourseNum;
    @BindView(R.id.group_server_num) TextView groupServerNum;
    @BindView(R.id.private_course_num) TextView privateCourseNum;
    @BindView(R.id.private_server_num) TextView privateServerNum;
    @BindView(R.id.total_course_num) TextView totalCourseNum;
    @BindView(R.id.total_server_num) TextView totalServerNum;

    public static CourseTypeFormFragment newInstance(List<CourseTypeform> typesgalance) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("form", (ArrayList<CourseTypeform>) typesgalance);
        CourseTypeFormFragment fragment = new CourseTypeFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_type_form, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            ArrayList<CourseTypeform> typesgalance = getArguments().getParcelableArrayList("form");
            if (typesgalance != null) {
                for (int i = 0; i < typesgalance.size(); i++) {
                    CourseTypeform t = typesgalance.get(i);
                    switch (t.course_type) {
                        case Configs.TYPE_PRIVATE:
                            privateCourseNum.setText(String.format(Locale.CHINA, getString(R.string.course_unit), t.course_count));
                            privateServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), t.server_count));
                            break;
                        case Configs.TYPE_GROUP:
                            groupCourseNum.setText(String.format(Locale.CHINA, getString(R.string.course_unit), t.course_count));
                            groupServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), t.server_count));

                            break;
                        default:
                            totalCourseNum.setText(String.format(Locale.CHINA, getString(R.string.course_unit), t.course_count));
                            totalServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), t.server_count));
                            break;
                    }
                }
            }
        }
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
