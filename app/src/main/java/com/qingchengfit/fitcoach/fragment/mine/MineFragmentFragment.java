package com.qingchengfit.fitcoach.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.GuideWindow;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import cn.qingchengfit.widgets.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.schedule.SpecialWebActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import java.util.Locale;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
public class MineFragmentFragment extends BaseFragment {

    @BindView(R.id.img_header) ImageView imgHeader;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.layout_my_orders) LinearLayout layoutMyOrders;
    private Unbinder unbinder;
    private QcCoachRespone.DataEntity.CoachEntity user;
    private Subscription sp1;
    private GuideWindow gd1;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.mine);
        toolbar.inflateMenu(R.menu.menu_setting);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
                toBaseInfo.putExtra("to", 0);
                startActivity(toBaseInfo);
                return false;
            }
        });
        if (App.gUser != null) {
            tvName.setText(App.gUser.username);
            Glide.with(getContext())
                .load(App.gUser.avatar)
                .asBitmap()
                .error(App.gUser.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                .into(new CircleImgWrapper(imgHeader, getContext()));
        }
        return view;
    }

    @Override public void onResume() {
        super.onResume();
        if (getActivity() instanceof Main2Activity && ((Main2Activity) getActivity()).getCurrrentPage() == 3) queryData();
    }

    @Override protected void onVisible() {
        super.onVisible();
        if (getContext() != null && layoutMyOrders != null && getActivity() instanceof Main2Activity) {
            if (((Main2Activity) getActivity()).ordersCount > 0 && !PreferenceUtils.getPrefBoolean(getContext(),
                App.coachid + "_has_show_orders", false)) {
                showGuide();
            }
        }
    }

    @Override protected void onInVisible() {
        super.onInVisible();
        hideGuide();
    }

    public void showGuide() {
        if (gd1 == null) gd1 = new GuideWindow(getContext(), "点击此处查看订单和门票", GuideWindow.DOWN);
        gd1.show(layoutMyOrders);
    }
    public void hideGuide(){
        if (gd1 != null)
            gd1.dismiss();
    }

    public void queryData() {
        if (sp1 != null && !sp1.isUnsubscribed()) sp1.unsubscribe();
        sp1 = QcCloudClient.getApi().getApi.qcGetCoach(App.coachid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<QcCoachRespone>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcCoachRespone qcCoachRespone) {
                    if (ResponseConstant.checkSuccess(qcCoachRespone)) {
                        user = qcCoachRespone.getData().getCoach();
                        if (user != null && tvName != null && imgHeader != null) {
                            tvName.setText(user.getUsername());
                            Glide.with(getContext())
                                .load(user.getAvatar())
                                .asBitmap()
                                .error(App.gUser.gender == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female)
                                .into(new CircleImgWrapper(imgHeader, getContext()));
                        }
                    } else {
                        ToastUtils.show(qcCoachRespone.getMsg());
                    }
                }
            });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.layout_header, R.id.layout_my_page, R.id.layout_my_resume, R.id.layout_my_courseplan, R.id.layout_my_orders })
    public void onClickFunction(View view) {
        switch (view.getId()) {
            case R.id.layout_header:
                Intent toBaseInfo = new Intent(getActivity(), SettingActivity.class);
                toBaseInfo.putExtra("to", 1);
                startActivity(toBaseInfo);
                break;
            case R.id.layout_my_page:
                //我的主页
                Intent toStudnet = new Intent(getActivity(), SpecialWebActivity.class);
                String s = "";
                toStudnet.putExtra("url", Configs.HOST_STUDENT_PREVIEW + s);
                startActivity(toStudnet);
                break;
            case R.id.layout_my_resume:
                Intent toResume = new Intent(getActivity(), SpecialWebActivity.class);
                toResume.putExtra("to", 1);
                toResume.putExtra("url", String.format(Locale.CHINA, Configs.HOST_RESUME, App.coachid));
                startActivity(toResume);
                break;
            case R.id.layout_my_courseplan:
                Intent tCoursePlan = new Intent(getContext(), FragActivity.class);
                tCoursePlan.putExtra("type", 14);
                startActivity(tCoursePlan);
                break;
            case R.id.layout_my_orders:
                if (gd1 != null) {
                    gd1.dismiss();
                }
                PreferenceUtils.setPrefBoolean(getContext(), App.coachid + "_has_show_orders", true);
                WebActivity.startWeb(Configs.Server + Configs.HOST_ORDERS, getContext());
                break;
            //case R.id.layout_baseinfo:
            //    Intent toStudnet = new Intent(getActivity(), StudentOrderPreviewActivity.class);
            //    String s = "";
            //    toStudnet.putExtra("url", Configs.HOST_STUDENT_PREVIEW + s);
            //    startActivity(toStudnet);
            //    break;
            //case R.id.layout_my_meeting:
            //    //修改个人介绍
            //    Intent toSelfInfo = new Intent(getActivity(), SettingActivity.class);
            //    toSelfInfo.putExtra("to", 5);
            //    toSelfInfo.putExtra("desc",user.getDescription());
            //    startActivity(toSelfInfo);
            //    break;
            //case R.id.layout_my_comfirm:
            //    Intent toComfirm = new Intent(getActivity(), SettingActivity.class);
            //    toComfirm.putExtra("to", 3);
            //    startActivity(toComfirm);
            //    break;
            //case R.id.layout_my_exp:
            //    Intent toExp = new Intent(getActivity(), SettingActivity.class);
            //    toExp.putExtra("to", 4);
            //    startActivity(toExp);
            //    break;
        }
    }
}
