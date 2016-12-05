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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MyHomeActivity;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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
    private QcCoachRespone.DataEntity.CoachEntity user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        if (App.gUser != null) {
            tvName.setText(App.gUser.username);
            Glide.with(getContext()).load(App.gUser.avatar).asBitmap()
                .error(App.gUser.gender == 0?R.drawable.default_manage_male:R.drawable.default_manager_female)
                .into(new CircleImgWrapper(imgHeader, getContext()));
        }
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        queryData();
    }

    public void queryData() {

        QcCloudClient.getApi().getApi.qcGetCoach(App.coachid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<QcCoachRespone>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    
                }

                @Override public void onNext(QcCoachRespone qcCoachRespone) {
                    if (ResponseConstant.checkSuccess(qcCoachRespone)) {
                        user = qcCoachRespone.getData().getCoach();
                        if (user!=null){
                            tvName.setText(user.getUsername());
                            Glide.with(getContext()).load(user.getAvatar()).asBitmap()
                                .error(App.gUser.gender == 0?R.drawable.default_manage_male:R.drawable.default_manager_female)
                                .into(new CircleImgWrapper(imgHeader, getContext()));
                        }
                    }else {
                        ToastUtils.show(qcCoachRespone.getMsg());
                    }
                }
            });
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
                Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
                toBaseInfo.putExtra("to", 1);
                startActivity(toBaseInfo);
                break;
            case R.id.layout_baseinfo:
                Intent toHome = new Intent(getActivity(), MyHomeActivity.class);
                startActivity(toHome);
                break;
            case R.id.layout_my_meeting:
                //修改个人介绍
                Intent toSelfInfo = new Intent(getActivity(), SettingActivity.class);
                toSelfInfo.putExtra("to", 5);
                toSelfInfo.putExtra("desc",user.getDescription());
                startActivity(toSelfInfo);
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
