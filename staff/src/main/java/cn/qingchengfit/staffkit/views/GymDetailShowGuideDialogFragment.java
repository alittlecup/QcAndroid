package cn.qingchengfit.staffkit.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.GoToGuideEvent;
import cn.qingchengfit.utils.CompatUtils;

/**
 * Created by peggy on 16/6/5.
 */

public class GymDetailShowGuideDialogFragment extends BaseFragment {

    @BindView(R.id.student_preview) TextView studentPreview;
    @BindView(R.id.bg) LinearLayout bg;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gymdetail_guide, container, false);
        unbinder = ButterKnife.bind(this, view);
        //        this.getDialog().setCanceledOnTouchOutside(false);
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Drawable a = ContextCompat.getDrawable(getContext(), R.drawable.ic_circle_info);
        Drawable b = a.mutate();
        DrawableCompat.setTint(a, CompatUtils.getColor(getActivity(), R.color.colorPrimary));
        studentPreview.setCompoundDrawablesWithIntrinsicBounds(b, null, null, null);

        //Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_graph_indicator);
        //Drawable a = drawable.mutate();
        //DrawableCompat.setTint(a, CompatUtils.getColor(getContext(), R.color.white));
        //bg.setBackground(a);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.student_preview) public void onClick() {
        RxBus.getBus().post(new GoToGuideEvent());
        getFragmentManager().popBackStackImmediate();
    }
}
