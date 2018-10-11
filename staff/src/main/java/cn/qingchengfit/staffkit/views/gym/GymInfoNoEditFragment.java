package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import javax.inject.Inject;

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
 * Created by Paper on 2017/4/18.
 */
public class GymInfoNoEditFragment extends BaseFragment {

	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView imgBrand;
	TextView tvGymName;
	TextView tvGymAddress;
	TextView tvGymContact;
	TextView tvGymDesc;

    @Inject GymWrapper gymWrapper;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_info_no_edit, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      imgBrand = (ImageView) view.findViewById(R.id.img_brand);
      tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
      tvGymAddress = (TextView) view.findViewById(R.id.tv_gym_address);
      tvGymContact = (TextView) view.findViewById(R.id.tv_gym_contact);
      tvGymDesc = (TextView) view.findViewById(R.id.tv_gym_desc);

      initToolbar(toolbar);
        initGym(gymWrapper.getCoachService());
      SensorsUtils.trackScreen(      this.getClass().getCanonicalName());
        return view;
    }

    private void initGym(CoachService coachService) {
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(coachService.getPhoto()))
            .asBitmap()
            .into(new CircleImgWrapper(imgBrand, getContext()));
        tvGymName.setText(coachService.getName());
        tvGymAddress.setText(coachService.getAddress());
        tvGymContact.setText(coachService.getPhone());
        tvGymDesc.setText("未填写");
        //tvGymAddress.setText(TextUtils.isEmpty(coachService.));
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("场馆信息");
    }

    @Override public String getFragmentName() {
        return GymInfoNoEditFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
