package cn.qingchengfit.staffkit.views.course.limit;

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
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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

@FragmentWithArgs public class OrderLimitFragment extends BaseFragment implements OrderLimitPresenter.MVPView {

    @BindView(R.id.civ_order_course_time) CommonInputView civOrderCourseTime;
    @BindView(R.id.sw_order_course) ExpandedLayout swOrderCourse;
    @BindView(R.id.civ_cancel_time) CommonInputView civCancelTime;
    @BindView(R.id.sw_cancle) ExpandedLayout swCancle;
    @BindView(R.id.sw_substitute) ExpandedLayout swSubstitute;

    @Arg boolean mIsPrivate;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @Inject OrderLimitPresenter mOrderLimitPresenter;
    private String mOrderId;
    private String mCancleId;
    private String mMsgId;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_limit, container, false);
        ButterKnife.bind(this, view);
        delegatePresenter(mOrderLimitPresenter, this);
        mCallbackActivity.setToolbar(mIsPrivate ? "私教预约限制" : "团课预约限制", false, null, R.menu.menu_comfirm,
            new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    showLoading();
                    comfirmConfigs();
                    return true;
                }
            });
        mOrderLimitPresenter.queryCancelLimit(mIsPrivate);
        mOrderLimitPresenter.queryOrderLimit(mIsPrivate);
        mOrderLimitPresenter.querySubstituteLimit(mIsPrivate);
        initToolbar(toolbar);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(mIsPrivate ? "私教预约限制" : "团课预约限制");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                showLoading();
                comfirmConfigs();
                return true;
            }
        });
    }

    @Override public String getFragmentName() {
        return OrderLimitFragment.class.getName();
    }

    public void comfirmConfigs() {
        if (swOrderCourse.isExpanded() && civOrderCourseTime.isEmpty()) {
            ToastUtils.show("请填写课程预约限制时间");
            return;
        }
        if (swCancle.isExpanded() && civCancelTime.isEmpty()) {
            ToastUtils.show("请填写取消预约限制时间");
            return;
        }
        List<ShopConfigBody.Config> data = new ArrayList<>();
        data.add(new ShopConfigBody.Config(mOrderId, civOrderCourseTime.getContent()));
        data.add(new ShopConfigBody.Config(mCancleId, civCancelTime.getContent()));
        //        data.add(new ShopConfigBody.Config(ShopConfigs.TEAM_MINUTES_,swOrderCourse.isExpanded()?"1":"0"));
        mOrderLimitPresenter.saveConfigs(new ShopConfigBody(data));
        showLoading();
    }

    @Override public void onSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onCourseOrderLimit(SignInConfig.Config config) {
        if (config != null) {
            if (config.getValue() instanceof Double) {
                swOrderCourse.setExpanded(config.getValueInt() > 0);
                civOrderCourseTime.setContent(config.getValueInt() + "");
                mOrderId = config.getId() + "";
            }
        }
    }

    @Override public void onCourseCancelLimit(SignInConfig.Config config) {
        if (config != null) {
            if (config.getValue() instanceof Double) {
                swCancle.setExpanded(((Double) config.getValue()).intValue() > 0);
                civCancelTime.setContent(((Double) config.getValue()).intValue() + "");
                mCancleId = config.getId() + "";
            }
        }
    }

    @Override public void onCourseSubstituteLimit(SignInConfig.Config config) {

    }

    @Override public void onSaveOk() {

    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }
}
