package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentEvaluateFragment extends VpFragment {

    @BindView(R.id.evaluate_tags) TagGroup evaluateTags;
    @BindView(R.id.no_evaluate) LinearLayout noEvaluate;
    private Unbinder unbinder;

    public StudentEvaluateFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_evaluate, container, false);
        unbinder = ButterKnife.bind(this, view);
        evaluateTags.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        QcCloudClient.getApi().getApi.qcGetEvaluate(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(qcEvaluateResponse -> {
                List<String> tags = new ArrayList<String>();
                for (QcEvaluateResponse.DataEntity.TagsEntity entity : qcEvaluateResponse.getData().getTags()) {
                    StringBuffer ss = new StringBuffer();
                    ss.append(entity.getName()).append(" (").append(entity.getCount()).append(")");
                    tags.add(ss.toString());
                }
                return tags;
            })
            .subscribe(strings -> {
                if (evaluateTags != null) {
                    if (strings != null && strings.size() > 0) {
                        evaluateTags.setTags(strings);
                        evaluateTags.setVisibility(View.VISIBLE);
                        noEvaluate.setVisibility(View.GONE);
                    } else {
                        evaluateTags.setVisibility(View.GONE);
                        noEvaluate.setVisibility(View.VISIBLE);
                    }
                }
            }, throwable -> {
            }, () -> {
            });

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public String getTitle() {
        return "学员评价";
    }
}
