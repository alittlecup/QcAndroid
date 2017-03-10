package com.qingchengfit.fitcoach.fragment.personalpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.fragment.ModifyBrifeFragment;
import com.qingchengfit.fitcoach.fragment.ModifyInfoFragment;
import com.qingchengfit.fitcoach.fragment.settings.ImagesFragment;

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
 * Created by Paper on 2017/3/7.
 */
public class EditHomeFragment extends BaseSettingFragment {

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_home, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0,R.drawable.ic_arrow_left,"编辑主页");
        return view;
    }

    @Override public String getFragmentName() {
        return EditHomeFragment.class.getName();
    }

    @OnClick({ R.id.btn_personal_intro, R.id.btn_pics, R.id.btn_base_info }) public void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_personal_intro:
                //fragmentCallBack.onFragmentChange();
                fragmentCallBack.onFragmentChange(ModifyBrifeFragment.newInstance(""));// TODO: 2017/3/7 设置描述 但是这个时候 我又拿不到  尴尬！
                break;
            case R.id.btn_pics:
                fragmentCallBack.onFragmentChange(new ImagesFragment());
                break;
            case R.id.btn_base_info:
                fragmentCallBack.onFragmentChange(ModifyInfoFragment.newInstance("", ""));
                break;
        }
    }
}
