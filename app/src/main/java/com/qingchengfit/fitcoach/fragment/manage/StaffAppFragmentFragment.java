package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.GuideActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


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
 * Created by Paper on 16/11/15.
 *
 * 提示跳转至 工作人员App
 *
 *
 */
public class StaffAppFragmentFragment extends DialogFragment {


    private Unbinder unbinder;

    public static StaffAppFragmentFragment newInstance() {
        Bundle args = new Bundle();
        StaffAppFragmentFragment fragment = new StaffAppFragmentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.title)
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_staff_app, container, false);
        unbinder=ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.close, R.id.open_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.open_app:
                try {
                    Intent toGuide= new Intent(getActivity(), GuideActivity.class);
                    startActivity(toGuide);


//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    ComponentName cn = new ComponentName("cn.qingchengfit.staffkit", "cn.qingchengfit.staffkit.MainActivity");
//                    intent.setComponent(cn);
//                    startActivity(intent);
                }catch (Exception e){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://fir.im/qcfit"));
                    startActivity(i);
                }
                break;
        }
    }
}