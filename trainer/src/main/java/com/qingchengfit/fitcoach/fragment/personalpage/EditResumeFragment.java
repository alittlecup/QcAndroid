package com.qingchengfit.fitcoach.fragment.personalpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.fragment.ModifyBrifeFragment;
import com.qingchengfit.fitcoach.fragment.RecordFragment;
//import com.qingchengfit.fitcoach.fragment.WorkExepSettingFragment;

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
 * Created by Paper on 2017/2/22.
 */

@FragmentWithArgs public class EditResumeFragment extends BaseSettingFragment {

    @Arg String desc;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_resume, container, false);
      view.findViewById(R.id.btn_base_info).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickFunction(v);
        }
      });
      view.findViewById(R.id.btn_train_grow).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickFunction(v);
        }
      });
      view.findViewById(R.id.btn_work_exp).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickFunction(v);
        }
      });
      view.findViewById(R.id.btn_personal_intro).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickFunction(v);
        }
      });

      fragmentCallBack.showToolbar();
        fragmentCallBack.onToolbarMenu(0, 0, "编辑我的简历");
        return view;
    }


    public void onClickFunction(View view) {
        switch (view.getId()) {
            case R.id.btn_base_info:
                routeTo("user","/edit/",null);
                //fragmentCallBack.onFragmentChange(ModifyInfoFragment.newInstance("", ""));
                break;
            case R.id.btn_train_grow:
                fragmentCallBack.onFragmentChange(new RecordFragment());
                break;
            case R.id.btn_work_exp:
              //fragmentCallBack.onFragmentChange(new WorkExepSettingFragment());
                break;
            case R.id.btn_personal_intro:
                fragmentCallBack.onFragmentChange(ModifyBrifeFragment.newInstance(desc));
                break;
        }
    }
}
