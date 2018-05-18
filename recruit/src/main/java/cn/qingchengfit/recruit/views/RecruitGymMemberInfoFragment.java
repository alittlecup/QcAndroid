package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

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
 * Created by Paper on 2017/6/9.
 */
@FragmentWithArgs public class RecruitGymMemberInfoFragment extends BaseFragment {

  @Arg String strArea;
  @Arg Integer staffNum;
  @Arg Integer memberNum;
  @Arg Integer trainerNum;

	TextView tvGymArea;
	TextView tvStaffNum;
	TextView tvMenberNum;
	TextView tvTrainerNum;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitGymMemberInfoFragmentBuilder.injectArguments(this);
    if (strArea == null) strArea = "0";
    if (staffNum == null) staffNum = 0;
    if (memberNum == null) memberNum = 0;
    if (trainerNum == null) trainerNum = 0;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_gym_member_info, container, false);
    tvGymArea = (TextView) view.findViewById(R.id.tv_gym_area);
    tvStaffNum = (TextView) view.findViewById(R.id.tv_staff_num);
    tvMenberNum = (TextView) view.findViewById(R.id.tv_menber_num);
    tvTrainerNum = (TextView) view.findViewById(R.id.tv_trainer_num);

    int area = 0;
    try {
      area = (int) Float.parseFloat(strArea);
    } catch (Exception e) {

    }
    tvGymArea.setText(area == 0 ? "--" : (area + "㎡"));
    tvStaffNum.setText(staffNum == 0 ? "--" : (staffNum + "人"));
    tvMenberNum.setText(memberNum == 0 ? "--" : (memberNum + "人"));
    tvTrainerNum.setText(trainerNum == 0 ? "--" : (trainerNum + "人"));
    view.setBackgroundResource(R.color.white);
    return view;
  }

  @Override public String getFragmentName() {
    return RecruitGymMemberInfoFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
