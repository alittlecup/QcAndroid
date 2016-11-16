package com.qingchengfit.fitcoach.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MeetActivity;
import com.qingchengfit.fitcoach.activity.MyHomeActivity;
import com.qingchengfit.fitcoach.activity.SettingActivity;

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
 * Created by Paper on 16/11/9.
 */
public class MineFragmentFragment extends Fragment {

    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder=ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.mine);
        toolbar.inflateMenu(R.menu.menu_setting);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
                toBaseInfo.putExtra("to", 0);
                startActivity(toBaseInfo);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_header, R.id.layout_baseinfo, R.id.layout_my_meeting, R.id.layout_my_comfirm, R.id.layout_my_exp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_header:
                Intent toHome = new Intent(getActivity(), MyHomeActivity.class);
                startActivity(toHome);
                break;
            case R.id.layout_baseinfo:
                Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
                toBaseInfo.putExtra("to", 1);
                startActivity(toBaseInfo);
                break;
            case R.id.layout_my_meeting:
                //会议
                Intent toMeeting = new Intent(getActivity(), MeetActivity.class);
                startActivity(toMeeting);
                break;
            case R.id.layout_my_comfirm:
                Intent toComfirm = new Intent(getActivity(), SettingActivity.class);
                toComfirm.putExtra("to", 3);
                startActivity(toComfirm);
                break;
            case R.id.layout_my_exp:
                Intent toExp = new Intent(getActivity(), SettingActivity.class);
                toExp.putExtra("to", 4);
                startActivity(toExp);
                break;
        }
    }
}
