package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.VpFragment;
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
 * Created by Paper on 2017/5/31.
 */
@FragmentWithArgs public class RecruitGymDescFragment extends VpFragment {
  @Arg Gym gym;
  TextView tvDesc;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitGymDescFragmentBuilder.injectArguments(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_gym_desc, container, false);
    tvDesc = view.findViewById(R.id.tv_desc);
    setDesc(gym);
    return view;
  }

  public void setDesc(Gym gym) {
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_gym_menber_info,
            RecruitGymMemberInfoFragmentBuilder.newRecruitGymMemberInfoFragment(
                gym.member_count == null ? 0 : gym.member_count,
                gym.staff_count == null ? 0 : gym.staff_count, gym.area,
                gym.coach_count == null ? 0 : gym.coach_count))
        .commit();
    if (!ListUtils.isEmpty(gym.facilities)) {
      getChildFragmentManager().beginTransaction()
          .replace(R.id.frag_gym_equipment,
              RecruitGymEquipmentFragmentBuilder.newRecruitGymEquipmentFragment(gym))
          .commit();
    }
    tvDesc.setText(gym.description);
  }

  @Override public String getFragmentName() {
    return RecruitGymDescFragment.class.getName();
  }

  @Override public String getTitle() {
    return "公司介绍";
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
