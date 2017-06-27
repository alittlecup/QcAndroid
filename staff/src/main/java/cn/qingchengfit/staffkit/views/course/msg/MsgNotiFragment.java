package cn.qingchengfit.staffkit.views.course.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.body.ShopConfigBody;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/1/12.
 */
@FragmentWithArgs public class MsgNotiFragment extends BaseFragment implements MsgNotiPresenter.MVPView {
    @BindView(R.id.sw_begin_noti_min) CommonInputView swBeginNotiMin;
    @BindView(R.id.sw_begin_noti) ExpandedLayout swBeginNoti;
    @BindView(R.id.sw_less_noti_min) CommonInputView swLessNotiMin;
    @BindView(R.id.sw_less_noti) ExpandedLayout swLessNoti;
    @BindView(R.id.sw_new_noti) ExpandedLayout swNewNoti;
    @BindView(R.id.sw_order_noti) ExpandedLayout swOrderNoti;
    @BindView(R.id.sw_cancle_noti) ExpandedLayout swCancleNoti;
    @BindView(R.id.sw_seed_to_subtitute) ExpandedLayout swSeedToSubtitute;

    /**
     * 是否为私教
     */
    @Arg boolean mIsPrivate;
    @Inject MsgNotiPresenter mMsgNotiPresenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    private String mBeginNotiId;
    private String mLessNotiId;
    private String mCancelId;
    private String mNewNotiId;
    private String mOrderNotiId;
    private String mSubstitueNotiId;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_noti, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(mMsgNotiPresenter, this);
        mCallbackActivity.setToolbar("预约短信通知", false, null, R.menu.menu_save, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                showLoading();
                onComfirm();
                return true;
            }
        });
        initToolbar(toolbar);
        String courseTypeStr = getString(mIsPrivate ? R.string.course_type_private : R.string.course_type_group);

        swBeginNoti.setLabel(getString(R.string.msg_noti_before, courseTypeStr));
        swLessNoti.setLabel(getString(R.string.msg_less_student));
        swCancleNoti.setLabel(getString(R.string.msg_cancle_order_noti_student, courseTypeStr));
        swNewNoti.setLabel(getString(R.string.msg_new_noti_trainer));
        swOrderNoti.setLabel(getString(R.string.msg_help_order_noti_student, courseTypeStr));
        swSeedToSubtitute.setLabel(getString(R.string.msg_send_to_substitute));
        mMsgNotiPresenter.queryShopConfig(getConfigKeys());
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("预约短信通知");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                showLoading();
                onComfirm();
                return false;
            }
        });
    }

    void onComfirm() {
        List<ShopConfigBody.Config> data = new ArrayList<>();
        data.add(new ShopConfigBody.Config(mBeginNotiId, swBeginNotiMin.getContent()));
        //        data.add(new ShopConfigBody.Config(mIsPrivate ? ShopConfigs.PRIVATE_BEFORE_REMIND_USER : ShopConfigs.TEAM_BEFORE_REMIND_USER, swBeginNoti.isExpanded() ? "1" : "0"));
        data.add(new ShopConfigBody.Config(mLessNotiId, swLessNotiMin.getContent()));
        //        data.add(new ShopConfigBody.Config(mIsPrivate ? , swLessNoti.isExpanded() ? "1" : "0"));

        data.add(new ShopConfigBody.Config(mCancelId, swCancleNoti.isExpanded() ? "1" : "0"));
        // 有新预约时提醒教练
        data.add(new ShopConfigBody.Config(mNewNotiId, swNewNoti.isExpanded() ? "1" : "0"));
        data.add(new ShopConfigBody.Config(mOrderNotiId, swOrderNoti.isExpanded() ? "1" : "0"));
        //// TODO: 2017/1/18 候补发送
        //        data.add(new ShopConfigBody.Config(mIsPrivate?ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE:ShopConfigs.TEAM_SMS,swSeedToSubtitute.isExpanded()?"1":"0"));
        ShopConfigBody body = new ShopConfigBody(data);
        mMsgNotiPresenter.editShopConfig(body);
    }

    private String getConfigKeys() {
        return (mIsPrivate ? ShopConfigs.PRIVATE_BEFORE_REMIND_USER_MINUTES : ShopConfigs.TEAM_BEFORE_REMIND_USER_MINUTES).concat(",")
            .concat(mIsPrivate ? ShopConfigs.PRIVATE_BEFORE_REMIND_USER : ShopConfigs.TEAM_BEFORE_REMIND_USER)
            .concat(",")
            .concat(mIsPrivate ? ShopConfigs.PRIVATE_COURSE_REMIND_TEACHER_MINUTES : ShopConfigs.TEAM_COURSE_REMIND_TEACHER_MINUTES)
            .concat(",")
            .concat(
                mIsPrivate ? ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL : ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL)
            .concat(",")
            .concat(mIsPrivate ? ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE : ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE)
            .concat(",")
            .concat(
                mIsPrivate ? ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE : ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE);
    }

    @Override public String getFragmentName() {
        return MsgNotiFragment.class.getName();
    }

    @Override public void onShopConfigs(List<SignInConfig.Config> configs) {
        if (configs != null) {
            for (SignInConfig.Config c : configs) {
                switch (c.getKey()) {
                    //提醒会员
                    case ShopConfigs.PRIVATE_BEFORE_REMIND_USER_MINUTES:
                    case ShopConfigs.TEAM_BEFORE_REMIND_USER_MINUTES:
                        if (c.getValue() instanceof Double) {
                            swBeginNoti.setExpanded(c.getValueInt() > 0);
                            swBeginNotiMin.setContent(c.getValueInt() + "");
                            mBeginNotiId = c.getId() + "";
                        }
                        break;
                    //r人数不足提醒教练
                    case ShopConfigs.TEAM_COURSE_REMIND_TEACHER_MINUTES:
                    case ShopConfigs.PRIVATE_COURSE_REMIND_TEACHER_MINUTES:
                        if (c.getValue() instanceof Double) {
                            swLessNoti.setExpanded(c.getValueInt() > 0);
                            swLessNotiMin.setContent(c.getValueInt() + "");
                            mLessNotiId = c.getId() + "";
                        }
                        break;
                    case ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE:
                    case ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE:
                        if (c.getValue() instanceof Boolean) {
                            swNewNoti.setExpanded((Boolean) c.getValue());
                            mNewNotiId = c.getId() + " ";
                        }
                        break;
                    case ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL:
                    case ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL:
                        if (c.getValue() instanceof Boolean) {
                            swCancleNoti.setExpanded((Boolean) c.getValue());
                            mCancelId = c.getId() + "";
                        }
                        break;
                    case ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE:
                    case ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE:
                        if (c.getValue() instanceof Boolean) {
                            swOrderNoti.setExpanded((Boolean) c.getValue());
                            mOrderNotiId = c.getId() + " ";
                        }
                        break;
                }
            }
        }
    }

    @Override public void onEditOk() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
