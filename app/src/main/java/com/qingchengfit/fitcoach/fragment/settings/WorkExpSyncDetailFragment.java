package com.qingchengfit.fitcoach.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.SyncExpBody;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
 * Created by Paper on 2017/3/7.
 */
public class WorkExpSyncDetailFragment extends BaseSettingFragment {

    @BindView(R.id.host_img) ImageView hostImg;
    @BindView(R.id.host_qc_identify) ImageView hostQcIdentify;
    @BindView(R.id.workexpedit_gym_name) TextView workexpeditGymName;
    @BindView(R.id.host_address) TextView hostAddress;
    @BindView(R.id.host_layout) RelativeLayout hostLayout;
    @BindView(R.id.workexpedit_start_time) CommonInputView workexpeditStartTime;
    @BindView(R.id.workexpedit_start_end) CommonInputView workexpeditStartEnd;
    @BindView(R.id.workexpedit_city) CommonInputView workexpeditCity;
    @BindView(R.id.workexpedit_position) CommonInputView workexpeditPosition;
    @BindView(R.id.workexpedit_descripe) EditText workexpeditDescripe;
    @BindView(R.id.workexpedit_expe_layout) LinearLayout workexpeditExpeLayout;
    @BindView(R.id.tv_group) TextView tvGroup;
    @BindView(R.id.sw_group) ExpandedLayout swGroup;
    @BindView(R.id.tv_private) TextView tvPrivate;
    @BindView(R.id.sw_private) ExpandedLayout swPrivate;
    @BindView(R.id.tv_sales) TextView tvSales;
    @BindView(R.id.sw_sale) ExpandedLayout swSale;
    @BindView(R.id.rootview) ScrollView rootview;

    private QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;

    public static WorkExpSyncDetailFragment newInstance(QcExperienceResponse.DataEntity.ExperiencesEntity e) {
        Bundle args = new Bundle();
        args.putParcelable("e", e);
        WorkExpSyncDetailFragment fragment = new WorkExpSyncDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experiencesEntity = (QcExperienceResponse.DataEntity.ExperiencesEntity) getArguments().getParcelable("e");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workexp_sync, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentCallBack.showToolbar();
        fragmentCallBack.onToolbarMenu(R.menu.menu_save, 0, "工作经历详情");
        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                putWorkExp();
                return true;
            }
        });
        workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
        Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTime(d);
        if (c.get(Calendar.YEAR) == 3000) {
            workexpeditStartEnd.setHint("至今");
        } else {
            workexpeditStartEnd.setHint(DateUtils.Date2YYYYMMDD(d));
        }

        workexpeditDescripe.setText(experiencesEntity.getDescription());
        workexpeditPosition.setHint(experiencesEntity.getPosition());
        if (experiencesEntity.getGym() != null) {
            workexpeditGymName.setText(experiencesEntity.getGym().getName());
            Glide.with(App.AppContex)
                .load(experiencesEntity.getGym().getPhoto())
                .asBitmap()
                .into(new CircleImgWrapper(hostImg, App.AppContex));
            hostAddress.setText(experiencesEntity.getGym().getAddress());
        }
        workexpeditDescripe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(workexpeditDescripe.getViewTreeObserver(), this);
                swGroup.setExpanded(!experiencesEntity.isGroup_is_hidden());
                swPrivate.setExpanded(!experiencesEntity.isPrivate_is_hidden());
                swSale.setExpanded(!experiencesEntity.isSale_is_hidden());
                tvGroup.setText(getString(R.string.exp_group, experiencesEntity.getGroup_course(), experiencesEntity.getGroup_user()));
                tvPrivate.setText(
                    getString(R.string.exp_group, experiencesEntity.getPrivate_course(), experiencesEntity.getPrivate_user()));
                tvSales.setText(getString(R.string.exp_sale, experiencesEntity.getSale()));
            }
        });
        return view;
    }

    void putWorkExp() {
        fragmentCallBack.ShowLoading("正在保存");
        RxRegiste(QcCloudClient.getApi().postApi.qcEditSyncExperience(experiencesEntity.getId(),
            new SyncExpBody.Builder().description(workexpeditDescripe.getText().toString().trim())
                .group_is_hidden(!swGroup.isExpanded())
                .private_is_hidden(!swPrivate.isExpanded())
                .sale_is_hidden(!swSale.isExpanded())
                .build()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                fragmentCallBack.hideLoading();
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    ToastUtils.show("保存成功");
                    getActivity().onBackPressed();
                } else {
                    ToastUtils.show("保存失败");
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                fragmentCallBack.hideLoading();
            }
        }));
    }

    @Override public String getFragmentName() {
        return WorkExpSyncDetailFragment.class.getName();
    }
}
